package com.project.moabuja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.moabuja.dto.NicknameDto;
import com.project.moabuja.service.FCMServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final FCMServiceImpl service;

    @PostMapping("/push")
    public String Alarm(@RequestBody NicknameDto dto) throws ExecutionException, InterruptedException, JsonProcessingException {
        service.sendMessageTo(dto.getNickname(), "ewhruo", "asdasd");
        return "됨";
    }

}
