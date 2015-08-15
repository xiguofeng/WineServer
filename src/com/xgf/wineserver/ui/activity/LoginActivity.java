package com.xgf.wineserver.ui.activity;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.User;
import com.xgf.wineserver.network.logic.UserLogic;
import com.xgf.wineserver.utils.UserInfoManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	public static final String ORIGIN_FROM_REG_KEY = "com.reg";

	public static final String ORIGIN_FROM_ORDER_KEY = "com.order";

	private EditText mAccountEt;
	private EditText mPassWordEt;
	private CheckBox mRemberpswCb;
	// private LinearLayout layoutProcess;
	private Button mLoginBtn;

	private String mAccount;
	private String mPassWord;

	private User mUser = new User();

	private Context mContext = LoginActivity.this;

	// 登陆装填提示handler更新主线程，提示登陆状态情况
	Handler mLoginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.LOGIN_SUC: {
				if (null != msg.obj) {
					mUser = (User) msg.obj;
					mUser.setPassword(mPassWord);
					UserInfoManager.setRememberPwd(mContext, true);
					UserInfoManager.saveUserInfo(LoginActivity.this, mUser);
					UserInfoManager.setUserInfo(LoginActivity.this);
					UserInfoManager.setLoginIn(LoginActivity.this, true);

					Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}

				break;
			}
			case UserLogic.LOGIN_FAIL: {
				Toast.makeText(mContext, R.string.login_fail, Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.LOGIN_EXCEPTION: {
				break;
			}
			case UserLogic.NET_ERROR: {
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		initView();
		initData();

	}

	protected void initView() {
		/* 初始化控件 */
		mAccountEt = (EditText) findViewById(R.id.login_username);
		mPassWordEt = (EditText) findViewById(R.id.login_password);

		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mLoginBtn.setOnClickListener(this);
	}

	private void initData() {
		// if (!TextUtils.isEmpty(getIntent().getAction()) &&
		// getIntent().getAction().equals(ORIGIN_FROM_REG_KEY)) {
		// mUserNameEt.setText(UserInfoManager.userInfo.getUserName());
		// mPassWordEt.setText(UserInfoManager.userInfo.getPassword());
		// login();
		//
		// } else
		// if (!TextUtils.isEmpty(getIntent().getAction()) &&
		// getIntent().getAction().equals(ORIGIN_FROM_ORDER_KEY)) {
		//
		// if (UserInfoManager.getRememberPwd(mContext)) {
		// mUserNameEt.setText(UserInfoManager.userInfo.getUserName());
		// mPassWordEt.setText(UserInfoManager.userInfo.getPassword());
		// }
		// // mShortcutBuyTv.setVisibility(View.GONE);
		// // // 检测是否存在SD卡，存在SD卡的情况下进行判断文件是否存在
		// // if (AndroidTools.isHasSD()) {
		// // // 检测是否存在文件，不存在，则创建xml文件
		// // if (AndroidTools.isFileExists(Fileconfig.xmlinfopath)) {
		// // // 存在xml,读取内容，放置到表单中国
		// // String xmlpath = Fileconfig.sdrootpath
		// // + Fileconfig.xmlinfopath;
		// // mUserNameEt.setText(XMLHelper.readXMLByNodeName(
		// // UserXmlParseConst.USERNAME, xmlpath));
		// // mPassWordEt.setText(XMLHelper.readXMLByNodeName(
		// // UserXmlParseConst.PASSWORD, xmlpath));
		// // mRemberpswCb.setChecked(true);
		// //
		// // }
		// // } else {
		// // // 没有内存卡，不需要执行操作
		// // }
		// } else {
		// if (UserInfoManager.getRememberPwd(mContext)) {
		// // UserInfoManager.setUserInfo(LoginActivity.this);
		// mUserNameEt.setText("13611586008");
		// mPassWordEt.setText("123456");
		// mRemberpswCb.setChecked(true);
		// }
		// }

		Log.e("xxx_login", "" + UserInfoManager.getRememberPwd(mContext));
		if (UserInfoManager.getRememberPwd(mContext)) {
			UserInfoManager.setUserInfo(mContext);
			// UserInfoManager.setUserInfo(LoginActivity.this);
			mAccountEt.setText(UserInfoManager.userInfo.getAccount());
			mPassWordEt.setText(UserInfoManager.userInfo.getPassword());
		}

	}

	private void login() {
		// 获取用户的登录信息，连接服务器，获取登录状态
		mAccount = mAccountEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();

		if ("".equals(mAccount) || "".equals(mPassWord)) {
			// layoutProcess.setVisibility(View.GONE);
			Toast.makeText(LoginActivity.this, mContext.getString(R.string.login_emptyname_or_emptypwd),
					Toast.LENGTH_SHORT).show();
		} else {
			mUser.setAccount(mAccount);
			mUser.setPassword(mPassWord);
			UserLogic.login(mContext, mLoginHandler, mUser);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn: {
			login();
			break;
		}

		default:
			break;
		}

	}

}
