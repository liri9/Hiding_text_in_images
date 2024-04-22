package com.example.textencryption;

import android.graphics.Bitmap;

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

    public static Bitmap encode(Bitmap image, String message) {
        int width = image.getWidth();
        int height = image.getHeight();
        Bitmap encodedImage = image.copy(Bitmap.Config.ARGB_8888, true);

        // Convert message to binary
        byte[] msgBytes = message.getBytes();
        StringBuilder binaryMsg = new StringBuilder();
        for (byte b : msgBytes) {
            binaryMsg.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        int msgLen = binaryMsg.length();
        int pixelIndex = 0;

        for (int index = 0; index < msgLen; index++) {
            if (pixelIndex >= width * height) break;  // Prevent exceeding the image size

            // Get coordinates
            int x = pixelIndex % width;
            int y = pixelIndex / width;

            // Current pixel
            int pixel = encodedImage.getPixel(x, y);
            int newPixel;

            // Modify the pixel's least significant bit
            if (binaryMsg.charAt(index) == '1') {
                newPixel = pixel | 1;  // Set the LSB to 1
            } else {
                newPixel = pixel & ~1; // Set the LSB to 0
            }

            encodedImage.setPixel(x, y, newPixel);
            pixelIndex++;
        }

        return encodedImage;
    }

    public static String decode(Bitmap image, int length) {
        int width = image.getWidth();
        int height = image.getHeight();
        StringBuilder binaryMsg = new StringBuilder();

        for (int index = 0; index < length; index++) {
            int x = index % width;
            int y = index / width;

            if (y >= height) break;  // Prevent exceeding the image bounds

            int pixel = image.getPixel(x, y);
            int lsb = pixel & 1;      // Extract the least significant bit
            binaryMsg.append(lsb);
        }

        // Convert binary string to ASCII text
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < binaryMsg.length(); i += 8) {
            String byteString = binaryMsg.substring(i, Math.min(i + 8, binaryMsg.length()));
            int charCode = Integer.parseInt(byteString, 2);
            message.append((char) charCode);
        }

        return message.toString();
    }


}
