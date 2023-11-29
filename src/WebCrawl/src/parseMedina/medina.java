package parseMedina;

import datavalidate.datavalidation;
import stopwords.manageStopwords;
import java.io.*;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class medina {

	public HashMap<String, Set<String>> app_ingr_to_dish = new HashMap<String, Set<String>>();
	public HashMap<String, Object[]> app_dish_to_price = new HashMap<String, Object[]>();
	public HashMap<String, Set<String>> mcr_ingr_to_dish = new HashMap<String, Set<String>>();
	public HashMap<String, Object[]> mcr_dish_to_price = new HashMap<String, Object[]>();
	public HashMap<String, Set<String>> bev_ingr_to_dish = new HashMap<String, Set<String>>();
	public HashMap<String, Object[]> bev_dish_to_price = new HashMap<String, Object[]>();
	public manageStopwords stopword_tree;
	
	public medina() {
		setStopword_tree(new manageStopwords());
	}
	
	public void parseAppetizers(Document doc, medina obj) {
		
		Element sec = doc.getElementsByClass("sqs-block menu-block sqs-block-menu").get(2);
		
		List<Element> dishes = sec.getElementsByClass("menu-item");

//		System.out.println("Mysize: " + dishes.size());
				
		for(Element e1: dishes) {
			
			datavalidation o = new datavalidation();
			
			String dish = e1.getElementsByClass("menu-item-title").text();
			
			dish = Normalizer.normalize(dish, Normalizer.Form.NFD);
			dish = dish.replaceAll("[^\\p{ASCII}]", "");
			
			if(!o.checkDishName(dish)) {
//				System.out.println("Bad name: " + dish);
				continue;
			}
			
			//System.out.println(dish);
			
			String[] res = dish.split("\\s*[^a-zA-Z]+\\s*");

			// need to add data validation step for valid price
			String price = e1.getElementsByClass("menu-item-price-top").text();
//			System.out.println(price);
			
			if(!o.checkPrice(price)) {
//				System.out.println("Bad price: " + price);
				continue;
			}
			
//			System.out.println("Dish name: " + dish);
//			System.out.println("Dish price: " + price);
			
			// mapping dish to price
			app_dish_to_price.put(dish, new Object[] {Float.parseFloat(price.substring(1)), "MEDINA CAFE"});
			
			// loop for iterating ingredients and mapping it to the dish
			for(String s: res)
			{
				String ing = s.trim().toLowerCase();
				List<String> check_plural = Arrays.asList("potatoes");

				if(check_plural.contains(ing)) {
					ing = ing.substring(0, ing.length()-2);
				}
				
				//TB Added: stopword functionality by using search() function
				
				if(ing.length()>0 && !obj.getStopword_tree().search(ing)) // to avoid empty ingredients
				{
					if(app_ingr_to_dish.get(ing)==null) {
						Set<String> newlist = new HashSet<String>();
						newlist.add(dish);
						app_ingr_to_dish.put(ing, newlist);
					}
					else {
						//assuming new dish name
						app_ingr_to_dish.get(ing).add(dish);
					}
				}
//					System.out.println(s.trim() + ' ');
			}
		}
		
//		for (Map.Entry<String, Set<String>> set : app_ingr_to_dish.entrySet()) {
//			 
//            // Printing all elements of a Map
//            System.out.print(set.getKey() + " dishes are: ");
//            System.out.print(set.getKey().length() + " is dish size ");
//            System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
//            for(String s: set.getValue()) {
//            	System.out.print(s + " , ");
//            }
//            System.out.println();
//        }
	}
	
	public void parseMainCourse(Document doc, medina obj) {
		
		Element sec = doc.getElementsByClass("sqs-block menu-block sqs-block-menu").get(0);
		
		List<Element> dishes = sec.getElementsByClass("menu-item");
		
		for(Element e1: dishes) {
			
			datavalidation o = new datavalidation();
			
			String dish = e1.getElementsByClass("menu-item-title").text();
			
			dish = Normalizer.normalize(dish, Normalizer.Form.NFD);
			dish = dish.replaceAll("[^\\p{ASCII}]", "");
			
//			System.out.println("Dish: " + dish);
			
			if(!o.checkDishName(dish)) {
				continue;
			}
			
			String dish_desc = e1.getElementsByClass("menu-item-description").text();
			dish_desc = Normalizer.normalize(dish_desc, Normalizer.Form.NFD);
			dish_desc = dish_desc.replaceAll("[^\\p{ASCII}]", "");
			String[] res = dish_desc.split("\\s*[^a-zA-Z]+\\s*");

			// need to add data validation step for valid price
			String price = e1.getElementsByClass("menu-item-price-top").text();
			
			if(!o.checkPrice(price)) {
				continue;
			}
			
//			System.out.println(price);

			// mapping dish to price
			mcr_dish_to_price.put(dish, new Object[] {Float.parseFloat(price.substring(1)), "MEDINA CAFE"});
			
			// loop for iterating ingredients and mapping it to the dish
			for(String s: res)
			{
				String ing = s.trim().toLowerCase();
//				List<String> check_plural = Arrays.asList("eggs","onions","mushrooms","tomatoes","peppers","olives");
				List<String> check_plural_2 = Arrays.asList("potatoes","tomatoes");

//				if(check_plural.contains(ing)) {
//					ing = ing.substring(0, ing.length()-1);
//				}
//				else if(check_plural_2.contains(ing)) {
//					ing = ing.substring(0, ing.length()-2);
//				}
				
				// Create a pattern for ingredients with an optional 's' at the end
		        Pattern pluralPattern = Pattern.compile("(.*?)(s)?$");
		        Matcher matcher = pluralPattern.matcher(ing);

		        if(check_plural_2.contains(ing)) {
					ing = ing.substring(0, ing.length()-2);
				}	
		        else if (matcher.matches()) {
		            // If it matches the pattern, remove the 's' or 'es' at the end
		            ing = matcher.group(1);
		        }
				
				//TB Added: stopword functionality by using search() function
				
				if(ing.length()>0 && !obj.getStopword_tree().search(ing)) // to avoid empty ingredients
				{
					if(mcr_ingr_to_dish.get(ing)==null) {
						Set<String> newlist = new HashSet<String>();
						newlist.add(dish);
						mcr_ingr_to_dish.put(ing, newlist);
					}
					else {
						//assuming new dish name
						mcr_ingr_to_dish.get(ing).add(dish);
					}
				}
			}			
		}
		
//		System.out.println("Size of map: " + mcr_dish_to_price.size());

//		for (Map.Entry<String, Set<String>> set : mcr_ingr_to_dish.entrySet()) {
//			 
//            // Printing all elements of a Map
//            System.out.print(set.getKey() + " dishes are: ");
////            System.out.print(set.getKey().length() + " is dish size ");
////            System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
//            for(String s: set.getValue()) {
//            	System.out.print(s + " , ");
//            }
//            System.out.println();
//        }		
	}
	
	public void parseBeverages(Document doc, medina obj) {
		
		Element sec = doc.getElementsByClass("sqs-block menu-block sqs-block-menu").get(3);
		
		List<Element> dishes = sec.getElementsByClass("menu-item");

//		System.out.println("Mysize: " + dishes.size());
				
		for(Element e1: dishes) {
			
			datavalidation o = new datavalidation();
			
			String dish = e1.getElementsByClass("menu-item-title").text();
			
			dish = Normalizer.normalize(dish, Normalizer.Form.NFD);
			dish = dish.replaceAll("[^\\p{ASCII}]", "");
			
			if(!o.checkBevName(dish)) {
//				System.out.println("Bad name: " + dish);
				continue;
			}
			
//			System.out.print(dish);
			
//			String input = e1.getElementsByClass("menu-item-description").text();
//
//	        // Define the regex pattern for splitting
//	        String regex = "\\s*[^a-zA-Z]+\\s*";
//
//	        // Create a Pattern object
//	        Pattern pattern = Pattern.compile(regex);
//
//	        // Create a Matcher object
//	        Matcher matcher = pattern.matcher(input);
//
//	        // Process the matches
//	        ArrayList<String> res_updated = new ArrayList<>();
//	        while (matcher.find()) {
//	            String ing = matcher.group().trim().toLowerCase();
//	            if (ing.length() > 0 && !obj.stopword_tree.search(ing)) {
//	                res_updated.add(ing);
//	            }
//	        }
			
			String[] res = e1.getElementsByClass("menu-item-description").text().split("\\s*[^a-zA-Z]+\\s*");
			
			ArrayList<String> res_updated = new ArrayList<>();
			
			// insert to check if it comes out empty due to stopwords
			for(String s: res) {
				String ing = s.trim().toLowerCase();
				if(ing.length()>0 && !obj.stopword_tree.search(ing)) // to avoid empty ingredients
				{
					res_updated.add(ing);
				}
			}
			
			// if empty due to no description available or empty due to all stopwords then pick from option
			if(res_updated.size()==0) {
				res = e1.getElementsByClass("menu-item-option").text().split("\\s*[^a-zA-Z]+\\s*");
				
				res_updated = new ArrayList<>();
				
				// insert to check if it comes out empty due to stopwords
				for(String s: res) {
					String ing = s.trim().toLowerCase();
					if(ing.length()>0 && !obj.stopword_tree.search(ing)) // to avoid empty ingredients
					{
						res_updated.add(ing);
					}
				}
				
				if(res_updated.size()==0) {
					res = dish.split("\\s*[^a-zA-Z]+\\s*");
				}
				else {
					res = res_updated.toArray(new String[0]);
				}
			}
			else {
				res = res_updated.toArray(new String[0]);
			}
			
			// need to add data validation step for valid price
			String price = e1.getElementsByClass("menu-item-price-top").text();
//			System.out.println(price);
			
			if(!o.checkPrice(price)) {
//				System.out.println("Bad price: " + price);
				continue;
			}
			
//			System.out.print("Dish name: " + dish + " ");
//			System.out.println(price);
			
			// mapping dish to price
			bev_dish_to_price.put(dish, new Object[] {Float.parseFloat(price.substring(1)), "MEDINA CAFE"});
			
			// loop for iterating ingredients and mapping it to the dish
			for(String s: res)
			{
				
				String ing = s.trim().toLowerCase();
				
				//TB Added: stopword functionality by using search() function
				
				if(ing.length()>0 && !obj.stopword_tree.search(ing)) // to avoid empty ingredients
				{
					if(bev_ingr_to_dish.get(ing)==null) {
						Set<String> newlist = new HashSet<String>();
						newlist.add(dish);
						bev_ingr_to_dish.put(ing, newlist);
					}
					else {
						//assuming new dish name
						bev_ingr_to_dish.get(ing).add(dish);
					}
				}
//					System.out.println(s.trim() + ' ');
			}
		}
		
		for (Map.Entry<String, Set<String>> set : bev_ingr_to_dish.entrySet()) {
			 
            // Printing all elements of a Map
            System.out.print(set.getKey() + " Beverages are: ");
            System.out.print(set.getKey().length() + " is Beverages size ");
            for(String s: set.getValue()) {
            	System.out.print(s + " , ");
            }
            System.out.println();
        }
		
//		for (Map.Entry<String, Object[]> set : bev_dish_to_price.entrySet()) {
//			 
//           // Printing all elements of a Map
//           System.out.print(set.getKey() + " Price is: $" + set.getValue()[0]);
//           System.out.print(set.getKey().length() + " is Beverages size ");
//           System.out.println();
//       }
	}
	
	public manageStopwords insertStopwords(medina obj, String filePath) throws IOException {
		return manageStopwords.insertStopwords(filePath, obj.getStopword_tree());
	}
	
	public void createStopwords(medina obj, String filePath) throws IOException{
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
		
//		HashMap<String, Object[]> t = new HashMap();
//		t.put("a", new Object[] {10.5f, "Cafe1"});
//		System.out.println(t.get("a")[0]);
		// read the HTML file
		File inputFile = new File("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Medina\\medinacafe.html");
		// parse the file by use of Jsoup library
		Document doc = Jsoup.parse(inputFile, null);
		
		// ADDING STOPWORDS ( decide between stopwords or non-stopwords/ingredients )
		
		// loop to iterate stopwords.txt and insert all stopwords in it
		String filePath = "C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\stopwords\\stopwords.txt";
		
		medina obj = new medina();
		obj.createStopwords(obj, filePath);
		
//        obj.stopword_tree.printTree();
        
        System.out.println("Height of the AVL tree: " + obj.getStopword_tree().getHeight());
        
//        obj.parseAppetizers(doc, obj);
        
        obj.parseMainCourse(doc, obj);
        
//        obj.parseBeverages(doc, obj);
        
		System.out.println("Size of map: " + obj.app_dish_to_price.size());
//		
//		for (Map.Entry<String, Set<String>> set : ingr_to_dish.entrySet()) {
//			 
//            // Printing all elements of a Map
//            System.out.print(set.getKey() + " dishes are: ");
//            System.out.print(set.getKey().length() + " is dish size ");
//            System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
//            for(String s: set.getValue()) {
//            	System.out.print(s + " , ");
//            }
//            System.out.println();
//        }
		// should print the price of item
//		System.out.println(sec);
		
	}

}
