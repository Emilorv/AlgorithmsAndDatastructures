package Ferdig.Komprimering;

/**
 * TreeNode class
 */
public class TreeNode {
    int frequency;
    TreeNode left;
    TreeNode right;
    TreeNode parent;

    /**
     * Constructor
     * @param fr int
     * @param l TreeNode
     * @param r TreeNode
     * @param f TreeNode
     */
    public TreeNode(int fr, TreeNode l, TreeNode r, TreeNode f){
        frequency = fr;
        left = l;
        right = r;
        parent = f;
    }

    /**
     * Finds the frequency
     * @return int
     */
    public int findFrequency() {
        return frequency;
    }

    /**
     * Sets the parent node
     * @param pare TreeNode
     */
    public void setParent(TreeNode pare){
        this.parent = pare;
    }
}
