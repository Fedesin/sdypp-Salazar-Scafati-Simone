package com.example;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class SobelController {

    private SobelOperator sobelOperator = new SobelOperator();
   
    //curl -X POST -F "file=@image.jpg" http://localhost:8080/sobel -o new_image.jpg
    @PostMapping("/sobel")
    public ResponseEntity<Resource> applySobelOperator(@RequestBody MultipartFile file)   {
        
            try {
            // Read the uploaded image into a BufferedImage
            BufferedImage image = ImageIO.read(file.getInputStream());

            // Apply Sobel operator on the image
            BufferedImage sobelImage = sobelOperator.sobelizador(image);

            // Specify the output file path
            String outputImagePath = "output.jpg";

            // Save the sobel image to the output path
            ImageIO.write(sobelImage, "jpg", new File(outputImagePath));
            File archivo = new File(outputImagePath);

             // Verificar si el archivo existe
            if (!archivo.exists()) {
                 return ResponseEntity.notFound().build();
            }

            // Crear un recurso de Spring para el archivo
            Resource recurso = new FileSystemResource(archivo);

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputImagePath + "\"")
                .body(recurso);
            } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
    }
}
