package beans;

import java.util.ArrayList;

public class Comment {
	private Long id;
	private User user;
	private Factory factory;
	private String text;
	double grade;
		
	
	public Comment() {
		super();
	}

	public Comment(Long id, User user, Factory factory, String text, double grade) {
		super();
		this.id = id;
		this.user = user;
		this.factory = factory;
		this.text = text;
		this.grade = grade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
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
	
}
