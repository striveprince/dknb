package com.app.ui.base.viewModel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.app.data.portlet.PagerModel;

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：18:56
 * modify developer：  admin
 * modify time：18:56
 * modify remark：
 *
 * @version 2.0
 */


public abstract class RadioPagerViewModel<PM extends PagerModel> extends RadioViewModel implements ViewPager.OnPageChangeListener{

    /**
     * sync the viewpager currentItem
     */
    public final ObservableInt currentItem = new ObservableInt(0);

    public RadioPagerViewModel(Context context) {
        super(context);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
