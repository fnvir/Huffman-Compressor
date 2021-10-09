package huffman;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import io.BinaryReader;
import io.BinaryWriter;

public class Decompressor extends AbstractCompressor {
    
    public Decompressor(String pathname) {
        this(new File(pathname));
    }
    
    public Decompressor(File f) {
        this(f,null);
    }
    
    public Decompressor(File f1,File f2)  {
        inputFile=f1;
        outputFile=f2;
        inputLen=f1.length();
        codelen=new int[256];
    }
    
    @Override
    public void setOutputDir(String outDir) {
//        String out=outDir+(inputFile.getName().replaceFirst("[.][^.]+$",""));
        String out=outDir+(inputFile.getName().replace(".huff",""));
        setOutputFile(new File(out));
    }
    
    @Override
    protected Boolean doInBackground() throws Exception {
        setProgress(0);
        decompress();
        return true;
    }
    
    public void decompress() throws IOException {
        BinaryReader br=new BinaryReader(inputFile);
        readConfig(br); //10%
//        setProgress(10);
        firePropertyChange("progress",0,10);
        if(distinctBytes==1) {
            decompress1Distinct();
        } else {
            Node tree=regenerateTree(); //30%
//            setProgress(40);
            firePropertyChange("progress",10,40);
            decompress0(br,tree); //59%
//            setProgress(97);
            firePropertyChange("progress",40,96);
            freememory(tree);
        }
        br.close();
//        setProgress(100);
        firePropertyChange("progress",96,100);
    }
    
    private void readConfig(BinaryReader br) throws IOException {
        minInx=br.readByte();
        maxInx=br.readByte();
        int bitsNeeded=br.readByte();
        if(minInx==maxInx) distinctBytes=1;
        if(distinctBytes==1) {
            outputLen=br.readNbitsLong(bitsNeeded);
        } else {
            for(int i=minInx;i<maxInx+1;i++) {
                codelen[i]=br.readNbits(bitsNeeded);
                if(codelen[i]>0) {
                    distinctBytes++;
                    maxLen=Math.max(maxLen,codelen[i]);
                }
            }
            br.flush();
            br.readNbits(br.readNbits(3));  //discard the extra bits
        }
    }
    
    private Node regenerateTree() {
        CanonicalHuffman c=new CanonicalHuffman(codelen,distinctBytes,maxLen>64);
        Long canon[]=c.getCanonicalLong();
        String canonStr[]=c.getCanonicalString();
        Node tree=new Node();
        for(int i=0;i<codelen.length;i++) {
            if(codelen[i]>0) {
                if(maxLen<=64)
                    addNode(tree,i,codelen[i],canon[i]);
                else
                    addNode(tree,i,0,canonStr[i]);
            }
        }
        return tree;
    }
    
    private void addNode(Node root,int b,int len,long canon) {
        if(len==0) {
            root.b=b;
            return;
        }
        if((canon&(1L<<(len-1)))==0) {
            if(root.left==null) root.left=new Node();
            addNode(root.left,b,len-1,canon);
        } else {
            if(root.right==null) root.right=new Node();
            addNode(root.right,b,len-1,canon);
        }
    }
    
    private void addNode(Node root,int b,int inx,String canon) {
        if(inx==canon.length()) {
            root.b=b;
            return;
        }
        if(canon.charAt(inx)=='0') {
            if(root.left==null) root.left=new Node();
            addNode(root.left,b,inx+1,canon);
        } else {
            if(root.right==null) root.right=new Node();
            addNode(root.right,b,inx+1,canon);
        }
    }
    
    private void decompress1Distinct() throws IOException {
        BinaryWriter bw=new BinaryWriter(outputFile);
        for(int i=0;i<getOutputFileLength();i++)
            bw.writeByte(minInx);
        bw.close();
    }
    
    private void decompress0(BinaryReader br, Node tree) throws IOException {
        Node curr=tree;
        int b;
        BinaryWriter bw=new BinaryWriter(outputFile);
        while(true) {
            try {b=br.readBit();}
            catch(EOFException e) {break;}
            if(b==0) curr=curr.left;
            else curr=curr.right;
            if(curr.b!=null) {
                bw.writeByte(curr.b);
                curr=tree;
            }
        }
        bw.close();
    }

}
