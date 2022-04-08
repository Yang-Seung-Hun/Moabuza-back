package com.project.moabuja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.project.moabuja.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Slf4j
@RequiredArgsConstructor
@Service
public class FCMServiceImpl implements FCMService{
    private final RedisTemplate redisTemplate;
//    private final Map<String, String> tokenMap = new HashMap<>();
    private final Map<String, String> messageMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(FCMServiceImpl.class);
    private static final String FIREBASE_CONFIG_PATH = "moabuza-firebase-adminsdk-h9ox1-5af37638bf (1).json";
//    @Value("${API_URL}")
    private final String API_URL = "https://fcm.google.api/v1/projects/moabuza/message:send";
    private final ObjectMapper objectMapper;

    @Override
    public void register(final String nickname, final String token) {
        log.info("token : " + token + " ====== 닉네임 : " + nickname);
        redisTemplate.opsForValue().set("Nickname:"+nickname, token);
    }

    @Override
    public String getToken(String nickname){
        return (String) redisTemplate.opsForValue().get("Nickname:"+nickname);
    }

    @Override
    public void sendMessageTo(String nickname, String title, String body) throws ExecutionException, InterruptedException, JsonProcessingException {

        String targetToken = getToken(nickname);
        messageMap.put("Df","Df");
        String _message = makeMessage(targetToken, title, body);
        Message message = Message.builder()
                .setToken(targetToken)
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300").putAllData(
                        messageMap
                ).build())
                .setNotification(new Notification(title, body))
                .build();

        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        logger.info("Sent message: " + response);

    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder().
                        token(targetToken).notification(
                                FcmMessage.Notification.builder()
                                        .title(title)
                                        .body(body)
                                        .image(null)
                                        .build()
                        ).build()
                ).validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }
}