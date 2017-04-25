package com.jiadu.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */
public class StringBufferPool {

    private int poolNum;

    List<StringBuffer> lists= new ArrayList<StringBuffer>(10);

    public StringBufferPool(int poolNum) {

        this.poolNum = poolNum;

        for(int i=0; i<poolNum ; i++) {

            StringBuffer sb=new StringBuffer();

            lists.add(sb);
        }
    }

    public StringBufferPool() {

        this(10);
    }

    public synchronized StringBuffer get(){

        if(lists.size()>0){

            return lists.remove(0);
        }

        return new StringBuffer();

    }

    public synchronized void recycle(StringBuffer sb){

        sb.delete(0,sb.length());

        if(lists.size()<10){
            lists.add(sb);
        }else {
            sb=null;
        }
    }
}
