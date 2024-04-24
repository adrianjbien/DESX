package com.example.desx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

public class DaoFile {

    public byte[] readFile(String filePath) throws Exception
    {
        FileInputStream fis = new FileInputStream(filePath);
        int counter = fis.available();
        byte[] data = new byte[counter];
        fis.read(data);
        fis.close();
        return data;
    }

    public void writeFile(byte[] data, String filePath) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }
}
