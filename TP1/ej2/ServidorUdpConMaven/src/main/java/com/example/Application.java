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
import java.util.*;

public class Application {

    final static short MAX_BUFFER = 1024;

    public static void main(String[] args) throws Exception {
        
        // Instancio el socket del server que va a estar 'en escucha' a la espera de clientes
        DatagramSocket serverSocket = new DatagramSocket(4444);

        System.out.println("Servidor escuchando en el puerto 4444...");

        while (true) {

            // Instancio el datagrama, le indico la estructura de datos que va a recibir lo que envie el 
            // cliente y la longitud del buffer de entrada
            DatagramPacket receivePacket = new DatagramPacket(new byte[MAX_BUFFER], MAX_BUFFER);

            // Acepto el nuevo datagrama enviado 
            serverSocket.receive(receivePacket);

            // Instancio un nuevo hilo para atender al cliente
            Thread thread = new Thread(new ClientHandler(serverSocket, receivePacket));

            // Inicio el hilo
            thread.start();
        }
    }
}

class ClientHandler implements Runnable {
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;

    public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    public void run() {

        try {
            
            // Obtengo la direccion IP origen
            InetAddress clientAddress = receivePacket.getAddress();

            // Obtengo el puerto origen
            int clientPort = receivePacket.getPort();

            // Obtengo los datos enviados por el cliente en el datagrama
            String sentence = new String(receivePacket.getData()).trim();

            // Mientras el cliente no envie 'exit' sigo esperando mensajes
            do {

                System.out.println("Se recibio de: " + clientAddress + ":" + clientPort + " - " + sentence);

                // Almaceno los datos enviados por el cliente en un array de bytes
                byte[] sendData = sentence.getBytes();

                // Instancio un nuevo datagrama para reenviar los datos al cliente
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                
                // Envio el datagrama al socket del cliente
                serverSocket.send(sendPacket);

                // Instancio un nuevo datagrama 
                DatagramPacket receivePacket = new DatagramPacket(new byte[Application.MAX_BUFFER], Application.MAX_BUFFER);

                // Acepto el nuevo datagrama enviado 
                serverSocket.receive(receivePacket);          

                sentence = new String(receivePacket.getData()).trim();

            } while(!sentence.equals("exit"));

            System.out.println("- No se espera recibir mas mensajes del cliente: " + clientAddress + ":" + clientPort);

        } catch (Exception error) {
            System.out.println(" Error: " + error.getMessage());
            System.exit(0);
        }
    }
}
