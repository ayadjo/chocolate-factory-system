package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;

import beans.Chocolate;
import beans.Factory;
import beans.Location;
import dao.ChocolateDAO;
import dao.FactoryDAO;
import dao.LocationDAO;
import dto.ChocolateDTO;
import dto.LocationDTO;
import enums.ChocolateType;

@Path("/factories")
public class FactoryService {
	
	@Context
	ServletContext ctx;
	

	public FactoryService() {
	}
	
	@PostConstruct
	public void init() {
			
		if(ctx.getAttribute("factoryDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("factoryDAO", new FactoryDAO(contextPath));
		}
		
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> findAll(){
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Factory findById(@PathParam ("id") Long id){
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.findById(id);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> search(
	        @QueryParam("name") @DefaultValue("") String name,
	        @QueryParam("chocolateName") @DefaultValue("") String chocolateName,
	        @QueryParam("location") @DefaultValue("") String location,
	        @QueryParam("grade") double grade) {
	    FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
	    return dao.search(name, chocolateName, location, grade);
	}
	
	@GET
	@Path("/sortAscending")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> sortAscending(){
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.findAllSortedAscending();
	}
	
	@GET
	@Path("/sortDescending")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> sortDescending(){
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.findAllSortedDescending();
	}
	
	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> filter(
			@QueryParam("chocolateType") String chocolateType,
	        @QueryParam("chocolateKind") String chocolateKind,
	        @QueryParam("isOpen") Boolean isOpen){
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.filter(chocolateType, chocolateKind, isOpen);
	}
	

}
