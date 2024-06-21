package beans;

import java.util.ArrayList;
import java.util.Date;

import enums.Gender;
import enums.Role;

public class User {
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Gender gender;
	private Date birthday;
	private Role role;
	private ArrayList<Purchase> purchases; //customer
	private int points; //customer
	private CustomerType type; //customer
	private Factory factory; //manager
	private boolean isBlocked;
	
	
	public User() {
		super();
		this.purchases = new ArrayList<>();
	}


	public User(Long id, String username, String password, String firstName, String lastName,
			Gender gender, Date birthday, Role role, int points,
			CustomerType type, Factory factory, boolean isBlocked) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
		this.points = points;
		this.type = type;
		this.factory = factory;
		this.isBlocked = isBlocked;
		this.purchases =  new ArrayList<>();
	}
	
	public String toStringForFile() {
        return id + ";" + username + ";" + password + ";" + firstName + ";" +  lastName + ";" + gender + ";" + birthday + ";" +
        		role + ";"+ points + ";" + 
        		(type != null ? type.getId() : -1) + ";" +
               (factory != null ? factory.getId() : -1) + ";" + isBlocked;
    }
	
	public User(Long id) {
		super();
		this.id = id;
	}

	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}



	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	public ArrayList<Purchase> getPurchases() {
		return purchases;
	}


	public void setPurchases(ArrayList<Purchase> purchases) {
		this.purchases = purchases;
	}



	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}


	public CustomerType getType() {
		return type;
	}


	public void setType(CustomerType type) {
		this.type = type;
	}


	public Factory getFactory() {
		return factory;
	}


	public void setFactory(Factory factory) {
		this.factory = factory;
	}


	public boolean isBlocked() {
		return isBlocked;
	}


	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	
}
