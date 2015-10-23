/**
 * COSC 2203-01 / Data Structures
 * Assignment 03 / "Huffman Coding"
 * Brian Scott
 *
 * 04 July 2015
 *
 * This class and its subclasses contain all features necessary
 * for describing Huffman trees. The abstract class is the base
 * lass for the tree itself, while its two subclasses, HData and HNode,
 * describe leaf nodes and non-leaf nodes, respectively.
 */
abstract class HTree implements Comparable<HTree> {
    protected final int freq;

    // Constructor
    HTree(int f) {
        freq = f;
    }

    // Overridden compareTo method for
    // comparing subtrees
    @Override
    public int compareTo(HTree ht) {
        return freq - ht.freq;
    }
}

// Class for leaf nodes
class HData extends HTree {
    public final char sym;

    // Constructor
    HData(int f, char c) {
        super(f);
        sym = c;
    }
}

// Class for non-leaf nodes
class HNode extends HTree {

    // subtrees
    public final HTree left, right;

    // Constructor
    HNode(HTree l, HTree r) {
        super(l.freq + r.freq);
        left = l;
        right = r;
    }
}

