package mainPage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import parseMedina.medina;
import crawlWebsites.crawlSites;
import datavalidate.datavalidation;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class mainConsole {

	public static void main(String[] args) throws IOException{
		
		// clear the console
		System.out.print("\033[H\033[2J");  
	    System.out.flush();
		
	    System.out.println("Crawling websites...");
	    
	    File file = new File("C:\\Users\\Arnab\\Downloads\\edgedriver_win64\\msedgedriver.exe");
		System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
		
		String[] urls = {"https://earls.ca/locations/london/menu/","https://www.cafemarch21.com/lunch","https://www.medinacafe.com/"};
		
		String[] save_path = {"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Earls\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Cafe21\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\crawlWebsites\\CrawlResult\\Medina\\"
		};
		
//		crawlSites.crawlWebsite(urls[0], save_path[0], "Earls");
//		crawlSites.crawlWebsite(urls[1], save_path[1], "CafeMarch21");
//		crawlSites.crawlWebsite(urls[2], save_path[2], "medinacafe");
	    
	    System.out.println("Parsing websites...");
	    
	    // call functions to crawl and parse
	    medina m_obj = new medina();
	 // read the HTML file
 		File inputFile = new File("C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\medinacafeHTML\\medinacafe.html");
 		// parse the file by use of Jsoup library
 		Document doc = Jsoup.parse(inputFile, null);
 		
 		// ADDING STOPWORDS ( decide between stopwords or non-stopwords/ingredients )
 		
 		// loop to iterate stopwords.txt and insert all stopwords in it
 		String filePath = "C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\stopwords\\stopwords.txt";
	    
	    m_obj.createStopwords(m_obj, filePath);
		
//      obj.stopword_tree.printTree();
      
	    System.out.println("Height of the AVL tree: " + m_obj.getStopword_tree().getHeight());
	      
	    m_obj.parseAppetizers(doc, m_obj);
	      
	    m_obj.parseMainCourse(doc, m_obj);
	    
	    System.out.println("Size of map: " + m_obj.app_dish_to_price.size());
	    
		System.out.println();
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		System.out.println("---------------------WELCOME TO BUGBUSTERS CAFE DISH RECOMMENDATION-------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		
		System.out.println();
		
		float exp_price = 10.00f; // default price keeping as 10 CAD
		boolean res = false;
		Scanner scanner1 = new Scanner(System.in);

        do {
        	System.out.println("ENTER THE EXPECTED PRICE OF THE DISH: ");

            try {
                String input = scanner1.nextLine();
                // Try to convert the input to a float
                exp_price = Float.parseFloat(input);
                datavalidation o = new datavalidation();
                String exp_price_str = Float.toString(exp_price);
                System.out.println("String value: " + exp_price_str);

                if (!o.checkexpPrice(exp_price_str)) {
                    System.out.println("Invalid valid price input.");
                    res = false;
                } else {
                    res = true;
                }
            } catch (NumberFormatException e) {
                // Handle the NumberFormatException
                System.out.println("Invalid non-float input. Please enter a valid price.");
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
	            System.out.println("Invalid non-integer input. Please enter valid option number (1 or 2 or 3)");
	        }
		}while(!res);

		scanner1.close();
		
		//category will surely be either 1/2/3
		
		if(category==1) {
			System.out.println("In Appetizers, we have dishes with ingredients as shown below: ");
			// code to be added
			System.out.println("Enter the appetizer ingredient: ");
		}
		else if(category==2) {
			System.out.println("In Mains, we have dishes with ingredients as shown below: ");
			// code to be added
			System.out.println("Enter the main course ingredient: ");
		}
		else {
			System.out.println("In Beverages, we have dishes with ingredients as shown below: ");
			// code to be added
			System.out.println("Enter the beverages ingredient: ");
		}
		
		System.out.println();
		
//		System.exit(0);
		
	}

}