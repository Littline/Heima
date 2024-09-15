package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
//先找到最基础解，然后逐次倒退，deque的size逐步减小
@Slf4j
public class Partition {

    List<List<String>> lists = new ArrayList<>();
    Deque<String> deque = new LinkedList<>();
    List<String> result =new ArrayList<>();
    StringBuilder path=new StringBuilder();
    public List<String> restoreIpAddresses(String s) {
        backTracingIP(s,0,0);
        log.info("IP result is: {}", result);
        return result;
    }
    @Test
    void testPartition() {

        // 分割回文串
        String s1 = "aab";
        String s2 = "a5c6e78db67e7ff715afeac98de4a9b3c";
        String s = "5525511135";
        String ss="0279245587303";
        restoreIpAddresses(ss);

//        partition(s1);
        partition(s2);

    }
    public List<List<String>> partition(String s) {
        backTracking(s, 0);
        return lists;
    }

    private void backTracking(String s, int startIndex) {
        //如果起始位置大于s的大小，说明找到了一组分割方案
        if (startIndex >= s.length()) {
            lists.add(new ArrayList(deque));
            return;
        }
        for (int i = startIndex; i < s.length(); i++) {
            //如果是回文子串，则记录
            if (isPalindrome(s, startIndex, i)) {
                String str = s.substring(startIndex, i + 1);
                deque.addLast(str);
            } else {
                continue;
            }
            //起始位置后移，保证不重复
            backTracking(s, i + 1);
            deque.removeLast();
        }
    }
    //判断是否是回文串
    private boolean isPalindrome(String s, int startIndex, int end) {
        for (int i = startIndex, j = end; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
        }
        return true;
    }
    public void backTracingIP(String s ,int index,int count){
        if(count==3&&isValid(s.substring(index))){
            path.append(s.substring(index));
            result.add(path.toString());
            path.delete(path.length() - (s.length() - index), path.length());
            return;
        }
        for(int i=index;i<s.length();i++){
            if(isValid(s.substring(index,i+1))){
                path.append(s.substring(index,i+1));
                path.append('.');
                backTracingIP(s,i+1,count+1);
                path.delete(path.length() - s.substring(index,i+1).length() - 1, path.length());
            }else{
                break;
            }
        }
    }
    private boolean isValid(String subS) {
        if (subS.isEmpty() || (subS.length() > 1 && subS.charAt(0) == '0')) {
            return false; // avoid numbers with leading zeros or empty string
        }
        try {
            int num = Integer.parseInt(subS);
            return num >= 0 && num <= 255;
        } catch (NumberFormatException e) {
            return false; // handle cases where parsing fails
        }
    }
}
