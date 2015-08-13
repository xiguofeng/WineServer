package com.xgf.wineserver.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.User;
import com.xgf.wineserver.network.logic.UserLogic;
import com.xgf.wineserver.utils.UserInfoManager;

public class ModifyPswActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	public static final String ORIGIN_FROM_REG_KEY = "com.reg";

	public static final String ORIGIN_FROM_ORDER_KEY = "com.order";

	private EditText mPhoneEt;
	private EditText mAuthCodeEt;
	private EditText mPassWordEt;
	private EditText mConfirmPwdEt;

	private Button mModifyBtn;

	private String mUserName;
	private String mPassWord;
	private String mConfirmPwd;
	private String mAuthCode;

	private User mUser = new User();

	private Context mContext = ModifyPswActivity.this;

	// 登陆装填提示handler更新主线程，提示登陆状态情况
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.MODIFY_PWD_SUC: {
				if (null != msg.obj) {
					mUser = (User) msg.obj;
					UserInfoManager.saveUserInfo(ModifyPswActivity.this, mUser);
					UserInfoManager.setUserInfo(ModifyPswActivity.this);
					UserInfoManager.setLoginIn(ModifyPswActivity.this, true);

					Intent intent = new Intent(ModifyPswActivity.this,
							HomeActivity.class);
					startActivity(intent);
					ModifyPswActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				}

				break;
			}
			case UserLogic.MODIFY_PWD_FAIL: {
				// Toast.makeText(mContext, R.string.login_fail,
				// Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.MODIFY_PWD_EXCEPTION: {
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
		setContentView(R.layout.modify_psw);
		initView();
		initData();

	}

	protected void initView() {
		/* 初始化控件 */
		mPhoneEt = (EditText) findViewById(R.id.modify_psw_phone_et);
		mAuthCodeEt = (EditText) findViewById(R.id.modify_psw_code_et);
		mPassWordEt = (EditText) findViewById(R.id.modify_psw_psw_et);
		mConfirmPwdEt = (EditText) findViewById(R.id.modify_psw_confirm_psw_et);

		mPhoneEt.addTextChangedListener(this);
		mAuthCodeEt.addTextChangedListener(this);
		mPassWordEt.addTextChangedListener(this);
		mConfirmPwdEt.addTextChangedListener(this);

		mModifyBtn = (Button) findViewById(R.id.modify_psw_btn);
		mModifyBtn.setOnClickListener(this);
	}

	private void initData() {
		mPhoneEt.setText("13611586008");
		mPassWordEt.setText("123456");
		mConfirmPwdEt.setText("123456");
	}

	private void modify() {
		// 获取用户的登录信息，连接服务器，获取登录状态
		mUserName = mPhoneEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();
		mConfirmPwd = mConfirmPwdEt.getText().toString().trim();
		mAuthCode = mAuthCodeEt.getText().toString().trim();

		if (TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassWord)
				|| TextUtils.isEmpty(mAuthCode)
				|| TextUtils.isEmpty(mConfirmPwd)) {
			// layoutProcess.setVisibility(View.GONE);
			Toast.makeText(ModifyPswActivity.this,
					mContext.getString(R.string.login_emptyname_or_emptypwd),
					Toast.LENGTH_SHORT).show();
		} else {
			mUser.setUserName(mUserName);
			mUser.setPassword(mPassWord);
			UserLogic.modifyPwd(mContext, mHandler, mUser, mAuthCode);
		}
	}

	private void updateShow() {
		if (TextUtils.isEmpty(mPhoneEt.getText().toString().trim())) {
			mPhoneEt.setError(getString(R.string.user_name_hint));
		}
		if (TextUtils.isEmpty(mAuthCodeEt.getText().toString().trim())) {
			mAuthCodeEt.setError(getString(R.string.logistics_code_hint));
		}
		if (TextUtils.isEmpty(mPassWordEt.getText().toString().trim())) {
			mPassWordEt.setError(getString(R.string.user_psw_hint));
		}
		if (TextUtils.isEmpty(mConfirmPwdEt.getText().toString().trim())) {
			mConfirmPwdEt.setError(getString(R.string.user_confirm_psw_hint));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_psw_btn: {
			modify();
			break;
		}

		default:
			break;
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// updateShow();
	}

}
