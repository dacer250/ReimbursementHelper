package com.reimbursementhelper.ui.ItemSelect;

import java.util.Map;

/**
 * @author mingC
 * @date 2018/2/13
 */
public interface ItemSelectConstract {

	interface Presenter {
		void bindView(ItemSelectActivity activity);

		/**
		 * 获取默认报销条目Map
		 * @return 报销条目Map，eg：{"交通":100, "住宿":200}
		 */
		Map<String, Double> getReimbItemMap();

		/**
		 * 添加条目
		 * @param item 新条目
		 * @param money 金额
		 */
		void addItem(String item, double money);

		/**
		 * 删除条目
		 * @param key 条目标签
		 */
		void removeItem(String key);

		/**
		 * 获取某条目当前金额
		 * @param key 键
		 * @return 金额
		 */
		double getMoney(String key);

		/**
		 * 处理报销条目金额
		 * @param key 条目键
		 * @param money 新值
		 */
		void setMoney(String key, double money);

		/**
		 * 检查条目是否重复
		 * @param itemName 条目名
		 * @return 重复返回true
		 */
		boolean isItemDuplicated(String itemName);
	}

	interface View {

	}

}
