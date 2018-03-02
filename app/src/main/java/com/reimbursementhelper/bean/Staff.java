package com.reimbursementhelper.bean;

import org.litepal.crud.DataSupport;

/**
 * 职员，报销主体
 * @author mingC
 * @date 2018/2/13
 */
public class Staff extends DataSupport{
	private int id;         //序号
	private String name;        //姓名
	private String department;  //部门
	private String jobTitle;    //职称
	private String idCard;  //身份证
	private String bankCard;    //银行卡

	@Override
	public String toString() {
		return "Staff{" + "id=" + id + ", name='" + name + '\'' + ", department='" + department + '\'' + ", jobTitle='" + jobTitle + '\'' + ", idCard='" + idCard + '\'' + ", bankCard='" + bankCard + '\'' + '}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
}
