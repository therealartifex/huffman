/**
 * COSC 2203-01 / Data Structures
 * Assignment 03 / "Huffman Coding"
 * Brian Scott
 *
 * 04 July 2015
 *
 * This is the main class, which contains methods for building, decoding,
 * and traversing Huffman trees. First, it reads a list of strings via standard
 * input, removing any non-word characters. It then places the strings into
 * a Huffman tree by passing an array of each character's frequency to the
 * buildTree method. Once the tree is built, it outputs the list of characters,
 * their frequencies and encodings, the characters with the shortest and
 * longest encodings, and the height of the tree. It then asks the user to
 * input a Huffman-encoded string, and uses it to traverse the tree and output
 * the characters that match the encoding. The program continues to prompt for
 * encoded strings until the user types the word "quit".
 */
import java.util.*;
public class Driver {
    private static List<String> results;
    private static List<String> shortest;
    private static List<String> longest;
    private static int maxHeight = 0;
    private static int minHeight = 255;
    private static HTree mainTree;

    // Program entry point
    public static void main(String[] args) {

        // field initializations
        int[] charFreqs = new int[256];
        results = new ArrayList<>();
        longest = new ArrayList<>();
        shortest = new ArrayList<>();
        String input = "-1";
        String list = "";

        System.out.printf("Enter some strings. Type a blank line when finished.\n");
        Scanner sc = new Scanner(System.in);
        while (!input.equals("") && input.length() <= 80){
            input = sc.nextLine();
            list += input;
        }
        if (list.equals("")) System.exit(0);

        // read each character and record the frequencies,
        // ignoring all non-word characters
        for (char c : list.replaceAll("\\W", "").toLowerCase().toCharArray())
            charFreqs[c]++;

        // build tree
        mainTree = buildTree(charFreqs);

        // print out results
        System.out.println("Input analysis complete.\nFREQ\tCHAR\tCODE\n-----------------------");

        genCodes(mainTree, new StringBuffer());
        Collections.sort(results);
        results.forEach(System.out::println);
        System.out.printf("Unique chars in tree: %d\nTree Height: %d\nLongest Encoding: %s\nShortest Encoding: %s%n", results.size(), maxHeight, longest, shortest);

        // Prompt for encoded strings until "quit" is entered
        String code_str;
        while (true) {
            System.out.println("Enter a string to be decoded:");
            code_str = sc.nextLine();
            if (code_str.equals("quit")) break;
            System.out.println(decode(mainTree, code_str));
        }
        System.out.println("Program terminated");
    }

    // Input is an array of frequencies, indexed by character code
    public static HTree buildTree(int[] charFreqs) {
        MinHeap<HTree> trees = new MinHeap<>();

        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreqs.length; i++) if (charFreqs[i] > 0) trees.add(new HData(charFreqs[i], (char)i));

        assert trees.size() > 0;

        // loop until there is only one tree left
        while (trees.size() > 1) {

            // two trees with least freq
            HTree a = trees.extractMin();
            HTree b = trees.extractMin();

            // put into new node and re-insert into queue
            trees.add(new HNode(a, b));
        }
        return trees.extractMin();
    }

    // Generates Huffman codes, calculates maxHeight and minHeight of tree
    public static void genCodes(HTree tree, StringBuffer code) {
        assert tree != null;
        if (tree instanceof HData) {
            HData leaf = (HData)tree;
            if (code.length() > maxHeight) {
                maxHeight = code.length();
                longest.clear();
                longest.add(code + " = " + leaf.sym);
            }

            if (code.length() < minHeight) {
                minHeight = code.length();
                shortest.clear();
                shortest.add(code + " = " + leaf.sym);
            }

            // print out character, frequency, and code for this leaf
            results.add(leaf.freq + "\t" + leaf.sym + "\t" + code);
        } else if (tree instanceof HNode) {
            HNode node = (HNode)tree;

            // traverse left
            code.append('0');
            genCodes(node.left, code);
            code.deleteCharAt(code.length()-1);

            // traverse right
            code.append('1');
            genCodes(node.right, code);
            code.deleteCharAt(code.length()-1);
        }
    }

    // Decodes an encoded string
    public static String decode(HTree currTree, String codedString) {
        StringBuilder result = new StringBuilder();

        for (int i=0;i<codedString.length(); i++) {
            if (currTree instanceof HNode) {
                HNode node = (HNode)currTree;

                if (codedString.charAt(i) == '1') currTree = node.right;
                else currTree = node.left;
            }

            if (currTree instanceof HData) {
                HData leaf = (HData)currTree;
                result.append(leaf.sym);
                currTree = mainTree;
            }
        }
        return result.toString();
    }
}
