package com.tth.id.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private final static String SECRET_KEY = "elcom_wq3Dr8O5wrkCSybDkQ==1_2020@)@)";
    private static final long REFRESH_JWT_EXPIRATION = 2592000000L;//30day

    public static String createToken(String content) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + REFRESH_JWT_EXPIRATION);
            return JWT.create()
                    .withIssuer("auth0")
                    .withClaim("content", content)
                    .withExpiresAt(expiryDate)
                    .sign(Algorithm.HMAC256(SECRET_KEY));
        } catch (Exception ex) {
            LOGGER.info(ex.toString());
        }
        return null;
    }

    public static String createToken(String iss, String sub, String aud, String claimName, String claimValue, String secretKey, Date exp) {
        try {
            return JWT.create()
                    .withIssuer(iss) // iss
                    .withExpiresAt(exp) // exp
                    .withSubject(sub) // sub
                    .withAudience(aud) //aud
                    .withClaim(claimName, claimValue) //room
                    .sign(Algorithm.HMAC256(secretKey));
        } catch (Exception ex) {
            LOGGER.info(ex.toString());
        }
        return null;
    }

    public static String getContentInToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("content").asString();
        } catch (Exception ex) {
            LOGGER.info(ex.toString());
        }
        return null;
    }
}
