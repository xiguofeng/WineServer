package com.xgf.wineserver.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.entity.OrderState;
import com.xgf.wineserver.network.config.MsgResult;
import com.xgf.wineserver.network.logic.OrderLogic;
import com.xgf.wineserver.ui.adapter.OrderWineAdapter;
import com.xgf.wineserver.ui.utils.ListItemClickParameterHelp;
import com.xgf.wineserver.ui.view.CustomProgressDialog2;
import com.xgf.wineserver.utils.UserInfoManager;

public class LogisticsConfirmActivity extends Activity implements OnClickListener, ListItemClickParameterHelp {
	private Context mContext;

	public TextView mNullTv;
	private ListView mOrderLv;
	private HashMap<String, Object> mOrderMsgMap = new HashMap<String, Object>();
	private OrderWineAdapter mOrderAdapter;
	private ArrayList<Order> orderList = new ArrayList<Order>();

	private CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDERLIST_HISTORY_GET_SUC: {
				if (null != msg.obj) {
					mOrderMsgMap.clear();
					mOrderMsgMap.putAll((Map<? extends String, ? extends Object>) msg.obj);
					mOrderAdapter.notifyDataSetChanged();

					mNullTv.setVisibility(View.VISIBLE);
					if (((ArrayList<Order>) mOrderMsgMap.get(MsgResult.ORDER_TAG)).size() > 0) {
						mNullTv.setVisibility(View.GONE);
					}

				}
				break;
			}
			case OrderLogic.ORDERLIST_HISTORY_GET_FAIL: {

				break;
			}
			case OrderLogic.ORDERLIST_HISTORY_GET_EXCEPTION: {
				break;
			}
			case OrderLogic.ORDER_CONFIRM_SUC: {
				Toast.makeText(mContext, mContext.getString(R.string.auth_receive_suc), Toast.LENGTH_SHORT).show();
				OrderLogic.getGrabOrdersHistory(mContext, mHandler, UserInfoManager.userInfo.getUserId(),
						OrderState.ORDER_STATUS_DELIVERY, "0", "30");
				break;
			}
			case OrderLogic.ORDER_CONFIRM_FAIL: {
				Toast.makeText(mContext, mContext.getString(R.string.auth_receive_fail), Toast.LENGTH_SHORT).show();
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
			if (null != mCustomProgressDialog && mCustomProgressDialog.isShowing()) {
				mCustomProgressDialog.dismiss();
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logi_confirm);
		mContext = LogisticsConfirmActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mNullTv = (TextView) findViewById(R.id.logi_confirm_null_tv);
		mOrderLv = (ListView) findViewById(R.id.logi_confirm_list_lv);
		mOrderAdapter = new OrderWineAdapter(mContext, mOrderMsgMap, this);
		mOrderLv.setAdapter(mOrderAdapter);
	}

	private void initData() {
		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		mNullTv.setVisibility(View.VISIBLE);
		OrderLogic.getGrabOrdersHistory(mContext, mHandler, UserInfoManager.userInfo.getUserId(),
				OrderState.ORDER_STATUS_GRABBED + "," + OrderState.ORDER_STATUS_DELIVERY, "0", "30");
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onClick(View item, View widget, int position, int which, String code) {
		switch (which) {
		case R.id.list_order_received_auth_btn: {
			OrderLogic.recieveConfirm(mContext, mHandler,
					((ArrayList<Order>) mOrderMsgMap.get(MsgResult.ORDER_TAG)).get(position).getId(), code);
			break;
		}
		case R.id.list_order_group_phone_tv: {
			if (!TextUtils.isEmpty(code)) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + code));
				startActivity(intent);
			}
			break;
		}
		case R.id.list_order_group_assistor_phone_tv: {
			if (!TextUtils.isEmpty(code)) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + code));
				startActivity(intent);
			}
			break;
		}
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			HomeActivity.setTab(HomeActivity.TAB_MAIN);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
