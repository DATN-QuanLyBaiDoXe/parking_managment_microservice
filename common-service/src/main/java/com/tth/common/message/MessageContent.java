package com.tth.common.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

import java.io.Serializable;

public class MessageContent implements Serializable {
    private int status;
    private String message;
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public MessageContent() {
    }

    public MessageContent(Object data) {
        this.status = 200;
        this.message = "200 OK";
        this.data = data;
    }

    public MessageContent(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public MessageContent(int status, String message, Object data, Long total) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    public MessageContent(Object data, Long total) {
        this.status = 200;
        this.message = "200 OK";
        this.data = data;
        this.total = total;
    }

    public String toJsonString() {
        return (new Gson()).toJson(this);
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}