package beans;

import java.util.Date;

import enums.Gender;
import enums.Role;

public class User {
	private String username;
	private String password;
	private String confirmationPassword;
	private String firstName;
	private String lastName;
	private Gender gender;
	private Date birthday;
	private Role role;
	//private <Purchase> purchases;
	//private ShoppingCart cart;
	private int points;
	private CustomerType type;
	private Factory factory;
}
