import java.net.*;
import java.io.*;
/**
 * Created by Patricia Bailey on 2/28/16.
 *
 * This class connects clients running on separate threads to the Handler class.
 */

public class Connect implements Runnable {

    private Socket client;
    private static Handler handle = new Handler();

    /**
     * Constructor for the Connect class.
     *
     * @param client
     */
    public Connect(Socket client) {
        this.client = client;
    }

    /**
     * Method that connects a client through a Socket connection to the Handler class.
     */
    public void run(){
        try {
            handle.handleRequest(client);
        }
        catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(0);
        }
    }
}
