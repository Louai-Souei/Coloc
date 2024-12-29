package CU.projet.ColocationUniversitaire.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@RequiredArgsConstructor
public class JwtService {
	
	private static final String SECRET_KEY = "NrE1i+B/772Ms86BGpOJrzaj//TMB+7ntAWowFHWjWqhcSboP5ILDYok6GfauxCg\r\n";
	private final UserRepository userRepository;

	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}
	
	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>() , userDetails);
		
	}

	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
	) {
		// Récupérer l'utilisateur à partir de son nom d'utilisateur (email)
		String username = userDetails.getUsername();
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		// Ajouter les informations dans extraClaims
		extraClaims.put("userId", user.getId());
		extraClaims.put("role", user.getRole().name()); // Ajouter le rôle de l'utilisateur
		extraClaims.put("email", user.getEmail()); // Ajouter l'email de l'utilisateur
		extraClaims.put("firstname", user.getFirstname()); // Ajouter le prénom
		extraClaims.put("lastname", user.getLastname()); // Ajouter le nom de famille
		extraClaims.put("photo", user.getPhoto()); // Ajouter l'URL de la photo (si nécessaire)

		// Générer le token avec les informations supplémentaires
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername()) // Sujet = email de l'utilisateur
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 72)) // Expiration de 72 heures
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
		
	}
	
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
		
	}

	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
	

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		 byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
	     return Keys.hmacShaKeyFor(keyBytes);
	}

	
	

}
