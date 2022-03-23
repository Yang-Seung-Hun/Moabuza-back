package com.project.moabuja.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface FCMService {

    public void register(final String nickname, final String token);

    public String getToken(String nickname);

    public void sendMessageTo(String nickname, String title, String body) throws ExecutionException, InterruptedException, JsonProcessingException;

}
