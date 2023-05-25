package com.example;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SobelOperator {

    
    public static BufferedImage sobelizador(BufferedImage inputImage) {

        BufferedImage grayImage = convertToGrayScale(inputImage);
        BufferedImage finalImage = applySobelOperator(grayImage);
        return finalImage;
    }
    // se convierte a escala de grises cada pixel de la imagen pasada por parametro
    private static BufferedImage convertToGrayScale(BufferedImage inputImage) {
       
        // se instancia una imagen con las mismas dimenciones que la imagen que se paso por parametros
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // se recorre la imagen extrayendo cada pixel y calculando el valor del pixel en escala de grises
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(inputImage.getRGB(x, y));
                int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                Color grayColor = new Color(grayValue, grayValue, grayValue);
                grayImage.setRGB(x, y, grayColor.getRGB());
            }
        }

        // se devuelve la imagen en escala de grises
        return grayImage;
    }

    // se aplica el operador sobel sobre la imagen pasada por parametro
    private static BufferedImage applySobelOperator(BufferedImage grayImage) {
        
        // se instancia una imagen con las mismas dimenciones que la imagen que se paso por parametros
        int width = grayImage.getWidth();
        int height = grayImage.getHeight();
        BufferedImage sobelImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // mascara a aplicar sobre cada pixel de la imagen
        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        // se recorre la imagen extrayendo cada pixel y aplicando la mascara sobre los mismos
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = calculateGradient(grayImage, x, y, sobelX);
                int pixelY = calculateGradient(grayImage, x, y, sobelY);
                int gradient = Math.min(255, Math.max(0, (int) Math.sqrt(pixelX * pixelX + pixelY * pixelY)));
                sobelImage.setRGB(x, y, new Color(gradient, gradient, gradient).getRGB());
            }
        }

        // se devuelve la imagen resultante
        return sobelImage;
    }

    // se calcula el gradiente sobre el pixel pasado por parametro
    private static int calculateGradient(BufferedImage grayImage, int x, int y, int[][] mask) {
        int result = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int pixel = new Color(grayImage.getRGB(x + i, y + j)).getRed();
                result += mask[i + 1][j + 1] * pixel;
            }
        }

        return result;
    }

}