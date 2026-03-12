import java.util.*;

class Transaction {

    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class ProblemVariant {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                System.out.println("Pair: " + map.get(complement).id + ", " + t.id);
            }

            map.put(t.amount, t);
        }
    }

    // Two Sum within 1 hour window
    public void findTwoSumTimeWindow(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= 3600000) {
                    System.out.println("TimeWindow Pair: " + prev.id + ", " + t.id);
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            if (map.get(key).size() > 1) {
                System.out.println("Duplicate: " + map.get(key));
            }
        }
    }

    // K-Sum (simple recursion)
    public void findKSum(int k, int target) {
        kSumHelper(new ArrayList<>(), 0, k, target);
    }

    private void kSumHelper(List<Transaction> current, int index, int k, int target) {

        if (k == 0 && target == 0) {
            System.out.println("K-Sum Found: " + current);
            return;
        }

        if (k == 0 || index >= transactions.size())
            return;

        Transaction t = transactions.get(index);

        current.add(t);
        kSumHelper(current, index + 1, k - 1, target - t.amount);

        current.remove(current.size() - 1);
        kSumHelper(current, index + 1, k, target);
    }

    public static void main(String[] args) {

        ProblemVariant pv = new ProblemVariant();

        pv.addTransaction(new Transaction(1, 500, "StoreA", "acc1", System.currentTimeMillis()));
        pv.addTransaction(new Transaction(2, 300, "StoreB", "acc2", System.currentTimeMillis()));
        pv.addTransaction(new Transaction(3, 200, "StoreC", "acc3", System.currentTimeMillis()));

        pv.findTwoSum(500);
        pv.findKSum(3, 1000);
        pv.detectDuplicates();
    }
}