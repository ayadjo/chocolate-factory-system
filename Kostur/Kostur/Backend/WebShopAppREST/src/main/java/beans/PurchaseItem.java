package beans;

public class PurchaseItem {
    private Chocolate chocolate;
    private int quantity;
    private Long purchaseId;
    
    
	public PurchaseItem() {
		super();
	}
	
	public PurchaseItem(Chocolate chocolate, int quantity, Long purchaseId) {
		super();
		this.chocolate = chocolate;
		this.quantity = quantity;
		this.purchaseId = purchaseId;
	}
	
	public Chocolate getChocolate() {
		return chocolate;
	}
	
	public void setChocolate(Chocolate chocolate) {
		this.chocolate = chocolate;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public Long getPurchaseId() {
		return purchaseId;
	}
	
	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}
    
}
