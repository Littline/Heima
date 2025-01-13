package com.hmdp;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class BcryptVerification {
    @Test
    void testBcrypt() {

        // 存储在数据库中的哈希密码
        String storedHash = "$2a$10$wwwzuhjqmMRPr6SnPhODu.wbfEBCx8wU062kW9x1wrYwKZGySdWcy";


        String password = "a5c6e78db67eff715afeac98de4a9b3c";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("Hashed password: " + hashedPassword);

        boolean passwordMatch = BCrypt.checkpw(password, storedHash);

        System.out.println("Password match: " + passwordMatch);
        System.out.println("Password match: " + BCrypt.checkpw("a5c6e78db67eff715afeac98de4a9b3c", "$2a$10$wwwzuhjqmMRPr6SnPhODu.wbfEBCx8wU062kW9x1wrYwKZGySdWcy"));
    }

    @Test
    void testMD5() {
        String input = "sujiuzheng";
        String input2="123456";
        String hexMd5=hex_md5(input);
        System.out.println("Hex MD5: " + hex_md5(input));
        System.out.println("Base64 MD5: " + b64_md5(input));
        System.out.println("123456Hex MD5: " + hex_md5(input2));
        String mysqlPassword = BCrypt.hashpw(hexMd5, BCrypt.gensalt());
        System.out.println("mysqlPassword: "+mysqlPassword);
    }

    // 使用小写十六进制返回MD5值
    public static String hex_md5(String input) {
        return bytesToHex(md5(input));
    }

    // 使用Base64返回MD5值
    public static String b64_md5(String input) {
        return bytesToBase64(md5(input));
    }

    // 计算MD5
    private static byte[] md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 将字节数组转换为Base64编码字符串
    private static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
