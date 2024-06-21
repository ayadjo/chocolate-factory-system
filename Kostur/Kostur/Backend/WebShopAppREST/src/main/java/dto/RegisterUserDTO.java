package dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import beans.User;
import enums.Gender;
import enums.Role;

public class RegisterUserDTO {
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Gender gender;
	private String birthday;
	private String role;
	
	public RegisterUserDTO() {
		super();
	}

	public RegisterUserDTO(Long id, String username, String password, String firstName, String lastName, Gender gender,
			String birthday, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
	}
	
	public User convertToUser() throws ParseException {
		User user = new User();
		user.setId((long) 0);
		user.setUsername(username);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setGender(gender);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedBirthday = dateFormat.parse(birthday);
		user.setBirthday(parsedBirthday);
		user.setRole(Role.Customer);
		user.setPoints(0);
		return user;
	}
	
	public static RegisterUserDTO convertToDTO(User user) {
		RegisterUserDTO dto = new RegisterUserDTO();
		dto.id = user.getId();
		dto.firstName = user.getFirstName();
		dto.lastName = user.getLastName();
		dto.gender = user.getGender();
		dto.birthday = user.getBirthday().toString();
		dto.username = user.getUsername();
		dto.password = user.getPassword();
		dto.role = user.getRole().toString();
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
