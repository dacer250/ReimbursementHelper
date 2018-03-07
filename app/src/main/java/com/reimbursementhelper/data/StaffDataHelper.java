package com.reimbursementhelper.data;

import com.reimbursementhelper.bean.Staff;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 人员数据操作类
 * @author mingC
 * @date 2018/3/1
 */
public class StaffDataHelper {

	public static List<Staff> getStaffList() {
		return DataSupport.findAll(Staff.class, false);
	}

	public static boolean addStaff(Staff staff) {
		return staff.save();
	}

	public static int getMaxStaffId() {
		return DataSupport.max(Staff.class, "id", Integer.class);
	}

	public static int getStaffsCount() {
		return DataSupport.count(Staff.class);
	}

	public static Staff getStaffById(int id) {
		return DataSupport.find(Staff.class, id, false);
	}

	public static int getMinStaffId() {
		return DataSupport.min(Staff.class, "id", Integer.class);
	}
}

