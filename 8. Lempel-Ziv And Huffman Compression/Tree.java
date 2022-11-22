package Ferdig.Komprimering;

/**
 * Tree class
 */
public class Tree {
    TreeNode root;

    /**
     * Constructor
     */
    public Tree(){
        root = null;
    }

    /**
     * Constructor
     */
    public Tree(TreeNode r){
        root = r;
    }

    /**
     * Returns true or false if it is empty or not
     * @return boolean
     */
    public boolean empty(){
        return root == null;
    }

    /**
     * Finds the depth of a node
     * @param n TreeNode
     * @return int
     */
    private int findDepth(TreeNode n){
        int d = -1;
        while (n!=null){
            d++;
            n = n.parent;
        }
        return d;
    }

    /**
     * Finds the height of a node
     * @param n TreeNode
     * @return int
     */
    private int findHeight(TreeNode n){
        if(n == null) return -1;
        else {
            int vh = findHeight(n.left);
            int hh = findHeight(n.right);
            if(vh >= hh) return vh + 1;
            else return hh + 1;
        }
    }

    /**
     * Finds the height of the root
     * @return int
     */
    public int findHeight(){
        return findHeight(root);
    }
}