import java.net.*;

public class ClienteUDP {
    private static final int PUERTO_SERVIDOR = 5000;
    private static final int TAM_BUFFER = 1024;

    public static void main(String[] args) throws Exception {
        DatagramSocket clienteSocket = new DatagramSocket();
        InetAddress direccionServidor = InetAddress.getByName("localhost");

        byte[] buffer;
        String mensaje = "";

        while (!mensaje.equals("salir")) {
            // Leer la entrada del usuario
            byte[] datosEntrada = new byte[TAM_BUFFER];
            System.in.read(datosEntrada);
            mensaje = new String(datosEntrada).trim();

            // Enviar el mensaje al servidor
            buffer = mensaje.getBytes();
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);
            clienteSocket.send(paquete);

            // Recibir la respuesta del servidor
            buffer = new byte[TAM_BUFFER];
            paquete = new DatagramPacket(buffer, buffer.length);
            clienteSocket.receive(paquete);
            mensaje = new String(paquete.getData()).trim();

            // Mostrar la respuesta del servidor
            System.out.println("Respuesta del servidor: " + mensaje);
        }

        clienteSocket.close();
    }
}
