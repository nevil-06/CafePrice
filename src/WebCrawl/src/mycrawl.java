import datavalidate.datavalidation;
import stopwords.manageStopwords;
import java.io.*;
import java.text.Normalizer;
import java.util.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class mycrawl {

	HashMap<String, Set<String>> app_ingr_to_dish = new HashMap<String, Set<String>>();
	HashMap<String, Float> app_dish_to_price = new HashMap<String, Float>();
	HashMap<String, Set<String>> mcr_ingr_to_dish = new HashMap<String, Set<String>>();
	HashMap<String, Float> mcr_dish_to_price = new HashMap<String, Float>();
	HashMap<String, Set<String>> bev_ingr_to_dish = new HashMap<String, Set<String>>();
	HashMap<String, Float> bev_dish_to_price = new HashMap<String, Float>();
	manageStopwords stopword_tree;
	
	public mycrawl() {
		stopword_tree = new manageStopwords();
	}
	
	public void parseAppetizers(Document doc, mycrawl obj) {
		
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
			
			String[] res = e1.getElementsByClass("menu-item-title").text().split("\\s*[^a-zA-Z]+\\s*");

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
			app_dish_to_price.put(dish, Float.parseFloat(price.substring(1)));
			
			// loop for iterating ingredients and mapping it to the dish
			for(String s: res)
			{
				
				String ing = s.trim().toLowerCase();
				
				//TB Added: stopword functionality by using search() function
				
				if(ing.length()>0 && !obj.stopword_tree.search(ing)) // to avoid empty ingredients
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
		
		for (Map.Entry<String, Set<String>> set : app_ingr_to_dish.entrySet()) {
			 
            // Printing all elements of a Map
            System.out.print(set.getKey() + " dishes are: ");
            System.out.print(set.getKey().length() + " is dish size ");
            System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
            for(String s: set.getValue()) {
            	System.out.print(s + " , ");
            }
            System.out.println();
        }
	}
	
	public void parseMainCourse(Document doc, mycrawl obj) {
		
		Element sec = doc.getElementsByClass("sqs-block menu-block sqs-block-menu").get(0);
		
		List<Element> dishes = sec.getElementsByClass("menu-item");
		
		for(Element e1: dishes) {
			
			datavalidation o = new datavalidation();
			
			String dish = e1.getElementsByClass("menu-item-title").text();
			
			dish = Normalizer.normalize(dish, Normalizer.Form.NFD);
			dish = dish.replaceAll("[^\\p{ASCII}]", "");
			
			if(!o.checkDishName(dish)) {
				continue;
			}
			
			String[] res = e1.getElementsByClass("menu-item-description").text().split("\\s*[^a-zA-Z]+\\s*");

			// need to add data validation step for valid price
			String price = e1.getElementsByClass("menu-item-price-top").text();
			
			if(!o.checkPrice(price)) {
				continue;
			}
			
//			System.out.println(price);

			// mapping dish to price
			mcr_dish_to_price.put(dish, Float.parseFloat(price.substring(1)));
			
			// loop for iterating ingredients and mapping it to the dish
			for(String s: res)
			{
				String ing = s.trim().toLowerCase();
				
				//TB Added: stopword functionality by using search() function
				
				if(ing.length()>0 && !obj.stopword_tree.search(ing)) // to avoid empty ingredients
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
//            System.out.print(set.getKey().length() + " is dish size ");
//            System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
//            for(String s: set.getValue()) {
//            	System.out.print(s + " , ");
//            }
//            System.out.println();
//        }		
	}
	
	public manageStopwords insertStopwords(mycrawl obj, String filePath) throws IOException {
		return manageStopwords.insertStopwords(filePath, obj.stopword_tree);
	}
	
	public static void main(String[] args) throws IOException {
		
		// read the HTML file
		File inputFile = new File("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\medinacafeHTML\\medinacafe.html");
		// parse the file by use of Jsoup library
		Document doc = Jsoup.parse(inputFile, null);
		
		// ADDING STOPWORDS ( decide between stopwords or non-stopwords/ingredients )
		
		// loop to iterate stopwords.txt and insert all stopwords in it
		String filePath = "C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\stopwords\\stopwords.txt";
		
		mycrawl obj = new mycrawl();
		obj.stopword_tree = obj.insertStopwords(obj, filePath);
		
//        obj.stopword_tree.printTree();
        
        System.out.println("Height of the AVL tree: " + obj.stopword_tree.getHeight());
        
        obj.parseAppetizers(doc, obj);
        
        obj.parseMainCourse(doc, obj);
        
//		System.out.println("Size of map: " + dish_to_price.size());
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
