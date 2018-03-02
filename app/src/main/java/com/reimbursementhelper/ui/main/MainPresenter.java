package com.reimbursementhelper.ui.main;

import com.reimbursementhelper.base.BaseActivity;

/**
 * @author mingC
 * @date 2018/2/13
 */
public class MainPresenter implements MainConstract.Presenter{
	private MainConstract.View mView;
	private BaseActivity mActivity;

	public MainPresenter(MainConstract.View view) {
		mView = view;
	}

	@Override
	public void bindView(MainActivity activity) {
		mActivity = activity;
	}


}
