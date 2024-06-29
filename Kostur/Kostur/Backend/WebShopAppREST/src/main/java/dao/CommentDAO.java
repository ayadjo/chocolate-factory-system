package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Basket;
import beans.Chocolate;
import beans.Comment;
import beans.Factory;
import beans.Location;
import beans.Purchase;
import beans.User;
import dto.CommentDTO;
import dto.LocationDTO;
import enums.CommentStatus;

public class CommentDAO {
	private HashMap<Long, Comment> comments = new HashMap<Long, Comment>();
	private String contextPath;
	
	public CommentDAO() {
		
	}
	
	public CommentDAO(String contextPath) {
		this.contextPath = contextPath;
		loadComments(contextPath);
	}
	
	public HashMap<Long, Comment> getComments() {
        return comments;
    }
	
	public Collection<Comment> findAll() {
		return comments.values();
	}
	
	public Comment save(CommentDTO commentDTO) {
		Comment comment = new Comment();
		Long maxId = -1L; 
		for (Long id : comments.keySet()) {
			if (id > maxId) {
				maxId = id;
			}
		}
		maxId++;
		comment.setId(maxId);
		
		UserDAO userDAO = new UserDAO(contextPath);
		User user = userDAO.findById(commentDTO.getUserId());
		comment.setUser(user);
		
		FactoryDAO factoryDAO = new FactoryDAO(contextPath);
		Factory factory = factoryDAO.findById(commentDTO.getFactoryId());
		comment.setFactory(factory);
		
		comment.setText(commentDTO.getText());
		comment.setGrade(commentDTO.getGrade());
		comment.setStatus(CommentStatus.Proccessing);
		comment.setDeleted(false);
		
		PurchaseDAO purchaseDAO = new PurchaseDAO(contextPath);
		purchaseDAO.updateHasComment(commentDTO.getPurchaseId());
		
		comments.put(maxId, comment);
		System.out.println("Saving comment: " + comment.toStringForFile());
	    writeToFile();
		return comment;
	}
	
	public Comment findById(Long id) {
        return comments.get(id);
    }
	

	private void loadComments(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/comments.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", userId = "", factoryId = "", text = "", grade = "", status = "", isDeleted = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					userId = st.nextToken().trim();
					factoryId = st.nextToken().trim();
					text = st.nextToken().trim();
					grade = st.nextToken().trim();
					status = st.nextToken().trim();
				    isDeleted = st.nextToken().trim();
				}
				
				UserDAO userDAO = new UserDAO(contextPath);
        		HashMap<Long, User> users = userDAO.getUsers();
        		User user = users.get(Long.parseLong(userId));
                if (user == null) {
                    user = new User(Long.parseLong(userId));
                }
                
                FactoryDAO factoryDAO = new FactoryDAO(contextPath);
        		HashMap<Long, Factory> factories = factoryDAO.getFactories();
        		Factory factory = factories.get(Long.parseLong(factoryId));
                if (factory == null) {
                	factory = new Factory(Long.parseLong(factoryId));
                }
				
				comments.put(Long.parseLong(id), new Comment(Long.parseLong(id), user, factory, text, Double.parseDouble(grade), CommentStatus.valueOf(status), Boolean.parseBoolean(isDeleted)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
		
	}
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "comments.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (Comment comment : comments.values()) {
	            String commentData = comment.toStringForFile();
	            out.write(commentData + "\n");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (out != null) {
	            try {
	                out.close();
	            } catch (Exception e) {}
	        }
	    }
	}
	
	public Collection<Comment> findByFactoryId(Long factoryId) {
		loadComments(contextPath);
	    List<Comment> comments = new ArrayList<>();
	    Collection<Comment> allComments = findAll();

	    for (Comment c : allComments) {
	        if (c.getFactory().getId().equals(factoryId) && !c.isDeleted()) {
	            comments.add(c);
	        }
	    }

	    return comments;
	}
	
	public Collection<Comment> findApprovedByFactoryId(Long factoryId) {
		loadComments(contextPath);
	    List<Comment> comments = new ArrayList<>();
	    Collection<Comment> allComments = findAll();

	    for (Comment c : allComments) {
	        if (c.getFactory().getId().equals(factoryId) && !c.isDeleted() && c.getStatus().equals(CommentStatus.Approved)) {
	            comments.add(c);
	        }
	    }

	    return comments;
	}
	
	public Comment approveComment(Long id) {
		Comment comment = findById(id);
		comment.setStatus(CommentStatus.Approved);
		writeToFile();
		
		return comment;
	}
	
	public Comment rejectComment(Long id) {
		Comment comment = findById(id);
		comment.setStatus(CommentStatus.Rejected);
		writeToFile();
		
		return comment;
	}
	
}
