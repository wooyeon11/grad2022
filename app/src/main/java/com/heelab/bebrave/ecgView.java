package com.heelab.bebrave;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class ecgView extends Fragment {

    private LineChart chartE,chartX,chartY,chartZ;
    TextView ecgText,accxText, accyText,acczText;
    public Handler ecgGraphHandler;

     Thread drawgraphThread;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =inflater.inflate(R.layout.ecgview, container, false);
        
        drawgraphThread = new drawGraphThread();
        drawgraphThread.setDaemon(true);
        drawgraphThread.start() ;
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chartE = (LineChart ) view.findViewById(R.id.ECGChart);
        SettingChart(chartE,1);
        chartX = (LineChart) view.findViewById(R.id.ACCXChart);
        SettingChart(chartX,2);
        chartY= (LineChart) view.findViewById(R.id.ACCYChart);
        SettingChart(chartY,3);
        chartZ = (LineChart) view.findViewById(R.id.ACCZChart);
        SettingChart(chartZ,4);

        ecgText = view.findViewById(R.id.ecgText);
        accxText = view.findViewById(R.id.accxText);
        accyText = view.findViewById(R.id.accyText);
        acczText = view.findViewById(R.id.acczText);

    }


    class drawGraphThread extends Thread {

        long ecgduration =(long)1000/(long)128;
        long accduration = (long)128/(long)28;
        long acccount=0;
        
        @Override
        public void run()
        {
            Looper.prepare();
            ecgGraphHandler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    Bundle bundle = msg.getData();
                    int[] Ecglist = bundle.getIntArray("ECG");
                    int[] Accx = bundle.getIntArray("ACCX");
                    int[] Accy = bundle.getIntArray("ACCY");
                    int[] Accz = bundle.getIntArray("ACCZ");

                    StringBuilder ecgSB = new StringBuilder();
                    StringBuilder accxSB = new StringBuilder();
                    StringBuilder accySB = new StringBuilder();
                    StringBuilder acczSB = new StringBuilder();

                    if (getActivity() != null) {
                        if(Ecglist!= null) {
                            for (int i=0; i<Ecglist.length;i++) {
                                addEntry(Ecglist[i]);
                                ecgSB.append(Ecglist[i]);
                                ecgSB.append("/");
                            }
                        }

                        if(Accx!= null) {
                            for (int i=0; i<Accx.length;i++) {
                                addEntryAcc(chartX,Accx[i]);
                                accxSB.append(Accx[i]);
                                accxSB.append("/");
                            }
                        }
                        if(Accy!= null) {
                            for (int i=0; i<Accy.length;i++) {
                                addEntryAcc(chartY,Accy[i]);
                                accySB.append(Accy[i]);
                                accySB.append("/");
                            }
                        }
                        if(Accz!= null) {
                            for (int i=0; i<Accz.length;i++) {
                                addEntryAcc(chartZ,Accz[i]);
                                acczSB.append(Accz[i]);
                                acczSB.append("/");
                            }
                        }
                    }
                    // 텍스트업데이트
                    updateLabel(ecgText,ecgSB.toString());
                    updateLabel(accxText,accxSB.toString());
                    updateLabel(accyText,accySB.toString());
                    updateLabel(acczText,acczSB.toString());

                }
            };//define Handler
            Looper.loop();
        }//run method
    }//Thread class




    public void SettingChart(LineChart chart,int flag)
    {

        chart.setDrawGridBackground(true);
       // chart.setBackgroundColor(Color.WHITE);
        chart.setGridBackgroundColor(Color.WHITE);

// description text
//        chart.getDescription().setEnabled(true);
        Description des = chart.getDescription();//raw_Chart.getDescription();
        des.setEnabled(true);
        des.setTextSize(13f);

        if(getActivity()==null)
        {
            return;
        }
        switch (flag)
        {
            case 1://ECG
                des.setText("ECG");
                des.setTextColor(getActivity().getColor(R.color.ecgcolor));
                break;
            case 2: // ACCX
                des.setText("ACCX");
                des.setTextColor(getActivity().getColor(R.color.accxcolor));
                break;
            case 3: // ACCY
                des.setText("ACCY");
                des.setTextColor(getActivity().getColor(R.color.accycolor));
                break;
            case 4: // ACCZ
                des.setText("ACCZ");
                des.setTextColor(getActivity().getColor(R.color.acczcolor));
                break;

        }


// touch gestures (false-비활성화)
        chart.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

//auto scale
        chart.setAutoScaleMinMaxEnabled(true);

// if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//X축
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawAxisLine(false);

        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);

//Legend
        Legend l = chart.getLegend();
        l.setEnabled(false);
//        l.setFormSize(10f); // set the size of the legend forms/shapes
//        l.setTextSize(12f);
//        l.setTextColor(Color.WHITE);

//Y축
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.DKGRAY);
       // leftAxis (100);
        leftAxis.setLabelCount(6,true);
        if(flag!=1) {
            leftAxis.setAxisMaximum(600);
            leftAxis.setAxisMinimum(-600);
        }
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


// don't forget to refresh the drawing
        chart.invalidate();
    }
    private void updateLabel(TextView textView, String str)
    {
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                textView.setText(str);
            }
        });
    }
    private void addEntryAcc(LineChart lineChart, long num)
    {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                LineData data= lineChart.getData();

                if (data == null) {
                    data = new LineData();
                    lineChart.setData(data);
                }

                ILineDataSet set = data.getDataSetByIndex(0);


                if (set == null) {
                    set = createSet(lineChart);
                    data.addDataSet(set);
                }

                if (set != null)
                    data.addEntry(new Entry(set.getEntryCount(), num), 0);
                data.notifyDataChanged();


                lineChart.notifyDataSetChanged();
                lineChart.setVisibleXRangeMaximum(140);//4초동안
                // this automatically refreshes the chart (calls invalidate())
                lineChart.moveViewTo(data.getEntryCount(), 0f, YAxis.AxisDependency.LEFT);


            }

        });//uithread


    }

    private void addEntry(long num) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                LineData data = chartE.getData();

                if (data == null) {
                    data = new LineData();
                    chartE.setData(data);
                }


                ILineDataSet set = data.getDataSetByIndex(0);

                // set.addEntry(...); // can be called as well

                if (set == null) {
                    set = createSet(chartE);
                    data.addDataSet(set);
                }
                if(set!=null)
                    data.addEntry(new Entry((float)set.getEntryCount(), num), 0);
                data.notifyDataChanged();


                // let the chart know it's data has changed
                chartE.notifyDataSetChanged();

                chartE.setVisibleXRangeMaximum(640);//4초동안
                // this automatically refreshes the chart (calls invalidate())
                chartE.moveViewTo(data.getEntryCount(), 0f, YAxis.AxisDependency.LEFT);

            }

        });//uithread

    }

    private LineDataSet createSet(LineChart lineChart) {

        LineDataSet set=null;

        if(getActivity()==null) return null;
        set = new LineDataSet(null, lineChart.getDescription().getText());

        set.setValueTextColor(lineChart.getDescription().getTextColor());
        set.setColor(lineChart.getDescription().getTextColor());

        if(set==null) return null;
        set.setLineWidth(1f);
        set.setDrawValues(false);

        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }


    @Override
    public void onPause() {
        super.onPause();

        if(getActivity()!=null) getActivity().finish();

    }

 }
