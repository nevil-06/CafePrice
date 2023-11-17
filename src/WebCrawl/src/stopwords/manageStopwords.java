package stopwords;

import java.util.*;

public class manageStopwords {

    private static class Node {
        String data;
        Node left, right;
        int height;

        Node(String value) {
            this.data = value;
            this.left = this.right = null;
            this.height = 1; // New nodes have height 1
        }
    }

    private Node root;

    public manageStopwords() {
        this.root = null;
    }

    // Get the height of a node
    private int height(Node node) {
        return (node != null) ? node.height : 0;
    }

    // Get the balance factor of a node
    private int getBalance(Node node) {
        return (node != null) ? height(node.left) - height(node.right) : 0;
    }

    // Perform a right rotation
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T = x.right;

        // Perform rotation
        x.right = y;
        y.left = T;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Perform a left rotation
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T = y.left;

        // Perform rotation
        y.left = x;
        x.right = T;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Insert a node into the AVL tree
    public void insert(String value) {
        root = insertRec(root, value);
    }

    private Node insertRec(Node root, String value) {
        if (root == null) {
            return new Node(value);
        }

        // Perform standard BST insertion
        if (value.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, value);
        } else if (value.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, value);
        } else {
            // Duplicate values are not allowed
            return root;
        }

        // Update height of the current node
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // Get the balance factor to check if the node is unbalanced
        int balance = getBalance(root);

        // Left Left Case
        if (balance > 1 && value.compareTo(root.left.data) < 0) {
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && value.compareTo(root.right.data) > 0) {
            return leftRotate(root);
        }

        // Left Right Case
        if (balance > 1 && value.compareTo(root.left.data) > 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Left Case
        if (balance < -1 && value.compareTo(root.right.data) < 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    // Print the AVL tree
    public void printTree() {
        printTree(root, null, "Root");
    }

    private void printTree(Node root, Node parent, String position) {
        if (root != null) {
            printTree(root.right, root, "Right");
            if (parent != null) {
                System.out.println(root.data + " (" + position + " child of " + parent.data + ")");
            } else {
                System.out.println(root.data + " (" + position + ")");
            }
            printTree(root.left, root, "Left");
        }
    }
    
    // New method to search for a word in the AVL tree
    public boolean search(String value) {
        return searchRec(root, value);
    }

    private boolean searchRec(Node root, String value) {
        if (root == null) {
            return false;
        }

        if (value.equals(root.data)) {
            return true;
        } else if (value.compareTo(root.data) < 0) {
            return searchRec(root.left, value);
        } else {
            return searchRec(root.right, value);
        }
    }
    
    // Example usage
    public static void main(String[] args) {
    	manageStopwords avlTree = new manageStopwords();

        avlTree.insert("banana");
        avlTree.insert("apple");
        avlTree.insert("orange");
        avlTree.insert("grape");
        
        avlTree.printTree();
        
        System.out.println(avlTree.search("aanana"));
    }
}