package com.ishan.blogapi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    //    @Value("${app.jwtSecret}")
//    private String jwtSecret;
//
//    @Value("${app.jwtExpirationMs}")
//    private long jwtExpirationMs;
    private final Algorithm algorithm = Algorithm.HMAC256("SECRET SIGNING KEY (should be in env or config)");

    public String createJWT(Integer userId) {
        // 1 day expiry time for now
        return createJWT(userId, new Date(), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
    }

    protected String createJWT(Integer userId, Date iat, Date exp) {
        String token = JWT.create()
                .withIssuer("blog-api")
                .withSubject(userId.toString())
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .sign(algorithm);
        return token;
    }

//    private Algorithm algorithm() {
//        return Algorithm.HMAC256(jwtSecret);
//    }

//    private Key key() {
//        return Keys.hmacShaKeyFor(
//                Decoders.BASE64.decode(jwtSecret)
//        );
//    }

    public Integer getUserIdFromJWT(String jwt) {
        var subject = JWT.decode(jwt).getSubject();
        return Integer.parseInt(subject);
    }

}
