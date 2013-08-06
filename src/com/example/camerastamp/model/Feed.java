package com.example.camerastamp.model;

/*
 * タイムラインに表示する情報（Feed）のデータを扱うモデル
 * JSONのパースなどなど実装予定
 */

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
				feed.add(item);	
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return feed;
		
	}
}
