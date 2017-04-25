package com.jiadu.chuankou_v2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiadu.serialport.SerialPort;
import com.jiadu.util.BytePool;
import com.jiadu.util.SharePreferenceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Called when the activity is first created.
     */
    FileOutputStream mOutputStream;
    FileInputStream mInputStream;
    SerialPort mSp;
    private Button mBt_open;
    private Button mBt_send;
    private EditText mEt;
    private boolean mFlag =true;
    private TextView mTv;
    private EditText mEt_dev;
    private String mDev;
    private Button mBt_save;
    private BytePool mBytePool = new BytePool();
    private Thread mThread;
    private Button mBt_receive;
    private Button mBt_stop;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
        
        initListener();
    }

    private void initData() {

        mDev = SharePreferenceUtil.getSring(this, "dev");
        
        if (TextUtils.isEmpty(mDev)){

            mDev="/dev/ttyUSB0";
           }

        mEt_dev.setText(mDev);

        mEt_dev.setSelection(mDev.length());
    }

    private void initListener() {

        mBt_open.setOnClickListener(this);
        mBt_send.setOnClickListener(this);
        mBt_save.setOnClickListener(this);
        mBt_receive.setOnClickListener(this);
        mBt_stop.setOnClickListener(this);
    }

    private void initView() {

        mBt_open = (Button) findViewById(R.id.bt_open);
        mBt_send = (Button) findViewById(R.id.bt_send);
        mEt = (EditText) findViewById(R.id.et_show);
        mTv = (TextView) findViewById(R.id.tv);
        mEt_dev = (EditText) findViewById(R.id.et_dev);
        mBt_save = (Button) findViewById(R.id.bt_save);
        mBt_receive = (Button) findViewById(R.id.bt_receive);
        mBt_stop = (Button) findViewById(R.id.bt_stop);

    }
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
              case R.id.bt_open:

                  if (mInputStream!=null && mOutputStream!=null){

                      Toast.makeText(getApplicationContext(),"已经是打开状态",Toast.LENGTH_SHORT).show();

                      return;

                     }

                  try {
                      mSp = new SerialPort(new File(mDev),115200);

                      mOutputStream=(FileOutputStream) mSp.getOutputStream();

                      mInputStream=(FileInputStream) mSp.getInputStream();

                      if (mInputStream!=null && mOutputStream!=null){

                            Toast.makeText(getApplicationContext(),"打开成功",Toast.LENGTH_SHORT).show();
                         }

                  } catch (IOException e) {
                      e.printStackTrace();
                  }

                  break;
              case R.id.bt_send:
                  try {

                      String s = mEt.getText().toString();

                      if (!TextUtils.isEmpty(s)){
                              mOutputStream.write(s.getBytes());
                              mOutputStream.write('\r');
                              mOutputStream.flush();
                          Toast.makeText(getApplicationContext(), "send", Toast.LENGTH_SHORT).show();
                         }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }

              break;
              case R.id.bt_save:

                  if(!TextUtils.isEmpty(mEt_dev.getText().toString())){

                    SharePreferenceUtil.putString(this,"dev",mEt_dev.getText().toString().trim());

                    mDev = mEt_dev.getText().toString().trim();
                  }

              break;
              case R.id.bt_receive:
                  
                  mThread = new Thread() {
                      private LinkedList<Byte> al=mBytePool.get();

                      @Override
                      public void run() {
                          if (mInputStream != null) {
                              mFlag =true;
                              try {
                                  while (mFlag) {
                                      
                                      int i = mInputStream.read();

                                      if(i == '\r'){
                                          continue;
                                      }

                                      if (i != '\n') {
                                          System.out.println("我收到了消息....." + i);

                                          al.add((byte) i);

                                      } else {

                                          final LinkedList<Byte> alTemp=al;
                                          al=mBytePool.get();
                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  int length = alTemp.size();

                                                  byte[] bt = new byte[length];

                                                  for (int i=0;i<length;i++){
                                                      bt[i]=alTemp.get(i);
                                                  }
                                                  try {
                                                      mTv.setText(new String(bt,0,length,"GBK"));
                                                  } catch (UnsupportedEncodingException e) {
                                                      e.printStackTrace();
                                                  }

                                                  mBytePool.recycle(alTemp);
                                              }
                                          });
                                      }
                                  }

                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                          }
                      }
                  };

                  mThread.start();

                  break;
              case R.id.bt_stop:

                  mFlag = false;

                  mSp.close();

                  mInputStream = null;
                  mOutputStream = null;

              break;

              default:
              break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mFlag=false;

        mThread.interrupt();

        if (mInputStream!=null ){
            try {
                
                mInputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOutputStream!=null){
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        mSp.close();

        System.exit(0);
    }
}
