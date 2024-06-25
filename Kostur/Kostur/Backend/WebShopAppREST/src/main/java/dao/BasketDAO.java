package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Basket;
import beans.BasketItem;
import beans.Chocolate;
import beans.CustomerType;
import beans.Location;
import beans.User;

public class BasketDAO {
	private HashMap<Long, Basket> baskets = new HashMap<Long, Basket>();
	private String contextPath;
	
	public BasketDAO() {
		
	}
	
	public BasketDAO(String contextPath) {
		this.contextPath = contextPath;
		loadBaskets(contextPath);
	}
	
	public HashMap<Long, Basket> getBaskets() {
        return baskets;
    }
	
	public Collection<Basket> findAll() {
		return baskets.values();
	}
	
	public Basket createBasketForUser(User user) {
		Basket basket = new Basket();
		basket.setUser(user);
		Long maxId = -1L; 
	    for (Long id : baskets.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    basket.setId(maxId);
		basket.setPrice((double) 0);
		baskets.put(maxId, basket); 
		writeToFile();
		return basket;
	}
	
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "baskets.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (Basket basket : baskets.values()) {
	            String basketData = basket.toStringForFile();
	            out.write(basketData + "\n");
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
	

	private void loadBaskets(String contextPath) {
		BufferedReader in = null;
		try {  
			
			UserDAO userDAO = new UserDAO(contextPath);
            HashMap<Long, User> users = userDAO.getUsers();
			
			File file = new File(contextPath + "/baskets.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", userId = "", price = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					userId = st.nextToken().trim();
					price = st.nextToken().trim();
				}
				
				User user = users.get(Long.parseLong(userId));
                if (user == null) {
                	user = new User(Long.parseLong(userId));
                }
				
				baskets.put(Long.parseLong(id), new Basket(Long.parseLong(id), user, Double.parseDouble(price)));
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
	
	
	public Basket findByUserId(Long userId) {
        for (Basket basket : baskets.values()) {
            if (basket.getUser().getId().equals(userId)) {
                return basket;
            }
        }
        return null; 
    }
	
	public Chocolate addChocolateToBasket(Long userId, Long chocolateId, int quantity) {
        Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Chocolate chocolate = chocolateDAO.findById(chocolateId);
        if (chocolate == null) {
            throw new IllegalArgumentException("Chocolate not found: " + chocolateId);
        } else if(chocolate.getOnStock() < quantity) {
        	throw new IllegalArgumentException("Not enough chocolate on stock!");
        } else if(chocolate.getOnStock() == 0) {
        	throw new IllegalArgumentException("Chocolate is out of stock!");
        }
        
        BasketItemDAO itemDAO = new BasketItemDAO(contextPath);
        Collection<BasketItem> items = itemDAO.findAll();

        boolean itemFound = false;
        for (BasketItem item : items) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasket().getId().equals(basket.getId()) && item.getIsDeleted() == false) {
                int newQuantity = item.getQuantity() + quantity;
                item = itemDAO.updateQuantity(item.getId(), newQuantity);
                chocolateDAO.decrementOnStock(chocolateId, quantity);
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            BasketItem newItem = new BasketItem();
            newItem.setQuantity(quantity);
            chocolateDAO.decrementOnStock(chocolateId, quantity);
            itemDAO.save(newItem, chocolate, basket);
        }


        basket.setPrice(basket.getPrice() + (chocolate.getPrice() * quantity));

        writeToFile();
        
        return chocolate;
        
    }

	public Basket incrementQuantity(Long userId, Long chocolateId){
        Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Chocolate chocolate = chocolateDAO.findById(chocolateId);
        if (chocolate == null) {
            throw new IllegalArgumentException("Chocolate not found: " + chocolateId);
        } else if(chocolate.getOnStock() == 0) {
        	throw new IllegalArgumentException("Chocolate is out of stock!");
        }

        BasketItemDAO itemDAO = new BasketItemDAO(contextPath);
        Collection<BasketItem> items = itemDAO.findAll();
        for (BasketItem item : items) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasket().getId().equals(basket.getId()) && item.getIsDeleted() == false) {
            	int newQuantity = item.getQuantity() + 1;
            	item = itemDAO.updateQuantity(item.getId(), newQuantity);
            	chocolateDAO.decrementOnStock(chocolateId, 1);
                basket.setPrice(basket.getPrice() + item.getChocolate().getPrice());
                break;
            }
        }

        writeToFile();
        
        return basket;        
    }

	
	public Basket decrementQuantity(Long userId, Long chocolateId){
        Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Chocolate chocolate = chocolateDAO.findById(chocolateId);
        if (chocolate == null) {
            throw new IllegalArgumentException("Chocolate not found: " + chocolateId);
        }

        BasketItemDAO itemDAO = new BasketItemDAO(contextPath);
        Collection<BasketItem> items = itemDAO.findAll();
        for (BasketItem item : items) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasket().getId().equals(basket.getId()) && item.getIsDeleted() == false) {
                int newQuantity = item.getQuantity() - 1;
                item = itemDAO.updateQuantity(item.getId(), newQuantity);
                chocolateDAO.incrementOnStock(chocolateId, 1);
                basket.setPrice(basket.getPrice() - item.getChocolate().getPrice());
                break;
            }
        }

        writeToFile();
        
        return basket;        
    }
	
	public Basket removeChocolateFromBasket(Long userId, Long chocolateId) {
		Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Chocolate chocolate = chocolateDAO.findById(chocolateId);
        if (chocolate == null) {
            throw new IllegalArgumentException("Chocolate not found: " + chocolateId);
        }

        BasketItemDAO itemDAO = new BasketItemDAO(contextPath);
        Collection<BasketItem> items = itemDAO.findAll();
        for (BasketItem item : items) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasket().getId().equals(basket.getId()) && item.getIsDeleted() == false) {            	
                basket.setPrice(basket.getPrice() - item.getChocolate().getPrice()*item.getQuantity());
                chocolateDAO.incrementOnStock(chocolateId, item.getQuantity());
                itemDAO.removeItem(item.getId());
                break;
            }
        }

        writeToFile();
        
		return basket;
	}
	
	 public Basket clearBasket(Long userId) {
        Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        basket.setPrice(0.0);
        
        BasketItemDAO itemDAO = new BasketItemDAO(contextPath);
        Collection<BasketItem> items = itemDAO.findAll();
        for (BasketItem item : items) {
            if (item.getBasket().getId().equals(basket.getId()) && item.getIsDeleted() == false) {
                itemDAO.removeItem(item.getId());
            }
        }
        writeToFile();

        return basket;
	        
	 }
}
