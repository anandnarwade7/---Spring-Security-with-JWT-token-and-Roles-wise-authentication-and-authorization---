package com.cyperts.ExcellML.UserAndRole;

import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetail implements UserDetailsService {
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsernameOrEmail(username, username);
		if (user == null) {
			throw new UsernameNotFoundException("User not exists by Username");
		}
		GrantedAuthority authorities =  new SimpleGrantedAuthority(user.getRoles().getName());
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), (Collection<? extends GrantedAuthority>) authorities);
	}
//		if (user == null) {
//			throw new UsernameNotFoundException("User not exists by Username");
//		}
//
//		String userRole = user.getRoles().getName(); // Assuming 'getRole()' is a method in your User class that returns
//														// the role as a String.
//
//		GrantedAuthority authority = new SimpleGrantedAuthority(userRole);
//
//		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
//				Collections.singletonList(authority));
//	}
}