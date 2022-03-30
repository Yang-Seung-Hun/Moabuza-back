package com.project.moabuja.dto;

import lombok.Getter;

@Getter
public class Res {

    public static class NicknameValidResponse {
        private final String msg;

        public NicknameValidResponse(String nickname){ this.msg = "해당 닉네임은 사용 중 입니다."; }

        public NicknameValidResponse(){ this.msg = "해당 닉네임은 사용이 가능합니다."; }
    }

    public static class UpdateInfoResponse {
        private final String msg;

        public UpdateInfoResponse(){ this.msg = "캐릭터, 닉네임 설정이 완료되었습니다."; }
    }

    public static class AlarmDeleteResponse {
        private final String msg;

        public AlarmDeleteResponse(){
            this.msg = "알람이 삭제 되었습니다.";
        }
    }

    public static class RecordDeleteResponse {
        private final String msg;

        public RecordDeleteResponse(){
            this.msg = "내역이 삭제 되었습니다.";
        }
    }

    public static class FriendPostResponse {
        private final String msg;

        public FriendPostResponse(){
            this.msg = "친구 요청을 보냈습니다.";
        }
    }

    public static class FriendAcceptResponse {
        private final String msg;

        public FriendAcceptResponse(){
            this.msg = "친구 요청을 수락하였습니다.";
        }
    }

    public static class FriendRefuseResponse {
        private final String msg;

        public FriendRefuseResponse(){ this.msg = "친구 요청을 거절하였습니다."; }
    }

    public static class FriendDeleteResponse {
        private final String msg;

        public FriendDeleteResponse(){
            this.msg = "친구가 삭제 되었습니다.";
        }
    }

    public static class ChallengePostResponse {
        private final String msg;

        public ChallengePostResponse(){
            this.msg = "도전해부자 요청을 보냈습니다.";
        }
    }

    public static class ChallengeAcceptResponse {
        private final String msg;

        public ChallengeAcceptResponse(){
            this.msg = "도전해부자 요청을 수락하였습니다.";
        }
    }

    public static class ChallengeRefuseResponse {
        private final String msg;

        public ChallengeRefuseResponse(){ this.msg = "도전해부자 요청을 거절하였습니다."; }
    }

    public static class ChallengeCreateResponse {
        private final String msg;

        public ChallengeCreateResponse(){
            this.msg = "도전해부자 생성을 완료하였습니다.";
        }
    }

    public static class ChallengeExitResponse {
        private final String msg;

        public ChallengeExitResponse(){ this.msg = "도전해부자 나가기 완료 되었습니다."; }
    }

    public static class GroupPostResponse {
        private final String msg;

        public GroupPostResponse(){
            this.msg = "같이해부자 요청을 보냈습니다.";
        }
    }

    public static class GroupAcceptResponse {
        private final String msg;

        public GroupAcceptResponse(){
            this.msg = "같이해부자 요청을 수락하였습니다.";
        }
    }

    public static class GroupRefuseResponse {
        private final String msg;

        public GroupRefuseResponse(){ this.msg = "같이해부자 요청을 거절하였습니다."; }
    }

    public static class GroupCreateResponse {
        private final String msg;

        public GroupCreateResponse(){
            this.msg = "같이해부자 생성을 완료하였습니다.";
        }
    }

    public static class GroupExitResponse {
        private final String msg;

        public GroupExitResponse(){ this.msg = "같이해부자 나가기 완료 되었습니다."; }
    }


    public static class LogoutResponse {
        private final String msg;

        public LogoutResponse(){ this.msg = "로그아웃이 완료되었습니다."; }
    }
}
