package com.reimbursementhelper.bean;

import org.litepal.crud.DataSupport;

/**
 * 报账记录
 * @author mingC
 * @date 2018/2/13
 */
public class Record extends DataSupport{
	private int id;             //序号
	private String dateTime;            //报账时间
	private int projectId;      //项目序号
	private int staffId;        //人员序号
	private double reimb;          //报账金额

	@Override
	public String toString() {
		return "Record{" + "id=" + id + ", dateTime='" + dateTime + '\'' + ", projectId=" + projectId + ", staffId=" + staffId + ", reimb=" + reimb + '}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public double getReimb() {
		return reimb;
	}

	public void setReimb(double reimb) {
		this.reimb = reimb;
	}
}
