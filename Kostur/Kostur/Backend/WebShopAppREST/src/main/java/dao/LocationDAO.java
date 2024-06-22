package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.Factory;
import beans.Location;
import beans.Product;
import dto.LocationDTO;

public class LocationDAO {
	private HashMap<Long, Location> locations = new HashMap<Long, Location>();
	private String contextPath;
	
	public LocationDAO() {
		
	}
	
	public LocationDAO(String contextPath) {
		this.contextPath = contextPath;
		loadLocations(contextPath);
	}
	
	public HashMap<Long, Location> getLocations() {
        return locations;
    }
	
	public Collection<Location> findAll() {
		return locations.values();
	}
	
	public Location save(LocationDTO locationDto) {
		Location location = locationDto.ConvertToLocation();
		Long maxId = -1L; 
		for (Long id : locations.keySet()) {
			if (id > maxId) {
				maxId = id;
			}
		}
		maxId++;
		location.setId(maxId);
		locations.put(maxId, location);
		System.out.println("Saving location: " + location.toStringForFile());
	    writeToFile();
		return location;
	}
	
	public Location findById(Long id) {
        return locations.get(id);
    }
	

	private void loadLocations(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/locations.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", longitude = "", latitude = "", address = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					longitude = st.nextToken().trim();
					latitude = st.nextToken().trim();
					address = st.nextToken().trim();
				}
				locations.put(Long.parseLong(id), new Location(Long.parseLong(id), Double.parseDouble(longitude), Double.parseDouble(latitude), address));
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
	        String filePath = this.contextPath + "locations.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (Location location : locations.values()) {
	            String locationData = location.toStringForFile();
	            System.out.println("Writing location data: " + locationData); 
	            out.write(locationData + "\n");
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
}
