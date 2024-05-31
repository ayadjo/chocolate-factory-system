package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.Factory;
import beans.User;
import dto.ChocolateDTO;
import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class ChocolateDAO {
	private HashMap<Long, Chocolate> chocolates = new HashMap<Long, Chocolate>();
	
	public ChocolateDAO() {
		
	}
	
	public ChocolateDAO(String contextPath) {
		loadChocolates(contextPath);
	}
	
	
	public Collection<Chocolate> findAll() {
		return chocolates.values();
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
	
	public Chocolate save(ChocolateDTO dto, User loggedInManager) {
		Chocolate chocolate = dto.ConvertToChocolate();
	    Long maxId = -1L; 
	    for (Long id : chocolates.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    chocolate.setId(maxId);
	    
	    chocolate.setFactory(loggedInManager.getFactory());
	    loggedInManager.getFactory().getChocolates().add(chocolate);
	    chocolate.setStatus(ChocolateStatus.outOfStock);
		
	    chocolates.put(maxId, chocolate); 
	    
	    return chocolate;
	}
	
	public Chocolate delete(Long id) {
		Chocolate foundChocolate = findById(id);
		if (foundChocolate == null) {
			return null;
		}
		foundChocolate.setDeleted(true);
		foundChocolate.getFactory().getChocolates().remove(foundChocolate);
		

		return foundChocolate;
	}
	
	public Chocolate updateChocolate(Long id, Chocolate chocolate) {
		Chocolate c = chocolates.containsKey(id) ? chocolates.get(id) : null;
		if (c == null) {
			return null;
		} else {
			c.setName(chocolate.getName());
	        c.setPrice(chocolate.getPrice());
	        c.setKind(chocolate.getKind());
	        c.setFactory(chocolate.getFactory());
	        c.setType(chocolate.getType());
	        c.setWeight(chocolate.getWeight());
	        c.setDescription(chocolate.getDescription());
	        c.setStatus(chocolate.getStatus());
	        c.setOnStock(chocolate.getOnStock());
	        c.setImage(chocolate.getImage());
	        c.setDeleted(false);
		}
		
		return c;
	}

}
