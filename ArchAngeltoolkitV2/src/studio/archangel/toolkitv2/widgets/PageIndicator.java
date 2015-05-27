package studio.archangel.toolkitv2.widgets;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import studio.archangel.toolkitv2.R;
import studio.archangel.toolkitv2.util.Util;

import java.util.ArrayList;

/**
 * Created by Michael on 2015/3/4.
 */
public class PageIndicator extends LinearLayout {
    ArrayList<PageIndicatorUnit> units;
    int last_index = -1;
    int res_unit_top = R.color.blue;
    int res_unit_middle = R.color.white;
    int res_unit_bottom = R.color.trans_white_50;
    int unit_radius;

    public PageIndicator(Context context) {
        super(context);
        init(context);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        setOrientation(HORIZONTAL);
        setPadding(Util.getPX(1), Util.getPX(1), Util.getPX(1), Util.getPX(1));
        units = new ArrayList<>();
        unit_radius = Util.getPX(10);
    }

    public void setUnitColor(int top, int middle, int bottom) {
        res_unit_top = top;
        res_unit_middle = middle;
        res_unit_bottom = bottom;
    }

    public void setCount(int count) {
        if (count <= 1) {
            setVisibility(View.GONE);
        }
        for (int i = 0; i < count; i++) {
            PageIndicatorUnit unit = new PageIndicatorUnit(getContext());
            unit.cd_top.setColor(getContext().getResources().getColor(res_unit_top));
            unit.cd_middle.setColor(getContext().getResources().getColor(res_unit_middle));
            unit.cd_bottom.setColor(getContext().getResources().getColor(res_unit_bottom));
            unit.setSelected(false, false);
            units.add(unit);
            addView(unit);
        }
        PageIndicatorUnit unit = units.get(units.size() - 1);
        LinearLayout.LayoutParams params = (LayoutParams) unit.getLayoutParams();
        params.rightMargin = 0;
        setClipChildren(false);
    }

    public void setUnitRadius(int radius) {
        unit_radius = radius;
    }

    public void setSelection(int index) {
        if (last_index >= 0 && last_index < units.size()) {
            PageIndicatorUnit last_unit = units.get(last_index);
            last_unit.setSelected(false);
        }
        if (index >= 0 && index < units.size()) {
            PageIndicatorUnit unit = units.get(index);
            unit.setSelected(true);
            last_index = index;
        }
    }
}

class PageIndicatorUnit extends View {
    boolean is_selected = true;
    GradientDrawable cd_top, cd_middle, cd_bottom;

    public PageIndicatorUnit(Context context) {
        super(context);
        init(context);
    }

    public PageIndicatorUnit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        setBackgroundResource(R.drawable.layer_indicator);
        LayerDrawable layer = (LayerDrawable) getBackground();
        cd_top = (GradientDrawable) layer.findDrawableByLayerId(R.id.layer_indicator_top);
        cd_middle = (GradientDrawable) layer.findDrawableByLayerId(R.id.layer_indicator_middle);
        cd_bottom = (GradientDrawable) layer.findDrawableByLayerId(R.id.layer_indicator_bottom);
//        cd_middle.setAlpha(255/8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.getPX(12), Util.getPX(12));
        params.weight = 1;
        params.rightMargin = Util.getPX(2);
        setLayoutParams(params);

    }

    public void setSelected(boolean selected) {
        setSelected(selected, true);
    }

    public void setSelected(boolean selected, boolean need_animation) {
        if (!(is_selected ^ selected)) {//如果状态不变
            return;
        }
        is_selected = selected;
        float target_scale = is_selected ? 1f : 0.75f;
        long duration = need_animation ? 350 : 0;
        ViewPropertyAnimator animator_scale = animate().scaleX(target_scale).scaleY(target_scale).setDuration(duration);
        ObjectAnimator animator_alpha = null;
        if (is_selected) {
            animator_alpha = ObjectAnimator.ofInt(cd_top, "alpha", 0, 255);
        } else {
            animator_alpha = ObjectAnimator.ofInt(cd_top, "alpha", 255, 0);
        }
        animator_alpha.setDuration(duration);
        animator_scale.start();
        animator_alpha.start();
    }
}
