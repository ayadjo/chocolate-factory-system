package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.CustomerType;
import beans.Location;


public class CustomerTypeDAO {
private HashMap<Long, CustomerType> types = new HashMap<Long, CustomerType>();
	
	public CustomerTypeDAO() {
		
	}
	
	public CustomerTypeDAO(String contextPath) {
		loadTypes(contextPath);
	}
	
	public HashMap<Long, CustomerType> getTypes() {
        return types;
    }
	
	public Collection<CustomerType> findAll() {
		return types.values();
	}
	
	public CustomerType findByName(String name) {
        for (CustomerType type : types.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
	
	private void loadTypes(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/customerTypes.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", discount = "", requiredPoints = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					name = st.nextToken().trim();
					discount = st.nextToken().trim();
					requiredPoints = st.nextToken().trim();
				}
				types.put(Long.parseLong(id), new CustomerType(Long.parseLong(id), name, Double.parseDouble(discount), Integer.parseInt(requiredPoints)));
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
	
	public Collection<String> findAllCustomerTypes() {
	    Set<String> customerTypes = new HashSet<>();
	    for (CustomerType type : types.values()) {
	    	customerTypes.add(type.getName());
	    }
	    return customerTypes;
	}
}
