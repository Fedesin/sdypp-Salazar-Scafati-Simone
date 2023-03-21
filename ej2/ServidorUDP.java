import java.net.*;

public class ServidorUDP {
    private static final int PUERTO_SERVIDOR = 5000;
    private static final int TAM_BUFFER = 1024;

    public static void main(String[] args) throws Exception {
        DatagramSocket servidorSocket = new DatagramSocket(PUERTO_SERVIDOR);
        byte[] buffer;

        System.out.println("Servidor UDP escuchando en el puerto " + PUERTO_SERVIDOR + "...");

        while (true) {
            buffer = new byte[TAM_BUFFER];
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
            servidorSocket.receive(paquete);

            // Convertir los bytes recibidos en un string
            String mensaje = new String(paquete.getData()).trim();

            // Mostrar el mensaje recibido
            System.out.println("Mensaje recibido: " + mensaje);

            // Preparar la respuesta
            InetAddress direccionCliente = paquete.getAddress();
            int puertoCliente = paquete.getPort();
            buffer = mensaje.getBytes();
            paquete = new DatagramPacket(buffer, buffer.length, direccionCliente, puertoCliente);

            // Enviar la respuesta al cliente
            servidorSocket.send(paquete);
        }
    }
}
