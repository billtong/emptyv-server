package com.empty.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class DataTools {

	/*
	 * 获得当前时间并返回恰当时间
	 */
	public static String GetCurrDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		return sdf.format(new Date());
	}

	/*
	 * 用于字符串+1
	 */
	public static String stringAdder(String ori, long adder) {
		Long num = Long.valueOf(ori);
		num += adder;
		return String.valueOf(num);
	}

	/*
	 * 用于日志记录
	 */
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

	/*
	 * 用于加密激活信息
	 */
	public static String encode(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes());
	}

	public static String encode(int value) {
		return Base64.getEncoder().encodeToString(String.valueOf(value).getBytes());
	}

	public static String decode(String code) {
		byte[] decodedCode = Base64.getDecoder().decode(code);
		String s = new String(decodedCode);
		return s;
	}

}
