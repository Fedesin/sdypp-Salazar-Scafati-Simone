package com.example;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

@RestController
public class FileController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Environment env;
    /*
    curl -X POST -F "file=@a.txt" -F "host=localhost" -F "id_usuario=12"" -F "port=8088" http://localhost:8081/cargar
    curl -X POST -F "file=@/ruta/al/archivo/a/subir" -F "host=localhost" -F "port=8088" -F "id_usuario=12" http://localhost:8081/cargar
     curl -X POST -F "file=@pepe.txt" -F "host=maestro" -F "port=8080" -F "id_usuario=90" http://localhost:59019/cargar
    */
    @PostMapping(value = "/cargar", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("host") String host, @RequestParam("port") int port, @RequestParam("id_usuario") int id_usuario) {
        try {
            // Guardar el archivo en la carpeta especificada
            String filename = file.getOriginalFilename();
            Path filepath = Paths.get("/app/archivos", filename);
            Files.write(filepath, file.getBytes());
            String maestroUrl = "http://";
            maestroUrl += host;
            maestroUrl += ":"+port+"/maestro/actualizar";
            RestTemplate restTemplate = new RestTemplate();
            // Obtener la dirección IP
            InetAddress ip = InetAddress.getLocalHost();
            String hostExtremo = ip.getHostAddress();
            // Obtener el puerto
            int portExtremo = port;
            Map<String, Object> params = new HashMap<>();
            params.put("filename", filename);
            params.put("hostExtremo", hostExtremo);
            params.put("id_usuario", id_usuario);
            params.put("portExtremo", portExtremo);
            Gson gson = new Gson();
            String json = gson.toJson(params);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            String respuesta = restTemplate.postForObject(maestroUrl, entity, String.class);
            return ResponseEntity.ok("Archivo enviado correctamente: " + filename + "\nRespuesta del maestro : " + respuesta);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el archivo");
        }
    }

    /*
    curl -X GET "http://localhost:8080/descargar?filename=a.txt&host=maestro&port=8080&id_usuario=12"
     curl -X GET "http://localhost:8080/descargar?filename=pepe.txt&host=maestro&port=8080&id_usuario=90"
    */
    //id usuario de quien soy yo, host y puerto del extremo a conectarme, y filename del nombre del archivo a descargar
    @GetMapping("/descargar")
    public ResponseEntity<Resource> descargar(@RequestParam("filename") String filename, @RequestParam("host") String host, @RequestParam("port") int port, @RequestParam("id_usuario") int id_usuario){
        try {
            // Construir URL para consultar al servidor maestro si el archivo existe
            String maestroUrl = "http://" + host + ":" + port + "/maestro/consultar/" + filename;
            // Crear un objeto RestTemplate para enviar solicitudes HTTP
            RestTemplate restTemplate = new RestTemplate();
            // Enviar una solicitud GET al servidor maestro para verificar si el archivo existe
            // y obtener la dirección IP y el número de puerto del servidor que lo tiene
            String respuesta = restTemplate.getForObject(maestroUrl, String.class);
           
                // Si el archivo existe, construir una URL para descargarlo del servidor que lo tiene
                String extremoUrl = "http://" + respuesta + "/getArchivo/?nombre="+filename;
                // Enviar una solicitud GET al servidor que tiene el archivo para descargarlo
                System.out.println(extremoUrl);
                // Crear otro objeto RestTemplate para enviar solicitudes HTTP al servidor que tiene el archivo
                RestTemplate restTemplate2 = new RestTemplate();   
                ResponseEntity<byte[]> response = restTemplate2.getForEntity(extremoUrl, byte[].class);
                
                // Guardar el archivo descargado como un objeto MultipartFile
                if (response.getStatusCode() == HttpStatus.OK) {
                 // Get the file bytes from the response
                 byte[] fileBytes = response.getBody();

                // Define the file path and name where you want to save the file
                  String filePath = "/app/archivos/" + filename;

                 try {
                // Save the file to the specified path
                  Files.write(Paths.get(filePath), fileBytes);
                  System.out.println("File saved successfully.");
                  } catch (IOException e) {
                  // Handle the exception if the file couldn't be saved
                  System.err.println("Error saving the file: " + e.getMessage());
                 }
                }            else {
                // Handle the case when the server returned an error status
                 System.err.println("File download failed. Server returned status: " + response.getStatusCodeValue());
                }
                
                // Obtener la dirección IP local y el número de puerto del servidor
                InetAddress ipLocal = InetAddress.getLocalHost();
                String hostExtremo = ipLocal.getHostAddress();
                int portLocal = Integer.parseInt(env.getProperty("server.port"));
                // Obtener el archivo del servidor a partir del nombre
                 File archivoDescargado = new File("/app/archivos/" + filename);

                // Verificar si el archivo existe
                if (!archivoDescargado.exists()) {
                  return ResponseEntity.notFound().build();
                  }

                // Crear un recurso de Spring para el archivo
                 Resource recurso = new FileSystemResource(archivoDescargado);
                // Llamar al método uploadFile para cargar el archivo descargado en el servidor
                //uploadFile(archivo,hostExtremo,portLocal,id_usuario);
                
                // Devolver una respuesta HTTP 200 (OK) con un mensaje indicando que se descargó el archivo correctamente
                     return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +filename+ "\"")
                .body(recurso);
        
        } catch (IOException e) {
            // Si se produce una excepción al enviar o recibir la solicitud HTTP, devolver una respuesta HTTP 500 (INTERNAL SERVER ERROR)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
/* 
    @GetMapping("/GET ArchivosDisponibles")
    public ResponseEntity<Resource> ArchivosDisponibles() {
             //---
            // FALTA HACER EL GET /PropietarioDelArchivo al maestro
            // LUEGO HACER UN GET /descargar al extremo
            //---
    }
*/
    /*
    curl -o archivo.txt http://localhost:8081/getArchivo?nombre=archivo.txt
    */
   @GetMapping("/getArchivo")
    public ResponseEntity<Resource> getArchivo(@RequestParam(name = "nombre") String nombreArchivo) {

        // Obtener el archivo del servidor a partir del nombre
        File archivo = new File("/app/archivos/" + nombreArchivo);

        // Verificar si el archivo existe
        if (!archivo.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Crear un recurso de Spring para el archivo
        Resource recurso = new FileSystemResource(archivo);

        // Crear una respuesta HTTP con el archivo adjunto
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getName() + "\"")
                .body(recurso);
        }
    
}






