package dto;

import beans.Factory;
import beans.User;

public class CommentDTO {
	private Long userId;
	private Long factoryId;
	private String text;
	double grade;
	private Long purchaseId;
	
	
	public CommentDTO() {
		super();
	}


	public CommentDTO(Long userId, Long factoryId, String text, double grade, Long purchaseId) {
		super();
		this.userId = userId;
		this.factoryId = factoryId;
		this.text = text;
		this.grade = grade;
		this.purchaseId = purchaseId;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Long getFactoryId() {
		return factoryId;
	}


	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public double getGrade() {
		return grade;
	}


	public void setGrade(double grade) {
		this.grade = grade;
	}


	public Long getPurchaseId() {
		return purchaseId;
	}


	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}
	
	
}
