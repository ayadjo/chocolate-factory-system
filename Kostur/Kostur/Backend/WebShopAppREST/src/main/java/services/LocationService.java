package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Location;
import dao.FactoryDAO;
import dao.LocationDAO;
import dto.LocationDTO;

@Path("/locations")
public class LocationService {

	@Context
	ServletContext ctx;
	

	public LocationService() {
	}
	
	@PostConstruct
	public void init() {
			
		if(ctx.getAttribute("locationDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("locationDAO", new LocationDAO(contextPath));
		}
		
	}
	
	@POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public LocationDTO saveLocation(LocationDTO locationDto) {
        LocationDAO dao = (LocationDAO) ctx.getAttribute("locationDAO");
        
        Location savedLocation = dao.save(locationDto);
        
        if (savedLocation == null) {
            return null;
        }
        
        return LocationDTO.convertToDTO(savedLocation);
    }
}
