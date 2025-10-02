package searchFrequency;

class SplayNode {
    String term;
    int frequency;
    SplayNode left, right, parent;

    public SplayNode(String term) {
        this.term = term;
        this.frequency = 1;
        this.left = this.right = this.parent = null;
    }
}

public class splaySearch {
    private SplayNode root;

    public splaySearch() {
        this.root = null;
    }

    // Splay operation to move a node closer to the root
    private void splay(SplayNode node) {
        while (node.parent != null) {
            SplayNode parent = node.parent;
            SplayNode grandParent = parent.parent;

            if (grandParent == null) {
                // Zig rotation
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                // Zig-zig or Zig-zag rotation
                if (node == parent.left && parent == grandParent.left) {
                    rotateRight(grandParent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandParent.right) {
                    rotateLeft(grandParent);
                    rotateLeft(parent);
                } else if (node == parent.right && parent == grandParent.left) {
                    rotateLeft(parent);
                    rotateRight(grandParent);
                } else {
                    rotateRight(parent);
                    rotateLeft(grandParent);
                }
            }
        }

        root = node;
    }

    // Rotate a node to the left
    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;
        x.right = y.left;

        if (y.left != null) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    // Rotate a node to the right
    private void rotateRight(SplayNode y) {
        SplayNode x = y.left;
        y.left = x.right;

        if (x.right != null) {
            x.right.parent = y;
        }

        x.parent = y.parent;

        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        x.right = y;
        y.parent = x;
    }

    // Insert a search term into the splay tree
    public void insert(String term) {
        if (root == null) {
            root = new SplayNode(term);
            return;
        }

        SplayNode current = root;
        SplayNode parent = null;

        while (current != null) {
            parent = current;

            if (term.compareTo(current.term) < 0) {
                current = current.left;
            } else if (term.compareTo(current.term) > 0) {
                current = current.right;
            } else {
                // Term already exists, update frequency
                current.frequency++;
                splay(current); // Splay the node to the root
                return;
            }
        }

        SplayNode newNode = new SplayNode(term);
        newNode.parent = parent;

        if (term.compareTo(parent.term) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode); // Splay the newly inserted node to the root
    }

    // Public method to get the frequency of a search term
    public int getFrequency(String term) {
        SplayNode node = findNode(term);
        if (node != null) {
            return node.frequency;
        }
        return 0; // Term not found
    }

    // Private method to find a node with a given term
    private SplayNode findNode(String term) {
        SplayNode current = root;

        while (current != null) {
            if (term.compareTo(current.term) < 0) {
                current = current.left;
            } else if (term.compareTo(current.term) > 0) {
                current = current.right;
            } else {
                // Term found, splay the node to the root
                splay(current);
                return current;
            }
        }

        return null; // Term not found
    }

    public static void main(String[] args) {

//        splaySearch splayTree = new splaySearch();
//
//        Scanner scanner = new Scanner(System.in);
//
//        // Taking user input
//        for (int i = 0; i < 5; i++) {
//            System.out.print("Enter Search Term: ");
//            String searchTerm = scanner.nextLine();
//            splayTree.insert(searchTerm);
//        }
//
//        int frequency = splayTree.getFrequency("balls");
//        
//        System.out.println("Frequency of '" + "balls" + "': " + frequency);

    }
}
