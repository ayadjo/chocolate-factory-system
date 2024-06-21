package dto;

import beans.Chocolate;
import beans.Location;

public class LocationDTO {
	private Long id; 
	private Double longitude;
	private Double latitude;
	private String address;
	
	public LocationDTO() {
		super();
	}

	public LocationDTO(Long id, Double longitude, Double latitude, String address) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}
	
	public Location ConvertToLocation() {
		Location location = new Location();
		location.setLongitude(longitude);
		location.setLatitude(latitude);
		location.setAddress(address);
		return location;
	}
	
	public static LocationDTO convertToDTO(Location location) {
		LocationDTO dto = new LocationDTO();
		dto.id = location.getId();
		dto.longitude = location.getLongitude();
		dto.latitude = location.getLatitude();
		dto.address = location.getAddress();
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
