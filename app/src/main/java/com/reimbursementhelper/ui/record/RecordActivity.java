package com.reimbursementhelper.ui.record;

import android.os.Bundle;
import android.util.Log;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;
import com.reimbursementhelper.util.Util;

public class RecordActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("RecordActivity", Util.getDateTime());
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_record;
	}

	@Override
	public void initView() {

	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {

	}
}
