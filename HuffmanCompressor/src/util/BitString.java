package util;

//BinaryString Class to generate Canonical Huffman Codes as String 
public class BitString {
    
    private char bits[];
    private int highest1BitIndex;
    
    public BitString() {
        this(8);
    }

    public BitString(int n) {
        bits=new char[n];
        for(int i=0;i<n;i++)
            bits[i]='0';
    }
    
    public void set(int pos) {
        if(pos>=bits.length) {
            resize(pos+1);
        }
        bits[pos]='1';
        if(pos>highest1BitIndex) highest1BitIndex=pos;
    }
    
    public void unset(int pos) {
        if(pos>=bits.length) return;
        bits[pos]='0';
    }
    
    public boolean isSet(int pos) {
        if(pos>=length())
            return false;
        return bits[pos]=='1';
    }
    
    public int get(int pos) {
        if(pos>=length())
            return 0;
        return bits[pos]-'0';
    }
    
    public BitString addOne() {
        add1(0);
        return this;
    }
    
    private void add1(int pos) {
        if(isSet(pos)) {
            unset(pos);
            add1(pos+1);
        } else {
            set(pos);
        }
    }
    
    public void add(BitString x) {
        for(int i=0;i<Math.min(x.length(),length());i++) {
            if(x.isSet(i)) {
                add1(i);
            }
        }
    }

    public void __leftShift__(int n) {
        int left=length()-1-highest1BitIndex;
        if(left<n) {
            bits=(repeat('0',n)+toStr().substring(0,highest1BitIndex+1)).toCharArray();
        } else {
            bits=(repeat('0',n)+(toStr().subSequence(0,length()-n))).toCharArray();
        }
        highest1BitIndex+=n;
//        bits=(repeat('0',n)+toStr()).toCharArray();
    }
    
    public void __rightShift__(int n) {
        bits=(toStr().substring(n,length())).toCharArray();
        highest1BitIndex=Math.max(highest1BitIndex-n,-1);
    }

    public void __AND__(BitString o) {
        for(int i=0;i<Math.max(length(),o.length());i++) {
            if(isSet(i)!=o.isSet(i)) unset(i);
        }
    }
    
    public void __OR__(BitString o) {
        for(int i=0;i<Math.max(length(),o.length());i++) {
            if(isSet(i)||o.isSet(i)) set(i);
        }
    }
    
    public void __XOR__(BitString o) {
        for(int i=0;i<Math.max(length(),o.length());i++) {
            if(isSet(i)==o.isSet(i)) unset(i);
            else set(i);
        }
    }
    
    private void resize(int n) {
        if(n>length()) {
            bits=(toStr()+repeat((char)(get(length())+'0'), n-bits.length)).toCharArray();
        } else {
            bits=(toStr().substring(0,n)).toCharArray();
            highest1BitIndex=-1;
            for(int i=n-1;i>-1;i--) {
                if(isSet(i)) {
                    highest1BitIndex=i;
                    break;
                }
            }
        }
    }
    
    public int length() {
        return bits.length;
    }
    
    //not using String.repeat() cause it needs Java >= 11
    private String repeat(char c,int n) {
        char s[]=new char[n];
        for(int i=0;i<n;i++) s[i]=c;
        return String.valueOf(s);
    }
    
    private char[] reversed() {
        int n=length();
        char temp[]=new char[n];
        for(int i=0;i<n;i++)
            temp[i]=bits[n-1-i];
        return temp;
    }
    
    private String toStr() {
        return String.valueOf(bits);
    }
    
    public String toString() {
        return String.valueOf(reversed());
    }

    public String toNbitString(int n) {
        int extras=n-length();
        if(extras<0) return toString().substring(-extras,length());
        return repeat('0',extras)+toString();
    }
    
}
