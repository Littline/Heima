package com.hmdp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.lang.model.element.VariableElement;
import javax.management.monitor.MonitorSettingException;
import javax.print.attribute.standard.DateTimeAtCompleted;

import org.junit.Test;

public class MyTest {
    public static void main(String[] args) throws ParseException {

        long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);
        String url = "http://www.lokapi.cn/smsUTF8.aspx";

//        String beforSign = "action=sendtemplate&username=17633815409&password="+getMD5String("Su9zheng")+"&token=b97b353e&timestamp="+timestamp;
//        String postData = "action=sendtemplate&username=17633815409&password="+getMD5String("Su9zheng")+"&token=b97b353e&templateid=63C794DE&param=17786426863|广西连云港27电信&rece=json&timestamp="+timestamp+"&sign="+getMD5String(beforSign);

        String beforSign = "action=sendtemplate&username=17633815409&password="+getMD5String("Su9zheng")+"&token=b97b353e&timestamp="+timestamp;
        String postData = "action=sendtemplate&username=17633815409&password="+getMD5String("Su9zheng")+"&token=b97b353e&templateid=63C794DE&param=17396154393|广西连云港27@18071045365|深圳妈湾56&rece=json&timestamp="+timestamp+"&sign="+getMD5String(beforSign);

        String result = sendPost(url,postData);

        System.out.println(result);

    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }




    public static String getMD5String(String rawString){    //用来计算MD5的函数
        String[] hexArray = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(rawString.getBytes());
            byte[] rawBit = md.digest();
            String outputMD5 = " ";
            for(int i = 0; i<16; i++){
                outputMD5 = outputMD5+hexArray[rawBit[i]>>>4& 0x0f];
                outputMD5 = outputMD5+hexArray[rawBit[i]& 0x0f];
            }
            return outputMD5.trim();
        }catch(Exception e){
            System.out.println("计算MD5值发生错误");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成秘钥
     *
     * @param tm
     * @param key
     * @return
     */
    public static String createSign(TreeMap<String, String> tm, String key) {
        StringBuffer buf = new StringBuffer(key);
        for (Map.Entry<String, String> en : tm.entrySet()) {
            String name = en.getKey();
            String value = en.getValue();
            if (!"sign".equals(name) && !"param".equals(name) && value != null && value.length() > 0 && !"null".equals(value)) {
                buf.append(name).append('=').append(value).append('&');
            }
        }
        String _buf = buf.toString();
        return _buf.substring(0, _buf.length() - 1);
    }

    /**
     * 将文件转成base64 字符串
     * @param
     * @return  *
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);;
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        //return new BASE64Encoder().encode(buffer);
        return "";
    }


}
