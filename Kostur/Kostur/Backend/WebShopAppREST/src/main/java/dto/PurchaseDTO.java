package dto;

import java.util.ArrayList;

import beans.Chocolate;
import beans.Purchase;
import beans.PurchaseItem;
import beans.User;

public class PurchaseDTO {
	private Long id; 
	private ArrayList<PurchaseItem> items;
	private double price;
		
	
	public PurchaseDTO() {
		super();
	}
	
	public PurchaseDTO(Long id, double price) {
		super();
		this.id = id;
		this.items = new ArrayList<>();
		this.price = price;
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
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public Purchase ConvertToPurchase() {
		Purchase purchase = new Purchase();
		purchase.setItems(items);
		purchase.setPrice(price);
		return purchase;
	}
	
	public static PurchaseDTO convertToDTO(Purchase purchase) {
		PurchaseDTO dto = new PurchaseDTO();
		dto.id = purchase.getId();
		dto.items = purchase.getItems();
		dto.price = purchase.getPrice();
		return dto;
	}
	
}
