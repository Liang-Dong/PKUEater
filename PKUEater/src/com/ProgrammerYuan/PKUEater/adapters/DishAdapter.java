package com.ProgrammerYuan.PKUEater.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.ProgrammerYuan.PKUEater.R;
import com.ProgrammerYuan.PKUEater.model.Canteen;
import com.ProgrammerYuan.PKUEater.model.Dish;
import org.w3c.dom.Text;
import studio.archangel.toolkitv2.adapters.CommonAdapter;
import studio.archangel.toolkitv2.adapters.CommonAdapterViewCache;
import studio.archangel.toolkitv2.interfaces.OnCacheGeneratedListener;

import java.util.List;

/**
 * Created by mac on 15/6/1.
 */
public class DishAdapter extends CommonAdapter<Dish> {

	public DishAdapter(Context context, List<Dish> list){
		this(context,list, R.layout.item_dish);
	}

	public DishAdapter(Context context, List<Dish> list, int item_layout) {
		super(context, list, item_layout);
		l = new OnCacheGeneratedListener<Dish>() {
			@Override
			public void onCacheGenerated(CommonAdapterViewCache c, Dish dish) {
				c.setViewValue(TextView.class,dish.getName(),R.id.item_dish_name);
				c.setViewValue(TextView.class,dish.getIntro(),R.id.item_dish_intro);
				c.setViewValue(ImageView.class,dish.getImageUrl(),R.id.item_dish_image);
			}
		};
	}

	public DishAdapter(Context context, List<Dish> list, int item_layout, boolean need_cache) {
		super(context, list, item_layout, need_cache);
	}
}
