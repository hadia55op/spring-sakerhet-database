package com.example.spring_sakerhet_database.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedJti = ConcurrentHashMap.newKeySet();

    public void blacklistJti(String jti) {
        if (jti != null) {
            blacklistedJti.add(jti);
        }
    }

    public boolean isBlacklisted(String jti) {
        if (jti == null) return true;
        return blacklistedJti.contains(jti);
    }

}

