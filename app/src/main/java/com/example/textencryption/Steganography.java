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

//    //        public static Bitmap encode(Bitmap image, String message) {
////        int width = image.getWidth();
////        int height = image.getHeight();
////        Bitmap encodedImage = image.copy(Bitmap.Config.ARGB_8888, true);
////
////        // Convert message to binary
////        byte[] msgBytes = message.getBytes();
////        StringBuilder binaryMsg = new StringBuilder();
////        for (byte b : msgBytes) {
////            binaryMsg.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
////        }
////
////        int msgLen = binaryMsg.length();
////        Log.d("decode","encode msg len: "+ msgLen);
////        int pixelIndex = 0;
////
////        for (int index = 0; index < msgLen; index++) {
////            if (pixelIndex >= width * height) break;  // Prevent exceeding the image size
////
////            // Get coordinates
////            int x = pixelIndex % width;
////            int y = pixelIndex / width;
////
////            // Current pixel
////            int pixel = encodedImage.getPixel(x, y);
////            int newPixel;
////
////            // Modify the pixel's least significant bit
////            if (binaryMsg.charAt(index) == '1') {
////                newPixel = pixel | 1;  // Set the LSB to 1
////            } else {
////                newPixel = pixel & ~1; // Set the LSB to 0
////            }
////
////            encodedImage.setPixel(x, y, newPixel);
////            pixelIndex++;
////        }
////
////        return encodedImage;
////    }
////    public static Bitmap encode(Bitmap image, String message) {
////        int width = image.getWidth();
////        int height = image.getHeight();
////        Bitmap encodedImage = image.copy(Bitmap.Config.ARGB_8888, true);
////
////        byte[] msgBytes = message.getBytes();
////        StringBuilder binaryMsg = new StringBuilder();
////        for (byte b : msgBytes) {
////            binaryMsg.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
////        }
////
////        // Save the number of characters, not bits
////        int msgLen = msgBytes.length;  // This should hold the actual number of characters
////        int bitsToEncode = binaryMsg.length(); // Total bits to encode
////        int pixelIndex = 0;
////
////        // First encode the length of the message
////        String lengthBinary = String.format("%32s", Integer.toBinaryString(msgLen)).replace(' ', '0');
////        for (int i = 0; i < 32; i++) {
////            int x = pixelIndex % width;
////            int y = pixelIndex / width;
////            int pixel = encodedImage.getPixel(x, y);
////            int newPixel = (lengthBinary.charAt(i) == '1') ? (pixel | 1) : (pixel & ~1);
////            encodedImage.setPixel(x, y, newPixel);
////            pixelIndex++;
////        }
////
////        // Then encode the message
////        for (int index = 0; index < bitsToEncode; index++) {
////            if (pixelIndex >= width * height) break;
////
////            int x = pixelIndex % width;
////            int y = pixelIndex / width;
////
////            int pixel = encodedImage.getPixel(x, y);
////            int newPixel = (binaryMsg.charAt(index) == '1') ? (pixel | 1) : (pixel & ~1);
////
////            encodedImage.setPixel(x, y, newPixel);
////            pixelIndex++;
////        }
////        Log.d("decode","encode msg len: "+ msgLen);
////        return encodedImage;
////    }
//    public static Bitmap encode(Bitmap image, String text) {
//
////        int width = image.getWidth();
////        int height = image.getHeight();
////        Bitmap encodedImage = image.copy(Bitmap.Config.ARGB_8888, true);
////
////        int pixelIndex = 0;
////
////        // Encode the length of the message
////        int msgLen = text.length();
////        String lengthBinary = String.format("%32s", Integer.toBinaryString(msgLen)).replace(' ', '0');
////        for (int i = 0; i < 32; i++) {
////            int x = pixelIndex % width;
////            int y = pixelIndex / width;
////            int pixel = encodedImage.getPixel(x, y);
////            int newPixel = (lengthBinary.charAt(i) == '1') ? (pixel | 1) : (pixel & ~1);
////            encodedImage.setPixel(x, y, newPixel);
////            pixelIndex++;
////        }
////
////        int charIndex = 0;
////        int charValue = text.charAt(charIndex);
////        int bitMask = 0x01; // Start with the least significant bit
////
////        for (int i = 0; i < text.length() * 8; i++) {
////            int x = pixelIndex % width;
////            int y = pixelIndex / width;
////
////            if (y >= height) {
////                throw new IllegalArgumentException("Text too long for the image");
////            }
////
////            int pixel = encodedImage.getPixel(x, y);
////            int lsb = (charValue & bitMask) > 0 ? 1 : 0; // Determine the bit to embed
////            int newPixel = (lsb == 1) ? (pixel | 1) : (pixel & ~1); // Set or clear the LSB
////            encodedImage.setPixel(x, y, newPixel);
////
////            bitMask <<= 1; // Move to the next bit in the character
////            if (bitMask == 0x100) { // If all bits of the current character are processed
////                charIndex++;
////                if (charIndex < text.length()) {
////                    charValue = text.charAt(charIndex);
////                    bitMask = 0x01; // Reset bit mask
////                }
////            }
////
////            pixelIndex++;
////        }
////        Log.d("decode", "encode msg len: " + msgLen);
////        Log.d("decode", "image bitmap: "+ encodedImage);
////
////        printBitmap(encodedImage,msgLen);
////        return encodedImage;
//
//        int width = image.getWidth();
//        int height = image.getHeight();
//        Bitmap encodedImage = image.copy(Bitmap.Config.ARGB_8888, true);  // Use ARGB_8888 for flexibility
//
//        int pixelIndex = 0;
//
//        // Encode the message length
//        int msgLen = text.length();
//        String lengthBinary = String.format("%32s", Integer.toBinaryString(msgLen)).replace(' ', '0');
//        for (int i = 0; i < 32; i++) {
//            // Modify based on color depth (here assuming 32-bit)
//            int color = encodedImage.getPixel(pixelIndex % width, pixelIndex / width);
//            int newColor = (lengthBinary.charAt(i) == '1') ? (color | 1) : (color & ~1);
//            encodedImage.setPixel(pixelIndex % width, pixelIndex / width, newColor);
//            pixelIndex++;
//        }
//
//        int charIndex = 0;
//        int charValue = text.charAt(charIndex);
//        int bitMask = 0x01; // Start with the least significant bit
//
//        for (int i = 0; i < text.length() * 8; i++) {
//            int x = pixelIndex % width;
//            int y = pixelIndex / width;
//
//            if (y >= height) {
//                throw new IllegalArgumentException("Text too long for the image");
//            }
//
//            // Modify based on color depth (here assuming 32-bit)
//            int color = encodedImage.getPixel(x, y);
//            int newColor = 0;  // Initialize with 0 to avoid modifying higher bits unintentionally
//            if ((i % 3) < 3) { // Process first 3 bits for RGB channels (adjust for different depths)
//                newColor = (charValue & bitMask) > 0 ? (color | (1 << (i % 3))) : (color & ~(1 << (i % 3)));
//            }
//            encodedImage.setPixel(x, y, newColor);
//
//            bitMask <<= 1; // Move to the next bit in the character
//            if (bitMask == 0x100) { // If all bits of the current character are processed
//                charIndex++;
//                if (charIndex < text.length()) {
//                    charValue = text.charAt(charIndex);
//                    bitMask = 0x01; // Reset bit mask
//                }
//            }
//
//            pixelIndex++;
//        }
//
//
//        Log.d("decode", "encode msg len: " + msgLen);
//        printBitmap(encodedImage,msgLen);
//        return encodedImage;
//    }
//
//    private static void printBitmap(Bitmap encodedImage, int msgLen) {
//        int width = encodedImage.getWidth();
//        int height = encodedImage.getHeight();
//        // Start reading right after the 32 bits used for the message length
//        for (int index = 0; index < msgLen * 8 + 32; index++) {
//            int x = index % width;
//            int y = index / width;
//
//            // Ensure we don't exceed the bitmap's dimensions
//            if (y >= height) {
//                break;
//            }
//
//            // Get pixel color
//            int pixel = encodedImage.getPixel(x, y);
//            // Extracting the LSB of the blue component of the pixel
//            int lsb = pixel & 0x00000001;  // LSB of blue channel if we're using ARGB_8888
//            // Log the LSB
//            Log.d("BitmapLSB", "Pixel (" + x + ", " + y + "): LSB = " + lsb);
//        }
//    }
//
//    public static String decode(Bitmap image) {
////        int width = image.getWidth();
////        int height = image.getHeight();
////        Log.d("decode", "image bitmap in decode before: "+ image);
////
////        StringBuilder binaryLength = new StringBuilder();
////        int pixelIndex = 0;
////
////        // Assuming the first 32 bits are used to store the length
////        for (int i = 0; i < 32; i++) {
////            if (pixelIndex >= width * height) break;  // Just a safeguard
////
////            // Get coordinates
////            int x = pixelIndex % width;
////            int y = pixelIndex / width;
////
////            // Current pixel
////            int pixel = image.getPixel(x, y);
////
////            // Extract the LSB of the pixel's blue color
////            int lsb = pixel & 1;
////            binaryLength.append(lsb);
////
////            pixelIndex++;
////        }
////
////        int messageLength = Integer.parseInt(binaryLength.toString(), 2);
////        Log.d("decode", "message length here: " + messageLength);
////        StringBuilder binaryMsg = new StringBuilder();
////
////        for (int index = 0; index < messageLength; index++) {
////            int x = index % width;
////            int y = index / width;
////
////            if (y >= height) break;  // Prevent exceeding the image bounds
////
////            int pixel = image.getPixel(x, y);
////            int lsb = pixel & 1;      // Extract the least significant bit
////            binaryMsg.append(lsb);
////        }
////
////        // Convert binary string to ASCII text
////        StringBuilder message = new StringBuilder();
////        for (int i = 0; i < binaryMsg.length(); i += 8) {
////            String byteString = binaryMsg.substring(i, Math.min(i + 8, binaryMsg.length()));
////            int charCode = Integer.parseInt(byteString, 2);
////            message.append((char) charCode);
////        }
////
//
////        return message.toString();
//        int width = image.getWidth();
//        int height = image.getHeight();
//        int pixelIndex = 0;
//
//        // Decode the message length
//        int messageLength = 0;
//        for (int i = 0; i < 32; i++) {
//            int x = pixelIndex % width;
//            int y = pixelIndex / width;
//            int pixel = image.getPixel(x, y);
//            int bit = pixel & 1;
//            messageLength |= (bit << (31 - i)); // Reconstruct from MSB to LSB
//            pixelIndex++;
//        }
//        Log.d("decode", "message length here: " + messageLength);
//        // Decode the text
//        StringBuilder message = new StringBuilder();
//        for (int i = 0; i < messageLength * 8; i++) {
//            int x = pixelIndex % width;
//            int y = pixelIndex / width;
//            int pixel = image.getPixel(x, y);
//            int bit = (pixel & 1) << (7 - (i % 8)); // Read bits from MSB to LSB
//
//            if ((i + 1) % 8 == 0) { // After reading 8 bits, form a character
//                char c = (char) (bit | (message.length() > 0 ? message.charAt(message.length() - 1) : 0));
//                message.append(c);
//                bit = 0;
//            } else if (message.length() > 0) {
//                char c = (char) (bit | message.charAt(message.length() - 1));
//                message.setCharAt(message.length() - 1, c);
//            }
//
//            pixelIndex++;
//        }
//
//        return message.toString();
//    }
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
