package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import beans.Basket;
import beans.Chocolate;
import beans.CustomerType;
import beans.Factory;
import beans.Location;
import beans.Purchase;
import beans.User;
import dto.ChocolateDTO;
import dto.LoginDTO;
import dto.RegisterUserDTO;
import dto.UpdateUserDTO;
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
	    user.setSuspicious(false);
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
			String line, id = "", username = "", password = "", firstName = "", lastName = "", gender = "", birthday = "", role = "", points = "", typeId = "", factoryId = "", isBlocked = "", isSuspicious = "";
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
					isSuspicious = st.nextToken().trim();
				}
				
				CustomerType type = types.get(Long.parseLong(typeId));
				if (type == null) {
					type = new CustomerType(Long.parseLong(typeId));
                }
                
                Factory factory = factories.get(Long.parseLong(factoryId));
                if (factory == null) {
                	factory = new Factory(Long.parseLong(factoryId));
                }
                
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
                Date parsedBirthday = inputDateFormat.parse(birthday);

				
				users.put(Long.parseLong(id), new User(Long.parseLong(id), username, password, firstName, lastName, Gender.valueOf(gender), parsedBirthday, Role.valueOf(role), Integer.parseInt(points), type, factory, Boolean.parseBoolean(isBlocked), Boolean.parseBoolean(isSuspicious)));
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
	
	public User updateUser(Long id, UpdateUserDTO dto) throws ParseException {
		User user = users.containsKey(id) ? users.get(id) : null;
		if (user == null) {
			return null;
		} else {
			user.setUsername(dto.getUsername());
			user.setFirstName(dto.getFirstName());
			user.setLastName(dto.getLastName());
			user.setGender(dto.getGender());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date parsedBirthday = dateFormat.parse(dto.getBirthday());
	        user.setBirthday(parsedBirthday);
		}
		writeToFile();
		
		return user;
	}


	public User updatePoints(Long id, double price) {
		User user = findById(id);
		
		int newPoints = (int) (price / 1000 * 133);
		user.setPoints(user.getPoints() + newPoints);
		
		return user;
	}
	
	public Collection<User> findAllUsers(Long excludeId) {
	    Collection<User> allUsers = findAll();
	    
	    for (User user : allUsers) {
	    	updateSuspiciousField(user.getId());
	    }
	    
	    return allUsers.stream()
	                   .filter(user -> !user.getId().equals(excludeId))
	                   .collect(Collectors.toList());
	}
	
	public Collection<String> findAllRoles() {
        String[] roles = Arrays.stream(Role.values())
                                .map(Enum::name)
                                .toArray(String[]::new);
        return Arrays.asList(roles);
    }
	
	public Collection<User> search(String firstName, String lastName, String username, Long excludeId) {
	    return users.values().stream()
	            .filter(u -> (firstName.isEmpty() || u.getFirstName().toLowerCase().contains(firstName.toLowerCase())) &&
	                         (lastName.isEmpty() || u.getLastName().toLowerCase().contains(lastName.toLowerCase())) &&
	                         !u.getId().equals(excludeId) &&  
	                         (username.isEmpty() || u.getUsername().toLowerCase().contains(username.toLowerCase())))
	            .collect(Collectors.toList());
	}
	
	public Collection<User> sortAscendingByFirstName(Long excludeId) {
	    return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getFirstName))
	            .collect(Collectors.toList());
	}

	
	public Collection<User> sortDescendingByFirstName(Long excludeId) {
	    return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getFirstName).reversed()) 
	            .collect(Collectors.toList());
	}

	
	public Collection<User> sortAscendingByLastName(Long excludeId) {
        return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getLastName))
	            .collect(Collectors.toList());
    }
	
	public Collection<User> sortDescendingByLastName(Long excludeId) {
		return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getLastName).reversed()) 
	            .collect(Collectors.toList());
    }
	
	public Collection<User> sortAscendingByUsername(Long excludeId) {
		return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getUsername))
	            .collect(Collectors.toList());
    }
	
	public Collection<User> sortDescendingByUsername(Long excludeId) {
		return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getUsername).reversed()) 
	            .collect(Collectors.toList());
    }
	
	public Collection<User> sortAscendingByPoints(Long excludeId) {
		return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getPoints))
	            .collect(Collectors.toList());
    }
	
	public Collection<User> sortDescendingByPoints(Long excludeId) {
		return users.values().stream()
	            .filter(user -> !user.getId().equals(excludeId))
	            .sorted(Comparator.comparing(User::getPoints).reversed()) 
	            .collect(Collectors.toList());
    }

	public Collection<User> filter(Long excludeId, String role, String type) {
	    return users.values().stream()
	            .filter(user -> (role.isEmpty() || user.getRole().toString().equals(role)) &&
	                            (type.isEmpty() || (user.getType().getName() != null && user.getType().getName().equals(type))) &&
	                            !user.getId().equals(excludeId))
	            .collect(Collectors.toList());
	}

	public Collection<User> findEmployeesByFactoryId(Long factoryId) {
	    List<User> foundUsers = new ArrayList<>();
	    Collection<User> allUsers = users.values();

	    for (User u : allUsers) {
	        if (u.getFactory().getId().equals(factoryId) && u.getRole().equals(Role.Employee)) {
	        	foundUsers.add(u);
	        }
	    }

	    return foundUsers;
	}
	
	public User createEmployee(RegisterUserDTO userDTO, Factory factory) throws ParseException {
	    User user = userDTO.convertToEmployee();
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
	
	public ArrayList<User> getAllSuspiciousUsers() {
		ArrayList<User> suspiciousUsers = new ArrayList<User>();
		PurchaseDAO purchaseDAO = new PurchaseDAO(contextPath);
		for(User user : users.values()) {
			if(purchaseDAO.getCancelledPurchasesCountInLastMonth(user.getId()) >= 5) {
				suspiciousUsers.add(user);
			}
		}
		return suspiciousUsers;
	}
	
	public User decrementPoints(Long id, double price) {
		User user = findById(id);
		
		int points = (int) (price / 1000 * 133 * 4);
		user.setPoints(user.getPoints() - points);
		
		return user;
	}
	
	public void updateSuspiciousField(Long userId) { 
	    ArrayList<User> suspiciousUsers = getAllSuspiciousUsers(); 
	    
	    User user = findById(userId);
	    if (user != null) {
	    	if (suspiciousUsers.contains(user)) {
	    		user.setSuspicious(true);
	    	}
	    } else {
	        throw new NotFoundException("User not found.");
	    }
	}

	public User blockUser(Long id) {
		User user = findById(id);
		if (user != null) {
			user.setBlocked(true);
		} else {
			throw new NotFoundException("User not found.");
		}
		return user;
	}

}
