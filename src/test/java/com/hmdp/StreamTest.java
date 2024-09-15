package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.out;
@Slf4j
public class StreamTest {
    @Test
    public void parallel(){
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        log.info("parallel output--默认情况是并行处理");
        numbers.parallelStream().forEach(out::println);
        log.info("serial output--如果结果需要有序，无需并行输出");
        numbers.parallelStream().forEachOrdered(out::println);
    }

    //some methods for create stream
    @Test//1.单列集合
    public void streamList(){
        ArrayList<String> list=new ArrayList<>();
        Collections.addAll(list,"a","b","c","d");
        list.stream().forEach(s-> out.println(s));
    }
    @Test//2.双列集合
    public void streamHashMap(){
        HashMap<String,Integer> hm = new HashMap<>();
        hm.put("aaa",111);
        hm.put("bbb",222);
        hm.put("ccc",333);
        hm.keySet().stream().forEach(s -> out.println(s));
        hm.entrySet().stream().forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry));
    }

    @Test//3.数组
    public void streamArray(){
        int[] arr={1,2,3,4,5,6,7};
        String[] arrstring={"a","b","c"};
        Arrays.stream(arr).forEach(s-> out.println(s));
        Arrays.stream(arrstring).forEach(s-> out.println(s));
    }

    @Test//4.零散数据
    public void streamData(){
        Stream.of(1,2,3,4,5,6,7,8,9,10,11,12,13).forEach(s-> out.println(s));

        Stream.of("a","b","c","d").forEach(s-> out.println(s));
    }

    @Test//中间方法
    public void streamMethod(){
        ArrayList<String> list1=new ArrayList<>();
        Collections.addAll(list1,"张无忌","张强","张翠山","张良","麻了","谢了坤");
        list1.stream()
                .filter(s->s.startsWith("张"))
                .filter(s->s.length()==2)
                .skip(0)
                .limit(5)
                .forEach(s -> out.println(s));
    }

    @Test//终结方法
    public void endMethod(){
        ArrayList<String> list1=new ArrayList<>();
        Collections.addAll(list1,"张无忌","张强","张翠山","张良","麻了","谢了坤");
        Object[] obj= list1.stream()
                .filter(s->s.startsWith("张"))
                .filter(s->s.length()==2)
                .skip(0)
                .limit(5)
                .toArray();
        out.println(Arrays.toString(obj));

        String[] str=list1.stream().toArray(value -> new String[value]);
        out.println(Arrays.toString(str));
    }
}
