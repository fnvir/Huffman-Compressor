package huffman;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import io.BinaryReader;
import io.BinaryWriter;

public class Compressor extends AbstractCompressor {
    
    private int[] freq;
    private String[] codes;
    
    public Compressor(String pathname) {
        this(new File(pathname));
    }
    
    public Compressor(File f) {
        this(f,null);
    }
    
    public Compressor(File f1,File f2) {
        inputFile=f1;
        outputFile=f2;
        inputLen=f1.length();
        codelen=new int[256];
        codes=new String[256];
        freq=new int[256];
    }
    
    @Override
    public void setOutputDir(String outDir) {
        String out=outDir+inputFile.getName()+".huff";
        setOutputFile(new File(out));
    }
    
    @Override
    protected Boolean doInBackground() throws Exception {
        setProgress(0);
        compress();
        return true;
    }
    
    public void compress() throws IOException {
        BinaryWriter bw=new BinaryWriter(outputFile);
        calcFreq(); //+6%
        setProgress(6);
        if(distinctBytes==1) {
            writeConfig(bw);
        } else {
            initialize(); //40%
            setProgress(40);
            writeConfig(bw); //5%
            setProgress(45);
            compress0(bw); //55%
        }
        setProgress(100);
        bw.close();
    }
    
    private void initialize() throws IOException {
        Node tree=generateTree(); //+8% 
        setProgress(14);
        generateCodes(tree,""); //+25%
        setProgress(39);
        freememory(tree); //+1%
    }

    private void calcFreq() throws IOException {
        BinaryReader br=new BinaryReader(inputFile);
        while(true) {
            try { freq[br.readByte()]++; } 
            catch(EOFException e) {break;}
        }
        br.close();
        for(int i=0;i<256;i++) {
            if(freq[i]>0) {
                distinctBytes++;
                if(i<minInx) minInx=i;
                if(i>maxInx) maxInx=i;
            }
        }
    }
    
    private Node generateTree() {
        PriorityQueue<Node> pq=new PriorityQueue<>();
        for(int i=0;i<256;i++)
            if(freq[i]>0)
                pq.add(new Node(i,freq[i]));
        while(pq.size()!=1)
            pq.add(new Node(pq.poll(),pq.poll()));
        return pq.poll();
    }
    
    private void generateCodes(Node root,String s) {
        if(root.b!=null) {
            codes[root.b]=s;
            codelen[root.b]=s.length();
            maxLen=Math.max(maxLen,s.length());
        }
        if(root.left!=null)
            generateCodes(root.left,s+"0");
        if(root.right!=null)
            generateCodes(root.right,s+"1");
    }
    
    private void writeConfig(BinaryWriter bw) throws IOException {
        if(distinctBytes==1) maxLen=getInputFileLength();
        int bitsNeeded=64-Long.numberOfLeadingZeros(maxLen);
        bw.writeByte(minInx);
        bw.writeByte(maxInx);
        bw.writeByte(bitsNeeded);
        if(distinctBytes==1) {
            bw.writeNbits(bitsNeeded,getInputFileLength());
        } else {
            for(int i=minInx;i<maxInx+1;i++) {
                bw.writeNbits(bitsNeeded,codelen[i]);
            }
            bw.flush();
            writeExtras(bw);
        }
    }
    
    private void writeExtras(BinaryWriter bw) throws IOException {
        long totalbits=3;
        for(int i=0;i<256;i++) totalbits+=(codelen[i]*freq[i]);
//        int extras=(int) (totalbits%8==0?0:8-totalbits%8);
        int extras=(int) (((totalbits+7)&-8)-totalbits);
        bw.writeNbits(3,extras);
        bw.writeNbits(extras,0);
    }
    
    private void compress0(BinaryWriter bw) throws IOException {
        CanonicalHuffman c=new CanonicalHuffman(codelen,distinctBytes,maxLen>64);
        Long canon[]=c.getCanonicalLong();
        String canonStr[]=c.getCanonicalString();
        BinaryReader br=new BinaryReader(inputFile);
        if(maxLen<=64)
            writeCanonical(br,bw,canon);
        else
            writeCanonicalStr(br,bw,canonStr);
//        int b;
//        while(true) {
//            try {b=br.readByte();}
//            catch(EOFException e) {break;}
//            if(c.getMaxLen()<=64)
//                bw.writeNbits(codelen[b],canon[b]);
//            else 
//                bw.writeNbits(canonStr[b]);
//        }
    }
    
    private void writeCanonical(BinaryReader br,BinaryWriter bw,Long canon[]) throws IOException {
        int b;
        while(true) {
            try {b=br.readByte();}
            catch(EOFException e) {break;}
            bw.writeNbits(codelen[b],canon[b]);
        }
    }

    private void writeCanonicalStr(BinaryReader br,BinaryWriter bw,String canon[]) throws IOException {
        int b;
        while(true) {
            try {b=br.readByte();}
            catch(EOFException e) {break;}
            bw.writeNbits(canon[b]);
        }
    }

}
