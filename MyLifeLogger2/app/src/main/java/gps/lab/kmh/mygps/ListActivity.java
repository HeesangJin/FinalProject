package gps.lab.kmh.mygps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "logList.db"; // name of Database;
    String tableName = "logListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;

    ListView mList;

    String selectedCard="null";
    int chooseCard=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);
        // 테이블 생성
        createTable();



        ListView mList = (ListView) findViewById(R.id.list_view);

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);

        Button btnBack = (Button)findViewById(R.id.button_back);
        btnBack.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        mList.setOnItemClickListener(listener);

        selectAll();

        String resul= "학업 : "+numOfString("학업") +"회\n" +
                "식사 : "+numOfString("식사")+"회\n" +
                "여가 : "+numOfString("여가")+"회\n" +
                "운동 : "+numOfString("운동")+"회\n" +
                "기타 : "+numOfString("기타")+"회" ;
        TextView logView = (TextView) findViewById(R.id.textNum);
        logView.setText(resul);

    }

    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {

        //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
        //첫번째 파라미터 : 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
        //두번째 파라미터 : 클릭된 아이템 뷰
        //세번째 파라미터 : 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3.....)
        //네번재 파리미터 : 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라이터인 position과 같은 값)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            //클릭된 아이템의 위치를 이용하여 데이터인 문자열을 Toast로 출력
            //loadCard(nameList.get(position));
            //selectedCard = nameList.get(position);
            //Toast.makeText(ListActivity.this, nameList.get(position), Toast.LENGTH_SHORT).show();



            Intent intent = new Intent(ListActivity.this, zoomActivity.class);
            intent.putExtra("TextIn", nameList.get(position)); //인텐트에 데이터 심어 보내기
            startActivity(intent);

        }
    };

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

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
        finish();
        Toast.makeText(getApplicationContext(), "일정이 모두 삭제되었습니다", Toast.LENGTH_LONG).show();
    }


    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        if(results != null) {
            results.moveToFirst();

            while (!results.isAfterLast()) {
                //int id = results.getInt(0);
                String name = results.getString(0) +" "+ results.getString(1) + "<"+results.getString(2) + ">\n "+ results.getString(3);

//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
                //Log.d("lab_sqlite", "index= " + id + " name=" + name);

                nameList.add(name);
                results.moveToNext();
            }
            results.close();
        }
    }

    public int numOfString(String val){
        int count=0;

        for(int i=0; i<nameList.size(); i++){
            if(nameList.get(i).contains(val))
                count++;
            Log.d("lab_sqlite", nameList.get(i));
        }
        return count;
    }


    public void removeData() {
        //DialogSimple();
        String sql = "delete from " + tableName + " where name = '" + selectedCard + "';";
        db.execSQL(sql);
        //loadCard(selectedCard);

        nameList.clear();
        selectAll();
        baseAdapter.notifyDataSetChanged();
    }



}
