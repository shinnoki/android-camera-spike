package com.konashi.fashionstamp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Item implements Serializable {
	
	private int id;
	private ArrayList<Comment> comments;
    private String image;
    private String createdAt;
    private String title;
    private String description;
    private HashMap<Integer, Integer> stampCount;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    public HashMap<Integer, Integer> getStampCount() {
		return stampCount;
	}
	public void setStampCount(HashMap<Integer, Integer> stampCount) {
		this.stampCount = stampCount;
	}
}
