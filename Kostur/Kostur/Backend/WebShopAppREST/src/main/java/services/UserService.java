package services;

import java.text.ParseException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import dao.ChocolateDAO;
import dao.UserDAO;
import dto.RegisterUserDTO;


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
}
