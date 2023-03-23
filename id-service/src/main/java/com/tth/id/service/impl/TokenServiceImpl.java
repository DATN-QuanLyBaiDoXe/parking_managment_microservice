package com.tth.id.service.impl;

import com.tth.id.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public boolean removeAccessToken() {
        LOGGER.info("Remove BearerToken");
        return true;
    }
}
