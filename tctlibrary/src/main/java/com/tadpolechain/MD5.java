package com.tadpolechain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MD5 {
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', 
		'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	private static char password[];

	private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        if(null != password && 16 == password.length){
        	for (int i = 0; i < b.length; i++) { 
                sb.append(password[(b[i] & 0xf0) >>> 4]);
                sb.append(password[b[i] & 0x0f]);
            }
        } else {
        	for (int i = 0; i < b.length; i++) { 
                sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
                sb.append(HEX_DIGITS[b[i] & 0x0f]);  
            }
        }
        return sb.toString();
	}
	
	/**
	 * MD5加密算法
	 * @param s 要加密的字符串
	 * @return 加密后的字符串（32位），发生错误则返回空字符串
	 */
	public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	/**
	 * MD5加密算法
	 * @param s 要加密的字符串
	 * @return 加密后的字符串（16位），用于标记本地数据
	 */
	public static String md5Short(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest).substring(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

	/**
	 * 设置加密序列，默认的是0-F
	 * 设置的加密序列必须是长度为16的字符数组，否则不起作用
	 * @param password 加密序列
	 */
	public static void setPassword(char[] password) {
		MD5.password = password;
	}

    public static boolean isChinaPhone(String str) throws PatternSyntaxException {
        String regExp = "^(1)\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isOtherPhone(String str, int num) throws PatternSyntaxException {
        String regExp = "^\\d{"+num+"}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
