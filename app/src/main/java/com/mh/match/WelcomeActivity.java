package com.mh.match;

import java.util.*;
import android.content.Intent;
import android.os.Bundle;

import com.mh.entry.Config;
import com.mh.utils.SPUtils;

public class WelcomeActivity extends BaseActivity {

	private Timer timer;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		final boolean isLogin = (boolean) SPUtils.get(Config.IS_LOGIN, false);
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (isLogin){
					intent=new Intent(WelcomeActivity.this,HomeActivity.class);
				}else {
					intent=new Intent(WelcomeActivity.this,LoginActivity.class);
				}
				startActivity(intent); // 执行
				finish();
			}
		};
		timer.schedule(task, 1000 * 2); // 3秒后
	}



	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		timer.cancel();
		super.onStop();
	}




}
