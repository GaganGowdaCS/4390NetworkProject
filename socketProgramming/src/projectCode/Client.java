package projectCode;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Client {
    private static final String SERVER_IP = "127.0.0.1"; // Change this to server IP address
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Initial attachment - Providing username
            System.out.println("Enter your username:");
            String username = consoleInput.readLine();
            out.println(username);

            // Wait for server acknowledgement
            String serverResponse = in.readLine();
            System.out.println("Server response: " + serverResponse);

            //Random random = new Random();
            
            //Prompt the user 3 times
            for (int i = 0; i < 3; i++) {
                // Send random math calculation requests
                //int num1 = random.nextInt(10);
                //int num2 = random.nextInt(10);
                //String request = num1 + " + " + num2; // Example request: Addition
                //out.println(request);
            	
            	//Record user input and send to server application
            	System.out.println("Enter math equation: (num + num)");
                String equation = consoleInput.readLine();
                out.println(equation);

                // Wait for server response
                serverResponse = in.readLine();
                System.out.println("Server response: " + serverResponse);

                // Simulate random time delay between requests
                Thread.sleep(1000);
            }

            // Close connection request
            out.println("CLOSE_CONNECTION");
            System.out.println("Connection closed.");
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
