import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

    public void writeMessage(BufferedReader serverReader, PrintWriter serverWriter, BufferedReader reader) {

        System.out.println(" Direccion IP cliente : ");
        String destinyAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Puerto cliente : ");
        Integer destinyPort = new Scanner(System.in).nextInt();

        try {

            serverWriter.println(destinyAddress + ":" + destinyPort);

            System.out.print(" Mensaje para : " + destinyAddress + ":" + destinyPort + " ('exit' para salir) \n");

            do {

                String message = reader.readLine();
        
                if(message == null || message.equals("exit")) {
                    serverWriter.println("exit");
                    break;
                }
                                    
                serverWriter.println(message);
            
            } while(true);

            System.out.print(" - Chat finalizado - \n");

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }

    public void readPendingMessages(BufferedReader serverReader, PrintWriter serverWriter) {

        try {
  
            do {

                String pendingMessages = serverReader.readLine();

                if(pendingMessages.isEmpty()) {
                    System.out.println("\n - No se encontraron mensajes - ");
                    break;
                }

                String[] messageArray = pendingMessages.split("/");

                for (int index = 0; index < messageArray.length; index++) {
                    System.out.println(messageArray[index]);
                }
                
                System.out.println("\n Desea borrar un mensaje? (yes/no) ");

                String operationToDo = new Scanner(System.in).nextLine();
                    
                while((!operationToDo.equals("yes")) && (!operationToDo.equals("no"))) {
                    operationToDo = new Scanner(System.in).nextLine();
                }
                
                if(operationToDo.equals("no")) {
                    serverWriter.println("-1");
                    break;
                }

                System.out.println(" Elija un mensaje entre 1 y " + messageArray.length);

                Integer messageNumber = new Scanner(System.in).nextInt();
                    
                while((messageNumber < 1) || (messageArray.length < messageNumber)) {
                    messageNumber = new Scanner(System.in).nextInt();
                }

                serverWriter.println(messageNumber.toString());

                if(messageArray.length == 1) {
                    System.out.println("\n - No messages found - ");
                    break;
                }
                
            } while(true);

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    } 

    public static void main(String[] args) throws Exception {

        // For calling no static methods
        TCPClient tcpClient = new TCPClient();

        System.out.println(" Direccion IP cliente : ");
        String sourceAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Puerto cliente : ");
        Integer sourcePort = new Scanner(System.in).nextInt();

        System.out.println(" Direccion IP servidor : ");
        String serverAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Puerto servidor : ");
        Integer serverPort = new Scanner(System.in).nextInt(); 

        try {

            // Socket en el cual va a estar el servidor "en escucha".
            Socket serverSocket = new Socket(InetAddress.getByName(serverAddress), serverPort, InetAddress.getByName(sourceAddress), sourcePort);

            // Si el socket del servidor es inexistente.
            if(!serverSocket.isConnected()) {
                System.out.println(" Servidor no encontrado! ");
                System.exit(0); 
            }

            System.out.println(" Conexion aceptada! ");
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            
            PrintWriter serverWriter = new PrintWriter(serverSocket.getOutputStream(), true);
            
            do {

                System.out.println("\n Desea escribir(wriite), leer(read) o salir(exit)? (write/read/exit) \n");

                String operationToDo = new Scanner(System.in).nextLine();
                
                while((!operationToDo.equals("write")) && (!operationToDo.equals("read")) && (!operationToDo.equals("exit"))) {
                    operationToDo = new Scanner(System.in).nextLine();
                }
    
                if(operationToDo.equals("write")) {
                    
                    serverWriter.println("write");
        
                    tcpClient.writeMessage(serverReader, serverWriter, reader);    
                } 
                
                if(operationToDo.equals("read")) {
    
                    serverWriter.println("read");
    
                    tcpClient.readPendingMessages(serverReader, serverWriter);
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