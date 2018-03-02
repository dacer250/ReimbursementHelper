package com.reimbursementhelper.ui.project;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.Global;
import com.reimbursementhelper.bean.Project;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectIntroFragment extends Fragment {


	@BindView(R.id.et_project_name)
	EditText etProjectName;
	@BindView(R.id.et_project_year)
	TextInputEditText etProjectYear;
	@BindView(R.id.et_project_department)
	TextInputEditText etProjectDepartment;
	@BindView(R.id.spinner_project_category)
	Spinner spinnerProjectCategory;
	@BindView(R.id.btn_project_budget)
	Button btnProjectBudget;
	Unbinder unbinder;

	ProjectActivity mActivity;
	Global global;

	Project newProject;

	public ProjectIntroFragment() {

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_project_intro, container, false);
		unbinder = ButterKnife.bind(this, view);

		mActivity = (ProjectActivity) getActivity();
		global = mActivity.getGlobal();

		if (global.newProject == null) {
			global.newProject = new Project();
			this.newProject = global.newProject;
		} else {
			//之前已经创建项目
			this.newProject = global.newProject;
			etProjectName.setText(newProject.getName());
			etProjectYear.setText(newProject.getYear());
			etProjectDepartment.setText(newProject.getDepartment());
		}
		btnProjectBudget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//进入预算页面
				save();
				gotoBudgetPage();
			}
		});
		spinnerProjectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String[] items = getActivity().getResources().getStringArray(R.array.project_category);
				newProject.setCategory(items[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		return view;
	}

	private void gotoBudgetPage() {
		mActivity.replaceFragment(new ProjectBudgetFragment());
	}

	public String getName() {
		return etProjectName.getText().toString();
	}

	public String getYear() {
		return etProjectYear.getText().toString();
	}

	public String getDepartment() {
		return etProjectDepartment.getText().toString();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		save();
		unbinder.unbind();
	}

	private void save() {
		//保存已填写信息
		newProject.setName(getName());
		newProject.setYear(getYear());
		newProject.setDepartment(getDepartment());
	}
}
