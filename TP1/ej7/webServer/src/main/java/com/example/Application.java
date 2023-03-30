/*-
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
public class Application{

    public static void main(String[] args) {
        // SpringApplication.run(Application.class, args);
        // //con el dato del header vamos a saber que docker levantar
        // @PostMapping("/suma"){
        //     //invocamos el contenedor suma
        //     public String obtenerSuma(@RequestBody SumaDto suma) {
        //         int resultado = suma.getNum1() + suma.getNum2();
        //         return "La suma de " + suma.getNum1() + " y " + suma.getNum2() + " es: " + resultado;
        //     }
        // }
        // @PostMapping("/saludo"){
        //     //invocamos el contenedor saludo
        // }
    }
}
// public static class SumaDto {
//     private int num1;
//     private int num2;

//     public int getNum1() {
//         return num1;
//     }

//     public void setNum1(int num1) {
//         this.num1 = num1;
//     }

//     public int getNum2() {
//         return num2;
//     }

//     public void setNum2(int num2) {
//         this.num2 = num2;
//     }
// }
