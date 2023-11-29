package parseArch;

import org.jsoup.Jsoup;

import parseMedina.medina;
import stopwords.manageStopwords;
import datavalidate.datavalidation;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class thearch {

	public HashMap<String, Set<String>> app_ingr_to_dish = new HashMap<>();
    public HashMap<String, Set<String>> mcr_ingr_to_dish = new HashMap<>();
    public HashMap<String, Set<String>> bev_ingr_to_dish = new HashMap<>();
    public HashMap<String, Object[]> app_dish_to_price = new HashMap<>();
    public HashMap<String, Object[]> bev_dish_to_price = new HashMap<>();
    public HashMap<String, Object[]> mcr_dish_to_price = new HashMap<>();
    private manageStopwords stopword_tree;
    
    public thearch() {
		setStopword_tree(new manageStopwords());
	}
    
    public manageStopwords insertStopwords(thearch obj, String filePath) throws IOException {
		return manageStopwords.insertStopwords(filePath, obj.getStopword_tree());
	}
	
	public void createStopwords(thearch obj, String filePath) throws IOException{
		obj.setStopword_tree(obj.insertStopwords(obj, filePath));
	}
	
	/**
	 * @return the stopword_tree
	 */
	public manageStopwords getStopword_tree() {
		return stopword_tree;
	}

	/**
	 * @param stopword_tree the stopword_tree to set
	 */
	public void setStopword_tree(manageStopwords stopword_tree) {
		this.stopword_tree = stopword_tree;
	}
    
    public static void main(String[] args) throws IOException {

        // Parse HTML content with Jsoup
    	File directory = new File("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Thearch\\TheArch.html");
    	medina m_obj = new medina();
    	String filePath = "C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\stopwords\\stopwords.txt";
	    m_obj.createStopwords(m_obj, filePath);
	    Document doc = Jsoup.parse(directory, "UTF-8");
	    thearch a = new thearch();
	    a.parseHtmlFile(doc, m_obj);
    }

    public void parseHtmlFile(Document doc, medina obj) {
        Set<String> dishAppDescArray = new HashSet<>();
        Set<String> dishMainCourseDescArray = new HashSet<>();
        Set<String> dishBeveragesDescArray = new HashSet<>();
        Set<String> uniqueAppIngSet = new HashSet<>();
        Set<String> uniqueMainCourseIngSet = new HashSet<>();
        Set<String> uniqueBeveragesIngSet = new HashSet<>();

        HashMap<String, String> hashMapAppNameToDesc = new HashMap<>();
        HashMap<String, String> hashMapMainCourseNameToDesc = new HashMap<>();
        HashMap<String, String> hashMapBeveragesNameToDesc = new HashMap<>();

        // Rest of the code for parsing HTML content with Jsoup
		Elements divInnerElements = doc.select("div.fp_inner_box");

		List<String> appetizersKeywords = Arrays.asList("Toast", "Salad", "Pancake", "Croissant", "Soup");

		List<String> mainCourseKeywords = Arrays.asList("Omelette", "Bowl", "Flufelette", "Baby", "Skillet", "Platter");

		datavalidation o = new datavalidation();

		for (Element innerDiv : divInnerElements) {
		    // Rest of the code for extracting information from div elements
//		    String dishDescString = innerDiv.select("a.fp_popTrig.fp_inline_btn").text();
		    String dishDescString = innerDiv.select("div.menu_description").text();
		    String dishName = innerDiv.select("h3").text().replaceAll("(Weekend|Friday|Weekday|Menu)", "").trim().split("\\$")[0];
		    dishName = Normalizer.normalize(dishName, Normalizer.Form.NFD);
		    dishName = dishName.replaceAll("[^\\p{ASCII}]", "");
		    if(dishName.equals("Bazaar Breakfast Platter  serves two"))
		    	dishName = "Bazaar Breakfast Platter (serves two)";
		    String aText = innerDiv.select("a.fp_popTrig.fp_inline_btn").text();
		    dishDescString = dishDescString.replace(aText, "");

//		    System.out.println(dishName);
		    
		    final String dish2 = dishName;
		 // Use regular expressions to find patterns
	        boolean containsAppetizers = appetizersKeywords.stream()
	                .anyMatch(keyword -> Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE)
	                        .matcher(dish2).find());
	        
	     // Use regular expressions to find patterns
	        boolean containsMainCourse = mainCourseKeywords.stream()
	                .anyMatch(keyword -> Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE)
	                        .matcher(dish2).find());
	        
//		    boolean containsAppetizers = appetizersKeywords.stream().anyMatch(dishName::contains);

//		    boolean containsMainCourse = mainCourseKeywords.stream().anyMatch(dishName::contains);

		    String price = innerDiv.select("span").text().replace(" ","");

		    String finalPrice = null;
		    if (price.contains("/")){
		        finalPrice = price.split("/")[1];
		    }else {
		        finalPrice = price;
		    }
		    if (containsAppetizers && o.checkDishName(dishName) && o.checkPrice(price)) {
		        String validPrice = finalPrice.replaceAll("\\$", "");
		        app_dish_to_price.put(dishName, new Object[] {Float.parseFloat(validPrice), "THE ARCH CAFE BAR"});
		        hashMapAppNameToDesc.put(dishName, dishDescString);
		        dishAppDescArray.add(dishDescString);
		    } else if (containsMainCourse && o.checkDishName(dishName) && o.checkPrice(price)) {
		        String validPrice = finalPrice.replaceAll("\\$", "");
		        mcr_dish_to_price.put(dishName, new Object[] {Float.parseFloat(validPrice), "THE ARCH CAFE BAR"});
		        hashMapMainCourseNameToDesc.put(dishName, dishDescString);
		        dishMainCourseDescArray.add(dishDescString);
		    } else if (o.checkDishName(dishName) && o.checkPrice(price)){
		        String validPrice = finalPrice.replaceAll("\\$", "");
		        bev_dish_to_price.put(dishName, new Object[] {Float.parseFloat(validPrice), "THE ARCH CAFE BAR"});
//		        hashMapBeveragesNameToDesc.put(dishName.replace("(","").replace(")",""), dishName);
		        hashMapBeveragesNameToDesc.put(dishName, dishName);
		        dishBeveragesDescArray.addAll(Arrays.stream(dishName.replace("(","").replace(")","").split(" ")).toList());
		    }
		}

//      String[] stopWordsArray = {"with", "shredded", "on", "two", "w", "sometimes", "s", "availability", "made", "soft", "based", "extra", "dressing", "style", "seed", "oil", "in", "is", "served", "the", "a", "and", "cast", "iron", "of", "or", "finished", "...", "your", "preferred", "delicate", "called", "whole", "three", "red", "topping", "smoked", "breaded", "special", "halal", "flavored", "fried", "spices", "crispy", "dried", "mix", "italian", "black", "marinated", "balsamic", "sliced", "cubes", "blue", "arch", "sweet", "icing", "breast", "small", "german", "sauted", "season", "assorted", "large", "batter", "cooked", "sauteed", "freshly", "flakes", "baked", "scrambled", "caraway", "house", "vegetables", "mixture", "topped", "fluffy", "side", "green", "clotted", "texture", "syrup", "candied", "roasted", "london", "white", "virgin", "double", "persian", "fog", "blend", "hot", "flat", "and", "blossom", "latter","fresh"};
		String[] check_plural_1 = {"pickles","pickled","pancakes","raisin","beans","walnuts","eggs","dates","nuts","herbs","shallots","mushrooms","iced"};
		String[] check_plural_2 = {"tomatoes","brewed"};
		
		//Appetizers HashMap
		for (String s : dishAppDescArray) 
		{
		    String[] ss = s.split("\\s*[^a-zA-Z]+\\s*");

		    for (String sss : ss) {
		        if (!sss.isEmpty()) {
		        	if(Arrays.asList(check_plural_1).contains(sss))
		        		sss = sss.substring(0, sss.length()-1);
		        	else if(Arrays.asList(check_plural_2).contains(sss))
		        		sss = sss.substring(0, sss.length()-2);
		            uniqueAppIngSet.add(sss.toLowerCase());
		        }
		    }
		}
		
		

		for (String s : uniqueAppIngSet) {
		    // Check if s is in stopWordsArray
//                boolean isStopWord = Arrays.asList(stopWordsArray).contains(s.toLowerCase());

		    if (!obj.getStopword_tree().search(s)) {
		        List<String> dishNames = hashMapAppNameToDesc.entrySet()
		                .stream()
		                .filter(ele -> ele.getValue().toLowerCase().contains(s))
		                .map(Map.Entry::getKey)
		                .toList();

		        // Add to the map only if the associated list is not empty
		        if (!dishNames.isEmpty()) {
		        	Set<String> dishSet = new HashSet<>(dishNames);
		            app_ingr_to_dish.put(s, dishSet);
		        }
		    }
		}
			
		Set<String> om = new HashSet<>();
		Set<String> crois = new HashSet<>();
		Set<String> salad = new HashSet<>();
		
		for (Map.Entry<String, Object[]> set : app_dish_to_price.entrySet()) {
			 
			String[] ss = set.getKey().toLowerCase().split("\\s*[^a-zA-Z]+\\s*");
			
			for(String s: ss) {
				if(s.equals("croissant")) {
					crois.add(set.getKey());
				}
				else if(s.equals("salad")) {
					salad.add(set.getKey());
				}
			}
		}
		
		// hardcoding to handle a few specific dishes that would be longer to handle in general
		app_ingr_to_dish.put("croissant", crois);
		app_ingr_to_dish.put("salad", salad);
		
		//Main Course HashMap
		for (String s : dishMainCourseDescArray) {
		    String[] ss = s.split("\\s*[^a-zA-Z]+\\s*");

		    for (String sss : ss) {
		        if (!sss.isEmpty()) {
		        	if(Arrays.asList(check_plural_1).contains(sss))
		        		sss = sss.substring(0, sss.length()-1);
		        	else if(Arrays.asList(check_plural_2).contains(sss))
		        		sss = sss.substring(0, sss.length()-2);
		            uniqueMainCourseIngSet.add(sss.toLowerCase());
		        }
		    }
		}

		for (String s : uniqueMainCourseIngSet) {
			
		    if (!obj.getStopword_tree().search(s)) {
		        List<String> dishNames = hashMapMainCourseNameToDesc.entrySet()
		                .stream()
		                .filter(ele -> ele.getValue().toLowerCase().contains(s))
		                .map(Map.Entry::getKey)
		                .toList();

		        // Add to the map only if the associated list is not empty
		        if (!dishNames.isEmpty()) {
		        	Set<String> dishSet = new HashSet<>(dishNames);
		            mcr_ingr_to_dish.put(s, dishSet);
		        }
		    }
		}

		for (Map.Entry<String, Object[]> set : mcr_dish_to_price.entrySet()) {
			 
			String[] ss = set.getKey().toLowerCase().split("\\s*[^a-zA-Z]+\\s*");
			
			for(String s: ss) {
				if(s.equals("omelette")) {
					om.add(set.getKey());
				}
			}
		}
		
		if(mcr_ingr_to_dish.containsKey("omelette")) {
			Set<String> to_add = mcr_ingr_to_dish.get("omelette");
			to_add.addAll(om);
		}
		
		//Beverages HashMap
		for (String s : dishBeveragesDescArray) {
		    String[] ss = s.split("\\s*[^a-zA-Z]+\\s*");

		    for (String sss : ss) {
		        if (!sss.isEmpty()) {
		        	if(Arrays.asList(check_plural_1).contains(sss))
		        		sss = sss.substring(0, sss.length()-1);
		        	else if(Arrays.asList(check_plural_2).contains(sss))
		        		sss = sss.substring(0, sss.length()-2);
		            uniqueBeveragesIngSet.add(sss.toLowerCase());
		        }
		    }
		}

		for (String s : uniqueBeveragesIngSet) {
		    
			if (!obj.getStopword_tree().search(s)) {
		        List<String> dishNames = hashMapBeveragesNameToDesc.entrySet()
		                .stream()
		                .filter(ele -> ele.getValue().toLowerCase().contains(s))
		                .map(Map.Entry::getKey)
		                .toList();

		        // Add to the map only if the associated list is not empty
		        if (!dishNames.isEmpty()) {
		        	Set<String> dishSet = new HashSet<>(dishNames);
		        	bev_ingr_to_dish.put(s, dishSet);
		        }
		    }
		}
        
//		for (Map.Entry<String, Object[]> set : bev_dish_to_price.entrySet()) {
//			 
//	          // Printing all elements of a Map
//	          System.out.print(set.getKey() + " Price is: $" + set.getValue()[0]);
//	          System.out.println();
//		}
//		
//		System.out.println();
//		
//		for (Map.Entry<String, Object[]> set : app_dish_to_price.entrySet()) {
//			 
//	          // Printing all elements of a Map
//	          System.out.print(set.getKey() + " Price is: $" + set.getValue()[0]);
//	          System.out.println();
//		}
//		
//		System.out.println();
//		
//		for (Map.Entry<String, Object[]> set : mcr_dish_to_price.entrySet()) {
//			 
//	          // Printing all elements of a Map
//	          System.out.print(set.getKey() + " Price is: $" + set.getValue()[0]);
//	          System.out.println();
//		}
//		
//		System.out.println();
		
//        for (Entry<String, Set<String>> set : app_ingr_to_dish.entrySet()) {			 
//           // Printing all elements of a Map
//           System.out.print(set.getKey() + " - dishes: ");
//           for(String s: set.getValue()) {
//        	   System.out.print(s + " , ");
//           }
//           System.out.println();
//        }
//        
//        for (Entry<String, Set<String>> set : mcr_ingr_to_dish.entrySet()) {			 
//            // Printing all elements of a Map
//            System.out.print(set.getKey() + " - Main dishes: ");
//            for(String s: set.getValue()) {
//         	   System.out.print(s + " , ");
//            }
//            System.out.println();
//         }
        
//        for (Entry<String, Set<String>> set : bev_ingr_to_dish.entrySet()) {			 
//            // Printing all elements of a Map
//            System.out.print(set.getKey() + " - Bev: ");
//            for(String s: set.getValue()) {
//         	   System.out.print(s + " , ");
//            }
//            System.out.println();
//         }

    }
}
