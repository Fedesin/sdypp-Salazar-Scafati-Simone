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
    */
    @PostMapping(value = "/cargar", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("host") String host, @RequestParam("port") int port, @RequestParam("id_usuario") int id_usuario) {
        try {
            // Guardar el archivo en la carpeta especificada
            String filename = file.getOriginalFilename();
            Path filepath = Paths.get("./archivos", filename);
            Files.write(filepath, file.getBytes());
            String maestroUrl = "http://";
            maestroUrl += host;
            maestroUrl += ":"+port+"/maestro/actualizar";
            RestTemplate restTemplate = new RestTemplate();
            // Obtener la dirección IP
            InetAddress ip = InetAddress.getLocalHost();
            String hostExtremo = ip.getHostAddress();
            // Obtener el puerto
            int portExtremo = Integer.parseInt(env.getProperty("server.port"));
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
    curl -X GET -F "filename=a.txt" -F "host=localhost" -F "port=8088" -F "id_usuario=12" http://localhost:8081/descargar
    curl -X GET "http://localhost:8081/descargar?filename=a.txt&host=localhost&port=8088&id_usuario=12"
    */
    //id usuario de quien soy yo, host y puerto del extremo a conectarme, y filename del nombre del archivo a descargar
    @GetMapping("/descargar")
public ResponseEntity<String> descargar(@RequestParam("filename") String filename, @RequestParam("host") String host, @RequestParam("port") int port, @RequestParam("id_usuario") int id_usuario){
    try {
        // Construir URL para consultar al servidor maestro si el archivo existe
        String maestroUrl = "http://" + host + ":" + port + "/maestro/consultar/" + filename;
        
        // Crear un objeto RestTemplate para enviar solicitudes HTTP
        RestTemplate restTemplate = new RestTemplate();
        
        // Crear un mapa de parámetros para enviar en la solicitud HTTP al servidor maestro
        Map<String, Object> params = new HashMap<>();
        params.put("filename", filename);
        
        // Convertir los parámetros a JSON
        Gson gson = new Gson();
        String json = gson.toJson(params);
        
        // Configurar encabezados de la solicitud HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Crear una entidad HttpEntity con los parámetros y encabezados
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        
        // Enviar una solicitud POST al servidor maestro para verificar si el archivo existe
        // y obtener la dirección IP y el número de puerto del servidor que lo tiene
        String respuesta = restTemplate.postForObject(maestroUrl, entity, String.class);
        if (!respuesta.equals("404")){
            // Si el archivo existe, construir una URL para descargarlo del servidor que lo tiene
            String extremoUrl = "http://" + respuesta + "/getArchivo";
            
            // Crear otro objeto RestTemplate para enviar solicitudes HTTP al servidor que tiene el archivo
            RestTemplate restTemplate2 = new RestTemplate();
            
            // Crear un mapa de parámetros para enviar en la solicitud HTTP al servidor que tiene el archivo
            Map<String, Object> params2 = new HashMap<>();
            params2.put("filename", filename);
            
            // Enviar una solicitud POST al servidor que tiene el archivo para descargarlo
            System.out.println("BANDERAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            ResponseEntity<byte[]> response = restTemplate2.postForEntity(extremoUrl, entity, byte[].class);
            System.out.println("2BANDERAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            // Guardar el archivo descargado como un objeto MultipartFile
            MultipartFile archivo = new MockMultipartFile("file", response.getBody());
            
            // Obtener la dirección IP local y el número de puerto del servidor
            InetAddress ipLocal = InetAddress.getLocalHost();
            String hostExtremo = ipLocal.getHostAddress();
            int portLocal = Integer.parseInt(env.getProperty("server.port"));
            
            // Llamar al método uploadFile para cargar el archivo descargado en el servidor
            uploadFile(archivo,hostExtremo,portLocal,id_usuario);
            
            // Devolver una respuesta HTTP 200 (OK) con un mensaje indicando que se descargó el archivo correctamente
            return ResponseEntity.ok("Archivo descargado ");
        } else {
            // Si el archivo no existe, devolver una respuesta HTTP 404 (NOT FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
    curl -o archivo.txt http://localhost:8081/descargar?nombre=archivo.txt
    */
   @GetMapping("/getArchivo")
    public ResponseEntity<Resource> getArchivo(@RequestParam(name = "nombre") String nombreArchivo) {

        // Obtener el archivo del servidor a partir del nombre
        File archivo = new File("./archivos/" + nombreArchivo);

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






