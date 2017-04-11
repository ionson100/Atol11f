package assa.com.example.user.atol11fstringvalue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import java.util.UUID;

/**
 * Created by USER on 11.04.2017.
 */

class Utils {

    public static String settingsKmm="<?xml version=\"1.0\"?><settings version=\"4\"><value name=\"Parity\">0</value><value name=\"DeviceName\">0555137_ATOL_11F</value><value name=\"Bits\">8</value><value name=\"Model\">67</value><value name=\"MACAddress\">34:87:3D:18:08:DF</value><value name=\"AutoDisableBluetooth\">0</value><value name=\"StopBits\">0</value><value name=\"ConnectionType\">1</value><value name=\"AutoEnableBluetooth\">1</value><value name=\"AccessPassword\">0</value><value name=\"BaudRate\">9600</value><value name=\"OfdPort\">BLUETOOTH</value><value name=\"Port\">BLUETOOTH</value><value name=\"TTYSuffix\">ttyACM0</value><value name=\"IPAddress\">192.168.0.123</value><value name=\"IPPort\">5555</value><value name=\"UserPassword\">30</value><value name=\"Protocol\">0</value></settings>";

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static ProgressDialog factoryDialog(Activity mActivity, String msg, final IActionE iAction) {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage(msg);
        dialog.setIndeterminate(true);
        if (iAction == null) {
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    iAction.action(null);
                }
            });
        }


        return dialog;
    }

    public static void messageBox(final String title, final String message, final Activity activity, final IActionE iAction) {
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_info);
                if (iAction != null) {
                    builder.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }

                    });
                }
                String but = "Ok";
                if (iAction == null) {
                    but = "Закрыть";
                }


                builder.setPositiveButton(but, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (iAction != null) {
                            iAction.action(null);
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });
    }
}
