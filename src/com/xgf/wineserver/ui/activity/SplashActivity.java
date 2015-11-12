package com.xgf.wineserver.ui.activity;

import cn.jpush.android.api.JPushInterface;

import com.xgf.wineserver.R;

import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	public static final String TAG = SplashActivity.class.getSimpleName();

	private final int SPLISH_DISPLAY_LENGTH = 1000; // 延迟2秒启动登陆界面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		initView();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(SplashActivity.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(SplashActivity.this);
	}

	@Override
	protected void initView() {
		// 启动三秒后进度到登陆界面
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				openActivity(LoginActivity.class);
				SplashActivity.this.finish();
			}
		}, SPLISH_DISPLAY_LENGTH);

	}

}
