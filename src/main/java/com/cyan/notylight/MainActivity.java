package com.cyan.notylight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	/** 用于打开授权的界面 */
	private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
	/** 用于检测是否有授权 */
	private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

	TextView mTextView;
	EditText et;
	Button save;
	Button del;
	Button show;
	CheckBox cb;

	Map<String, String> m = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!isEnabled()) {
			startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
		}
		cb = (CheckBox) findViewById(R.id.vibrator);
		et = (EditText) findViewById(R.id.name);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		del = (Button) findViewById(R.id.del);
		del.setOnClickListener(this);
		show = (Button) findViewById(R.id.query);
		show.setOnClickListener(this);

		mTextView = (TextView) findViewById(R.id.show_all_packages);
		mTextView.setText("正在读取所有应用，请稍候...");

		PackageManager packageManager = getPackageManager();
		List<PackageInfo> list = packageManager
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		StringBuilder stringBuilder = new StringBuilder();
		for (PackageInfo packageInfo : list) {
			stringBuilder.append("package name:" + packageInfo.packageName
					+ "\n");
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			stringBuilder.append("应用名称:"
					+ applicationInfo.loadLabel(packageManager) + "\n");
			m.put(applicationInfo.loadLabel(packageManager).toString(),
					packageInfo.packageName);
			if (packageInfo.permissions != null) {
				for (PermissionInfo p : packageInfo.permissions) {
					stringBuilder.append("权限包括:" + p.name + "\n");
				}
			}
			stringBuilder.append("\n");
		}
		mTextView.setText("读取完毕..请输入应用名");
		// mTextView.setText(stringBuilder.toString());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == save.getId()) {
			ContentValues cv = new ContentValues();
			cv.put(Table.c_name, et.getText().toString().trim());
			cv.put(Table.c_pkg, m.get(et.getText().toString().trim()));
			cv.put(Table.c_statu, cb.isChecked() ? 2 : 1);
			Table.insert(cv, this);
			Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
		} else if (v.getId() == del.getId()) {
			Table.delete(et.getText().toString().trim(), this);
			Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();

		} else if (v.getId() == show.getId()) {
			mTextView.setText(Table.show(this));
		}

	}

	private boolean isEnabled() {
		String pkgName = getPackageName();
		final String flat = Settings.Secure.getString(getContentResolver(),
				ENABLED_NOTIFICATION_LISTENERS);
		if (!TextUtils.isEmpty(flat)) {
			final String[] names = flat.split(":");
			for (int i = 0; i < names.length; i++) {
				final ComponentName cn = ComponentName
						.unflattenFromString(names[i]);
				if (cn != null) {
					if (TextUtils.equals(pkgName, cn.getPackageName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// =========================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
