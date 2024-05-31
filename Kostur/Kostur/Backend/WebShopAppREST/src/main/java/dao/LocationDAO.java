package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Location;

public class LocationDAO {
	private HashMap<Long, Location> locations = new HashMap<Long, Location>();
	
	public LocationDAO() {
		
	}
	
	public LocationDAO(String contextPath) {
		loadLocations(contextPath);
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
}
