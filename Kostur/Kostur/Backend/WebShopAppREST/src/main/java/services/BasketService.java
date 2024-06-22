package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Basket;
import beans.Chocolate;

import javax.ws.rs.PathParam;

import dao.BasketDAO;

@Path("/baskets")
public class BasketService {
	
	@Context
    ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("basketDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	BasketDAO basketDao = new BasketDAO(contextPath); 
            ctx.setAttribute("basketDAO", basketDao);
		}	
	}
	
	public BasketService() {
	}
	
	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Basket getBasket(@PathParam("userId") Long userId) {
		BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
		return dao.findByUserId(userId);
	}

	
	@PUT
    @Path("addChocolateToBasket/{userId}/{chocolateId}/{quantity}")
    @Produces(MediaType.APPLICATION_JSON)
    public Basket addChocolateToBasket(@PathParam("userId") Long userId, @PathParam("chocolateId") Long chocolateId, @PathParam("quantity") int quantity) {
        BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
        return dao.addChocolateToBasket(userId, chocolateId, quantity);
    }
	
	@PUT
    @Path("incrementQuantity/{userId}/{chocolateId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Basket incrementQuantity(@PathParam("userId") Long userId, @PathParam("chocolateId") Long chocolateId) {
        BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
        return dao.incrementQuantity(userId, chocolateId);
    }
	
	@PUT
    @Path("decrementQuantity/{userId}/{chocolateId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Basket decrementQuantity(@PathParam("userId") Long userId, @PathParam("chocolateId") Long chocolateId) {
        BasketDAO dao = (BasketDAO) ctx.getAttribute("basketDAO");
        return dao.decrementQuantity(userId, chocolateId);
    }
	
}
