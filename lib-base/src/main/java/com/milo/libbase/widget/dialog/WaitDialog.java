package com.milo.libbase.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.milo.libbase.R;

/**
 * 标题：
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2019/2/13 16:02
 */
public class WaitDialog extends Dialog {

    public TextView titleTv;
    public TextView msgTv;

    public String title;
    public String msg;

    public WaitDialog(Context context) {
        this(context, 0);
    }

    public WaitDialog(Context context, int theme) {
        super(context, R.style.libbase_WaitDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libbase_wait_dialog);

        titleTv = (TextView) findViewById(R.id.title_tv);
        msgTv = (TextView) findViewById(R.id.message_tv);

        if (titleTv != null) {
            titleTv.setText(title);
        }

        if (msgTv != null) {
            msgTv.setText(msg);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.msg = message;
        if (msgTv != null) {
            msgTv.setText(message);
        }
    }

}
