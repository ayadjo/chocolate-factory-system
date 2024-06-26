package beans;

import java.util.ArrayList;

import enums.CommentStatus;

public class Comment {
	private Long id;
	private User user;
	private Factory factory;
	private String text;
	double grade;
	private CommentStatus status;
	private boolean isDeleted;
	
	public Comment() {
		super();
	}

	public Comment(Long id, User user, Factory factory, String text, double grade, CommentStatus status, boolean isDeleted) {
		super();
		this.id = id;
		this.user = user;
		this.factory = factory;
		this.text = text;
		this.grade = grade;
		this.status = status;
		this.isDeleted = isDeleted;
	}
	
	public String toStringForFile() {
        return id + ";" + (user != null ? user.getId() : "-1") + ";" +
               (factory != null ? factory.getId() : "-1") + ";" + 
               text + ";" + grade + ";" + status + ";" + isDeleted;
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


	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
}
