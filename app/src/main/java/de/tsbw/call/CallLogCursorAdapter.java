package de.tsbw.call;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CallLogCursorAdapter extends CursorAdapter {
    /* Ex: 01. Januar 1970 00:00 */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d. MMMM yyyy HH:mm", Locale.GERMANY);

    CallLogCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.call_log_entry, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView numberTextView = view.findViewById(R.id.numberTextView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView typeTextView = view.findViewById(R.id.typeTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);

        int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
        String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
        String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
        if (NumberRewriteEngine.isDialerNumber(number)) {
            try {
                number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.POST_DIAL_DIGITS));
                number = NumberRewriteEngine.stripDialerNumberSuffix(number);
            }
            catch(Exception e){
                //POST_DIAL_DIGITS has been added in API lvl 24. For Android Versions below, this exception will be thrown.
                //This way of handling the Exception will lead to outgoing calls being shown in History as 'Unbekannt
                number=null;
            }
        }
        Long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));

        typeTextView.setText(translateType(context, type));

        if (name != null && !name.isEmpty()) {
            nameTextView.setText(name);
            nameTextView.setVisibility(View.VISIBLE);
        } else {
            nameTextView.setVisibility(View.GONE);
        }

        if (number == null || number.isEmpty()) {
            number = context.getString(R.string.unknown);
        }

        numberTextView.setText(number);
        dateTextView.setText(dateFormat.format(date));
    }

    private String translateType(Context context, int type) {
        switch (type)
        {
            case CallLog.Calls.INCOMING_TYPE:
                return context.getString(R.string.incoming_call);
            case CallLog.Calls.OUTGOING_TYPE:
                return context.getString(R.string.outgoing_call);
            case CallLog.Calls.MISSED_TYPE:
                return context.getString(R.string.missed_call);
            case CallLog.Calls.REJECTED_TYPE:
                return context.getString(R.string.missed_call);
            default:
                return context.getString(R.string.unknown);
        }
    }
}
