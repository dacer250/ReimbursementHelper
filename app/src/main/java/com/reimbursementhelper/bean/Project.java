package com.reimbursementhelper.bean;

import java.util.Map;

/**
 * 项目资料，包括项目预算
 * @author mingC
 * @date 2018/2/13
 */
public class Project{
	private int id;                             //序号

	private String name;                        //名称

	private String year;

	private String category;            //分类

	String department;

	Map<String, Double> totalMap;       //总预算，eg:"设备费":"1000.0"

	Map<String, Double> remainMap;      //已使用，eg:"设备费":"50.0"


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Project{" + "id=" + id + ", name='" + name + '\'' + ", year='" + year + '\'' + ", department='" + department + '\'' + ", totalMap=" + totalMap + ", remainMap=" + remainMap + '}';
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public Map<String, Double> getTotalMap() {
		return totalMap;
	}

	public void setTotalMap(Map<String, Double> totalMap) {
		this.totalMap = totalMap;
	}

	public Map<String, Double> getRemainMap() {
		return remainMap;
	}

	public void setRemainMap(Map<String, Double> remainMap) {
		this.remainMap = remainMap;
	}
}
