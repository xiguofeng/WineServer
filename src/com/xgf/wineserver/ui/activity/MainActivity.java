package com.xgf.wineserver.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.network.logic.OrderLogic;
import com.xgf.wineserver.service.GuardService;
import com.xgf.wineserver.service.MsgService;
import com.xgf.wineserver.ui.adapter.OrderAdapter;
import com.xgf.wineserver.utils.LocationUtilsV5;
import com.xgf.wineserver.utils.LocationUtilsV5.LocationCallback;
import com.xgf.wineserver.utils.UserInfoManager;

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ListView mOrderLv;

	private static ArrayList<Order> mOrderList = new ArrayList<Order>();

	private static OrderAdapter mOrderAdapter;

	private String mLat;

	private String mLon;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_GRAB_LIST_SUC: {
				if (null != msg.obj) {
					mOrderList.clear();
					mOrderList.addAll((Collection<? extends Order>) msg.obj);
					mOrderAdapter.notifyDataSetChanged();
				}
				break;
			}
			case OrderLogic.ORDER_GRAB_LIST_FAIL: {

				break;
			}
			case OrderLogic.ORDER_GRAB_LIST_EXCEPTION: {
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
		setContentView(R.layout.main);
		mContext = MainActivity.this;
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mContext = MainActivity.this;
		mOrderLv = (ListView) findViewById(R.id.main_order_list_lv);
		mOrderAdapter = new OrderAdapter(mContext, mOrderList);
		mOrderLv.setAdapter(mOrderAdapter);

	}

	private void initData() {
		mOrderList.clear();
		for (int i = 0; i < 10; i++) {
			Order order = new Order();
			order.setMemo("1.5km");
			order.setDeliveryTime("30分钟");
			order.setAddress("南京市江宁区" + i);
			order.setOrderTime("今天15:00");
			order.setAmount("洋河酒");
			mOrderList.add(order);
		}

		mOrderAdapter.notifyDataSetChanged();
		// getLoc();
		Intent intent = new Intent(getApplicationContext(), MsgService.class);
		getApplicationContext().startService(intent);

	}

	public static void refresh() {
		mOrderList.clear();
		mOrderList.addAll(MsgService.orderList);
		mOrderAdapter.notifyDataSetChanged();
	}

	private void getLoc() {
		LocationUtilsV5.getLocation(getApplicationContext(),
				new LocationCallback() {
					@Override
					public void onGetLocation(BDLocation location) {
						Log.e("xxx_latitude", "" + location.getLatitude());
						Log.e("xxx_longitude", "" + location.getLongitude());

						mLat = String.valueOf(location.getLatitude());
						mLon = String.valueOf(location.getLongitude());
						String addr = location.getAddrStr();
						OrderLogic.getRobOrder(mContext, mHandler,
								UserInfoManager.userInfo.getUserId(), mLon,
								mLat);
					}
				});
	}

	@Override
	public void onClick(View v) {
	}

}
