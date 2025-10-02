package frequencyCount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.*;

// Class representing a node in the binary search tree
class TreeNode {
    String key;    // The ingredient name
    int weight;    // The count or weight associated with the ingredient
    TreeNode left, right;   // References to the left and right child nodes

    // Constructor to initialize a tree node with a key and weight
    public TreeNode(String key, int weight) {
        this.key = key;
        this.weight = weight;
        this.left = this.right = null;
    }
}

// Class representing a Binary Search Tree for ingredient counting
public class ingredientCounterBST {
    private TreeNode root;   // The root node of the binary search tree

    // Constructor to initialize an empty BST
    public ingredientCounterBST() {
        this.root = null;
    }

    // Method to insert a new node with the given ingredient and weight into the BST
    public void insert(String key, int weight) {
        root = insertRec(root, key, weight);
    }

    // Recursive helper method to perform the insertion in the BST
    private TreeNode insertRec(TreeNode root, String key, int weight) {
        if (root == null) {
            return new TreeNode(key, weight);
        }

        int compareResult = key.compareTo(root.key);

        if (compareResult < 0) {
            root.left = insertRec(root.left, key, weight);
        } else if (compareResult > 0) {
            root.right = insertRec(root.right, key, weight);
        } else {
            root.weight += weight;
        }

        return root;
    }

    // Method to perform an in-order traversal of the BST and print the ingredients and their counts
    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    // Recursive helper method for in-order traversal
    private void inOrderTraversal(TreeNode root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.println(root.key + ": " + root.weight);
            inOrderTraversal(root.right);
        }
    }

    // Method to create the BST from multiple HashMaps containing ingredient-to-dish mappings
    public void createBSTFromHashMaps(HashMap<String, Set<String>>... hashMaps) {
        for (Map<String, Set<String>> map : hashMaps) {
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                String ingredient = entry.getKey();
                int weight = entry.getValue().size();
                insert(ingredient, weight);
            }
        }
    }

    // Method to get the count of a specific ingredient in the BST
    public int getCount(String ingredient) {
        return getCountRec(root, ingredient);
    }

    // Recursive helper method to search for an ingredient and return its count
    private int getCountRec(TreeNode root, String ingredient) {
        if (root == null) {
            return 0;
        }

        int compareResult = ingredient.compareTo(root.key);

        if (compareResult < 0) {
            return getCountRec(root.left, ingredient);
        } else if (compareResult > 0) {
            return getCountRec(root.right, ingredient);
        } else {
            return root.weight;
        }
    }

    // Main method for testing the BST
    public static void main(String[] args) {
        
    	System.out.println(1);
    	
    	Scanner scanner = new Scanner(System.in);


        //////@@@@@@@@@@@@@@@@@@/////////////////////

        /// Here instead of maincourseIngredientToDishes, appetizerIngredientToDishes and beverageIngredientToDishes you will pass your 3 hashmaps from each of the website and it will create 3 hashmaps for 3 websites
        /////////@@@@@@@@@@@@@@@@///////////////////////


        // Create HashMaps for different types of dishes and their ingredients
        HashMap<String, Set<String>> mcr = new HashMap<>();
        mcr.put("slices", new HashSet<>(Arrays.asList("Beef ramen")));
        mcr.put("chicken", new HashSet<>(Arrays.asList("Chicken ramen")));
        mcr.put("broth", new HashSet<>(Arrays.asList("Dumpling ramen (v)", "Beef ramen", "Kimchi dumpling ramen", "Smoked duck ramen", "Kimchi bacon ramen")));
        
        HashMap<String, Set<String>> app = new HashMap<>();
        app.put("brownie", new HashSet<>(Arrays.asList("Oreo Brownie", "Brownie", "Walnut Brownie", "Lotus Brownie")));
        app.put("tea", new HashSet<>(Arrays.asList("Green Tea Cookie")));
        app.put("walnut", new HashSet<>(Arrays.asList("Walnut Brownie")));
        
        HashMap<String, Set<String>> bev = new HashMap<>();
        bev.put("fruit", new HashSet<>(Arrays.asList("Passion fruit iced tea", "Fruit juice")));
        bev.put("cappuccino", new HashSet<>(Arrays.asList("Tangerine cappuccino", "Cappuccino")));
        bev.put("latte", new HashSet<>(Arrays.asList("Raspberry latte", "Cafe latte", "Peppermint tea latte", "Tiramisu latte", "Matcha latte", "Banana latte", "Caramel latte", "Chai tea latte")));

                // Create an instance of the IngredientCounterBST
        ingredientCounterBST bst = new ingredientCounterBST();

        // Build the BST using the data from the HashMaps
        bst.createBSTFromHashMaps(mcr, app, bev);

        // Print the in-order traversal of the BST
        System.out.println("BST In Order Traversal:");
        bst.inOrderTraversal();

        // Take user input for an ingredient
        System.out.print("\nEnter an ingredient to get its count: ");
        String userIngredient = scanner.nextLine();

        // Get and print the count for the user-specified ingredient
        int count = bst.getCount(userIngredient);
        System.out.println("Count of " + userIngredient + ": " + count);
    }
}