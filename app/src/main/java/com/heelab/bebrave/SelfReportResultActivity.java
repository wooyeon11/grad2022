package com.heelab.bebrave;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SelfReportResultActivity extends AppCompatActivity {

    int a=0,b=0;
    float t1=0f, t2=0f;
    private SeekBar seekBarA, seekBarB;
    private TextView text_th1, text_th1ex, text_th2, text_th2ex;
    private ImageView face1, face2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfreportresult);

        seekBarA = findViewById(R.id.seekbar);
        seekBarB = findViewById(R.id.seekbar2);
        text_th1=findViewById(R.id.badthought);
        text_th1ex=findViewById(R.id.badthoughtex1);
        text_th2=findViewById(R.id.badthought2);
        text_th2ex=findViewById(R.id.badthoughtex2);
        face1 = findViewById(R.id.face1);
        face2 = findViewById(R.id.face2);

        Button backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle= getIntent().getExtras();
        a= bundle.getInt("A");
        b= bundle.getInt("B");
        float[] th=bundle.getFloatArray("Th");
        float T1 = th[0];
        float T2= th[1];
        float T3= th[2];
        float T4= th[3];

        if(T1>=T2){
            if(T3>=T4){
                if(T1>=T3){
                    //1등 t1
                    setTH1(1,(int)T1);
                    if(T2>=T3){
                        //2등 t2
                        setTH2(2,(int)T2);
                    } else {
                        //2등 t3
                        setTH2(3,(int)T3);
                    }
                } else{
                    //1등 T3
                    setTH1(3,(int)T3);
                    if(T1>=T4){
                        //2등 t1
                        setTH2(1,(int)T1);
                    } else {
                        //2등 T4
                        setTH2(4,(int)T4);
                    }
                }
            } else {
                if(T1>=T4){
                    //t1 1등
                    setTH1(1,(int)T1);
                    if(T4>=T2){
                        //2등 t4
                        setTH2(4,(int)T4);
                    } else {
                        //2등 T2
                        setTH2(2,(int)T2);
                    }
                } else {
                    //t4 1등
                    setTH1(4,(int)T4);
                    if(T1>=T3){
                        //T12등
                        setTH2(1,(int)T1);
                    }else {
                        //t3 2등
                        setTH2(3,(int)T3);
                    }
                }
            }
        } else {
            if(T3>=T4){
                if(T2>=T3){
                    //1등 t2
                    setTH1(2,(int)T2);
                    if(T1>=T3){
                        //2등 t1
                        setTH2(1,(int)T1);
                    } else {
                        //2등 t3
                        setTH2(3,(int)T3);
                    }
                } else{
                    //1등 T3
                    setTH1(3,(int)T3);
                    if(T2>=T4){
                        //2등 t2
                        setTH2(2,(int)T2);
                    } else {
                        //2등 T4
                        setTH2(4,(int)T4);
                    }
                }
            } else {
                if(T2>=T4){
                    //t2 1등
                    setTH1(2,(int)T2);
                    if(T4>=T1){
                        //2등 t4
                        setTH2(4,(int)T4);
                    } else {
                        //2등 T1
                        setTH2(1,(int)T1);
                    }
                } else {
                    //t4 1등
                    setTH1(4,(int)T4);
                    if(T2>=T3){
                        //T2 2등
                        setTH2(2,(int)T2);
                    }else {
                        //t3 2등
                        setTH2(3,(int)T3);
                    }
                }
            }
        }

        seekBarA.setProgress(5*a,true);
        seekBarB.setProgress(5*b,true);
    }
    //1: 재앙화사고3
    //2: 과잉일반화2
    //3: 자기비하2
    //4:이분법적 사고3
    void setTH1(int flag, int score){
        if(score<20){
            face1.setImageResource(R.mipmap.level5face);
           // text_th1.setText("발견된 자동적 사고가 없습니다.\n");
        } else if(score<=50){
            face1.setImageResource(R.mipmap.level4face);
        }else if(score<=70){
            face1.setImageResource(R.mipmap.level3face);
        }else if(score<=85){
            face1.setImageResource(R.mipmap.level2face);
        }else {
            face1.setImageResource(R.mipmap.level1face);
        }
        switch (flag){
            case 1:
                String str = "재앙화 사고\n"+score+"%";
                text_th1.setText(str);
                text_th1ex.setText(getString(R.string.disaster1));
                break;
            case 2:
                str = "과잉일반화\n"+score+"%";
                text_th1.setText(str);
                text_th1ex.setText(getString(R.string.toomuchbad1));
                break;
            case 3:
                str = "자기비하\n"+score+"%";
                text_th1.setText(str);
                text_th1ex.setText(getString(R.string.poorself1));
                break;
            case 4:
                str = "이분법적 사고\n"+score+"%";
                text_th1.setText(str);
                text_th1ex.setText(getString(R.string.whiteorblack1));
                break;
        }
    }
    void setTH2(int flag, int score){
//        if(score<20){
//            text_th1.setText("발견된 자동적 사고가 없습니다.\n");
//        }
        if(score<20){
            face2.setImageResource(R.mipmap.level5face);
            // text_th1.setText("발견된 자동적 사고가 없습니다.\n");
        } else if(score<=50){
            face2.setImageResource(R.mipmap.level4face);
        }else if(score<=70){
            face2.setImageResource(R.mipmap.level3face);
        }else if(score<=85){
            face2.setImageResource(R.mipmap.level2face);
        }else {
            face2.setImageResource(R.mipmap.level1face);
        }
        switch (flag){
            case 1:
                String str = "재앙화 사고\n"+score+"%";
                text_th2.setText(str);
                text_th2ex.setText(getString(R.string.disaster1));
                break;
            case 2:
                str = "과잉일반화\n"+score+"%";
                text_th2.setText(str);
                text_th2ex.setText(getString(R.string.toomuchbad1));
                break;
            case 3:
                str = "자기비하\n"+score+"%";
                text_th2.setText(str);
                text_th2ex.setText(getString(R.string.poorself1));
                break;
            case 4:
                str = "이분법적 사고\n"+score+"%";
                text_th2.setText(str);
                text_th2ex.setText(getString(R.string.whiteorblack1));
                break;
        }
    }
}