package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高海军 帝奇 74394 on 2017 April  22:10.
 */
public class ListReverseTest {

    @Test
   public void reverse(){
        List<String> list = new ArrayList<String>();

        list.add("1");
        list.add("2");
        list.add("3");


        int size = list.size();
        for (int i = size-1; i>=0;i--) {
            String abc = list.get(i);

            //FIXME
            System.out.println(abc);
        }



    }
}
