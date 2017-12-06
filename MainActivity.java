package myapplication1.afinal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Handler;
import android.widget.TabHost;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;


public class MainActivity extends AppCompatActivity {
    //    첫번째 tab
    ImageView imageView;

    ArrayList<Drawable> drawableList = new ArrayList<Drawable>();

    Handler handler = new Handler();

    TabHost tabHost;

//    //    두번째 탭
//    EditText dbName;
//    Button dbButton;
//    SQLiteDatabase db = null;
//
//    //    db이름과 테이블을 저장할 곳
//    String DBName;
//    String TABLEName;
//    public static final String TAG = "MainActivity";
//
//    EditText tableName;
//    Button tableButton;
//
//    // 테이블 생성을 위한 EditText와 Button 객체
//    EditText Name;
//    EditText Age;
//    EditText Phone;
//    Button insertButton;
//    String name;
//    String age;
//    String phone;

    // 두번째 탭

    String databaseName;
    String tableName;
    TextView status;
    boolean databaseCreated = false;
    boolean tableCreated = false;

    
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 두번째 탭
        final EditText databaseNameInput = (EditText)findViewById(R.id.databaseNameInput);
        final EditText tableNameInput = (EditText)findViewById(R.id.tableNameInput);

        Button createDatabaseBtn = (Button) findViewById(R.id.createDatabaseBtn);
        createDatabaseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseName = databaseNameInput.getText().toString();
                createDatabase(databaseName);
            }
        });

        Button createTableBtn = (Button) findViewById(R.id.createTableBtn);
        createTableBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tableName = tableNameInput.getText().toString();
                createTable(tableName);
                int count = insertRecord(tableName);
                println(count + " records inserted.");
            }
        });

        status = (TextView) findViewById(R.id.status);





        // 첫번째 탭
        imageView = (ImageView) findViewById(R.id.imageView);

        startAnimation();

        //    세번째 탭
        // 지오코딩(GeoCoding) : 주소,지명 => 위도,경도 좌표로 변환
        //     위치정보를 얻기위한 권한을 획득, AndroidManifest.xml
        //    ACCESS_FINE_LOCATION : 현재 나의 위치를 얻기 위해서 필요함
        //    INTERNET : 구글서버에 접근하기위해서 필요함



        final TextView tv = (TextView)findViewById(R.id.textView4);
        Button b1 = (Button)findViewById(R.id.button1);
        Button b2 = (Button)findViewById(R.id.button2);
        Button b3 = (Button)findViewById(R.id.button3);
        Button b4 = (Button)findViewById(R.id.button4);

        final EditText et1 = (EditText)findViewById(R.id.editText1);
        final EditText et2 = (EditText)findViewById(R.id.editText2);
        final EditText et3 = (EditText)findViewById(R.id.editText3);

        final Geocoder geocoder = new Geocoder(this);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Address> list = null;
                try {
                    double d1 = Double.parseDouble(et1.getText().toString());
                    double d2 = Double.parseDouble(et2.getText().toString());
                    list = geocoder.getFromLocation(d1, d2, 10);
                    // d1 위도, d2 경도,  10 얻어올 값의 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");

                }
                if (list != null) {
                    if(list.size() ==0) {
                        tv.setText("해당되는 주소 정보는 없습니다.");
                    }else {
                        tv.setText(list.get(0).toString());
                    }
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Address> list = null;

                String str = et3.getText().toString();
                try {
                    list = geocoder.getFromLocationName(
                            str, // 지역 이름
                            10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        tv.setText("해당되는 주소 정보는 없습니다");
                    } else {
                        tv.setText(list.get(0).toString());
                    }
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 위도,경도 입력 후 지도 버튼 클릭 => 지도화면으로 인텐트 날리기
                double d1 = Double.parseDouble(et1.getText().toString());
                double d2 = Double.parseDouble(et2.getText().toString());

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:" + d1 + "," + d2));
                startActivity(intent);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 주소입력후 지도2버튼 클릭시 해당 위도경도값의 지도화면으로 이동
                List<Address> list = null;

                String str = et3.getText().toString();
                try {
                    list = geocoder.getFromLocationName
                            (str, // 지역 이름
                                    10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        tv.setText("해당되는 주소 정보는 없습니다");
                    } else {
                        // 해당되는 주소로 인텐트 날리기
                        Address addr = list.get(0);
                        double lat = addr.getLatitude();
                        double lon = addr.getLongitude();

                        String sss = String.format("geo:%f,%f", lat, lon);

                        Intent intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(sss));
                        startActivity(intent);
                    }
                }
            }
        });




// 두번째 탭1
//        dbName = (EditText) findViewById(R.id.dbName);
//        dbButton = (Button) findViewById(R.id.dbButton);
//        tableName = (EditText) findViewById(R.id.tableName);
//        tableButton = (Button) findViewById(R.id.tableButton);
//
////        DB 생성 버튼
//        dbButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                DBName = dbName.getText().toString();
//                makeDB(DBName);
//            }
//        });
//
//        // 테이블 생성 버튼
//        tableButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                TABLEName = tableName.getText().toString();
//                makeTABLE(TABLEName);
//            }
//        });
//
//        Name = (EditText) findViewById(R.id.Name);
//        Age = (EditText) findViewById(R.id.Age);
//        Phone = (EditText) findViewById(R.id.Phone);
//        insertButton = (Button) findViewById(R.id.insertButton);
//
//        insertButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                insertRecord();
//            }
//        });
//
//        // 두번째 탭 DB생성
//        private void makeDB(String name) {
//            try {
//                //DB 생성 메소두
//                db = openOrCreateDatabase(name, MODE_WORLD_WRITEABLE, null);
//                if (db!= null) {
//                    MakeToast(name + "DB 생성 성공");
//                }
//            } catch (Exception ex) {
//                Log.e(TAG, "데이터 베이스 생성 오류", ex);
//                MakeToast("DB 생성 오류");
//            }
//        }
//
//        public void MakeToast(String str) {
//            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
//        }
//
//        private void makeTABLE(String name) {
//            try {
//                String CREATE_SQL = "create table" + name + "(" +
//                        " _number integer PRIMARY KEY autoincrement,"
//                        + " name text,"
//                        + " age integer,"
//                        + "mobie text);";
//                db.execSQL(CREATE_SQL);
//                MakeToast(name + "테이블 생성 성공");
//            } catch (Exception ex) {
//                Log.e(TAG, "테이블 생성 실패", ex);
//                MakeToast("테이블 생성 실패");
//            }
//        }
//
//
//
//        public void settingArrgs() {
//            name = Name.getText().toString();
//            age = Age.getText().toString();
//            phone = Phone.getText().toString();
//        }
//
//        public void insertRecord() {
//            settingArrgs();
//            int count = 1;
//
//            ContentValues recordValues = new ContentValues();
//
//            recordValues.put("name", name);
//            recordValues.put("age", Integer.getInteger(age));
//            recordValues.put("mobile", phone);
//
//            try {
//                int rowPosition = (int) db.insert(TABLEName, null, recordValues);
//                MakeToast(String.valueOf(rowPosition) + "번째 레코드가 삽입되었습니다");
//            } catch (Exception ex) {
//                Log.e(TAG, "레코드 삽입 실패", ex);
//                MakeToast("레코드 삽입 실패");
//            }
//
//            Name.setText("");
//            Age.setText("");
//            Phone.setText("");
//        }


// 탭
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();


        TabHost.TabSpec tab1 = tabHost.newTabSpec("1").setContent(R.id.tab1).setIndicator("Thread");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("2").setContent(R.id.tab2).setIndicator("DB");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("3").setContent(R.id.tab3).setIndicator("지도");

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

    }

    // 두번째
    private void createDatabase(String name) {
        println("creating database [" + name + "].");

        try {
            db = openOrCreateDatabase(
                    name,
                    Activity.MODE_PRIVATE,
                    null);


            databaseCreated = true;
            println("database is created.");
        } catch(Exception ex) {
            ex.printStackTrace();
            println("database is not created.");
        }
    }

    private void createTable(String name) {
        println("creating table [" + name + "].");

        db.execSQL("create table if not exists " + name + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " age integer, "
                + " phone text);" );

        tableCreated = true;
    }

    private int insertRecord(String name) {
        println("inserting records into table " + name + ".");

        int count = 3;
        db.execSQL( "insert into " + name + "(name, age, phone) values ('John', 20, '010-7788-1234');" );
        db.execSQL( "insert into " + name + "(name, age, phone) values ('Mike', 35, '010-8888-1111');" );
        db.execSQL( "insert into " + name + "(name, age, phone) values ('Sean', 26, '010-6677-4321');" );

        return count;
    }

    /**
     * insert records using parameters
     */
    private int insertRecordParam(String name) {
        println("inserting records using parameters.");

        int count = 1;
        ContentValues recordValues = new ContentValues();

        recordValues.put("name", "Rice");
        recordValues.put("age", 43);
        recordValues.put("phone", "010-3322-9811");
        int rowPosition = (int) db.insert(name, null, recordValues);

        return count;
    }

    /**
     * update records using parameters
     */
    private int updateRecordParam(String name) {
        println("updating records using parameters.");

        ContentValues recordValues = new ContentValues();
        recordValues.put("age", 43);
        String[] whereArgs = {"Rice"};

        int rowAffected = db.update(name,
                recordValues,
                "name = ?",
                whereArgs);

        return rowAffected;
    }

    /**
     * delete records using parameters
     */
    private int deleteRecordParam(String name) {
        println("deleting records using parameters.");

        String[] whereArgs = {"Rice"};

        int rowAffected = db.delete(name,
                "name = ?",
                whereArgs);

        return rowAffected;
    }

    private void println(String msg) {
        Log.d("MainActivity", msg);
        status.append("\n" + msg);

    }





// 첫번째 탭 애니매이션
    public void startAnimation() {
        Resources res = getResources();

        drawableList.add(res.getDrawable(R.drawable.s1));
        drawableList.add(res.getDrawable(R.drawable.s2));
        drawableList.add(res.getDrawable(R.drawable.ss1));
        drawableList.add(res.getDrawable(R.drawable.ss2));
        drawableList.add(res.getDrawable(R.drawable.snoopy1));
        drawableList.add(res.getDrawable(R.drawable.snoopy1));

        AnimThread thread = new AnimThread();
        thread.start();
    }

    class AnimThread extends Thread {
        public void run() {
            int index = 0;
            for (int i = 0; i < 100; i++) {
                final Drawable drawable = drawableList.get(index);
                index += 1;
                if (index > 4) {
                    index = 0;
                }


                handler.post(new Runnable() {
                    public void run() {
                        imageView.setImageDrawable(drawable);
                    }
                });


                try {
                    Thread.sleep(500);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
