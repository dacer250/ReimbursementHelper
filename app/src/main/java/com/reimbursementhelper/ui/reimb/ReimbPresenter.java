package com.reimbursementhelper.ui.reimb;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.reimbursementhelper.base.Global;
import com.reimbursementhelper.base.MyApplication;
import com.reimbursementhelper.bean.Project;
import com.reimbursementhelper.bean.Record;
import com.reimbursementhelper.bean.Staff;
import com.reimbursementhelper.data.ProjectDataHelper;
import com.reimbursementhelper.data.StaffDataHelper;
import com.reimbursementhelper.ui.main.MainActivity;
import com.reimbursementhelper.util.Util;

import org.dom4j.DocumentException;
import org.litepal.crud.DataSupport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

/**
 * @author mingC
 * @date 2018/2/15
 */
public class ReimbPresenter {
	private ReimbActivity mActivity;

	private ReimbActivity mView;

	List<Project> projectList;
	List<Staff> staffList;

	public ReimbPresenter(ReimbActivity view) {
		mView = view;
		try {
			projectList = ProjectDataHelper.getProjectList();
			staffList = StaffDataHelper.getStaffList();

		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
	}

	public int getCurProjectPosition() {
		for (int i = 0; i < projectList.size(); i++) {
			if (projectList.get(i).getId() == mActivity.getGlobal().curProject.getId()) {
				return i;
			}
		}
		throw new IllegalStateException("当前项目不存在！");
	}

	public int getCurStaffPosition() {
		if (mActivity.getGlobal().curStaff == null) {
			return 0;
		}
		for (int i = 0; i < staffList.size(); i++) {
			if (staffList.get(i).getId() == mActivity.getGlobal().curStaff.getId()) {
				return i;
			}
		}
		return 0;
	}

	public List<String> getProjectsNameList() {
		List<String> projectNameList = new ArrayList<>();
		for (Project project : projectList) {
			projectNameList.add(String.format("%s（%s）", project.getName(), project.getYear()));
		}
		return projectNameList;
	}

	public List<String> getStaffsNameList() {
		List<String> staffNameList = new ArrayList<>();
		for (Staff staff : staffList) {
			staffNameList.add(String.format("%s（%s）", staff.getName(),staff.getJobTitle()));
		}
		return staffNameList;
	}

	public void setCurProject(int position) {
		mActivity.getGlobal().curProject = projectList.get(position);
		Log.d("ReimbPresenter", "更新当前项目为：" + mActivity.getGlobal().curProject);
	}

	public void setCurStaff(int position) {
		mActivity.getGlobal().curStaff = staffList.get(position);
		Log.d("ReimbPresenter", "更新当前人员为：" + mActivity.getGlobal().curStaff);
	}

	public void bindView(ReimbActivity activity) {
		mActivity = activity;
	}

	public Project getCurProject() {
		return ((MyApplication) mActivity.getApplication()).getGlobal().curProject;
	}

	public Staff getCurStaff() {
		return mActivity.getGlobal().curStaff;
	}

	/**
	 * 输出数据，制作excel文件；更新保存在xml里的项目信息
	 */
	public void submit() {

		Global global = ((MyApplication) mActivity.getApplication()).getGlobal();
		//检查报账单和报销单费用是否匹配
		double itemValue = 0.0;
		double reimbValue = 0.0;
		for (Double value : global.itemMap.values()) {
			itemValue += value;
		}
		for (Double value : global.reimbMap.values()) {
			reimbValue += value;
		}
		Log.d("ReimbPresenter", "报销总费用：" + itemValue);
		Log.d("ReimbPresenter", "报账总费用：" + reimbValue);
		if (itemValue != reimbValue) {
			new AlertDialog.Builder(mView)
					.setMessage("报销总费用：" + itemValue + " 与报账总费用：" + reimbValue + " 不匹配，请修改后重试！")
					.setTitle("提交失败！")
					.show();
			return;
		}
		Project curProject = global.curProject;
		String msg = String.format("报销项目：%s\n报销人员：%s\n报销条目：%s\n报账科目：%s", curProject.getName(), global.curStaff.getName(), global.reimbMap, global.itemMap);
		new AlertDialog.Builder(mActivity)
				.setMessage(msg)
				.setCancelable(true)
				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//回到主界面
						Intent intent = new Intent(mActivity, MainActivity.class);
						mActivity.startActivity(intent);
					}
				})
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						//回到主界面
						Intent intent = new Intent(mActivity, MainActivity.class);
						mActivity.startActivity(intent);
					}
				})
				.show();
		//修改项目剩余预算
		Project project = mActivity.getGlobal().curProject;
		Map<String, Double> remainMap = project.getRemainMap();
		Map<String, Double> reimbMap = mActivity.getGlobal().reimbMap;
		for (String key : reimbMap.keySet()) {
			//剩余预算-本次报账
			remainMap.put(key, remainMap.get(key) - reimbMap.get(key));
		}
		//保存修改到xml
		try {
			ProjectDataHelper.saveProjectRemain(project);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}

		//生成excel文件
		try {
			ProjectDataHelper.createExcel(global.curProject, global.curStaff, global.itemMap, global.reimbMap);
			Toast.makeText(mActivity, "表单已输出到SD卡根目录", Toast.LENGTH_SHORT).show();
		} catch (IOException | WriteException | BiffException e) {
			e.printStackTrace();
		}

		//保存到记录
		Record record = new Record();
		record.setId(DataSupport.max(Record.class, "id", Integer.class) + 1);
		record.setProject(global.curProject.getName());
		record.setStaff(global.curStaff.getName());
		record.setReimb(getTotalReimb(global.reimbMap));
		record.setDateTime(Util.getDateTimePretty());
		boolean result = record.save();
		if (result) {
			Log.d("ReimbPresenter", "记录保存成功：" + record);
		} else {
			Log.d("ReimbPresenter", "记录保存失败：" + record);
		}
		//清空本次报账
		mActivity.getGlobal().reimbMap = new HashMap<>();
	}

	double getTotalReimb(Map<String, Double> reimbMap) {
		double total = 0.0;
		for (Double value : reimbMap.values()) {
			total += value;
		}
		return total;
	}

	/**
	 * 判断报销额是否大于剩余预算
	 * @param reimb
	 * @return
	 */
	public boolean isReimbTooLarge(String name, double reimb) {
		return reimb > getCurProject().getRemainMap().get(name);
	}
}
