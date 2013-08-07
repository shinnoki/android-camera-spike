package com.konashi.fashionstamp.entity;

import java.io.Serializable;

public class Comment implements Serializable {
	private String body;
    private int stamp;
    private String createdAt;
    private float x;
    private float y;

    public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public int getStamp() {
		return stamp;
	}
	public void setStamp(int stamp) {
		this.stamp = stamp;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
        
}
