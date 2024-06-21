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
		basket.setId(nextId());
		basket.setPrice((double) 0);
		baskets.put(basket.getId(), basket); 
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
		
}
