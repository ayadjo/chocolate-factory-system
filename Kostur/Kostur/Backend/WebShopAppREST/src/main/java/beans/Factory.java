package beans;

import java.util.ArrayList;
import beans.Chocolate;
import beans.Location;

public class Factory {
	private Long id;
	private String name;
	private ArrayList<Chocolate> chocolates;
	private String startTime;
	private String endTime;
	private boolean isOpen;
	private Location location;
	private double grade;
	private String logo;
	private boolean isDeleted;
	
	public Factory() {
		super();
		this.chocolates = new ArrayList<>();
	}
	
	public Factory(Long id) {
		super();
		this.chocolates = new ArrayList<>();
		this.id = id;
	}


	public Factory(Long id, String name, String startTime, String endTime, boolean isOpen, Location location,
			double grade, String logo, boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isOpen = isOpen;
		this.location = location;
		this.grade = grade;
		this.logo = logo;
		this.chocolates = new ArrayList<>();
		this.isDeleted = isDeleted;
	}
	
	public String toStringForFile() {
        return id + ";" + name + ";" + startTime + ";" + endTime + ";" + isOpen + ";" +
               (location != null ? location.getId() : "-1") + ";" + grade + ";" + logo + ";" + isDeleted;
    }


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<Chocolate> chocolates) {
		this.chocolates = chocolates;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
