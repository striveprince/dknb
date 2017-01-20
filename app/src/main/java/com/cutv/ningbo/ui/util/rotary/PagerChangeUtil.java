package com.cutv.ningbo.ui.util.rotary;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.annotations.NonNull;
import com.cutv.ningbo.ui.base.adapter.pager.PagerListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：10:44
 * modify developer：  admin
 * modify time：10:44
 * modify remark：
 *
 * @version 2.0
 */
public class PagerChangeUtil<CL extends ChangeListener> implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private ViewPager viewPager;
    private RadioButton lastRadioButton;
    //    private HorizontalScrollView horizontalScrollView;
    private ImageView animView;
    //    private @LayoutRes int pointLayout;
    private int count = 0;
    private Listener<CL> listener;
    private int currentLeft = 0;
    private RadioGroup group;
    private boolean refreshRadio = false;
    /**
     * the appropriate adapter
     */
    private PagerListener<CL> adapter;

    public void setAdapter(PagerListener<CL> adapter) {
        this.adapter = adapter;
    }

    private List<CL> changeListeners = new ArrayList<>();

    public void setListener(Listener<CL> listener) {
        this.listener = listener;
    }

    /**
     * @param animView view move with the radio button and view pager
     */
    public void setAnimView(ImageView animView) {
        this.animView = animView;
    }


//    public void setPointLayout(@LayoutRes int pointLayout){this.pointLayout = pointLayout;}

//    public void setHorizontalScrollView(HorizontalScrollView horizontalScrollView) {this.horizontalScrollView = horizontalScrollView;}

    public void refreshRadioToGroup(boolean refreshRadio) {
        this.refreshRadio = refreshRadio;
    }

    /**
     * init the data,all the params can't be  null;
     *
     * @param data      the data
     * @param viewPager viewPager
     * @param group     click the appropriate radioButton to go to the corresponding page;
     */
    public void setData(Collection<CL> data, @NonNull ViewPager viewPager, @NonNull RadioGroup group) {
        this.viewPager = viewPager;
        this.group = group;
        if (group != null && refreshRadio) group.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(viewPager.getContext());
        if (count == 0) count = data.size();
        if (data != null) {
            changeListeners.clear();
            for (CL CL : data) {
                changeListeners.add(CL);
                RadioButton rb = (RadioButton) inflater.inflate(CL.getLayout(), null);
                rb.setText(CL.getTextName());
                rb.setCompoundDrawablesWithIntrinsicBounds(CL.left(), CL.top(), CL.right(), CL.bottom());
                if(CL.getLayoutParams(rb)!=null)rb.setLayoutParams(CL.getLayoutParams(rb));
                lastRadioButton = rb;
                if (listener != null) listener.initPager(CL, rb);
                if (group != null && refreshRadio) group.addView(rb);
            }
            adapter.setList(changeListeners);
            if (adapter instanceof PagerAdapter) viewPager.setAdapter((PagerAdapter) adapter);
        }
        if (group != null) {
            group.setOnCheckedChangeListener(this);
            ((RadioButton) group.getChildAt(0)).setChecked(true);
        }
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Timber.i("util:%1d", position);
        group.check(group.getChildAt(position).getId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        View view = group.findViewById(checkedId);
        int indexOfChild = group.indexOfChild(view);
        if (view != null) {
            if (animView != null) {
                TranslateAnimation animation = new TranslateAnimation(currentLeft, view.getLeft(), 0f, 0f);
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(100);
                animation.setFillAfter(true);
                animView.startAnimation(animation);
            }
            viewPager.setCurrentItem(indexOfChild);
            currentLeft = view.getLeft();
            if (listener != null)
                listener.checkIndexPager(indexOfChild, group, (RadioButton) view, lastRadioButton);
//            int mIndex = group.getChildCount() - 1 > 2 ? 2 : group.getChildCount();
//            if(horizontalScrollView!=null)horizontalScrollView.smoothScrollTo((indexOfChild >1?currentLeft:0)-group.getChildAt(mIndex).getLeft(),0);
            lastRadioButton = (RadioButton) view;
        }
    }

    public interface Listener<T> {
        void checkIndexPager(int index, RadioGroup group, RadioButton radioButton, RadioButton lastRadioButton);

        void initPager(T t, RadioButton radioButton);
    }
}
