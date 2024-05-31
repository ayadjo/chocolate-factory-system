package beans;

import java.util.ArrayList;

public class Basket {
	private Long id;
	private User user;
	private ArrayList<Chocolate> chocolates;
	int price;
	
	public Basket() {
		chocolates = new ArrayList<Chocolate>();
	}

	public Basket(Long id, User user, ArrayList<Chocolate> chocolates, int price) {
		super();
		this.id = id;
		this.user = user;
		this.chocolates = chocolates;
		this.price = price;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	

}
