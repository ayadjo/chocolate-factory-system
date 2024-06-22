package dto;

import beans.Chocolate;
import beans.Factory;

public class FactoryDTO {
	private Long id;
	private String name;
	private String startTime;
	private String endTime;
	private String logo;
	
	
	
	public FactoryDTO() {
		super();
	}

	public FactoryDTO(Long id,String name, String startTime, String endTime, String logo) {
		super();
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.logo = logo;
	}

	public Factory ConvertToFactory() {
		Factory factory = new Factory();
		factory.setName(name);
		factory.setStartTime(startTime);
		factory.setEndTime(endTime);
		factory.setLogo(logo);
		return factory;
	}
	
	public static FactoryDTO convertToDTO(Factory factory) {
		FactoryDTO dto = new FactoryDTO();
		dto.id = factory.getId();
		dto.name = factory.getName();
		dto.startTime = factory.getStartTime();
		dto.endTime = factory.getEndTime();
		dto.logo = factory.getLogo();
		
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	
}
