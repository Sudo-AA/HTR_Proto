package com.example.htr_proto;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import static java.lang.Math.floor;
import static java.lang.Math.round;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private LineChart mchartx;
    private LineChart mcharty;
    private LineChart mchartz;
    private Button connect, calibrate, rec, back, pos_ready, res_ready, act_ready, pos_bot, res_bot, act_bot, about_back, mes_back, help_mes, righth, lefth;
    private RelativeLayout RecWin, CalWin, WelWin, pos_re, res_re, act_re, about_us, mes, hprefer;
    private LinearLayout pos_lay, res_lay, act_lay;
    private ImageView about;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    RecyclerView reclist;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    ProgressBar pb, con_prog, handprog;
    TextView comp, vb, tr;
    String signal;
    String receive;
    String vibrate;
    String tremor;
    String reading;
    String freq;
    String datetime = getDateTime();
    Jata dbdata;
    float[] pfildatax = new float[]{};
    float[] pfildatay = new float[]{};
    float[] pfildataz = new float[]{};
    float[] rfildatax = new float[]{};
    float[] rfildatay = new float[]{};
    float[] rfildataz = new float[]{};
    float[] afildatax = new float[]{};
    float[] afildatay = new float[]{};
    float[] afildataz = new float[]{};
    ArrayList<String> rec_id, rec_datetime, rec_tremor, rec_vibrate;
    ListAdpater listAdpater;
    int rotator = 0;
    int graphchanger = 0;
    boolean bolcon;
    int rerun = 0;
    int revolver = 0;
    boolean bt = false;
    boolean shitter = false;

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WelWin = (RelativeLayout) findViewById(R.id.welcome);
        CalWin = (RelativeLayout) findViewById(R.id.calibration);
        RecWin = (RelativeLayout) findViewById(R.id.records);
        pos_re = (RelativeLayout) findViewById(R.id.postural);
        res_re = (RelativeLayout) findViewById(R.id.resting);
        act_re = (RelativeLayout) findViewById(R.id.action);
        hprefer = (RelativeLayout) findViewById(R.id.handprefer);
        about_us = (RelativeLayout) findViewById(R.id.about_us);
        mes = (RelativeLayout) findViewById(R.id.about_measure);
        pos_lay = (LinearLayout) findViewById(R.id.postural_graph);
        res_lay = (LinearLayout) findViewById(R.id.resting_graph);
        act_lay = (LinearLayout) findViewById(R.id.action_graph);
        connect = (Button) findViewById(R.id.connect_btn);
        calibrate = (Button) findViewById(R.id.calibrate);
        rec = (Button) findViewById(R.id.goto_record);
        back = (Button) findViewById(R.id.back_btn);
        pos_ready = (Button) findViewById(R.id.ready_pos);
        res_ready = (Button) findViewById(R.id.ready_res);
        act_ready = (Button) findViewById(R.id.ready_act);
        pos_bot = (Button) findViewById(R.id.pos_button);
        res_bot = (Button) findViewById(R.id.res_button);
        act_bot = (Button) findViewById(R.id.act_button);
        about_back = (Button) findViewById(R.id.about_back);
        help_mes = (Button) findViewById(R.id.measure);
        mes_back = (Button) findViewById(R.id.measure_back);
        righth = (Button) findViewById(R.id.righthand);
        lefth = (Button) findViewById(R.id.lefthand);
        vb = (TextView) findViewById(R.id.vibration);
        tr = (TextView) findViewById(R.id.tremor);
        comp = (TextView) findViewById(R.id.comp);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        con_prog = (ProgressBar) findViewById(R.id.progressBar2);
        handprog = (ProgressBar) findViewById(R.id.progressBar3);
        reclist = (RecyclerView) findViewById(R.id.recordList);
        signal = null;
        receive = null;
        dbdata = new Jata(MainActivity.this);
        rec_id = new ArrayList<String>();
        rec_datetime = new ArrayList<String>();
        rec_tremor = new ArrayList<String>();
        rec_vibrate = new ArrayList<String>();
        about = (ImageView) findViewById(R.id.about);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con_prog.setVisibility(ProgressBar.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findBT();
                            openBT();
                            WelWin.setVisibility(RelativeLayout.GONE);
                            CalWin.setVisibility(RelativeLayout.GONE);
                            RecWin.setVisibility(RelativeLayout.GONE);
                            hprefer.setVisibility(RelativeLayout.VISIBLE);
                            Toast.makeText(getApplicationContext(), "SUCCESSFULLY CONNECTED", Toast.LENGTH_LONG).show();
                            bolcon = true;
                            bt = true;
                            con_prog.setVisibility(ProgressBar. INVISIBLE);
                        } catch (IOException ex) {
                            alertdialog();

                        }
                    }
                }, 2500);



            }
        });
        calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalWin.setVisibility(RelativeLayout.GONE);
                WelWin.setVisibility(RelativeLayout.GONE);
                RecWin.setVisibility(RelativeLayout.GONE);
                pos_re.setVisibility(RelativeLayout.VISIBLE);
                res_re.setVisibility(RelativeLayout.GONE);
                act_re.setVisibility(RelativeLayout.GONE);
                calibrate.setClickable(false);
                pb.setVisibility(ProgressBar.VISIBLE);
                if (revolver == 1) {
                    try {
                        pfildatax = new float[0];
                        pfildatay = new float[0];
                        pfildataz = new float[0];
                        rfildatax = new float[0];
                        rfildatay = new float[0];
                        rfildataz = new float[0];
                        afildatax = new float[0];
                        afildatay = new float[0];
                        afildataz = new float[0];
                        graphchanger = 0;
                        tr.setText("HAND TREMOR : ");
                        vb.setText("VIBRATION OUTPUT : ");
                        signal = "6666";
                        sendData();
                    } catch (IOException ex) {

                    }

                }


            }
        });
        righth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handprog.setVisibility(ProgressBar.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            signal = "101";
                            sendData();
                            WelWin.setVisibility(RelativeLayout.GONE);
                            CalWin.setVisibility(RelativeLayout.VISIBLE);
                            RecWin.setVisibility(RelativeLayout.GONE);
                            hprefer.setVisibility(RelativeLayout.GONE);
                            handprog.setVisibility(ProgressBar.INVISIBLE);
                        } catch (IOException ex) {

                        }
                    }
                }, 2000);

            }
        });
        lefth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handprog.setVisibility(ProgressBar.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            signal = "102";
                            sendData();
                            WelWin.setVisibility(RelativeLayout.GONE);
                            CalWin.setVisibility(RelativeLayout.VISIBLE);
                            RecWin.setVisibility(RelativeLayout.GONE);
                            hprefer.setVisibility(RelativeLayout.GONE);
                            handprog.setVisibility(ProgressBar.INVISIBLE);
                        } catch (IOException ex) {

                        }
                    }
                }, 2000);

            }
        });

        pos_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signal = "1111";
                    sendData();
                    CalWin.setVisibility(RelativeLayout.VISIBLE);
                    WelWin.setVisibility(RelativeLayout.GONE);
                    RecWin.setVisibility(RelativeLayout.GONE);
                    pos_re.setVisibility(RelativeLayout.GONE);
                    pos_lay.setVisibility(LinearLayout.VISIBLE);
                    res_lay.setVisibility(LinearLayout.GONE);
                    act_lay.setVisibility(LinearLayout.GONE);
                    pos_bot.setClickable(false);
                    res_bot.setClickable(true);
                    act_bot.setClickable(true);
                    pos_bot.setTextColor(WHITE);
                    res_bot.setTextColor(BLACK);
                    act_bot.setTextColor(BLACK);
                    pos_bot.setBackgroundColor(BLACK);
                    res_bot.setBackgroundColor(WHITE);
                    act_bot.setBackgroundColor(WHITE);

                } catch (IOException ex) {

                }

            }
        });
        res_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signal = "2222";
                    sendData();
                    CalWin.setVisibility(RelativeLayout.VISIBLE);
                    WelWin.setVisibility(RelativeLayout.GONE);
                    RecWin.setVisibility(RelativeLayout.GONE);
                    res_re.setVisibility(RelativeLayout.GONE);
                    pos_lay.setVisibility(LinearLayout.GONE);
                    res_lay.setVisibility(LinearLayout.VISIBLE);
                    act_lay.setVisibility(LinearLayout.GONE);
                    pos_bot.setClickable(true);
                    res_bot.setClickable(false);
                    act_bot.setClickable(true);
                    pos_bot.setTextColor(BLACK);
                    res_bot.setTextColor(WHITE);
                    act_bot.setTextColor(BLACK);
                    pos_bot.setBackgroundColor(WHITE);
                    res_bot.setBackgroundColor(BLACK);
                    act_bot.setBackgroundColor(WHITE);

                } catch (IOException ex) {

                }

            }
        });
        act_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signal = "3333";
                    sendData();
                    CalWin.setVisibility(RelativeLayout.VISIBLE);
                    WelWin.setVisibility(RelativeLayout.GONE);
                    RecWin.setVisibility(RelativeLayout.GONE);
                    act_re.setVisibility(RelativeLayout.GONE);
                    pos_lay.setVisibility(LinearLayout.GONE);
                    res_lay.setVisibility(LinearLayout.GONE);
                    act_lay.setVisibility(LinearLayout.VISIBLE);
                    pos_bot.setClickable(true);
                    res_bot.setClickable(true);
                    act_bot.setClickable(false);
                    pos_bot.setTextColor(BLACK);
                    res_bot.setTextColor(BLACK);
                    act_bot.setTextColor(WHITE);
                    pos_bot.setBackgroundColor(WHITE);
                    res_bot.setBackgroundColor(WHITE);
                    act_bot.setBackgroundColor(BLACK);


                } catch (IOException ex) {

                }

            }
        });
        pos_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos_lay.setVisibility(LinearLayout.VISIBLE);
                res_lay.setVisibility(LinearLayout.GONE);
                act_lay.setVisibility(LinearLayout.GONE);
                pos_bot.setClickable(false);
                res_bot.setClickable(true);
                act_bot.setClickable(true);
                pos_bot.setTextColor(WHITE);
                res_bot.setTextColor(BLACK);
                act_bot.setTextColor(BLACK);
                pos_bot.setBackgroundColor(BLACK);
                res_bot.setBackgroundColor(WHITE);
                act_bot.setBackgroundColor(WHITE);
            }
        });
        res_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos_lay.setVisibility(LinearLayout.GONE);
                res_lay.setVisibility(LinearLayout.VISIBLE);
                act_lay.setVisibility(LinearLayout.GONE);
                pos_bot.setClickable(true);
                res_bot.setClickable(false);
                act_bot.setClickable(true);
                pos_bot.setTextColor(BLACK);
                res_bot.setTextColor(WHITE);
                act_bot.setTextColor(BLACK);
                pos_bot.setBackgroundColor(WHITE);
                res_bot.setBackgroundColor(BLACK);
                act_bot.setBackgroundColor(WHITE);
            }
        });
        act_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos_lay.setVisibility(LinearLayout.GONE);
                res_lay.setVisibility(LinearLayout.GONE);
                act_lay.setVisibility(LinearLayout.VISIBLE);
                pos_bot.setClickable(true);
                res_bot.setClickable(true);
                act_bot.setClickable(false);
                pos_bot.setTextColor(BLACK);
                res_bot.setTextColor(BLACK);
                act_bot.setTextColor(WHITE);
                pos_bot.setBackgroundColor(WHITE);
                res_bot.setBackgroundColor(WHITE);
                act_bot.setBackgroundColor(BLACK);
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelWin.setVisibility(RelativeLayout.GONE);
                CalWin.setVisibility(RelativeLayout.GONE);
                RecWin.setVisibility(RelativeLayout.VISIBLE);
                displayData();
                init_tremor_rec();
                // Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bolcon == true) {
                    WelWin.setVisibility(RelativeLayout.GONE);
                    CalWin.setVisibility(RelativeLayout.VISIBLE);
                    RecWin.setVisibility(RelativeLayout.GONE);

                } else if (bolcon == false) {
                    WelWin.setVisibility(RelativeLayout.VISIBLE);
                    CalWin.setVisibility(RelativeLayout.GONE);
                    RecWin.setVisibility(RelativeLayout.GONE);
                }
            }

        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalWin.setVisibility(RelativeLayout.GONE);
                WelWin.setVisibility(RelativeLayout.GONE);
                RecWin.setVisibility(RelativeLayout.GONE);
                pos_re.setVisibility(RelativeLayout.GONE);
                res_re.setVisibility(RelativeLayout.GONE);
                act_re.setVisibility(RelativeLayout.GONE);
                about_us.setVisibility(RelativeLayout.VISIBLE);
            }

        });
        about_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalWin.setVisibility(RelativeLayout.GONE);
                WelWin.setVisibility(RelativeLayout.VISIBLE);
                RecWin.setVisibility(RelativeLayout.GONE);
                pos_re.setVisibility(RelativeLayout.GONE);
                res_re.setVisibility(RelativeLayout.GONE);
                act_re.setVisibility(RelativeLayout.GONE);
                about_us.setVisibility(RelativeLayout.GONE);
            }

        });
        mes_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecWin.setVisibility(RelativeLayout.VISIBLE);
                mes.setVisibility(RelativeLayout.GONE);
            }

        });
        help_mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecWin.setVisibility(RelativeLayout.GONE);
                mes.setVisibility(RelativeLayout.VISIBLE);

            }

        });
    }

    void findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "NO BLUETOOTH ADAPTER", Toast.LENGTH_LONG).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("HAND_TREMOR_REDUCER_PRO")) {
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[65536];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            receive = data;
                                            if (receive != null) {
                                                result();
                                                rerun = 0;
                                            }


                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
                dis_exit();
            }

        });

        workerThread.start();
    }

    void sendData() throws IOException {
        String msg = signal;
        msg += "\n";
        mmOutputStream.write(msg.getBytes());

    }

    void closeBT() throws IOException {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        Toast.makeText(getApplicationContext(), "BLUETOOTH DISCONNECTED", Toast.LENGTH_LONG).show();
    }

    void result() {
        if (receive.equals("A")) {
            reading = "Postural tremor";
            pos_lay.setVisibility(LinearLayout.VISIBLE);
            res_lay.setVisibility(LinearLayout.GONE);
            act_lay.setVisibility(LinearLayout.GONE);
            pos_bot.setClickable(false);
            res_bot.setClickable(true);
            act_bot.setClickable(true);
            pos_bot.setTextColor(WHITE);
            res_bot.setTextColor(BLACK);
            act_bot.setTextColor(BLACK);
            pos_bot.setBackgroundColor(BLACK);
            res_bot.setBackgroundColor(WHITE);
            act_bot.setBackgroundColor(WHITE);

        } else if (receive.equals("B")) {
            reading = "Resting tremor";
            pos_lay.setVisibility(LinearLayout.GONE);
            res_lay.setVisibility(LinearLayout.VISIBLE);
            act_lay.setVisibility(LinearLayout.GONE);
            pos_bot.setClickable(true);
            res_bot.setClickable(false);
            act_bot.setClickable(true);
            pos_bot.setTextColor(BLACK);
            res_bot.setTextColor(WHITE);
            act_bot.setTextColor(BLACK);
            pos_bot.setBackgroundColor(WHITE);
            res_bot.setBackgroundColor(BLACK);
            act_bot.setBackgroundColor(WHITE);

        } else if (receive.equals("C")) {
            reading = "Action tremor";
            pos_lay.setVisibility(LinearLayout.GONE);
            res_lay.setVisibility(LinearLayout.GONE);
            act_lay.setVisibility(LinearLayout.VISIBLE);
            pos_bot.setClickable(true);
            res_bot.setClickable(true);
            act_bot.setClickable(false);
            pos_bot.setTextColor(BLACK);
            res_bot.setTextColor(BLACK);
            act_bot.setTextColor(WHITE);
            pos_bot.setBackgroundColor(WHITE);
            res_bot.setBackgroundColor(WHITE);
            act_bot.setBackgroundColor(BLACK);

        } else if (receive.equals("A1")) {
            pb.setVisibility(ProgressBar.INVISIBLE);
            comp.setText("Calibration Complete ");
            comp.setVisibility(View.VISIBLE);
            tremor = reading + " with average frequency of " + freq + " per second and low amplitude";
            vibrate = freq + " of frequency with low vibration pulse";
            tr.setText("HAND TREMOR : " + tremor);
            vb.setText("VIBRATION OUTPUT : " + vibrate);
            add_data();
        } else if (receive.equals("B2")) {
            pb.setVisibility(ProgressBar.INVISIBLE);
            comp.setText("Calibration Complete ");
            comp.setVisibility(View.VISIBLE);
            tremor = reading + " with average frequency of " + freq + " per second and medium amplitude";
            vibrate = freq + " of frequency with medium vibration pulse";
            tr.setText("HAND TREMOR : " + tremor);
            vb.setText("VIBRATION OUTPUT : " + vibrate);
            add_data();
        } else if (receive.equals("C3")) {
            pb.setVisibility(ProgressBar.INVISIBLE);
            comp.setText("Calibration Complete ");
            comp.setVisibility(View.VISIBLE);
            tremor = reading + " with average frequency of " + freq + " per second and high amplitude";
            vibrate = freq + " of frequency with high vibration pulse";
            tr.setText("HAND TREMOR : " + tremor);
            vb.setText("VIBRATION OUTPUT : " + vibrate);
            add_data();
        } else if (receive.equals("EX")) {
            pb.setVisibility(ProgressBar.INVISIBLE);
            comp.setText("Calibration Complete ");
            comp.setVisibility(View.VISIBLE);
            tremor = "No Abnormal Hand Tremor detected";
            vibrate = "No vibration output";
            tr.setText("HAND TREMOR : " + tremor);
            vb.setText("VIBRATION OUTPUT : " + vibrate);
            add_data();
            normal_exit();
        } else if (receive.equals("O")) {
            pb.setVisibility(ProgressBar.INVISIBLE);
            comp.setText("ALL THE PROCESSES ARE DONE :)");
            comp.setVisibility(View.VISIBLE);
            finish_cal();
            cal_state();
        } else if (receive.equals("G")) {
            shitter = true;
            graphchanger = 999;
        } else if (receive.equals("X1")) {
            graphchanger = 1;
            CalWin.setVisibility(RelativeLayout.GONE);
            WelWin.setVisibility(RelativeLayout.GONE);
            RecWin.setVisibility(RelativeLayout.GONE);
            pos_re.setVisibility(RelativeLayout.GONE);
            res_re.setVisibility(RelativeLayout.VISIBLE);
            act_re.setVisibility(RelativeLayout.GONE);
        } else if (receive.equals("X2")) {
            graphchanger = 2;
            CalWin.setVisibility(RelativeLayout.GONE);
            WelWin.setVisibility(RelativeLayout.GONE);
            RecWin.setVisibility(RelativeLayout.GONE);
            pos_re.setVisibility(RelativeLayout.GONE);
            res_re.setVisibility(RelativeLayout.GONE);
            act_re.setVisibility(RelativeLayout.VISIBLE);
        } else {
            comp.setVisibility(View.VISIBLE);
            float fil = Float.parseFloat(receive);
            if (graphchanger == 0) {
                if (rotator == 0) {
                    pfildatax = Arrays.copyOf(pfildatax, pfildatax.length + 1);
                    comp.setText("FETCHING DATA ... . . . .");
                    int length = pfildatax.length;
                    pfildatax[length - 1] = fil;
                    rotator = 1;
                } else if (rotator == 1) {
                    pfildatay = Arrays.copyOf(pfildatay, pfildatay.length + 1);
                    comp.setText("FETCHING DATA ..... . . . . . .");
                    int length = pfildatay.length;
                    pfildatay[length - 1] = fil;
                    rotator = 2;
                } else if (rotator == 2) {
                    pfildataz = Arrays.copyOf(pfildataz, pfildataz.length + 1);
                    comp.setText("FETCHING DATA .........");
                    int length = pfildataz.length;
                    pfildataz[length - 1] = fil;
                    rotator = 0;
                }
                pos_graph();
            } else if (graphchanger == 1) {
                if (rotator == 0) {
                    rfildatax = Arrays.copyOf(rfildatax, rfildatax.length + 1);
                    comp.setText("FETCHING DATA ... . . . .");
                    int length = rfildatax.length;
                    rfildatax[length - 1] = fil;
                    rotator = 1;
                } else if (rotator == 1) {
                    rfildatay = Arrays.copyOf(rfildatay, rfildatay.length + 1);
                    comp.setText("FETCHING DATA ..... . . . . . .");
                    int length = rfildatay.length;
                    rfildatay[length - 1] = fil;
                    rotator = 2;
                } else if (rotator == 2) {
                    rfildataz = Arrays.copyOf(rfildataz, rfildataz.length + 1);
                    comp.setText("FETCHING DATA .........");
                    int length = rfildataz.length;
                    rfildataz[length - 1] = fil;
                    rotator = 0;
                }
                res_graph();
            } else if (graphchanger == 2) {
                if (rotator == 0) {
                    afildatax = Arrays.copyOf(afildatax, afildatax.length + 1);
                    comp.setText("FETCHING DATA ... . . . .");
                    int length = afildatax.length;
                    afildatax[length - 1] = fil;
                    rotator = 1;
                } else if (rotator == 1) {
                    afildatay = Arrays.copyOf(afildatay, afildatay.length + 1);
                    comp.setText("FETCHING DATA ..... . . . . . .");
                    int length = afildatay.length;
                    afildatay[length - 1] = fil;
                    rotator = 2;
                } else if (rotator == 2) {
                    afildataz = Arrays.copyOf(afildataz, afildataz.length + 1);
                    comp.setText("FETCHING DATA .........");
                    int length = afildataz.length;
                    afildataz[length - 1] = fil;
                    rotator = 0;
                }
                act_graph();
            }else if(shitter == true){
                freq = Float.toString(round(fil)) + "hz";
                shitter = false;
            }
        }

    }

    void add_data() {
        RecordModel recordModel;
        try {
            recordModel = new RecordModel(-1, datetime, tremor, vibrate);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "RECORD FAILED !!!", Toast.LENGTH_SHORT).show();
            recordModel = new RecordModel(-1, datetime, "ERROR", "OCCUR");
        }

        dbdata = new Jata(MainActivity.this);

        boolean success = dbdata.addone(recordModel);
        Toast.makeText(MainActivity.this, " RECORD STATUS : " + success, Toast.LENGTH_SHORT).show();
        pb.setVisibility(ProgressBar.INVISIBLE);
    }

    void pos_graph() {
        mchartx = (LineChart) findViewById(R.id.plinechartx);
        mchartx.setDragEnabled(true);
        mchartx.setScaleEnabled(true);

        mcharty = (LineChart) findViewById(R.id.plinecharty);
        mcharty.setDragEnabled(true);
        mcharty.setScaleEnabled(true);

        mchartz = (LineChart) findViewById(R.id.plinechartz);
        mchartz.setDragEnabled(true);
        mchartz.setScaleEnabled(true);

        mchartx.getAxisRight().setEnabled(false);
        mcharty.getAxisRight().setEnabled(false);
        mchartz.getAxisRight().setEnabled(false);

        mchartx.getAxisLeft().setAxisMinValue(-0.009f);
        mcharty.getAxisLeft().setAxisMinValue(-0.009f);
        mchartz.getAxisLeft().setAxisMinValue(-0.009f);
        mchartx.getAxisLeft().setAxisMaxValue(0.009f);
        mcharty.getAxisLeft().setAxisMaxValue(0.009f);
        mchartz.getAxisLeft().setAxisMaxValue(0.009f);

        ArrayList<Entry> xValues = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<Entry> zValues = new ArrayList<>();


        int x = 0;
        while (pfildatax.length > x) {
            xValues.add(new Entry(x, pfildatax[x]));
            x++;
        }
        int y = 0;
        while (pfildatay.length > y) {
            yValues.add(new Entry(y, pfildatay[y]));
            y++;
        }
        int z = 0;
        while (pfildataz.length > z) {
            zValues.add(new Entry(z, pfildataz[z]));
            z++;
        }
        LineDataSet filteredx = new LineDataSet(xValues, "X AXIS DATA");
        filteredx.setFillAlpha(10);
        filteredx.setDrawCircles(false);
        filteredx.setColor(Color.BLACK);
        filteredx.setLineWidth(0.5f);
        filteredx.setValueTextSize(0f);

        LineDataSet filteredy = new LineDataSet(yValues, "Y AXIS DATA");
        filteredy.setFillAlpha(10);
        filteredy.setDrawCircles(false);
        filteredy.setColor(Color.BLACK);
        filteredy.setLineWidth(0.5f);
        filteredy.setValueTextSize(0f);

        LineDataSet filteredz = new LineDataSet(zValues, "Z AXIS DATA");
        filteredz.setFillAlpha(10);
        filteredz.setDrawCircles(false);
        filteredz.setColor(Color.BLACK);
        filteredz.setLineWidth(0.5f);
        filteredz.setValueTextSize(0f);


        ArrayList<ILineDataSet> dataSetsx = new ArrayList<>();
        dataSetsx.add(filteredx);
        ArrayList<ILineDataSet> dataSetsy = new ArrayList<>();
        dataSetsy.add(filteredy);
        ArrayList<ILineDataSet> dataSetsz = new ArrayList<>();
        dataSetsz.add(filteredz);
        mchartx.getDescription().setText("HAND TREMOR READING");
        LineData[] datax = {new LineData(dataSetsx)};
        mchartx.setData(datax[0]);
        mchartx.invalidate();

        mcharty.getDescription().setText("HAND TREMOR READING");
        LineData[] datay = {new LineData(dataSetsy)};
        mcharty.setData(datay[0]);
        mcharty.invalidate();

        mchartz.getDescription().setText("HAND TREMOR READING");
        LineData[] dataz = {new LineData(dataSetsz)};
        mchartz.setData(dataz[0]);
        mchartz.invalidate();

    }

    void res_graph() {
        mchartx = (LineChart) findViewById(R.id.rlinechartx);
        mchartx.setDragEnabled(true);
        mchartx.setScaleEnabled(true);

        mcharty = (LineChart) findViewById(R.id.rlinecharty);
        mcharty.setDragEnabled(true);
        mcharty.setScaleEnabled(true);

        mchartz = (LineChart) findViewById(R.id.rlinechartz);
        mchartz.setDragEnabled(true);
        mchartz.setScaleEnabled(true);

        mchartx.getAxisRight().setEnabled(false);
        mcharty.getAxisRight().setEnabled(false);
        mchartz.getAxisRight().setEnabled(false);

        mchartx.getAxisLeft().setAxisMinValue(-0.009f);
        mcharty.getAxisLeft().setAxisMinValue(-0.009f);
        mchartz.getAxisLeft().setAxisMinValue(-0.009f);
        mchartx.getAxisLeft().setAxisMaxValue(0.009f);
        mcharty.getAxisLeft().setAxisMaxValue(0.009f);
        mchartz.getAxisLeft().setAxisMaxValue(0.009f);

        ArrayList<Entry> xValues = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<Entry> zValues = new ArrayList<>();


        int x = 0;
        while (rfildatax.length > x) {
            xValues.add(new Entry(x, rfildatax[x]));
            x++;
        }
        int y = 0;
        while (rfildatay.length > y) {
            yValues.add(new Entry(y, rfildatay[y]));
            y++;
        }
        int z = 0;
        while (rfildataz.length > z) {
            zValues.add(new Entry(z, rfildataz[z]));
            z++;
        }
        LineDataSet filteredx = new LineDataSet(xValues, "X AXIS DATA");
        filteredx.setFillAlpha(10);
        filteredx.setDrawCircles(false);
        filteredx.setColor(Color.BLACK);
        filteredx.setLineWidth(0.5f);
        filteredx.setValueTextSize(0f);

        LineDataSet filteredy = new LineDataSet(yValues, "Y AXIS DATA");
        filteredy.setFillAlpha(10);
        filteredy.setDrawCircles(false);
        filteredy.setColor(Color.BLACK);
        filteredy.setLineWidth(0.5f);
        filteredy.setValueTextSize(0f);

        LineDataSet filteredz = new LineDataSet(zValues, "Z AXIS DATA");
        filteredz.setFillAlpha(10);
        filteredz.setDrawCircles(false);
        filteredz.setColor(Color.BLACK);
        filteredz.setLineWidth(0.5f);
        filteredz.setValueTextSize(0f);


        ArrayList<ILineDataSet> dataSetsx = new ArrayList<>();
        dataSetsx.add(filteredx);
        ArrayList<ILineDataSet> dataSetsy = new ArrayList<>();
        dataSetsy.add(filteredy);
        ArrayList<ILineDataSet> dataSetsz = new ArrayList<>();
        dataSetsz.add(filteredz);
        mchartx.getDescription().setText("HAND TREMOR READING");
        LineData[] datax = {new LineData(dataSetsx)};
        mchartx.setData(datax[0]);
        mchartx.invalidate();

        mcharty.getDescription().setText("HAND TREMOR READING");
        LineData[] datay = {new LineData(dataSetsy)};
        mcharty.setData(datay[0]);
        mcharty.invalidate();

        mchartz.getDescription().setText("HAND TREMOR READING");
        LineData[] dataz = {new LineData(dataSetsz)};
        mchartz.setData(dataz[0]);
        mchartz.invalidate();

    }

    void act_graph() {
        mchartx = (LineChart) findViewById(R.id.alinechartx);
        mchartx.setDragEnabled(true);
        mchartx.setScaleEnabled(true);

        mcharty = (LineChart) findViewById(R.id.alinecharty);
        mcharty.setDragEnabled(true);
        mcharty.setScaleEnabled(true);

        mchartz = (LineChart) findViewById(R.id.alinechartz);
        mchartz.setDragEnabled(true);
        mchartz.setScaleEnabled(true);

        mchartx.getAxisRight().setEnabled(false);
        mcharty.getAxisRight().setEnabled(false);
        mchartz.getAxisRight().setEnabled(false);

        mchartx.getAxisLeft().setAxisMinValue(-0.009f);
        mcharty.getAxisLeft().setAxisMinValue(-0.009f);
        mchartz.getAxisLeft().setAxisMinValue(-0.009f);
        mchartx.getAxisLeft().setAxisMaxValue(0.009f);
        mcharty.getAxisLeft().setAxisMaxValue(0.009f);
        mchartz.getAxisLeft().setAxisMaxValue(0.009f);

        ArrayList<Entry> xValues = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<Entry> zValues = new ArrayList<>();


        int x = 0;
        while (afildatax.length > x) {
            xValues.add(new Entry(x, afildatax[x]));
            x++;
        }
        int y = 0;
        while (afildatay.length > y) {
            yValues.add(new Entry(y, afildatay[y]));
            y++;
        }
        int z = 0;
        while (afildataz.length > z) {
            zValues.add(new Entry(z, afildataz[z]));
            z++;
        }
        LineDataSet filteredx = new LineDataSet(xValues, "X AXIS DATA");
        filteredx.setFillAlpha(10);
        filteredx.setDrawCircles(false);
        filteredx.setColor(Color.BLACK);
        filteredx.setLineWidth(0.5f);
        filteredx.setValueTextSize(0f);

        LineDataSet filteredy = new LineDataSet(yValues, "Y AXIS DATA");
        filteredy.setFillAlpha(10);
        filteredy.setDrawCircles(false);
        filteredy.setColor(Color.BLACK);
        filteredy.setLineWidth(0.5f);
        filteredy.setValueTextSize(0f);

        LineDataSet filteredz = new LineDataSet(zValues, "Z AXIS DATA");
        filteredz.setFillAlpha(10);
        filteredz.setDrawCircles(false);
        filteredz.setColor(Color.BLACK);
        filteredz.setLineWidth(0.5f);
        filteredz.setValueTextSize(0f);


        ArrayList<ILineDataSet> dataSetsx = new ArrayList<>();
        dataSetsx.add(filteredx);
        ArrayList<ILineDataSet> dataSetsy = new ArrayList<>();
        dataSetsy.add(filteredy);
        ArrayList<ILineDataSet> dataSetsz = new ArrayList<>();
        dataSetsz.add(filteredz);
        mchartx.getDescription().setText("HAND TREMOR READING");
        LineData[] datax = {new LineData(dataSetsx)};
        mchartx.setData(datax[0]);
        mchartx.invalidate();

        mcharty.getDescription().setText("HAND TREMOR READING");
        LineData[] datay = {new LineData(dataSetsy)};
        mcharty.setData(datay[0]);
        mcharty.invalidate();

        mchartz.getDescription().setText("HAND TREMOR READING");
        LineData[] dataz = {new LineData(dataSetsz)};
        mchartz.setData(dataz[0]);
        mchartz.invalidate();

    }


    void displayData() {
        // delete the previous data in the list
        rec_id.clear();
        rec_datetime.clear();
        rec_tremor.clear();
        rec_vibrate.clear();

        Cursor cursor = dbdata.readalldata();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "THERE IS NO HAND TREMOR RECORDS", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                rec_id.add(cursor.getString(0));
                rec_datetime.add(cursor.getString(1));
                rec_tremor.add(cursor.getString(2));
                rec_vibrate.add(cursor.getString(3));
            }
        }
    }

    void cal_state() {
        calibrate.setText("RECALIBRATE DEVICE");
        calibrate.setClickable(true);
        revolver = 1;

    }

    private void alertdialog() {
        con_prog.setVisibility(ProgressBar.INVISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ALERT MESSAGE ");
        builder.setMessage("CANT REACH THE DEVICE ITS EITHER THE DEVICE IS NOT AVAILABLE OR YOU ARE NOT PAIRED TO THE DEVICE BLUETOOTH \n \n DO YOU WANT TO GO TO THE HAND TREMOR RECORD [YES] OR [NO] GO BACK AND TRY AGAIN TO CONNECT? ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        bolcon = false;
                        WelWin.setVisibility(RelativeLayout.GONE);
                        CalWin.setVisibility(RelativeLayout.GONE);
                        RecWin.setVisibility(RelativeLayout.VISIBLE);
                        Button back = (Button) findViewById(R.id.back_btn);
                        displayData();
                        init_tremor_rec();
                        back.setText("GO BACK AND CONNECT");
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    private void normal_exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ALERT MESSAGE");
        builder.setMessage("YOU DON'T HAVE HAND TREMOR THE APP AND DEVICE WILL TURN OFF RERUN THE APP AND THE DEVICE IF YOU WANT TO TRY AGAIN")
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        finish();
                        System.exit(0);
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
    private void dis_exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ALERT MESSAGE");
        builder.setMessage("THE DEVICE BLUETOOTH HAS DISCONNECTED PLEASE RESTART THE HTR GLOVES!")
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        finish();
                        System.exit(0);
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
    private void finish_cal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NOTIFICATION MESSAGE");
        builder.setMessage("ALL PROCESS ARE DONE \n " +
                "RESULTS: \n" +
                "You have a " + tremor)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    void init_tremor_rec() {
        listAdpater = new ListAdpater(MainActivity.this, rec_id, rec_datetime, rec_tremor, rec_vibrate);
        Collections.reverse(rec_id);
        Collections.reverse(rec_datetime);
        Collections.reverse(rec_tremor);
        Collections.reverse(rec_vibrate);
        reclist.setAdapter(listAdpater);
        reclist.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onBackPressed() {
        if (bt == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ALERT MESSAGE");
            builder.setMessage("DO YOU WISH TO EXIT ?? \n \n If you want to exit the you need to reboot the HTR GLOVE to connect again !")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                closeBT();
                                finish();
                                System.exit(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        } else {
            finish();
            System.exit(0);
        }

    }

}