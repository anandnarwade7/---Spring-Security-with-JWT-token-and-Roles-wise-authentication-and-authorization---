package com.cyperts.ExcellML.UserAndRole;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cyperts.ExcellML.jwt.JwtAuthorizationFilter;
import com.cyperts.ExcellML.jwt.JwtUtil;

@RestController
@RequestMapping("/api")
public class HomeContoller {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	

//	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/login")
	public ResponseEntity<LoginRes> authenticateUser(@RequestBody LoginDto loginDto) {

		  try {
	        	System.out.println("Check Point 1:::::");
//	        	User user1=userRepository.findByEmail(loginDto.getEmail());
//	        	System.out.println(user1.toString());
//	            Authentication authentication =
//	                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
//	            System.out.println("Check Point 2::::: " +authentication);
//	            String email = authentication.getName();
//	            User user = new User(email,"");
//	            String token = jwtUtil.createToken(user);
//	            LoginRes loginRes = new LoginRes(loginDto.getEmail(),token);
//
//	            return ResponseEntity.ok(loginRes);
	        	
	        	User user = userRepository.findByEmail(loginDto.getEmail());
	        	System.out.println("Check Point 2::: ");
	        	
	        	String dbUserPassword = user.getPassword();
	        	System.out.println("Check Point 3:::: "+dbUserPassword);
	        	
	        	String loginUserPassword = loginDto.getPassword();
	        	System.out.println("Check Point 4:::: "+loginUserPassword);
	        	
	        	String encodedLoginPassword = passwordEncoder.encode(loginUserPassword);
	        	System.out.println("Check Point 5:::: "+encodedLoginPassword);
	        	
	        	if (passwordEncoder.matches(loginUserPassword, dbUserPassword)) {
	        		System.out.println("Check Point 6:::: ");
	        		  String token = jwtUtil.createToken(user);
			            LoginRes loginRes = new LoginRes(loginDto.getEmail(),token);

			            return ResponseEntity.ok(loginRes);
	        	}else {
	        		 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				}
		          
	        }catch (BadCredentialsException e){
	             e.printStackTrace();
	        }
		  return null;
	}

//	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDto) {
		System.out.println("check point 1");
		if (userRepository.existsByUsername(signUpDto.getUsername()) == null) {
			System.out.println("check point 2");
			return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
		}
		System.out.println("check point 3");
		if (userRepository.existsByEmail(signUpDto.getEmail()) == null) {
			System.out.println("check point 4");
			return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		System.out.println("check point 5");
		user.setFirstName(signUpDto.getFirstName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setMobileNo(signUpDto.getMobileNo());
		user.setLastName(signUpDto.getLastName());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		System.out.println("check point 6");
		Role roles = roleRepository.findByName("ROLE_ADMIN").get();
		user.setRoles(roles);
		System.out.println("check point 7");
		userRepository.save(user);
		System.out.println("check point 8");
		return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
	}

//	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getUserData/{username}")
	public User getUserData(@PathVariable String username) {
		return userRepository.findByUsername(username);
	}
}