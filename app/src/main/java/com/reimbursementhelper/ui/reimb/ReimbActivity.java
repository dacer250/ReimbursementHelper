package com.reimbursementhelper.ui.reimb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;
import com.reimbursementhelper.bean.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ReimbActivity extends BaseActivity {

	ReimbPresenter mPresenter = new ReimbPresenter(this);
	@BindView(R.id.listview_reimb)
	ListView listviewReimb;

	List<Map<String, String>> itemList;
	SimpleAdapter adapter;
	@BindView(R.id.btn_reimb_submit)
	Button btnReimbSubmit;
	@BindView(R.id.spinner_reimb_project)
	Spinner spinnerReimbProject;
	@BindView(R.id.spinner_reimb_staff)
	Spinner spinnerReimbStaff;
	@BindView(R.id.toolbar)
	Toolbar toolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPresenter.bindView(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public int getContentView() {
		return R.layout.activity_reimb;
	}

	@Override
	public void initView() {
		Project project = mPresenter.getCurProject();
		itemList = new ArrayList();
		adapter = new SimpleAdapter(this, itemList, R.layout.item_budget,
				new String[]{"name", "total", "remain", "reimb"},
				new int[]{R.id.tv_budget_name, R.id.tv_budget_total, R.id.tv_budget_remain, R.id.tv_budget_reimb});
		listviewReimb.setAdapter(adapter);
		listviewReimb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final Map<String, String> map = itemList.get(position);
				View dialogView = getLayoutInflater().inflate(R.layout.dialog_budget, null);
				final EditText etReimb = (EditText) dialogView.findViewById(R.id.et_budget_reimb);
				//弹出对话框提交报账金额
				new AlertDialog.Builder(ReimbActivity.this).setPositiveButton("提交",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String reimb = etReimb.getText().toString();
								double reimbD;
								if (reimb.equals("")) {
									reimbD = 0.0;
								} else {
									reimbD = Double.parseDouble(reimb);
								}
								//判断是否合法
								if (mPresenter.isReimbTooLarge(map.get("name"), reimbD)) {
									Toast.makeText(ReimbActivity.this,
											map.get("name") + " 报账数目（" + reimbD + "）超出剩余预算！",
											Toast.LENGTH_SHORT).show();
									return;
								}
								//更新界面
								map.put("reimb", "" + reimbD);
								adapter.notifyDataSetInvalidated();
								//记录在Global中
								if (getGlobal().reimbMap == null) {
									getGlobal().reimbMap = new HashMap<>();
								}
								getGlobal().reimbMap.put(map.get("name"), reimbD);
							}
						}).setTitle("报账科目-" + map.get("name")).setView(
						dialogView).setNegativeButton("取消", null).show();
			}
		});
		listviewReimb.setDividerHeight(1);
		btnReimbSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//提交按钮
				mPresenter.submit();
			}
		});
		setProjectSpinnerData(mPresenter.getProjectsNameList(), mPresenter.getCurProjectPosition());
		setStaffSpinnerData(mPresenter.getStaffsNameList(), mPresenter.getCurStaffPosition());
		spinnerReimbProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mPresenter.setCurProject(position);
				setShowingProject(mPresenter.getCurProject());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		spinnerReimbStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mPresenter.setCurStaff(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		setShowingProject(project);
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

	private void setShowingProject(Project project) {
		//先清空itemList
		itemList.clear();
		Map<String, Double> totalMap = project.getTotalMap();
		Map<String, Double> remainMap = project.getRemainMap();
		for (String key : totalMap.keySet()) {
			Map<String, String> map = new HashMap<>();
			map.put("name", key);
			map.put("total", "" + totalMap.get(key));
			map.put("remain", "" + remainMap.get(key));
			//处理用户回退到添加报销项目的活动
			if (getGlobal().reimbMap != null && getGlobal().reimbMap.get(key) != null) {
				map.put("reimb", "" + getGlobal().reimbMap.get(key));
			} else {
				map.put("reimb", "0.0");
			}
			itemList.add(map);
		}
		adapter.notifyDataSetChanged();
	}

	public void setProjectSpinnerData(List<String> list, int curProjectPosition) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerReimbProject.setAdapter(adapter);
		spinnerReimbProject.setSelection(curProjectPosition);
	}

	public void setStaffSpinnerData(List<String> list, int curStaffPosition) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerReimbStaff.setAdapter(adapter);
		spinnerReimbStaff.setSelection(curStaffPosition);
	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {
	}
}
