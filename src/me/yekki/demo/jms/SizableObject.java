package me.yekki.demo.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class SizableObject implements Serializable{

    private static final long serialVersionUID = 1L;

    List<byte[]> payload = new ArrayList<byte[]>();

    public SizableObject(List<byte[]> payload) {
        this.payload = payload;
    }

    public int size(){
        return payload.size();
    }

    public static SizableObject buildObject(long sizeInKB) {

        List<byte[]> payload = new ArrayList<byte[]>();

        LongStream.range(0, sizeInKB).forEach(i->payload.add(new byte[1024]));

        return new SizableObject(payload);
    }
}
