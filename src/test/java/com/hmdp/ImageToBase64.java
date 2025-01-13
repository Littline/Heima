package com.hmdp;

import java.io.*;
import java.util.Base64;

public class ImageToBase64 {

    public static String convertImageToBase64(String imagePath) {
        try {
            // 读取图片文件
            File imageFile = new File(imagePath);
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            byte[] imageBytes = new byte[(int) imageFile.length()];
            fileInputStream.read(imageBytes);
            fileInputStream.close();

            // 将二进制数据转换为 Base64 字符串
            String base64String = Base64.getEncoder().encodeToString(imageBytes);

            // 返回 Base64 字符串
            return "data:image/png;base64," + base64String;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String imagePath = "F:\\File\\ai助教\\一期一店\\snow.png"; // 修改为实际图片路径
        String base64String = convertImageToBase64(imagePath);

        if (base64String != null) {
            System.out.println("Base64 Encoded String: " + base64String);
        }
    }
}

