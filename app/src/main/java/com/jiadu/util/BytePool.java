package com.jiadu.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */
public class BytePool {

    private int poolNum;

    List<LinkedList<Byte>> lists= new ArrayList<LinkedList<Byte>>(poolNum);

    public BytePool(int poolNum) {

        this.poolNum = poolNum;

        for(int i=0; i<poolNum ; i++) {

            LinkedList<Byte> al=new LinkedList<Byte>();

            lists.add(al);
        }
    }

    public BytePool() {

        this(10);
    }

    public synchronized LinkedList<Byte> get(){

        if(lists.size()>0){

            return lists.remove(0);
        }

        return new LinkedList<Byte>();

    }

    public synchronized void recycle(LinkedList<Byte> sb){

        sb.clear();

        if(lists.size()<10){
            lists.add(sb);
        }else {
            sb=null;
        }
    }
}
