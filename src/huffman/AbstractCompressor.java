package huffman;

import java.io.File;
import javax.swing.SwingWorker;

public abstract class AbstractCompressor extends SwingWorker<Boolean,Integer> {
    
    protected File inputFile,outputFile;
    protected Long inputLen,outputLen;
    protected int[] codelen;
    
    
    public abstract void setOutputDir(String outDir);

    public void setOutputFile(File f) {
        outputFile=f;
    }
    
    public File getInputFile() {
        return inputFile;
    }
    
    public File getOutputFile() {
        return outputFile;
    }
    
    public Long getInputFileLength() {
        return inputLen;
    }
    
    public Long getOutputFileLength() {
        return outputLen;
    }
    
    protected void freememory(Node tree) {
        tree.clear();
        System.gc();
    }
    
    @Override
    protected void done() {
        outputLen=outputFile.length();
    }
    
}
