package com.ProgrammerYuan.PKUEater.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.ProgrammerYuan.PKUEater.R;
import com.ProgrammerYuan.PKUEater.activities.*;
import com.ProgrammerYuan.PKUEater.adapters.DishAdapter;
import com.ProgrammerYuan.PKUEater.model.Dish;
import com.ProgrammerYuan.PKUEater.utils.EaterDB;
import studio.archangel.toolkitv2.util.Logger;
import studio.archangel.toolkitv2.widgets.AngelActionBar;

import java.util.ArrayList;

/**
 * Created by mac on 15/5/31.
 */
public class MyCollectionFragment extends B2Fragment{

	HomeActivity owner;
	ListView list;
	DishAdapter adapter;
	ArrayList<Dish> dishes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		dishes.clear();
		dishes.addAll(EaterDB.getMyFavoriteDishes());
		adapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRealName("HomeFragment");
		if(!onCreateView(inflater,container,savedInstanceState, R.layout.frag_collection)){
			list = (ListView)cache.findViewById(R.id.frag_collection_list);
			dishes = new ArrayList<>();
			dishes.addAll(EaterDB.getMyFavoriteDishes());
//			dishes.add(new Dish("麻辣香锅","超辣超好吃",0,5));
//			dishes.add(new Dish("糖醋里脊","酸甜可口，鲜嫩多汁",1,3));
//			dishes.add(new Dish("木须肉","色泽鲜艳有营养",2,4));
			adapter = new DishAdapter(owner,dishes);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					Logger.out("id:" + i);
					Intent intent = new Intent(owner, DishDetailActivity.class);
					intent.putExtra("dish",dishes.get(i));
					startActivity(intent);
				}
			});
		}
		return cache;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		owner = (HomeActivity) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
//        front = false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		AngelActionBar aab = owner.getAngelActionBar();
		if (aab != null) {
			aab.setTitleText("我的收藏");
			aab.setMultiButtonEnabed(false);
			aab.getLeftButton().setVisibility(View.GONE);
			aab.setRightText("添加");
			aab.setRightListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(owner, DIYDishActivity.class);
					startActivity(intent);
				}
			});
		}
	}
}
