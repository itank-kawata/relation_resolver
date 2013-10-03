package jp.mytools.relationsearch.utils;

public class BytesUtil {

	public static String convertBytesToHexStr(byte[] bytes) {
		String hex = "";
		for (byte b : bytes) {
			// 16進数変換
			String out = String.format("%02x", b);
			hex += out;
		}
		return hex;
	}
	
}
