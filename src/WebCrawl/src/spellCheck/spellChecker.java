package spellCheck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class spellChecker {

	public spellChecker(){
		//nothing to initialize
	}
	
	public int myLCS(String word1, String word2) {
        
    	// used for dynamic programming to store the lengths of LCS between substrings of word1 and word2
    	int[][] lcs = new int[word1.length() + 1][word2.length() + 1];

    	//loops that iterate over each letter of the word for LCS
    	
        for (int word1_letter = 0; word1_letter <= word1.length(); word1_letter++) {
        	
            for (int word2_letter = 0; word2_letter <= word2.length(); word2_letter++) {
            	
            	// the base cases when i or j equals 0. When either i or j is 0, it means one of the words is empty, and the LCS is 0
                if (word1_letter == 0) {
                    lcs[word1_letter][word2_letter] = word2_letter;
                } 
                else if(word2_letter==0) {
                	lcs[word1_letter][word2_letter] = word1_letter;
                }
                // if the characters at positions (i - 1) in word1 and (j - 1) in word2 are the same, it increments the LCS length by 1
                else if (word1.charAt(word1_letter - 1) == word2.charAt(word2_letter - 1)) {
                    lcs[word1_letter][word2_letter] = lcs[word1_letter - 1][word2_letter - 1];
                } 
                //If the characters are not the same, it takes the maximum of the LCS lengths obtained by excluding the last character of word1 or word2
                else {
                    lcs[word1_letter][word2_letter] = 1 + Math.min(lcs[word1_letter - 1][word2_letter], Math.min(lcs[word1_letter][word2_letter-1] , lcs[word1_letter-1][word2_letter - 1]));
                }
            }
        }

        //Represents the length of the LCS between the entire word1 and word2
        return lcs[word1.length()][word2.length()];
    }
	
	public List<String> suggestwords(List<String> dictionary, String misspelledWord, int lcs_param) {
        
//    	System.out.println("Suggested Corrections:");

        List<String> suggestions = new ArrayList<>();
        
        // loop to find the maximum LCS
        for (String validWord : dictionary) {
        	
        	//finding LCS with entered word and dictionary word
            int lcsLength = myLCS(misspelledWord, validWord);
            
            // if length more than maxLength, then update maxLength
            if (lcsLength<=lcs_param) {
//            	System.out.println("Word: " + validWord + " and ED is : " + lcsLength);
                suggestions.add(validWord);
            } 
        }

        return suggestions;
    }
	
	public List<String> getSuggestions(spellChecker o, String misspelledWord, List<String> dictionary, int lcs_param) {
    	
        return suggestwords(dictionary, misspelledWord, lcs_param);
    }
	
	public static void main(String[] args) {
    	// initializing list of strings for storing words
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
//        String misspelledWord = "mjlk";
//
//        System.out.println("Spell Checker using LCS Approach");
//        System.out.println("------------------------------");
//        suggestwords(dictionary, misspelledWord, 2);
	}

}
