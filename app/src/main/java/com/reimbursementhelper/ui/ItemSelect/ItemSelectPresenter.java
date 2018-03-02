package com.reimbursementhelper.ui.ItemSelect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mingC
 * @date 2018/2/13
 */
public class ItemSelectPresenter implements ItemSelectConstract.Presenter{
	private ItemSelectActivity mActivity;
	private ItemSelectConstract.View mView;

	private Map<String, Double> mReimbItemMap = new HashMap<>(5);

	public ItemSelectPresenter(ItemSelectConstract.View view) {
		mView = view;
		//绑定到Global中
	}

	@Override
	public void bindView(ItemSelectActivity activity) {
		mActivity = activity;
		mActivity.getGlobal().itemMap = mReimbItemMap;
	}

	@Override
	public Map<String, Double> getReimbItemMap() {
		return mReimbItemMap;
	}

	@Override
	public void addItem(String item, double money) {
		mReimbItemMap.put(item, money);
	}

	@Override
	public double getMoney(String key) {
		return mReimbItemMap.get(key);
	}

	@Override
	public void setMoney(String key, double money) {
		mReimbItemMap.put(key, money);
	}

	@Override
	public boolean isItemDuplicated(String itemName) {
		return mReimbItemMap.get(itemName) != null;
	}

	@Override
	public void removeItem(String key) {
		mReimbItemMap.remove(key);
	}
}
