package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	public Collection<Purchase> findAllByFactoryId(Long factoryId) {
		List<Purchase> factoryPurchases = new ArrayList<>();
		boolean factoryItem = false;
        for (Purchase purchase : purchases.values()) {		
        	for (PurchaseItem item : purchase.getItems()) {  
        		if (item.getChocolate().getFactory().getId().equals(factoryId)) { 
        			factoryItem = true;
        		}
        	}
        	if(factoryItem) {
        		factoryPurchases.add(purchase);  
        	}
        }
        
        List<Purchase> updatedFactoryPurchases = new ArrayList<>();
        for (Purchase purchase : factoryPurchases) {		//prodji kroz sve kupovine fabrike
        	purchase.setPrice(0);
        	ArrayList<PurchaseItem> updatedItems = new ArrayList<>();
        	for (PurchaseItem item : purchase.getItems()){
        		if (item.getChocolate().getFactory().getId().equals(factoryId)) {
        			purchase.setPrice(purchase.getPrice() + item.getChocolate().getPrice()*item.getQuantity());
        			updatedItems.add(item);
        		}	
        	}
        	purchase.setItems(null);
        	purchase.setItems(updatedItems);
        	updatedFactoryPurchases.add(purchase);
        }
        
        return updatedFactoryPurchases; 
    }
	
	public Collection<Purchase> sortByAttribute(String attribute, String order) {
	    Stream<Purchase> stream = purchases.values().stream();
	    Comparator<Purchase> comparator;

	    switch (attribute) {
	        case "price":
	            comparator = Comparator.comparing(Purchase::getPrice);
	            break;
	        case "date":
	            comparator = Comparator.comparing(Purchase::getPurchaseDateAndTime);
	            break;
	        /*case "factoryName":
	            comparator = Comparator.comparing(p -> p.getFactory().getName());
	            break;*/
	        default:
	            throw new IllegalArgumentException("Unknown attribute: " + attribute);
	    }

	    if ("desc".equals(order)) {
	        comparator = comparator.reversed();
	    }

	    return stream.sorted(comparator).collect(Collectors.toList());
	}

	 public Collection<Purchase> searchPurchases(String factoryName, Double priceFrom, Double priceTo, String dateFrom, String dateTo) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	        List<Date> dateFromParsed = new ArrayList<>();
	        List<Date> dateToParsed = new ArrayList<>();
	        
	        try {
	            if (dateFrom != null) {
	                dateFromParsed.add(sdf.parse(dateFrom));
	            }
	            if (dateTo != null) {
	                dateToParsed.add(sdf.parse(dateTo));
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        
	        return purchases.values().stream()
	            //.filter(p -> factoryName == null || p.getFactory().getName().toLowerCase().contains(factoryName.toLowerCase()))
	            .filter(p -> priceFrom == null || p.getPrice() >= priceFrom)
	            .filter(p -> priceTo == null || p.getPrice() <= priceTo)
	            .filter(p -> dateFromParsed.isEmpty() || p.getPurchaseDateAndTime().compareTo(dateFromParsed.get(0)) >= 0)
	            .filter(p -> dateToParsed.isEmpty() || p.getPurchaseDateAndTime().compareTo(dateToParsed.get(0)) <= 0)
	            .collect(Collectors.toList());
	    }

	
}
