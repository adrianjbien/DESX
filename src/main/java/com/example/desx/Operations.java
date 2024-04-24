package com.example.desx;

import java.util.ArrayList;

public class Operations {

    public byte[] permutation(byte[] data, ArrayList<Integer> indexes, boolean isReversedOrder) {
        byte[] permutedData = new byte[indexes.size()];
        for (int i = 0; i < permutedData.length; i++) {
            if (!isReversedOrder) {
                permutedData[i] = data[indexes.get(i) - 1];
            } else {
                permutedData[indexes.get(i) - 1] = data[i];
            }
        }
        return permutedData;
    }

    public byte[] xor(byte[] data1, byte[] data2) {
        byte[] result = new byte[data1.length];
        for (int i = 0; i < result.length; i++) {
            if (data1[i] == data2[i]) {
                result[i] = 0;
            } else {
                result[i] = 1;
            }
        }
        return result;
    }

    public byte[][] divide(byte[] data) {
        byte[][] result = new byte[2][data.length / 2];
        int counter = 0;
        for (int i = 0; i < data.length; i++) {
            if (i < data.length / 2) {
                result[0][i] = data[i];
            } else {
                result[1][counter] = data[i];
                counter++;
            }
        }
        return result;
    }

    // permutacja E robiaca z 32 bitow R (prawa czesc wiadomosci) 48 bitow ktore beda xorowane z podkluczem
    public byte[] funE(byte[] data, ArrayList<Integer> indexes) {
        return this.permutation(data, indexes, false);
    }

    public byte[] sBox(byte[] input, byte[][] box) {
        int i = input[0] * 2 + input[5];
        int j = input[1] * 8 + input[2] * 4 + input[3] * 2 + input[4];
        byte temp = box[i][j];
        byte[] result = new byte[4];
        for (int k = 3; k > -1; k--) {
            result[3 - k] = (byte) (temp < 1 << k ? 0 : 1);
            temp -= (byte) ((1 << k) * result[3 - k]);
        }
        return result;
    }

    public byte[] leftShift(byte[] half, int numberOfShifts) {
        byte[] temp = new byte[numberOfShifts];
        for (int l = 0; l < numberOfShifts; l++) {
            temp[l] = half[l];
        }

        int j;
        for (j = 0; j < half.length - numberOfShifts; j++) {
            half[j] = half[j + numberOfShifts];
        }

        for (int l = 0; l < numberOfShifts; l++) {
            half[j] = temp[l];
            j++;
        }
        return half;
    }

    public byte[] getKey(boolean forDES) {
        byte[] key;
        if (forDES) {
            key = new byte[56];
        } else {
            key = new byte[64];
        }
        for (int i = 0; i < key.length; i++) {
            key[i] = (byte) (Math.random() * 2);
        }
        return key;
    }

    public byte[][] divideDataIntoBlocks(byte[] loadedData) {
        int numberOfBlocks = (loadedData.length + 63) / 64;
        byte[][] result = new byte[numberOfBlocks][64];
        int counter = 0;

        for (int i = 0; i < numberOfBlocks; i++) {
            for (int j = 0; j < 64; j++) {
                int index = i * 64 + j;
                if (index < loadedData.length) {
                    result[i][j] = loadedData[index];
                } else {
                    counter++;
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    // firstHalf to pierwsza czesc permutacji PC-1, analogicznie secondHalf
    // n jest stale = 16
    public byte[][] keySelection(byte[] key, int n, ArrayList<Integer> firstHalf, ArrayList<Integer> secondHalf, ArrayList<Integer> PC2) {
        byte[][] subkeysSet = new byte[n][];
        // znalezienie startowych C i D z permutacji PC1 wykonanej na kluczu 64 bitowym
        byte[] subKey;
        byte[] C = this.permutation(key, firstHalf, false);
        byte[] D = this.permutation(key, secondHalf, false);

        int shifts;


        // w zaleznosci jaka jest iteracja rundy (n) tyle razy bedziemy musieli shiftowac (jedno lub dwukrotnie [shifts])
        for (int i = 0; i < n; i++) {
            if (i == 0 || i == 1 || i == 8 || i == 15) {
                shifts = 1;
                C = this.leftShift(C, shifts);
                D = this.leftShift(D, shifts);
            } else {
                shifts = 2;
                C = this.leftShift(C, shifts);
                D = this.leftShift(D, shifts);
            }
            // laczenie C i D
            byte[] concatenation = new byte[56];
            int counter = 0;
            for (int m = 0; m < 56; m++) {
                if (m < 28) {
                    concatenation[m] = C[m];
                } else {
                    concatenation[m] = D[counter++];
                }
            }

            // permutacja PC2 tworzaca 48 bitowy podklucz z 56 bitow C i D
            subKey = this.permutation(concatenation, PC2, false);
            subkeysSet[i] = subKey;
        }

        return subkeysSet;
    }

    public byte[][] iteration(byte[][] dividedData, byte[] key, int n, int it, ArrayList<Integer> ePerm, ArrayList<Integer> firstHalf, ArrayList<Integer> secondHalf, ArrayList<Integer> PC2, ArrayList<Integer> pPerm, byte[][][] sBoxes) {
        byte[][] result = new byte[2][32];


        // funkcja szyfrujaca f(R,K)
        byte[] eFunResult = this.funE(dividedData[1], ePerm);
        byte[][] subKeys = this.keySelection(key, n, firstHalf, secondHalf, PC2);
        byte[] KeyXorE = this.xor(eFunResult, subKeys[it]);


        // zamiana na 2D zeby przekazac wiersze sBoxom
        byte[][] temp = new byte[8][6];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                temp[i][j] = KeyXorE[i * 6 + j];
            }
        }
        // z tymi sBoxami mozna zrobic tak ze wrzuci sie je do 3 wymiarowej tablicy i kazdy indeks bedzie reprezentowac osobny 2D sbox
        byte[][] sBoxesResult = new byte[8][4];
        for (int i = 0; i < 8; i++) {
            sBoxesResult[i] = this.sBox(temp[i], sBoxes[i]);
        }

        // zamiana 2D tablicy na 1D - 32 bity
        byte[] tempOut = new byte[32];
        int counter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                tempOut[counter++] = sBoxesResult[i][j];
            }
        }

        byte[] postPPermutation = this.permutation(tempOut, pPerm, false);


        for (int i = 0; i < 32; i++) {
            // przypisanie L' = R
            result[0][i] = dividedData[1][i];
        }
        result[1] = this.xor(dividedData[0], postPPermutation);
        return result;
    }

    public byte[] des(boolean encoding, Converter converter, byte[] firstKey, byte[] secondKey, byte[] thirdKey, byte[] message, Primitives primitives) {
        byte[] mess;
        if (encoding) {
            mess = this.xor(message, firstKey);
        } else {
            // potem tutaj dodac jeszcze zeby z hexa konwertowalo do byte
            mess = this.xor(message, thirdKey);
        }


        byte[] initialPermutationMessage = this.permutation(mess, primitives.IP, false);

        byte[][] tempResult = this.divide(initialPermutationMessage);

        if (encoding) {
            for (int i = 0; i < 16; i++) {
                tempResult = this.iteration(tempResult, secondKey, 16, i, primitives.ePerm, primitives.firstHalf, primitives.secondHalf, primitives.PC2, primitives.pPerm, primitives.sBoxes);
            }
        } else {
            for (int i = 15; i >= 0; i--) {
                tempResult = this.iteration(tempResult, secondKey, 16, i, primitives.ePerm, primitives.firstHalf, primitives.secondHalf, primitives.PC2, primitives.pPerm, primitives.sBoxes);
            }
        }

        byte[] tempLeft = tempResult[0];
        tempResult[0] = tempResult[1];
        tempResult[1] = tempLeft;

        byte[] finalResult = new byte[64];
        int counter = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 32; j++) {
                finalResult[counter++] = tempResult[i][j];
            }
        }

        byte[] codedMessage = this.permutation(finalResult, primitives.iIP, false);
        if (encoding) {
            return this.xor(codedMessage, thirdKey);
        } else {
            return this.xor(codedMessage, firstKey);
        }
    }
}
