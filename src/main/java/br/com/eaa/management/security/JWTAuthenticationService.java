package br.com.eaa.management.security;

import br.com.eaa.management.loader.ApplicationContextLoader;
import br.com.eaa.management.model.Role;
import br.com.eaa.management.model.User;
import br.com.eaa.management.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class JWTAuthenticationService {

    private static final long EXPIRATION_TIME = 172800000;

    private static final String SECRET = "*--*/4265a3-*3*/e9s+w9/3e*-";

    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    public void addAuthentication(HttpServletResponse response, String username, Object credentials) throws IOException {
        String JWT = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS512, SECRET).compact();
        String token = TOKEN_PREFIX +" "+ JWT;
        List<Role> roles = (List<Role>) credentials;
        response.addHeader(HEADER_STRING, token);
        corsLiberation(response);
        response.getWriter().write("{\"Authorization\":\""+ token +"\", \"role\":\""+roles.get(0).getRoleName()+"\"}");

    }

    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader(HEADER_STRING);
            if(token != null) {
                String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
                if(user !=null) {
                    User usuario = ApplicationContextLoader.getApplicationContext().getBean(UserRepository.class).findUserByUsername(user);
                    if(usuario !=null) {
                        return new UsernamePasswordAuthenticationToken(usuario.getUsername(),usuario.getPassword(),usuario.getAuthorities());
                    }

                }

            }
        corsLiberation(response);
        return null;
    }

    private void corsLiberation(HttpServletResponse response) {
        if(response.getHeader("Access-Control-Allow-Origin") == null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        if(response.getHeader("Access-Control-Allow-Headers") == null) {
            response.addHeader("Access-Control-Allow-Headers", "*");
        }
        if(response.getHeader("Access-Control-Request-Headers") == null) {
            response.addHeader("Access-Control-Request-Headers", "*");
        }
        if(response.getHeader("Access-Control-Allow-Methods") == null) {
            response.addHeader("Access-Control-Allow-Methods", "*");
        }
    }
}
