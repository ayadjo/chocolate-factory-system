package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.ws.rs.QueryParam;

import beans.Chocolate;
import beans.Factory;
import beans.Location;
import dto.ChocolateDTO;
import dto.FactoryDTO;
import enums.ChocolateKind;
import enums.ChocolateStatus;
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
	
	public Factory save(FactoryDTO dto, Location location) {
		Factory factory = dto.ConvertToFactory();
	    Long maxId = -1L; 
	    for (Long id : factories.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    factory.setId(maxId);
	    
	   
	    if(location != null) {
	        factory.setLocation(location);
	    } else {
	    	throw new IllegalArgumentException("Location " + location.getId() + " does not exist.");
	    }
	  
	    factory.setOpen(true);
	    factory.setGrade(0);
		
	    factories.put(maxId, factory); 
	    System.out.println("Saving factory: " + factory.toStringForFile());
	    writeToFile();
	    return factory;
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
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "factories.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (Factory factory : factories.values()) {
	            String factoryData = factory.toStringForFile();
	            System.out.println("Writing factory data: " + factoryData); 
	            out.write(factoryData + "\n");
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

    public List<Factory> getCombinedResults(String name,
                                            String chocolateName,
                                            String location,
                                            Integer grade,
                                            String chocolateType,
                                            String chocolateKind,
                                            Boolean isOpen,
                                            String sortOrder) {
        
        Collection<Factory> results = findAll();

        // Primjena filtera
        if (chocolateType != null && !chocolateType.isEmpty()) {
            results = results.stream()
                    .filter(factory -> hasChocolateType(factory.getId(), chocolateType))
                    .collect(Collectors.toList());
        }
        if (chocolateKind != null && !chocolateKind.isEmpty()) {
            results = results.stream()
                    .filter(factory -> hasChocolateKind(factory.getId(), chocolateKind))
                    .collect(Collectors.toList());
        }
        if (isOpen != null) {
            results = results.stream()
                    .filter(factory -> factory.isOpen() == isOpen)
                    .collect(Collectors.toList());
        }

        // Primjena pretrage
        if (name != null && !name.isEmpty()) {
            results = results.stream()
                    .filter(factory -> factory.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (chocolateName != null && !chocolateName.isEmpty()) {
            results = results.stream()
                    .filter(factory -> hasChocolateWithName(factory.getId(), chocolateName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (location != null && !location.isEmpty()) {
            results = results.stream()
                    .filter(factory -> factory.getLocation().getAddress().toLowerCase().contains(location.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (grade != null) {
            results = results.stream()
                    .filter(factory -> factory.getGrade() >= grade)
                    .collect(Collectors.toList());
        }

        // Primjena sortiranja
        if (sortOrder != null && !sortOrder.isEmpty()) {
            Comparator<Factory> comparator = Comparator.comparing(Factory::getName)
                    .thenComparing(f -> f.getLocation().getAddress())
                    .thenComparing(Factory::getGrade);
            
            if ("desc".equalsIgnoreCase(sortOrder)) {
                comparator = comparator.reversed();
            }

            results = results.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>(results);
    }

}
