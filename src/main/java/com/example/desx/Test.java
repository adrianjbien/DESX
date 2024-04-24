package com.example.desx;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws Exception {
        byte[] key1 = {1,0,1,0,1,1,0,0,1,0,1,0,1,1,0,0,1,0,1,0,0,0,0,1,0,0,1,
                0,0,0,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,1,1,1,1,1,0,1,1,1,0,1,1,0,1,0,1,0,1,0};
        byte[] key2 = {0,0,1,0,1,1,0,0,1,0,1,0,1,1,0,0,1,0,1,0,0,0,0,1,0,0,1,
                0,0,0,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,1,1,1,1,1,0,1,1,1,0,1,1,0,1,0,1,0,1,0};
        byte[] key3 = {1,1,1,0,1,1,0,0,1,0,1,0,1,1,0,0,1,0,1,0,0,0,0,1,0,0,1,
                0,0,0,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,1,1,1,1,1,0,1,1,1,0,1,1,0,1,0,1,0,1,0};


        Converter converter = new Converter();
        Primitives primitives = new Primitives();
        DaoFile daoFile = new DaoFile();
        Operations operations = new Operations();
        byte[] byteMessage = daoFile.readFile("C:/Users/adria/OneDrive/Pulpit/polibuda/SEM4/KRYPTO/untitled/test.txt");
        byte[] bits = converter.convertFileToBytes(byteMessage);

        byte[][] blocks = operations.divideDataIntoBlocks(bits);

        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = operations.des(true,converter,key1,key2,key3,blocks[i],primitives);
        }

        byte[] result = new byte[blocks.length * 64];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < 64; j++) {
                result[i * 64 + j] = blocks[i][j];
            }
        }


        daoFile.writeFile(converter.convertBytesToFile(result) ,"C:/Users/adria/OneDrive/Pulpit/polibuda/SEM4/KRYPTO/untitled/test2.txt");

        byte[] read = daoFile.readFile("C:/Users/adria/OneDrive/Pulpit/polibuda/SEM4/KRYPTO/untitled/test2.txt");
        byte[] cos2 = converter.convertFileToBytes(read);
        blocks = operations.divideDataIntoBlocks(cos2);

        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = operations.des(false,converter,key1,key2,key3,blocks[i],primitives);
        }

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < 64; j++) {
                result[i * 64 + j] = blocks[i][j];
            }
        }
        daoFile.writeFile(converter.convertBytesToFile(result) ,"C:/Users/adria/OneDrive/Pulpit/polibuda/SEM4/KRYPTO/untitled/testde.txt");

    }
}
