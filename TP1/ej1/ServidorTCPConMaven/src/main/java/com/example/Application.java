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

public class Application {
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
        System.out.println("- Cliente conectado: " + socketCliente);

        // Se crea un lector y un escritor para comunicarse con el cliente
        out = new PrintWriter(socketCliente.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));

        String inputLine, outputLine;
        // Se espera a recibir un mensaje del cliente
        while (!(inputLine = in.readLine()).equals("exit")) {
            System.out.println("Mensaje recibido del cliente: " + inputLine);
            // Se responde al cliente con el mismo mensaje
            out.println(inputLine);
        }

        System.out.println("- Conexion finalizada con cliente: " + socketCliente);

        // Se cierran los recursos
        out.close();
        in.close();
        socketCliente.close();
        servidorSocket.close();
    }
}

