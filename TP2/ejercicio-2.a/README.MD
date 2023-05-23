
# Enunciado

2) El operador de Sobel es una máscara que, aplicada a una imagen, permite detectar
(resaltar) bordes. Este operador es una operación matemática que, aplicada a cada
pixel y teniendo en cuenta los píxeles que lo rodean, obtiene un nuevo valor (color)
para ese pixel. Aplicando la operación a cada píxel, se obtiene una nueva imagen que
resalta los bordes.

a) Desarrollar un proceso centralizado que tome una imagen, aplique la máscara, y
genere un nuevo archivo con el resultado.

## Ejecucion

Este tutorial utiliza Maven. Para construir ejecute:

```
mvn clean install
```
luego:
```
java -jar SobelOperator.jar [path-input-image].[extension] [path-processed-image]
```
