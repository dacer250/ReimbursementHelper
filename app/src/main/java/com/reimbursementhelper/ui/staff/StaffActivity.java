package com.reimbursementhelper.ui.staff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;
import com.reimbursementhelper.base.BaseConfig;
import com.reimbursementhelper.bean.Staff;
import com.reimbursementhelper.data.StaffDataHelper;

import java.util.List;

import butterknife.BindView;

public class StaffActivity extends BaseActivity {

	@BindView(R.id.lv_staff)
	ListView lvStaff;
	@BindView(R.id.btn_staff_add)
	Button btnStaffAdd;
	@BindView(R.id.layout_staff)
	FrameLayout layoutStaff;

	StaffEditFragment staffEditFragment;

	List<Staff> staffList;
	MyStaffAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		staffEditFragment = new StaffEditFragment();
		staffList = StaffDataHelper.getStaffList();
		for (Staff staff : staffList) {
			Log.d("StaffActivity", "staff:" + staff);
		}
		adapter = new MyStaffAdapter(this, staffList);
		lvStaff.setAdapter(adapter);
		lvStaff.setDividerHeight(0);
		adapter.setOnEditClickListener(new MyStaffAdapter.OnClickListener() {
			@Override
			public void onClick(Staff staff) {
				Toast.makeText(StaffActivity.this, "编辑", Toast.LENGTH_SHORT).show();
			}
		});
		adapter.setOnDelClickListener(new MyStaffAdapter.OnClickListener() {
			@Override
			public void onClick(Staff staff) {
				//判断是否最后一个人员，如果是则不能删除！
				if (StaffDataHelper.getStaffsCount() == 1) {
					Toast.makeText(StaffActivity.this, "至少需要有一个人员处于默认状态，删除失败！",
							Toast.LENGTH_SHORT).show();
				} else {
					staff.delete();
					Toast.makeText(StaffActivity.this, staff.getName() + " 删除成功！", Toast.LENGTH_SHORT).show();
					//如果删除的人员处于默认状态则传递给序号最小的未删除人员
					if (BaseConfig.instance.defaultStaffId == staff.getId()) {
						int minId = StaffDataHelper.getMinStaffId();
						BaseConfig.instance.defaultStaffId = minId;
						//修改当前人员
						getGlobal().curStaff = StaffDataHelper.getStaffById(minId);
					}
				}
				refresh();
			}
		});
		lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d("StaffActivity", "点击staffList.get(position):" + staffList.get(position));
			}
		});
		Toast.makeText(this, "长按人员可设为默认状态", Toast.LENGTH_SHORT).show();
		lvStaff.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
			                               long id) {
				new AlertDialog.Builder(StaffActivity.this)
						.setMessage("是否设为默认人员？")
						.setPositiveButton("是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int id = staffList.get(position).getId();
								//修改默认序号
								BaseConfig.instance.defaultStaffId = id;
								//修改当前人员
								getGlobal().curStaff = StaffDataHelper.getStaffById(id);
								//保存到sp中
								SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
								editor.putInt("defaultStaffId", id);
								editor.apply();
								adapter.notifyDataSetInvalidated();
							}
						})
						.setNeutralButton("取消", null)
						.show();
				return false;
			}
		});
		btnStaffAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击添加时显示fragment
				android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				staffEditFragment.setEditStaff(null);
				transaction.add(R.id.layout_staff, staffEditFragment);
				transaction.commitAllowingStateLoss();
				//隐藏按钮
				btnStaffAdd.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 隐藏编辑Fragment
	 */
	public void removeEditFragment() {
		android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.remove(staffEditFragment);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_staff;
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

	public void refresh() {
		//重新抓取人员数据
		staffList.clear();
		staffList.addAll(StaffDataHelper.getStaffList());
		adapter.notifyDataSetChanged();
		//重置按钮状态
		btnStaffAdd.setVisibility(View.VISIBLE);
	}
}
