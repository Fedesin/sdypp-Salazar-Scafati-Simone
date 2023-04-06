import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteUDP {

    final static short MAX_BUFFER = 1024;
    public static void main(String[] args) throws Exception {

        try {

            // Instacion el socket al que se va a enviar el datagrama
            DatagramSocket clientSocket = new DatagramSocket();

            // Instancio direccion IP del servidor
            InetAddress serverAddress = InetAddress.getByName("localhost");

            // Instancio puerto del servidor
            int serverPort = 4444;

            System.out.println("- Escriba mensaje para el servidor ('exit' para salir) -");

            byte[] sendData;
            DatagramPacket sendPacket;
            DatagramPacket receivePacket;

            String userInput;

            while (!(userInput = new Scanner(System.in).nextLine()).equals("exit")) {
                
                // Almaceno en el array los datos ingresados por el cliente
                sendData = userInput.getBytes();

                // Instancio el datagrama con los datos y socket del servidor
                sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                
                // Envio el datagrama
                clientSocket.send(sendPacket);

                // Instancio el datagrama, le indico la estructura de datos para almacenar 
                // lo que responda el servidor y la longitud del buffer
                receivePacket = new DatagramPacket(new byte[MAX_BUFFER], MAX_BUFFER);
                
                // Recibo lo que respondio el servidor
                clientSocket.receive(receivePacket);

                String serverReply = new String(receivePacket.getData()).trim();

                System.out.println("Respuesta del servidor: " + serverReply);

            }

            byte[] lastData = "exit".getBytes();

            // Instancio un ultimo datagrama para indicarle al servidor que el cliente no va a enviar mas mensajes
            DatagramPacket lastPacket = new DatagramPacket(lastData, lastData.length, serverAddress, serverPort);
                
            // Envio el datagrama
            clientSocket.send(lastPacket);

            // Cierro el socket
            clientSocket.close();

        } catch(Exception error) {
            System.out.println(" Error: " + error.getMessage());
            System.exit(0);
        }
    }
}

