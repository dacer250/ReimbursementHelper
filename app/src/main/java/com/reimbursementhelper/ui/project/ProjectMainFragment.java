package com.reimbursementhelper.ui.project;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseConfig;
import com.reimbursementhelper.bean.Project;
import com.reimbursementhelper.data.ProjectDataHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectMainFragment extends Fragment {


	@BindView(R.id.lv_project_operate)
	ListView lvProjectOperate;
	@BindView(R.id.btn_project_add)
	Button btnProjectAdd;

	ProjectActivity mActivity;

	public ProjectMainFragment() {

	}

	Unbinder unbinder;
	MyProjectAdapter adapter;
	List<Project> projectList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mActivity = (ProjectActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_project_main, container, false);
		unbinder = ButterKnife.bind(this, view);
		try {
			projectList = ProjectDataHelper.getProjectList();
			adapter = new MyProjectAdapter(getActivity(), projectList);
			lvProjectOperate.setAdapter(adapter);
			lvProjectOperate.setDividerHeight(0);
			Toast.makeText(mActivity, "长按项目可设为默认状态", Toast.LENGTH_SHORT).show();
			//长按可设为默认
			lvProjectOperate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
				                               long id) {
					new AlertDialog.Builder(mActivity)
							.setMessage("是否设为默认项目？")
							.setPositiveButton("是", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									int id = projectList.get(position).getId();
									//修改默认序号
									BaseConfig.instance.defaultProjectId = id;
									//修改当前项目
									try {
										mActivity.getGlobal().curProject = ProjectDataHelper.getProjectById(id);
									} catch (IOException | XmlPullParserException e) {
										e.printStackTrace();
									}
									//保存到sp中
									SharedPreferences.Editor editor = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE).edit();
									editor.putInt("defaultProjectId", id);
									editor.apply();
									adapter.notifyDataSetInvalidated();
									//更新当前报账
									mActivity.getGlobal().reimbMap = new HashMap<>();
								}
							})
							.setNeutralButton("取消", null)
							.show();
					return false;
				}
			});
			adapter.setOnDeleteClickListener(new MyProjectAdapter.OnClickListener() {
				@Override
				public void onClick(Project project) {
					Toast.makeText(getActivity(), project.getName() + "- 删除",
							Toast.LENGTH_SHORT).show();
					int count = ProjectDataHelper.getProjectsCount();
					Log.d("ProjectMainFragment", "删除前剩余项目数" + count);
					if (ProjectDataHelper.getProjectsCount() == 1) {
						Toast.makeText(mActivity, "至少需要有一个默认项目，删除失败!", Toast.LENGTH_SHORT).show();
					} else {
						ProjectDataHelper.deleteProject(project);
						if (BaseConfig.instance.defaultProjectId == project.getId()) {
							int minId = ProjectDataHelper.getMinProjectId();
							//将最小序号项目设为默认
							BaseConfig.instance.defaultProjectId = minId;
							//保存到sp
							SharedPreferences.Editor editor = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE).edit();
							editor.putInt("defaultProjectId", minId);
							editor.apply();
							try {
								mActivity.getGlobal().curProject = ProjectDataHelper.getProjectById(minId);
							} catch (IOException | XmlPullParserException e) {
								e.printStackTrace();
							}
						}
					}
					//刷新
					refresh();
				}
			});

			adapter.setOnEditClickListener(new MyProjectAdapter.OnClickListener() {
				@Override
				public void onClick(Project project) {
					Toast.makeText(getActivity(), project.getName() + "- 编辑",
							Toast.LENGTH_SHORT).show();
				}
			});

			btnProjectAdd.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					gotoIntroFragment();
				}
			});
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
		return view;
	}

	private void gotoIntroFragment() {
		((ProjectActivity)getActivity()).replaceFragment(new ProjectIntroFragment());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	void refresh() {
		projectList.clear();
		try {
			projectList.addAll(ProjectDataHelper.getProjectList());
			adapter.notifyDataSetChanged();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
