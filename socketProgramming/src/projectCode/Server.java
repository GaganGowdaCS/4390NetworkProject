package projectCode;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;
    private static Map<String, Date> users = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Math server is running...");

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
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Get client's username during initial attachment
                //out.println("Enter your username:");
                username = in.readLine();
                users.put(username, new Date());
                out.println("Connection successful. Welcome, " + username + "!");

                String request;
                while ((request = in.readLine()) != null) {
                    System.out.println("Request from " + username + ": " + request);
                    // Process the request - Basic math calculations
                    String response = processRequest(request);
                    //out.println("Response from server: " + response);
                    out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Connection closed with client: " + username);
                users.remove(username);
            }
        }

        private String processRequest(String request) {
            // Perform basic math calculations
            // Addition, Subtraction, Multiplication, Division
        	// Split the request into operands and operator
            String[] parts = request.split("\\s+");
            if (parts.length != 3) {
                return "Invalid request format. Please use 'operand operator operand'.";
            }

            try {
                int operand1 = Integer.parseInt(parts[0]);
                String operator = parts[1];
                int operand2 = Integer.parseInt(parts[2]);

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
                        if (operand2 == 0) {
                            return "Cannot divide by zero.";
                        }
                        result = operand1 / operand2;
                        break;
                    default:
                        return "Invalid operator. Please use '+', '-', '*', or '/'.";
                }
                return String.valueOf(result);
            } catch (NumberFormatException e) {
                return "Invalid operands. Please use numbers for operands.";
            }
            //return "Processed: " + request;
        }
    }
}
