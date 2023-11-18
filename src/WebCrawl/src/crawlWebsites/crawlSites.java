package crawlWebsites;

import java.io.*;
import java.util.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.JavascriptExecutor;

public class crawlSites {

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
		
		//----------------- CRAWLING
		
		File file = new File("C:\\Users\\Arnab\\Downloads\\edgedriver_win64\\msedgedriver.exe");
		System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
		
		String[] urls = {"https://earls.ca/locations/london/menu/","https://www.cafemarch21.com/lunch","https://www.medinacafe.com/"};
		
		String[] save_path = {"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\EarlsHTML\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\CafeMarch21HTML\\",
				"C:\\Users\\Arnab\\OneDrive\\Desktop\\UoW\\Subject Materials\\ACC\\Eclipse_WS\\CrawlProj\\src\\medinacafeHTML\\"
		};
		
		crawlWebsite(urls[0], save_path[0], "Earls");
		crawlWebsite(urls[1], save_path[1], "CafeMarch21");
		crawlWebsite(urls[2], save_path[2], "medinacafe");
		
	}
}
