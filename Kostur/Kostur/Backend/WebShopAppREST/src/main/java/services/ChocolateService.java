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
			ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
		}
	}
	
	public ChocolateService() {
	}

	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Chocolate createChocolate(ChocolateDTO chocolateDto) {  //dodati za ulogovanog manager-a
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        User loggedInManager = new User("pera","pera","pera","Pera","Peric",Gender.Male,new Date(),Role.Manager,new ArrayList<>(), new Basket(),0,new CustomerType(), new Factory());
        Chocolate createdchocolate = dao.save(chocolateDto, loggedInManager);
        return createdchocolate;
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
	public Chocolate updateProduct(@PathParam("id") Long id, Chocolate chocolate) {
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		return dao.updateChocolate(id, chocolate);
	}
}
