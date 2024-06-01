package beans;

import enums.ChocolateKind;

import javax.json.bind.annotation.JsonbTransient;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class Chocolate {
	private Long id;
	private String name;
	private double price;
	private ChocolateKind kind;
	//@JsonbTransient
	private Factory factory;
	private ChocolateType type;
	private double weight;
	private String description;
	private ChocolateStatus status;
	private int onStock;
	private String image;
	private boolean isDeleted;
	

	public Chocolate() {
		super();
	}



	public Chocolate(Long id, String name, double price, ChocolateKind kind, Factory factory, ChocolateType type,
			double weight, String description, ChocolateStatus status, int onStock, String image, boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.kind = kind;
		this.factory = factory;
		this.type = type;
		this.weight = weight;
		this.description = description;
		this.status = status;
		this.onStock = onStock;
		this.image = image;
		this.isDeleted = isDeleted;
	}

	public String toStringForFile() {
        return id + ";" + name + ";" + price + ";" + kind + ";" + 
               (factory != null ? factory.getId() : "null") + ";" + type + ";" + weight + ";" + 
               description + ";" + status + ";" + onStock + ";" + image + ";" + isDeleted;
    }


	public boolean isDeleted() {
		return isDeleted;
	}



	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}



	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public ChocolateKind getKind() {
		return kind;
	}


	public void setKind(ChocolateKind kind) {
		this.kind = kind;
	}


	public Factory getFactory() {
		return factory;
	}


	public void setFactory(Factory factory) {
		this.factory = factory;
	}


	public ChocolateType getType() {
		return type;
	}


	public void setType(ChocolateType type) {
		this.type = type;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public ChocolateStatus getStatus() {
		return status;
	}


	public void setStatus(ChocolateStatus status) {
		this.status = status;
	}


	public int getOnStock() {
		return onStock;
	}


	public void setOnStock(int onStock) {
		this.onStock = onStock;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}
	
}
