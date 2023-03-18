/*-
 * =====LICENSE-START=====
 * Java 11 Application
 * ------
 * Copyright (C) 2020 - 2023 Organization Name
 * ------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====LICENSE-END=====
 */

package com.example;

import java.net.*;

public class Application {
    private static final int PUERTO_SERVIDOR = 5001;
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
