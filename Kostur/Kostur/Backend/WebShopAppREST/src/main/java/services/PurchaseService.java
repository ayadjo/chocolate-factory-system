package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Basket;
import beans.Chocolate;
import beans.Factory;
import beans.Purchase;
import dao.BasketDAO;
import dao.ChocolateDAO;
import dao.FactoryDAO;
import dao.PurchaseDAO;
import dto.ChocolateDTO;
import dto.PurchaseDTO;

@Path("/purchases")
public class PurchaseService {
	
	@Context
    ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("purchaseDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	PurchaseDAO purchaseDao = new PurchaseDAO(contextPath); 
            ctx.setAttribute("purchaseDAO", purchaseDao);
		}
	}
	
	public PurchaseService() {
	}
	
	@GET
	@Path("/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Purchase> findAllByUserId(@PathParam("userId") Long userId) {
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		return dao.findAllByUserId(userId);
	}
	
	@POST
	@Path("/{userId}/{basketId}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PurchaseDTO createPurchase(@PathParam("userId") Long userId, @PathParam("basketId") Long basketId) {  
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		Purchase createdPurchase = dao.save(userId, basketId);
		
		if (createdPurchase == null) {
			return null;
		}
        
        return PurchaseDTO.convertToDTO(createdPurchase);
    }
	
	@GET
	@Path("/factory/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Purchase> findAllByFactoryId(@PathParam("factoryId") Long factoryId) {
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		return dao.findAllByFactoryId(factoryId);
	}
	
}
