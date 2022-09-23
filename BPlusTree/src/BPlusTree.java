import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BPlusTree<K extends Comparable<K>, V> {
    private BPlusNode<K, V> root;

    // maximum number of entries in the node
    private int order;

    // head node of linked list
    private BPlusNode<K, V> head;

    public BPlusNode<K, V> getRoot() {
        return root;
    }
    public void setRoot(BPlusNode<K, V> root) {
        this.root = root;
    }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order){ this.order = order; }

    public BPlusNode<K, V> getHead() {
        return head;
    }
    public void setHead(BPlusNode<K, V> head) {
        this.head = head;
    }


    public V get(K key) {
        return root.get(key);
    }

    public void insertOrUpdate(K key, V value) {
        root.insertOrUpdate(key, value, this);
    }

    BPlusTree(int order) {
        if (order < 3) {
            System.out.print("order must be greater than 2");
            System.exit(0);
        }
        this.order = order;
        root = new BPlusNode<>(true);
        head = root;
    }

    public void printBPlusTree() {
        Queue<BPlusNode<K,V>> queue = new LinkedList<>();
        queue.add(root);
        int level = -1;
        while(!queue.isEmpty()){
            int size = queue.size();
            level++;
            System.out.print("level: "+level+" keys: ");
            while(size-->0){
                BPlusNode<K,V> node = queue.poll();
                List<Map.Entry<K, V>> entries = node.getEntries();
                for(int i = 0; i<entries.size(); i++){
                    System.out.print(entries.get(i)+" ");
                }
                System.out.print(", ");
                if(node.isLeaf) continue;
                for(int i = 0; i<node.getChildren().size(); i++){
                    queue.add(node.getChildren().get(i));
                }
            }
            System.out.println("");
        }
    }

    public void printAscendingOrder(){
        System.out.println("Begin print the numbers in ascending order...");
        BPlusNode<K,V> p = head;
        while(p!=null){
            for(int i = 0; i<p.getEntries().size(); i++){
                System.out.print(p.getEntries().get(i).getKey());
                System.out.print(" ");
            }
            p = p.next;
        }
        System.out.print("\n");
    }
}
