package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import beans.Basket;
import beans.Chocolate;
import beans.Factory;
import beans.Purchase;
import beans.PurchaseItem;
import beans.User;
import dto.ChocolateDTO;
import dto.PurchaseDTO;
import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;
import enums.PurchaseStatus;

public class PurchaseDAO {
	private HashMap<Long, Purchase> purchases = new HashMap<Long, Purchase>();
	private String contextPath;
	
	public PurchaseDAO() {
		
	}
	
	public PurchaseDAO(String contextPath) {
		this.contextPath = contextPath;
		loadPurchases(contextPath);
	}
	
	public HashMap<Long, Purchase> getPurchases() {
        return purchases;
    }
	
	public Collection<Purchase> findAll() {
		return purchases.values();
	}
	

	private void loadPurchases(String contextPath) {
	    BufferedReader in = null;
	    try {
	        File file = new File(contextPath + "/purchases.txt");
	        System.out.println(file.getCanonicalPath());
	        in = new BufferedReader(new FileReader(file));
	        String line;
	        while ((line = in.readLine()) != null) {
	            System.out.println("Reading line: " + line);
	            line = line.trim();
	            if (line.equals("") || line.indexOf('#') == 0)
	                continue;
	            
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            
	            StringTokenizer st = new StringTokenizer(line, ";");
	            String id = st.nextToken().trim();
	            String purchaseDateAndTime = st.nextToken().trim();
	            String price = st.nextToken().trim();
	            String userId = st.nextToken().trim();
	            String status = st.nextToken().trim();

	            long purchaseId = Long.parseLong(id);
	            double priceDouble = Double.parseDouble(price);
	            long userIdLong = Long.parseLong(userId);
	            PurchaseStatus purchaseStatus = PurchaseStatus.valueOf(status);
	            
	            SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
                Date parsedPurchaseDate = inputDateFormat.parse(purchaseDateAndTime);

	            
	            User user = new User(userIdLong);
	            Purchase purchase = new Purchase(purchaseId, new ArrayList<>(), parsedPurchaseDate, priceDouble, user, purchaseStatus);
	            
	            purchases.put(purchaseId, purchase);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (in != null) {
	            try {
	                in.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	public Purchase save(PurchaseDTO dto, Long userId) {
		Purchase purchase = dto.ConvertToPurchase();
		Long maxId = -1L;
		for (Long id : purchases.keySet()) {
			if (id > maxId) { 
	            maxId = id;
	        }
		}
		maxId++;
		purchase.setId(maxId);
		
		UserDAO userDAO = new UserDAO(contextPath);
		User user = userDAO.findById(userId);
		
		
		purchase.setPurchaseDateAndTime(new Date());
		purchase.setStatus(PurchaseStatus.Processing);
		purchase.setUser(user);
		purchases.put(purchase.getId(), purchase);
			
		return purchase;
	}

	
	public Collection<Purchase> findAllByUserId(Long userId) {
		List<Purchase> userPurchases = new ArrayList<>();
        for (Purchase purchase : purchases.values()) {
            if (purchase.getUser().getId().equals(userId)) {
            	userPurchases.add(purchase);
            }
        }
        return userPurchases; 
    }
}
