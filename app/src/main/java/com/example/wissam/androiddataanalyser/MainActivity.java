package com.example.wissam.androiddataanalyser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private RecyclerView                mRecyclerView;
    private MyAdapter                   mAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;
    private LineGraphSeries<DataPoint>  mSeries;
    private GraphView                   graph;
    private SqlManager                  dataBdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBdd = new SqlManager(this);  //create bdd

        ArrayList<DataPoint> allData;
        allData = dataBdd.getAllData();  //get all data from bdd


        graph = (GraphView) findViewById(R.id.graph);
        mSeries = new LineGraphSeries<DataPoint>(allData.toArray(new DataPoint[allData.size()])); //put all data from bdd on graph
        graph.addSeries(mSeries);

        // enable scaling and scrolling graph
        setViewPort();
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(allData);
        mRecyclerView.setAdapter(mAdapter);

        getData(); //get the data coming from the raspberry python programme

    }
    public int byteArrayStringEnd(byte[] byteArray) {
        int i = 0;
        while (byteArray[i] != '\0') {
            ++i;
        }
        return i;
    }
    public void setViewPort(){
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(mSeries.getHighestValueX()+1);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(mSeries.getHighestValueY()+1);
    }
    //get the data coming from the raspberry python programme
    public void getData() {
        new Thread(new Runnable() {
            public void run() {
                boolean notConnected = true;
                while(notConnected) {
                    try {
                        notConnected = false;
                        Socket socket = new Socket("192.168.43.193", 15550);

                        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                        dOut.writeUTF(String.valueOf(mSeries.getHighestValueX()));
                        dOut.flush(); // Send off the data

                        int messageNumber = 0;
                        while (messageNumber++ < 10) {
                            DataInputStream DIS = new DataInputStream(socket.getInputStream());
                            byte[] rawMessage = new byte[1024];
                            DIS.read(rawMessage);
                            String message = new String(rawMessage, 0, byteArrayStringEnd(rawMessage), StandardCharsets.UTF_8);
                            String[] split = message.split(";");
                            final Double number = Double.parseDouble(split[0]);
                            final Double time = Double.parseDouble(split[1]) / 1000;
                            Log.i("THREAD", "Number : " + String.valueOf(number) + " | " + "TIME : " + String.valueOf(time));
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    mSeries.appendData(new DataPoint(number, time), true, 40 );
                                    dataBdd.open();
                                    dataBdd.insertData(new Data(number, time));
                                    dataBdd.close();
                                    setViewPort();

                                    mAdapter.addItem(time, number); //add new element real time
                                }

                            });
                        }
                        socket.close();
                    } catch (IOException e) {
                        notConnected = true;

                    }
                }
            }
        }).start();
    }
}
