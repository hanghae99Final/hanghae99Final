package org.sparta.mytaek1.global.message;

public enum SuccessMessage {

    LOGIN_SUCCESS_MESSAGE("로그인에 성공했습니다."),
    JOIN_SUCCESS_MESSAGE("회원가입이 완료되었습니다."),
    BROADCAST_START_MESSAGE("방송을 시작했습니다."),
    BROADCAST_END_MESSAGE("방송을 시작했습니다.");


    private final String successMessage;

    SuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getSuccessMessage() {
        return "[SUCCESS] " + successMessage;
    }
}
