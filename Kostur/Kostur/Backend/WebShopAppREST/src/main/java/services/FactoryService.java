package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;

import beans.Chocolate;
import beans.Factory;
import beans.Location;
import beans.Purchase;
import dao.ChocolateDAO;
import dao.FactoryDAO;
import dao.LocationDAO;
import dao.PurchaseDAO;
import dto.ChocolateDTO;
import dto.FactoryDTO;
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
	
	@POST
	@Path("/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public FactoryDTO saveFactory(FactoryDTO factoryDTO, @PathParam("locationId") Long locationId) {  
        FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        
        LocationDAO locationDAO = (LocationDAO) ctx.getAttribute("locationDAO");
	    Location location = locationDAO.findById(locationId);
        
	    Factory factory = factoryDAO.save(factoryDTO, location);
        
        if (factory == null) {
			return null;
		}
        
        return FactoryDTO.convertToDTO(factory);
    }
	
	@GET
	@Path("/combined")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> getCombinedResults(@QueryParam("name") String name,
	                                        @QueryParam("chocolateName") String chocolateName,
	                                        @QueryParam("location") String location,
	                                        @QueryParam("grade") Integer grade,
	                                        @QueryParam("chocolateType") String chocolateType,
	                                        @QueryParam("chocolateKind") String chocolateKind,
	                                        @QueryParam("isOpen") Boolean isOpen,
	                                        @QueryParam("sortOrder") String sortOrder) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.getCombinedResults(name, chocolateName, location, grade, chocolateType, chocolateKind, isOpen, sortOrder);
	}
	
	@PUT
	@Path("grade/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Factory updateFactoryAverageRating(@PathParam ("id") Long id){
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.updateFactoryAverageRating(id);
	}
	
	@PATCH
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Factory delete(@PathParam("id") Long id) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
		return dao.deleteFactory(id);
	}
	
	@GET
	@Path("/sortBy/{attribute}/{order}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> sortByAttribute(@PathParam("attribute") String attribute, @PathParam("order") String order) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDAO");
	    return dao.sortByAttribute(attribute, order);
	}
}
