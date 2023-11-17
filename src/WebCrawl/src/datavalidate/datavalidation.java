package datavalidate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class datavalidation {

	public boolean checkPrice(String price) {
		if(price.matches("\\$\\d{2}")) {
			return true;
		}
		else
			return false;
	}
	
	public boolean checkDishName(String dishname) {
		if(dishname.matches("^[a-zA-Z][a-zA-Z0-9 *,:]*$")) {	
			return true;
		}
		else
			return false;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String price = "Israeli Couscous * available after 11:00am";
		datavalidation o = new datavalidation();
		System.out.println(o.checkDishName(price));
	}

}
