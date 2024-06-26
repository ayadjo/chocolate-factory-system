package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import beans.BasketItem;
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

public class ChocolateDAO {
	private HashMap<Long, Chocolate> chocolates = new HashMap<Long, Chocolate>();
	private String contextPath;
	
	public ChocolateDAO() {
	}
	
	public ChocolateDAO(String contextPath) {
		this.contextPath = contextPath;
		loadChocolates(contextPath);
	}
	
	
	public Collection<Chocolate> findAll() {
		return chocolates.values();
	}
	
	public HashMap<Long, Chocolate> getChocolates() {
        return chocolates;
    }
	
	
	public Chocolate findById(Long id) {
		return chocolates.containsKey(id) ? chocolates.get(id) : null;
	}
	
	private void loadChocolates(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolates.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", price = "", kind = "", factoryId = "", type = "", weight = "", description = "", status = "", onStock = "", image = "", isDeleted = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				System.out.println("Reading line: " + line);
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
	                id = st.nextToken().trim();
	                name = st.nextToken().trim();
	                price = st.nextToken().trim();
	                kind = st.nextToken().trim();
	                factoryId = st.nextToken().trim();
	                type = st.nextToken().trim();
	                weight = st.nextToken().trim();
	                description = st.nextToken().trim();
	                status = st.nextToken().trim();
	                onStock = st.nextToken().trim();
	                image = st.nextToken().trim();
	                isDeleted = st.nextToken().trim();
				}
				chocolates.put(Long.parseLong(id), new Chocolate(Long.parseLong(id), name, Double
						.parseDouble(price), ChocolateKind.valueOf(kind), new Factory(Long.parseLong(factoryId)), ChocolateType.valueOf(type), Double.parseDouble(weight), description, ChocolateStatus.valueOf(status), Integer.parseInt(onStock), image, Boolean.parseBoolean(isDeleted)));
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
	
	public Chocolate save(ChocolateDTO dto, Factory factory) {
		Chocolate chocolate = dto.ConvertToChocolate();
	    Long maxId = -1L; 
	    for (Long id : chocolates.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    chocolate.setId(maxId);
	    
	   
	    if(factory != null) {
	        chocolate.setFactory(factory);
	    } else {
	    	throw new IllegalArgumentException("Factory " + factory.getId() + " does not exist.");
	    }
	    chocolate.setStatus(ChocolateStatus.outOfStock);
		
	    chocolates.put(maxId, chocolate); 
	    System.out.println("Saving chocolate: " + chocolate.toStringForFile());
	    writeToFile();
	    return chocolate;
	}
	
	public Chocolate delete(Long id) {
		Chocolate foundChocolate = findById(id);
		if (foundChocolate == null) {
			return null;
		}
		foundChocolate.setDeleted(true);
		foundChocolate.getFactory().getChocolates().remove(foundChocolate);
		
		writeToFile();

		return foundChocolate;
	}
	
	public Chocolate updateChocolate(Long id, ChocolateDTO dto) {
		Chocolate c = chocolates.containsKey(id) ? chocolates.get(id) : null;
		if (c == null) {
			return null;
		} else {
			c.setName(dto.getName());
	        c.setPrice(dto.getPrice());
	        c.setKind(dto.getKind());
	        c.setType(dto.getType());
	        c.setWeight(dto.getWeight());
	        c.setDescription(dto.getDescription());
	        c.setImage(dto.getImage());
	        c.setDeleted(false);
		}
		writeToFile();
		
		return c;
	}
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "chocolates.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (Chocolate chocolate : chocolates.values()) {
	            String chocolateData = chocolate.toStringForFile();
	            out.write(chocolateData + "\n");
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

	
	public Collection<Chocolate> findByFactoryId(Long factoryId) {
		loadChocolates(contextPath);
	    List<Chocolate> foundChocolates = new ArrayList<>();
	    Collection<Chocolate> allChocolates = chocolates.values();

	    for (Chocolate c : allChocolates) {
	        if (c.getFactory().getId().equals(factoryId) && !c.isDeleted()) {
	            foundChocolates.add(c);
	        }
	    }

	    return foundChocolates;
	}
	
	public Collection<String> findAllChocolateTypes() {
	    Set<String> types = new HashSet<>();
	    for (Chocolate chocolate : chocolates.values()) {
	        types.add(chocolate.getType().toString());
	    }
	    return types;
	}


	public Collection<String> findAllChocolateKinds() {
	    Set<String> kinds = new HashSet<>();
	    for (Chocolate chocolate : chocolates.values()) {
	        kinds.add(chocolate.getKind().toString());
	    }
	    return kinds;
	}

	public Chocolate editQuantity(Long id, int quantity) {
		Chocolate foundChocolate = findById(id);
		if (foundChocolate == null) {
			return null;
		}
		foundChocolate.setOnStock(quantity);
		
		writeToFile();

		return foundChocolate;
	}
	
	public void updateQuantityAfterPurchase(PurchaseDTO purchaseDto){
		Purchase purchase = purchaseDto.ConvertToPurchase();
		
		for (PurchaseItem item : purchase.getItems()) {
	        Chocolate chocolate = item.getChocolate();
	        int quantity = item.getQuantity();
	        
	  
	        Chocolate storedChocolate = findById(chocolate.getId());
	        if (storedChocolate != null) {
                storedChocolate.setOnStock(storedChocolate.getOnStock() - quantity); 
	        } else {
	            throw new RuntimeException("Chocolate with ID: " + chocolate.getId() + " not found in the database.");
	        }
	    }
		
	}
	
	public Chocolate decrementOnStock(Long id, int quantity){
		Chocolate chocolate = chocolates.containsKey(id) ? chocolates.get(id) : null;
		if (chocolate == null) {
			return null;
		} else {
			int oldOnStock = chocolate.getOnStock();
	        chocolate.setOnStock(chocolate.getOnStock() - quantity);
	        if((oldOnStock - quantity)==0) {
	        	chocolate.setStatus(ChocolateStatus.outOfStock);
	        }
		}
		writeToFile();
		
		return chocolate;
	}
	
	public Chocolate incrementOnStock(Long id, int quantity){
		Chocolate chocolate = chocolates.containsKey(id) ? chocolates.get(id) : null;
		if (chocolate == null) {
			return null;
		} else {
			int oldOnStock = chocolate.getOnStock();
	        chocolate.setOnStock(chocolate.getOnStock() + quantity);
	        if((oldOnStock + quantity)>0) {
	        	chocolate.setStatus(ChocolateStatus.onStock);
	        }
		}
		writeToFile();
		
		return chocolate;
	}
	
	public Collection<Chocolate> findByFactoryIdForCustomers(Long factoryId) {
		loadChocolates(contextPath);
	    List<Chocolate> foundChocolates = new ArrayList<>();
	    Collection<Chocolate> allChocolates = chocolates.values();
	    
	    for (Chocolate c : allChocolates) {
	        if (c.getStatus().equals(ChocolateStatus.onStock) && c.getFactory().getId().equals(factoryId) && !c.isDeleted()) {
	            foundChocolates.add(c);
	        }
	    }

	    return foundChocolates;
	}
	
	
}
