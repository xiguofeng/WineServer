package com.xgf.wineserver.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.entity.OrderState;
import com.xgf.wineserver.network.config.MsgResult;
import com.xgf.wineserver.network.logic.OrderLogic;
import com.xgf.wineserver.ui.adapter.OrderWineAdapter;
import com.xgf.wineserver.ui.utils.ListItemClickParameterHelp;
import com.xgf.wineserver.utils.UserInfoManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class LogisticsConfirmActivity extends Activity implements OnClickListener, ListItemClickParameterHelp {
	private Context mContext;

	private ListView mOrderLv;
	private HashMap<String, Object> mOrderMsgMap = new HashMap<String, Object>();
	private OrderWineAdapter mOrderAdapter;
	private ArrayList<Order> orderList = new ArrayList<Order>();

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
						OrderState.ORDER_STATUS_CONFIRMED, "0", "30");
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
		setContentView(R.layout.logi_confirm);
		mContext = LogisticsConfirmActivity.this;
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mOrderLv = (ListView) findViewById(R.id.logi_confirm_list_lv);
		mOrderAdapter = new OrderWineAdapter(mContext, mOrderMsgMap, this);
		mOrderLv.setAdapter(mOrderAdapter);
	}

	private void initData() {
		OrderLogic.getGrabOrdersHistory(mContext, mHandler, UserInfoManager.userInfo.getUserId(),
				OrderState.ORDER_STATUS_CONFIRMED, "0", "30");
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
		default:
			break;
		}

	}

}
