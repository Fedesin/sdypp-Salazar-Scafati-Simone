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

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Application {

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
