package beans;


public class BasketItem {
	private Long id;
    private Chocolate chocolate;
    private int quantity;
    private Basket basket;
    private boolean isDeleted;
    
	
	public BasketItem() {
		super();
	}


	public BasketItem(Long id, Chocolate chocolate, int quantity, Basket basket, boolean isDeleted) {
		super();
		this.id = id;
		this.chocolate = chocolate;
		this.quantity = quantity;
		this.basket = basket;
		this.isDeleted = isDeleted;
	}

	public String toStringForFile() {
        return id + ";" + (chocolate != null ? chocolate.getId() : "-1") + ";" + quantity + ";" + 
        		(basket != null ? basket.getId() : "-1") + ";" + isDeleted;
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


	public Basket getBasket() {
		return basket;
	}


	public void setBasket(Basket basket) {
		this.basket = basket;
	}


	public boolean getIsDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	

    
}