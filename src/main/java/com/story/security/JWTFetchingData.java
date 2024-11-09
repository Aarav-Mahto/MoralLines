package com.story.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.story.entity.RegisterUser;
import com.story.repository.UserRepository;


@Service
public class JWTFetchingData implements UserDetailsService {
	
	Map<String, String> userCredential;

	@Autowired
	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;
	
    public JWTFetchingData() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("aarav".equals(username)) {
			UserDetails userDetails = User.builder()
                    .username("aarav")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMIN")
                    .build();
			setUserInfoSession("Aarav Mahto", "aarav", "ADMIN");
			return userDetails;
        } 
		
		
		Optional<RegisterUser> optional = userRepository.findById(username);
		if(optional.isPresent()) {
			RegisterUser user = optional.get();
			UserDetails userDetails = User.builder()
					.username(user.getEmail())
					.password(user.getPassword())
					.roles(user.getUser_type())
					.build();
			setUserInfoSession(user.getName(), user.getEmail(), user.getUser_type());
			return userDetails;
		}
		else {
			setUserInfoSession("Error", "Error", "Error");
			throw new UsernameNotFoundException(username+" not Found!");
		}
	}
	
	public void setUserInfoSession(String name, String email, String accessType) {
		userCredential =new HashMap<>();
		userCredential.put("name", name);
		userCredential.put("email", email);
		userCredential.put("accessType", accessType);
	}
	public Map<String, String> getUserInfoSession() {
		return userCredential;
	}
}