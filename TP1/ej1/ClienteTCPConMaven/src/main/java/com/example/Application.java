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
