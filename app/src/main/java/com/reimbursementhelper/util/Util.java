package com.reimbursementhelper.util;

import android.content.Context;

import com.reimbursementhelper.base.Global;
import com.reimbursementhelper.base.MyApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author mingC
 * @date 2018/2/13
 */
public class Util {
	private static Context sContext;

	public static void init(Context applicationContext) {
		sContext = applicationContext;
	}

	public static void destory() {
		if (sContext != null) {
			sContext = null;
		}
	}

	public static Context getApplicationContext() {
		return sContext;
	}

	public static Global getGlobal() {
		return ((MyApplication) sContext).getGlobal();
	}

	/**
	 * 获取保存项目的文件，使用前确保Global已经初始化
	 * @return
	 */
	public static File getProjectsFile() {
		return new File(Util.getApplicationContext().getFilesDir(), "projects.xml");
	}

	public static String getYear() {
		Calendar calendar = Calendar.getInstance();
		return "" + calendar.get(Calendar.YEAR);
	}

	public static String getMonth() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		return "" + month;
	}

	public static String getDay() {
		Calendar calendar = Calendar.getInstance();
		return "" + calendar.get(Calendar.DAY_OF_MONTH);
	}


	public static String getDateTime() {
		java.util.Date date = Calendar.getInstance().getTime();
		return new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.CHINA).format(date);
	}
}
