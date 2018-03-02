package com.reimbursementhelper.ui.project;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;

/**
 * 项目编辑界面，有添加和修改两种情况
 */
public class ProjectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		replaceFragmentNoAdd(new ProjectMainFragment());
	}

	@Override
	public int getContentView() {
		return R.layout.activity_project;
	}

	@Override
	public void initView() {

	}

	/**
	 * 转换Fragment，并把旧的放入回退栈
	 * @param fragment
	 */
	public void replaceFragment(Fragment fragment) {
		android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.layout_project, fragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 转换Fragment，无视旧的
	 * @param fragment
	 */
	public void replaceFragmentNoAdd(Fragment fragment) {
		android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.layout_project, fragment);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {

	}


}
