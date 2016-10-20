package com.example.eroland.mpu_ws_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eroland.mpu_ws_android.databinding.FragmentPlotBinding;
import com.example.eroland.mpu_ws_android.ui.DelegateBarChart;
import com.example.eroland.mpu_ws_android.ui.DelegateLineChart;
import com.example.eroland.mpu_ws_android.utils.ListDelegateAdapter;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class PlotFragment extends Fragment {
    private String ws_ip;
    private int ws_port;
    private int n_sensors;
    private WebSocket ws;
    private ArrayList<ChartData> list;

    private OnFragmentInteractionListener mListener;

    public PlotFragment() {
        // Required empty public constructor
    }

    public static PlotFragment newInstance() {
        return new PlotFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        ws_ip = sharedPref.getString(getString(R.string.wsIP), "0.0.0.0");
        ws_port = sharedPref.getInt(getString(R.string.wsPort), 3000);
        n_sensors = sharedPref.getInt(getString(R.string.nSensors), 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPlotBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plot,
                container, false);

        View view = binding.getRoot();

        list = new ArrayList<>();

        for (int i = 0; i < n_sensors; ++i) {
            list.add(generateLineData());
            //list.add(new LineData());
        }

        //list.add(generateData(5));

        ListDelegateAdapter adapter = new ListDelegateAdapter(list);

        adapter.registerDelegate(new DelegateLineChart());
        adapter.registerDelegate(new DelegateBarChart());

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.toolbar_connect:
                connectServer();
                return true;
            case R.id.toolbar_start_preview:
                ws.send("startPreview");
                return true;
            case R.id.toolbar_stop_preview:
                ws.send("stopPreview");
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


    public void connectServer() {
        String uri = "http://" + ws_ip + ":" + ws_port;
        AsyncHttpClient.getDefaultInstance().websocket(uri, "client", (ex, webSocket) -> {
            if (ex != null) {
                System.out.println("error");
                ex.printStackTrace();
                return;
            }
            ws = webSocket;
            //Toast.makeText(getActivity().getApplicationContext(), "Connected to server", Toast.LENGTH_SHORT).show();
            webSocket.setStringCallback(s -> {
                try{
                    int n = Integer.parseInt(s);
                    System.out.println(n);
                    ChartData d = list.get(0);
                    d.addEntry(new Entry(d.getEntryCount(),n),0);
                    d.notifyDataChanged();
                }catch (NumberFormatException nfe){
                    System.out.println("Could not parse " + nfe);
                }
            });
        });
    }

    private LineData generateLineData() {
        LineData lineData = new LineData();

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < 6; i++) {
            float mult = 10 / 2f;
            float val = (float) (Math.random() * mult) + 50;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals1.add(new Entry(i, val));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            float mult = 8;
            float val = (float) (Math.random() * mult) + 450;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals2.add(new Entry(i, val));
        }

        LineDataSet set1, set2;

        set1 = new LineDataSet(yVals1, "Acc X");

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        //set1.setFillFormatter(new MyFillFormatter(0f));
        //set1.setDrawHorizontalHighlightIndicator(false);
        //set1.setVisible(false);
        //set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        set2 = new LineDataSet(yVals2, "DataSet 2");
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(2f);
        set2.setCircleRadius(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        //set2.setFillFormatter(new MyFillFormatter(900f));

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        //dataSets.add(set2);

        return new LineData(dataSets);
    }

    private BarData generateData(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (float) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setBarShadowColor(Color.rgb(203, 203, 203));

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        sets.add(d);

        BarData cd = new BarData(sets);
        cd.setBarWidth(0.9f);
        return cd;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
