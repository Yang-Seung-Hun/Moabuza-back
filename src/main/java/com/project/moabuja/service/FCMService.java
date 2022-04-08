package com.project.moabuja.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface FCMService {

    void register(final String nickname, final String token);

    String getToken(String nickname);

    void sendMessageTo(String nickname, String title, String body) throws ExecutionException, InterruptedException, JsonProcessingException;

}
