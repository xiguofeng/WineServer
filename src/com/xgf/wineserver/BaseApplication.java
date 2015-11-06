package com.xgf.wineserver;

import cn.jpush.android.api.JPushInterface;

import com.xgf.wineserver.config.Constants;
import com.xgf.wineserver.utils.image.ImageLoaderConfig;

import android.app.Application;
import android.content.res.Configuration;

public class BaseApplication extends Application {


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
		
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}


}
