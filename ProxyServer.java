import java.net.*;
import java.io.*;
import java.util.concurrent.*;

/**
 * Created by Patricia Bailey on 2/28/16.
 *
 * This class is the client-side of the Multithreaded Name Server.
 *
 * This server will operate as follows:
 *
 * It will listen to port 8080 waiting for client connections. When a connection is made,
 * it will service the client in a separate thread, where the HTTP request from the client
 * will be sent to a host server and a response sent back to the client. The actual server
 * will resume listening for additional client connections.
 *
 * The server thread will initially wait for the client to respond.
 *
 * Run the ProxyServer and then input something like:
 *
 * http://localhost:8080/www.people.westminstercollege.edu/faculty/ggagne/spring2016/352/handouts/hw3/index.html
 * or
 * http://localhost:8080/www.amazon.com
 *
 * into the web browser address window.
 */

public class  ProxyServer
{
    public static final int DEFAULT_PORT = 8080;

    //thread pool for concurrency
    private static final Executor exec = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        ServerSocket sock = null;

        try {
            // establish the socket
            sock = new ServerSocket(DEFAULT_PORT);

            while (true) {
                //listen for connections
                //service the connection in a separate thread.
                Runnable task = new Connect(sock.accept());
                exec.execute(task);
            }
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
        finally {
            if (sock != null)
                sock.close();
        }
    }
}

