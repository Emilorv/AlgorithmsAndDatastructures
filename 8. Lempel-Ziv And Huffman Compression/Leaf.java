package Ferdig.Komprimering;

/**
 * Leaf class which extends TreeNode
 * Has element
 */
public class Leaf extends TreeNode{
    char element;

    /**
     * Constructor
     * @param e char
     * @param fr int
     * @param l TreeNode
     * @param r TreeNode
     * @param f TreeNode
     */
    public Leaf(char e, int fr, TreeNode l, TreeNode r, TreeNode f) {
        super(fr, l, r, f);
        this.element = e;
    }

    /**
     * Finds the element
     * @return char
     */
    public char findKey() {
        return element;
    }
}
