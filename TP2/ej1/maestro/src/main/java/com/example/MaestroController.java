/*
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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.example.ActualizarRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/maestro")
public class MaestroController {

    @Autowired
    private PostgresController postgresController;

    @Autowired
    private HttpServletRequest request;
    //
    @PostMapping(value = "/actualizar", produces = "text/plain")
    public ResponseEntity<String> actualizar(@RequestBody ActualizarRequest request) {
        String filename = request.getFilename();
        int id_usuario = request.getId_usuario();
        String hostExt = request.getHostExtremo();
        int portExt = request.getPortExtremo();
        String extremoUrl = hostExt + ":" + portExt;
        insertUsuario(id_usuario,hostExt,portExt);
        insertArchivo(id_usuario,filename);
        return ResponseEntity.ok("Archivo "+filename+" recibido correctamente, lo envio el extremo: "+extremoUrl+".");
}

    @PostMapping("/mensaje")
    public ResponseEntity<String> recibirMensaje(@RequestBody String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
        return ResponseEntity.ok("Mensaje recibido correctamente");
    }

    @GetMapping("/usuario_IP_Port/{id}")
    @ResponseBody
    public String usuario_IP_Port(@PathVariable int id) {
        String[] usuario = postgresController.getUsuario(id);
        Gson gson = new Gson();
        return gson.toJson(new Usuario(usuario[0], Integer.parseInt(usuario[1])));
    }

    // Usuario class
    class Usuario {
        private String ip;
        private int port;
    public Usuario(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    }

    public void insertArchivo(int userId,String fileName) {
        postgresController.insertArchivo(userId, fileName);
    }

     public void insertUsuario(int userId,String ip,int port) {
        postgresController.insertUsuario(userId, ip, port);
    }

    public String[] getUsuario(int userId) {
       return postgresController.getUsuario(userId);
    }

}
