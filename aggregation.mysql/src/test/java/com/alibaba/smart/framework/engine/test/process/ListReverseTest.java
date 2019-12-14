package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 高海军 帝奇 74394 on 2017 April  22:10.
 */
public class ListReverseTest {

    @Test
    public void reverse_empty() {
        List<String> list = new ArrayList<String>();

        int size = list.size();

        assertListReverse(list, size);


    }


    @Test
    public void reverse1() {
        List<String> list = new ArrayList<String>();

        list.add("1");

        int size = list.size();
        assertListReverse(list, size);


    }

    @Test
    public void reverse3() {
        List<String> list = new ArrayList<String>();

        list.add("1");
        list.add("2");
        list.add("3");


        int size = list.size();
        assertListReverse(list, size);


    }





    private void assertListReverse(List<String> list, int size) {
        for (int i = size - 1; i >= 0; i--) {
            String actual = list.get(i);
            System.out.println(actual);

            int temp = i + 1;
            String expected = temp + "";

            Assert.assertEquals(expected, actual);
        }
    }
}
