import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> queries = new ArrayList<>();
}

public class AutocompleteSystem {

    TrieNode root = new TrieNode();
    HashMap<String, Integer> frequency = new HashMap<>();


    // Insert query into Trie
    public void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.queries.add(query);
        }
    }


    // Update frequency when searched
    public void updateFrequency(String query) {

        frequency.put(query, frequency.getOrDefault(query, 0) + 1);

        insert(query);
    }


    // Search suggestions for prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequency.get(b) - frequency.get(a));

        pq.addAll(node.queries);

        List<String> result = new ArrayList<>();

        int count = 0;

        while (!pq.isEmpty() && count < 10) {
            result.add(pq.poll());
            count++;
        }

        return result;
    }


    public static void main(String[] args) {

        AutocompleteSystem ac = new AutocompleteSystem();

        ac.updateFrequency("java tutorial");
        ac.updateFrequency("javascript");
        ac.updateFrequency("java download");
        ac.updateFrequency("java tutorial");

        System.out.println(ac.search("jav"));
    }
}