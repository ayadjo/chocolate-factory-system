package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.CustomerTypeDAO;

@Path("/types")
public class CustomerTypeService {
	@Context
    ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("typeDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	CustomerTypeDAO typeDAO = new CustomerTypeDAO(contextPath); 
            ctx.setAttribute("typeDAO", typeDAO);
		}	
	}
	
	public CustomerTypeService() {
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> findAllChocolateTypes(){
		CustomerTypeDAO typeDAO = (CustomerTypeDAO) ctx.getAttribute("typeDAO");
		return typeDAO.findAllCustomerTypes();
	}
}
