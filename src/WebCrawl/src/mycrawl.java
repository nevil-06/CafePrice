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

	public static void writeContent(String folderName, String content, String fileName, String extension){
		try {
			System.out.println("Here");
			File f = new File(folderName + fileName + extension);
			System.out.println("Here");
			FileWriter writer = new FileWriter(f, false);
			System.out.println("Here");
			writer.write(content);
			System.out.println("Report Created is in Location : " + f.getAbsolutePath());
			writer.close();
		} catch (Exception e) {
			System.out.println("OOPS!!!Error in Writing file");
		}
	}
	
	public static Hashtable<String, String> createFile(String url, String content, String fileName, String folder) throws IOException{
		Hashtable<String, String> url_Map = new Hashtable<String, String>();
		url_Map.put(fileName + ".html", url);
		writeContent(folder, content, fileName , ".html");
		return url_Map;
	}
	
	public static void crawlWebsite(String url, String save_path, String filename) throws IOException{
		
		WebDriver driver = new EdgeDriver();
		JavascriptExecutor js = (JavascriptExecutor)driver;
		driver.navigate().to(url);
		String content = driver.getPageSource();
		createFile(url, content, filename, save_path);
		
	}
	
	public static void main(String[] args) throws IOException {
		
//		----------------- CRAWLING
		
		File file = new File("C:\\Users\\Arnab\\Downloads\\edgedriver_win64\\msedgedriver.exe");
		System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
		
		String[] urls = {"https://earls.ca/locations/london/menu/","https://www.cafemarch21.com/lunch","https://www.medinacafe.com/"};
		String[] save_path = {"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\EarlsHTML\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\CafeMarch21HTML\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\medinacafeHTML\\"
		};
		
//		crawlWebsite(urls[0], save_path[0]);
//		crawlWebsite(urls[1], save_path[1], "CafeMarch21");
//		crawlWebsite(urls[2], save_path[2], "bipartisan");
		
//		-----------------------------------------------------------------------
		// read the HTML file
		File inputFile = new File("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\medinacafeHTML\\medinacafe.html");
		// parse the file by use of Jsoup library
		Document doc = Jsoup.parse(inputFile, null);
		
		// ADDING STOPWORDS ( decide between stopwords or non-stopwords/ingredients )
		
		manageStopwords stopword_tree = new manageStopwords();
		// loop to iterate stopwords.txt and insert all stopwords in it
		
		//		READING BRUNCH SECTION
		
		//String els = doc.select("p").get(3).text(); // getting the text of the 3rd indexed paragraph out of all <p> elements
//		String ele = doc.getElementsByClass("menu-item").get(1).getElementsByClass("menu-item-price-top").text();
		Element sec = doc.getElementsByClass("sqs-block menu-block sqs-block-menu").get(0);
		
		List<Element> dishes = sec.getElementsByClass("menu-item");

		HashMap<String, Set<String>> ingr_to_dish = new HashMap<String, Set<String>>();
		HashMap<String, Float> dish_to_price = new HashMap<String, Float>();
		
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
			
			String[] res = e1.getElementsByClass("menu-item-description").text().split("\\s*[^a-zA-Z]+\\s*");

			// need to add data validation step for valid price
			String price = e1.getElementsByClass("menu-item-price-top").text();
			System.out.println(price);
			
			if(!o.checkPrice(price)) {
//				System.out.println("Bad price: " + price);
				continue;
			}
			
//			System.out.println("Dish name: " + dish);
//			System.out.println("Dish price: " + price);
			
			// mapping dish to price
			dish_to_price.put(dish, Float.parseFloat(price.substring(1)));
			
			// loop for iterating ingredients and mapping it to the dish
			for(String s: res)
			{
				
				String ing = s.trim().toLowerCase();
				
				//TB Added: stopword functionality by using search() function
				
				if(ing.length()>0) // to avoid empty ingredients
				{
					if(ingr_to_dish.get(ing)==null) {
						Set<String> newlist = new HashSet<String>();
						newlist.add(dish);
						ingr_to_dish.put(ing, newlist);
					}
					else {
						//assuming new dish name
						ingr_to_dish.get(ing).add(dish);
					}
				}
//					System.out.println(s.trim() + ' ');
			}
			
			
//			System.out.println();
//			System.out.println(e1.getElementsByClass("menu-item-description").text().split("\\s*[a-zA-Z]+\\s*"));
//			System.out.println(e1.getElementsByClass("menu-item-price-top").text());
		}
		
		System.out.println("Size of map: " + dish_to_price.size());
		
		for (Map.Entry<String, Set<String>> set : ingr_to_dish.entrySet()) {
			 
            // Printing all elements of a Map
            System.out.print(set.getKey() + " dishes are: ");
            System.out.print(set.getKey().length() + " is dish size ");
            System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
            for(String s: set.getValue()) {
            	System.out.print(s + " , ");
            }
            System.out.println();
        }
		// should print the price of item
//		System.out.println(sec);
		
	}

}
