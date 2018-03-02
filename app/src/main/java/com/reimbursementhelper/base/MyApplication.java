package com.reimbursementhelper.base;

import android.content.res.Configuration;

import com.reimbursementhelper.util.Util;

import org.litepal.LitePalApplication;

/**
 * @author mingC
 * @date 2018/2/13
 */
public class MyApplication extends LitePalApplication{
	private Global mGlobal;

	@Override
	public void onCreate() {
		super.onCreate();
		Util.init(this);
		BaseConfig.initConfig(this);
		mGlobal = new Global();

	}


	public Global getGlobal() {
		return mGlobal;
	}


	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}
}
