package parseCafe21;

import datavalidate.datavalidation;
import parseMedina.medina;
import stopwords.manageStopwords;

import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import stopwords.manageStopwords;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.time.Duration;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class cafe21 {
	
	public HashMap<String, Set<String>> app_ingr_to_dish = new HashMap<String, Set<String>>();
	public HashMap<String, Object[]> app_dish_to_price = new HashMap<String, Object[]>();
	public HashMap<String, Set<String>> mcr_ingr_to_dish = new HashMap<String, Set<String>>();
	Map<String, String> ingredientDishMap = new HashMap<>();
	public HashMap<String, Object[]> mcr_dish_to_price = new HashMap<String, Object[]>();
	public HashMap<String, Set<String>> bev_ingr_to_dish = new HashMap<String, Set<String>>();
	public HashMap<String, Object[]> bev_dish_to_price = new HashMap<String, Object[]>();
	private manageStopwords stopword_tree;
	
	public cafe21() {
		setStopword_tree(new manageStopwords());
	}
	
	public manageStopwords insertStopwords(medina m_obj, String filePath) throws IOException {
		return manageStopwords.insertStopwords(filePath, m_obj.getStopword_tree());
	}
	
	public void createStopwords(medina m_obj, String filePath) throws IOException{
		m_obj.setStopword_tree(m_obj.insertStopwords(m_obj, filePath));
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
	
	public void parseMenu(cafe21 obj, medina m_obj) {
		
		Logger logger = Logger.getLogger("");
        // Disable logging for Selenium's CdpVersionFinder
        Logger seleniumLogger = Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder");
        seleniumLogger.setLevel(Level.OFF);
        // Create a custom console handler to suppress warnings
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.OFF);
        // Attach the custom console handler to the Selenium logger
        seleniumLogger.addHandler(consoleHandler);
        // Disable parent handlers to suppress duplicate messages
        seleniumLogger.setUseParentHandlers(false);
        // Set the level of the root logger to OFF to suppress other unrelated messages
        logger.setLevel(Level.OFF);
        
		// Set the path to your ChromeDriver executable
		File file = new File("C:\\Users\\Arnab\\Downloads\\edgedriver_win64\\msedgedriver.exe");
		System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
		
//        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");  // Run in headless mode
//        options.addArguments("window-size=1920x1080"); // Set the window size
//
//        // Initializing the Chrome driver
//        WebDriver driver = new ChromeDriver(options);
		
		// Initialize EdgeOptions
        EdgeOptions options = new EdgeOptions();
        // Add arguments to run Edge in headless mode
        options.addArguments("--headless");
        // Set the window size
        options.addArguments("window-size=1920x1080");
        // Initializing the Edge driver
        WebDriver driver = new EdgeDriver(options);

        // Used advanced feature of selenium to achieve proper wait-time for loading elements
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));

        // Navigating to the cafe's website
        driver.get("https://www.cafemarch21.com/lunch");

        WebElement initial = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#bc0255a1-574d-11eb-8326-f52232b2fd7d > div > div > div > div > div > div > div:nth-child(2) > div > div:nth-child(1) > div > div > div:nth-child(2) > div > div > p")));
        // Locate the "p" element containing all the data
        WebElement dataElement = driver.findElement(By.cssSelector("#bc0255a1-574d-11eb-8326-f52232b2fd7d > div > div > div > div > div > div > div:nth-child(2) > div > div:nth-child(1) > div > div > div:nth-child(2) > div > div > p"));

        // Extract text from the "p" element
        String dataText = dataElement.getText();

        // Split the text into separate lines
        String[] lines = dataText.split("\n");

        // Process each line
        String dishName = null;
        float price = 0.0f;

        for (String line : lines) {
            // Check if the line contains a dollar sign ('$') to indicate a price
            if (line.contains("$")) {
//            	System.out.println("Line is: " + line + "Dish is: " + dishName);
                // Extract dish name and price from the previous line
                if (dishName != null) {
                    mcr_dish_to_price.put(dishName, new Object[] {price, "CAFE MARCH 21"});
                    dishName = null;
                    price = 0.0f;
                }

                // Split the line to extract dish name and price
                String[] parts = line.split("\\$");

                // Extract dish name
                dishName = parts[0].trim();

                // Extract price if available
                if (parts.length > 1) {
                    price = Float.parseFloat(parts[1].trim());
                }
            } else {
                // If the line does not contain a dollar sign, consider it as ingredient
                if (dishName != null) {
                    ingredientDishMap.put(line.trim(), dishName);
                }
            }
        }
        
//        System.out.println("Dish is: " + dishName + "Price is: " + price);
        mcr_dish_to_price.put(dishName, new Object[] {price, "CAFE MARCH 21"});
        
        Set<String> dishIng = new HashSet<>();

        for (String s : ingredientDishMap.keySet()) {
            String[] slist = s.split(" ");
            for (String st : slist) {

//                System.out.println(st);
                if (st.equals("")) {
                    continue;
                }

                dishIng.add(st.replace(",", "").toLowerCase());
            }
        }
        
        Set<String> ramen = new HashSet<>();
		
		for (Map.Entry<String, Object[]> set : mcr_dish_to_price.entrySet()) {
			 
			String[] ss = set.getKey().toLowerCase().split("\\s*[^a-zA-Z]+\\s*");
			
			for(String s: ss) {
				if(s.equals("ramen")) {
					ramen.add(set.getKey());
				}
			}
		}
		
		// hardcoding to handle a few specific dishes that would be longer to handle in general
		mcr_ingr_to_dish.put("ramen", ramen);
        
//        String[] stopWords = {"slices", "smoked", "black", "bits", "vegan", "korean", "fried", "gochujang", "stir", "sweet", "green", "lotus", "with", "dark", "london", "fog", "cold", "hot", "white", "passion", "tangerine", "cafe", "sweet"};
        String[] check_plural = {"dumplings","iced"};
        
        for (String s : dishIng) {
        	final String s2 = s;
        	if(!m_obj.stopword_tree.search(s2)) {
        		// Mapping data in to map of main-course map as (key = ingridient , value ={dishesh name}
                Set<String> indList = ingredientDishMap.values().stream().filter(map -> map.toLowerCase().contains(s2)).collect(Collectors.toSet());
                if (indList.size()==0){
                    indList = ingredientDishMap.entrySet().stream()
                            .filter(entry -> entry.getKey().contains(s2))
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toSet());
                }
                if(Arrays.asList(check_plural).contains(s))
                	s = s.substring(0, s.length()-1);
                mcr_ingr_to_dish.put(s, indList);
        	}
        }

//        System.out.println(ingredientDishMap.values());
//        System.out.println("Main Course Ing to dish: "+mcr_ingr_to_dish);


        // Print the maps
//        System.out.println("Main Course Price Map: " + mcr_dish_to_price);
//        System.out.println("Ingredient Dish Map: " + ingredientDishMap);
//
//        System.out.println(dishIng);


        //Appetizers ..............................................................................


        WebElement close1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#leadform-popup-close-7e667604-377d-4042-b637-6b3c6e403f73")));


//        WebElement close1 = driver.findElement((By.cssSelector("#leadform-popup-close-7e667604-377d-4042-b637-6b3c6e403f73")));
        close1.click();

        WebElement apPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#\\34 25730e0-47f2-11eb-b443-3f2b74b80d95 > ul > li:nth-child(3) > a")));
        apPage.click();


//        WebElement apPage = driver.findElement((By.cssSelector("#\\34 25730e0-47f2-11eb-b443-3f2b74b80d95 > ul > li:nth-child(3) > a")));
//        apPage.click();


        // WebElement apNamePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#fad23d81-574e-11eb-8326-f52232b2fd7d > div > div > div > div > div.w-cell.col.col-12.col-sm-7.offset-0.offset-sm-1 > div > div:nth-child(1) > div > div > div > div > div > p")));

        WebElement apNamePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#fad23d81-574e-11eb-8326-f52232b2fd7d > div > div > div > div > div.w-cell.col.col-12.col-sm-7.offset-0.offset-sm-1 > div > div:nth-child(1) > div > div > div > div > div > p")));
        WebElement apNamePrice1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#fad23d81-574e-11eb-8326-f52232b2fd7d > div > div > div > div > div.w-cell.col.col-12.col-sm-7.offset-0.offset-sm-1 > div > div:nth-child(2) > div > div > div > div > div > p")));
        // WebElement apNamePrice = driver.findElement(By.cssSelector("#fad23d81-574e-11eb-8326-f52232b2fd7d > div > div > div > div > div.w-cell.col.col-12.col-sm-7.offset-0.offset-sm-1 > div > div:nth-child(1) > div > div > div > div > div > p"));

        String apNamePriceText = apNamePrice.getText() + "\n" + apNamePrice1.getText();

        String[] ent = apNamePriceText.split("\n");

        // Maps to store data

        // Process each entry on the new page
        for (String entry : ent) {

            if (entry.isEmpty()) {
                continue;

            }

//            System.out.println(entry);
            // Split the entry to extract dish name and price
            String[] parts = entry.split("\\$");
//            System.out.println(parts);

            // Extract dish name and price
            String newApName = parts[0].trim();
            
            newApName = Normalizer.normalize(newApName, Normalizer.Form.NFD);
            newApName = newApName.replaceAll("[^\\p{ASCII}]", "");
			
            // Store the data from the new page in the new map
            app_dish_to_price.put(newApName, new Object[] {Float.parseFloat(parts[1].trim()), "CAFE MARCH 21"});
        }

//        System.out.println("Appetizer Price Map: " + app_dish_to_price);        

        Set<String> appIng = new HashSet<>();

        // Mapping data into map (key = ingridient , value = {appetizers}

        for (String s : app_dish_to_price.keySet()) {
            String[] slist = s.split(" ");
            for (String st : slist) {
//                System.out.println(st);
                if (st.equals("")) {
                    continue;
                }
                appIng.add(st.replace(",", "").toLowerCase());
            }
        }

        for (String s : appIng) {
        	if(!m_obj.stopword_tree.search(s)) {
	        	s = Normalizer.normalize(s, Normalizer.Form.NFD);
	        	final String s2 = s.replaceAll("[^\\p{ASCII}]", ""); // should be final to work in contains() below
	            Set<String> appIndList = app_dish_to_price.keySet().stream().filter(map -> map.toLowerCase().contains(s2)).collect(Collectors.toSet());
	            if(Arrays.asList(check_plural).contains(s))
	            	s = s.substring(0, s.length()-1);
	            app_ingr_to_dish.put(s, appIndList);
        	}
        }

//        System.out.println("Appetizer Ing to dish: "+app_ingr_to_dish);


        //Drinks (Bevarages)  .....................................................

//        WebElement close2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#leadform-popup-close-7e667604-377d-4042-b637-6b3c6e403f73")));
//
//        close2.click();

        WebElement bevPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#\\34 25730e0-47f2-11eb-b443-3f2b74b80d95 > ul > li:nth-child(2) > a")));
        bevPage.click();

        WebElement bevNamePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#\\33 8243190-5748-11eb-9ddf-b52ff62bb522 > div > div > div > div > div > div > div > div > div:nth-child(2) > div > div > div > div > div > p")));
        WebElement bevNamePrice1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#\\33 8243190-5748-11eb-9ddf-b52ff62bb522 > div > div > div > div > div > div > div > div > div:nth-child(3) > div > div > div > div > div > p")));


        String bevNamePriceText = bevNamePrice.getText() + "\n" + bevNamePrice1.getText();

        String[] enty = bevNamePriceText.split("\n");

        // Maps to store data

        // Process each entry on the new page
        for (String entry : enty) {

            if (entry.isEmpty()) {
                continue;

            }

            if (!entry.contains("$")) {
                continue;
            }


            //System.out.println(entry);
            // Split the entry to extract dish name and price
            String[] partes = entry.split("\\$");
            //System.out.println(partes);

            // Extract dish name and price
            String newBevName = partes[0].trim();

            // Store the data from the new page in the new map
            bev_dish_to_price.put(newBevName, new Object[] {Float.parseFloat(partes[1].trim()), "CAFE MARCH 21"});
        }

//        System.out.println("Beverage Price Map: " + bev_dish_to_price);

        Set<String> drinkIng = new HashSet<>();

        for (String s : bev_dish_to_price.keySet()) {
            String[] slist = s.split(" ");
            for (String st : slist) {
                if (st.equals("")) {
                    continue;
                }
                drinkIng.add(st.replace(",", "").toLowerCase());
            }
        }

        for (String s : drinkIng) {
        	final String s2 = s;
        	if(!m_obj.stopword_tree.search(s)) {
	        	Set<String> drinkIndList = bev_dish_to_price.keySet().stream().filter(map -> map.toLowerCase().contains(s2)).collect(Collectors.toSet());
	            if(Arrays.asList(check_plural).contains(s))
	            	s = s.substring(0, s.length()-1);
	            bev_ingr_to_dish.put(s, drinkIndList);
        	}
        }

//        System.out.println("Beverages Ing to dish: "+bev_ingr_to_dish);

        driver.quit();
        
        System.out.println();
        
//        for (Map.Entry<String, Set<String>> set : mcr_ingr_to_dish.entrySet()) {
//			 
//          // Printing all elements of a Map
//          System.out.print(set.getKey() + " dishes are: ");
////          System.out.print(set.getKey().length() + " is dish size ");
////          System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
//          for(String s: set.getValue()) {
//        	System.out.print(s + " , ");
////          	System.out.print(s + "price is: $" + mcr_dish_to_price.get(s)[0] + ", ");
//          }
//          System.out.println();
//        }
//        
//        System.out.println();
//        
//        for (Map.Entry<String, Set<String>> set : app_ingr_to_dish.entrySet()) {
////			 
////         // Printing all elements of a Map
//         System.out.print(set.getKey() + " bev are: ");
////         System.out.print(set.getKey().length() + " is dish size ");
////         System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
//         for(String s: set.getValue()) {
//         	System.out.print(s + " , ");
//         }
//         System.out.println();
//       }
//        
//        System.out.println();
//        
//        for (Map.Entry<String, Set<String>> set : bev_ingr_to_dish.entrySet()) {
////			 
////         // Printing all elements of a Map
//         System.out.print(set.getKey() + " dishes are: ");
////         System.out.print(set.getKey().length() + " is dish size ");
////         System.out.print(set.getKey() + " dishes size is : " + set.getValue().size() + " and dishes: ");
//         for(String s: set.getValue()) {
//         	System.out.print(s + " , ");
//         }
//         System.out.println();
//        }
         
// 		for (Map.Entry<String, Object[]> set1 : bev_dish_to_price.entrySet()) {
//		 
//       // Printing all elements of a Map
//       System.out.print(set1.getKey() + " Price is: $" + set1.getValue()[0]);
//       System.out.print(set1.getKey().length() + " is Beverages size ");
//       System.out.println();
//   }
//         
// 		System.out.println();
// 		
// 		for (Map.Entry<String, Object[]> set2 : app_dish_to_price.entrySet()) {
//		 
//       // Printing all elements of a Map
//       System.out.print(set2.getKey() + " Price is: $" + set2.getValue()[0]);
//       System.out.print(set2.getKey().length() + " is Beverages size ");
//       System.out.println();
//   }
//        
// 		System.out.println();
// 		
// 		for (Map.Entry<String, Object[]> set3 : mcr_dish_to_price.entrySet()) {
//		 
//       // Printing all elements of a Map
//       System.out.print(set3.getKey() + " Price is: $" + set3.getValue()[0]);
////       System.out.print(set3.getKey().length() + " is Beverages size ");
//       System.out.println();
//   }
        
      
        
	}
	
	public static void main(String []args) {
        
//		cafe21 obj = new cafe21();
//		obj.parseMenu(obj);
	}
}
