package com.zjut.grade.manager.chatObject;

/**
 * User:huangtao
 * Date:2016-02-14
 * description：
 */
public class ChatObject extends Room {
    private String userName;
    private String message;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
