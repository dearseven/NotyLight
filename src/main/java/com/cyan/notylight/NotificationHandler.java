package com.cyan.notylight;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("OverrideAbstract")
public class NotificationHandler extends NotificationListenerService {
	TelephonyManager mTelephonyManager = null;

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		if (mTelephonyManager == null) {
			mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		}

		//微信 com.tencent.mm
		//qq  com.tencent.mobileqq
		//weibo com.sina.weibo

		String pn=sbn.getPackageName();
		Log.d("SevenNLS", pn);

		Map<String,Integer>m=new HashMap<String,Integer>();
		m.put("com.tencent.mm",1);
		m.put("com.sina.weibo",1);
		m.put("com.tencent.mobileqq",1);


		// 点亮屏幕
		if (!isScreenOn(this)&&mTelephonyManager.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
			if(m.get(pn.trim())!=null){
			Log.d("SevenNLS", "cyan 准备点亮屏幕");
			wakeUpAndUnlock(this);
			}
		}

		// super.onNotificationPosted(sbn);
	}

	@SuppressWarnings("deprecation")
	public static void wakeUpAndUnlock(Context context) {
		// 不解锁~~~
		// KeyguardManager km = (KeyguardManager) context
		// .getSystemService(Context.KEYGUARD_SERVICE);
		// KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
		// // 解锁
		// kl.disableKeyguard();

		// 点亮屏幕
		// 获取电源管理器对象
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		// 点亮屏幕
		wl.acquire(/* 10000 */);
		// 释放
		wl.release();
	}

	@SuppressLint("NewApi")
	/**
	 * 如果屏幕没有点亮返回false
	 * @param context
	 * @return
	 */
	public boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		if (VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn()) {
			return true;
		}
		return false;
	}

	// @Override
	// public void onNotificationPosted(StatusBarNotification sbn,
	// RankingMap rankingMap) {
	// super.onNotificationPosted(sbn, rankingMap);
	// }

	// 1---------------
	// 屏幕解锁通知
	/***
	 * <receiver android:name="com.home.testscreen.MyReceiver"> <intent-filter>
	 * <action android:name="android.intent.action.USER_PRESENT" />
	 * </intent-filter> </receiver>
	 *
	 */

	// 接受屏幕解锁通知接收者
	/**
	 * package com.home.testscreen;
	 *
	 * import android.content.BroadcastReceiver; import android.content.Context;
	 * import android.content.Intent; import android.widget.Toast;
	 *
	 * public class MyReceiver extends BroadcastReceiver {
	 *
	 * @Override public void onReceive(Context context, Intent intent) { // 解锁
	 *           if (intent != null &&
	 *           Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
	 *           Toast.makeText(context, "屏幕已解锁", Toast.LENGTH_SHORT).show(); }
	 *           }
	 *
	 *           }
	 */

	// 2-------------
	// 主动判断屏幕是否亮着：
	/**
	 * public boolean isScreenOn(Context context) { PowerManager pm =
	 * (PowerManager) context.getSystemService(Context.POWER_SERVICE); if
	 * (pm.isScreenOn()) { return true; } return false; }
	 */

	// 3-------------
	// 判断是否开启了重力感应：
	/**
	 * public boolean screenIsOpenRotate(Context context) { int gravity = 0; try
	 * { gravity = Settings.System.getInt(context.getContentResolver(),
	 * Settings.System.ACCELEROMETER_ROTATION); } catch
	 * (SettingNotFoundException e) { e.printStackTrace(); } if (gravity == 1) {
	 * return true; } return false; }
	 */
}
