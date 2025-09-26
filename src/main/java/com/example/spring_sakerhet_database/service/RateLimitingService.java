package com.example.spring_sakerhet_database.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    private final int MAX_ATTEMPT = 5;
    private final long BLOCK_TIME_MILLIS = 60 * 1000; // 1 minute block time

    // Track attempts and time
    private final Map<String, Attempt> attemptsCache = new ConcurrentHashMap<>();

    public boolean isBlocked(String key) {
        Attempt attempt = attemptsCache.get(key);
        if (attempt == null) return false;

        // If blocked and block time not expired
        if (attempt.blockedUntil > Instant.now().toEpochMilli()) {
            return true;
        } else if (attempt.blockedUntil != 0) {
            // Unblock after block time expired
            attemptsCache.remove(key);
            return false;
        }
        return false;
    }

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
    }

    public void loginFailed(String key) {
        Attempt attempt = attemptsCache.getOrDefault(key, new Attempt(0,0));
        int newCount = attempt.count + 1;

        if (newCount >= MAX_ATTEMPT) {
            // Block user for 1 minute
            attemptsCache.put(key, new Attempt(newCount, Instant.now().toEpochMilli() + BLOCK_TIME_MILLIS));
        } else {
            attemptsCache.put(key, new Attempt(newCount, 0));
        }
    }

    private static class Attempt {
        int count;
        long blockedUntil;

        public Attempt(int count, long blockedUntil) {
            this.count = count;
            this.blockedUntil = blockedUntil;
        }
    }
}

