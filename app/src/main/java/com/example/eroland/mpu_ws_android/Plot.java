package com.example.eroland.mpu_ws_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.eroland.mpu_ws_android.databinding.FragmentPlotBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Plot.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Plot#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Plot extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private  String ws_ip;
    private int ws_port;
    private int nSensors;

    private OnFragmentInteractionListener mListener;

    public Plot() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Plot.
     */
    // TODO: Rename and change types and number of parameters
    public static Plot newInstance(String param1, String param2) {
        Plot fragment = new Plot();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_plot, container, false);
        FragmentPlotBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plot, container, false);
        View view = binding.getRoot();
        ListView lv = binding.listView;
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        ws_ip = sharedPref.getString(getString(R.string.wsIP), "0.0.0.0");
        ws_port = sharedPref.getInt(getString(R.string.wsPort), 3000);
        nSensors = sharedPref.getInt(getString(R.string.nSensors), 1);
        ArrayList<LineData> list = new ArrayList<LineData>();
        for(int i = 0; i < nSensors; i++){
            list.add(new LineData());
        }
        CharDataAdapter charDataAdapter = new CharDataAdapter(getContext(), list);
        lv.setAdapter(charDataAdapter);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()){
            case R.id.toolbar_connect:
                connectServer();
                return true;
            //TODO Poner los demas items
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void connectServer() {
        String uri = "http://"+ws_ip+":"+ws_port;
        AsyncHttpClient.getDefaultInstance().websocket(uri, "client", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    System.out.println("error");
                    ex.printStackTrace();
                    return;
                }
                webSocket.setStringCallback(s -> {
                    try {
                        JSONObject jObject = new JSONObject(s);
                        JSONArray jLectures = jObject.getJSONArray("lectures");
                        for (int i = 0; i < jLectures.length(); i++) {
                            System.out.println(jLectures.get(i));
                            //TODO Graficar las lectuas en los plots
                        }
                    } catch (JSONException e) {
                        //TODO
                    }
                });
            }
        });
    }

    private class CharDataAdapter extends ArrayAdapter<LineData>{
        public CharDataAdapter(Context context, List<LineData> objects){
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LineData data = getItem(position);
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_linechart, null);
                viewHolder.chart = (LineChart) convertView.findViewById(R.id.chart);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // apply styling
            data.setValueTextColor(Color.BLACK);
            viewHolder.chart.setDrawGridBackground(false);

            XAxis xAxis = viewHolder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);

            YAxis leftAxis = viewHolder.chart.getAxisLeft();
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);

            YAxis rightAxis = viewHolder.chart.getAxisRight();
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(15f);

            // set data
            //viewHolder.chart.setData(data);

            // do not forget to refresh the chart
//            holder.chart.invalidate();
            viewHolder.chart.animateY(700);

            return convertView;
        }

        private class ViewHolder {

            LineChart chart;
        }
    }
}
