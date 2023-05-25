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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.NoHttpResponseException;
import com.github.dockerjava.api.DockerClient;

@RestController
public class Controlador{


    //ahora la suma se realiza recibiendo un json en el cuerpo
    @PostMapping("/filtro_sobel")
    public String obtenerSuma(@RequestBody  String payload) throws IOException,InterruptedException,Exception {
        bashCommand command = new bashCommand("fedesin31/worker_sobel");
        command.dockerRun();
        String message = command.genericTask(payload);
        command.dockerStop();
        return message;
    }    

    public static class bashCommand {
    private String dockerImage;
    private int hostPort;    
    
    bashCommand(String dockerImage){
        this.dockerImage = dockerImage;
    }


    public void dockerRun() throws IOException,InterruptedException {
        this.hostPort = this.findAvailablePort();
        //este comando nos va a servir cuando el servidor este dockerizado, realice la ejecución de manera local
        //String dockerCommand = "docker run --rm -p -v "+ this.hostPort +":8080 "+"/sdypp/sdypp-Salazar-Scafati-Simone/TP1/ej7/task/"+this.dockerImage+":/app " + this.dockerImage;
        //String dockerCommand = "docker run --rm -p -v "+ this.hostPort +":8080 " + "~/sdypp/sdypp-Salazar-Scafati-Simone/TP1/ej7/task/"+this.dockerImage+":/app/task task";
        //Este comando nos sirve cuando ejecutamos el servidor NO dockerizado
        String dockerCommand = "docker run --rm -p "+ this.hostPort +":8080 " + this.dockerImage;
        this.runCommand(dockerCommand);
        this.waitForContainer(this.hostPort);
      
    }

    public void dockerStop() throws IOException {
        String dockerCommand = "docker stop $(docker ps -q -f 'publish'="+this.hostPort+")";
        this.runCommand(dockerCommand);
    }


    private void runCommand(String dockerCommand) throws IOException {
    System.out.println(dockerCommand);
    ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", dockerCommand);
    Process process = pb.start();
    }

    public void waitForContainer(int port) throws IOException, InterruptedException {
        String healthcheckCommand = "while ! nc -z host.docker.internal  " + port + "; do sleep 1; done";
        Process process = Runtime.getRuntime().exec(new String[] {"/bin/bash", "-c", healthcheckCommand});
        process.waitFor();
    }

    private static int findAvailablePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      int port = socket.getLocalPort();
      return port;
    }}


    public String genericTaskGET(String requestBody) throws Exception, InterruptedException {
        // Espera a que el enpoint este listo para hacer peticiones
        waitForEndpoint("http://host.docker.internal:"+this.hostPort+"/statusSaludo");
    
        // Crear cliente HTTP
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // Definir URL y cuerpo de la petición
            HttpGet httpGet = new HttpGet("http://host.docker.internal:"+this.hostPort+"/saludo");
    
            // Establecer el tipo de contenido en la solicitud
            httpGet.setHeader("Content-Type", "application/json");
    
            // Ejecutar petición
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                // Procesar respuesta
                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);
                System.out.println("Response: "+responseBody);
                return responseBody;
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }


    public String genericTask(String requestBody) throws Exception,InterruptedException {
        //Espera a que el enpoint este listo para hacer peticiones
        waitForEndpoint("http://host.docker.internal:"+this.hostPort+"/status");
        // Crear cliente HTTP
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // Definir URL y cuerpo de la petición
            HttpPost httpPost = new HttpPost("http://host.docker.internal:"+this.hostPort+"/suma");
            httpPost.setHeader("Content-Type", "application/json");
            System.out.println("Request: "+requestBody);
            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // Ejecutar petición
            CloseableHttpResponse response = httpClient.execute(httpPost);

            try {
                // Procesar respuesta
                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);
                System.out.println("Response: "+responseBody);
                return responseBody;
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
      }

    public void waitForEndpoint(String url) throws InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(1))
            .GET()
            .build();
    boolean endpointAvailable = false;
    System.out.print("Contenedor tarea generica cargando:");
    while (!endpointAvailable) {
        try {
            System.out.print(".");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                endpointAvailable = true;
            }
        } catch (Exception e) {
            // Endpoint not available yet, wait and try again
            Thread.sleep(1000);
        }
    }
    System.out.println("");
}


  }
}
