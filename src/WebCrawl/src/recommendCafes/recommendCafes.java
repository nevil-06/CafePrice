package recommendCafes;

import frequencyCount.ingredientCounterBST;
import parseMedina.medina;
import parseCafe21.cafe21;
import parseArch.thearch;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class recommendCafes {

	private static class Dish {
        String dishName;
        String cafeName;
        Float price;

        public Dish(String dishName, String cafeName, Float object) {
            this.dishName = dishName;
            this.cafeName = cafeName;
            this.price = object;
        }
    }

    // Priority queue to store dishes based on their prices
	// this also implements page ranking too
    private PriorityQueue<Dish> priorityQueue;

    // Constructor to initialize the priority queue
    public recommendCafes() {
        // Using a custom comparator to prioritize dishes based on their prices
        priorityQueue = new PriorityQueue<>((dish1, dish2) -> {
            int priceComparison = Float.compare(dish1.price, dish2.price);
            
            if (priceComparison == 0) {
                // If prices are equal, compare by cafe name
                return dish1.cafeName.compareTo(dish2.cafeName);
            }

            return priceComparison;
        });
    }

    // Method to add a dish to the priority queue
    public void addDish(String dishName, String cafeName, Float object) {
        Dish dish = new Dish(dishName, cafeName, object);
        priorityQueue.add(dish);
    }

    // Method to retrieve and remove the dish with the lowest price
    public Dish getLowestPriceDish() {
        return priorityQueue.poll();
    }

    // Method to check if the priority queue is empty
    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    // Method to get the size of the priority queue
    public int size() {
        return priorityQueue.size();
    }
    
    /*
     * Implements page/cafe ranking by using the price
     * 
     */
    public void suggestCafesByPrice(String ing, int cat, medina m_obj, cafe21 c_obj, thearch a_obj, float exp_price) {
    	
    	recommendCafes dishpop = new recommendCafes();
    	
    	if(cat==1) {
    		if(m_obj.app_ingr_to_dish.containsKey(ing) && m_obj.app_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: m_obj.app_ingr_to_dish.get(ing)) {
            		Float price = (Float) m_obj.app_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) m_obj.app_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    		
    		if(c_obj.app_ingr_to_dish.containsKey(ing) && c_obj.app_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: c_obj.app_ingr_to_dish.get(ing)) {
            		Float price = (Float) c_obj.app_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) c_obj.app_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    		
    		if(a_obj.app_ingr_to_dish.containsKey(ing) && a_obj.app_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: a_obj.app_ingr_to_dish.get(ing)) {
            		Float price = (Float) a_obj.app_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) a_obj.app_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    	}
    	else if(cat==2) {
    		if(m_obj.mcr_ingr_to_dish.containsKey(ing) && m_obj.mcr_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: m_obj.mcr_ingr_to_dish.get(ing)) {
            		Float price = (Float) m_obj.mcr_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) m_obj.mcr_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    		
    		if(c_obj.mcr_ingr_to_dish.containsKey(ing) && c_obj.mcr_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: c_obj.mcr_ingr_to_dish.get(ing)) {
            		Float price = (Float) c_obj.mcr_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) c_obj.mcr_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    		
    		if(a_obj.mcr_ingr_to_dish.containsKey(ing) && a_obj.mcr_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: a_obj.mcr_ingr_to_dish.get(ing)) {
            		Float price = (Float) a_obj.mcr_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) a_obj.mcr_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    	}
    	else if(cat==3) {
    		if(m_obj.bev_ingr_to_dish.containsKey(ing) && m_obj.bev_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: m_obj.bev_ingr_to_dish.get(ing)) {
            		Float price = (Float) m_obj.bev_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) m_obj.bev_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    		
    		if(c_obj.bev_ingr_to_dish.containsKey(ing) && c_obj.bev_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: c_obj.bev_ingr_to_dish.get(ing)) {
            		Float price = (Float) c_obj.bev_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
            			dishpop.addDish(dish, (String) c_obj.bev_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    		
//    		System.out.println("a obj contains ing: " + a_obj.bev_ingr_to_dish.containsKey(ing));
//    		
//    		for (Map.Entry<String, Object[]> set : a_obj.bev_dish_to_price.entrySet()) {
//			 
//		          // Printing all elements of a Map
//		          System.out.print(set.getKey() + " Price is: $" + set.getValue()[0] + " Cafe is: " + set.getValue()[1]);
//		          System.out.println();
//			}
//			
//			System.out.println();
//    		
//		        for (Entry<String, Set<String>> set : a_obj.bev_ingr_to_dish.entrySet()) {			 
//	          // Printing all elements of a Map
//	          System.out.print(set.getKey() + " - Bev: ");
//	          for(String s: set.getValue()) {
//	       	   System.out.print(s + " , ");
//	          }
//	          System.out.println();
//	       }
			
    		if(a_obj.bev_ingr_to_dish.containsKey(ing) && a_obj.bev_ingr_to_dish.get(ing).size()>0) {
    			for(String dish: a_obj.bev_ingr_to_dish.get(ing)) {
//    				System.out.println("in");
            		Float price = (Float) a_obj.bev_dish_to_price.get(dish)[0];
            		if(price<=exp_price) {
//            			System.out.println("in2");
            			dishpop.addDish(dish, (String) a_obj.bev_dish_to_price.get(dish)[1] , price);
            		}
            	}
    		}
    	}
    	
    	// Retrieving and displaying the dish with the lowest price
    	System.out.println();
    	if(dishpop.size()==0) {
    		System.out.println("NO RECOMMENDATIONS BASED ON THE EXPECTED PRICE.");
    		System.out.println("NO WORRIES! WE HAVE SUGGESTIONS FOR YOU, AS REFERENCE, BASED ON THE POPULARUTY OF THE INGREDIENT!");
    		System.out.println("HOPE IT HELPS YOU IN THE FUTURE!");
    		System.out.println();
    		return;
    	}
    	
    	System.out.println("Below are the recommendations! ");
    	System.out.println();
    	System.out.println("RECOMMENDATION BY PRICE (LOW TO HIGH)");
		System.out.println("------------------------------------------------------");
		System.out.println();
    	System.out.println("DISH\t\t\t\t\t\t\t\t\t\t CAFE\t\t\t\t\t PRICE");
    	System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
    	while (!dishpop.isEmpty()) {
    	    Dish lowestPriceDish = dishpop.getLowestPriceDish();
    	    System.out.printf("%-70s %-50s $%.2f%n", lowestPriceDish.dishName, lowestPriceDish.cafeName, lowestPriceDish.price);
    	}
    }
    
    private void addDishesToQueue(String ing, Map<String, Set<String>> ingrToDish, Map<String, Object[]> dishToPrice, ingredientCounterBST bst, recommendCafes dishpop) {
        if (ingrToDish.containsKey(ing) && ingrToDish.get(ing).size() > 0) {
            for (String dish : ingrToDish.get(ing)) {
                if (bst.getCount(ing) > 0) {
                    Float pop = (float) bst.getCount(ing);
                    dishpop.addDish(dish, (String) dishToPrice.get(dish)[1], -pop);
                }
            }
        }
    }
    
    /*
     * Implements page/cafe ranking by using the price
     * 
     */
    public void suggestCafesByPopularity(String ing, int cat, ingredientCounterBST bst_1, ingredientCounterBST bst_2, ingredientCounterBST bst_3, medina m_obj, cafe21 c_obj, thearch a_obj) {
    	
    	recommendCafes dishpop = new recommendCafes();
    	
    	if (cat == 1) {
            addDishesToQueue(ing, m_obj.app_ingr_to_dish, m_obj.app_dish_to_price, bst_1, dishpop);
            addDishesToQueue(ing, c_obj.app_ingr_to_dish, c_obj.app_dish_to_price, bst_2, dishpop);
            addDishesToQueue(ing, a_obj.app_ingr_to_dish, a_obj.app_dish_to_price, bst_3, dishpop);
        } else if (cat == 2) {
            addDishesToQueue(ing, m_obj.mcr_ingr_to_dish, m_obj.mcr_dish_to_price, bst_1, dishpop);
            addDishesToQueue(ing, c_obj.mcr_ingr_to_dish, c_obj.mcr_dish_to_price, bst_2, dishpop);
            addDishesToQueue(ing, a_obj.mcr_ingr_to_dish, a_obj.mcr_dish_to_price, bst_3, dishpop);
        } else {
            addDishesToQueue(ing, m_obj.bev_ingr_to_dish, m_obj.bev_dish_to_price, bst_1, dishpop);
            addDishesToQueue(ing, c_obj.bev_ingr_to_dish, c_obj.bev_dish_to_price, bst_2, dishpop);
            addDishesToQueue(ing, a_obj.bev_ingr_to_dish, a_obj.bev_dish_to_price, bst_3, dishpop);
        }
    	
    	// Retrieving and displaying the dish with the lowest price
    	System.out.println();
		System.out.println("RECOMMENDATION BY POPULARITY (HIGH TO LOW)");
		System.out.println("------------------------------------------------------");
    	System.out.println();
    	System.out.println("RANK\t\tDISH\t\t\t\t\t\t\t\t\t CAFE");
    	System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
    	int rank = 1;
    	Map<String, Float> cafeRanks = new HashMap<>(); // Map to store cafe ranks
    	while (!dishpop.isEmpty()) {
    		Dish lowestPriceDish = dishpop.getLowestPriceDish();
            String cafeName = lowestPriceDish.cafeName;

            // Print cafe rank only if it's the first occurrence of the cafe in the loop
            if (!cafeRanks.containsKey(cafeName)) {
                cafeRanks.put(cafeName, (float) rank);
                System.out.printf("%-5d\t\t%-70s %-50s%n", rank, lowestPriceDish.dishName, cafeName);
                rank++;
            } else {
                System.out.printf("%-5s\t\t%-70s %-50s%n", "", lowestPriceDish.dishName, cafeName);
            }
    	}
    }
    
    public static void main(String[] args) {
        // Example usage of DishPriorityQueue
//    	recommendCafes dishQueue = new recommendCafes();
//
//        // Adding dishes to the priority queue
//        dishQueue.addDish("Pasta", "Italian Cafe", 9.99f);
//        dishQueue.addDish("Burger", "Fast Food Joint", 5.99f);
//        dishQueue.addDish("Sushi", "Sushi Bar", 12.99f);
//
//        // Retrieving and displaying the dish with the lowest price
//        while (!dishQueue.isEmpty()) {
//            Dish lowestPriceDish = dishQueue.getLowestPriceDish();
//            System.out.println("Dish: " + lowestPriceDish.dishName +
//                               ", Cafe: " + lowestPriceDish.cafeName +
//                               ", Price: $" + lowestPriceDish.price);
//        }
    }

}
