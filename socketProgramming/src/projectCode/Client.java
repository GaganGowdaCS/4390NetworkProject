package projectCode;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Client {
	// Initialize variables
    private static final String SERVER_IP = "127.0.0.1"; 
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
        	// Create TCP socket and input/output
            Socket socket = new Socket(SERVER_IP, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Prompt username from Client
            System.out.println("Enter your username:");
            String username = consoleInput.readLine();
            out.println(username);

            // Receive acknowledgement from server
            String serverResponse = in.readLine();
            System.out.println("Server response: " + serverResponse);

            // Begin prompting user for basic math equations
            String equation = "";
            while (!equation.equals("done")){

            	//Record user input and send to server 
            	System.out.println("Enter math equation: (num + num). To end, type 'done'");
                equation = consoleInput.readLine();
                
                // If user enters "done" then client program is stopped
                if (equation.equals("done")) {
                	break;
                }
                // Send user input to server
                out.println(equation);

                // Wait for server response
                serverResponse = in.readLine();
                System.out.println("Server response: " + serverResponse);

                // Add time delay between requests
                Thread.sleep(1000);
            }

            // Close connection request after 3 equations
            out.println("CLOSE_CONNECTION");
            System.out.println("Connection closed.");
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
