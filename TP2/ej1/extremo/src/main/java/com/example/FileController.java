package com.example;
import java.io.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class FileController {
    
    @PostMapping("/cargar")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Guardar el archivo en la carpeta especificada
            String filename = file.getOriginalFilename();
            Path filepath = Paths.get("./archivos", filename);
            Files.write(filepath, file.getBytes());
            //---
            // FALTA HACER EL POST /Actualizar al maestro
            // enviar nombre del archivo,ip y puerto del extremo
            //---
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el archivo");
        }
        return ResponseEntity.ok("Archivo recibido y guardado correctamente");
    }

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






