package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.BasketItem;
import beans.Chocolate;
import dao.BasketDAO;
import dao.BasketItemDAO;
import dao.ChocolateDAO;

@Path("/basketItems")
public class BasketItemService {
	@Context
    ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("basketItemDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	BasketItemDAO itemDAO = new BasketItemDAO(contextPath); 
            ctx.setAttribute("basketItemDAO", itemDAO);
		}	
	}
	
	public BasketItemService() {
	}
	
	@GET
	@Path("/byBasket/{basketId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<BasketItem> findByBasketId(@PathParam("basketId") Long basketId){
		BasketItemDAO itemDAO = (BasketItemDAO) ctx.getAttribute("basketItemDAO");
		return itemDAO.findByBasketId(basketId);
	}
}
