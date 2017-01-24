package com.cutv.ningbo.ui.activity.main;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cutv.ningbo.R;
import com.cutv.ningbo.data.api.UserApi;
import com.cutv.ningbo.data.params.UserParams;
import com.cutv.ningbo.data.save.SharePreferenceUtil;
import com.cutv.ningbo.databinding.ActivityMainBinding;
import com.cutv.ningbo.inject.qualifier.context.ActivityContext;
import com.cutv.ningbo.inject.qualifier.preference.NingSharePreference;
import com.cutv.ningbo.ui.base.fragment.BaseFragment;
import com.cutv.ningbo.ui.base.respond.Respond;
import com.cutv.ningbo.ui.base.viewModel.RadioViewModel;
import com.cutv.ningbo.data.portlet.PagerModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：15:51
 * modify developer：  admin
 * modify time：15:51
 * modify remark：
 *
 * @version 2.0
 */
public class MainViewModel extends RadioViewModel<Respond.RadioRespond> {
    private UserApi api;
    private List<PagerMainModel> list = new ArrayList<>();
    private int currentTab = 0;
    private ActivityMainBinding binding;
    @Inject
    @NingSharePreference
    SharePreferenceUtil util;
    @Inject
    UserParams userScore;

    public void setBinding(ActivityMainBinding binding){
        this.binding = binding;
    }

    @Inject
    MainViewModel(@ActivityContext Context context, UserApi api) {
        super(context);
        this.api = api;
    }

    @Override
    public void attachView(Respond.RadioRespond httpInitRespond, Bundle savedInstanceState) {
        super.attachView(httpInitRespond,savedInstanceState);
//        http(api.getScore(userScore), integer -> {
//            Toast.makeText(getContext(),"obtain score success",Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    public Collection<? extends PagerModel> getPagerModels() {
        list.clear();
        for(int i = 0;i<5;i++)list.add(new PagerMainModel(i));
        return list;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==0)return;
        int position = group.indexOfChild(group.findViewById(checkedId));
        FragmentTransaction ft = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = list.get(position).getItem(position,getContext());
        BaseFragment beforeFragment = list.get(currentTab).getItem(position,getContext());
        beforeFragment.onPause();
        ft.hide(beforeFragment);
        if (fragment.isAdded()) fragment.onResume();
        else ft.add(R.id.tab_content, fragment);
        ft.show(fragment);
        ft.commitAllowingStateLoss();
        currentTab = position;
        getRespond().onCheckedChanged(position);
    }


    public List<PagerMainModel> getList() {
        return list;
    }
}
