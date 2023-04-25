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
import com.example.Extremo;
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

@RestController
@RequestMapping("/maestro")
public class MaestroController {

    @Autowired
    private HttpServletRequest request;
    //
    @PostMapping(value = "/actualizar", produces = "text/plain")
    public ResponseEntity<String> actualizar(@RequestBody ActualizarRequest request) {
        String filename = request.getFilename();
        String hostExt = request.getHostExtremo();
        int portExt = request.getPortExtremo();
        String extremoUrl = hostExt + ":" + portExt;
        System.out.println("200 ok");
        //acá deberia ir la conexion a la bd 
        return ResponseEntity.ok("Archivo "+filename+" recibido correctamente, lo envio el extremo: "+extremoUrl+".");
}

    @PostMapping("/mensaje")
    public ResponseEntity<String> recibirMensaje(@RequestBody String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
        return ResponseEntity.ok("Mensaje recibido correctamente");
    }
}
