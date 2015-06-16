package com.ProgrammerYuan.PKUEater.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ProgrammerYuan.PKUEater.R;
import studio.archangel.toolkitv2.widgets.AngelActionBar;

/**
 * Created by mac on 15/6/1.
 */
public class CommentCanteenActivity extends Activity {


	AngelActionBar aab;
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
		aab.setRightText("保存");
		aab.getRightButton().getTextView().setTextColor(getResources().getColor(R.color.main_2));
		aab.setRightListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				comment();
			}
		});
		bar.setCustomView(aab);
		aab.setTitleText(title);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_comment_canteen);
		setupActionBar("评价食堂");

	}

	public void comment(){

	}
}