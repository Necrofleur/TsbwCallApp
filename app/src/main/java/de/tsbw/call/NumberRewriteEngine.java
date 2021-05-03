package de.tsbw.call;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;

class NumberRewriteEngine {
    private static boolean DEBUG = false;

    private static final String DIALER_NUMBER = "04841899279709";

    /*
     * Needs to start with a DTMF pause or wait character (',' or ';').
     */
    private static final String DIALER_NUMBER_SUFFIX = ",0";

    /*
     * Important: The IGNORE_PREFIX is needed to ensure that rewriting
     * is idempotent, i.e. rewriting a number more than once does not
     * result in multiple prefixes.
     */
    private static final String IGNORE_PREFIX = "048418992";

    static String rewriteNumber(String number) {
        if (DEBUG) {
            /*
             * Don't call people by accident while debugging.
             */
            return null;
        }

        number = number.replaceAll("\\+", "00");
        if (number.startsWith("0")||number.equals("116117")) {
            if (!number.startsWith(IGNORE_PREFIX)) {
                return DIALER_NUMBER + DIALER_NUMBER_SUFFIX + number;
            } else {
                return number;
            }
        } else {
            return number;
        }
    }

    static void placeCall(Context context, String number) {
        String rewrittenNumber = NumberRewriteEngine.rewriteNumber(number);
        Uri uri =  Uri.fromParts("tel", rewrittenNumber, null);
        Intent intent;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            intent = new Intent(Intent.ACTION_CALL, uri);
        } else {
            intent = new Intent(Intent.ACTION_DIAL, uri);
        }
        context.startActivity(intent);
    }

    static boolean isDialerNumber(String number) {
        return number != null && number.equals(DIALER_NUMBER);
    }

    static String stripDialerNumberSuffix(String number) {
        if (number.startsWith(DIALER_NUMBER_SUFFIX)) {
            return number.substring(DIALER_NUMBER_SUFFIX.length());
        } else {
            return number;
        }
    }
}
