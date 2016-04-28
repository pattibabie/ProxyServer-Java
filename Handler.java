import java.net.*;
import java.io.*;

/**
 * Created by Patricia Bailey on 2/28/16.
 *
 * This class performs the logic for executing an HTTP request.
 */
public class Handler{

    public static final int PORT = 80;
    public static final int BUFFER = 256;

    /**
     * Method that receives a request for resources from a client, sends an HTTP request
     * to a host server, and returning the response to the client, communicating with
     * Socket connections.
     *
     * @param client
     */
    public void handleRequest(Socket client) throws IOException{

        //Variables
        BufferedReader fromClient = null;
        DataOutputStream toOriginHost = null;
        InputStream hostInStream;
        OutputStream clientOutStream;
        Socket hostSocket;

        String clientRequest;
        String[] tempString;
        String clientString;
        String hostString;
        String resource = " ";

        String httpRequestLine1;
        String httpRequestLine2;
        String httpRequestLine3;
        String httpRequest;

        byte[] buffer;
        int numOfBytes;
        InetAddress host;

        try {
            //read from socket
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            clientRequest  = fromClient.readLine();

            //Parse client request
            tempString = clientRequest.split(" ", 3);
            clientString = tempString[1];
            tempString = clientString.split("/",3);
            hostString = tempString[1];
            if(tempString.length == 3)
                resource = tempString[2];

            //Piece the HTTP request together
            httpRequestLine1 = "GET /" + resource + " HTTP/1.1\r\n";
            httpRequestLine2 = "Host: "+ hostString +"\r\n";
            httpRequestLine3 = "Connection: close\r\n\r\n";
            httpRequest = httpRequestLine1 + httpRequestLine2 + httpRequestLine3;

            //Get IP address to open socket to host
            host = InetAddress.getByName(hostString);

            //Open socket to host with host IP and port
            hostSocket = new Socket(host, PORT);

            //Open stream to the host
            toOriginHost = new DataOutputStream(hostSocket.getOutputStream());

            //Write httpRequest to socket as a sequence of bytes
            toOriginHost.writeBytes(httpRequest);

            //Open input and output streams
            hostInStream = hostSocket.getInputStream();
            clientOutStream = client.getOutputStream();

            //Return resource to client
            buffer = new byte[BUFFER];
            while ((numOfBytes = hostInStream.read(buffer)) != -1)
                clientOutStream.write(buffer, 0, numOfBytes);

            // close the output stream
            toOriginHost.close();

        } catch(UnknownHostException uhe){
            System.err.println(uhe);
            System.exit(0);
        } catch(IOException io){
            System.err.println(io);
            System.exit(0);
        }
        finally {
            /**
             * Close streams and socket
             */
            if (fromClient != null)
                fromClient.close();
            if (toOriginHost != null)
                toOriginHost.close();
        }
    }
}
