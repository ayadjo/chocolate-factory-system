package beans;

import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class Chocolate {
	private Long id;
	private String name;
	private double price;
	private ChocolateKind kind;
	//private Factory factory;
	private ChocolateType type;
	private double weight;
	private String description;
	private ChocolateStatus status;
	private int onStock;
}
