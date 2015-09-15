package com.xgf.wineserver.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.baidu.location.BDLocation;
import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.network.config.MsgResult;
import com.xgf.wineserver.network.logic.OrderLogic;
import com.xgf.wineserver.service.MsgService;
import com.xgf.wineserver.ui.adapter.OrderAdapter;
import com.xgf.wineserver.ui.utils.ListItemClickHelp;
import com.xgf.wineserver.ui.view.CustomProgressDialog2;
import com.xgf.wineserver.utils.LocationUtilsV5;
import com.xgf.wineserver.utils.LocationUtilsV5.LocationCallback;
import com.xgf.wineserver.utils.UserInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		ListItemClickHelp {

	private Context mContext;

	private ListView mOrderLv;

	private static ArrayList<Order> mOrderList = new ArrayList<Order>();

	public static OrderAdapter mOrderAdapter;

	private String mLat;

	private String mLon;

	private CustomProgressDialog2 mCustomProgressDialog;

	private long exitTime = 0;

	public static TextView mNullTv;

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

					mNullTv.setVisibility(View.VISIBLE);
					if (mOrderList.size() > 0) {
						mNullTv.setVisibility(View.GONE);
					}
				}
				break;
			}
			case OrderLogic.ORDER_GRAB_LIST_FAIL: {

				break;
			}
			case OrderLogic.ORDER_GRAB_LIST_EXCEPTION: {
				break;
			}

			case OrderLogic.ORDER_GRAB_SUC: {
				Toast.makeText(mContext,
						mContext.getString(R.string.grab_order_suc),
						Toast.LENGTH_SHORT).show();

				break;
			}
			case OrderLogic.ORDER_GRAB_FAIL: {
				Toast.makeText(mContext,
						mContext.getString(R.string.grab_order_fail),
						Toast.LENGTH_SHORT).show();
				break;
			}
			case OrderLogic.ORDER_GRAB_EXCEPTION: {
				break;
			}
			case OrderLogic.NET_ERROR: {
				break;
			}
			default:
				break;
			}
			if (null != mCustomProgressDialog
					&& mCustomProgressDialog.isShowing()) {
				mCustomProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = MainActivity.this;
		mCustomProgressDialog = new CustomProgressDialog2(mContext);
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
		mNullTv = (TextView) findViewById(R.id.main_order_null_tv);
		mOrderLv = (ListView) findViewById(R.id.main_order_list_lv);
		mOrderAdapter = new OrderAdapter(mContext, mOrderList, this);
		mOrderLv.setAdapter(mOrderAdapter);
		mNullTv.setVisibility(View.VISIBLE);
		// Log.e("xxx_id_main", UserInfoManager.userInfo.getUserId());

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

		mOrderAdapter.notifyDataSetChanged();
		// getLoc();
		Intent intent = new Intent(getApplicationContext(), MsgService.class);
		getApplicationContext().startService(intent);

	}

	public static void refresh() {
		mOrderList.clear();
		mOrderList.addAll(MsgService.sOrderList);
		mOrderAdapter.notifyDataSetChanged();

		mNullTv.setVisibility(View.VISIBLE);
		if (mOrderList.size() > 0) {
			mNullTv.setVisibility(View.GONE);
		}
	}

	private void getLoc() {
		LocationUtilsV5.getLocation(getApplicationContext(),
				new LocationCallback() {
					@Override
					public void onGetLocation(BDLocation location) {
						// Log.e("xxx_latitude", "" + location.getLatitude());
						// Log.e("xxx_longitude", "" + location.getLongitude());

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), R.string.exit,
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		switch (which) {
		case R.id.list_order_rob_btn: {
			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.show();
			}
			OrderLogic.grabOrder(mContext, mHandler,
					UserInfoManager.userInfo.getUserId(),
					mOrderList.get(position).getId());
			break;
		}
		default:
			break;
		}

	}

}
