package com.cutv.ningbo.ui.util.rotary;

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：12:11
 * modify developer：  admin
 * modify time：12:11
 * modify remark：
 *
 * @version 2.0
 */


public interface ChangeListener<T> {
    T getT();

    String getTextName();

    int left();

    int right();

    int top();

    int bottom();

    int getLayout();
}
