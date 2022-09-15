package com.heelab.bebrave;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ThinkingTest extends Fragment {

    private RadioGroup radioGroup1,radioGroup2,radioGroup3,radioGroup4,radioGroup5,radioGroup6,radioGroup7,radioGroup8,radioGroup9,radioGroup10;
    Button donereport;
    String mytime="";
    View view;
    int mode=2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.thinkingtestfragment, container, false);
        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //라디오 그룹
        radioGroup1 = (RadioGroup) view.findViewById(R.id.post_radioGroup1);
        radioGroup2 = (RadioGroup) view.findViewById(R.id.post_radioGroup2);
        radioGroup3 = (RadioGroup) view.findViewById(R.id.post_radioGroup3);
        radioGroup4 = (RadioGroup) view.findViewById(R.id.post_radioGroup4);
        radioGroup5 = (RadioGroup) view.findViewById(R.id.post_radioGroup5);
        radioGroup6 = (RadioGroup) view.findViewById(R.id.post_radioGroup6);
        radioGroup7 = (RadioGroup) view.findViewById(R.id.post_radioGroup7);
        radioGroup8 = (RadioGroup) view.findViewById(R.id.post_radioGroup8);
        radioGroup9 = (RadioGroup) view.findViewById(R.id.post_radioGroup9);
        radioGroup10 = (RadioGroup) view.findViewById(R.id.post_radioGroup10);



        //확인버튼
        donereport = (Button) view.findViewById(R.id.donereport);
        donereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donereportListner();
            }
        });

        SimpleDateFormat timeformat= new SimpleDateFormat ( "yyyy/MM/dd HH:mm:ss");
        Date time = new Date();
        mytime = timeformat.format(time);
    }

    private void donereportListner()
    {
        //a: 수행부족에 대한 예언
        //b: 타인의 부정적 평가에 대한 공포

        //1: 재앙화사고3
        //2: 과잉일반화2
        //3: 자기비하2
        //4:이분법적 사고3


        int a=0,b=0,t1=0,t2=0,t3=0,t4=0;
        int rb1 = radioGroup1.getCheckedRadioButtonId();
        switch (rb1){
            case R.id.pq1_1:
                a+=-0;t1+=0;
                break;
            case R.id.pq1_2:
                a+=1;t1+=1;
                break;
            case R.id.pq1_3:
                a+=2;t1+=2;
                break;
            case R.id.pq1_4:
                a+=3;t1+=3;
                break;
            case R.id.pq1_5:
                a+=4;t1+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb2 = radioGroup2.getCheckedRadioButtonId();
        switch (rb2){
            case R.id.pq2_1:
                b+=0; t2+=0;
                break;
            case R.id.pq2_2:
                b+=1;t2+=1;
                break;
            case R.id.pq2_3:
                b+=2;t2+=2;
                break;
            case R.id.pq2_4:
                b+=3;t2+=3;
                break;
            case R.id.pq2_5:
                b+=4;t2+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb3 = radioGroup3.getCheckedRadioButtonId();
        switch (rb3){
            case R.id.pq3_1:
                a+=0; t2+=0;
                break;
            case R.id.pq3_2:
                a+=1;t2+=1;
                break;
            case R.id.pq3_3:
                a+=2;t2+=2;
                break;
            case R.id.pq3_4:
                a+=3;t2+=3;
                break;
            case R.id.pq3_5:
                a+=4;t2+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb4 = radioGroup4.getCheckedRadioButtonId();
        switch (rb4){
            case R.id.pq4_1:
                a+=0;t3+=0;
                break;
            case R.id.pq4_2:
                a+=1;t3+=1;
                break;
            case R.id.pq4_3:
                a+=2;t3+=2;
                break;
            case R.id.pq4_4:
                a+=3;t3+=3;
                break;
            case R.id.pq4_5:
                a+=4;t3+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb5 = radioGroup5.getCheckedRadioButtonId();
        switch (rb5){
            case R.id.pq5_1:
                b+=0; t1+=0;
                break;
            case R.id.pq5_2:
                b+=1; t1+=1;
                break;
            case R.id.pq5_3:
                b+=2; t1+=2;
                break;
            case R.id.pq5_4:
                b+=3; t1+=3;
                break;
            case R.id.pq5_5:
                b+=4; t1+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb6 = radioGroup6.getCheckedRadioButtonId();
        switch (rb6){
            case R.id.pq6_1:
                b+=0; t4+=0;
                break;
            case R.id.pq6_2:
                b+=1; t4+=1;
                break;
            case R.id.pq6_3:
                b+=2; t4+=2;
                break;
            case R.id.pq6_4:
                b+=3; t4+=3;
                break;
            case R.id.pq6_5:
                b+=4; t4+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb7 = radioGroup7.getCheckedRadioButtonId();
        switch (rb7){
            case R.id.pq7_1:
                a+=0; t1+=0;
                break;
            case R.id.pq7_2:
                a+=1; t1+=1;
                break;
            case R.id.pq7_3:
                a+=2; t1+=2;
                break;
            case R.id.pq7_4:
                a+=3; t1+=3;
                break;
            case R.id.pq7_5:
                a+=4; t1+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb8 = radioGroup8.getCheckedRadioButtonId();
        switch (rb8){
            case R.id.pq8_1:
                b+=0; t4+=0;
                break;
            case R.id.pq8_2:
                b+=1; t4+=1;
                break;
            case R.id.pq8_3:
                b+=2; t4+=2;
                break;
            case R.id.pq8_4:
                b+=3; t4+=3;
                break;
            case R.id.pq8_5:
                b+=4; t4+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb9 = radioGroup9.getCheckedRadioButtonId();
        switch (rb9){
            case R.id.pq9_1:
                a+=0; t3+=0;
                break;
            case R.id.pq9_2:
                a+=1; t3+=1;
                break;
            case R.id.pq9_3:
                a+=2; t3+=2;
                break;
            case R.id.pq9_4:
                a+=3; t3+=3;
                break;
            case R.id.pq9_5:
                a+=4; t3+=4;
                break;
            default:
                emptyWarning();
                break;
        }
        int rb10 = radioGroup10.getCheckedRadioButtonId();
        switch (rb10){
            case R.id.pq10_1:
                b+=0; t4+=0;
                break;
            case R.id.pq10_2:
                b+=1; t4+=1;
                break;
            case R.id.pq10_3:
                b+=2; t4+=2;
                break;
            case R.id.pq10_4:
                b+=3; t4+=3;
                break;
            case R.id.pq10_5:
                b+=4; t4+=4;
                break;
            default:
                emptyWarning();
                break;
        }

//        Bundle bundle = new Bundle();
//        String str= String.valueOf(ans1)+String.valueOf(ans2)+String.valueOf(ans3)+String.valueOf(ans4)+String.valueOf(ans5)+String.valueOf(
//                ans6)+String.valueOf(ans7)+String.valueOf(ans8)+String.valueOf(ans9)+String.valueOf(ans10);
//        int tmp[] = new int[5];
//        tmp[0]=ans1-1;
//        tmp[1]=ans2-1;
//        tmp[2]=ans3-1;
//        tmp[3]=ans4-1;
//        tmp[4]=ans5-1;
//        bundle.putBoolean("Flag",false);
//        bundle.putString("REPORTTIME",mytime);
//        bundle.putIntArray("REPORTDATA",tmp);
//        bundle.putInt("REPORTMODE", mode);

//        ((MainActivity) MainActivity.context_main).sendReportResult(bundle);
//        getActivity().finish();
//        ((MainActivity) MainActivity.context_main).myToast(str);


        Intent intent = new Intent(getActivity(), SelfReportResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("A",a);
        bundle.putInt("B",b);
        float T1 = t1/12f*100;
        float T2= t2/8f*100;
        float T3 = t3/8f*100;
        float T4 = t4/12f*100;
        bundle.putFloatArray("Th",new float[]{T1,T2,T3,T4});

        intent.putExtras(bundle);
        startActivity(intent);

    }
    void emptyWarning(){
        ((MainActivity) MainActivity.context_main).myToast("빈칸을 모두 채워주세요");
        return;
    }

}