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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controlador{

    @GetMapping("/saludo")
    public String saludar() {
        return "Hola desde mi servidor web Spring!";
    }

    //ahora la suma se realiza recibiendo un json en el cuerpo
    @PostMapping("/suma")
    public String obtenerSuma(@RequestBody  String payload) throws IOException {
        bashCommand command = new bashCommand("task");
        command.dockerRun();
        //enviar por parametros la tarea ejecutarTarea(suma.getNum1(),suma.getNum2());
    //   command.dockerStop();
    return "";
    }    

    public static class bashCommand {
    private String dockerImage;    
    
    bashCommand(String dockerImage){
        this.dockerImage = dockerImage;
    }

    public void dockerRun() throws IOException {
        int hostPort = this.findAvailablePort();
        String dockerCommand = "docker run --rm -p "+ hostPort +":8080 " + this.dockerImage;
        this.runCommand(dockerCommand);
      
    }

    public void dockerStop() throws IOException {
        String dockerCommand = "docker stop " + this.dockerImage;
        this.runCommand(dockerCommand);
    }


    private void runCommand(String dockerCommand) throws IOException {
    // Start the Docker process
    ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", dockerCommand);
    Process process = pb.start();

    // Read the output of the Docker process
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }

    // Wait for the Docker process to finish
    try {
      int exitCode = process.waitFor();
      System.out.println("Exited with error code " + exitCode);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    }

    private static int findAvailablePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      int port = socket.getLocalPort();
      System.out.println("Using port " + port);
      return port;
    }
    }
    }
}
