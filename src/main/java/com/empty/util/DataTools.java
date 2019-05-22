package com.empty.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class DataTools {

    public static String GetCurrDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
        return sdf.format(new Date());
    }

    public static String stringAdder(String ori, long adder) {
        Long num = Long.valueOf(ori);
        num += adder;
        return String.valueOf(num);
    }

    public static void WriteStringToFile2(String filePath, String str) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String encode(int value) {
        return Base64.getEncoder().encodeToString(String.valueOf(value).getBytes());
    }

    public static String decode(String code) {
        byte[] decodedCode = Base64.getDecoder().decode(code);
        return new String(decodedCode);
    }
}
