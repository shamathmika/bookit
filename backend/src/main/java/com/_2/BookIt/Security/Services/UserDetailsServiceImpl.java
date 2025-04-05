package com._2.BookIt.Security.Services;

// Project packages

import com._2.BookIt.Model.User;
import com._2.BookIt.Repository.UserRepository;

// Spring packages
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserDetailsService to load user data and build UserDetails object.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	/**
	 * Loads user details using email.
	 *
	 * @param email Email used to login
	 * @return UserDetails object
	 * @throws UsernameNotFoundException If user is not found in the repository
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
		
		return UserDetailsImpl.build(user);
	}
}
