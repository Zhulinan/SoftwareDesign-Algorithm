import java.util.*;

public class BPlusNode<K extends Comparable<K>, V> {
    protected boolean isLeaf;

    private BPlusNode<K, V> parent;

    // pre node for leaf node
    private BPlusNode<K, V> previous;

    // next node for leaf node
    protected BPlusNode<K, V> next;

    // map for node values, key for index, v for true value
    private List<Map.Entry<K, V>> entries;

    private List<BPlusNode<K, V>> children;

    public boolean getIsLeaf() { return isLeaf; }
    public void setIsLeaf(boolean isLeaf) { this.isLeaf = isLeaf; }

    public BPlusNode<K, V> getParent() { return parent; }
    public void setParent(BPlusNode<K, V> parent) { this.parent = parent; }

    public BPlusNode<K, V> getPrevious() { return previous; }
    public void setPrevious(BPlusNode<K, V> previous) { this.previous = previous; }

    public BPlusNode<K, V> getNext() { return next; }
    public void setNext(BPlusNode<K, V> next) { this.next = next; }

    public List<Map.Entry<K, V>> getEntries() {
        return entries;
    }
    public void setEntries(List<Map.Entry<K, V>> entries) {
        this.entries = entries;
    }

    public List<BPlusNode<K, V>> getChildren(){
        return children;
    }
    public void setChildren(List<BPlusNode<K, V>> children) {
        this.children = children;
    }


    protected BPlusNode(boolean isLeaf) {
        this.isLeaf = isLeaf;
        entries = new ArrayList<>();

        if (!isLeaf) {
            children = new ArrayList<>();
        }
    }

    //get value inside the node
    public V get(K key) {
        if (isLeaf) {
            int low = 0, high = entries.size() - 1, mid;
            int comp;
            while (low <= high) {
                mid = (low + high) / 2;
                comp = entries.get(mid).getKey().compareTo(key);
                if (comp == 0) {
                    return entries.get(mid).getValue();
                } else if (comp < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
            return null;
        }
        //if the node is not a leaf node
        //if the value is less then the left key, search through the first child node
        if (key.compareTo(entries.get(0).getKey()) < 0) {
            return children.get(0).get(key);
            //if the value is bigger than the right key, search through the last child node
        } else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
            return children.get(children.size() - 1).get(key);
            //search through the last node bigger than the value
        } else {
            int low = 0, high = entries.size() - 1, mid;
            int comp;
            while (low <= high) {
                mid = (low + high) / 2;
                comp = entries.get(mid).getKey().compareTo(key);
                if (comp == 0) {
                    return children.get(mid + 1).get(key);
                } else if (comp < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
            return children.get(low).get(key);
        }
    }

    public void insertOrUpdate(K key, V value, BPlusTree<K, V> tree) {
        if (isLeaf) {
            //no need to split, directly insert or update
            if (contains(key) != -1 || entries.size() < tree.getOrder()) {
                insertOrUpdate(key, value);
                return;
            }
            //need split
            BPlusNode<K, V> left = new BPlusNode<>(true);
            BPlusNode<K, V> right = new BPlusNode<>(true);
            if (previous != null) {
                previous.next = left;
                left.previous = previous;
            }
            if (next != null) {
                next.previous = right;
                right.next = next;
            }
            if (previous == null) {
                tree.setHead(left);
            }

            left.next = right;
            right.previous = left;
            previous = null;
            next = null;

            //insert new key-value and copy old key-value to new nodes
            copy2Nodes(key, value, left, right, tree);

            //if it's not a root node
            if (parent != null) {
                int index = parent.children.indexOf(this);
                parent.children.remove(this);
                left.parent = parent;
                right.parent = parent;
                parent.children.add(index, left);
                parent.children.add(index + 1, right);
                parent.entries.add(index, right.entries.get(0));
                entries = null;
                children = null;

                parent.updateInsert(tree);
                parent = null;
            } else {// if it's a root node
                BPlusNode<K, V> parent = new BPlusNode<>(false);
                tree.setRoot(parent);
                left.parent = parent;
                right.parent = parent;
                parent.children.add(left);
                parent.children.add(right);
                parent.entries.add(right.entries.get(0));
                entries = null;
                children = null;
            }
            return;

        }
        //if it's not a leaf node
        if (key.compareTo(entries.get(0).getKey()) < 0) {
            children.get(0).insertOrUpdate(key, value, tree);
        } else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
            children.get(children.size() - 1).insertOrUpdate(key, value, tree);
        } else {
            int low = 0, high = entries.size() - 1, mid;
            int comp;
            while (low <= high) {
                mid = (low + high) / 2;
                comp = entries.get(mid).getKey().compareTo(key);
                if (comp == 0) {
                    children.get(mid + 1).insertOrUpdate(key, value, tree);
                    break;
                } else if (comp < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
            if (low > high) {
                children.get(low).insertOrUpdate(key, value, tree);
            }
        }
    }

    private void copy2Nodes(K key, V value, BPlusNode<K, V> left,
                            BPlusNode<K, V> right, BPlusTree<K, V> tree) {
        int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
        boolean b = false;// to indicate whether the key-value has been inserted or not
        for (int i = 0; i < entries.size(); i++) {
            if (leftSize != 0) {
                leftSize--;
                copy2NodeResponse response = copy2Node(key, value, left, b, i);
                i = response.i;
                b = response.b;
            } else {
                copy2NodeResponse response = copy2Node(key, value, right, b, i);
                i = response.i;
                b = response.b;
            }
        }
        if (!b) {
            right.entries.add(new AbstractMap.SimpleEntry<>(key, value));
        }
    }

    private class copy2NodeResponse{
        private boolean b;
        private int i;
        private copy2NodeResponse(boolean b, int i){
            this.b = b;
            this.i = i;
        }
    }
    private copy2NodeResponse copy2Node(K key, V value, BPlusNode<K, V> node, boolean b, int i){
        if (!b && entries.get(i).getKey().compareTo(key) > 0) {
            node.entries.add(new AbstractMap.SimpleEntry<>(key, value));
            b = true;
            i--;
        } else {
            node.entries.add(entries.get(i));
        }
        return new copy2NodeResponse(b,i);
    }

    //update for node after some node insert in the middle
    private void updateInsert(BPlusTree<K, V> tree) {

        //if the number of children larger then the order number, the node should be split
        if (children.size() > tree.getOrder()) {
            BPlusNode<K, V> left = new BPlusNode<>(false);
            BPlusNode<K, V> right = new BPlusNode<>(false);
            //set the length of each side
            int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
            int rightSize = (tree.getOrder() + 1) / 2;
            //copy nodes to new nodes, and update the entries
            for (int i = 0; i < leftSize; i++) {
                left.children.add(children.get(i));
                children.get(i).parent = left;
            }
            for (int i = 0; i < rightSize; i++) {
                right.children.add(children.get(leftSize + i));
                children.get(leftSize + i).parent = right;
            }
            for (int i = 0; i < leftSize - 1; i++) {
                left.entries.add(entries.get(i));
            }
            for (int i = 0; i < rightSize - 1; i++) {
                right.entries.add(entries.get(leftSize + i));
            }

            // if is not a root node
            if (parent != null) {
                int index = parent.children.indexOf(this);
                parent.children.remove(this);
                left.parent = parent;
                right.parent = parent;
                parent.children.add(index, left);
                parent.children.add(index + 1, right);
                parent.entries.add(index, entries.get(leftSize - 1));
                entries = null;
                children = null;

                parent.updateInsert(tree);
                parent = null;
            } else {//if it's a root node
                BPlusNode<K, V> parent = new BPlusNode<>(false);
                tree.setRoot(parent);
                left.parent = parent;
                right.parent = parent;
                parent.children.add(left);
                parent.children.add(right);
                parent.entries.add(entries.get(leftSize - 1));
                entries = null;
                children = null;
            }
        }
    }


    // To check whether the node contains this key value
    private int contains(K key) {
        int low = 0, high = entries.size() - 1, mid;
        int comp;
        while (low <= high) {
            mid = (low + high) / 2;
            comp = entries.get(mid).getKey().compareTo(key);
            if (comp == 0) {
                return mid;
            } else if (comp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    // Insert or update key-value to the node
    protected void insertOrUpdate(K key, V value) {
        int low = 0, high = entries.size() - 1, mid;
        int comp;
        while (low <= high) {
            mid = (low + high) / 2;
            comp = entries.get(mid).getKey().compareTo(key);
            if (comp == 0) {
                entries.get(mid).setValue(value);
                break;
            } else if (comp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (low > high) {
            entries.add(low, new AbstractMap.SimpleEntry<>(key, value));
        }
    }
}
