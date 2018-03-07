package com.reimbursementhelper.ui.record;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

/**
 * 记录活动
 */
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
		lvRecord.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
			                               long id) {
				PopupMenu popupMenu = new PopupMenu(RecordActivity.this, view);
				popupMenu.inflate(R.menu.record_item);
				popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						if (item.getItemId() == R.id.menu_record_item_del) {
							//删除
							int recordId = recordList.get(position).getId();
							int result = DataSupport.delete(Record.class, recordId);
							if (result == 1) {
								showToast("删除成功");
							} else {
								showToast("删除失败");
							}
							updateItemList();
						}
						return true;
					}
				});
				popupMenu.show();
				return false;
			}
		});
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
		updateItemList();
	}

	List<Record> recordList;
	private void updateItemList() {
		//清空
		itemList.clear();
		//抓取
		recordList = DataSupport.findAll(Record.class);
		Log.d("RecordActivity", "所有记录：");
		Log.d("RecordActivity", "recordList:" + recordList);
		int id = 1;
		for (Record record : recordList) {
			Map<String, String> map = new HashMap<>();
			map.put("id", String.valueOf(id++));
			map.put("project",record.getProject());
			map.put("staff", record.getStaff());
			map.put("reimb", record.getReimb() + "");
			map.put("time", record.getDateTime());
			itemList.add(map);
		}
		adapter.notifyDataSetChanged();
	}
}
