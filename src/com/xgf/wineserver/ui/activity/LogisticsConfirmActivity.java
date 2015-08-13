package com.xgf.wineserver.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xgf.wineserver.R;
import com.xgf.wineserver.network.logic.OrderLogic;
import com.xgf.wineserver.utils.UserInfoManager;

public class LogisticsConfirmActivity extends Activity implements
		OnClickListener {

	private Context mContext;
	private EditText mCodeEt;
	private Button mSubmitBtn;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_CONFIRM_SUC: {
				if (null != msg.obj) {
				}
				break;
			}
			case OrderLogic.ORDER_CONFIRM_FAIL: {

				break;
			}
			case OrderLogic.ORDER_CONFIRM_EXCEPTION: {
				break;
			}
			case OrderLogic.NET_ERROR: {
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
		setContentView(R.layout.logistics_confirm);
		mContext = LogisticsConfirmActivity.this;
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		mContext = LogisticsConfirmActivity.this;
		mCodeEt = (EditText) findViewById(R.id.logistics_confirm_code_et);
		mSubmitBtn = (Button) findViewById(R.id.logistics_confirm_btn);
		mSubmitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logistics_confirm_btn: {
			if (!TextUtils.isEmpty(mCodeEt.getText().toString().trim())) {

				OrderLogic.recieveConfirm(mContext, mHandler,
						UserInfoManager.userInfo.getUserId(), mCodeEt.getText()
								.toString().trim());

			} else {

			}
			break;
		}
		default:
			break;
		}
	}

}
