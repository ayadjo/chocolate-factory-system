package beans;

import java.util.ArrayList;

public class Basket {
	private Long id;
	private User user;
	private ArrayList<Chocolate> chocolates;
	private Double price;
	
	public Basket() {
		chocolates = new ArrayList<Chocolate>();
	}

	public Basket(Long id, User user, Double price) {
		super();
		this.id = id;
		this.user = user;
		this.price = price;
		this.chocolates = new ArrayList<>();
	}
	
	

	public Basket(Long id) {
		super();
		this.id = id;
	}

	public String toStringForFile() {
        return id + ";"  + (user != null ? user.getId() : "null") + ";" + price;
    }
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<Chocolate> chocolates) {
		this.chocolates = chocolates;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	

}
