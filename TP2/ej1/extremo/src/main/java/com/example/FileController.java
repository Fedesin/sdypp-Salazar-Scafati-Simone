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
    curl -X POST -F "file=@a.txt" -F "host=localhost" -F "id_usuario=12"" -F "port=8088" http://localhost:8081/cargar
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
    curl -X POST -F "filename=a.txt" -F "host=localhost" -F "port=8088" -F "id_usuario=12" http://localhost:8081/cargar
    */
    //id usuario de quien soy yo, host y puerto del extremo a conectarme, y filename del nombre del archivo a descargar
    @GetMapping("/descargar")
    public ResponseEntity<Resource> descargar(@RequestParam("filename") String filename, @RequestParam("host") String host, @RequestParam("port") int port, @RequestParam("id_usuario") int id_usuario){
            // recibo el nombre del archivo a bajar, la ip del maestro a cual consulto
            // si no existe el maestro me devuelve un 404 y devuelvo eso al cliente y listo
            // si existe llamo al getArchivo y le paso el name y la dir ip q me haya devuelto el maestro
            // llamo al actualizar del maestro
            // y devuelvo 200 ok al cliente
            try {
                // consulto al maestro si eso existe
                maestroUrl += host;
                maestroUrl += ":"+port+"/maestro/consultar";
                RestTemplate restTemplate = new RestTemplate();
                Map<String, Object> params = new HashMap<>();
                params.put("filename", filename);
                Gson gson = new Gson();
                String json = gson.toJson(params);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(json, headers);
                //la idea es que me devuelva la dir ip y el port de quien tiene el filename
                String respuesta = restTemplate.postForObject(maestroUrl, entity, String.class);
                if (respuesta!="404"){
                    //Si llegue acá es porque el archivo existe, alguien lo tiene
                    respuesta +="/getArchivo";
                    extremoUrl = respuesta;
                    RestTemplate restTemplate2 = new RestTemplate();
                    Map<String, Object> params2 = new HashMap<>();
                    params2.put("filename", filename);
                    Gson gson2 = new Gson();
                    String json2 = gson.toJson(params);
                    HttpHeaders headers2 = new HttpHeaders();
                    headers2.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity2 = new HttpEntity<>(json, headers);
                    entity2 = restTemplate.postForObject(extremoUrl, entity2, String.class);
                    // tengo en el body de la entity2 el archivo, ahora deberia llamar a la funcion uploadFile, pasandole
                    // el archivo en cuestion, mi ip y mi puerto y el id_usuario que es pasado al comienzo de la funcion.
                    uploadFile(entity2.body, ip.getHostAddress(), Integer.parseInt(env.getProperty("server.port"), id_usuario));
                    return ResponseEntity.ok("Archivo descargado correctamente: " + filename+" Descargado de: "+extremoUrl);
                }else{
                    return ResponseEntity.notFound(404);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al Descargar el archivo");
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






