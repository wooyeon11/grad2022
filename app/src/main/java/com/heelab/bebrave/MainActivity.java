package com.heelab.bebrave;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.heelab.bebrave.ForegroundService.MyBinder;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;

import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.empatica.empalink.EmpaDeviceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.me.berndporr.iirj.Butterworth;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    private static final String myService="0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String myCharacteristic="0000fff4-0000-1000-8000-00805f9b34fb";
    public List<String> SesnorLogString;
    private long currentMillis=0;

    ForegroundService serviceClass;
    boolean isService = false;
    boolean isECGView = false;
    ecgView ECGfragment=null;

    boolean isServer = false;

    int CaliFlag = 0;
    int CaliCount=0;

    ImageView bgapp,img_blue,dbicon,ecgconnection;
    Switch ecgswitch;
    LinearLayout  firstmain;
    RelativeLayout splashtext;
    Animation frombottom;
    Button goUnity,calli,userSetting,ecgbutton,reportbutton,stressbutton,goAlarm;
    TextView dbtext,usertext;
    TextView bletitle;
    ListView blelist;
    AlertDialog bleDialog;
    AlertDialog e4Dialog;


    private BleDevice mybleDevice;
    private EmpaDeviceManager deviceManager = null;

    private DeviceAdapter mDeviceAdapter;

    private TextView E4statusLabel;

    // private static Handler mHandler ;

    String user_name="no_name";

    private ImageView ecg_loading;
    private ProgressDialog progressDialog;
    private Animation operatingAnim;

    private List<Float> bvp_list;
    private List<Float> temp_list;
    private List<Float> eda_list;


    private String devicename="";
    private boolean isECGStart = false;
    public boolean isLogin = false;
    public static Context context_main; // context 변수 선언
    Butterworth butterworth;
    private boolean connectionflag = false;
    public boolean doneCali = false;
    private boolean myECGdisconnect = false;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MyBinder mb = (MyBinder) iBinder;
            serviceClass = mb.getService(); // 서비스가 제공하는 메소드 호출하여 서비스쪽 객체를 전달받을수 있다.
            isService = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
        }
    };

    void AccBit(byte raw1, byte raw2, byte raw3, byte raw4,byte raw5){
        int acc1 = ((int)raw1)&0xFF;
        int tmp1 = ((int)raw2)&0xFF;
        Log.d("HEEE", "ACC1; "+acc1+"  "+Integer.toBinaryString(acc1)+" TMP1  "+tmp1+"  "+Integer.toBinaryString(tmp1));
        acc1= (acc1 << 2) + (tmp1>> 6);
     //   Log.d("HEEE", "@ACC1; "+acc1+"  "+Integer.toBinaryString(acc1));

        int acc2 = tmp1 ;//0b0011 1111;

        int tmp2 = ((int)raw3)&0xFF;
        Log.d("HEEE", "@TMP2; "+tmp2+"  "+Integer.toBinaryString(tmp2));
        int acc22= (acc2<<4)&1023+ (tmp2 >> 4);
        Log.d("HEEE", "@ACC2; "+acc22+"  "+Integer.toBinaryString(acc22));
        acc2= ((acc2<<4)+ (tmp2 >> 4))&1023;
        Log.d("HEEE", "@고친ACC2; "+acc2+"  "+Integer.toBinaryString(acc2));

        int acc3 = tmp2 & 15;//0b00001111;
        Log.d("HEEE", "@ACC3; "+acc3+"  "+Integer.toBinaryString(acc3));

        int tmp3 = ((int)raw4)&0xFF;
        Log.d("HEEE", "@TMP3; "+tmp3+"  "+Integer.toBinaryString(tmp3));

        acc3 = (acc3<<6)&1023 + (tmp3 >> 2);
        Log.d("HEEE", "@ACC3; "+acc3+"  "+Integer.toBinaryString(acc3));

        int acc4 = tmp3 & 3;// 0b00000011;
        Log.d("HEEE", "@ACC41; "+acc4+"  "+Integer.toBinaryString(acc4));
        int tmp4 = ((int)raw5)&0xFF;
        acc4 = (acc4<<8)&1023+tmp4;
        Log.d("HEEE", "@TMP42; "+tmp4+"  "+Integer.toBinaryString(tmp4));
        Log.d("HEEE", "@ACC42; "+acc4+"  "+Integer.toBinaryString(acc4));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main = this; // onCreate에서 this 할당

        //   mHandler = new Handler() ;
        SesnorLogString = new ArrayList<>();
        bvp_list= new ArrayList<>();
        temp_list= new ArrayList<>();
        eda_list= new ArrayList<>();

        butterworth = new Butterworth();
        //Bandpass
        butterworth.bandPass(4,128,50.25,99.5);

        UImanager();

        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("com.ims.empalink.sendintent");

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

        userSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //로그인 다이얼로그
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.logindialog, null);
                final EditText id = (EditText)loginLayout.findViewById(R.id.id);
                final EditText pw = (EditText)loginLayout.findViewById(R.id.pw);
                final EditText age = (EditText)loginLayout.findViewById(R.id.age);

                final RadioGroup genderRadio = (RadioGroup) loginLayout.findViewById(R.id.LoginradioGroup);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Create an account/Sign in")
                        .setView(loginLayout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {

                                String tmp =  id.getText().toString();
                                String tmp_pw =  pw.getText().toString();
                                String tmp_age = age.getText().toString();
                                int userAge=0;
                                try {
                                    userAge =  Integer.parseInt(tmp_age);
                                }
                                catch (NumberFormatException e)
                                {
                                    e.printStackTrace();
                                    userAge=0;
                                   // Toast.makeText(MainActivity.this, "Age", Toast.LENGTH_SHORT).show();
                                }
                                int rb4 = genderRadio.getCheckedRadioButtonId();
                                int Gender=0;
                                switch (rb4) {
                                    case R.id.male:
                                        Gender = 1;
                                        break;
                                    case R.id.female:
                                        Gender = 2;
                                        break;
                                    case R.id.trans:
                                        Gender = 3;
                                        break;
                                }

                                if(tmp.length()==0||tmp_pw.length()==0)
                                {
                                    Toast.makeText(MainActivity.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                                    isLogin= false;
                                    return;

                                }
//                                if(tmp.length()>6)
//                                {
//                                    Toast.makeText(MainActivity.this, "이름을 다시 설정해주세요", Toast.LENGTH_SHORT).show();
//                                    isLogin=false;
//                                    return;
//                                }
                                user_name = tmp;

                                serviceClass.user_name = user_name;
                                serviceClass.user_pw = tmp_pw;
                                serviceClass.userAge=userAge;
                                serviceClass.userGender=Gender;
                                serviceClass.nameEvent();//name , id 매칭


                            }
                        }).show();


            }

        });
        ecgswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ecgswitch.isChecked())
                {

                    if(!isServer)
                    {
                        myToast("먼저 서버연결을 확인해주세요");
                        ecgswitch.setChecked(false);
                        return;
                    } else if(!isLogin) {
                        myToast("먼저 로그인 해주세요");
                        ecgswitch.setChecked(false);
                        return;
                    }

                    checkPermissions();
                    OnBLE();
                }
                else
                {
                    myECGdisconnect=true;
                    OffBLE();
                }
            }

        });

        goUnity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //StartUnityFeedback();
               Intent intent = new Intent(MainActivity.this, MiddleActivity.class);
               startActivity(intent);
            }
        });
        goAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetAlarm.class);
                startActivity(intent);
            }
        });
        ecgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
            }

        });
        calli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCaliView();
            }
        });
        stressbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);

                startActivity(intent);
            }
        });
        reportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, reportMain.class);
                startActivity(intent);
            }
        });
        // 브로드 캐스트 1000ms 씩 가장 최근 것 쏘기


        // ECG 기기 연결 대화창 어댑터
        mDeviceAdapter = new DeviceAdapter(this);
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                    mybleDevice = bleDevice;
                    updateLabel(bletitle,"연결완료-정보를 요청해주세요");
                    long timenow= System.currentTimeMillis();
                    Date date = new Date(timenow);
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                    // nowDate 변수에 값을 저장한다.
                    String formatDate = sdfNow.format(date);
                    SesnorLogString.add(date+": "+bleDevice+" 연결 성공 ");
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    bvp_list.clear();
                    eda_list.clear();
                    temp_list.clear();
                }
                OffBLE();
            }

            @Override
            public void onDetail(BleDevice bleDevice) {

                if (BleManager.getInstance().isConnected(bleDevice)) {
                    progressDialog.setMessage("정보 요청 중...");
                    progressDialog.show();
                    blenotify(bleDevice);
                }
            }
        });
        // 연결 화면 다이얼로그
        makeBleDialog();
        //우선 끄고 시작

        OffBLE();


        //6시 알람
        set6();

        // 쓰레드 못죽게
        PowerManager.WakeLock wakeLock=null;
        if(wakeLock == null){
            PowerManager powerManager=(PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"myapp:wakelock");
            wakeLock.acquire();
        }
        if(wakeLock!=null){
            wakeLock.release();
            wakeLock=null;
        }
        //서비스
        Intent serviceintent = new Intent(this,ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceintent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 백그라운드 서비스 바인딩
        Intent intent = new Intent(
                MainActivity.this, // 현재 화면
                ForegroundService.class); // 다음넘어갈 컴퍼넌트

        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);

    }

    public boolean StartUnityFeedback() {
        if(isServer && isLogin&&doneCali) {
            serviceClass.feedbackdataEvent(true);
            return true;
        }
        else {
            if (!isServer) myToast("서버 연결이 되지 않았습니다.");
            if (!isLogin) myToast("로그인 해주세요.");
            if (!doneCali) myToast("Calibration 해주세요.");
            return false;
        }
    }

    public void set6(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour>=18) return;
        
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 1,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        calendar.set(Calendar.HOUR_OF_DAY,18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        AlarmManager alarmManager =(AlarmManager)getSystemService(Context.ALARM_SERVICE) ;

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

    }
    private void UImanager()
    {
        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        splashtext = (RelativeLayout) findViewById(R.id.splashtext);
        // hometext = (LinearLayout) findViewById(R.id.hometext);
        firstmain = (LinearLayout) findViewById(R.id.firstmain);
        bgapp=(ImageView) findViewById(R.id.bgapp);

        ecgconnection=(ImageView) findViewById(R.id.ecgconnect_icon);
        ecgswitch = (Switch) findViewById(R.id.ecgswitch);
        goUnity = (Button) findViewById((R.id.goUnity));
        ecgbutton = (Button) findViewById((R.id.ecgbutton));
        reportbutton = (Button) findViewById((R.id.e4button));
        stressbutton = (Button) findViewById((R.id.stressbutton));

        calli = (Button) findViewById(R.id.calli);
        userSetting=(Button) findViewById(R.id.goSetting);
        goAlarm=(Button) findViewById(R.id.goAlarm);
        //
        dbicon = (ImageView) findViewById(R.id.dbicon);
        dbtext =(TextView) findViewById(R.id.dbtext);
        usertext = (TextView) findViewById(R.id.idtext);

        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        progressDialog = new ProgressDialog(this);

        //메인창을 보이기 위한 애니메이션
        //bgapp.animate().translationY(-1200).setDuration(800).setStartDelay(300);
        ScaleAnimation backanim = new ScaleAnimation(1,1,1,0.52f,0,0);
        backanim.setDuration(800);
        backanim.setStartOffset(300);
        backanim.setFillAfter(true);
        bgapp.startAnimation(backanim);
        splashtext.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);
        firstmain.startAnimation(frombottom);
    }


//    class NewRunnable implements Runnable {
//        @Override
//        public void run() {
//
//            while(true) {
//                Intent sendIntent = new Intent();
//
//                // We add flags for example to work from background
//                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//
//                // SetAction uses a string which is an important name as it identifies the sender of the itent and that we will give to the receiver to know what to listen.
//                // By convention, it's suggested to use the current package name com.MentlsCompany.MentlsTest
//                sendIntent.setAction("com.ims.E4");
//
//                Bundle extras = new Bundle();
//                //            extras.putString("USERNAME",user_name);
//
//                if (isE4Start) {
//                    int bvp_count = u_bvp_list.size();
//                    if (bvp_count <= 0) {
//                        float data1[] = {0};
//                        extras.putFloatArray("BVP", data1);//0:bva 1:eda 2:temp
//                    } else {
//                        float data1[] = new float[bvp_count];
//                        for (int c = 0; c < bvp_count; c++) {
//                            data1[c] = (u_bvp_list.get(0) != null ? u_bvp_list.get(0) : Float.NaN); // Or whatever default you want.
//                            u_bvp_list.remove(0);
//                        }
//                        extras.putFloatArray("BVP", data1);//0:bva 1:eda 2:temp
//                    }
//
//
//                    int eda_count = u_eda_list.size();
//                    if (eda_count <= 0) {
//                        float data2[] = {0};
//                        extras.putFloatArray("EDA", data2);//0:bva 1:eda 2:temp
//
//                    } else {
//                        float data2[] = new float[eda_count];
//                        for (int c = 0; c < eda_count; c++) {
//                            data2[c] = (u_eda_list.get(0) != null ? u_eda_list.get(0) : Float.NaN); // Or whatever default you want.
//                            u_eda_list.remove(0);
//                        }
//                        extras.putFloatArray("EDA", data2);//0:bva 1:eda 2:temp
//                    }
//
//
//                    int temp_count = u_temp_list.size();
//                    if (temp_count <= 0) {
//                        float data3[] = {0};
//                        extras.putFloatArray("TEMP", data3);//0:bva 1:eda 2:temp
//                    } else {
//                        float data3[] = new float[temp_count];
//                        for (int c = 0; c < temp_count; c++) {
//                            data3[c] = (u_temp_list.get(0) != null ? u_temp_list.get(0) : Float.NaN); // Or whatever default you want.
//                            u_temp_list.remove(0);
//                        }
//                        extras.putFloatArray("TEMP", data3);//0:bva 1:eda 2:temp
//                    }
//
//                 //   ((ecgView)ecgView.context_ecgview).insertECG(extras);
//
//                    //  sendIntent.putExtras(extras);
//                                // Here we fill the Intent with our data, here just a string with an incremented number in it.
//                                // sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
//                                // And here it goes ! our message is send to any other app that want to listen to it.
//                  //  LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sendIntent);
//                                //  Log.d("test",msg);
//                                // In our case we run this method each second with postDelayed
//                                // mHandler.removeCallbacks(this);
//                                //mHandler.postDelayed(this, 1000);
//                }
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    //stress view 액티비티 트리거 리시버 form ForegroundService
//    public  void startStressView(Bundle bundle,boolean flag)
//    {
//        if(flag) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    progressDialog.dismiss();
//                    Intent intent = new Intent(getApplicationContext(), stressView.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
//        }
//        else
//        {
//            progressDialog.dismiss();
//        }
//    }
    public  void startCaliView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(connectionflag) {
                    Intent intent = new Intent(MainActivity.this, callibrationView.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "디바이스를 먼저 연결해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //버튼은 끄는 건 이벤트 없으니까 따로 다른 클래스에서 시작과 끝에서 불러주도록 이 함수를 만듦
    public void toggleGraphView(int flag,  Fragment fragment)
    {
        if(flag==1) {
            isECGView = true;
            ECGfragment = (ecgView) fragment;
        }
        else
        {
            isECGView = false;
            ECGfragment = null;

        }
    }

    public void changeDBicon(boolean tmp){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(tmp)
                {
                    dbicon.setImageResource(R.mipmap.dbok);
                    dbtext.setText("connected");
                    isServer = true;
                }
                else
                {
                    dbicon.setImageResource(R.mipmap.dbfail);
                    dbtext.setText("disconnected");
                    isServer = false;
                }
            }
        });

    }

    public void sendReportResult(Bundle bundle)
    {
        Handler serviceHanlder = serviceClass.sendHandler;
        if(serviceHanlder!=null)
        {
            Message message= serviceHanlder.obtainMessage();
            message.setData(bundle);
            serviceHanlder.sendMessage(message);
            myToast("답변이 제출되었습니다.");
        }
        else
        {
            myToast("서버 연결을 확인해주세요");
        }

    }
    /*public boolean finishCalii(){

        ProgressDialog pd = ProgressDialog.show(context_main, "", "Checking if calibration is done...");
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
               if(!doneCali){
                   Toast.makeText(context_main,"Calibration 다시 시도해주세요",Toast.LENGTH_SHORT).show();
               }

            }
        });

        while (serviceClass.getCalliResultfromDB()<=0){
            if(!pd.isShowing()) break;
        }
        int calliEnd=serviceClass.getCalliResultfromDB();
        if(calliEnd>0){
            Toast.makeText(context_main,"Completed",Toast.LENGTH_SHORT).show();
            doneCali=true;
            serviceClass.isCalli = 0;
            pd.dismiss();
            return  true;
        } else {
            Toast.makeText(context_main,"Calibration 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
    }*/


    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }
    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                if(ecg_loading!=null) {
                    ecg_loading.startAnimation(operatingAnim);
                    ecg_loading.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                if(ecg_loading!=null) {
                    ecg_loading.clearAnimation();
                    ecg_loading.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.setMessage("ECG PATCH 연결 중...");
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                if(ecg_loading!=null) {
                    ecg_loading.clearAnimation();
                    ecg_loading.setVisibility(View.INVISIBLE);
                }
                progressDialog.dismiss();

                myToast("ECG Patch 연결을 다시 시도해주세요.");
                isECGStart = false;
                OffBLE();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                //mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.setDevice(bleDevice);
                setMtu(bleDevice,512);
                mDeviceAdapter.notifyDataSetChanged();
                sendNotification(0,"",3);
                //Toast.makeText(MainActivity.this, "연결됨", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                isECGStart = false;

                bvp_list.clear();
                eda_list.clear();
                temp_list.clear();
                if (isActiveDisConnected) {
                    myToast(getString(R.string.active_disconnected));
                } else {
                    myToast(getString(R.string.disconnected));
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                    if(!myECGdisconnect) sendNotification(1,bleDevice.getName(),3);
                }
                OffBLE();
                myECGdisconnect=false;
            }
        });
    }

    private void makeBleDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.blealert, null);
        ecg_loading = (ImageView) view.findViewById(R.id.ecg_loading);
        bletitle = (TextView) view.findViewById(R.id.dialog_title);

        builder.setView(view);

        blelist = (ListView)view.findViewById(R.id.list_device2);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(!isECGStart) ecgswitch.setChecked(false);
            }
        });
        bleDialog = builder.create();


        blelist.setAdapter(mDeviceAdapter);

        bleDialog.setCancelable(true);

        bleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }


    private void blenotify(final BleDevice bleDevice)
    {
        BleManager.getInstance().notify(
                bleDevice,
                myService,
                myCharacteristic,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {

                        blewrite(bleDevice);
                    }


                    @Override
                    public void onNotifyFailure(BleException exception) {
                        progressDialog.dismiss();
                        myToast("ECG PATCH의 연결을 다시 시도해주세요.");
                        mybleDevice = null;
                        long timenow= System.currentTimeMillis();
                        Date date = new Date(timenow);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String formatDate = sdfNow.format(date);
                        SesnorLogString.add(date+": "+bleDevice.getName()+" 데이터 수신 실패 ");

                    }

                    @Override
                    public void onCharacteristicChanged(byte[] value) {

                        if(value.length!=237) return;

                        if(!isECGStart) {// 1>> ecg 처음 받을 때
                            isECGStart = true;
                            currentMillis= System.currentTimeMillis();//첫시작의 시간을 저장
                            myToast("ECG PATCH의 연결이 완료되었습니다.");

                            long timenow= System.currentTimeMillis();
                            Date date = new Date(timenow);
                            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            String formatDate = sdfNow.format(date);
                            SesnorLogString.add(date+": "+bleDevice.getName()+" 데이터 수신 시작 ");

                            updateImage(ecgconnection,R.mipmap.connected);
                            updateLabel(bletitle,"연결할 ECG Patch를 선택해주세요");
                            if(bleDialog.isShowing()) bleDialog.dismiss();
                            if(progressDialog.isShowing()) progressDialog.dismiss();
                            mybleDevice = bleDevice;

                        }


                            if(!connectionflag){// 3>> 처음으로  연결됐을 때
                                connectionflag =true;
                                currentMillis= System.currentTimeMillis();
                                //doneCali = serviceClass.getCalliResultfromDB();
                                if(!doneCali)//캘리함?
                                {
                                    CaliFlag = 1;
                                    Toast.makeText(context_main, "Calibration 을 시작합니다.", Toast.LENGTH_SHORT).show();
                                    startCaliView();

                                } else {
                                    CaliFlag=0;
                                }
                            }


                        StringBuilder sb = new StringBuilder();

                        int buf = 0;
                        int pre_buf = ((int)value[0])&0xFF;

                        int[] raw_ecg = new int[128];

                        for(int c=0;c<128; c++)
                        {
                            int level = 0;
                            buf = ((int)value[c])&0xFF;
                            if(Math.abs(buf-pre_buf)>128)
                            {
                                if (buf > pre_buf) level--;
                                else level++;
                            }
                            pre_buf = buf;
                            buf = (int)(buf + level * 255);
                            raw_ecg[c] = buf;
                            sb.append(buf);
                            if(c==127) sb.append("/");
                            else sb.append(",");

                            //필터링
                          //  filtered_ecg[c] = (float) butterworth.filter(buf);

                        }
                        int index=0;
                        int[] accall = new int[84];

//                        Log.d("HEEE", "@전; "+value[180]+" @트 "+Integer.toBinaryString((int)value[180])+" @후인트: "+Integer.toBinaryString(mp));
//                        int tmp11=value[180]<<2;
//                        int tmp21=((int)value[180])<<2;
//                        int tmp31=mp<<2;
//                        Log.d("HEEE", "$전; "+Integer.toBinaryString(tmp11)+" $인트 "+Integer.toBinaryString(tmp21)+" $후인트: "+Integer.toBinaryString(tmp31));

                        for (int c = 0; c < 21; c++)
                        {
                            int acc1 = ((int)value[5 * c + 128])&0xFF;
                            int tmp1 = ((int)value[5 * c + 128 + 1])&0xFF;
                         //   Log.d("HEEE", "ACC1 " + acc1 );
                         //   Log.d("HEEE", "TMP1 " + tmp1 );

                            acc1= (acc1 << 2) + (tmp1>> 6);
                         //   Log.d("HEEE", "@ACC1 " + acc1 );

                            int acc2 = tmp1 ;//0b0011 1111;

                            int tmp2 = ((int)value[5 * c + 128 + 2 ])&0xFF;
                         //   Log.d("HEEE", "TMP2 " + acc1 );

                            acc2= (acc2<<4)+ (tmp2 >> 4);
                            acc2 = acc2&1023;

                            int acc3 = tmp2 & 15;//0b00001111;

                            int tmp3 = ((int)value[5 * c + 128 + 3])&0xFF;

                            acc3 = (acc3<<6)+ (tmp3 >> 2);
                            acc3 = acc3 & 1023;

                            int acc4 = tmp3 & 3;// 0b00000011;
                            int tmp4 = ((int)value[5 * c + 128 + 4])&0xFF;
                            acc4 = (acc4<<8)+tmp4;
                            acc4 = acc4&1023;
                         //   Log.d("HEEE", acc1+" "+acc2+" "+acc3+" "+acc4);
                            if (acc1 > 511) acc1 = acc1 - 1023;
                            if (acc2 > 511) acc2 = acc2 - 1023;
                            if (acc3 > 511) acc3 = acc3 - 1023;
                            if (acc4 > 511) acc4 = acc4 - 1023;

                            accall[index++] = acc1;
                            accall[index++] = acc2;
                            accall[index++] = acc3;
                            accall[index++] = acc4;

                            sb.append(acc1);
                            sb.append(",");
                            sb.append(acc2);
                            sb.append(",");
                            sb.append(acc3);
                            sb.append(",");
                            sb.append(acc4);
                            if(c==20)sb.append("/");
                            else sb.append(",");

                        }

                        //ecg 브로드캐스팅

                        if(isECGView)//그래프 plot 위함
                        {
                            Handler ECGGraphHandler=ECGfragment.ecgGraphHandler;
                            if(ECGGraphHandler!=null) {
                                int[] raw_accx = new int[28];
                                int[] raw_accy = new int[28];
                                int[] raw_accz = new int[28];
                                for(int i=0; i<28;i++){
                                    raw_accx[i] = accall[3*i];
                                 //   Log.d("HEEE", "xxx: "+raw_accx[i]);
                                }
                                for(int i=0; i<28;i++){
                                    raw_accy[i] = accall[1+3*i];
                                 //   Log.d("HEEE", "yyy"+raw_accy[i]);
                                }
                                for(int i=0; i<28;i++){
                                    raw_accz[i] = accall[2+3*i];
                                 //   Log.d("HEEE", "zzz"+raw_accz[i]);
                                }

                                Bundle extras= new Bundle();
                                extras.putIntArray("ECG",raw_ecg);
                                extras.putIntArray("ACCX",raw_accx);
                                extras.putIntArray("ACCY",raw_accy);
                                extras.putIntArray("ACCZ",raw_accz);
                                Message message = ECGGraphHandler.obtainMessage();
                                message.setData(extras);
                                ECGGraphHandler.sendMessage(message);
                            }
                        }

                        int bvpcount = bvp_list.size();
                        for( int c=0; c<bvpcount ;c++)
                        {
                            sb.append(bvp_list.get(0));
                            bvp_list.remove(0);
                            if(c!=bvpcount-1) sb.append(",");
                        }
                        sb.append("/");
                        int edacount = eda_list.size();
                        for( int c=0; c<edacount;c++)
                        {
                            sb.append(eda_list.get(0));
                            eda_list.remove(0);
                            if(c!=edacount-1)sb.append(",");
                        }
                        sb.append("/");
                        int tempcount = temp_list.size();
                        for( int c=0; c<tempcount;c++)
                        {
                            sb.append(temp_list.get(0));
                            temp_list.remove(0);
                            if(c!=tempcount-1) sb.append(",");
                        }
                        sb.append("/");

                        if(connectionflag)//모두 연결된 정상적인 state 일땐 service class 에 timelist와 datalist에 데이터 더하기
                        {
                            // 현재시간을 msec 으로 구한다.
                            // 현재시간을 date 변수에 저장한다.
                            Date date = new Date(currentMillis);
                            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                            // nowDate 변수에 값을 저장한다.
                            String formatDate = sdfNow.format(date);
                            //serviceClass.time_list.add(formatDate);
                            // serviceClass.data_list.add(sb.toString());

                            Handler serviceHanlder = serviceClass.sendHandler;
                            if(serviceHanlder!=null)
                            {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Flag",true);
                                bundle.putString("DATATIME",formatDate);
                                bundle.putString("FULLDATA",sb.toString());
                                bundle.putInt("CALI",CaliFlag);
                                Message message= serviceHanlder.obtainMessage();
                                message.setData(bundle);
                                serviceHanlder.sendMessage(message);
                                if(CaliFlag==1) {
                                    CaliCount++;
                                    if(CaliCount>=60){//1분 캘리브레이션 60개 데이터 수 채우면 다시 0으로
                                        CaliCount=0;
                                        CaliFlag=0;
                                    }
                                }
                            }
                            currentMillis+=1000;
                            //시간이 30초 이상 차이가 커지면 다시 최근시간으로 업데이트
                            long now =  System.currentTimeMillis();
                            if(Math.abs(now-currentMillis)>1000*30){
                                currentMillis=now;
                            }

                        }

                    }
                });
    }

    void stopNotify(BleDevice bleDevice)
    {
        BleManager.getInstance().stopNotify(bleDevice,myService,myCharacteristic);

    }

    void blewrite(BleDevice bleDevice)
    {

        String hex = "67";
        BleManager.getInstance().write(
                bleDevice,
                myService,
                myCharacteristic,
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {

                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        OffBLE();
                        myToast("ECG Patch 정보 요청 실패, 다시 시도해주세요");
                        BleManager.getInstance().disconnectAllDevice();
                        long timenow= System.currentTimeMillis();
                        Date date = new Date(timenow);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String formatDate = sdfNow.format(date);
                        SesnorLogString.add(date+": "+bleDevice.getName()+" 데이터 수신 실패 ");

                        mybleDevice = null;
                    }
                });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Log.i("tag", "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Log.i("tag", "onMtuChanged: " + mtu);
            }
        });
    }

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            myToast("핸드폰의 블루투스기능을 켜주세요");
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {

                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }



    @Override
    protected void onResume() {
        super.onResume();

        //showConnectedDevice();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (deviceManager != null) {
            deviceManager.stopScanning();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deviceManager != null) {
            deviceManager.cleanUp();
        }
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        unbindService(serviceConnection);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The user chose not to enable Bluetooth
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            // You should deal with this
            startScan();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    private void updateImage(final ImageView imageview, final int res)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageview.setImageResource(res);
            }
        });
    }

    // Update a label with some text, making sure this is run in the UI thread
    private void updateLabel(final TextView label, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                label.setText(text);
            }
        });
    }
    public void updateNameLabel(boolean flag) {
        if(flag)
        {
            updateLabel(usertext,user_name+"님");
        }
        else
        {
            user_name="no_name";
            updateLabel(usertext,"로그인 해주세요");
        }

    }



    void  OnBLE() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(!ecgswitch.isChecked()) ecgswitch.setChecked(true);
                bleDialog.show();
            }
        });
    }

    void sendNotification(int flag,String str,int id)
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(flag==1)//연결해제f
                {
                    serviceClass.disconnectNotification(str,id);
                }
            }
        });
    }
    void OffBLE() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try {
                    bleDialog.dismiss();
                    progressDialog.dismiss();

                    ecgconnection.setImageResource(R.mipmap.disconnected);
                    ecgswitch.setChecked(false);
                    //   updateLabel( readytext, "센서를 연결해주세요.");

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        try {
            connectionflag = false;
            if (serviceClass != null) serviceClass.initialize();
            if (mybleDevice != null) {
                BleManager.getInstance().cancelScan();
                if (BleManager.getInstance().isConnected(mybleDevice)) {
                    BleManager.getInstance().disconnect(mybleDevice);
                    mDeviceAdapter.removeDevice(mybleDevice);
                    mDeviceAdapter.notifyDataSetChanged();

                    long timenow= System.currentTimeMillis();
                    Date date = new Date(timenow);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String formatDate = sdfNow.format(date);
                    SesnorLogString.add(date+": "+mybleDevice.getName()+" 연결 해제 ");

                }
                mDeviceAdapter.removeDevice(mybleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                mybleDevice = null;
            }

            isECGStart = false;
        }
        catch (Exception e)
        {

        }
    }


    public void myToast(final String string)
    {
        if(Looper.myLooper() != Looper.getMainLooper()) {

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();

                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
        }
    }





}
