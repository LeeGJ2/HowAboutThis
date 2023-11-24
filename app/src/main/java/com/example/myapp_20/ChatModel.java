package com.example.myapp_20;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;

public class ChatModel
{
    public String chatTitle;

    public String roomId;

    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, MyComment> comments = new HashMap<>();


    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }

    public Map<String, MyComment> getComments() {
        return comments;
    }

    public void setComments(Map<String, MyComment> comments) {
        this.comments = comments;
    }

    public static class MyComment
    {
        public String uid;
        public String message;
        public Object timestamp;
        public Map<String, Object> readUsers = new HashMap<>();
    }
}
