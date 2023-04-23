package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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