import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket servidorSocket = null;
        Socket socketCliente = null;
        PrintWriter out = null;
        BufferedReader in = null;

        // Se crea el servidor socket
        servidorSocket = new ServerSocket(4444);
        System.out.println("Servidor escuchando en el puerto 4444...");

        // Se espera la conexión del cliente
        socketCliente = servidorSocket.accept();
        System.out.println("Cliente conectado: " + socketCliente);

        // Se crea un lector y un escritor para comunicarse con el cliente
        out = new PrintWriter(socketCliente.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));

        String inputLine, outputLine;
        // Se espera a recibir un mensaje del cliente
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Mensaje recibido del cliente: " + inputLine);
            // Se responde al cliente con el mismo mensaje
            out.println(inputLine);
        }

        // Se cierran los recursos
        out.close();
        in.close();
        socketCliente.close();
        servidorSocket.close();
    }
}
