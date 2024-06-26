package beans;

import java.util.ArrayList;
import java.util.Date;

import enums.PurchaseStatus;

public class Purchase {
	private Long id;  
	private ArrayList<PurchaseItem> items;
	private Date purchaseDateAndTime;
	private double price;
	private User user;
	private PurchaseStatus status;
	private Factory factory;
	private String rejectionNote;
	private boolean hasComment;
	
	public Purchase() {
		super();
		this.items = new ArrayList<>();
	}

	public Purchase(Long id, Date purchaseDateAndTime, double price,
			User user, PurchaseStatus status, Factory factory, String rejectionNote, boolean hasComment) {
		super();
		this.id = id;
		this.purchaseDateAndTime = purchaseDateAndTime;
		this.price = price;
		this.user = user;
		this.status = status;
		this.items = new ArrayList<>();
		this.factory = factory;
		this.rejectionNote = rejectionNote;
		this.hasComment = hasComment;
	}
	
	public String toStringForFile() {
        return id + ";" + purchaseDateAndTime + ";" + price + ";" + 
               (user != null ? user.getId() : "-1") + ";" + status + ";" +
               (factory != null ? factory.getId() : "-1") + ";" + rejectionNote + ";" + hasComment;
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

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public String getRejectionNote() {
		return rejectionNote;
	}

	public void setRejectionNote(String rejectionNote) {
		this.rejectionNote = rejectionNote;
	}

	public boolean isHasComment() {
		return hasComment;
	}

	public void setHasComment(boolean hasComment) {
		this.hasComment = hasComment;
	}
	
	
	
}
