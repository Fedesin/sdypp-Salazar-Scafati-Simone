import java.net.*;
import java.io.*;
import java.util.*;

public class MultiuserQueueTCPServer {
    public static void main(String[] args) {
        
        System.out.println(" Server address : ");
        String serverAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Server port : ");
        Integer serverPort = new Scanner(System.in).nextInt(); 

        System.out.println(" Number of concurrent costumers : ");
        Integer serverBacklog = new Scanner(System.in).nextInt(); 

        try {
            
            // Iniciamos el server en el socket indicado.
            ServerSocket serverSocket = new ServerSocket(serverPort, serverBacklog, InetAddress.getByName(serverAddress));
            
            System.out.println(" Server started! ");

            // HashMap donde se tiene por valor de "key" el socket destinatario 
            // y de "value" una cola de mensajes asociados.
            HashMap<String, List<String>> messageHashMap = new HashMap<>();

            do {

                // Acepto la conexion del nuevo cliente.
                Socket clientSocket = serverSocket.accept();
                
                System.out.println(" Client connected from : " + clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getPort());

                ClientHandler clientHandler = new ClientHandler(clientSocket, serverSocket, messageHashMap);
                
                // Creo un nuevo hilo para el cliente.
                clientHandler.start();

            } while(true);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private HashMap<String, List<String>> messageHashMap;

    public ClientHandler(Socket clientSocket, ServerSocket serverSocket, HashMap<String, List<String>> messageHashMap) {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;
        this.messageHashMap = messageHashMap;
    }

    public void writeMessages(BufferedReader clientReader, PrintWriter clientWriter) {
        List<String> messageList;

        try {

            // Recibimos el socket del cliente destino del mensajes.
            String destinyClient = clientReader.readLine();

            do {

                // Recibimos el mensaje que envio el cliente.
                String clientInput = clientReader.readLine();
    
                // Validamos que el cliente no quiera termina de enviar mensajes.
                if(clientInput == null || clientInput.equals("exit")) {
                    break;
                }
    
                // Agregamos el mensaje a la lista de "espera" del destinatario asociado.
                if(!messageHashMap.containsKey(destinyClient)) {
                    messageList = new ArrayList<>();
                } else {
                    messageList = messageHashMap.get(destinyClient);
                }

                messageList.add(" <" + clientSocket.getLocalAddress().getHostAddress() + ":" +clientSocket.getPort() +"> " + clientInput);
                messageHashMap.put(destinyClient, messageList);
                
            } while(true);

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }

    public void readPendingMessages(PrintWriter clientWriter, BufferedReader clientReader, List<String> messageList) {

        try {

            do {

                String pendingMessages = "";

                for (String message : messageList) {
                    pendingMessages += " " + (messageList.indexOf(message) + 1) + "-" + message + "/";
                }

                clientWriter.println(pendingMessages);

                String messageToDelete = clientReader.readLine();

                if(messageToDelete.equals("-1")) {
                    break;
                }
                
                messageList.remove(Integer.parseInt(messageToDelete) - 1);

            } while(true);

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }

    public void run() {

        try {

            // Permite obtener del canal los datos que se envien por parte del cliente.
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Permite enviar los datos por el canal los datos que se envien al cliente por parte del server.
            PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            
            do {

                // Recibo la operacion que se va a realizar
                String operationToDo = clientReader.readLine();

                if(operationToDo.equals("write")) {

                    writeMessages(clientReader, clientWriter);

                } 
                
                if(operationToDo.equals("read")) {

                    String clientHashKey = clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getPort();
                    if(messageHashMap.containsKey(clientHashKey)) {
                        readPendingMessages(clientWriter, clientReader, messageHashMap.get(clientHashKey));
                    } else
                        clientWriter.println("");
                    
                }

                if(operationToDo.equals("exit")) {
                    System.out.println(" - Connection finished - ");
                    break;
                }

            } while(true);

            clientSocket.close();

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }  
    }
}