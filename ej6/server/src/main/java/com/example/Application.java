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
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Application {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/sumar", new SumarHandler());
        server.createContext("/restar", new RestarHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado en el puerto 8000.");
    }

    static class SumarHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String vector1Param = exchange.getRequestURI().getQuery().split("&")[0];
                String vector2Param = exchange.getRequestURI().getQuery().split("&")[1];
                String vectoraux = vector1Param.split("=")[1];
                String[] vector1 =  vectoraux.split(",");
                String vectoraux2 = vector2Param.split("=")[1];
                String[] vector2 =  vectoraux2.split(",");
                int[] resultado = new int[vector1.length];
                for (int i = 0; i < vector1.length; i++) {
                    resultado[i] = Integer.parseInt(vector1[i]) + Integer.parseInt(vector2[i]);
                }
                // Introducimos el error intencional
                resultado[0]=100;
                String response = "Resultado de la suma: " + vectorToString(resultado);
                response += " - V1: ["+ vectoraux +"]";
                response += " - V2: ["+ vectoraux2+"]";;
            
                 exchange.sendResponseHeaders(200, response.length());
                 OutputStream outputStream = exchange.getResponseBody();
                 outputStream.write(response.getBytes());
                outputStream.close();
            }
        }


   

private String vectorToString(int[] vector) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < vector.length; i++) {
        stringBuilder.append(vector[i]);
        if (i != vector.length - 1) {
            stringBuilder.append(",");
        }
    }
    return stringBuilder.toString();
}
}

static class RestarHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String vector1Param = exchange.getRequestURI().getQuery().split("&")[0];
            String vector2Param = exchange.getRequestURI().getQuery().split("&")[1];
            String vectoraux = vector1Param.split("=")[1];
            String[] vector1 =  vectoraux.split(",");
            String vectoraux2 = vector2Param.split("=")[1];
            String[] vector2 =  vectoraux2.split(",");
            int[] resultado = new int[vector1.length];
            for (int i = 0; i < vector1.length; i++) {
                resultado[i] = Integer.parseInt(vector1[i]) - Integer.parseInt(vector2[i]);
            }
            // Introducimos el error intencional
            resultado[0]=100;
            String response = "Resultado de la suma: " + vectorToString(resultado);
            response += " - V1: ["+ vectoraux +"]";
            response += " - V2: ["+ vectoraux2+"]";;
        
             exchange.sendResponseHeaders(200, response.length());
             OutputStream outputStream = exchange.getResponseBody();
             outputStream.write(response.getBytes());
            outputStream.close();
        }
    }




private String vectorToString(int[] vector) {
StringBuilder stringBuilder = new StringBuilder();
for (int i = 0; i < vector.length; i++) {
    stringBuilder.append(vector[i]);
    if (i != vector.length - 1) {
        stringBuilder.append(",");
    }
}
return stringBuilder.toString();
}
}
}
