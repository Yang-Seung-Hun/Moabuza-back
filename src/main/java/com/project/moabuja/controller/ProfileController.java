package com.project.moabuja.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile(){
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "real1" : profiles.get(0);

        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);

    }

    //무중단배포 테스트를 위한 version 확인용. 새로고침하면 return 값이 자동으로 바뀌는지
    @GetMapping("/version")
    public String checkVersion() {
        return "ver2";
    }

}
