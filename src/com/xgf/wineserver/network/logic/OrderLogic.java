package com.xgf.wineserver.network.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import com.xgf.wineserver.entity.Goods;
import com.xgf.wineserver.entity.Order;
import com.xgf.wineserver.network.config.MsgResult;
import com.xgf.wineserver.network.config.RequestUrl;
import com.xgf.wineserver.utils.JsonUtils;
import com.xgf.wineserver.utils.UserInfoManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class OrderLogic {

	public static final int NET_ERROR = 0;

	public static final int ORDER_GRAB_LIST_SUC = NET_ERROR + 1;

	public static final int ORDER_GRAB_LIST_FAIL = ORDER_GRAB_LIST_SUC + 1;

	public static final int ORDER_GRAB_LIST_EXCEPTION = ORDER_GRAB_LIST_FAIL + 1;

	public static final int ORDER_GRAB_SUC = ORDER_GRAB_LIST_EXCEPTION + 1;

	public static final int ORDER_GRAB_FAIL = ORDER_GRAB_SUC + 1;

	public static final int ORDER_GRAB_EXCEPTION = ORDER_GRAB_FAIL + 1;

	public static final int ORDERLIST_HISTORY_GET_SUC = ORDER_GRAB_EXCEPTION + 1;

	public static final int ORDERLIST_HISTORY_GET_FAIL = ORDERLIST_HISTORY_GET_SUC + 1;

	public static final int ORDERLIST_HISTORY_GET_EXCEPTION = ORDERLIST_HISTORY_GET_FAIL + 1;

	public static final int ORDER_CONFIRM_SUC = ORDERLIST_HISTORY_GET_EXCEPTION + 1;

	public static final int ORDER_CONFIRM_FAIL = ORDER_CONFIRM_SUC + 1;

	public static final int ORDER_CONFIRM_EXCEPTION = ORDER_CONFIRM_FAIL + 1;

	public static void getRobOrder(final Context context, final Handler handler, final String userId,
			final String longitude, final String latitude) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE, RequestUrl.order.queryOrderForGrab);
					Log.e("xxx_id_order", UserInfoManager.userInfo.getUserId());

					rpc.addProperty("userId", URLEncoder.encode(userId, "UTF-8"));
					rpc.addProperty("longitude", URLEncoder.encode(longitude, "UTF-8"));
					rpc.addProperty("latitude", URLEncoder.encode(latitude, "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/" + RequestUrl.order.queryOrderForGrab, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					Log.e("xxx_orders_result", resultStr.toString());
					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseGrabOrdersListData(obj, handler);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// {"datas":{"total":0,"list":[]},"message":"操作成功","result":"0"}
	private static void parseGrabOrdersListData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response.getJSONObject(MsgResult.RESULT_DATAS_TAG);

				ArrayList<Order> tempOrderList = new ArrayList<Order>();
				JSONArray orderListArray = jsonObject.getJSONArray(MsgResult.RESULT_LIST_TAG);

				HashMap<String, Object> msgMap = new HashMap<String, Object>();

				String lastestOrderTimestamp = jsonObject.getString("lastestOrderTimestamp");

				int size = orderListArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject orderJsonObject = orderListArray.getJSONObject(i);
					Order order = (Order) JsonUtils.fromJsonToJava(orderJsonObject, Order.class);
					tempOrderList.add(order);

					ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
					JSONArray goodsArray = orderJsonObject.getJSONArray("items");

					for (int j = 0; j < goodsArray.length(); j++) {
						JSONObject goodsJsonObject = goodsArray.getJSONObject(j);
						Goods goods = new Goods();
						goods.setId(goodsJsonObject.getString("productId"));
						goods.setName(goodsJsonObject.getString("productName"));
						goods.setSalesPrice(goodsJsonObject.getString("salePrice"));
						goods.setNum(goodsJsonObject.getString("count"));
						tempGoodsList.add(goods);
					}
					msgMap.put(order.getId(), tempGoodsList);

				}
				msgMap.put(MsgResult.ORDER_TAG, tempOrderList);
				msgMap.put("lastestOrderTimestamp", lastestOrderTimestamp);

				Message message = new Message();
				message.what = ORDER_GRAB_LIST_SUC;
				message.obj = msgMap;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ORDER_GRAB_LIST_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_GRAB_LIST_EXCEPTION);
		}
	}

	public static void grabOrder(final Context context, final Handler handler, final String userId,
			final String orderId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE, RequestUrl.order.grabOrder);

					rpc.addProperty("userId", URLEncoder.encode(userId, "UTF-8"));
					rpc.addProperty("orderId", URLEncoder.encode(orderId, "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/" + RequestUrl.order.grabOrder, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					Log.e("xxx_grabOrder_result", resultStr.toString());
					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseGrabOrderData(obj, handler);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// {"datas":{},"message":"操作成功","result":"0"}
	private static void parseGrabOrderData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(ORDER_GRAB_SUC);
			} else {
				handler.sendEmptyMessage(ORDER_GRAB_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_GRAB_EXCEPTION);
		}
	}

	public static void getGrabOrdersHistory(final Context context, final Handler handler, final String userId,
			final String orderStatus, final String pageNum, final String pageSize) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE, RequestUrl.order.queryOrderOfDelivery);

					rpc.addProperty("userId", URLEncoder.encode(userId, "UTF-8"));
					rpc.addProperty("orderStatus", URLEncoder.encode(orderStatus, "UTF-8"));
					rpc.addProperty("pageNum", URLEncoder.encode(pageNum, "UTF-8"));
					rpc.addProperty("pageSize", URLEncoder.encode(pageSize, "UTF-8"));
					rpc.addProperty("md5", URLEncoder.encode("1111", "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/" + RequestUrl.order.queryOrderOfDelivery, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);
					Log.e("xxx_GrabOrdersHistory_result", resultStr.toString());
					if (!TextUtils.isEmpty(resultStr)) {
						Log.e("xxx_GrabOrdersHistory_result", resultStr.toString());
						JSONObject obj = new JSONObject(resultStr);
						parseGrabOrdersHistoryData(obj, handler);
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// {"datas":{"total":"4","list":[{"phone":"1002","orderTime":"2015-08-06
	// 16:35:26","orderStatus":"1","id":"","amount":"240","deliveryTime":"2030-00-00
	// 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"}]},{"phone":"1002","orderTime":"2015-08-06
	// 16:35:47","orderStatus":"1","id":"","amount":"240","deliveryTime":"2030-00-00
	// 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"}]},{"phone":"1002","orderTime":"2015-08-06
	// 16:44:35","orderStatus":"1","id":"","amount":"628","deliveryTime":"2030-00-00
	// 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"},{"productId":"2222","productName":"梦之蓝9","salePrice":"388","count":"1"}]},{"phone":"1002","orderTime":"2015-08-06
	// 16:44:48","orderStatus":"1","id":"","amount":"628","deliveryTime":"2030-00-00
	// 00:00:00","address":"1234","payStatus":"","memo":"","items":[{"productId":"10002","productName":"海之蓝","salePrice":"120","count":"2"},{"productId":"2222","productName":"梦之蓝9","salePrice":"388","count":"1"}]}]},"message":"操作成功",,"result":"0"}
	private static void parseGrabOrdersHistoryData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response.getJSONObject(MsgResult.RESULT_DATAS_TAG);

				ArrayList<Order> tempOrderList = new ArrayList<Order>();
				JSONArray orderListArray = jsonObject.getJSONArray(MsgResult.RESULT_LIST_TAG);

				HashMap<String, Object> msgMap = new HashMap<String, Object>();

				int size = orderListArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject orderJsonObject = orderListArray.getJSONObject(i);
					Order order = (Order) JsonUtils.fromJsonToJava(orderJsonObject, Order.class);
					tempOrderList.add(order);

					ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
					JSONArray goodsArray = orderJsonObject.getJSONArray("items");

					for (int j = 0; j < goodsArray.length(); j++) {
						JSONObject goodsJsonObject = goodsArray.getJSONObject(j);
						Goods goods = new Goods();
						goods.setId(goodsJsonObject.getString("productId"));
						goods.setName(goodsJsonObject.getString("productName"));
						goods.setIconUrl(goodsJsonObject.getString("iconUrl"));
						goods.setSalesPrice(goodsJsonObject.getString("salePrice"));
						goods.setNum(goodsJsonObject.getString("count"));
						tempGoodsList.add(goods);
					}
					msgMap.put(order.getId(), tempGoodsList);

				}
				msgMap.put(MsgResult.ORDER_TAG, tempOrderList);

				Message message = new Message();
				message.what = ORDERLIST_HISTORY_GET_SUC;
				message.obj = msgMap;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ORDERLIST_HISTORY_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDERLIST_HISTORY_GET_EXCEPTION);
		}
	}

	public static void recieveConfirm(final Context context, final Handler handler, final String orderId,
			final String authCode) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					SoapObject rpc = new SoapObject(RequestUrl.NAMESPACE, RequestUrl.order.recieveConfirm);

					rpc.addProperty("orderId", URLEncoder.encode(orderId, "UTF-8"));
					rpc.addProperty("authCode", URLEncoder.encode(authCode, "UTF-8"));

					AndroidHttpTransport ht = new AndroidHttpTransport(RequestUrl.HOST_URL);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);

					ht.call(RequestUrl.NAMESPACE + "/" + RequestUrl.order.recieveConfirm, envelope);

					SoapObject so = (SoapObject) envelope.bodyIn;

					String resultStr = (String) so.getProperty(0);

					Log.e("xxx_recieveConfirm_result", resultStr.toString());
					if (!TextUtils.isEmpty(resultStr)) {
						JSONObject obj = new JSONObject(resultStr);
						parseRecieveConfirmData(obj, handler);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// {"datas":{},"message":"操作成功","result":"0"}
	private static void parseRecieveConfirmData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(ORDER_CONFIRM_SUC);
			} else {
				handler.sendEmptyMessage(ORDER_CONFIRM_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_CONFIRM_EXCEPTION);
		}
	}

}
