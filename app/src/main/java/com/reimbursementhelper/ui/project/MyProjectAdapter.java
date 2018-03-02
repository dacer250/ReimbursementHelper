package com.reimbursementhelper.ui.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseConfig;
import com.reimbursementhelper.bean.Project;

import java.util.List;

/**
 * @author mingC
 * @date 2018/2/28
 */
public class MyProjectAdapter extends BaseAdapter{

	//上下文
	Context context;

	//布局装载器
	LayoutInflater layoutInflater;

	//数据源
	private List<Project> projectList;

	//事件监听器
	private OnClickListener onEditClickListener;
	private OnClickListener onDeleteClickListener;

	public void setOnEditClickListener(OnClickListener onEditClickListener) {
		this.onEditClickListener = onEditClickListener;
	}

	public void setOnDeleteClickListener(OnClickListener onDeleteClickListener) {
		this.onDeleteClickListener = onDeleteClickListener;
	}

	public MyProjectAdapter(Context context, List<Project> projectList) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.projectList = projectList;
	}

	@Override
	public int getCount() {
		return projectList.size();
	}

	@Override
	public Object getItem(int position) {
		return projectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Project project = projectList.get(position);
		View view = convertView;

		view = layoutInflater.inflate(R.layout.item_project_operate, null);
		TextView tvId = (TextView) view.findViewById(R.id.tv_project_id);
		TextView tvName = (TextView) view.findViewById(R.id.tv_project_name);
		TextView tvCategory = (TextView) view.findViewById(R.id.tv_project_category);
		TextView tvEdit = (TextView) view.findViewById(R.id.tv_project_edit);
		TextView tvDel = (TextView) view.findViewById(R.id.tv_project_del);
		tvId.setText(String.valueOf(project.getId()));
		tvName.setText(project.getName());
		tvCategory.setText(project.getCategory());
		tvEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onEditClickListener != null) {
					onEditClickListener.onClick(project);
				}
			}
		});
		tvDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onDeleteClickListener != null) {
					onDeleteClickListener.onClick(project);
				}
			}
		});


		TextView tvDefault = (TextView) view.findViewById(R.id.tv_project_default);
		if (project.getId() == BaseConfig.instance.defaultProjectId) {
			tvDefault.setText("默认");
		} else {
			tvDefault.setText("");
		}
		return view;
	}

	interface OnClickListener {
		void onClick(Project project);
	}
}
