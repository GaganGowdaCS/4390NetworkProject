package projectCode;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	// Initialize variables
    private static final int PORT = 5000;
    private static Map<String, Date> users = new HashMap<>();

    public static void main(String[] args) {
        try {
        	// Create TCP server socket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Math server is running...");

            // Keep accepting new users while server is online
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                // Start a new thread to handle client's requests
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
    	// Initialize variables
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
            	// Initialize input and output
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Get username from client program
                username = in.readLine();
                // Add users to map
                users.put(username, new Date());
                // Confirm connection to specific user
                out.println("Connection successful. Welcome, " + username + "!");

                // Process request and send response
                String request;
                while ((request = in.readLine()) != null) {
                    System.out.println("Request from " + username + ": " + request);
                    // Process basic math calculation
                    String response = processRequest(request);
                    // Send response
                    out.println(response);
                }
            } catch (IOException e) {
            	// When client disconnects, remove them from active users
                System.out.println("Connection closed with client: " + username);
                users.remove(username);
            }
        }

        // Method for processing client request 
        private String processRequest(String request) {
            // Perform Addition, Subtraction, Multiplication, Division
        	// Split the request 
            String[] parts = request.split("\\s+");
            // User must enter 3 parts only
            if (parts.length != 3) {
                return "Invalid request format. Please use 'operand operator operand'.";
            }

            try {
            	// Initialize parts 1, 2, and 3
                int operand1 = Integer.parseInt(parts[0]);
                String operator = parts[1];
                int operand2 = Integer.parseInt(parts[2]);

                // Perform math based on operator
                int result;
                switch (operator) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                    	// Error if divide by Zero
                        if (operand2 == 0) {
                            return "Cannot divide by zero.";
                        }
                        result = operand1 / operand2;
                        break;
                    default:
                    	// If operator is invalid for the program
                        return "Invalid operator. Please use '+', '-', '*', or '/'.";
                }
                // Return result of computation
                return String.valueOf(result);
            } catch (NumberFormatException e) {
                return "Invalid operands. Please use numbers for operands.";
            }
        }
    }
}
