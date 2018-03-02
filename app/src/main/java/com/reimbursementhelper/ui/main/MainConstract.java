package com.reimbursementhelper.ui.main;

/**
 * @author mingC
 * @date 2018/2/13
 */
public interface MainConstract {
	interface View {

	}

	interface Presenter {
		/**
		 * 绑定活动
		 * @param activity 主活动
		 */
		void bindView(MainActivity activity);

		
	}
}
