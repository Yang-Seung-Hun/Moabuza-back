package com.project.moabuja.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.service.MemberServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Rollback(value = true)
@Transactional
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;
    @MockBean
    private MemberServiceImpl memberService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @BeforeTransaction
    public void memberSetup() {
        memberRepository.save(Member.builder()
                .email("test@a.b")
                .password("password")
                .kakaoId(21231L)
                .nickname("통통이")
                .hero(Hero.tongki)
                .build());
    }

    @Test
    @WithUserDetails(value = "password")
    @DisplayName("[GET] Home 화면 테스트")
    public void getHome() throws Exception {
        // given
        String object = objectMapper.writeValueAsString(HomeResponseDto.builder()
                .groupCurrentAmount(3000)
                .groupNeedAmount(7000)
                .groupPercent(30)
                .groupName("같이모으기")
                .groupGoalAmount(10000)
                .challengeCurrentAmount(5000)
                .challengeNeedAmount(5000)
                .challengePercent(50)
                .challengeName("도전모으기")
                .challengeGoalAmount(10000)
                .hero(Hero.tongki)
                .nickname("통통이")
                .totalAmount(10000)
                .wallet(2000)
                .alarmCount(3)
                .isFirstLogin(true)
                .build());

        // when - then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/home")
                    .content(object)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.groupCurrentAmount").value(3000))
                .andExpect(jsonPath("$.data.groupName").value("같이모으기"))
                .andExpect(jsonPath("$.data.challengeCurrentAmount").value(5000))
                .andExpect(jsonPath("$.data.challengeName").value("도전모으기"))
                .andExpect(jsonPath("$.data.nickname").value("통통이"))
                .andExpect(jsonPath("$.data.totalAmount").value(10000))
                .andDo(print());

    }
}
