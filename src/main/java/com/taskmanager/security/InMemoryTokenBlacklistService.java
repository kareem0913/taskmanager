package com.taskmanager.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

@Service
public class InMemoryTokenBlacklistService {

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String token, Date expiryDate) {
        if (token == null || expiryDate == null) return;
        blacklist.put(token, expiryDate.getTime());
    }

    public boolean isBlacklisted(String token) {
        Long expire = blacklist.get(token);
        if (expire == null) return false;
        if (expire < System.currentTimeMillis()) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
