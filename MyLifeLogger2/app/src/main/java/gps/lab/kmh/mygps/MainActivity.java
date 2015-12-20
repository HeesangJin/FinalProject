package gps.lab.kmh.mygps;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends FragmentActivity {

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "logList.db"; // name of Database;
    String tableName = "logListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    TextView logView;


    String logData="";
    String logDay="";
    String logTime_hour="";
    String logTime_min="";
    String logWork="";
    String logWhat="";
    String logTime="";
    String x="";
    String y="";

    static final LatLng SEOUL = new LatLng( 37.56, 126.97);
    private GoogleMap map;

    int isOnFind=1;
    int allOn=0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Main", "onCreate");

        db = openOrCreateDatabase(dbName, dbMode, null);

        //removeTable();
        createTable();

        logView = (TextView) findViewById(R.id.log);
        logView.setText("현재 위치를 찾는 중입니다");

        Button btnReLocate = (Button) findViewById(R.id.button_reLocate);
        Button btnWorkStyle = (Button) findViewById(R.id.button_workStyle);
        Button btnWhatHappen = (Button) findViewById(R.id.button_WahtHappen);
        Button btnTime = (Button) findViewById(R.id.button_time);
        Button btnInsert = (Button) findViewById(R.id.button_insert);
        Button btnLog = (Button) findViewById(R.id.button_log);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //Marker seoul = map.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        buttonTurnOff();
        findLocate();

        btnReLocate.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                buttonTurnOff();
                findLocate();
                setOnFind();
                logView.setText("현재 위치를 찾는 중입니다");
            }
        });
        btnWorkStyle.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RadioButtonActivity.class);
                //intent.putExtra("TextIn", mText.getText().toString()); //인텐트에 데이터 심어 보내기
                startActivityForResult(intent, 0);
            }
        });
        btnWhatHappen.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditTextActivity.class);
                //intent.putExtra("TextIn", mText.getText().toString()); //인텐트에 데이터 심어 보내기
                startActivityForResult(intent, 1);
            }
        });
        btnTime.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                timeOn();
            }
        });

        btnInsert.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(allOn>=3){
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat CurDateFormat = new SimpleDateFormat("MMdd");
                    //String strCurDate2 = CurDateFormat.format(date);
                    //logDay +=strCurDate2;
                    logTime = logTime_hour+logTime_min;

                    CurDateFormat = new SimpleDateFormat("MM월dd일");
                    String strCurDate = CurDateFormat.format(date);
                    logDay = strCurDate;
                    logTime = logTime_hour+":"+logTime_min;
                    logData = strCurDate + " "+ logTime_hour +":"+ logTime_min +" \n[" + logWork+"]" + logWhat;

                    Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                    intent.putExtra("TextIn", logData); //인텐트에 데이터 심어 보내기
                    startActivityForResult(intent, 2);

                }
                else{
                    Toast.makeText(getApplicationContext(), "양식을 모두 입력해야 합니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLog.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(day text not null, time text not null, " +
                    "work text not null, what text not null, x text not null, y text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    public void insertData() {
            String sql = "insert into " + tableName + " values('"+logDay+ "','"+logTime+"'," +
                    "'"+logWork+"','"+logWhat+"','"+x+"','"+y+"');";
            db.execSQL(sql);
    }

    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
        finish();
    }

    public void timeOn(){
        TimePickerDialog dialog = new TimePickerDialog(this, listener, 00, 00, false);
        dialog.show();
    }

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
// 설정버튼 눌렀을 때
            //Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            TextView mText = (TextView)findViewById(R.id.textTime);
            mText.setText(hourOfDay + "시 " + minute + "분");
            logTime_hour = ""+hourOfDay;
            logTime_min = ""+minute;
            allOn++;
        }
    };

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    TextView mText = (TextView)findViewById(R.id.textWorkStyle);
                    mText.setText(data.getStringExtra("TextOut"));
                    logWork =data.getStringExtra("TextOut");
                    allOn++;
                }
                break;
            case 1:
                if(resultCode == RESULT_OK) {
                    TextView mText = (TextView)findViewById(R.id.textWhatHappen);
                    logWhat = data.getStringExtra("TextOut");
                    mText.setText(logWhat);
                    allOn++;
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    insertData();
                    Toast.makeText(getApplicationContext(), "일정이 추가되었습니다", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




    public void findLocate(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(isOnFind == 1) {
                    buttonTurnOn();
                    setOffFind();
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    LatLng CURRENT_LOCATION = new LatLng(lat, lng);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 16));
                    Marker seoul = map.addMarker(new MarkerOptions().position(CURRENT_LOCATION).title("CURRNT_LOCATION"));

                    logView.setText("(" + lat + " , " + lng + ")");
                    x = ""+lat;
                    y = ""+lng;
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                logView.setText("onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                logView.setText("현재 위치를 찾는 중입니다");
            }

            public void onProviderDisabled(String provider) {
                logView.setText("GPS기능을 켜주세요");
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }


    public void buttonTurnOn(){
        Button btnReLocate = (Button)findViewById(R.id.button_reLocate);
        Button btnWorkStyle = (Button)findViewById(R.id.button_workStyle);
        Button btnWhatHappen = (Button)findViewById(R.id.button_WahtHappen);
        Button btnTime = (Button) findViewById(R.id.button_time);
        Button btnInsert = (Button) findViewById(R.id.button_insert);
        Button btnLog = (Button) findViewById(R.id.button_log);
        btnReLocate.setVisibility(View.VISIBLE);
        btnWorkStyle.setVisibility(View.VISIBLE);
        btnWhatHappen.setVisibility(View.VISIBLE);
        btnTime.setVisibility(View.VISIBLE);
        btnInsert.setVisibility(View.VISIBLE);
        btnLog.setVisibility(View.VISIBLE);
    }

    public void buttonTurnOff(){
        Button btnReLocate = (Button)findViewById(R.id.button_reLocate);
        Button btnWorkStyle = (Button)findViewById(R.id.button_workStyle);
        Button btnWhatHappen = (Button)findViewById(R.id.button_WahtHappen);
        Button btnTime = (Button) findViewById(R.id.button_time);
        Button btnInsert = (Button) findViewById(R.id.button_insert);
        Button btnLog = (Button) findViewById(R.id.button_log);
        btnReLocate.setVisibility(View.INVISIBLE);
        btnWorkStyle.setVisibility(View.INVISIBLE);
        btnWhatHappen.setVisibility(View.INVISIBLE);
        btnTime.setVisibility(View.INVISIBLE);
        btnInsert.setVisibility(View.INVISIBLE);
        btnLog.setVisibility(View.INVISIBLE);
    }
    public void setOnFind(){
        isOnFind = 1;
    }
    public void setOffFind(){
        isOnFind = 0;
    }


}

