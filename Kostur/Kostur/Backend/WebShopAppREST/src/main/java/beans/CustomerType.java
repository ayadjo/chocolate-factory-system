package beans;

public class CustomerType {
	private Long id;
	private String name;
	private double discount;
	private int requiredPoints;
	
	public CustomerType() {
		super();
	}

	public CustomerType(Long id, String name, double discount, int requiredPoints) {
		super();
		this.id = id;
		this.name = name;
		this.discount = discount;
		this.requiredPoints = requiredPoints;
	}
	
	

	public CustomerType(Long id) {
		super();
		this.id = id;
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

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getRequiredPoints() {
		return requiredPoints;
	}

	public void setRequiredPoints(int requiredPoints) {
		this.requiredPoints = requiredPoints;
	}
	
	
}
