package com.xgf.wineserver.ui.activity;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.User;
import com.xgf.wineserver.network.logic.UserLogic;
import com.xgf.wineserver.utils.UserInfoManager;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ModifyPwdActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	public static final String ORIGIN_FROM_REG_KEY = "com.reg";

	public static final String ORIGIN_FROM_ORDER_KEY = "com.order";

	public static final int TIME_UPDATE = 1;

	private EditText mPhoneEt;
	private EditText mAuthCodeEt;
	private EditText mPassWordEt;
	private EditText mConfirmPwdEt;

	private Button mModifyBtn;

	private String mPhone;
	private String mPassWord;
	private String mConfirmPwd;
	private String mAuthCode;
	private String mAuthConfirm;

	private LinearLayout mAuthCodeLl;
	private TextView mTimingTv;

	private User mUser = new User();

	private int mTiming = 60;

	private Context mContext = ModifyPwdActivity.this;

	// 登陆装填提示handler更新主线程，提示登陆状态情况
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.MODIFY_PWD_SUC: {

				UserInfoManager.saveUserInfo(ModifyPwdActivity.this, mUser);
				UserInfoManager.setUserInfo(ModifyPwdActivity.this);
				UserInfoManager.setLoginIn(ModifyPwdActivity.this, true);

				Toast.makeText(ModifyPwdActivity.this,
						mContext.getString(R.string.modify_pwd_suc),
						Toast.LENGTH_SHORT).show();

				HomeActivity.setTab(HomeActivity.TAB_MAIN);

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

			case UserLogic.SEND_AUTHCODE_SUC: {
				if (null != msg.obj) {
					mAuthConfirm = (String) msg.obj;
				}
				mTimeHandler.sendEmptyMessage(TIME_UPDATE);
				break;
			}
			case UserLogic.SEND_AUTHCODE_FAIL: {
				Toast.makeText(mContext, R.string.login_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.SEND_AUTHCODE_EXCEPTION: {
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

	private Handler mTimeHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIME_UPDATE: {
				if (mTiming > 0) {
					mTiming--;
					mTimingTv.setText(String.valueOf(mTiming));
					mAuthCodeLl.setClickable(false);
					mAuthCodeLl.setBackground(mContext.getResources()
							.getDrawable(R.drawable.corners_bg_gray_all));
					;
					mTimeHandler.sendEmptyMessageDelayed(TIME_UPDATE, 1000);
				} else {
					mAuthCodeLl.setClickable(true);
					mAuthCodeLl.setBackground(mContext.getResources()
							.getDrawable(R.drawable.corners_bg_orange_all));
					mTimingTv
							.setText(getString(R.string.get_verification_code));
					mTiming = 60;
				}
				break;
			}
			default:
				break;
			}
		};

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

		mAuthCodeLl = (LinearLayout) findViewById(R.id.modify_psw_code_ll);
		mAuthCodeLl.setOnClickListener(this);
		mTimingTv = (TextView) findViewById(R.id.modify_psw_code_btn_tv);

		mPhoneEt.addTextChangedListener(this);
		mAuthCodeEt.addTextChangedListener(this);
		mPassWordEt.addTextChangedListener(this);
		mConfirmPwdEt.addTextChangedListener(this);

		mModifyBtn = (Button) findViewById(R.id.modify_psw_btn);
		mModifyBtn.setOnClickListener(this);
	}

	private void initData() {
	}

	private void modify() {
		// 获取用户的登录信息，连接服务器，获取登录状态
		mPhone = mPhoneEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();
		mConfirmPwd = mConfirmPwdEt.getText().toString().trim();
		mAuthCode = mAuthCodeEt.getText().toString().trim();

		if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mPassWord)
				|| TextUtils.isEmpty(mAuthCode)
				|| TextUtils.isEmpty(mConfirmPwd)) {
			// layoutProcess.setVisibility(View.GONE);
			Toast.makeText(ModifyPwdActivity.this,
					mContext.getString(R.string.login_emptyname_or_emptypwd),
					Toast.LENGTH_SHORT).show();
		} else {
			mUser = UserInfoManager.userInfo;
			mUser.setUserName(mPhone);
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
		case R.id.modify_psw_code_ll: {
			mPhone = mPhoneEt.getText().toString().trim();
			if (!TextUtils.isEmpty(mPhone)) {
				UserLogic.sendAuthCode(mContext, mHandler, mPhone, "3");

			} else {
				Toast.makeText(mContext, getString(R.string.mobile_phone_hint),
						Toast.LENGTH_SHORT).show();
			}
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
