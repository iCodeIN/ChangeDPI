/*
 * @author      rx1310 <rx1310@inbox.ru>
 * @copyright   Copyright (c) o1310, 2020
 * @license     MIT License
 */

package o1310.rx1310.app.cdpi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import o1310.rx1310.app.cdpi.R;
import android.text.TextUtils;

public class MainActivity extends Activity {

	Button applyButton;
	EditText inputField;
	TextView appInfoText, defaultDPI;
	
	String cmdRebootSystem = "su -c svc power reboot";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.activity_main);
		setContentView(R.layout.activity_main);
		
		inputField = findViewById(R.id.etInputNum);
		//inputField.setText(getDefaultDPI());
		
		appInfoText = findViewById(R.id.tvAppInfo);
		appInfoText.setText(String.format(getString(R.string.desc_about), appVersion(this))); // Отображение версии приложения на экране
		appInfoText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aboutDialog();
				Toast.makeText(MainActivity.this, "made with " + ("♥️") + " by rx1310 (from o1310)", Toast.LENGTH_LONG).show(); // Маленькая пасхалка
			}
		});
		
		defaultDPI = findViewById(R.id.defaultDPI);
		defaultDPI.setText(String.format(getString(R.string.default_dpi), getDefaultDPI()));
		//currentDPI.setText(CmdExec.execute(true, "getprop ro.sf.lcd_density").getResult());
		
		applyButton = findViewById(R.id.btnApply);
		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String runCmd = CmdExec.execute(true, "wm density " + inputField.getText().toString()).getResult(); // выполнение команды
				applyButton.setText(runCmd); // отображение текущего значения DPI в кнопке
				
				if (!TextUtils.isEmpty(inputField.getText().toString())) {
					rebootDialog();
				}
				
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
	
	/* Вычисление стандартного значения DPI
	 * На экран выводится значение строки ro.sf.lcd_density из файла system/build.prop
	 * Взято из: https://github.com/rx1310/android_packages_apps_ParanoidOTA/blob/marshmallow/src/com/paranoid/paranoidota/Utils.java */
	String getDefaultDPI() {
		
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
	
	// О программе
	void aboutDialog() {

		// создаем диалог
		AlertDialog.Builder b = new AlertDialog.Builder(this);

		b.setTitle(R.string.about_dialog_title);
		//b.setIcon(R.drawable.ic_logo);
		b.setMessage(R.string.about_message);
		b.setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // обработка нажатия кнопки "Да"
				public void onClick(DialogInterface d, int i) {
					d.cancel();
				}
			});
		b.setNegativeButton("Telegram", new DialogInterface.OnClickListener() { // обработка нажатия кнопки "Telegram"
				public void onClick(DialogInterface d, int i) {
					startActivity(new Intent (Intent.ACTION_VIEW, Uri.parse("https://t.me/o1310")));
				}
			});
		b.setNeutralButton(R.string.about_dialog_action_source_code, new DialogInterface.OnClickListener() { // обработка нажатия кнопки "Да"
				public void onClick(DialogInterface d, int i) {
					startActivity(new Intent (Intent.ACTION_VIEW, Uri.parse("https://github.com/o1310/ChangeDPI")));
				}
			});

		AlertDialog a = b.create(); // создаем диалог

		a.show(); // отображаем диалог

	}
	
	void rebootDialog() {
		
		AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);

		b.setTitle(R.string.reboot_dialog_title);
		b.setIcon(R.mipmap.ic_launcher_round);
		b.setMessage(R.string.reboot_dialog_message);
		b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() { // обработка нажатия кнопки "Да"
			public void onClick(DialogInterface d, int i) {
				CmdExec.execute(true, cmdRebootSystem).getResult(); // выполнение команды
			}
		});
		b.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() { // обработка нажатия кнопки "Да"
			public void onClick(DialogInterface d, int i) {
				d.cancel();
			}
		});

		b.show();
		
	}
	
}
