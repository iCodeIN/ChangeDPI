/*
 * @author      rx1310 <rx1310@inbox.ru>
 * @copyright   Copyright (c) o1310, 2020
 * @license     MIT License
 */

package o1310.rx1310.app.cdpi;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import o1310.rx1310.app.cdpi.MainActivity;

public class MainActivity extends Activity {

	Button applyButton;
	EditText inputField;
	TextView appInfoText, currentDPI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.activity_main);
		setContentView(R.layout.activity_main);
		
		inputField = findViewById(R.id.etInputNum);
		
		appInfoText = findViewById(R.id.tvAppInfo);
		appInfoText.setText(String.format(getString(R.string.desc_about), appVersion(this))); // Отображение версии приложения на экране
		appInfoText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "made with " + ("♥️") + " by rx1310 (from o1310)", Toast.LENGTH_LONG).show(); // Маленькая пасхалка
			}
		});
		
		currentDPI = findViewById(R.id.currentDPI);
		currentDPI.setText(String.format(getString(R.string.current_dpi), getCurrentDPI()));
		//currentDPI.setText(CmdExec.execute(true, "getprop ro.sf.lcd_density").getResult());
		
		applyButton = findViewById(R.id.btnApply);
		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String runCmd = CmdExec.execute(true, "wm density " + inputField.getText().toString()).getResult(); // выполнение команды
				applyButton.setText(runCmd); // отображение текущего значения DPI в кнопке
			}
		});
		
	}
	
	/* Вычисление версии APK.
	 * Возвращается строка в формате VERSION_NAME.VERSION_CODE (напр.: 1.191231) */
	public static String appVersion(Context c) {

		String s, a;
		int v;
		
		PackageManager m = c.getPackageManager();

		try {
			PackageInfo i = m.getPackageInfo(c.getPackageName(), 0);
			s = i.versionName;
			v = i.versionCode;
			a = s + "." + v;
		} catch(PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			a = "error(String.appVersion)";
		}

		return a;

	}
	
	/* Вычисление текущего значения DPI
	 * На экран выводится значение строки ro.sf.lcd_density из файла system/build.prop
	 * Взято из: https://github.com/rx1310/android_packages_apps_ParanoidOTA/blob/marshmallow/src/com/paranoid/paranoidota/Utils.java */
	String getCurrentDPI() {
		
		try {
			
			Process p = Runtime.getRuntime().exec("getprop ro.sf.lcd_density");
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder s = new StringBuilder();
			String l;
			
			while((l = b.readLine()) != null) {
				
				s.append(l);
				
			}
			
			return s.toString();
			
		} catch(IOException e) {
			
			// Runtime error
			
		}
		
		return null;
		
	}
	
}
