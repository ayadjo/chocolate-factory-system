package beans;

import java.util.ArrayList;

public class BasketItem {
    private Chocolate chocolate;
    private int quantity;
    private Long basketId;
    
    public BasketItem() {
	}
    
	public BasketItem(Chocolate chocolate, int quantity, Long basketId) {
		super();
		this.chocolate = chocolate;
		this.quantity = quantity;
		this.basketId = basketId;
	}
	
	
	public Long getBasketId() {
		return basketId;
	}

	public void setBasketId(Long basketId) {
		this.basketId = basketId;
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

    
}