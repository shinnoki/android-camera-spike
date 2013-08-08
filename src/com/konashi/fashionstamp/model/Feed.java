package com.konashi.fashionstamp.model;

/*
 * タイムラインに表示する情報（Feed）のデータを扱うモデル
 * JSONのパースなどなど実装予定
 */

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.JsonObjectRequest;
import com.konashi.fashionstamp.entity.Comment;
import com.konashi.fashionstamp.entity.Item;

public class Feed {

	public static Item parseItem(JSONObject obj) {
		Item item = new Item();
		try {
			item.setId(obj.getInt("id"));
			item.setTitle(obj.getString("title"));
			item.setDescription(obj.getString("description"));
			item.setImage(obj.getJSONObject("image").getString("url"));
			item.setCreatedAt(obj.getString("created_at"));
			ArrayList<Comment> comments = parseComments(obj.getJSONArray("comments"));
			item.setComments(comments);
			item.setStampCount(countStamp(comments));
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return item;
	}

	public static ArrayList<Item> parseFeedJson(JSONArray response) {
		ArrayList<Item> feed = new ArrayList<Item>();
		for (int i = 0; i < response.length(); i++) {
			try {
				JSONObject obj = response.getJSONObject(i);
				feed.add(parseItem(obj));
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return feed;
	}

	private static ArrayList<Comment> parseComments(JSONArray comment_arr) {
		ArrayList<Comment> comments = new ArrayList<Comment>();

		for (int i = 0; i < comment_arr.length(); i++) {
			Comment comment = new Comment();
			try {
				JSONObject obj = comment_arr.getJSONObject(i);
				comment.setBody(obj.getString("body"));
				comment.setX((float) obj.getDouble("x"));
				comment.setY((float) obj.getDouble("y"));
				comment.setCreatedAt(obj.getString("created_at"));
				int stampId = obj.getInt("stamp");
				comment.setStamp(stampId);
				
				comments.add(comment);
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return comments;
	}
	
	public static Comment parseComment(JSONObject jsonComment) {
			Comment comment = new Comment();
			try {
				comment.setBody(jsonComment.getString("body"));
				comment.setX((float) jsonComment.getDouble("x"));
				comment.setY((float) jsonComment.getDouble("y"));
				comment.setCreatedAt(jsonComment.getString("created_at"));
				int stampId = jsonComment.getInt("stamp");
				comment.setStamp(stampId);
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			return comment;
	}

	private static int[] countStamp(ArrayList<Comment> comments){
		int[] stampCountArray = {0, 0, 0};
		for(Comment comment : comments){
			stampCountArray[comment.getStamp() - 1]++;
		}
		return stampCountArray;
	}
}
