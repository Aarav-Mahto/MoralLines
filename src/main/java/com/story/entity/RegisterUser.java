package com.story.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "userinfo")
public class RegisterUser {

	@Id
    @Column(unique = true, nullable = false)
    private String email; // Primary key

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String user_type;

	private Date registerDate;

	private String registered_by;

    @Transient
    private String confirmPassword;


	public RegisterUser() {
		super();
	}

	public RegisterUser(String email, String name, String phone, String password, String user_type, String confirmPassword) {
		super();
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.user_type = user_type;
		this.confirmPassword = confirmPassword;
	}



	public RegisterUser(String email, String name, String phone, String user_type, String password, String confirmPassword, Date registerDate,
			String registered_by) {
		super();
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.user_type = user_type;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.registerDate = registerDate;
		this.registered_by = registered_by;
	}

	public String getRegistered_by() {
		return registered_by;
	}

	public void setRegistered_by(String registered_by) {
		this.registered_by = registered_by;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
