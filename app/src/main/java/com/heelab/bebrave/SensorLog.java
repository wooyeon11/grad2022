package com.heelab.bebrave;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SensorLog extends Fragment {
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =inflater.inflate(R.layout.sensorlog, container, false);

        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.loglist);
         int size = ((MainActivity)MainActivity.context_main).SesnorLogString.size();
        if(size>0) {
            String[] statesList = new String[size];
            for (int i = 0; i < size; i++) {
                statesList[i] = ((MainActivity) MainActivity.context_main).SesnorLogString.get(i);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, statesList);
            listView.setAdapter(adapter);
            listView.setClickable(false);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    int itemPosition = position;
//                    String itemValue = (String) listView.getItemAtPosition(position);
//
////              Toast.makeText(getApplicationContext(),
////                "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
////                .show();
//                }
//            });
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(getActivity()!=null) getActivity().finish();
     }



}
