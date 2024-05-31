package dto;

import beans.Chocolate;
import enums.ChocolateKind;
import enums.ChocolateType;

public class ChocolateDTO {
	private Long id;
	private String name;
	private double price;
	private ChocolateKind kind;
	private ChocolateType type;
	private double weight;
	private String description;
	private String image;
	
	
	public ChocolateDTO() {
		super();
	}

	
	public ChocolateDTO(Long id, String name, double price, ChocolateKind kind, ChocolateType type, double weight,
			String description, String image) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.kind = kind;
		this.type = type;
		this.weight = weight;
		this.description = description;
		this.image = image;
	}



	public Chocolate ConvertToChocolate() {
		Chocolate chocolate = new Chocolate();
		chocolate.setName(name);
		chocolate.setPrice(price);
		chocolate.setKind(kind);
		chocolate.setType(type);
		chocolate.setWeight(weight);
		chocolate.setDescription(description);
		chocolate.setImage(image);
		return chocolate;
	}
	
	public static ChocolateDTO convertToDTO(Chocolate chocolate) {
		ChocolateDTO dto = new ChocolateDTO();
		dto.id = chocolate.getId();
		dto.name = chocolate.getName();
		dto.price = chocolate.getPrice();
		dto.kind = chocolate.getKind();
		dto.type = chocolate.getType();
		dto.weight = chocolate.getWeight();
		dto.description = chocolate.getDescription();
		dto.image = chocolate.getImage();
		return dto;
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


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
}
