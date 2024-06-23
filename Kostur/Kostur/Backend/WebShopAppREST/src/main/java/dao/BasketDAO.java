package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
	
	
	private Long nextId() {
		long id = 0;
		for (Basket basket : baskets.values()) {
			if (basket.getId() > id) {
				id = basket.getId();
			}
		}
		return id + 1;
	}
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "baskets.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (Basket basket : baskets.values()) {
	            String basketData = basket.toStringForFile();
	            System.out.println("Writing basket data: " + basketData); 
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
	
	public Basket addChocolateToBasket(Long userId, Long chocolateId, int quantity) {
        Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Chocolate chocolate = chocolateDAO.findById(chocolateId);
        if (chocolate == null) {
            throw new IllegalArgumentException("Chocolate not found: " + chocolateId);
        }

        boolean itemFound = false;
        for (BasketItem item : basket.getItems()) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasketId().equals(basket.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            BasketItem newItem = new BasketItem();
            newItem.setChocolate(chocolate);
            newItem.setQuantity(quantity);
            newItem.setBasketId(basket.getId());
            basket.getItems().add(newItem);
        }


        basket.setPrice(basket.getPrice() + (chocolate.getPrice() * quantity));

        writeToFile();
        
        return basket;
        
    }

	public Basket incrementQuantity(Long userId, Long chocolateId) {
        Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Chocolate chocolate = chocolateDAO.findById(chocolateId);
        if (chocolate == null) {
            throw new IllegalArgumentException("Chocolate not found: " + chocolateId);
        }


        for (BasketItem item : basket.getItems()) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasketId().equals(basket.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                basket.setPrice(basket.getPrice() + item.getChocolate().getPrice());
                break;
            }
        }

        writeToFile();
        
        return basket;        
    }

	
	public Basket decrementQuantity(Long userId, Long chocolateId) {
        Basket basket = findByUserId(userId);
        if (basket == null) {
            throw new IllegalArgumentException("Basket not found for user: " + userId);
        }
        
        ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Chocolate chocolate = chocolateDAO.findById(chocolateId);
        if (chocolate == null) {
            throw new IllegalArgumentException("Chocolate not found: " + chocolateId);
        }


        for (BasketItem item : basket.getItems()) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasketId().equals(basket.getId())) {
                item.setQuantity(item.getQuantity() - 1);
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

        for (BasketItem item : basket.getItems()) {
            if (item.getChocolate().getId().equals(chocolateId) && item.getBasketId().equals(basket.getId())) {            	
                basket.setPrice(basket.getPrice() - item.getChocolate().getPrice()*item.getQuantity());
                basket.getItems().remove(item);
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
        basket.getItems().clear();

        return basket;
	        
	 }
}
