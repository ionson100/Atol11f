package assa.com.example.user.atol11fstringvalue;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atol.drivers.fptr.Fptr;
import com.atol.drivers.fptr.IFptr;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<Integer, String> map3 = new HashMap<>();
    private Map<Integer, String> map4 = new HashMap<>();
    private ListView listView;
    private TextView log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = (TextView) findViewById(R.id.log);
        listView = (ListView) findViewById(R.id.list_table);
        findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int position, long arg3) {

                DialogEditKmmTable dialogEditKmmTable = new DialogEditKmmTable();
                dialogEditKmmTable.setActite((Tiny) arg1.getTag(), map3, map4, new IActionE() {
                    @Override
                    public void action(Object o) {

                        final Tiny tiny = (Tiny) o;

                        if (tiny.table == 1) {
                            SetOneCaption.set(MainActivity.this, tiny.purpose, tiny.value, new IActionE() {
                                @Override
                                public void action(Object o) {
                                    TextView t = (TextView) arg1.findViewById(R.id.text2);
                                    t.setText(tiny.value);
                                }
                            });
                        }
                        if (tiny.table == 2) {
                            SetOneValue.set(MainActivity.this, tiny.purpose, Integer.parseInt(tiny.value), new IActionE() {
                                @Override
                                public void action(Object o) {
                                    TextView t = (TextView) arg1.findViewById(R.id.text2);
                                    t.setText(tiny.value);
                                }
                            });
                        }
                    }
                });
                dialogEditKmmTable.show(MainActivity.this.getSupportFragmentManager(), "dsdddsssr");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Laberator().execute();


    }

    private class Laberator extends AsyncTask<Void, String, Void> {
        private List<Tiny> tityList = new ArrayList<>();
        private int i = 0;
        private Fptr fptr = new Fptr();
        private String error;

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
                fptr.create(getApplication());
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
                ///////////////////////////////////// url


                for (i = 6; i < 300; i++) {

                    int ss = fptr.put_CaptionPurpose(i);
                    if (ss == 0) {
                        if (fptr.GetCaption() == 0) {
                            publishProgress(String.valueOf(i));

                            Tiny tity = new Tiny();
                            tity.name = fptr.get_CaptionName();
                            tity.table = 1;
                            tity.purpose = i;
                            tity.value = fptr.get_Caption();
                            tityList.add(tity);
                        }

                    }


                }//374

                for (i = 0; i < 380; i++) {
                    if (fptr.put_ValuePurpose(i) == 0) {
                        if (fptr.GetValue() == 0) {
                            publishProgress(String.valueOf(i));
                            Tiny tity = new Tiny();
                            tity.name = fptr.get_ValueName();
                            tity.table = 2;
                            tity.purpose = i;
                            tity.value = String.valueOf((int) fptr.get_Value());
                            tityList.add(tity);
                        }
                    }
                }
            } catch (Exception ex) {

                error = ex.getMessage();

            } finally {
                fptr.ResetMode();
                fptr.destroy();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values == null || values.length == 0) {
                return;
            }
            log.setText(values[0]);

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (error != null) {
                log.setText(error);
            } else {
                log.setVisibility(View.GONE);
                ListTableAdaptor adaptor = new ListTableAdaptor(MainActivity.this, R.layout.item_table_kassa, tityList);
                listView.setAdapter(adaptor);
            }
        }
    }

    private class ListTableAdaptor extends ArrayAdapter<Tiny> {

        private final int resource;
        private Map<String, View> map = new HashMap<>();

        ListTableAdaptor(@NonNull Context context, @LayoutRes int resource, @NonNull List<Tiny> objects) {
            super(context, resource, objects);
            this.resource = resource;


            {
                InputStream is;
                try {
                    is = getBaseContext().getAssets().open("tt3");
                } catch (IOException ignored) {
                    return;
                }

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;
                try {
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                } catch (IOException ignored) {
                    return;
                }

                try {
                    Gson gson = new Gson();
                    map3 = gson.fromJson(total.toString(), new TypeToken<Map<Integer, String>>() {
                    }.getType());
                } catch (Exception ex) {

                }
            }


            {

                InputStream is = null;
                try {
                    is = getBaseContext().getAssets().open("tt4");
                } catch (IOException ignored) {
                    return;
                }

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;
                try {
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                } catch (IOException ignored) {
                    return;
                }

                try {
                    Gson gson = new Gson();
                    map4 = gson.fromJson(total.toString(), new TypeToken<Map<Integer, String>>() {
                    }.getType());
                } catch (Exception ignored) {
                }
            }


        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View mView = convertView;

            final Tiny p = getItem(position);
            if (map.containsKey(p.idu)) {
                return map.get(p.idu);
            } else {
                mView = getView(p);
                map.put(p.idu, mView);
            }


            return mView;
        }

        @NonNull
        private View getView(final Tiny p) {
            final View mView;
            mView = LayoutInflater.from(getContext()).inflate(resource, null);
            TextView textView1 = (TextView) mView.findViewById(R.id.text1);
            TextView textView2 = (TextView) mView.findViewById(R.id.text2);
            TextView textView3 = (TextView) mView.findViewById(R.id.text3);
            textView1.setText(String.valueOf(p.purpose));
            textView2.setText(p.value);

            if (p.name != null) {
                textView3.setText(p.name);
            } else {
                textView3.setVisibility(View.GONE);
            }
            mView.setTag(p);
            return mView;
        }
    }
}

