package com.konashi.fashionstamp.model;

/*
 * タイムラインに表示する情報（Feed）のデータを扱うモデル
 * JSONのパースなどなど実装予定
 */

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konashi.fashionstamp.entity.Comment;
import com.konashi.fashionstamp.entity.Item;

public class Feed {
	
	public ArrayList<Item> parseFeedJson(JSONArray response){
		ArrayList<Item> feed = new ArrayList<Item>();
		for(int i = 0; i < response.length(); i++){
			Item item = new Item();
			try {
				JSONObject obj = response.getJSONObject(i);
				item.setTitle(obj.getString("title"));
				item.setDescription(obj.getString("description"));
				item.setImage(obj.getJSONObject("image").getString("url"));
				item.setCreatedAt(obj.getString("created_at"));
				item.setComments(parseComments(obj.getJSONArray("comments")));
				
				feed.add(item);	
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return feed;
	}
	
	private ArrayList<Comment> parseComments(JSONArray comment_arr){
		ArrayList<Comment> comments = new ArrayList<Comment>();
		for(int i = 0; i < comment_arr.length(); i++){
			Comment comment = new Comment();
			try {
				JSONObject obj = comment_arr.getJSONObject(i);
				comment.setBody(obj.getString("body"));
				comment.setX((float) obj.getDouble("x"));
				comment.setY((float) obj.getDouble("y"));
				comment.setStamp(obj.getInt("stamp"));
				comment.setCreatedAt(obj.getString("created_at"));
				
				comments.add(comment);
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return comments;
		
	}
}
