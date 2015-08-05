package com.xgf.wineserver.ui.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.xgf.wineserver.R;

public class HomeActivity extends TabActivity implements
		android.view.View.OnClickListener {

	public static final String TAG = HomeActivity.class.getSimpleName();

	public static final String TAB_MAIN = "MAIN";
	public static final String TAB_CART = "CART";
	public static final String TAB_MORE = "MORE";

	private RadioGroup mTabButtonGroup;

	private static TabHost mTabHost;

	private static RelativeLayout mPayMenuRl;

	private static LinearLayout mMainPayMenuLl;

	private static LinearLayout mCartPayMenuLl;

	private static LinearLayout mCartBuyLl;

	private static TextView mMainTotalMoneyTv;

	private static TextView mCartTotalMoneyTv;

	private static TextView mCartTotalNumTv;

	private CheckBox mCheckAllIb;

	private LinearLayout mBuyLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		findViewById();
		initView();
		initData();
	}

	private void findViewById() {
		mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);
	}

	private void initView() {
		mTabHost = getTabHost();

		Intent i_home = new Intent(this, MainActivity.class);
		Intent i_cart = new Intent(this, MainActivity.class);
		Intent i_more = new Intent(this, MainActivity.class);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
				.setContent(i_home));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_CART).setIndicator(TAB_CART)
				.setContent(i_cart));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_MORE).setIndicator(TAB_MORE)
				.setContent(i_more));

		mTabHost.setCurrentTabByTag(TAB_MAIN);

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.home_tab_main_rb:
							mTabHost.setCurrentTabByTag(TAB_MAIN);

							break;

						case R.id.home_tab_logistics_rb:
							mTabHost.setCurrentTabByTag(TAB_CART);

							break;

						case R.id.home_tab_rob_rb:
							mTabHost.setCurrentTabByTag(TAB_MORE);

							break;

						case R.id.home_tab_accout_rb:
							mTabHost.setCurrentTabByTag(TAB_MORE);

							break;
						default:
							break;
						}
					}
				});

	}

	private void initData() {
		// mTabHost.setCurrentTabByTag(TAB_MAIN);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public static void modifyMainPayView(String totalPrice) {
		mMainTotalMoneyTv.setText(totalPrice);
		if (Double.parseDouble(totalPrice) > 0) {
			showOrhHideMainPayBar(true);
		}
	}

	public static void modifyCartPayView(String totalPrice, String totalNum) {
		mCartTotalMoneyTv.setText(totalPrice);
		mCartTotalNumTv.setText("(" + totalNum + ")");
	}

	public static void showOrhHideMainPayBar(boolean flag) {
		mPayMenuRl.setVisibility(View.VISIBLE);
		if (flag && mTabHost.getCurrentTabTag().endsWith(TAB_MAIN)) {
			mMainPayMenuLl.setVisibility(View.VISIBLE);
			mCartPayMenuLl.setVisibility(View.GONE);
		} else {
			mPayMenuRl.setVisibility(View.GONE);
		}
	}

	public static void showOrHideCartPayBar(boolean flag) {
		mPayMenuRl.setVisibility(View.VISIBLE);
		if (flag && mTabHost.getCurrentTabTag().endsWith(TAB_CART)) {
			mMainPayMenuLl.setVisibility(View.GONE);
			mCartPayMenuLl.setVisibility(View.VISIBLE);
		} else {
			mPayMenuRl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {

	}

	protected void exitApp() {
		showAlertDialog("退出程序", "确定退出？", "确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}, "取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	protected void showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
	}

}
