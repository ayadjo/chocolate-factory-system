package beans;

public class Location {
	private Long id;
	private double longitude;
	private double latitude;
	private String address;
	
	public Location() {
		super();
	}

	public Location(Long id, double longitude, double latitude, String address) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}
	

	public Location(Long id) {
		super();
		this.id = id;
	}
	
	public String toStringForFile() {
        return id + ";" + longitude + ";" + latitude + ";" + address;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
