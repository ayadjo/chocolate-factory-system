package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;

import beans.Basket;
import beans.Chocolate;
import beans.CustomerType;
import beans.Factory;
import beans.Product;
import beans.Purchase;
import beans.User;
import dao.ChocolateDAO;
import dao.FactoryDAO;
import dto.ChocolateDTO;
import enums.Gender;
import enums.Role;

@Path("/chocolates")
public class ChocolateService {
	
	@Context
    ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("chocolateDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	ChocolateDAO chocolateDao = new ChocolateDAO(contextPath); 
            ctx.setAttribute("chocolateDAO", chocolateDao);
		}
		
		/*if (ctx.getAttribute("factoryDAO") == null) {
	        //String contextPath = ctx.getRealPath("");
	        //FactoryDAO factoryDAO = new FactoryDAO(contextPath); 
	        //ctx.setAttribute("factoryDAO", factoryDAO);
	        
	        ChocolateDAO chocolateDao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
			ctx.setAttribute("factoryDAO", new FactoryDAO("factoryDAO", chocolateDao));
	    }*/
	}
	
	public ChocolateService() {
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Chocolate getChocolate(@PathParam("id") Long id) {
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return dao.findById(id);
	}

	
	@POST
	@Path("/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ChocolateDTO addChocolate(ChocolateDTO chocolateDto, @PathParam("factoryId") Long factoryId) {  
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
	    Factory factory = factoryDAO.findById(factoryId);
        Chocolate createdChocolate = dao.save(chocolateDto, factory);
        
        if (createdChocolate == null) {
			return null;
		}
        
        return ChocolateDTO.convertToDTO(createdChocolate);
    }
	
	@PATCH
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Chocolate deleteChocolate(@PathParam("id") Long id) {
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return dao.delete(id);
	}
	
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ChocolateDTO updateProduct(@PathParam("id") Long id, ChocolateDTO chocolateDTO) {
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		Chocolate updatedChocolate = dao.updateChocolate(id, chocolateDTO);
		
		if (updatedChocolate == null) {
			return null;
		}
		
		return ChocolateDTO.convertToDTO(updatedChocolate);
	}
	
	@GET
	@Path("/byFactory/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Chocolate> findAllByFactoryId(@PathParam("factoryId") Long factoryId){
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return dao.findByFactoryId(factoryId);
	}
	
	@GET
	@Path("/types")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> findAllChocolateTypes(){
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return dao.findAllChocolateTypes();
	}
	
	@GET
	@Path("/kinds")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> findAllChocolateKinds(){
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return dao.findAllChocolateKinds();
	}
}
