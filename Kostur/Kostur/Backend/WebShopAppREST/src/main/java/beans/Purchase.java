package beans;

import java.util.ArrayList;

import enums.PurchaseStatus;

public class Purchase {
	private Long id;  //10 karaktera
	private Factory factory;
	private ArrayList<Chocolate> chocolates;
	private String purchaseDateAndTime;
	private int price;
	private User user;
	private PurchaseStatus status;
	
	public Purchase() {
		super();
	}

	public Purchase(Long id, Factory factory, ArrayList<Chocolate> chocolates, String purchaseDateAndTime, int price,
			User user, PurchaseStatus status) {
		super();
		this.id = id;
		this.factory = factory;
		this.chocolates = chocolates;
		this.purchaseDateAndTime = purchaseDateAndTime;
		this.price = price;
		this.user = user;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public ArrayList<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<Chocolate> chocolates) {
		this.chocolates = chocolates;
	}

	public String getPurchaseDateAndTime() {
		return purchaseDateAndTime;
	}

	public void setPurchaseDateAndTime(String purchaseDateAndTime) {
		this.purchaseDateAndTime = purchaseDateAndTime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PurchaseStatus getStatus() {
		return status;
	}

	public void setStatus(PurchaseStatus status) {
		this.status = status;
	}
	
}
