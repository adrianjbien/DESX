package com.example.desx;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Converter {

    public String asciiToBinaryString(String text) {
        byte[] bytes = text.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }
    public String BytesToAscii(String bytes) {
        return Arrays.stream(bytes.split("(?<=\\G.{8})"))/* regex to split the bits array by 8*/
                .parallel()
                .map(eightBits -> (char)Integer.parseInt(eightBits, 2))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
    }


    public byte[] convertStringToByteFile(String text) {
        int length = text.length() / 8;
        byte[] byteArray = new byte[length];

        for (int i = 0; i < length; i++) {
            String byteString = text.substring(i * 8, (i + 1) * 8);
            byte byteValue = (byte) Integer.parseInt(byteString, 2);
            byteArray[i] = byteValue;
        }
        return byteArray;
    }
    public byte[] convertStringToByte(String text) {
        byte[] byteArray = new byte[text.length()];

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            byte value = (byte) (c == '1' ? 1 : 0);
            byteArray[i] = value;
        }
        return byteArray;
    }

    public String convertByteToString(byte[] data) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            result.append(data[i]);
        }
        return result.toString();
    }
    public String byteStringToHex(String coded) {
        return new BigInteger(coded, 2).toString(16);
    }

    public String hexToBin(String hex) {
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }

    public String binToHex(String bin) {
        Map<String, String> binaryToHexMap = new HashMap<>();
        binaryToHexMap.put("0000", "0");
        binaryToHexMap.put("0001", "1");
        binaryToHexMap.put("0010", "2");
        binaryToHexMap.put("0011", "3");
        binaryToHexMap.put("0100", "4");
        binaryToHexMap.put("0101", "5");
        binaryToHexMap.put("0110", "6");
        binaryToHexMap.put("0111", "7");
        binaryToHexMap.put("1000", "8");
        binaryToHexMap.put("1001", "9");
        binaryToHexMap.put("1010", "A");
        binaryToHexMap.put("1011", "B");
        binaryToHexMap.put("1100", "C");
        binaryToHexMap.put("1101", "D");
        binaryToHexMap.put("1110", "E");
        binaryToHexMap.put("1111", "F");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < bin.length(); i += 4) {
            String binaryChunk = bin.substring(i, i + 4);
            String hexValue = binaryToHexMap.get(binaryChunk);
            result.append(hexValue);
        }

        return result.toString();
    }

    public byte[] convertFileToBytes(byte[] fileData) {
        StringBuilder summary = new StringBuilder();
        for (byte b : fileData) {
            summary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return this.convertStringToByte(summary.toString());
    }

    public byte[] convertBytesToFile(byte[] data) {
        byte[] result = new byte[data.length / 8];
        StringBuilder temp = new StringBuilder();
        int counter = 0;
        for (byte b : data) {
            temp.append(b);
            if (temp.length() == 8) {
                result[counter++] = (byte) Integer.parseInt(temp.toString(), 2);
                temp.setLength(0);
            }
        }
        return result;
    }
}
