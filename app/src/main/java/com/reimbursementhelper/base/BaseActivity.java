package com.reimbursementhelper.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * 所有活动的基类，封装了活动常用方法
 * @author mingC
 * @date 2018/2/13
 */
public abstract class BaseActivity extends AppCompatActivity{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		ButterKnife.bind(this);
		initToolbar();
		initView();
		initListener();
		initData();
	}

	Toast toast;

	/**
	 * 显示Toast信息，后调用此方法的toast会覆盖前一个调用的toast
	 * @param msg
	 */
	public void showToast(String msg) {
		if (toast == null) {
			toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	/**
	 * 对标题栏的初始化应在这里进行
	 */
	public abstract void initToolbar();

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
