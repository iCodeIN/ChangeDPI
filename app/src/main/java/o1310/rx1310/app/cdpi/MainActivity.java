/*
 * @author      rx1310 <rx1310@inbox.ru>
 * @copyright   Copyright (c) o1310, 2020
 * @license     MIT License
 */

package o1310.rx1310.app.cdpi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button applyButton;
	EditText inputEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.activity_main);
		setContentView(R.layout.activity_main);
		
		inputEditText = findViewById(R.id.etInputNum);
		
		applyButton = findViewById(R.id.btnApply);
		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
	}
	
}
