package io;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BinaryReader {
    
    private BufferedInputStream in;
    private int b; //single unsigned-byte buffer
    private int len; //number of bits read from the byte buffer
    
    public BinaryReader(String filename) throws FileNotFoundException {
        this(new File(filename));
    }
    
    public BinaryReader(File f) throws FileNotFoundException {
        in=new BufferedInputStream(new FileInputStream(f));
    }
    
    // Reads a single unsigned byte into the 8bit buffer
    private int read() throws IOException {
        b=in.read();
        if(b<0)
            throw new EOFException();
        return b;
    }
    
    //reads a single bit
    public int readBit() throws IOException {
        return readNbits(1);
    }
    
    //reads N bits from the byte buffer 'b'
    public int readNbits(int N) throws IOException {
        if(N<0||N>8) throw new IllegalArgumentException("0<N<=8");
        if(len==0) read();
        int bt;
        int left=8-len;
        if(N>left) {
            len=0;
            int x=N-left;
            bt=(b<<x)|readNbits(x);
        } else {
            bt=b>>(left-N);
            len+=N;
            b&=255>>len;
//            b&=((1<<(left-N))-1);
            if(len==8) len=0;
        }
        return bt;
    }
    
    public int readByte() throws IOException {
        return len==0?read():readNbits(8);
    }
    
    public void flush() {
        b=0;
        len=0;
    }
    
    public void close() throws IOException {
        flush();
        in.close();
    }
    
}
