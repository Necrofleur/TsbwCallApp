package de.tsbw.call;

import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class PickFromCallLogActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_from_call_log);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            CallLogCursorAdapter adapter = new CallLogCursorAdapter(this, cursor);
            setListAdapter(adapter);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        TextView numberTextView = view.findViewById(R.id.numberTextView);
        String number = numberTextView.getText().toString();
        if (!number.equals(getString(R.string.unknown))) {
            NumberRewriteEngine.placeCall(this, number);
        }
    }
}
