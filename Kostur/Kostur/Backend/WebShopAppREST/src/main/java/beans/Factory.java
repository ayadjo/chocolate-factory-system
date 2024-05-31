package beans;


public class Factory {
	private Long id;
	private String name;
	private ArrayList<Chocolate> chocolates;
	private String startTime;
	private String endTime;
	private boolean isOpen;
	private Location location;
	private double grade;
	
	public Factory() {
		super();
		chocolates =  new ArrayList<Chocolate>();
	}
	
	
	
}
