package com.reimbursementhelper.data;


import android.util.Log;

import com.reimbursementhelper.base.BaseConfig;
import com.reimbursementhelper.bean.Project;
import com.reimbursementhelper.bean.Staff;
import com.reimbursementhelper.util.Util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


/**
 * 项目数据操作类
 * @author mingC
 * @date 2018/2/26
 */
public class ProjectDataHelper {

	/**
	 * 获取项目数量
	 * @return
	 */
	public static int getProjectsCount() {
		try {
			Document document = new SAXReader().read(getProjectsFile());
			return document.getRootElement().elements("project").size();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 删除项目，依据项目序号
	 * @param project 要删除的项目
	 */
	public static void deleteProject(Project project) {
		int id = project.getId();
		Log.d("ProjectDataHelper", "删除id：" + id);
		try {
			Document document = new SAXReader().read(getProjectsFile());
			String xpath = String.format("/projectList/project[@id='%d']", id);
			Node projectNode = document.selectSingleNode(xpath);
			if (projectNode == null) {
				Log.d("ProjectDataHelper", "null!!!项目删除失败");
			}
			document.getRootElement().remove(projectNode);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(getProjectsFile()), format);
			xmlWriter.write(document);
			xmlWriter.close();
			Log.d("ProjectDataHelper", "删除成功");
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	public static int getMinProjectId() {
		try {
			Document document = new SAXReader().read(getProjectsFile());
			Element rootElem = document.getRootElement();
			List<Element> elements = rootElem.elements("project");
			int min = 999;
			for (Element element : elements) {
				String idStr = element.attribute("id").getStringValue();
				int id = Integer.parseInt(idStr);
				if (id < min) {
					min = id;
				}
			}
			return min;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return 999;
	}

	/**
	 * 获取以保存的项目的最大序号
	 * @return
	 */
	public static int getMaxProjectId(){
		try {
			Document document = new SAXReader().read(getProjectsFile());
			Element rootElem = document.getRootElement();
			List<Element> elements = rootElem.elements("project");
			int max = 1;
			for (Element element : elements) {
				String idStr = element.attribute("id").getStringValue();
				int id = Integer.parseInt(idStr);
				if (id > max) {
					max = id;
				}
			}
			return max;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return 999;
	}

	/**
	 * 获取本地存储的项目信息
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static List<Project> getProjectList() throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		InputStream in = new FileInputStream(getProjectsFile());
		parser.setInput(new InputStreamReader(in));
		int eventType = parser.getEventType();
		List<Project> projectList = new ArrayList<>(5);
		Project project = null;
		Map<String, Double> totalMap = null;
		Map<String, Double> remainMap = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String nodeName = parser.getName();
			switch (eventType) {
				case XmlPullParser.START_TAG:
					if ("project".equals(nodeName)) {
						//新增项目
						project = new Project();
						project.setId(Integer.parseInt(parser.getAttributeValue(null, "id")));
						project.setName(parser.getAttributeValue(null, "name"));
						project.setYear(parser.getAttributeValue(null, "year"));
						project.setDepartment(parser.getAttributeValue(null, "department"));
						project.setCategory(parser.getAttributeValue(null, "category"));
					}else if ("budgetList".equals(nodeName)) {
						//新增科目列表
						totalMap = new HashMap<>(20);
						remainMap = new HashMap<>(20);
					} else if ("budget".equals(nodeName)) {
						//添加科目
						String name = parser.getAttributeValue(null, "name");
						String total = parser.getAttributeValue(null, "total");
						String remain = parser.getAttributeValue(null, "remain");
						totalMap.put(name, Double.parseDouble(total));
						remainMap.put(name, Double.parseDouble(remain));
					}
					break;
				case XmlPullParser.END_TAG:
					if ("project".equals(nodeName)) {
						//添加项目到list
						projectList.add(project);
						Log.d("XML解析", "添加project" + project);
					} else if ("budgetList".equals(nodeName)) {
						//添加科目到项目中
						project.setTotalMap(totalMap);
						project.setRemainMap(remainMap);
					}
					break;
				default:
			}
			eventType = parser.next();
		}
		return projectList;
	}

	public static Project getProjectById(int id) throws IOException, XmlPullParserException {
		List<Project> projectList = getProjectList();
		for (Project project : projectList) {
			if (project.getId() == id) {
				return project;
			}
		}
		return new Project();
	}

	/**
	 * 添加项目
	 * @param project 新项目
	 */
	public static void addProject(Project project) throws DocumentException, IOException {
		Document document = new SAXReader().read(getProjectsFile());
		Element rootElem = document.getRootElement();
		Element projectElem = rootElem.addElement("project");
		projectElem.addAttribute("id", project.getId() + "");
		projectElem.addAttribute("name", project.getName());
		projectElem.addAttribute("year", project.getYear());
		projectElem.addAttribute("department", project.getDepartment());
		projectElem.addAttribute("category", project.getCategory());
		Element listElem = projectElem.addElement("budgetList");
		Map<String, Double> totalMap = project.getTotalMap();
		Map<String, Double> remainMap = project.getRemainMap();
		for (String key : totalMap.keySet()) {
			Element budgetElem = listElem.addElement("budget");
			budgetElem.addAttribute("name", key);
			budgetElem.addAttribute("total", "" + totalMap.get(key));
			budgetElem.addAttribute("remain", "" + remainMap.get(key));
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(getProjectsFile()), format);
		xmlWriter.write(document);
		xmlWriter.close();
		Log.d("ProjectDataHelper", "添加成功" + project);
	}

//	public static void updateProject(Project project) {
//		try {
//			Document document = new SAXReader().read(getProjectsFile());
//			int id = project.getId();
//			String xpath = String.format("/projectList/project[@id='%d']", id);
//			Element projectElement = (Element) document.selectSingleNode(xpath);
//
//			OutputFormat format = OutputFormat.createPrettyPrint();
//			format.setEncoding("utf-8");
//			XMLWriter xmlWriter = null;
//			xmlWriter = new XMLWriter(new FileOutputStream(getProjectsFile()), format);
//			xmlWriter.write(document);
//			xmlWriter.close();
//		} catch (IOException | DocumentException e) {
//			e.printStackTrace();
//		}
//
//		Log.d("ProjectDataHelper", "修改成功");
//	}

	/**
	 * 保存修改剩余预算后的项目，根据序号决定修改对象
	 * @param project 修改后的项目
	 */
	public static void saveProjectRemain(Project project) throws IOException, DocumentException {
		Document document = new SAXReader().read(getProjectsFile());
		int id = project.getId();
		Map<String, Double> remainMap = project.getRemainMap();
		for (String key : remainMap.keySet()) {
			String xpath = String.format(
					"/projectList/project[@id='%d']/budgetList/budget[@name='%s']", id, key);
			Element budgetNode = (Element) document.selectSingleNode(xpath);
			budgetNode.attribute("remain").setText("" + remainMap.get(key));
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(getProjectsFile()), format);
		xmlWriter.write(document);
		xmlWriter.close();
		Log.d("ProjectDataHelper", "保存成功");
	}

	private static File getProjectsFile() {
		return Util.getProjectsFile();
	}

	/**
	 * 保存Excel到根目录下
	 * @param project
	 * @param staff
	 * @param itemMap
	 */
	public static void createExcel(Project project, Staff staff, Map<String, Double> itemMap, Map<String, Double> reimbMap) throws IOException, WriteException, BiffException {
		String filename = Util.getDateTime() + ".xls";
		//报销单生成
		WritableWorkbook workbook = Workbook.createWorkbook(new File(BaseConfig.instance.itemOutputDir, filename));
		WritableSheet sheet1 = workbook.createSheet("费用报销审批单", 0);
		mergeCells(sheet1, 0, 0, 0, 8);
		addCell(sheet1, 0, 0, "费用报销审批单");
		addCell(sheet1, 1, 0, "报销部门：");
		addCell(sheet1, 1, 1, staff.getDepartment());
		addCell(sheet1, 1, 3, Util.getYear());
		addCell(sheet1, 1, 4, "年");
		addCell(sheet1, 1, 5, Util.getMonth());
		addCell(sheet1, 1, 6, "月");
		addCell(sheet1, 1, 7, Util.getDay());
		addCell(sheet1, 1, 8, "日填");
		addCell(sheet1, 2, 0, "用途");
		addCell(sheet1, 2, 1, "金额（元）");

		int row = 3;
		for (String key : itemMap.keySet()) {
			addCell(sheet1, row, 0, key);
			addCell(sheet1, row, 1, "" + itemMap.get(key));
			row++;
		}
		addCell(sheet1, row, 0, "报销人：");
		addCell(sheet1, row, 1, staff.getName());
		workbook.write();
		workbook.close();

		//报账单生成
		InputStream tempIn = Util.getApplicationContext().getAssets().open("报账申请表模版.xls");
		Workbook temp = Workbook.getWorkbook(tempIn);
		WritableWorkbook wb = Workbook.createWorkbook(new File(BaseConfig.instance.budgetOutputDir, filename), temp);
		WritableSheet sheet2 = wb.getSheet(0);
		//获取格式
		Cell cell = sheet2.getCell(0,5);
		CellFormat format = cell.getCellFormat();
		if (format == null) {
			format = new WritableCellFormat();
		}

		//输入费用
		row = 6;
		Map<String, Double> totalMap = project.getTotalMap();
		Map<String, Double> remainMap = project.getRemainMap();
		double reimbSum = 0;
		double totalSum = 0;
		double remainSum = 0;
		double acculateReimbSum = 0;
		for (String key : totalMap.keySet()) {
			double reimb = reimbMap.get(key) == null ? 0.0 : reimbMap.get(key);
			double total = totalMap.get(key);
			double remain = remainMap.get(key);
			double acculateReimb = total - remain;
			reimbSum += reimb;
			totalSum += total;
			remainSum += remain;
			acculateReimbSum += acculateReimb;
			//费用名称
			sheet2.addCell(new Label(0, row, key, format));
			//合同预算
			sheet2.addCell(new Label(1, row, "" + total, format));
			//本次报账
			sheet2.addCell(new Label(2, row, "" + reimb, format));
			//累积报账
			sheet2.addCell(new Label(3, row, "" + acculateReimb, format));
			//结存资金
			sheet2.addCell(new Label(4, row, "" + remain, format));
			row++;
		}
		//合计
		sheet2.addCell(new Label(1, row, "" + totalSum, format));
		sheet2.addCell(new Label(2, row, "" + reimbSum, format));
		sheet2.addCell(new Label(3, row, "" + acculateReimbSum, format));
		sheet2.addCell(new Label(4, row, "" + remainSum, format));
		//输入单位、经办人、项目名、资金总额
		sheet2.addCell(new Label(1, 2, project.getDepartment(), format));
		sheet2.addCell(new Label(4, 2, staff.getName(), format));
		sheet2.addCell(new Label(1, 3, project.getName(), format));
		sheet2.addCell(new Label(1, 4, reimbSum + "", format));
		//输入年月日
		String date = Util.getYear() + "年" + Util.getMonth() + "月" + Util.getDay() + "日";
		sheet2.addCell(new Label(0, 1, date, format));
		wb.write();
		wb.close();
	}

	/**
	 * 合并格子
	 * @param sheet
	 * @param r1
	 * @param c1
	 * @param r2
	 * @param c2
	 */
	public static void mergeCells(WritableSheet sheet, int r1, int c1, int r2, int c2) throws WriteException {
		sheet.mergeCells(c1, r1, c2, r2);
	}

	/**
	 * 给sheet添加格子
	 * @param sheet
	 * @param row
	 * @param cow
	 * @param label
	 */
	public static void addCell(WritableSheet sheet, int row, int cow, String label) throws WriteException {
		sheet.addCell(new Label(cow, row, label));
	}
}
