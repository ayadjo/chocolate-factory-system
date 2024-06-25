package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Basket;
import beans.BasketItem;
import beans.Chocolate;
import beans.Factory;
import beans.Location;
import beans.User;
import dto.FactoryDTO;
import dto.UpdateUserDTO;

public class BasketItemDAO {
	private HashMap<Long, BasketItem> items = new HashMap<Long, BasketItem>();
	private String contextPath;
	
	public BasketItemDAO() {
		
	}
	
	public BasketItemDAO(String contextPath) {
		this.contextPath = contextPath;
		loadItems(contextPath);
	}
	
	public HashMap<Long, BasketItem> getBaskets() {
        return items;
    }
	
	public Collection<BasketItem> findAll() {
		return items.values();
	}
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "basketItems.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (BasketItem item : items.values()) {
	            String basketItem = item.toStringForFile();
	            out.write(basketItem + "\n");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (out != null) {
	            try {
	                out.close();
	            } catch (Exception e) {}
	        }
	    }
	}
	

	private void loadItems(String contextPath) {
		BufferedReader in = null;
		try {  
			
			BasketDAO basketDAO = new BasketDAO(contextPath);
            HashMap<Long, Basket> baskets = basketDAO.getBaskets();
            
            ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
            HashMap<Long, Chocolate> chocolates = chocolateDAO.getChocolates();
			
			File file = new File(contextPath + "/basketItems.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", chocolateId = "", quantity = "", basketId = "", isDeleted = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					chocolateId = st.nextToken().trim();
					quantity = st.nextToken().trim();
					basketId = st.nextToken().trim();
					isDeleted = st.nextToken().trim();
				}
				
				Chocolate chocolate = chocolates.get(Long.parseLong(chocolateId));
                if (chocolate == null) {
                	chocolate = new Chocolate(Long.parseLong(chocolateId));
                }
                
                Basket basket = baskets.get(Long.parseLong(basketId));
                if (basket == null) {
                	basket = new Basket(Long.parseLong(basketId));
                }
				
				items.put(Long.parseLong(id), new BasketItem(Long.parseLong(id), chocolate, Integer.parseInt(quantity), basket, Boolean.parseBoolean(isDeleted)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
		
	}
	
	public BasketItem save(BasketItem item, Chocolate chocolate, Basket basket) {
	    Long maxId = -1L; 
	    for (Long id : items.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    item.setId(maxId);
	    
	   
	    if(chocolate != null) {
	        item.setChocolate(chocolate);
	    } else {
	    	throw new IllegalArgumentException("Chocolate " + chocolate.getId() + " does not exist.");
	    }
	    
	    if(basket != null) {
	        item.setBasket(basket);
	    } else {
	    	throw new IllegalArgumentException("Basket " + basket.getId() + " does not exist.");
	    }
	  
	    item.setDeleted(false);
		
	    items.put(maxId, item); 
	    item.toStringForFile();
	    writeToFile();
	    return item;
	}
	
	public BasketItem updateQuantity(Long id, int quantity){
		BasketItem item = items.containsKey(id) ? items.get(id) : null;
		if (item == null) {
			return null;
		} else {
	        item.setQuantity(quantity);
		}
		writeToFile();
		
		return item;
	}
	
	public void removeItem(Long id){
		BasketItem item = items.containsKey(id) ? items.get(id) : null;
		if (item == null) {
			return;
		} else {
	        item.setDeleted(true);
		}
		writeToFile();
	}
	
	public Collection<BasketItem> findByBasketId(Long basketId) {
	    List<BasketItem> foundItems = new ArrayList<>();
	    Collection<BasketItem> allItems = items.values();

	    for (BasketItem i : allItems) {
	        if (i.getBasket().getId().equals(basketId) && i.getIsDeleted() == false) {
	        	foundItems.add(i);
	        }
	    }

	    return foundItems;
	}
}
