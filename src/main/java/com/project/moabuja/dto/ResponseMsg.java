package com.project.moabuja.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMsg {

    // Member
    NicknameOverlap("해당 닉네임은 사용 중 입니다."),
    NicknameOK("해당 닉네임은 사용이 가능합니다."),

    UpdateInfo("캐릭터, 닉네임 설정이 완료되었습니다."),

    // Alarm
    AlarmDelete("알람이 삭제 되었습니다."),

    // Record
    RecordDelete("내역이 삭제 되었습니다."),

    // Friend
    FriendPostValid("친구요청을 보낸 상태입니다."),
    FriendNotExist("존재하지 않는 아이디입니다."),
    FriendShipExist("이미 친구로 저장되어 있습니다"),
    FriendSerchOK("친구 요청이 가능합니다."),
    FriendPost("친구 요청을 보냈습니다."),
    FriendAccept("친구 요청을 수락하였습니다."),
    FriendRefuse("친구 요청을 거절하였습니다."),
    FriendDelete("친구가 삭제 되었습니다."),

    // Challenge
    ChallengePost("도전해부자 요청을 보냈습니다."),
    ChallengeAccept("도전해부자 요청을 수락하였습니다."),
    ChallengeRefuse("도전해부자 요청을 거절하였습니다."),
    ChallengeCreate("도전해부자 생성을 완료하였습니다."),
    ChallengeExit("도전해부자 나가기 완료 되었습니다."),

    // Group
    GroupPost("같이해부자 요청을 보냈습니다."),
    GroupAccept("같이해부자 요청을 수락하였습니다."),
    GroupRefuse("같이해부자 요청을 거절하였습니다."),
    GroupCreate("같이해부자 생성을 완료하였습니다."),
    GroupExit("같이해부자 나가기 완료 되었습니다."),

    // Logout
    Logout("로그아웃이 완료되었습니다.");

    private final String msg;
}
