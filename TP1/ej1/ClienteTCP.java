import java.io.*;
import java.net.*;

public class ClienteTCP {
    public static void main(String[] args) throws IOException {
        Socket socketCliente = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try{

            // Se establece la conexión con el servidor
            socketCliente = new Socket("localhost", 4444);

            // Se crea un lector y un escritor para comunicarse con el servidor
            out = new PrintWriter(socketCliente.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            System.out.println("- Escriba mensaje para el servidor ('exit' para salir) -");

            // Se espera a que el usuario escriba un mensaje
            while (!(userInput = stdIn.readLine()).equals("exit")) {
                // Se envía el mensaje al servidor
                out.println(userInput);
                // Se espera la respuesta del servidor y se muestra por pantalla
                System.out.println("Respuesta del servidor: " + in.readLine());
            }

            // Le indico al server que quiero terminar la conexion
            out.println("exit");

            System.out.println("- Conexion finalizada -");

            // Se cierran los recursos
            out.close();
            in.close();
            stdIn.close();
            socketCliente.close();
        
        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }
}
