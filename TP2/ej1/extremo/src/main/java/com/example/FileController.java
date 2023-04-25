package com.example;
import java.io.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.Maestro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;

@RestController
public class FileController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Environment env;
    /*
    curl -X POST -F "file=@a.txt" -F "host=localhost" -F "port=8088" http://localhost:8081/cargar
    */
    @PostMapping(value = "/cargar", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("host") String host, @RequestParam("port") int port) {
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
    @GetMapping("/getArchivo")
    public ResponseEntity<Resource> getArchivo(@RequestParam(name = "nombre") String nombreArchivo) {
             //---
            // FALTA HACER EL GET /PropietarioDelArchivo al maestro
            // LUEGO HACER UN GET /descargar al extremo
            //---
    }

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
   @GetMapping("/descargar")
    public ResponseEntity<Resource> descargar(@RequestParam(name = "nombre") String nombreArchivo) {
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






