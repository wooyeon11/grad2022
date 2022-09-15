package com.heelab.bebrave;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class FinalDataFrag extends Fragment {

    TextView intro_text;
    LineChart HRChart;
    LineChart BRChart;
    BarChart ANXIETYChart;
    ValueFormatter formatter;
    Bundle DBData;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           Bundle bundle =  intent.getExtras();
           if(bundle==null)
           {
               Log.d("HEEE","데이터를 받아오지 못하였습니다.\n나중에 다시 시도해주세요.");
               intro_text.setText("데이터를 받아오지 못하였습니다.\n나중에 다시 시도해주세요.");
              // finish();

           }
           else
           {
               Log.d("HEEE","데이터 받아옴");
               DBData = bundle;
               settingdata();

           }
            // intent ..
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =inflater.inflate(R.layout.stressview, container, false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        
        HRChart= (LineChart) view.findViewById(R.id.meanHRChart);
        BRChart= (LineChart) view.findViewById(R.id.meanBrChart);
        ANXIETYChart= (BarChart) view.findViewById(R.id.AnxietyChart);
        intro_text=  view.findViewById(R.id.intro);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver, new IntentFilter("DBDATA"));

        if(!((ForegroundService) ForegroundService.foregroundService).dataEvent()) {
            intro_text.setVisibility(View.VISIBLE);
            intro_text.setText("데이터를 받아오지 못하였습니다.\n나중에 다시 시도해주세요.");
            Log.d("HEEE","데이터 받아옴XX");
            // finish();
        } else {
            Log.d("HEEE","데이터 받아옴0");
            intro_text.setVisibility(View.GONE);
        }
//        ecg_sqa_list=new ArrayList<>();
//        ppg_sqa_list=new ArrayList<>();
//        HRmean_list=new ArrayList<>();
//        BRmean_list=new ArrayList<>();
//        RESP_list=new ArrayList<>();
//        Anxiety_list=new ArrayList<>();
//        DateTime_list=new ArrayList<>();


    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("HEEE","ONREUME");
        if(intro_text!=null){
        if (!((ForegroundService) ForegroundService.foregroundService).dataEvent()) {
            intro_text.setText("데이터를 받아오지 못하였습니다.\n나중에 다시 시도해주세요.");
            // finish();
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }
    
    private void settingdata()
    {
//        ArrayList<Integer> ecg_sqa_list=null;
        ArrayList<Integer> HRmean_list=null;
        ArrayList<Integer>  BRmean_list=null;
        ArrayList<Integer> Anxiety_list=null;
        ArrayList<String> DateTime_list=null;
        ArrayList<Entry> HRentries = new ArrayList<>();
        ArrayList<Entry> BRentries = new ArrayList<>();
        List<BarEntry> Anxietyentries = new ArrayList<>();
        try{
            if(DBData.getStringArrayList("TIME")!=null)
                DateTime_list = DBData.getStringArrayList("TIME");

//                ecg_sqa_list = DBData.getIntegerArrayList("ECG_SQA");
                HRmean_list =  DBData.getIntegerArrayList("HRmean");
                BRmean_list =  DBData.getIntegerArrayList("BRmean");
                Anxiety_list= DBData.getIntegerArrayList("Anxiety");

            for(int i =0; i<DateTime_list.size();i++)
            {
//                String[] tmp1= DateTime_list.get(i).split(" ");
//                tmp1[1].split(":")
                if(true) {//ecg_sqa_list.get(i)==1
                    HRentries.add(new Entry(i, HRmean_list.get(i)));
                    BRentries.add(new Entry(i, BRmean_list.get(i)));
                    Anxietyentries.add(new BarEntry(i,Anxiety_list.get(i)+0.1f));
                }
            }


            if(Anxiety_list!=null&&HRentries!=null&&BRentries!=null)
            {
                drawChart(HRentries,1,DateTime_list);
                drawChart(BRentries,2,DateTime_list);
                drawBar(Anxietyentries,DateTime_list);
            }
            else
            {
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            //text.setText("오류");
        }
        finally {
           //text.setText(tmp.toString());
        }
    }
//시간축
    public class XAxisValueFormatter extends ValueFormatter
    {
        private ArrayList<String> mValues =null;
        private int mValueCount = 0;

        /**
         * An empty constructor.
         * Use `setValues` to set the axis labels.
         */


        public XAxisValueFormatter(ArrayList<String> _list) {
            if (_list!= null)
                setValues(_list);
        }


        @Override
        public String getAxisLabel(float value, AxisBase axis){
            int index = Math.round(value);

            if (index < 0 || index >= mValueCount || index != (int)value)
                return String.valueOf(value);

            return getValues((int)value);
        }

        public String getValues(int index)
        {
            String tmp = mValues.get(index);
            try {
                String tmp2[] = tmp.split(" ");
                String tmp3[] = tmp2[1].split(":");
                String mytime = tmp3[0] + ":" + tmp3[1];
                return mytime;
            }catch (NullPointerException e )
            {
                e.printStackTrace();
            }
            return "";


//                String[] tmp1= DateTime_list.get(i).split(" ");
//                tmp1[1].split(":")            return mValues.get(index);
        }

        public void setValues(ArrayList<String> values)
        {
            if (values == null)
                values = new ArrayList<String>();

            this.mValues = values;
            this.mValueCount = values.size();
        }
    }

    

    private void drawBar(List<BarEntry> Anxietyentries,ArrayList<String> timelist)
    {
        ValueFormatter yformatter = new ValueFormatter(){
            @Override
            public String getAxisLabel(float value, AxisBase axis)
            {
                if(value<0.5)
                return  "나쁨";
                else
                     return "양호";
            }
        };

        BarDataSet barSet = new BarDataSet(Anxietyentries,"stress");
        BarData barData = new BarData(barSet);
        barData.setBarWidth(1.4f); // set custom bar width

        barSet.setDrawValues(false);
        barSet.setColor(getContext().getColor(R.color.panel2));
        barSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        ANXIETYChart.setData(barData);
        ANXIETYChart.setDragDecelerationEnabled(true);
        ANXIETYChart.setVisibleXRangeMaximum(30);

        Description des= ANXIETYChart.getDescription();
        des.setEnabled(false);

        YAxis y = ANXIETYChart.getAxisLeft();

        y.setTextColor(Color.BLACK);
        y.setSpaceTop(6);
        y.setSpaceBottom(0);

        y.setValueFormatter(yformatter);
        y.setLabelCount(2,true);

        YAxis y2 = ANXIETYChart.getAxisRight();
        y2.setEnabled(false);

        XAxis x = ANXIETYChart.getXAxis();
        x.setValueFormatter(new XAxisValueFormatter(timelist));
        x.setTextColor(Color.BLACK);
        x.setDrawGridLines(false);
        //x.setSpaceMin(1.9f);
      //  x.setXOffset(-1.9f);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend legend = ANXIETYChart.getLegend();
        legend.setEnabled(false);

        ANXIETYChart.animateXY(2000, 2000); //애니메이션 기능 활성화
        ANXIETYChart.setFitBars(false); // make the x-axis fit exactly all bars
        ANXIETYChart.invalidate(); // refresh


    }

    private void drawChart(ArrayList<Entry> entries,int flag,ArrayList<String> timelist)
    {

        //데이터
        LineDataSet lineDataSet = new LineDataSet(entries,flag+"test1");
        lineDataSet.setLineWidth(2.0f);
        lineDataSet.setDrawFilled(true); //선아래로 색상표시
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(3f);
        //데이터 셋
        LineChart lineChart = null;
        if(flag==1) {
            lineChart = HRChart;
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);//setDrawCubic(true);

            lineDataSet.setColor(getContext().getColor(R.color.myblue));
            lineDataSet.setFillColor(getContext().getColor(R.color.myblue));
            lineDataSet.setCircleColor(getContext().getColor(R.color.myblue));

            lineDataSet.setFillAlpha(50);

        }
        else if(flag==2) {
            lineChart = BRChart;
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);//setDrawCubic(true);

            lineDataSet.setColor(getContext().getColor(R.color.mygreen));
            lineDataSet.setFillColor(getContext().getColor(R.color.mygreen));
            lineDataSet.setCircleColor(getContext().getColor(R.color.mygreen));

            lineDataSet.setFillAlpha(50);

        }


        //데이터 셋


        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData); // set the data and list of lables into chart
        lineChart.setDragDecelerationEnabled(true);
        lineChart.setVisibleXRangeMaximum(30);


        Description des= lineChart.getDescription();
        des.setEnabled(false);

        YAxis y = lineChart.getAxisLeft();

        y.setTextColor(Color.BLACK);

        y.setSpaceBottom(0);
        y.setSpaceTop(0);
        //y축 값 범위
        if(flag==1) {
            y.setAxisMaximum(100);
            y.setAxisMinimum(50);
        }
        else {
            y.setAxisMaximum(900);
            y.setAxisMinimum(600);
        }
        YAxis y2 = lineChart.getAxisRight();
        y2.setEnabled(false);

        XAxis x = lineChart.getXAxis();
        //  x.setValueFormatter(formatter);
        x.setTextColor(Color.BLACK);
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setValueFormatter(new XAxisValueFormatter(timelist));
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
//
//        legend.setTextColor(Color.BLACK);

        lineChart.animateXY(2000, 2000); //애니메이션 기능 활성화
        lineChart.invalidate();
    }
}
