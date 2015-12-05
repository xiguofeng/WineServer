package com.xgf.wineserver.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Version;
import com.xgf.wineserver.network.logic.AppLogic;
import com.xgf.wineserver.utils.SystemUtils;

public class SplashActivity extends BaseActivity {

	public static final String TAG = SplashActivity.class.getSimpleName();

	private final int SPLISH_DISPLAY_LENGTH = 1000; // 延迟2秒启动登陆界面

	private String mDownUrl;

	Handler mVersionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case AppLogic.GET_VERSION_SUC: {
				if (null != msg.obj) {
					Version version = (Version) msg.obj;
					mDownUrl = version.getUrl();

					if (TextUtils.isEmpty(mDownUrl)) {
						if ("Y".equals(version.getForce())) {
							showDialog();
						}
					}
				} else {
					jump();
				}
				break;
			}
			case AppLogic.GET_VERSION_FAIL: {
				jump();
				break;
			}
			case AppLogic.GET_VERSION_EXCEPTION: {
				jump();
				break;
			}
			case AppLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		initData();
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

	}

	private void initData() {
		checkUpdateVersion();
	}

	private void checkUpdateVersion() {
		AppLogic.getVersion(SplashActivity.this, mVersionHandler,
				SystemUtils.getVersion(getApplicationContext()));
	}

	protected void jump() {
		// 启动三秒后进度到登陆界面
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				openActivity(LoginActivity.class);
				SplashActivity.this.finish();
			}
		}, SPLISH_DISPLAY_LENGTH);

	}

	private void showDialog() {
		// 先new出一个监听器，设置好监听
		DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case Dialog.BUTTON_POSITIVE: {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(mDownUrl));
					startActivity(intent);
					break;
				}
				case Dialog.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		// dialog参数设置
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("提示"); // 设置标题
		builder.setMessage("版本强制升级"); // 设置内容
		// builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("下载新版本", dialogOnclicListener);
		builder.setNegativeButton("退出应用", dialogOnclicListener);
		builder.create().show();
	}
}
