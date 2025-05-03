package com._2.BookIt.Security.Services;

// Project packages

import com._2.BookIt.Model.User;

// Spring packages
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// Java packages
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Implementation of Spring Security's UserDetails interface for representing user details.
 */
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	@Getter
	private String id;
	@Getter
	private String name;
	@Getter
	private String email;
	@Getter
	private String phoneNumber;
	
	@JsonIgnore
	private String password;
	
	private GrantedAuthority authority;
	
	public UserDetailsImpl (String id, String name, String email, String phoneNumber, String password, GrantedAuthority authority) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.authority = authority;
	}
	
	/**
	 * Builds a UserDetailsImpl instance from a User object.
	 *
	 * @param user The User object.
	 * @return A UserDetailsImpl instance.
	 */
	public static UserDetailsImpl build (User user) {
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
		return new UserDetailsImpl(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.getPassword(),
				authority);
	}
	
	// Override methods
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return Collections.singletonList(authority); // Must return a collection
	}
	
	@Override
	public String getPassword () {
		return password;
	}
	
	@Override
	public String getUsername () {
		return email; // We use email to authenticate
	}
	
	@Override
	public boolean equals (Object o) {
		if (this == o) // Check if the same object
			return true;
		if (o == null || getClass() != o.getClass()) // Check if the object is null or not of the same class
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o; // Cast to UserDetailsImpl
		return Objects.equals(id, user.id); // Check if IDs are equal
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
