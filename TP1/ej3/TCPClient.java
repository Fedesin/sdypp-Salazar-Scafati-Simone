import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

    public void writeMessage(BufferedReader serverReader, PrintWriter serverWriter, BufferedReader reader) {

        System.out.println(" Destiny client address : ");
        String destinyAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Destiny client port : ");
        Integer destinyPort = new Scanner(System.in).nextInt();

        try {

            serverWriter.println(destinyAddress + ":" + destinyPort);

            System.out.print(" Message to : " + destinyAddress + ":" + destinyPort + "\n");

            do {

                System.out.print(" Enter message : ");
                String message = reader.readLine();
        
                if(message == null || message.equals("exit")) {
                    serverWriter.println("exit");
                    break;
                }
                                    
                serverWriter.println(message);
            
            } while(true);

            System.out.print(" - Finished chat - \n");

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }

    public void readPendingMessages(BufferedReader serverReader) {

        try {
  
            String pendingMessages = serverReader.readLine();

            String[] array = pendingMessages.split("/");

            for (int index = 0; index < array.length; index++) {
                System.out.println(array[index]);
            }
            
        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    } 

    public static void main(String[] args) throws Exception {

        // For calling no static methods
        TCPClient tcpClient = new TCPClient();

        System.out.println(" Source client address : ");
        String sourceAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Source client port : ");
        Integer sourcePort = new Scanner(System.in).nextInt();

        System.out.println(" Server address : ");
        String serverAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Server port : ");
        Integer serverPort = new Scanner(System.in).nextInt(); 

        try {

            // Socket en el cual va a estar el servidor "en escucha".
            Socket serverSocket = new Socket(InetAddress.getByName(serverAddress), serverPort, InetAddress.getByName(sourceAddress), sourcePort);

            // Si el socket del servidor es inexistente.
            if(!serverSocket.isConnected()) {
                System.out.println(" Unreacheable server! ");
                System.exit(0); // VER
            }

            System.out.println(" Connection accepted! ");
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            
            PrintWriter serverWriter = new PrintWriter(serverSocket.getOutputStream(), true);
            
            do {

                System.out.println("\n Want to write a message, read pending ones or exit? (write/read/exit) \n");

                String operationToDo = new Scanner(System.in).nextLine();
                
                while((!operationToDo.equals("write")) && (!operationToDo.equals("read")) && (!operationToDo.equals("exit"))) {
                    operationToDo = new Scanner(System.in).nextLine();
                }
    
                if(operationToDo.equals("write")) {
                    
                    serverWriter.println("write");
        
                    do {
    
                        tcpClient.writeMessage(serverReader, serverWriter, reader);
    
                        System.out.println(" Want to write messages to another client? (yes/no) \n");
                        
                        operationToDo = new Scanner(System.in).nextLine();
                        
                        while((!operationToDo.equals("yes")) && (!operationToDo.equals("no"))) {
                            operationToDo = new Scanner(System.in).nextLine();
                        }
    
                        if(operationToDo.equals("no")) {
                            break;
                        } else {
                            serverWriter.println("write");
                        }
    
                    } while(true);
    
                } 
                
                if(operationToDo.equals("read")) {
    
                    serverWriter.println("read");
    
                    tcpClient.readPendingMessages(serverReader);
                }
    
                if(operationToDo.equals("exit")) {
    
                    serverWriter.println("exit");
                    break;
    
                }

            } while(true);
    
            serverSocket.close();

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }
}