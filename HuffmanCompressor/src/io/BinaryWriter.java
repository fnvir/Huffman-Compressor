package io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryWriter {
    
    private BufferedOutputStream out;
    private int b; //8 bit buffer to store bits
    private int len; //number of bits written in the buffer
    
    public BinaryWriter(String name) {
        this(new File(name));
    }
    
    public BinaryWriter(File f) {
        try {
            out=new BufferedOutputStream(new FileOutputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    //flushes the 8-bit buffer
    private void write(int bt) throws IOException {
        out.write(bt);
        len=0;
        b=0;
    }
    
    //writes a single bit into the buffer
    public void writeBit(char bit) throws IOException {
        len++;
        if(bit=='1')
            b|=(1<<(8-len));
        if(len==8) 
            write(b);
    }
    
    //writes all bits from the binary-string
    public void writeNbits(String bits) throws IOException {
        for(int i=0;i<bits.length();i++)
            writeBit(bits.charAt(i));
    }
    
    // writes the last N bits from the 64-bit buffer: 'bt'
    public void writeNbits(int N,long bt) throws IOException {
//        if(N>64) throw new IllegalArgumentException("long cannot hold more than 64 bits");
        if(N>8) {
            writeNbits0(8,(int)(bt>>(N-8)));
            writeNbits(N-8,bt);
        } else {
            writeNbits0(N,(int)bt);
        }
    }
    
    // writes the last N bits from the int 'bt'
    public void writeNbits(int N,int bt) throws IOException {
//        if(N>32) throw new IllegalArgumentException("int cannot hold more than 32 bits");
        if(N>8) {
            writeNbits0(8,bt>>(N-8));
            writeNbits(N-8,bt);
        } else {
            writeNbits0(N,bt);
        }
    }
    
    // writes the last N bits into the 8-bit buffer
    // N: 1-based
    private void writeNbits0(int N,int bt) throws IOException {
//        if(N<0||N>8) throw new IllegalArgumentException(" 0<N<=8 ");
        int left=8-len;
        if(N>left) {
            int x=N-left;
            b|=(bt>>x);
            write(b);
//            writeNbits0(x,bt&((1<<x)-1));
            writeNbits0(x,bt);
        } else {
            b|=(bt<<(left-N))&0xff;
            len+=N;
            if(len==8) write(b);
        }
    }
    public void writeByte(int bt) throws IOException {
        if(len==0) write(bt);
        else writeNbits0(8,bt);
    }
    
    public void flush() throws IOException {
        while(len>0)
            writeBit('0');
    }
    
    public void close() throws IOException {
        flush();
        out.close();
        out=null;
    }
    
}
