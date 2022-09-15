package com.heelab.bebrave;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class TrainingResultFrag extends Fragment {

    SeekBar AnxietyAnswer;
    TextView TotaltimeText;
    CircularSeekBar TotaltimeCircle;
    TextView BadactionText;
    CircularSeekBar BadactionCircle;
    EditText UserText;

    public TrainingResultFrag() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TrainingResultFrag newInstance(String param1, String param2) {
        TrainingResultFrag fragment = new TrainingResultFrag();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_training_result, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AnxietyAnswer = view.findViewById(R.id.anxietyanswer);
        TotaltimeText = view.findViewById(R.id.totaltimetext);
        TotaltimeCircle = view.findViewById(R.id.totaltimecircle);
        BadactionText = view.findViewById(R.id.badactiontext);
        BadactionCircle = view.findViewById(R.id.badactioncircle);
        UserText = view.findViewById(R.id.thoughtinput);
        Button donereport = (Button) view.findViewById(R.id.donereport);
        donereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int felt_anxiety = AnxietyAnswer.getProgress();
                String user_thought = UserText.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putBoolean("Flag",false);
                bundle.putInt("UserAnxiety",felt_anxiety);
                bundle.putString("UserThought",user_thought);
                Handler serviceHanlder = ((ForegroundService)ForegroundService.foregroundService).sendHandler;
               if( serviceHanlder!=null){
                   Message message= serviceHanlder.obtainMessage();
                   message.setData(bundle);
                   serviceHanlder.sendMessage(message);
               }
                
            }
        });
    }


}