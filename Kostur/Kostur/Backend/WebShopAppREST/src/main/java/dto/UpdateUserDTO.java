package dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import beans.Chocolate;
import beans.User;
import enums.Gender;

public class UpdateUserDTO {
	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private Gender gender;
	private String birthday;
	
	public UpdateUserDTO() {
		super();
	}

	public UpdateUserDTO(Long id, String username, String firstName, String lastName, Gender gender, String birthday) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
	}

	public User ConvertToUser() throws ParseException {
		User user = new User();
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setGender(gender);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedBirthday = dateFormat.parse(birthday);
        user.setBirthday(parsedBirthday);
        
		return user;
	}
	
	public static UpdateUserDTO convertToDTO(User user) {
		UpdateUserDTO dto = new UpdateUserDTO();
		dto.id = user.getId();
		dto.firstName = user.getFirstName();
		dto.lastName = user.getLastName();
		dto.gender = user.getGender();
		dto.birthday = user.getBirthday().toString();
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
	
	
}
