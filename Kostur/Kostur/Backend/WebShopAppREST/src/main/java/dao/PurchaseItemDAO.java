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
import beans.PurchaseItem;

public class PurchaseItemDAO {
	private HashMap<Long, PurchaseItem> items = new HashMap<Long, PurchaseItem>();
	private String contextPath;
	
	public PurchaseItemDAO() {
		
	}
	
	public PurchaseItemDAO(String contextPath) {
		this.contextPath = contextPath;
		loadItems(contextPath);
	}
	
	public HashMap<Long, PurchaseItem> getBaskets() {
        return items;
    }
	
	public Collection<PurchaseItem> findAll() {
		return items.values();
	}
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "purchaseItems.txt";
	        System.out.println(this.contextPath + "purchaseItems.txt");
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (PurchaseItem item : items.values()) {
	            String purchaseItem = item.toStringForFile();
	            System.out.println("Writing item: " + purchaseItem);
	            out.write(purchaseItem + "\n");    
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
            
            ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
            HashMap<Long, Chocolate> chocolates = chocolateDAO.getChocolates();
			
			File file = new File(contextPath + "/purchaseItems.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", chocolateId = "", quantity = "", purchaseId = "";
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
					purchaseId = st.nextToken().trim();
				}
				
				Chocolate chocolate = chocolates.get(Long.parseLong(chocolateId));
                if (chocolate == null) {
                	chocolate = new Chocolate(Long.parseLong(chocolateId));
                }
                
				
				items.put(Long.parseLong(id), new PurchaseItem(Long.parseLong(id), chocolate, Integer.parseInt(quantity), Long.parseLong(purchaseId)));
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
	
	public PurchaseItem save(PurchaseItem item, Chocolate chocolate) {
		System.out.println("save method in PurchaseItem");
	    Long maxId = -1L; 
	    for (Long id : items.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    item.setId(maxId);
	    System.out.println("item.id = "+ maxId);
	    
	   
	    if(chocolate != null) {
	        item.setChocolate(chocolate);
	    } else {
	    	throw new IllegalArgumentException("Chocolate " + chocolate.getId() + " does not exist.");
	    }
		
	    items.put(maxId, item); 
	    item.toStringForFile();
	    System.out.println("item "+ item);
	    writeToFile();
	    return item;
	}
}
