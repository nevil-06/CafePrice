package mainPage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import searchFrequency.splaySearch;
import recommendCafes.recommendCafes;
import spellCheck.spellChecker;
import wordCompletion.wordCompletion;
import parseMedina.medina;
import frequencyCount.ingredientCounterBST;
import parseArch.thearch;
import parseCafe21.cafe21;
import crawlWebsites.crawlSites;
import datavalidate.datavalidation;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* 
To run: go to parent folder of project in CMD

>>javac -d bin -cp lib\* src\recommendCafes\*.java src\searchFrequency\*.java src\datavalidate\*.java src\stopwords\*.java src\crawlWebsites\*.java src\parseMedina\*.java src\parseCafe21\*.java src\frequencyCount\*.java src\wordCompletion\*.java src\spellCheck\*.java src\parseArch\*.java src\mainPage\mainConsole.java
 
>>java -cp bin;lib\* src\mainPage\mainConsole.java

*/

public class mainConsole {

	public static void parseMedina(medina m_obj) throws IOException {
			
	    	// read the HTML file
	 		File inputFile = new File("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Medina\\medinacafe.html");
	 		// parse the file by use of Jsoup library
	 		Document doc = Jsoup.parse(inputFile, null);
	 		
	 		// ADDING STOPWORDS ( decide between stopwords or non-stopwords/ingredients )
	 		
	 		// loop to iterate stopwords.txt and insert all stopwords in it
	 		String filePath = "C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\stopwords\\stopwords.txt";
		    
		    m_obj.createStopwords(m_obj, filePath);
			
		    System.out.println("Created AVL tree for stopwords...");
		    
	//      obj.stopword_tree.printTree();
	      
//		    System.out.println("Height of the AVL tree: " + m_obj.getStopword_tree().getHeight());
		      
		    m_obj.parseAppetizers(doc, m_obj);
		      
		    m_obj.parseMainCourse(doc, m_obj);
		    
		    m_obj.parseBeverages(doc, m_obj);
		    
//		    System.out.println("Size of map: " + m_obj.app_dish_to_price.size());
	}
	
	public static void parseCafe21(cafe21 c_obj, medina m_obj) {
		c_obj.parseMenu(c_obj, m_obj);
	}
	
	public static void parseTheArch(thearch a_obj, medina m_obj) throws IOException {
		
		// read the HTML file
 		File inputFile = new File("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Thearch\\TheArch.html");
 		// parse the file by use of Jsoup library
 		Document doc = Jsoup.parse(inputFile, "UTF-8");
 		
		a_obj.parseHtmlFile(doc, m_obj);
	}
	
	public static ArrayList<String> createIngredientThesaurus(medina m_obj, cafe21 c_obj, thearch a_obj, int ret_value) {
		
		ArrayList<String> ing_app_medina = new ArrayList<>(m_obj.app_ingr_to_dish.keySet());
		ArrayList<String> ing_mains_medina = new ArrayList<>(m_obj.mcr_ingr_to_dish.keySet());
		ArrayList<String> ing_bev_medina = new ArrayList<>(m_obj.bev_ingr_to_dish.keySet());
		ArrayList<String> ing_app_cafe21 = new ArrayList<>(c_obj.app_ingr_to_dish.keySet());
		ArrayList<String> ing_mains_cafe21 = new ArrayList<>(c_obj.mcr_ingr_to_dish.keySet());
		ArrayList<String> ing_bev_cafe21 = new ArrayList<>(c_obj.bev_ingr_to_dish.keySet());
		ArrayList<String> ing_app_arch = new ArrayList<>(a_obj.app_ingr_to_dish.keySet());
		ArrayList<String> ing_mains_arch= new ArrayList<>(a_obj.mcr_ingr_to_dish.keySet());
		ArrayList<String> ing_bev_arch = new ArrayList<>(a_obj.bev_ingr_to_dish.keySet());
		
		if(ret_value==1) {
			ArrayList<String> onlyAppList = new ArrayList<>(ing_app_medina);
			onlyAppList.addAll(ing_app_cafe21);
			onlyAppList.addAll(ing_app_arch);
			return onlyAppList;
		}
		else if(ret_value==2) {
			ArrayList<String> onlyMainsList = new ArrayList<>(ing_mains_medina);
			onlyMainsList.addAll(ing_mains_cafe21);
			onlyMainsList.addAll(ing_mains_arch);
			return onlyMainsList;
		}
		else if(ret_value==3) {
			ArrayList<String> onlyBevList = new ArrayList<>(ing_bev_medina);
			onlyBevList.addAll(ing_bev_cafe21);
			onlyBevList.addAll(ing_bev_arch);
			return onlyBevList;
		}
			
		//concat lists
		ArrayList<String> combinedList = new ArrayList<>(ing_app_medina);
		combinedList.addAll(ing_mains_medina);
		combinedList.addAll(ing_mains_cafe21);
		combinedList.addAll(ing_mains_arch);
		combinedList.addAll(ing_app_cafe21);
		combinedList.addAll(ing_app_arch);
		combinedList.addAll(ing_bev_medina);
		combinedList.addAll(ing_bev_cafe21);
		combinedList.addAll(ing_bev_arch);
		
		Set<String> unique_ing = new HashSet<>(combinedList);
		
		// save to text file
		try (FileWriter writer = new FileWriter("all_ingredients.txt")) {
            for (String element : unique_ing) {
                writer.write(element + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return combinedList;
	}
	
	public static int askUser(Scanner scanner1) {
		
		boolean res = false;
		int category = 0, display = 0;
		
		do {
			System.out.println("ENTER 1 FOR YES AND 2 FOR NO. ");
			
			if (scanner1.hasNextInt()) {
	            // Read the integer
	            category = scanner1.nextInt();
	            if(category<1 || category>2) {
	            	System.out.println("Invalid integer input. Please enter valid option number (1 or 2)");
	            	res = false;
	            }
	            else {
	            	res = true;
	            }
	        }
			else {
	            // If the input is not an integer, handle the error
				scanner1.next();
				if(display>0)
					System.out.println("Invalid non-integer input. Please enter valid option number (1 or 2)");
				else
				{
					System.out.println("Invalid non-integer input. Please enter valid option number (1 or 2)");
					display+=1;
				}
	        }
		}while(!res);
		
		return category;
	}
	
	@SuppressWarnings("unchecked")
	public static void dishRecommendation(medina m_obj, cafe21 c_obj, thearch a_obj, Scanner scanner1, int category, splaySearch searchfreq, float exp_price) {
		
		// adding to BST
	    ingredientCounterBST bst_medina = new ingredientCounterBST();
	    ingredientCounterBST bst_cafe21 = new ingredientCounterBST();
	    ingredientCounterBST bst_arch = new ingredientCounterBST();
	    
	    if(category==1) {
	    	bst_medina.createBSTFromHashMaps(m_obj.app_ingr_to_dish);
		    bst_cafe21.createBSTFromHashMaps(c_obj.app_ingr_to_dish);
		    bst_arch.createBSTFromHashMaps(a_obj.app_ingr_to_dish);
	    }
	    else if(category==2) {
	    	bst_medina.createBSTFromHashMaps(m_obj.mcr_ingr_to_dish);
		    bst_cafe21.createBSTFromHashMaps(c_obj.mcr_ingr_to_dish);
		    bst_arch.createBSTFromHashMaps(a_obj.mcr_ingr_to_dish);
	    }
	    else {
	    	bst_medina.createBSTFromHashMaps(m_obj.bev_ingr_to_dish);
		    bst_cafe21.createBSTFromHashMaps(c_obj.bev_ingr_to_dish);
		    bst_arch.createBSTFromHashMaps(a_obj.bev_ingr_to_dish);
	    }

	    System.out.println("Created frequency count BST...");
	    
		String ing_input;
		boolean ing_res = false;
		
		int display = 0; // workaround for the Java glitch
		
        do {
        	if(display>0) {
        		if(category==1)
        			System.out.println("ENTER THE APPETIZER KEYWORD: ");
        		else if(category==2)
        			System.out.println("ENTER THE MAIN COURSE KEYWORD: ");
        		else
        			System.out.println("ENTER THE BEVERAGES KEYWORD: ");
        	}

        	ing_input = scanner1.nextLine();
            // Try to convert the input to a float
            datavalidation o = new datavalidation();
            
            if (!o.checkIngInput(ing_input)) {
            	if(display>0)
            		System.out.println("Invalid ingredient input. Please enter single-word valid keyword.");
            	else {
            		display += 1;
            	}
                ing_res = false;
            } 
            else 
            {	
            	display+=1;
            	if(bst_medina.getCount(ing_input)==0 && bst_cafe21.getCount(ing_input)==0 && bst_arch.getCount(ing_input)==0) {
    	        	
    	        	spellChecker s_obj = new spellChecker();
    	        	wordCompletion trie = new wordCompletion();
    	        	
    	        	List<String> ret_list = createIngredientThesaurus(m_obj, c_obj, a_obj, category);
    	        	
    	        	if(ret_list.contains(ing_input)) {
    	        		System.out.println("Ingredient found benchod!! " + ing_input);
    	        	}
    	        	
    	        	trie.createDict(ret_list);
    	        	
    	        	List<String> suggestions = s_obj.getSuggestions(s_obj, ing_input, ret_list, 2);
    	        	List<String> word_completions = trie.suggest(ing_input);
    	        	
    	        	if(suggestions.size()==0 && word_completions.size()==0) // if no suggestions found
    	        	{
    	        		System.out.println("No suggestions found using the entered keyword. Please search for another keyword.");
    	        	}
    	        	else // if some suggestion found
    	        	{
    	        		System.out.println();
    	        		System.out.println("Could not find any dish with the entered keyword. Please find below few suggestions and search for another keyword:");
    	        		System.out.println();
    	        		int counter = 1;
    	        		System.out.println("SUGGESTIONS");
    	        		System.out.println("-----------------------------------");
    	        		
    	        		Set<String> final_completion = new HashSet<>(word_completions);
    	        		Set<String> final_suggestions = new HashSet<>(suggestions);
    	        		
    	        		for(String suggest: final_completion) {
    	        			System.out.println(suggest);
    	        			counter+=1;
    	        			if(counter>5)
    	        				break;
    	        		}
    	        		
    	        		counter = 1;
    	        		for(String suggest: final_suggestions) {
    	        			if(!final_completion.contains(suggest))
    	        			{
    	        				System.out.println(suggest);
        	        			counter+=1;
    	        			}
    	        			if(counter>5)
    	        				break;
    	        		}
    	        		System.out.println("-----------------------------------");
    	        	}
    	        	
    	        	System.out.println();
    	        	System.out.println("Do you wish to search for another ingredient?");
    	        	int search_or_not = askUser(scanner1);
    	        	
    	        	display = 0; // to avoid glitch again
    	        	if(search_or_not==1)
    	        		ing_res = false;
    	        	else
    	        		ing_res = true;
    	        }
    	        else // if the ingredient is valid
    	        {
    	        	int getSearchFreq = searchfreq.getFrequency(ing_input);
    	        	if(getSearchFreq>0) {
    	        		System.out.println();
    	        		System.out.println("-------------------------------------------------------------------------------------------------");
    	        		System.out.println("HEY THERE!! You have searched for this dish before " + getSearchFreq + " times.");
    	        		System.out.println("-------------------------------------------------------------------------------------------------");
    	        		System.out.println();
    	        	}
    	        	searchfreq.insert(ing_input);
    	        	
    	        	recommendCafes r_obj = new recommendCafes();
    	        	
//    	        	System.out.println("Size 1: " + m_obj.app_ingr_to_dish.get(ing_input).size());
//    	        	System.out.println("Size 2: " + c_obj.app_ingr_to_dish.get(ing_input).size());
    	        	System.out.println();
    	        	
	        		r_obj.suggestCafesByPrice(ing_input, category, m_obj, c_obj, a_obj, exp_price);
	        		r_obj.suggestCafesByPopularity(ing_input, category, bst_medina, bst_cafe21, bst_arch, m_obj, c_obj, a_obj);
	        		System.out.println();
    	        	
    	        	ing_res = true; // to break the loop after recommending
    	        }
            }
        } while (!ing_res);
	}
	
	@SuppressWarnings("unchecked")
	public static void interactWithUser(Scanner scanner1, medina m_obj, cafe21 c_obj, thearch a_obj, splaySearch searchfreq) {
		
		System.out.println();
		
		float exp_price = 10.00f; // default price keeping as 10 CAD
		boolean res = false;
		
		int display = 0; // workaround for the Java glitch
		
        do {
        	if(display>0)
        		System.out.println("ENTER THE EXPECTED PRICE OF THE DISH: ");

            try {
                String input = scanner1.nextLine();
                // Try to convert the input to a float
                exp_price = Float.parseFloat(input);
                datavalidation o = new datavalidation();
                String exp_price_str = Float.toString(exp_price);
//                System.out.println("String value: " + exp_price_str);

                if (!o.checkexpPrice(exp_price_str)) {
                    System.out.println("Invalid price input. Please enter an expected price greater than 0.");
                    res = false;
                } else {
                	if(exp_price>0)
                		res = true;
                	else {
                		System.out.println("Invalid price input. Please enter an expected price greater than 0.");
                	}
                }
            } catch (NumberFormatException e) {
                // Handle the NumberFormatException
            	if(display>0)
            		System.out.println("Invalid non-float input. Please enter an expected price greater than 0.");
            	else
            		display+=1;
                res = false;
            }
        } while (!res);

        // Close the scanner after all input reading is done
//        scanner1.close();
		
        System.out.println();
		System.out.println("WE HAVE THE BELOW THREE CATEGORIES OF FOOD IN OUR MENU: ");
		System.out.println();
		System.out.println("1. APPETIZERS");
		System.out.println("2. MAINS");
		System.out.println("3. BEVERAGES");
		System.out.println();
		
		res = false; //to break input loop
		int category = 0; // input variable
		
		do {
			System.out.println("ENTER ONE OF THE ABOVE NUMBERS TO PROCEED ( 1 or 2 or 3 ): ");
			
			if (scanner1.hasNextInt()) {
	            // Read the integer
	            category = scanner1.nextInt();
	            if(category<1 || category>3) {
	            	System.out.println("Invalid integer input. Please enter valid option number (1 or 2 or 3)");
	            	res = false;
	            }
	            else {
	            	res = true;
	            }
	        }
			else {
	            // If the input is not an integer, handle the error
				scanner1.next();
				if(display>0)
					System.out.println("Invalid non-integer input. Please enter valid option number (1 or 2 or 3)");
				else
					display+=1;
	        }
		}while(!res);

//		scanner1.close();
		
		//category will surely be either 1/2/3
		
		if(category==1) {
			System.out.println("In Appetizers, find below list of the few top keywords across dishes: ");
			System.out.println();
			System.out.println("potato, egg, cookie, brownie, croissant, salad, bacon..");
			System.out.println();
			
			dishRecommendation(m_obj, c_obj, a_obj, scanner1, category, searchfreq, exp_price);

//	        System.out.println("Ingredient is: " + ing_input);
//	        C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\all_ingredients.txt
	        
		}
		else if(category==2) {
			System.out.println("In Mains, find below list of the few top keywords across dishes: ");
			System.out.println();
			System.out.println("egg, chicken, salmon, beef, onion, potato, ramen, duck, cheese, pancake..");
			System.out.println();
			
			dishRecommendation(m_obj, c_obj, a_obj, scanner1, category, searchfreq, exp_price);
		}
		else {
			System.out.println("In Beverages, find below list of the few top keywords across dishes: ");
			System.out.println();
			System.out.println("cappuchino, latte, tea, beer, lemonade,");
			System.out.println();
			
			dishRecommendation(m_obj, c_obj, a_obj, scanner1, category, searchfreq, exp_price);
		}
		
		System.out.println();
		
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException{
		
		// clear the console
		System.out.print("\033[H\033[2J");  
	    System.out.flush();
		
	    File file = new File("C:\\Users\\Arnab\\Downloads\\edgedriver_win64\\msedgedriver.exe");
		System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
		
		String[] urls = {"https://thearch.ca/menu/","https://www.cafemarch21.com/lunch","https://www.medinacafe.com/"};
		
		String[] save_path = {"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Thearch\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Cafe21\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Medina\\"
		};
		
		System.out.println();
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		System.out.println("---------------------WELCOME TO BUGBUSTERS CAFE DISH RECOMMENDATION-------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		System.out.println();
		
		System.out.println("Do you wish to crawl?");
		
		Scanner scanner1 = new Scanner(System.in);
//		boolean res = false; //to break input loop
		int crawl_res = askUser(scanner1); // input variable
		
		if(crawl_res==1) {
			System.out.println("Crawling websites...");
			crawlSites.crawlWebsite(urls[0], save_path[0], "TheArch");
			crawlSites.crawlWebsite(urls[1], save_path[1], "CafeMarch21");
			crawlSites.crawlWebsite(urls[2], save_path[2], "medinacafe");
			System.out.println("Crawling complete...");
		}
	    
		System.out.println("Using saved HTML files for parsing...");
	    System.out.println("Parsing websites and performing pattern matching & data validation using Regex within...");
	    
	    // call functions to crawl and parse
    	medina m_obj = new medina();
	    parseMedina(m_obj);
//	    System.out.println("Height of the AVL tree: " + m_obj.getStopword_tree().getHeight());
//	    System.out.println("Size of Medina app map: " + m_obj.app_dish_to_price.size());
//	    System.out.println("Size of Medina Mains map: " + m_obj.mcr_dish_to_price.size());
//	    System.out.println("Size of Medina Bev map: " + m_obj.bev_dish_to_price.size());
	    cafe21 c_obj = new cafe21();
	    parseCafe21(c_obj, m_obj);
//	    System.out.println("Size of Cafe21 App map: " + c_obj.app_dish_to_price.size());
//	    System.out.println("Size of Cafe21 Mains map: " + c_obj.mcr_dish_to_price.size());
//	    System.out.println("Size of Cafe21 Bev map: " + c_obj.bev_dish_to_price.size());
	    thearch a_obj = new thearch();
	    parseTheArch(a_obj, m_obj);
	    
	    System.out.println("Created 2-level inverted index of ingredients to dishes and respective cafe...");
	    System.out.println("Created list of stopwords...");
	    System.out.println("Completed parsing websites...");
	    System.out.println("Data validation completed...");
	    System.out.println();
	    
	    createIngredientThesaurus(m_obj, c_obj, a_obj, 0);
	    
	    System.out.println("Created list of ingredients...");
	    
//	    int count = bst_medina.getCount("egg");
//        System.out.println("Count of egg" + ": " + count);
        
//        count = bst_cafe21.getCount("cookie");
//        System.out.println("Count of cookie" + ": " + count);
	    
//        System.exit(0);
		
		boolean searchDish = true;
		int searchDish_res = 0;
		
        splaySearch searchfreq = new splaySearch();
//          splayTree.insert(searchTerm);
//      int frequency = splayTree.getFrequency("balls");
		
		do {
			interactWithUser(scanner1, m_obj, c_obj, a_obj, searchfreq);
			
			System.out.println("Do you wish to search more dishes?");
			System.out.println();
			System.out.println("Enter 1 for YES or 2 for NO");
			
			boolean inp_res = true;
			do {
				if (scanner1.hasNextInt()) {
		            // Read the integer
					searchDish_res = scanner1.nextInt();
		            if(searchDish_res<1 || searchDish_res>2) {
		            	System.out.println("Invalid integer input. Please enter valid option number (1 or 2)");
		            	inp_res = true;
		            }
		            else {
		            	inp_res = false;
		            }
		        }
				else {
		            // If the input is not an integer, handle the error
					scanner1.next();
		            System.out.println("Invalid non-integer input. Please enter valid option number (1 or 2)");
		        }
			}while(inp_res);
			
			if(searchDish_res==2)
				searchDish = false;
			
		}while(searchDish);
		
		System.out.println();
		System.out.println("WE THANK YOU FOR USING OUR RECOMMENDATION ENGINE!! SEE YOU AGAIN!!");
		System.out.println();
		playSignOffAnimation();
		
		scanner1.close();
		
//		System.exit(0);
		
	}
	
	public static void playSignOffAnimation() {
        for (int i = 3; i >= 0; i--) {
        	clearScreen();
            System.out.println("Exiting in " + i + " seconds...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        clearScreen();
        System.out.println("Goodbye!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}