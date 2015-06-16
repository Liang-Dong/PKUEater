package com.ProgrammerYuan.PKUEater.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ProgrammerYuan.PKUEater.R;
import com.ProgrammerYuan.PKUEater.model.Dish;
import com.ProgrammerYuan.PKUEater.utils.EaterDB;
import studio.archangel.toolkitv2.AngelActivity;
import studio.archangel.toolkitv2.dialogs.LoadingDialog;
import studio.archangel.toolkitv2.util.image.ImageProvider;
import studio.archangel.toolkitv2.widgets.AngelActionBar;

import java.util.ArrayList;

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
	private ArrayList<Dish> dishes;
	int offset = 0,canteen_id;
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

		refreshBtn = (TextView) findViewById(R.id.act_dish_recommendation_refresh_btn);
		refreshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				offset +=3;
				offset %= dishes.size();
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
		if(dishes.size() > 0){
			Dish dish = dishes.get(0);
			dishes.clear();
			for(int i = 0;i<6;i++)
				dishes.add(dish);
		} else {
			getDishes();
		}
		for(int i = 0;i<3;i++)
			fillData(i,dishes.get(i));
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

	public void getDishes(){

	}
}