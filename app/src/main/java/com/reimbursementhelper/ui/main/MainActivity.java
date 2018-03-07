package com.reimbursementhelper.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;
import com.reimbursementhelper.ui.ItemSelect.ItemSelectActivity;
import com.reimbursementhelper.ui.project.ProjectActivity;
import com.reimbursementhelper.ui.record.RecordActivity;
import com.reimbursementhelper.ui.staff.StaffActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面活动
 */
public class MainActivity extends BaseActivity implements MainConstract.View {


	MainConstract.Presenter mPresenter = new MainPresenter(this);
	@BindView(R.id.rv_main_staff)
	RelativeLayout rvMainStaff;
	@BindView(R.id.rv_main_project)
	RelativeLayout rvMainProject;
	@BindView(R.id.rv_main_reimb)
	RelativeLayout rvMainReimb;
	@BindView(R.id.rv_main_record)
	RelativeLayout rvMainRecord;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);
		mPresenter.bindView(this);
		//权限申请
		if (ContextCompat.checkSelfPermission(MainActivity.this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(MainActivity.this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); //参数：上下文，权限列表，请求码
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		if (requestCode == 1 && grantResults.length > 0) {
			if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				finish();
			}
		}
	}

	public void initToolbar() {
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	@Override
	public int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	public void initView() {
		initToolbar();
	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {
	}

	@OnClick({R.id.rv_main_staff, R.id.rv_main_project, R.id.rv_main_reimb, R.id.rv_main_record})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.rv_main_staff:
				startActivity(new Intent(this, StaffActivity.class));
				break;
			case R.id.rv_main_project:
				startActivity(new Intent(this, ProjectActivity.class));
				break;
			case R.id.rv_main_reimb:
				startActivity(new Intent(this, ItemSelectActivity.class));
				break;
			case R.id.rv_main_record:
				startActivity(new Intent(this, RecordActivity.class));
				break;
		}
	}
}
