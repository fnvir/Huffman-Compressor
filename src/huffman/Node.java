package huffman;


class Node implements Comparable<Node> {
    
    Integer b;
    Integer freq;
    Node left,right;
    
    Node(){}
    
    Node(int b,int v) {
        this.b=b;
        freq=v;
    }
    
    Node(Node l,Node r) {
        freq=l.freq+r.freq;
        left=l;
        right=r;
    }
    
    void clear() {
        if(left!=null)
            left.clear();
        if(right!=null)
            right.clear();
        b=freq=null;
        left=right=null;
    }
    
    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.freq, o.freq);
    }
    
}
