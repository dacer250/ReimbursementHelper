package com.reimbursementhelper.ui.record;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;
import com.reimbursementhelper.bean.Record;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RecordActivity extends BaseActivity {

	@BindView(R.id.lv_record)
	ListView lvRecord;
	SimpleAdapter adapter;
	List<Map<String, String>> itemList;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		itemList = new LinkedList<>();
		adapter = new SimpleAdapter(this, itemList, R.layout.item_record,
				new String[]{"id", "project", "staff", "reimb", "time"},
				new int[]{R.id.tv_record_id, R.id.tv_record_project, R.id.tv_record_staff, R.id.tv_record_reimb, R.id.tv_record_time});
		lvRecord.setAdapter(adapter);
		lvRecord.setDividerHeight(1);
	}

	@Override
	public void initListener() {

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

	@Override
	public void initData() {
		List<Record> recordList = DataSupport.findAll(Record.class);
		Log.d("RecordActivity", "所有记录：");
		Log.d("RecordActivity", "recordList:" + recordList);
		for (Record record : recordList) {
			Map<String, String> map = new HashMap<>();
			map.put("id", record.getId() + "");
			map.put("project",record.getProject());
			map.put("staff", record.getStaff());
			map.put("reimb", record.getReimb() + "");
			map.put("time", record.getDateTime());
			itemList.add(map);
		}
		adapter.notifyDataSetChanged();
	}
}
