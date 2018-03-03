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
	private String project;      //项目
	private String staff;        //人员
	private double reimb;          //报账金额

	@Override
	public String toString() {
		return "Record{" + "id=" + id + ", dateTime='" + dateTime + '\'' + ", project='" + project + '\'' + ", staff='" + staff + '\'' + ", reimb=" + reimb + '}';
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

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public double getReimb() {
		return reimb;
	}

	public void setReimb(double reimb) {
		this.reimb = reimb;
	}
}
