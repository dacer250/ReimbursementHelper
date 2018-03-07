package com.reimbursementhelper.base;

import android.util.Log;

import com.reimbursementhelper.bean.Project;
import com.reimbursementhelper.bean.Staff;
import com.reimbursementhelper.data.ProjectDataHelper;
import com.reimbursementhelper.util.Util;

import org.litepal.crud.DataSupport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reimbursementhelper.util.Util.getApplicationContext;

/**
 * 保存一些全局变量，不推荐使用，对于较大数据在Activity之间传递应使用持久化存储。
 * @author mingC
 * @date 2018/2/13
 */
public class Global {
	public Global() {
		init();
	}

	/**
	 * 当前选择项目
	 */
	public Project curProject;

	/**
	 * 申请报销人员
	 */
	public Staff curStaff;

	/**
	 * 当前报销账目，eg："设备费":100
	 */
	public Map<String, Double> reimbMap = new HashMap<>();

	/**
	 * 手写报销条目，eg："交通":100
	 */
	public Map<String, Double> itemMap = new HashMap<>();

	/**
	 * 新项目（或旧项目编辑后）
	 */
	public Project newProject;

	/**
	 * 从xml中读取默认项目信息
	 */
	void init() {
		initDemoFile();
		initStaff();
		//设置当前项目
		try {
			curProject = ProjectDataHelper.getProjectById(BaseConfig.instance.defaultProjectId);
			Log.d("Global", "当前项目" + curProject);
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
		//设置当前使用人员
		Staff staff = DataSupport.find(Staff.class, BaseConfig.instance.defaultStaffId);
		Log.d("Global", "当前使用人员" + staff);
		curStaff = staff;
	}

	//初始化测试数据
	private void initStaff() {
		List<Staff> list = DataSupport.findAll(Staff.class);
		if (list.size() != 0) {
			Log.d("Global", "Staff已经存在！");
			return;
		}
		Staff staff = new Staff();
		staff.setId(1);
		staff.setName("大大");
		staff.setDepartment("农经所信息室");
		staff.setIdCard("4400000000000000");
		staff.setBankCard("62280000000000000");
		staff.setJobTitle("主任副研究员");
		staff.save();
		Log.d("Global", "默认staff创建成功");
		BaseConfig.instance.defaultStaffId = 1;
	}

	/**
	 * 初始化项目文件projects.xml，用来保存项目
	 */
	private void initDemoFile() {
		File fileDir = getApplicationContext().getFilesDir();
		//检查projects.xml是否存在
		File[] fileList = fileDir.listFiles();
		boolean exists = false;
		for (File file : fileList) {
			if (file.getName().equals("projects.xml")) {
				exists = true;
			}
		}
		//若不存在则创建该文件
		if (! exists) {
			Log.d("Global", "projects.xml不存在，即将创建");
			File file = new File(fileDir, "projects.xml");
			try {
				//将assets下的eg.xml内容保存
				InputStream in = Util.getApplicationContext().getAssets().open("eg.xml");
				OutputStream out = new FileOutputStream(file);
				int len = 0;
				byte[] b = new byte[3000];
				len = in.read(b);
				out.write(b, 0, len);
				out.close();
				in.close();
				Log.d("Global", "projects.xml创建成功");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.d("Global", "projects.xml文件已存在");
		}
		BaseConfig.instance.defaultProjectId = 1;
	}


}
