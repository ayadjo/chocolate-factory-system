package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Chocolate;
import beans.Factory;
import beans.Location;
import enums.ChocolateKind;
import enums.ChocolateType;

public class FactoryDAO {
	private HashMap<Long, Factory> factories = new HashMap<Long, Factory>();
	private String contextPath;
	
	public FactoryDAO() {
		
	}
	
    public FactoryDAO(String contextPath) {
    	this.contextPath = contextPath;
		loadFactories(contextPath);
	}
	
	
	public HashMap<Long, Factory> getFactories() {
        return factories;
    }
	
	public Collection<Factory> findAll() {
		List<Factory> sortedFactories = new ArrayList<>(factories.values());
		
		Collections.sort(sortedFactories, new Comparator<Factory>() {
			@Override
			public int compare(Factory f1, Factory f2) {
				return Boolean.compare(f2.isOpen(), f1.isOpen());
			}
		});
		
		return sortedFactories;
	}
	
	public Factory findById(Long id) {
        return factories.get(id);
    }
	
	private void loadFactories(String contextPath) {
		BufferedReader in = null;
		try {
			
			LocationDAO locationDAO = new LocationDAO(contextPath);
            HashMap<Long, Location> locations = locationDAO.getLocations();
			
			File file = new File(contextPath + "/factories.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", startTime = "", endTime = "", isOpen = "", locationId = "", grade = "", logo = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					name = st.nextToken().trim();
					startTime = st.nextToken().trim();
					endTime = st.nextToken().trim();
					isOpen = st.nextToken().trim();
					locationId = st.nextToken().trim();
					grade = st.nextToken().trim();
					logo = st.nextToken().trim();
				}
				
				Location location = locations.get(Long.parseLong(locationId));
                if (location == null) {
                    location = new Location(Long.parseLong(locationId));
                }

                factories.put(Long.parseLong(id), new Factory(Long.parseLong(id), name, startTime, endTime, Boolean.parseBoolean(isOpen), location, Double.parseDouble(grade), logo));
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
	
	
	public Collection<Factory> search(String name, String chocolateName, String location, double grade) {
	    return factories.values().stream()
	            .filter(f -> (name.isEmpty() || f.getName().toLowerCase().contains(name.toLowerCase())) &&
	                         (location.isEmpty() || f.getLocation().getAddress().toLowerCase().contains(location.toLowerCase())) &&
	                         (f.getGrade() >= grade) &&
	                         (chocolateName.isEmpty() || hasChocolateWithName(f.getId(), chocolateName.toLowerCase())))
	            .collect(Collectors.toList());
	}

	private boolean hasChocolateWithName(Long factoryId, String chocolateName) {
		ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
		
	    Collection<Chocolate> chocolates = chocolateDAO.findByFactoryId(factoryId);
	    return chocolates.stream()
	                     .anyMatch(c -> c.getName().toLowerCase().contains(chocolateName));
	}
	
    public Collection<Factory> findAllSortedAscending() {
        return findAllSorted(true);
    }

    public Collection<Factory> findAllSortedDescending() {
        return findAllSorted(false);
    }

    private List<Factory> findAllSorted(boolean ascending) {
        List<Factory> sortedFactories = new ArrayList<>(factories.values());

        Comparator<Factory> comparator = Comparator.comparing(Factory::getName)
                .thenComparing(f -> f.getLocation().getAddress())
                .thenComparing(Factory::getGrade);

        if (!ascending) {
            comparator = comparator.reversed();
        }

        Collections.sort(sortedFactories, comparator);
        return sortedFactories;
    }
    
    public Collection<Factory> filter(String chocolateType, String chocolateKind, boolean isOpen) {
        return factories.values().stream()
                .filter(f -> (chocolateType.isEmpty() || hasChocolateType(f.getId(), chocolateType)) &&
                             (chocolateType.isEmpty() || chocolateKind.isEmpty() || hasChocolateKind(f.getId(), chocolateKind)) &&
                             (isOpen ? f.isOpen() : !f.isOpen()))
                .collect(Collectors.toList());
    }

    private boolean hasChocolateType(Long factoryId, String chocolateType) {
    	ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Collection<Chocolate> chocolates = chocolateDAO.findByFactoryId(factoryId);
        return chocolates.stream()
        		.anyMatch(c -> c.getType().name().equals(chocolateType));
    }

    private boolean hasChocolateKind(Long factoryId, String chocolateKind) {
    	ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
        Collection<Chocolate> chocolates = chocolateDAO.findByFactoryId(factoryId);
        return chocolates.stream()
        		.anyMatch(c -> c.getKind().name().equals(chocolateKind));
    }

	
}
