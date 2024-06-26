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

import javax.ws.rs.NotFoundException;

import beans.Basket;
import beans.BasketItem;
import beans.Chocolate;
import beans.Factory;
import beans.Purchase;
import beans.PurchaseItem;
import beans.User;
import dto.ChocolateDTO;
import dto.PurchaseDTO;
import dto.RejectPurchaseDTO;
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
	
	public Purchase findById(Long id) {
		return purchases.containsKey(id) ? purchases.get(id) : null;
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
	            String factoryId = st.nextToken().trim();
	            String rejectionNote = st.nextToken().trim();

	            long purchaseId = Long.parseLong(id);
	            double priceDouble = Double.parseDouble(price);
	            long userIdLong = Long.parseLong(userId);
	            PurchaseStatus purchaseStatus = PurchaseStatus.valueOf(status);
	            
	            SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
                Date parsedPurchaseDate = inputDateFormat.parse(purchaseDateAndTime);

	            
	            User user = new User(userIdLong);
	            Purchase purchase = new Purchase(purchaseId, parsedPurchaseDate, priceDouble, user, purchaseStatus,  new Factory(Long.parseLong(factoryId)), rejectionNote);
	            
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

	public Purchase save(Long userId, Long basketId) {
		Purchase purchase = new Purchase();
		Long maxId = -1L;
		for (Long id : purchases.keySet()) {
			if (id > maxId) { 
	            maxId = id;
	        }
		}
		maxId++;
		purchase.setId(maxId);
		
		PurchaseItemDAO purchaseItemDAO = new PurchaseItemDAO(contextPath);
		
		UserDAO userDAO = new UserDAO(contextPath);
		User user = userDAO.findById(userId);
		
		BasketDAO basketDAO = new BasketDAO(contextPath);
		Basket basket = basketDAO.findByUserId(userId);
		
		Long factoryId = -1L;
		
		BasketItemDAO basketItemDAO = new BasketItemDAO(contextPath);
		Collection<BasketItem> items = basketItemDAO.findAll();
		for(BasketItem basketItem : items) {
			if(basketItem.getIsDeleted() == false && basketItem.getBasket().getId().equals(basketId)) {
				PurchaseItem purchaseItem = new PurchaseItem();
				purchaseItem.setPurchaseId(purchase.getId());
				purchaseItem.setQuantity(basketItem.getQuantity());
				purchaseItemDAO.save(purchaseItem, basketItem.getChocolate());
				purchase.getItems().add(purchaseItem);
				factoryId = basketItem.getChocolate().getFactory().getId();
			}
		}
		
		FactoryDAO factoryDAO = new FactoryDAO(contextPath);
		Factory factory = factoryDAO.findById(factoryId);
		
		purchase.setFactory(factory);
		purchase.setPrice(basket.getPrice());
		purchase.setPurchaseDateAndTime(new Date());
		purchase.setStatus(PurchaseStatus.Processing);
		purchase.setUser(user);
		purchases.put(purchase.getId(), purchase);
		writeToFile();
		
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
		PurchaseItemDAO purchaseItemDAO = new PurchaseItemDAO(contextPath);
		Collection<PurchaseItem> items = purchaseItemDAO.findAll();
        for (Purchase purchase : purchases.values()) {	
        	if(purchase.getFactory().getId().equals(factoryId)) {
        		factoryPurchases.add(purchase);
        	}
//        	for (PurchaseItem item : items) {  
//        		if (item.getChocolate().getFactory().getId().equals(factoryId) && item.getPurchaseId().equals(purchase.getId())) { 
//        			factoryItem = true;
//        		}
//        	}
//        	if(factoryItem) {
//        		factoryPurchases.add(purchase);  
//        	}
        }
        
        /*List<Purchase> updatedFactoryPurchases = new ArrayList<>();
        for (Purchase purchase : factoryPurchases) {		
        	purchase.setPrice(0);
        	ArrayList<PurchaseItem> updatedItems = new ArrayList<>();
        	for (PurchaseItem item : items){
        		if (item.getChocolate().getFactory().getId().equals(factoryId)) {
        			purchase.setPrice(purchase.getPrice() + item.getChocolate().getPrice()*item.getQuantity());
        			updatedItems.add(item);
        		}	
        	}
        	purchase.setItems(null);
        	purchase.setItems(updatedItems);
        	updatedFactoryPurchases.add(purchase);
        }*/
        
        return factoryPurchases; //updatedFactoryPurchases; 
    }
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "purchases.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (Purchase purchase : purchases.values()) {
	            String purchaseData = purchase.toStringForFile();
	            System.out.println("Writing purchase data: " + purchaseData); 
	            out.write(purchaseData + "\n");
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

	
	public Purchase rejectPurchase(RejectPurchaseDTO dto) {
		ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
		
		PurchaseItemDAO purchaseItemDAO = new PurchaseItemDAO(contextPath);
		Collection<PurchaseItem> items = purchaseItemDAO.findAll();
		
		for(PurchaseItem item : items) {
			if(item.getPurchaseId().equals(dto.getPurchaseId())) {
				chocolateDAO.incrementOnStock(item.getChocolate().getId(), item.getQuantity());
			}
		}
		
		
		Purchase purchase = findById(dto.getPurchaseId());
		purchase.setStatus(PurchaseStatus.Rejected);
		purchase.setRejectionNote(dto.getRejectionNote());
		writeToFile();
		
		return purchase;
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
	 
	 public Purchase cancelPurchase (Long id) {
		 ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
		 Purchase purchase = findById(id);
		 if (purchase != null) {
	            purchase.setStatus(PurchaseStatus.Cancelled);

	            //writeToFile();
	        } else {
	            // Handle case where purchase is not found
	            throw new NotFoundException("Purchase not found. ");
	        }
		 return purchase;
	 }

	
}
