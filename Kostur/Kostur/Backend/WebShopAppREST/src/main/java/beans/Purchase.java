package beans;

import java.util.ArrayList;
import java.util.Date;

import enums.PurchaseStatus;

public class Purchase {
	private Long id;  //10 karaktera
	private ArrayList<PurchaseItem> items;
	private Date purchaseDateAndTime;
	private double price;
	private User user;
	private PurchaseStatus status;
	
	public Purchase() {
		super();
	}

	public Purchase(Long id, ArrayList<PurchaseItem> items, Date purchaseDateAndTime, double price,
			User user, PurchaseStatus status) {
		super();
		this.id = id;
		this.items = items;
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

	public ArrayList<PurchaseItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<PurchaseItem> items) {
		this.items = items;
	}

	public Date getPurchaseDateAndTime() {
		return purchaseDateAndTime;
	}

	public void setPurchaseDateAndTime(Date purchaseDateAndTime) {
		this.purchaseDateAndTime = purchaseDateAndTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
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
