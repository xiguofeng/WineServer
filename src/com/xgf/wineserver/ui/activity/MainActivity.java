package com.xgf.wineserver.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.ui.adapter.OrderAdapter;

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ListView mOrderLv;

	private ArrayList<Order> mOrderList = new ArrayList<Order>();

	private OrderAdapter mOrderAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
		mOrderAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
	}

}
