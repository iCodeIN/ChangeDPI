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
import o1310.rx1310.app.cdpi.MainActivity;

public class MainActivity extends Activity {

	Button applyButton;
	EditText inputField;
	TextView appInfoText;
	
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
	
}
