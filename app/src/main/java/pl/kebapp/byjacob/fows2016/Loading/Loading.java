package pl.kebapp.byjacob.fows2016.Loading;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pl.kebapp.byjacob.fows2016.JSONParser;
import pl.kebapp.byjacob.fows2016.OknoGlowne;
import pl.kebapp.byjacob.fows2016.R;
import pl.kebapp.byjacob.fows2016.SQLiteV2.PrelegenciDbAdapter;
import pl.kebapp.byjacob.fows2016.SQLiteV2.PrelegenciTask;
import pl.kebapp.byjacob.fows2016.SQLiteV2.ProgramDbAdapter;
import pl.kebapp.byjacob.fows2016.SQLiteV2.ProgramTask;

import static pl.kebapp.byjacob.fows2016.SharedPreferences.*;

public class Loading extends AppCompatActivity {

    private static final Integer WERSJA_APLIKACJI = 16;

    private static final String DEBUG_TAG = "LoadingWindow";

    private static Boolean AKTUALIZACJA_PROGRAM = false;
    private static Boolean AKTUALIZACJA_PRELEGENCI = false;
    private static Boolean AKTUALIZACJA_APLIKACJI = false;

    private SharedPreferences preferences;
    private Handler handler;

    private static Activity ACTIVITY;
    //uprawnienia
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        preferences = getSharedPreferences(WERSJE_TABEL_SP, Activity.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ACTIVITY = this;
        Fragment fragment = new FOWS_fragment_loading();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_loading, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: //skończona aktualizacja wersji tabel
                        if (AKTUALIZACJA_PROGRAM)
                            new DownloadDBProgram().execute();
                        else
                            handler.sendEmptyMessage(1);
                        break;
                    case 1: //kontynuacja pobierania tabel
                        if (AKTUALIZACJA_PRELEGENCI)
                            new DownloadDBPrelegenci().execute();
                        else
                            handler.sendEmptyMessageDelayed(2, 3000);
                        break;
                    case 2: //ukrywanie 1 loga i czekanie z drugim

                        Fragment fragment = new WG_fragment_loading();
                        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_loading, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        ProgramDbAdapter programDbAdapter = new ProgramDbAdapter
                                (getApplicationContext());
                        programDbAdapter.open(preferences.getInt(PROGRAM_SP, 0));
                        programDbAdapter.test();
                        programDbAdapter.close();
                        PrelegenciDbAdapter prelegenciDbAdapter = new PrelegenciDbAdapter
                                (getApplicationContext());
                        prelegenciDbAdapter.open(preferences.getInt(PRELEGENCI_SP, 0));
                        prelegenciDbAdapter.test();
                        prelegenciDbAdapter.close();
                        handler.sendEmptyMessageDelayed(3, 5000);

                        break;
                    case 3: //ładowanie drugiego okna
                        Intent intent = new Intent(getApplicationContext(), OknoGlowne.class);
                        startActivityForResult(intent, 0);
                        setResult(Activity.RESULT_OK);
                        finish();
                        break;
                    case 99:
                        Fragment fragment2 = new Aktualizacja_fragment();
                        android.app.FragmentTransaction transaction2 = getFragmentManager()
                                .beginTransaction();
                        transaction2.replace(R.id.fragment_loading, fragment2);
                        transaction2.addToBackStack(null);
                        transaction2.commit();
                        break;
                }
            }
        };
        if (checkPermissions()) {
            new GetWersja().execute();
        } else {
            /*Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);*/
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(ACTIVITY, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new AlertDialog.Builder(this).setTitle(R.string.error_title_brak_uprawnien).setMessage
                            (getString(R.string.error_brak_uprawnien))
                            .setNegativeButton(getString(R.string.error_wyjdz), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Process.killProcess(Process.myPid());
                                    System.exit(1);
                                }
                            }).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class GetWersja extends AsyncTask<Void, Void, JSONObject> {

        JSONParser jsonParser = new JSONParser();
        final String URL = "http://137.74.171.255/db_dajWersje.php";
        final String TAG_SUCCESS = "OK";
        public static final String TAG_MESSAGES = "status";

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String, String> nie = new HashMap<>();
                nie.put("test", "test");
                JSONObject json = jsonParser.makeHttpRequest(URL, "POST", nie);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            int success = 0;
            JSONArray tabele_array = null;
            JSONObject tabele_object = null;
            Integer program_v = null;
            Integer prelegenci_v = null;
            if (jsonObject != null) {
                try {
                    tabele_array = jsonObject.getJSONArray("result");

                    for (int i = 0; i < tabele_array.length(); i++) {
                        tabele_object = tabele_array.getJSONObject(i);
                        if (tabele_object.getString("tabela").equals("program")) {
                            program_v = tabele_object.getInt("wersja");
                            if (preferences.getInt(PROGRAM_SP, 0) != program_v) {
                                preferences.edit().putInt(PROGRAM_SP, program_v).commit();
                                AKTUALIZACJA_PROGRAM = true;
                            }
                        }
                        if (tabele_object.getString("tabela").equals("prelegenci")) {
                            prelegenci_v = tabele_object.getInt("wersja");
                            if (preferences.getInt(PRELEGENCI_SP, 0) != prelegenci_v) {
                                preferences.edit().putInt(PRELEGENCI_SP, prelegenci_v).commit();
                                AKTUALIZACJA_PRELEGENCI = true;
                            }
                        }
                        if (tabele_object.getString("tabela").equals("aplikacja")) {
                            prelegenci_v = tabele_object.getInt("wersja");
                            if (WERSJA_APLIKACJI != prelegenci_v) {
                                AKTUALIZACJA_APLIKACJI = true;
                            }
                        }
                    }
                    if (AKTUALIZACJA_APLIKACJI) {
                        handler.sendEmptyMessageDelayed(99, 1500);
                        preferences.edit().putInt(PROGRAM_SP, 0).commit();
                        preferences.edit().putInt(PRELEGENCI_SP, 0).commit();
                    } else
                        handler.sendEmptyMessage(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(DEBUG_TAG, "Błąd pobierania wersji tabel: " + e.toString());
                }
            }
            if (jsonObject == null) //jeżeli nie uda się pobrać json pokazuje błąd i nie
            // próbuje aktualizować plików
            {
                Toast.makeText(getApplicationContext(), R.string.error_offline, Toast
                        .LENGTH_SHORT).show();
                if (preferences.getInt(PRELEGENCI_SP, 0) != 0 && preferences.getInt(PROGRAM_SP, 0)
                        != 0) {
                    handler.sendEmptyMessageDelayed(2, 3000);

                } else {
                    AlertDialog.Builder brakBazy = new AlertDialog.Builder(ACTIVITY);
                    brakBazy.setTitle(R.string.error_title_brak_plikow);
                    brakBazy.setMessage(R.string.error_brak_plikow);
                    brakBazy.setNegativeButton(R.string.error_wyjdz, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ACTIVITY.finish();
                            System.exit(0);
                        }
                    });
                    brakBazy.show();
                }
            }
        }
    }

    private class DownloadDBProgram extends AsyncTask<Void, Void, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        public static final String URL = "http://137.74.171.255/db_dajProgram.php";

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String, String> nie = new HashMap<>();
                nie.put("test", "test");
                JSONObject json = jsonParser.makeHttpRequest(URL, "POST", nie);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            JSONArray program_array = null;
            JSONObject program_object = null;
            ProgramDbAdapter programDbAdapter = new ProgramDbAdapter(getApplicationContext());
            programDbAdapter.open(preferences.getInt(PROGRAM_SP, 0));
            long test = 0;
            Cursor testWiersza;
            try {
                program_array = jsonObject.getJSONArray("result");
                testWiersza = null;
                for (int i = 0; i < program_array.length(); i++) {
                    program_object = program_array.getJSONObject(i);
                    ProgramTask task;
                    if (Locale.getDefault().toString().equals("pl_PL")) {
                        task = new ProgramTask(program_object.getInt("id"),
                                program_object.getInt("date"), program_object.getInt("startH"),
                                program_object.getInt("startM"), program_object.getInt("endH"),
                                program_object.getInt("endM"), program_object.getString("theme"),
                                program_object.getInt("speakerID"), program_object.getString("lang"));
                    } else {
                        task = new ProgramTask(program_object.getInt("id"),
                                program_object.getInt("date"), program_object.getInt("startH"),
                                program_object.getInt("startM"), program_object.getInt("endH"),
                                program_object.getInt("endM"), program_object.getString("theme_en"),
                                program_object.getInt("speakerID"), program_object.getString("lang"));
                    }
                    test = programDbAdapter.insertProgram(task);
                    testWiersza = programDbAdapter.getALLProgram();
                    testWiersza.close();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            programDbAdapter.close();
            handler.sendEmptyMessage(1);
        }
    }

    private class DownloadDBPrelegenci extends AsyncTask<Void, Void, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        public static final String URL = "http://137.74.171.255/db_dajPrelegenci.php";

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String, String> nie = new HashMap<>();
                nie.put("test", "test");
                JSONObject json = jsonParser.makeHttpRequest(URL, "POST", nie);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            JSONArray prelegenci_array = null;
            JSONObject prelegenci_object = null;
            PrelegenciDbAdapter prelegenciDbAdapter = new PrelegenciDbAdapter
                    (getApplicationContext());
            prelegenciDbAdapter.open(preferences.getInt(PRELEGENCI_SP, 0));
            long test = 0;
            try {
                prelegenci_array = jsonObject.getJSONArray("result");
                for (int i = 0; i < prelegenci_array.length(); i++) {
                    prelegenci_object = prelegenci_array.getJSONObject(i);
                    PrelegenciTask task;
                    if (Locale.getDefault().toString().equals("pl_PL")) {
                        task = new PrelegenciTask(prelegenci_object.getInt("id"),
                                prelegenci_object.getString("name"), prelegenci_object.getString
                                ("info"),
                                prelegenci_object.getString("URLpicture"), prelegenci_object
                                .getString("company"));
                    } else {
                        task = new PrelegenciTask(prelegenci_object.getInt("id"),
                                prelegenci_object.getString("name"), prelegenci_object.getString
                                ("info_en"),
                                prelegenci_object.getString("URLpicture"), prelegenci_object
                                .getString("company_en"));
                    }
                    test = prelegenciDbAdapter.insertPrelegenci(task);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            prelegenciDbAdapter.close();
            handler.sendEmptyMessageDelayed(2, 1000);
        }
    }

}
