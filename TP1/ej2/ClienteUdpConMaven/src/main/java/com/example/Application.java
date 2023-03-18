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
