//package com.example.spring_sakerhet_database.utility;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//public class JwtKeyGenerator {
//    public static void main(String[] args) {
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
//        System.out.println("Generated key for HS512:\n" + base64);
//    }
//}
