package gps.lab.kmh.mygps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

public class RadioButtonActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //layoutParams.dimAmount = 0.7f;
        //getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_radio_button);

        Button btnOk = (Button)findViewById(R.id.button_ok);
        btnOk.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TextOut", getResultString());
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

    public String getResultString(){
        String result;
        RadioButton opStudy = (RadioButton)findViewById(R.id.option1);
        RadioButton opFood = (RadioButton)findViewById(R.id.option2);
        RadioButton opPlay = (RadioButton)findViewById(R.id.option3);
        RadioButton opExer = (RadioButton)findViewById(R.id.option4);
        RadioButton opEtc = (RadioButton)findViewById(R.id.option5);

        if(opStudy.isChecked()){
            result = opStudy.getText().toString();
        }
        else if(opFood.isChecked()) {
            result = opFood.getText().toString();
        }
        else if(opFood.isChecked()) {
            result = opFood.getText().toString();
        }
        else if(opPlay.isChecked()) {
            result = opPlay.getText().toString();
        }
        else if(opExer.isChecked()) {
            result = opExer.getText().toString();
        }
        else if(opEtc.isChecked()) {
            result = opEtc.getText().toString();
        }
        else {
            result = opStudy.getText().toString();
        }
        return result;
    }



}
