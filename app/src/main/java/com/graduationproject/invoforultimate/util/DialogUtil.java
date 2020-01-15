package com.graduationproject.invoforultimate.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.graduationproject.invoforultimate.MainActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import com.graduationproject.invoforultimate.service.TrackHandler;
import com.graduationproject.invoforultimate.service.TrackThread;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by INvo
 * on 2019-10-10.
 */
public class DialogUtil {
    private TrackThread trackThread;
    private boolean check = false;
    private String TerminalName;
    private TrackHandler trackHandler;
    private InitializeTerminal initializeTerminal;
//    private TrackHandler trackHandler;

    /*
     * 创建Terminal的progressBar
     * */
    public ProgressDialog startProgressBar(final Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle("没有找到终端设备");
        progressDialog.setMessage("正在为你创建");
        progressDialog.setCancelable(false);
        progressDialog.show();
        /*trackHandler = new TrackHandler();
        new Thread(){
            @Override
            public void run() {
                trackHandler.sendEmptyMessageDelayed(Constants.CreateTerminalCommand, 2000);
                Log.d("myhandlertest2", "ssssssss:" );
            }
        }.start();
        *//*trackThread = new TrackThread(context, Constants.CreateTerminalCommand,getTerminalName(),new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        });
        trackThread.run();*/
        return progressDialog;
    }

    public void stopProgressBar(Context context) {

    }

    public void createTerminalID(final Context context, final Activity activity) {
//        final String result = "";
//         final String result;
//        ProgressDialog progressDialog;
//        String tid;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.create_tid, null);
        Button button = view.findViewById(R.id.check_tid);
        final EditText editText = view.findViewById(R.id.create_tid);
//        final TextView textView = view.findViewById(R.id.notification_tid);
        builder.setTitle("该设备没有注册终端")
                .setMessage("请创建你的终端ID以作为标识")
                .setCancelable(false)
                .setView(view);
        final AlertDialog alertDialog = builder.show();
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //监听回车按下事件
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        InputUtil.hideAllInputMethod(activity);
                        if (0 == editText.getText().toString().length()) {
                            ToastUtil.showToast(context, Constants.InputEmpty);
                        }
                    }
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String result = editText.getText().toString();
                setTerminalName(editText.getText().toString());
//                Log.d("DialogUtil", result);
//                ToastUtil.showToast(context, result);
                if (0 != editText.getText().toString().length()) {
//                    ToastUtil.showToast(context, editText.getText().toString());
                    alertDialog.dismiss();
                    ProgressDialog progressDialog=startProgressBar(context);
//                    Log.d("myhandlertest2", "ssssssss:");
                    /*
                     * 从这里开始提交terminal name
                     * */
                    initializeTerminal = new InitializeTerminal();
                    /*if (initializeTerminal.setTerminal(context, getTerminalName())) {
                        ToastUtil.showToast(context, Constants.CreateTerminalSucceed);
                    } else {
                        ToastUtil.showToast(context, Constants.CreateTerminalFailure);
                    }*/

//                                        Message message = Message.obtain();

                    trackHandler = new TrackHandler() {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            Log.d("mymsg", "msg:" + msg);
                            if (msg.what == Constants.MsgTerminalSuccess) {
                                String tid=null;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                                     tid = jsonObject.getJSONObject("data").getString("tid");
                                    Log.d("my", tid);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {
                                    /*
                                     * 写入tid到sp
                                     * */
                                    Log.d("DialogUtil-TerminalName", getTerminalName());//check name exist
                                    initializeTerminal.setTerminal(context, tid,getTerminalName());
                                    ToastUtil.showToast(context, Constants.CreateTerminalSucceed);
                                    progressDialog.dismiss();
                                }
                            } else {
                                /*
                                 * 重新处理结果
                                 * */
                                if (msg.what == Constants.MsgTerminalExistError) {
                                    ToastUtil.showLongToast(context, Constants.TerminalExistError);
                                    progressDialog.dismiss();
                                    createTerminalID(context, activity);
                                }
                                if (msg.what == Constants.MsgTerminalInvalidError) {
                                    ToastUtil.showLongToast(context, Constants.TerminalInvalidError);
                                    progressDialog.dismiss();
                                    createTerminalID(context, activity);
                                }
                            }
                        }
                    };

                    initializeTerminal.createTerminal(context, getTerminalName(),trackHandler);
                    Log.d("mymesswhat", "message.whaxxxxxt:" );
                }
            }
        });
    }

    public String getTerminalName() {
        return TerminalName;
    }

    public void setTerminalName(String terminalName) {
        this.TerminalName = terminalName;
    }

    public void checkFalse(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("本次服务将不会上传")
                .setMessage("因为距离或时间太短等其他因素，本次记录的轨迹将不会上传到服务器上")
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void exitDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("是否退出软件？")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)
                .show();
    }
}
