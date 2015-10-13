package com.xgf.wineserver.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.xgf.wineserver.R;
import com.xgf.wineserver.config.Constants;
import com.xgf.wineserver.entity.Goods;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.entity.OrderState;
import com.xgf.wineserver.network.config.MsgResult;
import com.xgf.wineserver.ui.utils.ListItemClickParameterHelp;
import com.xgf.wineserver.ui.view.OrderWineView;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderWineAdapter extends BaseAdapter {

	private Context mContext;

	private HashMap<String, Object> mMap;

	private HashMap<String, String> etMap = new HashMap<String, String>();

	private LayoutInflater mInflater;

	private ListItemClickParameterHelp mCallback;

	public OrderWineAdapter(Context context, HashMap<String, Object> datas,
			ListItemClickParameterHelp callback) {
		this.mContext = context;
		this.mMap = datas;
		this.mCallback = callback;
		mInflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		if (((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)) != null) {
			return ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).size();
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
			convertView = mInflater
					.inflate(R.layout.list_order_wine_item, null);

			holder = new ViewHolder();
			holder.mId = (TextView) convertView
					.findViewById(R.id.list_order_group_id_tv);
			holder.mTime = (TextView) convertView
					.findViewById(R.id.list_order_group_time_tv);
			holder.mState = (TextView) convertView
					.findViewById(R.id.list_order_group_state_tv);
			holder.mPayType = (TextView) convertView
					.findViewById(R.id.list_order_group_pay_type_tv);
			holder.mTotalMoney = (TextView) convertView
					.findViewById(R.id.list_order_group_total_money_tv);
			
			holder.mAddress = (TextView) convertView
					.findViewById(R.id.list_order_group_address_tv);

			holder.mPhone = (TextView) convertView
					.findViewById(R.id.list_order_group_phone_tv);
			holder.mAssistorPhone = (TextView) convertView
					.findViewById(R.id.list_order_group_assistor_phone_tv);
			
			holder.mInvoice = (TextView) convertView
					.findViewById(R.id.list_order_group_invoice_tv);

			holder.mCodeEt = (EditText) convertView
					.findViewById(R.id.list_order_confirm_code_et);
			holder.mConfirmBtn = (Button) convertView
					.findViewById(R.id.list_order_received_auth_btn);
			holder.mWineLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_wine_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final int tempPosition = position;
		final String id = ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
				.get(position).getId();
		holder.mCodeEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// 将editText中改变的值设置的HashMap中
				etMap.put(id, s.toString());
			}
		});

		holder.mId.setText(id);
		String time = ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(
				position).getDeliveryTime();
		if (time.length() > 18) {
			time = time.substring(5, 16);
		}
		holder.mTime.setText(time);
		holder.mAddress.setText(((ArrayList<Order>) mMap
				.get(MsgResult.ORDER_TAG)).get(position).getAddress());
		holder.mTotalMoney.setText("￥"+((ArrayList<Order>) mMap
				.get(MsgResult.ORDER_TAG)).get(position).getAmount());

		final String phone = ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
				.get(position).getPhone();
		holder.mPhone.setText(phone);
		
		final String assistorPhone = ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
				.get(position).getAssistorPhone();
		holder.mAssistorPhone.setText(assistorPhone);
		
		String invoice = ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
				.get(position).getInvoice();
		if (!TextUtils.isEmpty(invoice) && "true".equals(invoice)) {
			holder.mInvoice.setText("需要发票");
		} else if (!TextUtils.isEmpty(invoice) && "false".equals(invoice)) {
			holder.mInvoice.setText("不需发票");
		}

		String payType = "";
		String payWay = ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(
				position).getPayWay();
		if (!TextUtils.isEmpty(payWay)) {
			if (Constants.PAY_WAY_ALIPAY.equals(payWay)) {
				payType = "支付宝支付";
			} else if (Constants.PAY_WAY_WXPAY.equals(payWay)) {
				payType = "微信支付";
			} else if (Constants.PAY_WAY_UNIONPAY.equals(payWay)) {
				payType = "银联支付";
			} else if (Constants.PAY_WAY_POSPAY.equals(payWay)) {
				payType = "POS机支付";
			} else if (Constants.PAY_WAY_CASHPAY.equals(payWay)) {
				payType = "现金支付";
			}
		}
		holder.mPayType.setText(payType);
		holder.mState.setText(OrderState.state[(Integer
				.parseInt(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
						.get(position).getOrderStatus()) - 1)]);

		final View view = convertView;
		final int whichCancel = holder.mConfirmBtn.getId();
		holder.mConfirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(etMap.get(id))) {
					mCallback.onClick(view, v, tempPosition, whichCancel,
							etMap.get(id));
				} else {
					Toast.makeText(mContext,
							mContext.getString(R.string.logistics_code_hint),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		final int whichTel = holder.mPhone.getId();
		holder.mPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(phone)) {
					mCallback.onClick(view, v, tempPosition, whichTel, phone);
				}
			}
		});
		final int whichAssistorTel = holder.mAssistorPhone.getId();
		holder.mAssistorPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(assistorPhone)) {
					mCallback.onClick(view, v, tempPosition, whichAssistorTel, assistorPhone);
				}
			}
		});

		holder.mWineLl.removeAllViews();

		ArrayList<Goods> goodsList = new ArrayList<Goods>();
		goodsList.addAll(((ArrayList<Goods>) mMap.get(((ArrayList<Order>) mMap
				.get(MsgResult.ORDER_TAG)).get(position).getId())));
		for (int i = 0; i < goodsList.size(); i++) {
			// TODO
			Goods goods = goodsList.get(i);
			OrderWineView orderWineView = new OrderWineView(mContext, goods);
			holder.mWineLl.addView(orderWineView);
		}
		return convertView;
	}

	static class ViewHolder {

		public TextView mState;

		public TextView mTime;

		public TextView mId;

		public TextView mPayType;
		
		public TextView mTotalMoney;

		public TextView mAddress;

		public TextView mInvoice;

		public TextView mPhone;
		
		public TextView mAssistorPhone;

		public Button mConfirmBtn;

		public EditText mCodeEt;

		public LinearLayout mWineLl;

	}

}
