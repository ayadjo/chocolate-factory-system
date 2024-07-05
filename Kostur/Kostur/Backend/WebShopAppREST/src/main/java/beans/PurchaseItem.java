package beans;

public class PurchaseItem {
	private Long id;
    private Chocolate chocolate;
    private int quantity;
    private Long purchaseId;
    
    
	public PurchaseItem() {
		super();
	}
	
	public PurchaseItem(Long id, Chocolate chocolate, int quantity, Long purchaseId) {
		super();
		this.id = id;
		this.chocolate = chocolate;
		this.quantity = quantity;
		this.purchaseId = purchaseId;
	}
	
	public String toStringForFile() {
        return this.id + ";" + (this.chocolate != null ? this.chocolate.getId() : "-1") + ";" + this.quantity + ";" + 
        	   this.purchaseId;
    }
	

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
