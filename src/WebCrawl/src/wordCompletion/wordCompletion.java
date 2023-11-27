package wordCompletion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class wordCompletion {

    public class TrieNode {
        Map<Character, TrieNode> children;
        char c;
        boolean isWord;

        public TrieNode(char c) {
            this.c = c;
            children = new HashMap<>();
        }

        public TrieNode() {
            children = new HashMap<>();
        }

        public void insert(String word) {
            if (word == null || word.isEmpty())
                return;
            char firstChar = word.charAt(0);
            TrieNode child = children.get(firstChar);
            if (child == null) {
                child = new TrieNode(firstChar);
                children.put(firstChar, child);
            }

            if (word.length() > 1)
                child.insert(word.substring(1));
            else
                child.isWord = true;
        }
    }

    TrieNode root;

    public wordCompletion() {

    }

    public boolean find(String prefix, boolean exact) {
        TrieNode lastNode = root;
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return false;
        }
        return !exact || lastNode.isWord;
    }

    public boolean find(String prefix) {
        return find(prefix, false);
    }

    public void suggestHelper(TrieNode root, List<String> list, StringBuffer curr) {
        if (root.isWord) {
            list.add(curr.toString());
        }

        if (root.children == null || root.children.isEmpty())
            return;

        for (TrieNode child : root.children.values()) {
            suggestHelper(child, list, new StringBuffer(curr).append(child.c));
        }
    }

    public List<String> suggest(String prefix) {
        List<String> list = new ArrayList<>();
        TrieNode lastNode = root;
        StringBuffer curr = new StringBuffer();
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return list;
            curr.append(c);
        }
        suggestHelper(lastNode, list, curr);
        return list;
    }

    public void createDict(List<String> dictionary) {
    	    	
    	root = new TrieNode();
        for (String word : dictionary)
            root.insert(word);

    }
    
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        // initializing list of strings for storing words
//    	List<String> dictionary = new ArrayList<>();
//    	
//    	// reading the dictionary file
//    	try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\all_ingredients.txt"))) 
//        {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                dictionary.add(line.trim());
//            }
//        } catch (IOException e) {
//            System.err.println("Error loading the dictionary file: " + e.getMessage());
//        }
//
//        System.out.print("Enter a prefix to suggest words: ");
//        String prefix = scanner.nextLine();
//
//        wordCompletion trie = new wordCompletion(dictionary);
//        List<String> suggestions = trie.suggest(prefix);
//        System.out.println("Suggestions: " + suggestions);
//
//        scanner.close();
    }
}
