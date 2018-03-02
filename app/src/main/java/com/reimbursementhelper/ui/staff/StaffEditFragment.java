package com.reimbursementhelper.ui.staff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.reimbursementhelper.R;
import com.reimbursementhelper.bean.Staff;
import com.reimbursementhelper.data.StaffDataHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaffEditFragment extends Fragment {

	StaffActivity mActivity;
	@BindView(R.id.et_staff_name)
	EditText etStaffName;
	@BindView(R.id.et_staff_department)
	EditText etStaffDepartment;
	@BindView(R.id.et_staff_idcard)
	EditText etStaffIdcard;
	@BindView(R.id.et_staff_bankcard)
	EditText etStaffBankcard;
	@BindView(R.id.btn_staff_ok)
	Button btnStaffOk;
	@BindView(R.id.btn_staff_cancel)
	Button btnStaffCancel;
	Unbinder unbinder;
	@BindView(R.id.et_staff_jobtitle)
	EditText etStaffJobtitle;

	public Staff getEditStaff() {
		return editStaff;
	}

	public void setEditStaff(Staff editStaff) {
		this.editStaff = editStaff;
	}

	/**
	 * 正在编辑的人员，如果为null，则为添加人员状态
	 */
	private Staff editStaff;

	public StaffEditFragment() {

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		mActivity = (StaffActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_staff_edit, container, false);
		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@OnClick({R.id.btn_staff_ok, R.id.btn_staff_cancel})
	public void onViewClicked(View view) {
		boolean result = false;
		switch (view.getId()) {
			case R.id.btn_staff_ok:
				//检查
				if (isItemEmpty()) {
					Toast.makeText(mActivity, "存在未填写信息！", Toast.LENGTH_SHORT).show();
					return;
				}
				//点击确定按钮，保存数据
				Staff staff = new Staff();
				staff.setName(etStaffName.getText().toString());
				staff.setJobTitle(etStaffJobtitle.getText().toString());
				staff.setDepartment(etStaffDepartment.getText().toString());
				staff.setBankCard(etStaffBankcard.getText().toString());
				staff.setIdCard(etStaffIdcard.getText().toString());
				if (editStaff == null) {
					//添加
					int max = StaffDataHelper.getMaxStaffId();
					Log.d("StaffEditFragment", "Staff最大Id：" + max);
					staff.setId(max + 1);
					result = StaffDataHelper.addStaff(staff);
				} else {
					//编辑
					staff.setId(editStaff.getId());
					result = (1 == staff.update(editStaff.getId()));
				}
				if (result) {
					Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mActivity, "保存失败", Toast.LENGTH_SHORT).show();
					Log.d("StaffEditFragment", "修改人员失败");
				}
			case R.id.btn_staff_cancel:
				//点击取消按钮
				//fragment取消显示
				mActivity.removeEditFragment();
				//刷新
				refresh();
				break;
		}
		//fragment取消显示
		mActivity.removeEditFragment();
		//刷新
		refresh();
	}

	private void refresh() {
		mActivity.refresh();
	}

	private boolean isItemEmpty() {
		return (etStaffIdcard.getText().toString().equals("") || etStaffBankcard.getText().toString().equals(
				"") || etStaffDepartment.getText().toString().equals("") || etStaffName.toString().equals("") || etStaffJobtitle.getText().toString().equals(""));
	}
}
