package com.example.a30_alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a30_alarm.R;

public class MainActivity extends Activity {
    // Questo valore serve ad identificare l'alarm
    private final static int REQUEST_CODE = 0;
    TextView tvAttivo;
    AlarmManager alarmManager;
    Context context;
    Intent intent;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAttivo = (TextView) findViewById(R.id.tvAttivo);
        tvAttivo.setText("Alarm non attivo");
        context = getApplicationContext();

        //Riferimento all'alarm manager
        alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
    }


    public void setAlarm(View v) {

        // Creiamo l'intent e settiamo appropriatamente i flag per lanciare
        // l'activity o portarla in primo piano se è già stata lanciata.
        intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);// Questo permete di lanciare l'intent come un'activity
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //Questo porta l'activity in foreground
        intent.setData(Uri.parse("custom://12345")); // Questo permette di individuare l'intent
        //intent.setAction(String.valueOf(12345));

        // Poichè l'activity verrà lanciata successivamente creiamo un PendingIntent
        // che permetterà all'alarmManager di lanciare il nostro intent
        // il requestCode serve al sistema per identificare i pendingIntent
        pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, 0);

        //Selezioniamo il tipo di Alarm
        int alarmType = AlarmManager.ELAPSED_REALTIME;
        //l'intent verrà rilanciato dopo RITARDO millisecondi
        final int RITARDO = 30000; //millisecondi
        final int secondi = RITARDO/1000;

        //alarmManager.set(alarmType, SystemClock.elapsedRealtime() + RITARDO, pendingIntent);
        //tvAttivo.setText("Alarm one-time attivo ("+secondi+"sec)");

        alarmManager.setRepeating(alarmType, SystemClock.elapsedRealtime() + RITARDO, RITARDO, pendingIntent);
        tvAttivo.setText("Alarm repeating attivo (ogni "+secondi+"sec)");
    }

    public void cancelAlarm(View v) {
        //Per cancellare un pendingIntent dobbiamo trovarlo
        //Per trovarlo creiamo un intent uguale a quello che dobbiamo cercare
        intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setData(Uri.parse("custom://12345")); // Questo permette di individuare l'intent
        //intent.setAction(String.valueOf(12345));
        //FLAG_NO_CREATE indica che non vogliamo creare un nuovo intento ma solo cercarlo
        //Se non viene trovato il pendingIntent sarà nullo
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent == null) {
            Toast.makeText(context,"Pending intent non trovato",Toast.LENGTH_LONG).show();
        }
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            tvAttivo.setText("Alarm cancellato");
        }
    }
    public void finishActivity(View v) {
        this.finish();
    }
}
