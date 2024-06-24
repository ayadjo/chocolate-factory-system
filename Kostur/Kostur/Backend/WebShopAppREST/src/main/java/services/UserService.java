package services;

import java.text.ParseException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Chocolate;
import beans.Factory;
import beans.User;
import dao.ChocolateDAO;
import dao.CustomerTypeDAO;
import dao.FactoryDAO;
import dao.UserDAO;
import dto.ChocolateDTO;
import dto.LoginDTO;
import dto.RegisterUserDTO;
import dto.UpdateUserDTO;


@Path("/users")
public class UserService {
	@Context
	ServletContext ctx;
	
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

	public UserService() {
	}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("userDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	 @POST
	 @Path("/createCustomer")
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	public RegisterUserDTO createUser(RegisterUserDTO userDTO) throws ParseException {
        LOGGER.info("Creating user with username: " + userDTO.getUsername());

        UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
        if (userDAO == null) {
            LOGGER.severe("UserDAO not found in context");
            return null;
        }

        User user = userDAO.createCustomer(userDTO);
        if (user == null) {
            LOGGER.warning("User creation failed for username: " + userDTO.getUsername());
            return null;
        }

        LOGGER.info("User created successfully with ID: " + user.getId());
        return RegisterUserDTO.convertToDTO(user);
    }
	 
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RegisterUserDTO loginUser(LoginDTO dto, @Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User user = userDAO.login(dto);
		if (user == null) {
			return null;
		}
		request.getSession().setAttribute("user", user);
		return RegisterUserDTO.convertToDTO(user);
	}
	
	@GET
	@Path("/availableManagers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> findAvailableManagers(){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.findAvailableManagers();
	}
	
	@POST
	@Path("/createManager/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RegisterUserDTO createManager(RegisterUserDTO userDTO, @PathParam("factoryId") Long factoryId) throws ParseException {
       UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
       FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
	    
       Factory factory = factoryDAO.findById(factoryId);
       
       User user = userDAO.createManager(userDTO, factory);
       if (user == null) {
           LOGGER.warning("User creation failed for username: " + userDTO.getUsername());
           return null;
       }

       return RegisterUserDTO.convertToDTO(user);
   }
	
	@PATCH
	@Path("/{managerId}/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public RegisterUserDTO chooseManager(@PathParam("managerId") Long managerId, @PathParam("factoryId") Long factoryId) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
	    FactoryDAO factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
	    
	    Factory factory = factoryDAO.findById(factoryId);
	    
	    User user = userDAO.chooseManager(managerId, factory);
	    if (user == null) {
	           LOGGER.warning("Failed to choose manager!");
	           return null;
	    }
	    
	    return RegisterUserDTO.convertToDTO(user);
	}
	
	@GET
	@Path("/getUser/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public User findUser(@PathParam("userId") Long userId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.findById(userId);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") Long id) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.findById(id);
	}
	
	@PUT
	@Path("/points/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User updatePoints(@PathParam("id") Long id,  @QueryParam("price") double price) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.updatePoints(id, price);

	}
	
	@PUT
	@Path("/updateUser/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateUserDTO updateUser(@PathParam("id") Long id, UpdateUserDTO userDto) throws ParseException {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User updatedUser = userDAO.updateUser(id, userDto);
		
		if (updatedUser == null) {
			return null;
		}
		
		return UpdateUserDTO.convertToDTO(updatedUser);
	}
	
	@GET
	@Path("/allUsers/{adminId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> findAllUsers(@PathParam("adminId") Long adminId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.findAllUsers(adminId);
	}
	

	
	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> findAllRoles(){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.findAllRoles();
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> search(
			@QueryParam("excludeId") Long excludeId,
	        @QueryParam("firstName") @DefaultValue("") String firstName,
	        @QueryParam("lastName") @DefaultValue("") String lastName,
	        @QueryParam("username") @DefaultValue("") String username) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
	    return userDAO.search(firstName, lastName, username, excludeId);
	}
	
	@GET
	@Path("/sortAsc/firstName/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortAscendingByFirstName(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortAscendingByFirstName(excludeId);
	}
	
	@GET
	@Path("/sortAsc/lastName/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortAscendingByLastName(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortAscendingByLastName(excludeId);
	}
	
	@GET
	@Path("/sortAsc/username/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortAscendingByUsername(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortAscendingByUsername(excludeId);
	}
	
	@GET
	@Path("/sortAsc/points/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortAscendingByPoints(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortAscendingByPoints(excludeId);
	}
	
	@GET
	@Path("/sortDesc/firstName/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortDescendingByFirstName(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortDescendingByFirstName(excludeId);
	}
	
	@GET
	@Path("/sortDesc/lastName/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortDescendingByLastName(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortDescendingByLastName(excludeId);
	}
	
	@GET
	@Path("/sortDesc/username/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortDescendingByUsername(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortDescendingByUsername(excludeId);
	}
	
	@GET
	@Path("/sortDesc/points/{excludeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortDescendingByPoints(@PathParam("excludeId") Long excludeId){
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		return userDAO.sortDescendingByPoints(excludeId);
	}
	
}
