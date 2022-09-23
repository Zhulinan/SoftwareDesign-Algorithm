import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class BPlusTreeTest {
    public static void main(String[] args) {

        int size = 1000000;
        int order = 5;

        BPlusTree<Integer, Integer> orderTree = new BPlusTree<>(order);
        testOrderInsert(size, order, orderTree);
        orderTree.printBPlusTree();
        orderTree.printAscendingOrder();
        testOrderSearch(orderTree, size);


        BPlusTree<Integer, Integer> randomTree = new BPlusTree<>(order);
        HashSet randomSet = testRandomInsert(size, order, randomTree);
        randomTree.printBPlusTree();
        randomTree.printAscendingOrder();
        testRandomSearch(size, randomTree, randomSet);

    }

    private static HashSet<Integer> testRandomInsert(int size, int order, BPlusTree<Integer, Integer> tree) {
        System.out.println("\nTest random insert " + size + " datas, of order:"
                + order);
        Random random = new Random();
        int randomNumber;
        long current = System.currentTimeMillis();
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            randomNumber = random.nextInt();
            System.out.print(randomNumber + " ");
            set.add(randomNumber);
            tree.insertOrUpdate(randomNumber, randomNumber);
        }
        long duration = System.currentTimeMillis() - current;
        System.out.println("time elpsed for duration: " + duration);
        return set;
    }

    private static void testOrderInsert(int size, int order, BPlusTree<Integer, Integer> tree) {
        System.out.println("\nTest order insert " + size + " datas, of order:"
                + order);
        long current = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            tree.insertOrUpdate(i, i);
        }
        long duration = System.currentTimeMillis() - current;
        System.out.println("time elpsed for duration: " + duration);
    }

    private static void testOrderSearch(BPlusTree<Integer, Integer> tree, int size) {
        System.out.println("Begin order search...");
        long current = System.currentTimeMillis();
        for (int j = 0; j < size; j++) {
            if (tree.get(j) == null) {
                System.err.println("get node error:" + j);
                break;
            }
            System.out.println("number: "+j+" , search result: "+tree.get(j));
        }
        long duration = System.currentTimeMillis() - current;
        System.out.println("time elpsed for duration: " + duration);
    }

    private static void testRandomSearch(int size, BPlusTree<Integer, Integer> tree, HashSet<Integer> set) {
        ArrayList<Integer> list = new ArrayList(set);
        Random random = new Random();
        int randomNumber;
        System.out.println("Begin random search...");
        long current = System.currentTimeMillis();
        for (int j = 0; j < size; j++) {
            randomNumber = random.nextInt();
            if(randomNumber%2==0){
                int randomIndex = new Random().nextInt(list.size());
                randomNumber = list.get(randomIndex);
            }
            if (tree.get(randomNumber) == null) {
                if(set.contains(randomNumber)) {
                    System.err.println("get node error:" + randomNumber);
                    break;
                }
            }
            System.out.println("number: "+randomNumber+" , search result: "+tree.get(randomNumber));

        }
        long duration = System.currentTimeMillis() - current;
        System.out.println("time elpsed for duration: " + duration);
    }
}
