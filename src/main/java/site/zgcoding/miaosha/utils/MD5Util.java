package site.zgcoding.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author Zg.Li · 2020/4/19
 */
public class MD5Util {

	private static final String salt = "1a2b3c4d";

	public static String encrypt(String src) {
		return DigestUtils.md5Hex(src);
	}

	public static String inputPwdToFormPwd(String inputPwd) {
		String processSrc = salt.charAt(5) + salt.charAt(0) + inputPwd + salt.charAt(1) + salt.charAt(4);
		return encrypt(processSrc);
	}

	public static String formPwdToDBPwd(String formPwd, String salt) {
		String processSrc = salt.charAt(5) + salt.charAt(0) + formPwd + salt.charAt(1) + salt.charAt(4);
		return encrypt(processSrc);
	}

	public static String inputPwdToDBPwd(String inputPwd, String saltDB) {
		String formPwd = inputPwdToFormPwd(inputPwd);
		return formPwdToDBPwd(formPwd, saltDB);
	}

	public static void main(String[] args) {
//		System.out.println(inputPwdToFormPwd("123456"));
//		System.out.println(formPwdToDBPwd("123456", salt));
		System.out.println(inputPwdToDBPwd("123456", salt));
	}
}
