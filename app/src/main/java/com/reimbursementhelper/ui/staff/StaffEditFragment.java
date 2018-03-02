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
	@BindView(R.id.et_project_idcard)
	EditText etProjectIdcard;
	@BindView(R.id.et_staff_bankcard)
	EditText etStaffBankcard;
	@BindView(R.id.btn_staff_ok)
	Button btnStaffOk;
	@BindView(R.id.btn_staff_cancel)
	Button btnStaffCancel;
	Unbinder unbinder;
	@BindView(R.id.et_staff_jobtitle)
	EditText etStaffJobtitle;

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
		switch (view.getId()) {
			case R.id.btn_staff_ok:
				if (checkEmpty()) {
					int max = StaffDataHelper.getMaxStaffId();
					Log.d("StaffEditFragment", "Staff最大Id：" + max);
					Staff staff = new Staff();
					staff.setId(max + 1);
					staff.setName(etStaffName.getText().toString());
					staff.setJobTitle(etStaffJobtitle.getText().toString());
					staff.setDepartment(etStaffDepartment.getText().toString());
					staff.setBankCard(etStaffBankcard.getText().toString());
					staff.setIdCard(etProjectIdcard.getText().toString());
					StaffDataHelper.addStaff(staff);
					Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
					//刷新
					refresh();
				} else {
					Toast.makeText(mActivity, "存在未填写信息", Toast.LENGTH_SHORT).show();
				} break;
			case R.id.btn_staff_cancel:
				break;
		}
	}

	private void refresh() {
		mActivity.refresh();
	}

	private boolean checkEmpty() {
		return !(etProjectIdcard.getText().toString().equals("") || etStaffBankcard.toString().equals(
				"") || etStaffDepartment.toString().equals("") || etStaffName.toString().equals(""));
	}
}
