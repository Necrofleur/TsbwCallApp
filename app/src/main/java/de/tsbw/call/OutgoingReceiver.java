package de.tsbw.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OutgoingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        setResultData(NumberRewriteEngine.rewriteNumber(number));
    }
}
