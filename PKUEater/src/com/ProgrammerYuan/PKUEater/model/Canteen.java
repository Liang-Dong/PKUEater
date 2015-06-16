package com.ProgrammerYuan.PKUEater.model;

import android.database.Cursor;
import com.ProgrammerYuan.PKUEater.utils.EaterDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mac on 15/5/31.
 */
public class Canteen extends DBEntry implements Serializable {
	private int id,comment_count;
	private String name;
	private String content;
	private String startTime,endTime;
	private String image_url;
	private String location;
	private float crowdedness;
	public ArrayList<Dish> dishes;

	public Canteen(){

	}

	@Override
	public String getDeletingSql() {
		return "delete from `canteens` where id = " + id;
	}

	@Override
	public String getSavingSql() {
		return "replace into `canteens`(`id`,`name`,`image`,`content`,`comment_count`,`rate`) values (" + id + ",'" + name + "','" + image_url  + "','" + content + "'," + comment_count + "," + crowdedness + ")";
	}

	@Override
	public String getCreatingTableSql() {
		return "create table if not exists `canteens`(" +
				"`id` int primary key," +
				"`name` varchar(255)," +
				"`image` varchar(255)," +
				"`content`varchar(255)," +
				"`comment_count` int default 0," +
				"`rate` float" +
				");";
	}

	public Canteen(JSONObject jo) {
		if(jo == null) return;
		name = jo.optString("name");
		image_url = jo.optString("image");
		content = jo.optString("description");
		id = jo.optInt("id");
		comment_count = jo.optInt("crowd_rate_count", 0);
		crowdedness = Double.valueOf(jo.optDouble("crowd_rate",0)).floatValue();
		if(jo.has("dishes")){
			dishes = new ArrayList<>();
			JSONArray ja = jo.optJSONArray("dishes");
			for(int i = 0;i<ja.length();i++){
				try {
					Dish dish = new Dish(id,ja.getJSONObject(i));
					dishes.add(dish);
					EaterDB.saveEntry(dish);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Canteen(Cursor c){
		if(c == null) return;
		id = c.getInt(c.getColumnIndex("id"));
		name = c.getString(c.getColumnIndex("name"));
		image_url = c.getString(c.getColumnIndex("image"));
		content = c.getString(c.getColumnIndex("content"));
		comment_count = c.getInt(c.getColumnIndex("comment_count"));
		crowdedness = c.getFloat(c.getColumnIndex("rate"));
		dishes = EaterDB.getDishesOfCanteen(id, 0, 15);
	}

	public int getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public String getImageUrl(){
		return image_url;
	}

	public String getContent(){
		return content;
	}

	public float getRate(){
		return crowdedness;
	}

	public String getDishesTitle(){
		String ret = "";
		for(int i = 0;i < Math.min(dishes.size(),3);i++){
			ret += dishes.get(i).getName() + "\n";
		}
		return ret;
	}
}
