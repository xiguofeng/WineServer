package com.xgf.wineserver.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.network.config.MsgResult;
import com.xgf.wineserver.network.logic.OrderLogic;
import com.xgf.wineserver.ui.adapter.OrderHistoryAdapter;
import com.xgf.wineserver.ui.view.CustomListView;
import com.xgf.wineserver.ui.view.CustomProgressDialog2;
import com.xgf.wineserver.utils.TimeUtils;
import com.xgf.wineserver.utils.UserInfoManager;

public class GrabOrderHistoryActivity extends Activity implements
		OnClickListener {

	private Context mContext;

	private CustomListView mOrderTodayLv;

	private ListView mOrderHistoryLv;

	private TextView mTotalNumTv;

	private TextView mTotalIncomeTv;

	private TextView mTodayNumTv;

	private TextView mHistoryNumTv;

	private ArrayList<Order> mOrderList = new ArrayList<Order>();

	private ArrayList<Order> mOrderTodayList = new ArrayList<Order>();

	private ArrayList<Order> mOrderHistoryList = new ArrayList<Order>();

	private OrderHistoryAdapter mOrderTodayAdapter;

	private OrderHistoryAdapter mOrderHistoryAdapter;

	private HashMap<String, Object> mOrderMsgMap = new HashMap<String, Object>();
	
	private CustomProgressDialog2 mCustomProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDERLIST_HISTORY_GET_SUC: {
				if (null != msg.obj) {
					mOrderMsgMap.clear();
					mOrderMsgMap
							.putAll((Map<? extends String, ? extends Object>) msg.obj);

					mOrderList.clear();
					mOrderList.addAll((ArrayList<Order>) mOrderMsgMap
							.get(MsgResult.ORDER_TAG));
					setData();
				}
				break;
			}
			case OrderLogic.ORDERLIST_HISTORY_GET_FAIL: {

				break;
			}
			case OrderLogic.ORDERLIST_HISTORY_GET_EXCEPTION: {
				break;
			}
			case OrderLogic.NET_ERROR: {
				break;
			}
			default:
				break;
			}
			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grab_order_history);
		mContext = GrabOrderHistoryActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
		initView();
		// initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mContext = GrabOrderHistoryActivity.this;
		mTotalNumTv = (TextView) findViewById(R.id.garb_history_total_num_tv);
		mTotalIncomeTv = (TextView) findViewById(R.id.garb_history_total_income_tv);
		mTodayNumTv = (TextView) findViewById(R.id.garb_history_today_num_tv);
		mHistoryNumTv = (TextView) findViewById(R.id.garb_history_history_num_tv);

		mOrderTodayLv = (CustomListView) findViewById(R.id.grab_order_history_list_today_lv);
		mOrderTodayAdapter = new OrderHistoryAdapter(mContext, mOrderTodayList);
		mOrderTodayLv.setAdapter(mOrderTodayAdapter);

		mOrderHistoryLv = (ListView) findViewById(R.id.grab_order_history_list_lv);
		mOrderHistoryAdapter = new OrderHistoryAdapter(mContext,
				mOrderHistoryList);
		mOrderHistoryLv.setAdapter(mOrderHistoryAdapter);

	}

	private void initData() {
		mOrderList.clear();
		// for (int i = 0; i < 10; i++) {
		// Order order = new Order();
		// order.setMemo("1.5km");
		// order.setDeliveryTime("30分钟");
		// order.setAddress("南京市江宁区" + i);
		// order.setOrderTime("今天15:00");
		// order.setAmount("洋河酒");
		// mOrderList.add(order);
		// }
		if (null != mCustomProgressDialog) {
			mCustomProgressDialog.show();
		}
		OrderLogic.getGrabOrdersHistory(mContext, mHandler,
				UserInfoManager.userInfo.getUserId(), "", "0", "30");
	}

	private void setData() {
		mTotalNumTv.setText("" + mOrderList.size());

		mOrderTodayList.clear();
		mOrderHistoryList.clear();
		double price = 0;
		for (Order order : mOrderList) {
			price = price + Double.parseDouble(order.getAmount());

			String orderTimeStr = new String(order.getDeliveryTime());
			if (!TextUtils.isEmpty(orderTimeStr) && orderTimeStr.length() > 10) {
				String dateYMD = TimeUtils.getTodayCommonPattern().substring(0,
						10);
				String orderYMD = orderTimeStr.substring(0, 10);
				if (dateYMD.equals(orderYMD)) {
					mOrderTodayList.add(order);
				} else {
					mOrderHistoryList.add(order);
				}
			} else {
				mOrderHistoryList.add(order);
			}
		}

		if (mOrderTodayList.size() == 0) {
			mOrderTodayLv.setVisibility(View.GONE);
			mTodayNumTv.setText(getString(R.string.today) + "(" + "无" + ")");
		}
		mOrderTodayAdapter.notifyDataSetChanged();
		mOrderHistoryAdapter.notifyDataSetChanged();

		mTotalIncomeTv.setText(String.valueOf(price));

	}

	@Override
	public void onClick(View v) {
	}

}
