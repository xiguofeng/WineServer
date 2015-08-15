package com.xgf.wineserver.ui.adapter;

import java.util.ArrayList;

import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.ui.utils.ListItemClickHelp;
import com.xgf.wineserver.utils.TimeUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Order> mDatas;

	private LayoutInflater mInflater;

	private ListItemClickHelp mCallback;

	public OrderAdapter(Context context, ArrayList<Order> datas, ListItemClickHelp callback) {
		this.mContext = context;
		this.mDatas = datas;
		this.mCallback = callback;
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
			convertView = mInflater.inflate(R.layout.list_order_item, null);

			holder = new ViewHolder();
			holder.mDistanceTv = (TextView) convertView.findViewById(R.id.list_order_distance_tv);
			holder.mTimeTv = (TextView) convertView.findViewById(R.id.list_order_time_tv);
			holder.mInfoTv = (TextView) convertView.findViewById(R.id.list_order_info_tv);
			holder.mWaitTimeTv = (TextView) convertView.findViewById(R.id.list_order_wait_time_tv);
			holder.mAddressTv = (TextView) convertView.findViewById(R.id.list_order_address_tv);

			holder.mRobBtn = (Button) convertView.findViewById(R.id.list_order_rob_btn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mDatas.get(position).getDistance().length() > 3) {
			double distance = Double.parseDouble(mDatas.get(position).getDistance());
			distance = distance / 1000;

			holder.mDistanceTv.setText(String.valueOf(distance).substring(0, 3) + "km");
		} else {
			holder.mDistanceTv.setText(mDatas.get(position).getDistance() + "km");
		}
		holder.mTimeTv.setText(mDatas.get(position).getOrderTime());
		holder.mInfoTv.setText(mDatas.get(position).getAmount() + "元");
		holder.mWaitTimeTv.setText(mDatas.get(position).getDeliveryTime());
		holder.mAddressTv.setText(mDatas.get(position).getAddress());

		long orderTime = TimeUtils.dateToLong(mDatas.get(position).getOrderTime(), TimeUtils.FORMAT_PATTERN_DATE);
		long deliveryTime = TimeUtils.dateToLong(mDatas.get(position).getDeliveryTime(), TimeUtils.FORMAT_PATTERN_DATE);
		String waitTime = String.valueOf((deliveryTime - orderTime) / 60);
		holder.mWaitTimeTv.setText(waitTime + "分钟");

		String orderTimeStr = new String(mDatas.get(position).getOrderTime());
		if (!TextUtils.isEmpty(orderTimeStr)) {
			if (orderTimeStr.length() > 11) {
				String dateYMD = TimeUtils.getTodayCommonPattern().substring(0, 10);
				String dateHMM = orderTimeStr.substring(11);
				String orderYMD = orderTimeStr.substring(0, 10);
				if (dateYMD.equals(orderYMD)) {
					holder.mTimeTv.setText("今天" + dateHMM);
				} else {
					String orderMDHMM = orderTimeStr.substring(5);
					holder.mTimeTv.setText(orderMDHMM);
				}
			}
		}

		final View view = convertView;
		final int p = position;
		final int which = holder.mRobBtn.getId();
		holder.mRobBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onClick(view, v, p, which);
			}
		});

		return convertView;
	}

	static class ViewHolder {

		public TextView mDistanceTv;

		public TextView mTimeTv;

		public TextView mInfoTv;

		public TextView mWaitTimeTv;

		public TextView mAddressTv;

		private Button mRobBtn;

	}

}
