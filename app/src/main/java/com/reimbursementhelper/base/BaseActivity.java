package com.reimbursementhelper.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author mingC
 * @date 2018/2/13
 */
public abstract class BaseActivity extends AppCompatActivity{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		ButterKnife.bind(this);
		initView();
		initListener();
		initData();
	}


	public Global getGlobal() {
		return ((MyApplication) getApplication()).getGlobal();
	}

	/**
	 * 返回一个用于显示界面的布局id
	 * @return 视图id
	 */
	public abstract int getContentView();

	/** 初始化View的代码 */
	public abstract void initView();

	/** 初始化监听器的代码 */
	public abstract void initListener();

	/** 初始数据的代码 */
	public abstract void initData();
}