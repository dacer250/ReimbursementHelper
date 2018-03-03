package com.reimbursementhelper.ui.project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;

import butterknife.BindView;

/**
 * 项目编辑界面，有添加和修改两种情况
 */
public class ProjectActivity extends BaseActivity {

	@BindView(R.id.layout_project)
	FrameLayout layoutProject;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

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

	@Override
	public void initToolbar() {
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.onBackPressed();
		}
		return true;
	}

	/**
	 * 转换Fragment，并把旧的放入回退栈
	 *
	 * @param fragment
	 */
	public void replaceFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.layout_project, fragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 转换Fragment，无视旧的
	 *
	 * @param fragment
	 */
	public void replaceFragmentNoAdd(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
