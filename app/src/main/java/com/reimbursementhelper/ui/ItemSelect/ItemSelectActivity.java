package com.reimbursementhelper.ui.ItemSelect;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reimbursementhelper.R;
import com.reimbursementhelper.base.BaseActivity;
import com.reimbursementhelper.ui.reimb.ReimbActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ItemSelectActivity extends BaseActivity implements ItemSelectConstract.View {

	@BindView(R.id.lil_item_container)
	LinearLayout lilItemContainer;
	@BindView(R.id.btn_item_add)
	Button btnItemAdd;
	@BindView(R.id.btn_item_enter)
	Button btnItemEnter;

	ItemSelectConstract.Presenter mPresenter;
	AlertDialog addDialog;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPresenter = new ItemSelectPresenter(this);
		mPresenter.bindView(this);
	}

	@Override
	public int getContentView() {
		return R.layout.activity_item_select;
	}


	@Override
	public void initView() {
		addDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
		final EditText use = (EditText) addDialogView.findViewById(R.id.et_item_add_use);
		final EditText money = (EditText) addDialogView.findViewById(R.id.et_item_add_money);

		addDialog = new AlertDialog.Builder(this).setView(addDialogView).setTitle(
				"添加报销条目").setNegativeButton("取消", null).setPositiveButton("添加",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String key = use.getText().toString();
						double value = Double.parseDouble(money.getText().toString());
						//判断是否重复
						if (mPresenter.isItemDuplicated(key)) {
							Toast.makeText(ItemSelectActivity.this, "条目重复！",
									Toast.LENGTH_SHORT).show();
							return;
						}
						//添加
						mPresenter.addItem(key, value);
						dialog.dismiss();
						//更新界面
						final View child = LayoutInflater.from(ItemSelectActivity.this).inflate(
								R.layout.item_reimb, null);
						lilItemContainer.addView(child);
						((TextView) child.findViewById(R.id.tv_item_label)).setText(key);
						((TextView) child.findViewById(R.id.tv_item_money)).setText(
								String.format("%.2f", value));
						//item的删除事件
						TextView tvDel = (TextView) child.findViewById(R.id.tv_item_del);
						tvDel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//删除条目
								String key = use.getText().toString();
								Toast.makeText(ItemSelectActivity.this, "删除" + key,
										Toast.LENGTH_SHORT).show();
								mPresenter.removeItem(key);
								lilItemContainer.removeView(child);
							}
						});
					}
				}).setCancelable(false).create();
	}

	/**
	 * 更新界面
	 */
	void updateView() {

	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {

	}

	View addDialogView;

	@OnClick({R.id.btn_item_add, R.id.btn_item_enter})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_item_add:
				addItem();
				break;
			case R.id.btn_item_enter:
				startActivity(new Intent(this, ReimbActivity.class));
				break;
		}
	}

	void addItem() {
		addDialog.show();
		Toast.makeText(this, "dialog showing", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.onBackPressed();
		}
		return true;
	}

	@Override
	public void initToolbar() {
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
}
