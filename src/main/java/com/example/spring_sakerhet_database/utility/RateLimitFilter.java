package com.example.spring_sakerhet_database.utility;



import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Refill refill = Refill.greedy(4, Duration.ofMinutes(1)); // 2 requests per minute
        Bandwidth limit = Bandwidth.classic(4, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        boolean shouldRateLimit = false;

        // Normalize URI for dynamic endpoints
        if ("POST".equalsIgnoreCase(method) && (
                uri.equals("/api/auth/refresh") ||
                        uri.equals("/api/auth/logout") ||
                        uri.equals("/api/auth/register") ||
                        uri.equals("/authors")
        )) {
            shouldRateLimit = true;

        } else if ("GET".equalsIgnoreCase(method) && (
                uri.startsWith("/authors/name/") ||
                        uri.equals("/books") ||
                        uri.equals("/books/search") ||
                        uri.matches("/users/\\d+/loans") ||
                        uri.startsWith("/users/email/")
        )) {
            shouldRateLimit = true;
        }

        if (shouldRateLimit) {
            String ip = request.getRemoteAddr();
            String key = ip + ":" + uri;  // key = per endpoint per IP

            Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());

            if (!bucket.tryConsume(1)) {
                response.setStatus(429);
                response.setContentType("text/plain");
                response.getWriter().write("Rate limit exceeded: Max 4 requests per minute for this endpoint.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}





