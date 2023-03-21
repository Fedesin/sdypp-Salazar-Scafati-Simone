## Ejercicio 3
*3) Escriba un servidor de mensajes de colas, que permita a los clientes dejar un mensaje (identificando de alguna forma a quien se lo dejan), y bajar los mensajes que le estan dirigidos. La comunicacion entre cliente y servidor debe ser mediante sockets, y el servidor debe poder atender varios clientes a la vez. 

Se planteo un servidor TCP multiusurio que permite a los clientes escribir mensajes a un cliente destino y que estos sean almacenados en una "cola" a la espera de ser leidos.

Desde el lado del cliente, se permite escribir mensajes a varios destinos y leer los mensajes que le llegaron dentro de la misma conexion.
