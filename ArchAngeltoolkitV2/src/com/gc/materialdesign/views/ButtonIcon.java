package com.gc.materialdesign.views;

import com.gc.materialdesign.utils.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ButtonIcon extends ButtonFloat {

	public ButtonIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackground(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ripple_speed = Utils.dpToPx(2, getResources());
		rippleSize = Utils.dpToPx(5, getResources());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean returnBool = super.onTouchEvent(event);
		if(x != -1){
			x = getWidth() / 2;
			y = getHeight() / 2;
		}
		return returnBool;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (x != -1) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(makePressColor());
			canvas.drawCircle(x, y, radius, paint);
			if(radius > getHeight()/rippleSize)
				radius += ripple_speed;
			if(radius >= getWidth() / 2 - ripple_speed){
				x = -1;
				y = -1;
				radius = getHeight()/rippleSize;
				if(listener != null)
					listener.onClick(this);
			}
		}
		invalidate();
	}
	
	@Override
	public int makePressColor() {
		return backgroundColor;
	}

}
