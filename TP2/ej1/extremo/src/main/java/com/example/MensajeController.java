package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
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
import org.springframework.web.client.RestTemplate;

@RestController
public class MensajeController {
    @Autowired
    private RestTemplate restTemplate;
    @PostMapping("/enviar-mensaje")
    public String enviarMensaje(@RequestBody String mensaje) {
        String microservicio1Url = "http://localhost:8080/microservicio1/mensaje";
        String respuesta = restTemplate.postForObject(microservicio1Url, mensaje, String.class);
        return "Mensaje enviado correctamente: " + mensaje + "\nRespuesta del microservicio 1: " + respuesta;
    }

}