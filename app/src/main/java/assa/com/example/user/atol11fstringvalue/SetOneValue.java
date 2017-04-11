package assa.com.example.user.atol11fstringvalue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.atol.drivers.fptr.Fptr;
import com.atol.drivers.fptr.IFptr;

public class SetOneValue {

    public static void set(final Activity activity, final int purpose, final int value, final IActionE iActionE) {
        new AsyncTask<Void, String, Void>() {
            private String errorText;
            private ProgressDialog dialog;

            private Fptr fptr;

            private void checkError() throws Exception {
                int rc = fptr.get_ResultCode();
                if (rc < 0) {
                    String rd = fptr.get_ResultDescription(), bpd = null;
                    if (rc == -6) {
                        bpd = fptr.get_BadParamDescription();
                    }
                    if (bpd != null) {
                        throw new Exception(String.format("[%d] %s (%s)", rc, rd, bpd));
                    } else {
                        throw new Exception(String.format("[%d] %s", rc, rd));
                    }
                }
            }


            @Override
            protected Void doInBackground(Void... params) {

                try {
                    fptr = new Fptr();
                    fptr.create(activity.getApplication());
                    if (fptr.put_DeviceSettings(Utils.settingsKmm) < 0) {
                        checkError();
                    }
                    if (fptr.put_DeviceEnabled(true) < 0) {
                        checkError();
                    }
                    if (fptr.GetStatus() < 0) {
                        checkError();
                    }
                    if (fptr.put_UserPassword("00000030") < 0) {
                        checkError();
                    }
                    if (fptr.put_Mode(IFptr.MODE_PROGRAMMING) < 0) {
                        checkError();
                    }
                    if (fptr.SetMode() < 0) {
                        checkError();
                    }


                    ////////////////////////////////////////////////////////

                    if (fptr.put_ValuePurpose(purpose) < 0) {
                        checkError();
                    }
                    if (fptr.put_Value(value) < 0) {
                        checkError();
                    }
                    if (fptr.SetValue() < 0) {
                        checkError();
                    }
                    //////////////////////////////////////////

                } catch (Exception ex) {
                    errorText = ex.getMessage();
                } finally {
                    fptr.ResetMode();
                    fptr.destroy();
                }
                return null;
            }


            @Override
            protected void onPreExecute() {
                dialog = Utils.factoryDialog(activity, activity.getString(R.string.xzx), null);
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
                if (errorText != null) {
                    Utils.messageBox("error", errorText, activity, null);
                } else {
                    Utils.messageBox("успех", activity.getString(R.string.wqeqwe), activity, null);
                    iActionE.action(true);
                }
            }

        }.execute();
    }
}
