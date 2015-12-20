package gps.lab.kmh.mygps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class EditTextActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        setContentView(R.layout.activity_edit_text);



        Button btnOk = (Button)findViewById(R.id.button_ok);
        btnOk.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TextOut", getResultText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button btnCancel = (Button)findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    String getResultText(){
        EditText mEdit = (EditText)findViewById(R.id.editText);
        return mEdit.getText().toString();
    }
}
