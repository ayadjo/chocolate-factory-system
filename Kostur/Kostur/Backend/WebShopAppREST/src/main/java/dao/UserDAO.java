package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import beans.Basket;
import beans.Chocolate;
import beans.CustomerType;
import beans.Factory;
import beans.Location;
import beans.Purchase;
import beans.User;
import dto.LoginDTO;
import dto.RegisterUserDTO;
import enums.Gender;
import enums.Role;
import services.UserService;

public class UserDAO {
	private HashMap<Long, User> users = new HashMap<Long, User>();
	private String contextPath;
	
	private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
	
	public UserDAO() {
		
	}
	
	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadUsers(contextPath);
	}
	
	public HashMap<Long, User> getUsers() {
        return users;
    }
	
	public Collection<User> findAll() {
		return users.values();
	}
	
	
	public User createCustomer(RegisterUserDTO userDTO) throws ParseException {
	    User user = userDTO.convertToUser();
	    if (!isUsernameUnique(user.getUsername())) {
	        LOGGER.warning("Username not unique: " + user.getUsername());
	        return null;
	    }

	    CustomerTypeDAO customerTypeDAO = new CustomerTypeDAO(contextPath); 
	    CustomerType regularType = customerTypeDAO.findByName("Regular");
	    if (regularType == null) {
	        LOGGER.warning("Regular customer type not found");
	        return null;
	    }
	    
	    Long maxId = -1L; 
	    for (Long id : users.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    user.setId(maxId);

	    user.setType(regularType);
	    user.setBlocked(false);
	    user.setFactory(null);
	    users.put(maxId, user); 
	    writeToFile();
	    
	    BasketDAO basketDAO = new BasketDAO(contextPath);
	    basketDAO.createBasketForUser(user);

	    return user;
	}

	public User login(LoginDTO dto) {
		for (User user : users.values()) {
			if (user.getUsername().equals(dto.getUsername()) && user.getPassword().equals(dto.getPassword()) && user.isBlocked() == false) {
				return user;
			}
		}
		return null;
	}
	
	private boolean isUsernameUnique(String username) {
	    for (User u : users.values()) { 
	        if (u.getUsername().equals(username)) {
	            return false;
	        }
	    }
	    return true;
	}

	
	public User createManager(RegisterUserDTO userDTO, Factory factory) throws ParseException {
	    User user = userDTO.convertToManager();
	    if (!isUsernameUnique(user.getUsername())) {
	        LOGGER.warning("Username not unique: " + user.getUsername());
	        return null;
	    }
	    
	    Long maxId = -1L; 
	    for (Long id : users.keySet()) { 
	        if (id > maxId) { 
	            maxId = id;
	        }
	    }
	    maxId++;
	    user.setId(maxId);

	    user.setBlocked(false);
	    
	    if(factory != null) {
	        user.setFactory(factory);
	    } else {
	    	throw new IllegalArgumentException("Factory " + factory.getId() + " does not exist.");
	    }
	    
	    users.put(maxId, user); 
	    writeToFile();
	    return user;
	}

	public User findById(Long id) {
		return users.containsKey(id) ? users.get(id) : null;
	}
	
	private void writeToFile() {
	    BufferedWriter out = null;
	    try {
	        String filePath = this.contextPath + "users.txt";
	        File file = new File(filePath);
	        out = new BufferedWriter(new FileWriter(file));
	        for (User user : users.values()) {
	            String userData = user.toStringForFile();
	            System.out.println("Writing user data: " + userData); 
	            out.write(userData + "\n");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (out != null) {
	            try {
	                out.close();
	            } catch (Exception e) {}
	        }
	    }
	}

	private void loadUsers(String contextPath) {
		BufferedReader in = null;
		try {
			
			CustomerTypeDAO typeDAO = new CustomerTypeDAO(contextPath);
            HashMap<Long, CustomerType> types = typeDAO.getTypes();
            
           
            FactoryDAO factoryDAO = new FactoryDAO(contextPath);
            HashMap<Long, Factory> factories = factoryDAO.getFactories();
			
			File file = new File(contextPath + "/users.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", username = "", password = "", firstName = "", lastName = "", gender = "", birthday = "", role = "", points = "", typeId = "", factoryId = "", isBlocked = "";
			StringTokenizer st;
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					username = st.nextToken().trim();
					password = st.nextToken().trim();
					firstName = st.nextToken().trim();
					lastName = st.nextToken().trim();
					gender = st.nextToken().trim();
					birthday = st.nextToken().trim();
					role = st.nextToken().trim();
					points = st.nextToken().trim();
					typeId = st.nextToken().trim();
					factoryId = st.nextToken().trim();
					isBlocked = st.nextToken().trim();
				}
				
				CustomerType type = types.get(typeId);
	            if (type == null && !typeId.equals("-1")) {
	                type = new CustomerType(Long.parseLong(typeId));
	            }
                
                Factory factory = factories.get(Long.parseLong(factoryId));
                if (factory == null && !factoryId.equals("-1")) {
                	factory = new Factory(Long.parseLong(factoryId));
                }
                
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
                Date parsedBirthday = inputDateFormat.parse(birthday);

				
				users.put(Long.parseLong(id), new User(Long.parseLong(id), username, password, firstName, lastName, Gender.valueOf(gender), parsedBirthday, Role.valueOf(role), Integer.parseInt(points), type, factory, Boolean.parseBoolean(isBlocked)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
		
	}
	
	public Collection<User> findAvailableManagers() {
        List<User> availableManagers = new ArrayList<>();

        for (User user : users.values()) {
            if (user.getRole() == Role.Manager && user.getFactory() == null) {
                availableManagers.add(user);
            }
        }

        return availableManagers;
    }
	
	
	public User chooseManager(Long id, Factory factory) {
		User foundUser = findById(id);
		if (foundUser == null) {
			return null;
		}
		
		foundUser.setFactory(factory);
		
		writeToFile();

		return foundUser;
	}
	
	public User findById(Long id) {
		return users.containsKey(id) ? users.get(id) : null;
	}


	public User updatePoints(Long id, double price) {
		User user = findById(id);
		
		int newPoints = (int) (price / 1000 * 133);
		user.setPoints(user.getPoints() + newPoints);
		
		return user;
	}
}
