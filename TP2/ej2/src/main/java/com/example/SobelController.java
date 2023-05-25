package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class SobelController {

    private SobelOperator sobelOperator = new SobelOperator();

    @PostMapping("/sobel")
    public ResponseEntity<String> applySobelOperator(@RequestBody MultipartFile file) {
        try {
            // Read the uploaded image into a BufferedImage
            BufferedImage image = ImageIO.read(file.getInputStream());

            // Apply Sobel operator on the image
            BufferedImage sobelImage = sobelOperator.sobelizador(image);

            // Specify the output file path
            String outputImagePath = "output.jpg";

            // Save the sobel image to the output path
            ImageIO.write(sobelImage, "jpg", new File(outputImagePath));

            return ResponseEntity.ok("Sobel operation completed. Result image saved as output.jpg");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing image: " + e.getMessage());
        }
    }
}
