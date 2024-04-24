package com.example.textencryption;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Steganography {
    private static Steganography steganography = null;


    private Steganography() {
    }

    public static void init() {
        if (steganography == null) steganography = new Steganography();
    }

    public static Steganography getInstance() {
        return steganography;
    }


public static Bitmap encode(Bitmap image, String text) {
    // Convert the text into bytes
    byte[] textBytes = text.getBytes();

    // Calculate the total bits needed to encode the length and the text
    int totalBits = 32 + textBytes.length * 8;

    // Get the image dimensions
    int width = image.getWidth();
    int height = image.getHeight();

    if (totalBits > width * height) {
        throw new IllegalArgumentException("Image is too small to hold the text");
    }

    // Create a mutable copy of the image
    Bitmap encodedImage = image.copy(Bitmap.Config.ARGB_8888, true);

    // Encode the length of the text into the first 32 bits
    int length = textBytes.length;
    for (int i = 0; i < 32; i++) {
        int x = i % width;
        int y = i / width;
        int pixel = encodedImage.getPixel(x, y);
        int lsb = (length >> (31 - i)) & 1;
        int newPixel = setLeastSignificantBit(pixel, lsb);
        encodedImage.setPixel(x, y, newPixel);
    }

    // Encode the text bytes into the image
    int bitIndex = 32;
    for (byte b : textBytes) {
        for (int i = 0; i < 8; i++) {
            int x = bitIndex % width;
            int y = bitIndex / width;
            int pixel = encodedImage.getPixel(x, y);
            int lsb = (b >> (7 - i)) & 1;
            int newPixel = setLeastSignificantBit(pixel, lsb);
            encodedImage.setPixel(x, y, newPixel);
            bitIndex++;
        }
    }
    return encodedImage;
}

    private static int setLeastSignificantBit(int pixel, int bit) {
        int alpha = Color.alpha(pixel);
        int red = Color.red(pixel);
        int green = Color.green(pixel);
        int blue = Color.blue(pixel);

        // Change the LSB of each color component
        red = (red & 0xFE) | ((bit >> 2) & 1);
        green = (green & 0xFE) | ((bit >> 1) & 1);
        blue = (blue & 0xFE) | (bit & 1);

        return Color.argb(alpha, red, green, blue);
    }
    public static String decode(Bitmap encodedImage) {
        int width = encodedImage.getWidth();
        int height = encodedImage.getHeight();

        // Extract the length of the text from the first 32 bits
        int length = 0;
        for (int i = 0; i < 32; i++) {
            int x = i % width;
            int y = i / width;
            int pixel = encodedImage.getPixel(x, y);
            int bit = getLeastSignificantBit(pixel);
            length |= (bit << (31 - i));
        }

        // Prepare a buffer to hold the decoded bytes
        byte[] messageBytes = new byte[length];

        // Extract the message from the image
        int bitIndex = 32;
        for (int j = 0; j < length; j++) {
            byte currentByte = 0;
            for (int i = 0; i < 8; i++) {
                int x = bitIndex % width;
                int y = bitIndex / width;
                int pixel = encodedImage.getPixel(x, y);
                int bit = getLeastSignificantBit(pixel);
                currentByte |= (bit << (7 - i));
                bitIndex++;
            }
            messageBytes[j] = currentByte;
        }

        // Convert the byte array to a string
        return new String(messageBytes);
    }

    private static int getLeastSignificantBit(int pixel) {
        int red = Color.red(pixel);
        int green = Color.green(pixel);
        int blue = Color.blue(pixel);

        // Extract the LSB from each color component
        int lsbRed = red & 1;
        int lsbGreen = green & 1;
        int lsbBlue = blue & 1;

        return (lsbRed << 2) | (lsbGreen << 1) | lsbBlue;
    }
}
