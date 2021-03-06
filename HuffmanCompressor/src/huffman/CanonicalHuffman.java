package huffman;

import util.BitString;

class CanonicalHuffman {
    
    private static class Entry implements Comparable<Entry> {
        int b,len; //unsigned byte b and its bit length
        public Entry(int b,int len) {
            this.b=b;
            this.len=len;
        }
        public int compareTo(Entry o) {
            int x=Integer.compare(this.len, o.len);
            return x==0?Integer.compare(b,o.b):x;
        }
    }
    
    private Long canon[]; //stores the canonical code if the max codelen<=64
    private String canonStr[]; //stores the canonical codes as string
    private Entry entries[]; //Array of all bytes in the File with their Huffman code length

    
    //size - number of unique bytes in the file
    // asBitString - to generate canonical codes as String
    public CanonicalHuffman(int[] codelen,int size,boolean asBitString) {
        entries=new Entry[size];
        for(int bt=0,i=0;bt<256;bt++)
            if(codelen[bt]>0)
                entries[i++]=new Entry(bt,codelen[bt]);
        java.util.Arrays.sort(entries);
        canonize(asBitString);
    }
    
    private void canonize(boolean asStr) {
        if(asStr) canonizeString();
        else canonizeLong();
    }
    
    private void canonizeLong() {
        canon=new Long[256];
        long code=-1;
        int len=0;
        for(Entry x:entries) {
            canon[x.b]=code=(code+1)<<(x.len-len);
            len=x.len;
        }
    }
    
    private void canonizeString() {
        canonStr=new String[256];
        BitString pre=new BitString(entries[0].len);
        canonStr[entries[0].b]=pre.toString();
        for(int i=1;i<entries.length;i++) {
            int x=entries[i].len, y=entries[i-1].len;
            pre.addOne().__leftShift__(x-y);
            canonStr[entries[i].b]=pre.toNbitString(x);
        }
    }
    
    public Long[] getCanonicalLong() {
        return canon;
    }
    
    public String[] getCanonicalString() {
        return canonStr;
    }
    
}