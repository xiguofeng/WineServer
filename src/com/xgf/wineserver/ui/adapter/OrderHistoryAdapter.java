package com.xgf.wineserver.ui.adapter;

import java.util.ArrayList;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderHistoryAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Order> mDatas;

	private LayoutInflater mInflater;

	public OrderHistoryAdapter(Context context, ArrayList<Order> datas) {
		this.mContext = context;
		this.mDatas = datas;

		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_order_history_item,
					null);

			holder = new ViewHolder();
			holder.mTimeTv = (TextView) convertView
					.findViewById(R.id.list_order_history_time_tv);
			holder.mInfoTv = (TextView) convertView
					.findViewById(R.id.list_order_history_price_tv);
			holder.mAddressTv = (TextView) convertView
					.findViewById(R.id.list_order_history_address_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mTimeTv.setText(mDatas.get(position).getDeliveryTime());
		holder.mInfoTv.setText(mDatas.get(position).getAmount()+"元");
		holder.mAddressTv.setText(mDatas.get(position).getAddress());

		return convertView;
	}

	static class ViewHolder {

		public TextView mTimeTv;

		public TextView mInfoTv;

		public TextView mAddressTv;

	}

}
