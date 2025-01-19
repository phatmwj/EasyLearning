package tpp.profixer.customer.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;

import timber.log.Timber;
import tpp.profixer.customer.R;

public class GPSObserver extends ContentObserver {

    private Context context;
    AlertDialog alert;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public GPSObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        boolean isGPSEnabled = isGPSEnabled(context);
        if (isGPSEnabled) {
            if(alert != null){
                alert.dismiss();
            }
            Timber.d("GPS is enabled");
        } else {
            showEnableGPSDialog();
            Timber.d("GPS is disabled");
        }
    }

    public boolean isGPSEnabled(Context context) {
        try {
            int locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return locationMode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY || locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
        } catch (Settings.SettingNotFoundException e) {
            Timber.e(e);
            return false;
        }
    }

    private void showEnableGPSDialog() {
        if(alert == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.gps_is_disabled))
                    .setMessage(R.string.request_enable_gps)
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert = builder.create();
        }
        alert.show();
    }
}
