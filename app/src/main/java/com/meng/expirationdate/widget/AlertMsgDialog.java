package com.meng.expirationdate.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.meng.expirationdate.R;
import com.meng.expirationdate.utils.Utils;

public class AlertMsgDialog {
    public static AlertDialog showMsgDialog(Context context, String title, String msg, boolean cancelable, String cancelText, View.OnClickListener cancelListener, String okText, View.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Translucent_NoTitle_Default);
        View view = LayoutInflater.from(context).inflate(R.layout.public_dialog_alter, null);
        TextView tv1 = view.findViewById(R.id.tv1);
        if (!TextUtils.isEmpty(title)) {
            tv1.setVisibility(View.VISIBLE);
            tv1.setText(title);
        }
        TextView tv2 = view.findViewById(R.id.tv2);
        tv2.setText(msg);
        builder.setView(view);
        builder.setCancelable(cancelable);
        AlertDialog dialog = builder.create();
        LinearLayout ll_cancelok = view.findViewById(R.id.ll_cancelok);
        TextView ok = view.findViewById(R.id.ok);
        if (!TextUtils.isEmpty(cancelText)) {
            ll_cancelok.setVisibility(View.VISIBLE);
            TextView left = view.findViewById(R.id.tv_left);
            TextView right = view.findViewById(R.id.tv_rignt);
            left.setText(cancelText);
            left.setOnClickListener(v -> {
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
                dialog.dismiss();
            });
            right.setText(okText);
            right.setOnClickListener(v -> {
                if (okListener != null) {
                    okListener.onClick(v);
                }
                dialog.dismiss();
            });
        } else if (!TextUtils.isEmpty(okText)) {
            ok.setVisibility(View.VISIBLE);
            ok.setText(okText);
            ok.setOnClickListener(v -> {
                if (okListener != null) {
                    okListener.onClick(v);
                }
                dialog.dismiss();
            });
        }
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(Utils.convertDpToPixelOfInt(280), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return dialog;
    }

    public static AlertDialog showColorMsgDialog(Context context, String title, String msg, boolean cancelable, String cancelText, View.OnClickListener cancelListener, String okText, View.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Translucent_NoTitle_Default);
        View view = LayoutInflater.from(context).inflate(R.layout.public_dialog_alter, null);
        TextView tv1 = view.findViewById(R.id.tv1);
        if (!TextUtils.isEmpty(title)) {
            tv1.setVisibility(View.VISIBLE);
            tv1.setText(title);
        }
        TextView tv2 = view.findViewById(R.id.tv2);
        if (!TextUtils.isEmpty(msg)) {
            //msg格式 name$#ffffff|哈哈哈哈|点击查看$#fff000
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            String[] strings = msg.split("\\|");
            if (strings.length > 0) {
                for (String string : strings) {
                    String[] str = string.split("[$]");
                    if (str.length > 1) {
                        SpannableString spannableString = new SpannableString(str[0]);
                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(str[1])), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.append(spannableString);
                    } else {
                        spannableStringBuilder.append(str[0]);
                    }
                }
            }
            tv2.setText(spannableStringBuilder);
        }
        builder.setView(view);
        builder.setCancelable(cancelable);
        AlertDialog dialog = builder.create();
        LinearLayout ll_cancelok = view.findViewById(R.id.ll_cancelok);
        TextView ok = view.findViewById(R.id.ok);
        if (!TextUtils.isEmpty(cancelText)) {
            ll_cancelok.setVisibility(View.VISIBLE);
            TextView left = view.findViewById(R.id.tv_left);
            TextView right = view.findViewById(R.id.tv_rignt);
            left.setText(cancelText);
            left.setOnClickListener(v -> {
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
                dialog.dismiss();
            });
            right.setText(okText);
            right.setOnClickListener(v -> {
                if (okListener != null) {
                    okListener.onClick(v);
                }
                dialog.dismiss();
            });
        } else if (!TextUtils.isEmpty(okText)) {
            ok.setVisibility(View.VISIBLE);
            ok.setText(okText);
            ok.setOnClickListener(v -> {
                if (okListener != null) {
                    okListener.onClick(v);
                }
                dialog.dismiss();
            });
        }
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(Utils.convertDpToPixelOfInt(280), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return dialog;
    }
}
