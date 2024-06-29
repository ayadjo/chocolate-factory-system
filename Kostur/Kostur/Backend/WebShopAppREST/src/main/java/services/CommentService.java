package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Chocolate;
import beans.Comment;
import beans.Purchase;
import dao.ChocolateDAO;
import dao.CommentDAO;
import dao.PurchaseDAO;
import dto.CommentDTO;

@Path("/comments")
public class CommentService {
	@Context
    ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("commentDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	CommentDAO commentDAO = new CommentDAO(contextPath); 
            ctx.setAttribute("commentDAO", commentDAO);
		}	
	}
	
	public CommentService() {
	}
	
	@POST
	@Path("/addComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment createComment(CommentDTO commentDTO) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		return commentDAO.save(commentDTO);
	}
	
	@GET
	@Path("/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> findAllByFactoryId(@PathParam("factoryId") Long factoryId){
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		return commentDAO.findByFactoryId(factoryId);
	}
	
	@GET
	@Path("/getApproved/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> findApprovedByFactoryId(@PathParam("factoryId") Long factoryId){
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		return commentDAO.findApprovedByFactoryId(factoryId);
	}
	
	@PATCH
	@Path("/approve/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment approveComment(@PathParam("commentId") Long commentId) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		return commentDAO.approveComment(commentId);
	}
	
	@PATCH
	@Path("/reject/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment rejectComment(@PathParam("commentId") Long commentId) {
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		return commentDAO.rejectComment(commentId);
	}
}
