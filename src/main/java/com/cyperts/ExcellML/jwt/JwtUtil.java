package com.cyperts.ExcellML.jwt;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.cyperts.ExcellML.UserAndRole.User;
import com.cyperts.ExcellML.UserAndRole.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
@Service
public class JwtUtil {

	private final UserRepository userRepository;
	private final String secret_key = "Deqzfgkivkalsuetyf=nwxxn12ndiendi34mnvnbxvsgeirolfjfuywbqhbwuxnnfkfpogmmvbfmvormvkrmvjvnmtovmtmgvmgitmvtimggitjgitmfjswe432=0";
	private long accessTokenValidity = 60 * 60 * 1000;
	private final JwtParser jwtParser;
	private final String TOKEN_HEADER = "Authorization";
	private final String TOKEN_PREFIX = "Bearer ";

	public JwtUtil( UserRepository userRepository) {
		this.jwtParser = Jwts.parser().setSigningKey(secret_key);
		this.userRepository = userRepository;
	}

//	   public String createToken(User user) {
//	        String roles = user.getRole().getName(); // Replace with the actual method to retrieve roles from the User object
//	        return generateToken(user.getEmail());
//	    }
	
    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        claims.put("username", user.getUsername());
        claims.put("mobileNo", user.getMobileNo());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();

    }
        
        //	public String generateToken(String user, String roles ) {
//		Claims claims = Jwts.claims().setSubject(user);
//		claims.put("roles", roles);
//		claims.put("firstName", user.getFirstName());
//		claims.put("lastName", user.getLastName());
//		Date tokenCreateTime = new Date();
//		Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
//		return Jwts.builder().setClaims(claims).setExpiration(tokenValidity)
//				.signWith(SignatureAlgorithm.HS256, secret_key).compact();
//		
////		Map<String, Object> claims = new HashMap<>();
////        claims.put("roles", roles);
////
////        return Jwts.builder()
////                .setClaims(claims)
////                .setSubject(user)
////                .setIssuedAt(new Date(System.currentTimeMillis()))
////                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
////                .signWith(SignatureAlgorithm.HS512, secret_key)
////                .compact();
//
//	}
	   
//	   public String generateToken(User user) {
//	        Claims claims = Jwts.claims().setSubject(user.getEmail());
////	        claims.put("roles", user.getRole().getName());
//	        claims.put("firstName", user.getFirstName());
//	        claims.put("lastName", user.getLastName());
//
//	        Date tokenCreateTime = new Date();
//	        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
//
//	        return Jwts.builder()
//	                .setClaims(claims)
//	                .setExpiration(tokenValidity)
//	                .signWith(SignatureAlgorithm.HS256, secret_key)
//	                .compact();
//	    }
	

	public String extractRoles(String token) {
        Claims claims = decodeToken(token);
        Object object = claims.get("roles");
		return token;
        
    }
		
//    private String createToken(Map<String, Object> claims, Authentication authentication) {
//    	String role =authentication.getAuthorities().stream()
//  	     .map(r -> r.getAuthority()).collect(Collectors.toSet()).iterator().next();
//        return Jwts.builder().claim("role",role).setSubject(authentication.getName()).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)))
//                .signWith(SignatureAlgorithm.HS256, secret_key).compact();
//    }

	private Claims parseJwtClaims(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public Claims resolveClaims(HttpServletRequest req) {
		try {
			String token = resolveToken(req);
			if (token != null) {
				return parseJwtClaims(token);
			}
			return null;
		} catch (ExpiredJwtException ex) {
			req.setAttribute("expired", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			req.setAttribute("invalid", ex.getMessage());
			throw ex;
		}
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(TOKEN_HEADER);
		if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(TOKEN_PREFIX.length());
		}
		return null;
	}
	
	public Claims decodeToken(String token) {
		try {
		System.out.println("Check point 1");
		Claims claims = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody();
		System.out.println("Check point 2");
	    return claims;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean validateClaims(Claims claims) throws AuthenticationException {
		try {
			return claims.getExpiration().after(new Date());
		} catch (Exception e) {
			throw e;
		}
	}

	public String getEmail(Claims claims) {
		return claims.getSubject();
	}

	private List<String> getRoles(Claims claims) {
		return (List<String>) claims.get("roles");
	}
	
    public User getUserFromToken(String token) {
    	System.out.println(token);
        Claims claims = decodeToken(token);
        System.err.println(claims);
        String email = claims.getSubject();
        System.err.println(email);
       User user= userRepository.findByEmail(email);
       System.out.println("user:::: "+user);
       return user;
    }
    
//    @PreAuthorize("hasRole('ADMIN')")	
//    public List<User> userList(){
//    	return userRepository.findAll();
//    }

}
