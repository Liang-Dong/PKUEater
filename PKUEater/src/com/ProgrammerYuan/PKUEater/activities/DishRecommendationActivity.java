package com.ProgrammerYuan.PKUEater.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ProgrammerYuan.PKUEater.R;
import com.ProgrammerYuan.PKUEater.model.Dish;
import com.ProgrammerYuan.PKUEater.utils.EaterDB;

import studio.archangel.toolkitv2.AngelActivity;
import studio.archangel.toolkitv2.dialogs.LoadingDialog;
import studio.archangel.toolkitv2.util.image.ImageProvider;
import studio.archangel.toolkitv2.widgets.AngelActionBar;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mac on 15/5/31.
 */
public class DishRecommendationActivity extends AngelActivity {

	AngelActionBar aab;
	TextView refreshBtn;
	RelativeLayout rl_dish1, rl_dish2, rl_dish3;
	ImageView iv_like1,iv_like2,iv_like3,iv_avatar1,iv_avatar2,iv_avatar3;
	TextView tv_name1,tv_name2,tv_name3;
	boolean like1 = false,like2 = false,like3 = false;
	private ArrayList<Dish> dishes, dishBase;
	int offset = 0,canteen_id;
	
	// ----------------------added by Wilford-----------------------
	final String startTime = "0600";
	final String endTime = "1900";
	
	final int ctnNum = 6; 
	final int likeWeight = 10;
	
	final int walkLim = 500;
	final double ctnLat[] = {0, 0, 0, 0, 0, 0};
	final double ctnLon[] = {0, 0, 0, 0, 0, 0};
	
	int lastCtnPriority[] = new int [ctnNum];
	int nowCtnPriority[] = {1, 1, 1, 1, 1, 1};
	
	
	public void updateCtnPriority(Location nowlct)
	{
		
		float[] results = new float[1];
		for (int i = 0; i < ctnNum; i ++)	
		{
			double lat = nowlct.getLatitude(), lon = nowlct.getLongitude();
			Location.distanceBetween(lat, lon, ctnLat[i], ctnLon[i], results);
			if (results[0] < 40)
				nowCtnPriority[i] = 2000;
			if (results[0] > walkLim)
				nowCtnPriority[i] = 1;
			else
				nowCtnPriority[i] = 100;
		}
	}
	
	LocationManager lMng;
	public Location getLocation(Context context) 
	{ 
		 Location location = lMng.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		 if (location == null) {
			 location = lMng.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		   }
		 return location;
	}
	
	//-------------------------------End-------------------------------
	
	public void setupActionBar(String title) {
		ActionBar bar = getActionBar();
		if (bar == null) {
			return;
		}
		bar.setIcon(getResources().getDrawable(studio.archangel.toolkitv2.R.color.trans));
		bar.setDisplayHomeAsUpEnabled(false);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowHomeEnabled(false);
		bar.setTitle("");
		if (aab == null) {
			aab = new AngelActionBar(this);
		}
		aab.setBackgroundResource(R.color.white);
		aab.getShadow().setVisibility(View.VISIBLE);
		aab.getLeftButton().setVisibility(View.INVISIBLE);
		bar.setCustomView(aab);
		aab.setTitleText(title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_dish_recommendation);
		setupActionBar("菜品推荐");
		Bundle data = getIntent().getExtras();
		if(data != null){
			canteen_id = data.getInt("canteen_id",-1);
		} else {
			canteen_id = -1;
		}

		// ----------------------added by Wilford-----------------------
		if (canteen_id == -1)
		{
			lMng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);   
			if (!lMng.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  
				Toast.makeText(this, "开个GPS呗亲", Toast.LENGTH_SHORT).show();  
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
				startActivityForResult(intent, 0);  
				return;  
			}

			Location nowlct = getLocation(this);
			updateCtnPriority(nowlct);
			lMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3200, 25, locationListener);  
		}
    // 位置监听  
    
	    //-------------------------------End-------------------------------
		
		refreshBtn = (TextView) findViewById(R.id.act_dish_recommendation_refresh_btn);
		refreshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				getDishes(); //offset +=3;	// ----------------------added by Wilford-----------------------
				//offset %= dishes.size();
				flip(0);
			}
		});
//		dialog = new LoadingDialog(this,R.color.main_2);
//		dishes = new ArrayList<>();
//		dishes.add(new Dish("麻辣香锅", "超辣超好吃", 0,5));
//		dishes.add(new Dish("糖醋里脊", "酸甜可口，鲜嫩多汁", 1,3));
//		dishes.add(new Dish("木须肉", "色泽鲜艳有营养", 2,4));
//		dishes.add(new Dish("木须肉", "色泽鲜艳有营养", 2,4));
//		dishes.add(new Dish("糖醋里脊", "酸甜可口，鲜嫩多汁", 1,3));
//		dishes.add(new Dish("麻辣香锅", "超辣超好吃", 0,5));
		rl_dish1 = (RelativeLayout)findViewById(R.id.act_dish_recommendation_card1);
		rl_dish2 = (RelativeLayout)findViewById(R.id.act_dish_recommendation_card2);
		rl_dish3 = (RelativeLayout)findViewById(R.id.act_dish_recommendation_card3);
		tv_name1 = (TextView)findViewById(R.id.act_dish_recommendation_card1_name);
		tv_name2 = (TextView)findViewById(R.id.act_dish_recommendation_card2_name);
		tv_name3 = (TextView)findViewById(R.id.act_dish_recommendation_card3_name);
		iv_avatar1 = (ImageView)findViewById(R.id.act_dish_recommendation_card1_image);
		iv_avatar2 = (ImageView)findViewById(R.id.act_dish_recommendation_card2_image);
		iv_avatar3 = (ImageView)findViewById(R.id.act_dish_recommendation_card3_image);
		iv_like1 = (ImageView)findViewById(R.id.act_dish_recommendation_card1_like);
		iv_like2 = (ImageView)findViewById(R.id.act_dish_recommendation_card2_like);
		iv_like3 = (ImageView)findViewById(R.id.act_dish_recommendation_card3_like);
		iv_like1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				like1 = !like1;
				iv_like1.setImageResource(like1 ? R.drawable.icon_like : R.drawable.icon_not_like);
				EaterDB.likeDish(dishes.get(offset),like1);
			}
		});
		iv_like2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				like2 = !like2;
				iv_like2.setImageResource(like2 ? R.drawable.icon_like : R.drawable.icon_not_like);
				EaterDB.likeDish(dishes.get(offset + 1), like2);
			}
		});
		iv_like3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				like3 = !like3;
				iv_like3.setImageResource(like3 ? R.drawable.icon_like : R.drawable.icon_not_like);
				EaterDB.likeDish(dishes.get(offset + 2), like3);
			}
		});
		rl_dish1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(DishRecommendationActivity.this,DishDetailActivity.class);
				intent.putExtra("dish",dishes.get(offset));
				startActivity(intent);
			}
		});
		rl_dish2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(DishRecommendationActivity.this,DishDetailActivity.class);
				intent.putExtra("dish",dishes.get(offset + 1));
				startActivity(intent);
			}
		});
		rl_dish3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(DishRecommendationActivity.this,DishDetailActivity.class);
				intent.putExtra("dish",dishes.get(offset + 2));
				startActivity(intent);
			}
		});
		
		dishes = new ArrayList<>();
		dishes.addAll(EaterDB.getDishesOfCanteen(canteen_id, 0, 6));
		// ----------------------added by Wilford-----------------------
		/*
		if(dishes.size() > 0){
			Dish dish = dishes.get(0);
			dishes.clear();
			for(int i = 0;i<6;i++)
				dishes.add(dish);
		} else {*/
			getDishes();
		//}
		offset = 0;
		for(int i = 0;i<3;i++)
			fillData(i,dishes.get(i + offset));
		
	}

	private void fillData(int index,Dish dish){
		switch (index){
			case 0:
//				iv_avatar1.setImageResource(dish.pic_resource);
				ImageProvider.display(iv_avatar1,dish.getImageUrl());
				tv_name1.setText(dish.getName());
				iv_like1.setImageResource(dish.isLiked ? R.drawable.icon_like : R.drawable.icon_not_like);
				like1 = dish.isLiked;
				break;
			case 1:
//				iv_avatar2.setImageResource(dish.pic_resource);
				ImageProvider.display(iv_avatar2,dish.getImageUrl());
				tv_name2.setText(dish.getName());
				iv_like2.setImageResource(dish.isLiked ? R.drawable.icon_like : R.drawable.icon_not_like);
				like1 = dish.isLiked;
				break;
			case 2:
//				iv_avatar3.setImageResource(dish.pic_resource);
				ImageProvider.display(iv_avatar3,dish.getImageUrl());
				tv_name3.setText(dish.getName());
				iv_like3.setImageResource(dish.isLiked ? R.drawable.icon_like : R.drawable.icon_not_like);
				like1 = dish.isLiked;
				break;
		}
	}

	private void flip(final int index){
		switch (index){
			case 0:
				ObjectAnimator anim1 = ObjectAnimator.ofFloat(rl_dish1,"rotationY",0,90);
				final ObjectAnimator anim2 = ObjectAnimator.ofFloat(rl_dish1,"rotationY",-90,0);
				anim1.setDuration(300);
				anim2.setDuration(300);
				anim1.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						fillData(index, dishes.get(offset + index));

						anim2.start();
					}
				});
				anim1.start();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						flip(1);
					}
				},150);
				break;
			case 1:
				ObjectAnimator anim3 = ObjectAnimator.ofFloat(rl_dish2,"rotationY",0,90);
				final ObjectAnimator anim4 = ObjectAnimator.ofFloat(rl_dish2,"rotationY",-90,0);
				anim3.setDuration(300);
				anim4.setDuration(300);
				anim3.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						fillData(index, dishes.get(offset + index));
						anim4.start();
					}
				});
				anim3.start();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						flip(2);
					}
				}, 150);
				break;
			case 2:
				ObjectAnimator anim5 = ObjectAnimator.ofFloat(rl_dish3,"rotationY",0,90);
				final ObjectAnimator anim6 = ObjectAnimator.ofFloat(rl_dish3,"rotationY",-90,0);
				anim5.setDuration(300);
				anim6.setDuration(300);
				anim5.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						fillData(index, dishes.get(offset + index));
						anim6.start();
					}
				});
				anim5.start();
				break;
		}
	}
	
	
	// ----------------------added by Wilford-----------------------
	private LocationListener locationListener = new LocationListener() {	  
        /** 
         * 位置信息变化时触发 
         */  
        public void onLocationChanged(Location location) {  
        	updateCtnPriority(location);
        }
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
    };
	
    class toSort implements Comparable
    {
    	int index, value;
		@Override
		public int compareTo(Object o) {
			toSort p = (toSort) o;
			if (value > p.value)
				return 1;
			else if (value < p.value)
				return -1;
			else
				return 0;
		}
	}
    
    private toSort ref [] = new toSort [100];
    public void generateWL()
	{
    	int iLike, dishPriority;
		for (int i = 0; i < dishes.size(); i++)
		{
			// Mystle: whether the style of the dish matches my favourite tags
			// -1 for match least favourite; 0 for no match; 1 for match farourite
			// might be updated when favourite tags change

			// likeWeight is a setting that should be adjusted during communicating with the server			
			
			// iLike:  100 for I have loved the dish; 0 for not
			ref[i].index = i;
			Dish dis = dishes.get(i);
			dishPriority = (int) (dis.getRating() * likeWeight + (dis.isLiked ? 100 : 0));
			if (canteen_id == -1)
				dishPriority *= (isOpen()? 10:1);
			ref[i].value = dishPriority; // insert and rank/not to insert the dish in Waiting list according to its Priority
		}
		Arrays.sort(ref);
		for (int i = 0; i < dishes.size(); i++)
			dishBase.add(dishes.get(ref[i].index));
		dishes.clear();
		for (int i = 0; i < dishes.size(); i++)
			dishBase.add(dishBase.get(i));
	}
    
    public boolean isOpen()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("HHmm");  
		String date=sdf.format(new java.util.Date());  
		if (date.compareTo(startTime) < 0 || date.compareTo(endTime) > 0)
			return false;
		else
			return true;
	}
    
    
	public void getDishes(){
		if (canteen_id != -1 || lastCtnPriority.equals(nowCtnPriority) == true) // location has not changed much
		{
			offset += 3;
			offset %= dishes.size();
		}
		else
		{
			if (canteen_id == -1)
				for (int i = 0; i < ctnNum; i++)
					lastCtnPriority[i] = nowCtnPriority[i];
			generateWL();
			offset = 0;				
		} 
	}
	
	
}