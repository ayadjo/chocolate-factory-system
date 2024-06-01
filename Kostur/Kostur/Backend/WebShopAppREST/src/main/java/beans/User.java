package beans;

import java.util.ArrayList;
import java.util.Date;

import enums.Gender;
import enums.Role;

public class User {
	private String username;
	private String password;
	private String confirmationPassword;
	private String firstName;
	private String lastName;
	private Gender gender;
	private Date birthday;
	private Role role;
	private ArrayList<Purchase> purchases;
	private Basket basket;
	private int points;
	private CustomerType type;
	private Factory factory;
	
	
	
	public User() {
		super();
	}


	public User(String username, String password, String confirmationPassword, String firstName, String lastName,
			Gender gender, Date birthday, Role role, ArrayList<Purchase> purchases, Basket basket, int points,
			CustomerType type, Factory factory) {
		super();
		this.username = username;
		this.password = password;
		this.confirmationPassword = confirmationPassword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
		this.purchases = purchases;
		this.basket = basket;
		this.points = points;
		this.type = type;
		this.factory = factory;
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


	public String getConfirmationPassword() {
		return confirmationPassword;
	}


	public void setConfirmationPassword(String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
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


	public Basket getBasket() {
		return basket;
	}


	public void setBasket(Basket basket) {
		this.basket = basket;
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
	
	
}
