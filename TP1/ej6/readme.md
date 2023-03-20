## Ejercicio 6
*2) Escribir un servidor utilizando Servidor Web HTTP, que ofrezca la posibilidad de sumar y restar vectores de enteros. Introduzca un error en su código que modifique los vectores recibidos por parámetro. ¿Qué impacto se genera? ¿Qué conclusión saca sobre la forma de pasaje de parámetros en HTTP? Mostrar claramente los valores de los vectores del lado cliente, antes y después de la ejecución de la suma o resta.*

A partir de un codigo generado por chatgpt se creo  un servidor http que recibe dos vectores como parametros y dependiendo de como sea la url se indica si se quiere sumar o restar.Se inserto ademas un error en el codigo al momento de sumar o restar. La forma de
pasar parametros en http puede ser poco segura por que estos pueden ser interceptados por un tercero, ademas la aplicacion cliente y el servidor deben conocer exactamente como se va a llamar el o los nombres de los parametros que van a utilizar y que informacion debe contener. A continuacion adjunto capturas del mismo.

![Ejemplo de funcionamento del ejercicio 6](https://github.com/Fedesin/sdypp-Salazar-Scafati-Simone/blob/main/TP1/ej2/Captura de pantalla 2023-03-20 180602.jpg)
![Ejemplo de funcionamento del ejercicio 6](https://github.com/Fedesin/sdypp-Salazar-Scafati-Simone/blob/main/TP1/ej2/Captura de pantalla 2023-03-20 180649.jpg)
