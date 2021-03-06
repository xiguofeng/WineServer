package com.xgf.wineserver.ui.view;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgf.wineserver.R;
import com.xgf.wineserver.entity.Goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderWineView extends LinearLayout {

	private ImageView mIv;

	private TextView mNameTv;
	private TextView mNumTv;
	private TextView mPriceTv;

	public OrderWineView(Context context, Goods goods) {
		super(context);
		initView(context, goods);
		fillData(context, goods);
	}

	private void initView(final Context context, Goods goods) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.order_wine, null);
		mIv = (ImageView) layout.findViewById(R.id.order_wine_iv);
		mNameTv = (TextView) layout.findViewById(R.id.order_wine_name_tv);
		mPriceTv = (TextView) layout.findViewById(R.id.order_wine_price_tv);
		mNumTv = (TextView) layout.findViewById(R.id.order_wine_num_tv);

		this.addView(layout);
	}

	public void fillData(Context context, Goods goods) {

		ImageLoader.getInstance().displayImage(goods.getIconUrl(), mIv);
		mNameTv.setText(goods.getName());
		mPriceTv.setText(goods.getSalesPrice()+"元");
		mNumTv.setText(goods.getNum()+"瓶");
	}

}
