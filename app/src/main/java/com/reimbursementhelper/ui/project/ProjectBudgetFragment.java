package com.reimbursementhelper.ui.project;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.reimbursementhelper.R;
import com.reimbursementhelper.bean.Project;
import com.reimbursementhelper.data.ProjectDataHelper;

import org.dom4j.DocumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectBudgetFragment extends Fragment {

	ProjectActivity mActivity;
	@BindView(R.id.tv_project_budget_name)
	TextView tvProjectBudgetName;
	@BindView(R.id.tv_project_budget_money)
	TextView tvProjectBudgetMoney;
	@BindView(R.id.lv_project_budget)
	ListView lvProjectBudget;
	Unbinder unbinder;
	@BindView(R.id.btn_budget_submit)
	Button btnBudgetSubmit;

	List<Map<String, String>> budgetList;

	public ProjectBudgetFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		mActivity = (ProjectActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_project_budget, container, false);
		unbinder = ButterKnife.bind(this, view);
		budgetList = getBudgetItems();
		final SimpleAdapter adapter = new SimpleAdapter(mActivity, budgetList,
				R.layout.item_project_budget, new String[]{"name", "money"},
				new int[]{R.id.tv_project_budget_name, R.id.tv_project_budget_money});
		lvProjectBudget.setAdapter(adapter);
		lvProjectBudget.setDividerHeight(1);
		lvProjectBudget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final Map<String, String> map = budgetList.get(position);
				View dialogView = LayoutInflater.from(mActivity).inflate(
						R.layout.dialog_edit_budget, null);
				final EditText etBudget = (EditText) dialogView.findViewById(
						R.id.et_project_budget);
				new AlertDialog.Builder(mActivity).setView(dialogView).setTitle(
						"预算-" + map.get("name")).setCancelable(false).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								map.put("money", etBudget.getText().toString());
								adapter.notifyDataSetChanged();
							}
						}).setNeutralButton("取消", null).show();
			}
		});
		return view;
	}

	private List<Map<String, String>> getBudgetItems() {
		List<Map<String, String>> list = new ArrayList<>(20);
		try {
			InputStream in = mActivity.getAssets().open("budget_item.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			while (true) {
				String line = reader.readLine();
				if (line != null) {
					Map<String, String> map = new HashMap<>();
					map.put("name", line);
					map.put("money", "0");
					list.add(map);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();

		//保存已编辑的预算
		save();
	}

	void save() {
		Project newProject = mActivity.getGlobal().newProject;
		if (newProject == null) {
			return;
		}
		Map<String, Double> totalMap = new HashMap<>();
		Map<String, Double> remainMap = new HashMap<>();
		for (Map<String, String> map : budgetList) {
			String name = map.get("name");
			Double budget = Double.parseDouble(map.get("money"));
			totalMap.put(name, budget);
			remainMap.put(name, budget);
		}
		newProject.setRemainMap(remainMap);
		newProject.setTotalMap(totalMap);
	}

	@OnClick(R.id.btn_budget_submit)
	public void onViewClicked() {
		//设置序号为已保存项目的最大序号加1
		mActivity.getGlobal().newProject.setId(ProjectDataHelper.getMaxProjectId() + 1);
		//提交
		save();
		Log.d("ProjectBudgetFragment",
				"mActivity.getGlobal().newProject:" + mActivity.getGlobal().newProject);
		//保存到xml
		try {
			ProjectDataHelper.addProject(mActivity.getGlobal().newProject);
			Toast.makeText(mActivity, "保存成功！", Toast.LENGTH_SHORT).show();
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		//初始化
		mActivity.getGlobal().newProject = null;
		//回到项目界面
		mActivity.finish();
		mActivity.startActivity(new Intent(mActivity, ProjectActivity.class));
	}

}
