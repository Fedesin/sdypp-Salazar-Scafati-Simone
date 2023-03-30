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

    public void writeMessage(BufferedReader serverReader, PrintWriter serverWriter, BufferedReader reader) {

        System.out.println(" Destiny client address : ");
        String destinyAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Destiny client port : ");
        Integer destinyPort = new Scanner(System.in).nextInt();

        try {

            serverWriter.println(destinyAddress + ":" + destinyPort);

            System.out.print(" Message to : " + destinyAddress + ":" + destinyPort + " (exit to leave) \n");

            do {

                System.out.print(" Enter message : ");
                String message = reader.readLine();
        
                if(message == null || message.equals("exit")) {
                    serverWriter.println("exit");
                    break;
                }
                                    
                serverWriter.println(message);
            
            } while(true);

            System.out.print(" - Finished chat - \n");

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }

    public void readPendingMessages(BufferedReader serverReader, PrintWriter serverWriter) {

        try {
  
            do {

                String pendingMessages = serverReader.readLine();

                if(pendingMessages.isEmpty()) {
                    System.out.println("\n - No messages found - ");
                    break;
                }

                String[] messageArray = pendingMessages.split("/");

                for (int index = 0; index < messageArray.length; index++) {
                    System.out.println(messageArray[index]);
                }
                
                System.out.println("\n Want to delete a message? (yes/no) ");

                String operationToDo = new Scanner(System.in).nextLine();
                    
                while((!operationToDo.equals("yes")) && (!operationToDo.equals("no"))) {
                    operationToDo = new Scanner(System.in).nextLine();
                }
                
                if(operationToDo.equals("no")) {
                    serverWriter.println("-1");
                    break;
                }

                System.out.println(" Choose an message between 1 an " + messageArray.length);

                Integer messageNumber = new Scanner(System.in).nextInt();
                    
                while((messageNumber < 1) || (messageArray.length < messageNumber)) {
                    messageNumber = new Scanner(System.in).nextInt();
                }

                serverWriter.println(messageNumber.toString());

                if(messageArray.length == 1) {
                    System.out.println("\n - No messages found - ");
                    break;
                }
                
            } while(true);

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    } 

    public static void main(String[] args) throws Exception {

        // For calling no static methods
        Application tcpClient = new Application();

        System.out.println(" Source client address : ");
        String sourceAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Source client port : ");
        Integer sourcePort = new Scanner(System.in).nextInt();

        System.out.println(" Server address : ");
        String serverAddress = new Scanner(System.in).nextLine();
        
        System.out.println(" Server port : ");
        Integer serverPort = new Scanner(System.in).nextInt(); 

        try {

            // Socket en el cual va a estar el servidor "en escucha".
            Socket serverSocket = new Socket(InetAddress.getByName(serverAddress), serverPort, InetAddress.getByName(sourceAddress), sourcePort);

            // Si el socket del servidor es inexistente.
            if(!serverSocket.isConnected()) {
                System.out.println(" Unreacheable server! ");
                System.exit(0); // VER
            }

            System.out.println(" Connection accepted! ");
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            
            PrintWriter serverWriter = new PrintWriter(serverSocket.getOutputStream(), true);
            
            do {

                System.out.println("\n Want to write a message, read pending ones or exit? (write/read/exit) \n");

                String operationToDo = new Scanner(System.in).nextLine();
                
                while((!operationToDo.equals("write")) && (!operationToDo.equals("read")) && (!operationToDo.equals("exit"))) {
                    operationToDo = new Scanner(System.in).nextLine();
                }
    
                if(operationToDo.equals("write")) {
                    
                    serverWriter.println("write");
        
                    tcpClient.writeMessage(serverReader, serverWriter, reader);    
                } 
                
                if(operationToDo.equals("read")) {
    
                    serverWriter.println("read");
    
                    tcpClient.readPendingMessages(serverReader, serverWriter);
                }
    
                if(operationToDo.equals("exit")) {
    
                    serverWriter.println("exit");

                    break;
                }

            } while(true);
    
            serverSocket.close();

        } catch(IOException e) {
            System.out.println(" Error: " + e.getMessage());
            System.exit(0);
        }
    }
}
