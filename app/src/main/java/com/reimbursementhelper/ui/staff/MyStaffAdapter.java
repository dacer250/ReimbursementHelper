package com.reimbursementhelper.ui.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseConfig;
import com.reimbursementhelper.bean.Staff;

import java.util.List;

/**
 * @author mingC
 * @date 2018/3/1
 */
public class MyStaffAdapter extends BaseAdapter{

	private Context mContext;
	private List<Staff> mStaffList;
	private LayoutInflater layoutInflater;

	private OnClickListener onEditClickListener;
	private OnClickListener onDelClickListener;


	public void setOnEditClickListener(OnClickListener onEditClickListener) {
		this.onEditClickListener = onEditClickListener;
	}

	public void setOnDelClickListener(OnClickListener onDelClickListener) {
		this.onDelClickListener = onDelClickListener;
	}

	public MyStaffAdapter(Context context, List<Staff> staffList) {
		mContext = context;
		mStaffList = staffList;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mStaffList.size();
	}

	@Override
	public Object getItem(int position) {
		return mStaffList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Staff staff = mStaffList.get(position);
		View view = layoutInflater.inflate(R.layout.item_staff, null);
			TextView tvId = (TextView) view.findViewById(R.id.tv_staff_id);
			TextView tvName = (TextView) view.findViewById(R.id.tv_staff_name);
			TextView tvEdit = (TextView) view.findViewById(R.id.tv_staff_edit);
			TextView tvDel = (TextView) view.findViewById(R.id.tv_staff_del);
			tvId.setText("" + staff.getId());
			tvName.setText(staff.getName());
			tvEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onEditClickListener != null) {
						onEditClickListener.onClick(staff);
					}
				}
			});
			tvDel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onDelClickListener != null) {
						onDelClickListener.onClick(staff);
					}
				}
			});

		TextView tvDefault = (TextView) view.findViewById(R.id.tv_staff_default);
		if (BaseConfig.instance.defaultStaffId == staff.getId()) {
			tvDefault.setText("默认");
		} else {
			tvDefault.setText("");
		}
		return view;
	}

	interface OnClickListener {
		void onClick(Staff staff);
	}
}
