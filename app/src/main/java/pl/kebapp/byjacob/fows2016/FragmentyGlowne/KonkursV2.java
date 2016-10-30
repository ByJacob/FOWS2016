package pl.kebapp.byjacob.fows2016.FragmentyGlowne;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import pl.kebapp.byjacob.fows2016.JSONParser;
import pl.kebapp.byjacob.fows2016.Obiekty.konkursTask;
import pl.kebapp.byjacob.fows2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KonkursV2 extends Fragment {

    private final String mUstawieniaKonkursNazwa = "fows2016_Konkurs";
    private final String mIdentyfikator = "identyfikator";
    //Handler
    Handler handler;
    private EditText mImie;
    private EditText mNazwisko;
    private EditText mAlbum;
    private SharedPreferences mUstawieniaKonkurs;
    private konkursTask[] mPytania = null;
    //Okna
    private RelativeLayout mOknoRejstracji;
    private RelativeLayout mOknoKonkursu;
    private TextView mNRpytania;
    private TextView mPytanie;
    private Typeface mFace;
    private Button mOdp1;
    private Button mOdp2;
    private Button mOdp3;
    private Button mOdp4;
    private AlertDialog.Builder mPotwierdz_zapis;
    private Button mZapisz;

    public KonkursV2() {
        // Required empty public constructor
    }

    private static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        String brak = "";
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int losowo = 48 + random.nextInt(74);
            brak += (char) losowo;
        }
        return brak;
    }

    private static String codeMACadress() {
        String address = getMacAddr();
        String wynik = "";
        int srednik = (int) ':';
        for (int i = 0; i < address.length(); i++) {
            int litera = (int) address.charAt(i);
            if (litera == srednik)
                continue;
            litera = litera + 95;
            if (litera > 127)
                litera = litera - 94;
            wynik += (char) litera;
        }
        return wynik;
    }

    private String dajKod() {
        String kod = mUstawieniaKonkurs.getString(mIdentyfikator, "brak");
        if (kod.equals("brak")) {
            kod = codeMACadress();
            mUstawieniaKonkurs.edit().putString(mIdentyfikator, kod).apply();
        }
        return kod;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUstawieniaKonkurs = getActivity().getSharedPreferences(mUstawieniaKonkursNazwa, Context
                .MODE_PRIVATE);
        dajKod();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_konkurs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOknoRejstracji = (RelativeLayout) getView().findViewById(R.id.relativ_rejstracja);
        mOknoKonkursu = (RelativeLayout) getView().findViewById(R.id.relativ_konkurs);
        mNRpytania = (TextView) getView().findViewById(R.id.textViewkonkurs_nrPytania);
        mPytanie = (TextView) getView().findViewById(R.id.textViewkonkurs_pytanie);
        mFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        mOdp1 = (Button) getView().findViewById(R.id.konkurs_odp1);
        mOdp2 = (Button) getView().findViewById(R.id.konkurs_odp2);
        mOdp3 = (Button) getView().findViewById(R.id.konkurs_odp3);
        mOdp4 = (Button) getView().findViewById(R.id.konkurs_odp4);
        mImie = (EditText) getView().findViewById(R.id.editTextkonkurs_imie);
        mNazwisko = (EditText) getView().findViewById(R.id.editTextkonkurs_nazwisko);
        mAlbum = (EditText) getView().findViewById(R.id.editTextkonkurs_album);
        mZapisz = (Button) getView().findViewById(R.id.buttonkonkurs_zapisz);

        mPotwierdz_zapis = new AlertDialog.Builder(getView().getContext());
        mPotwierdz_zapis.setTitle(getString(R.string.zapisz));
        mPotwierdz_zapis.setPositiveButton(getString(R.string.akceptuje), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isNetworkAvailable())
                    new Rejstracja().execute();
                else
                    Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast
                            .LENGTH_LONG)
                            .show();
            }
        });
        mPotwierdz_zapis.setNegativeButton(getString(R.string.nieakceptuje), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mPotwierdz_zapis.setCancelable(false);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0://początkowe sprawdzenie czy konkurs
                        new czyKonkurs().execute();
                        break;
                    case 1://czy zarejstrowany
                        new czyZarejstrowany().execute();
                        break;
                    case 2://czy wypelnil odpowiedzi
                        new czyWypelnilOdpowiedzi().execute();
                        break;
                    case 99://pobierz konkurs
                        new dajKonkurs().execute();
                        break;
                }
            }
        };

        handler.sendEmptyMessage(0);//sprawdzenie czy konkurs

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView zarejestruj = (TextView) getView().findViewById(R.id.textViewkonkurs_zarejstruj);
        zarejestruj.setTypeface(mFace);
    }

    private void brakKonkursu() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-mOknoRejstracji.getWidth()
                * 1.5));
        animation.setDuration(1500);
        mOknoRejstracji.setVisibility(View.GONE);
        mOknoRejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOknoKonkursu.setVisibility(View.VISIBLE);
                mOknoRejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNRpytania.setText(R.string.niekatywnyKonkurs_title);
                mNRpytania.setTypeface(mFace);
                mNRpytania.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                mPytanie.setText(getString(R.string.nieaktywnyKonkurs_message) + " " +
                        getString(R.string.app_full_name));
                mPytanie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mOdp1.setVisibility(View.INVISIBLE);
                mOdp2.setVisibility(View.INVISIBLE);
                mOdp3.setVisibility(View.INVISIBLE);
                mOdp4.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void pokazRejstracje() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOknoRejstracji.setVisibility(View.VISIBLE);
            }
        });
        mZapisz.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (!isNetworkAvailable()) {
                                               Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast
                                                       .LENGTH_LONG)
                                                       .show();
                                               return;
                                           }
                                           if (mImie.getText().length() > 1 && mNazwisko.getText().length() > 1 && mAlbum.getText
                                                   ().length() > 1) {
                                               mPotwierdz_zapis.setMessage(getString(R.string.rejstracja_mesage1) +
                                                       "." + "\n" + getString(R.string.imie) + ": " + mImie.getText().toString() +
                                                       "\n" + getString(R.string.nazwisko) + ": " + mNazwisko.getText().toString() + "\n" + getString(R.string.album) + ": " + mAlbum.getText().toString());
                                               mPotwierdz_zapis.show();
                                           } else
                                               Toast.makeText(getView().getContext(), getString(R.string.niewypelnione_pola), Toast
                                                       .LENGTH_SHORT).show();
                                       }
                                   }

        );
    }

    private void brakInternetu() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-mOknoRejstracji.getWidth()
                * 1.5));
        animation.setDuration(1500);
        mOknoRejstracji.setVisibility(View.GONE);
        mOknoRejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOknoKonkursu.setVisibility(View.VISIBLE);
                mOknoRejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNRpytania.setText(getString(R.string.brak_internetu));
                mNRpytania.setTypeface(mFace);
                mNRpytania.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                mPytanie.setText(R.string.brakinternetu_message);
                mPytanie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mOdp1.setVisibility(View.INVISIBLE);
                mOdp2.setVisibility(View.INVISIBLE);
                mOdp3.setVisibility(View.INVISIBLE);
                mOdp4.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void skonczonyKonkurs() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-mOknoRejstracji
                .getWidth()
                * 1.5));
        animation.setDuration(1500);
        mOknoRejstracji.setVisibility(View.GONE);
        mOknoRejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOknoKonkursu.setVisibility(View.VISIBLE);
                mOknoRejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNRpytania.setText(getString(R.string.zrobiony_title));
                mNRpytania.setTypeface(mFace);
                mNRpytania.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                mPytanie.setText(getString(R.string.zrobiony_message));
                mPytanie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mOdp1.setVisibility(View.INVISIBLE);
                mOdp2.setVisibility(View.INVISIBLE);
                mOdp3.setVisibility(View.INVISIBLE);
                mOdp4.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void startKonkurs() {
        mOknoRejstracji = (RelativeLayout) getView().findViewById(R.id
                .relativ_rejstracja);
        mOknoKonkursu = (RelativeLayout) getView().findViewById(R.id.relativ_konkurs);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-mOknoRejstracji.getWidth()
                * 1.5));
        animation.setDuration(1500);
        mOknoRejstracji.setVisibility(View.GONE);
        mOknoRejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOknoKonkursu.setVisibility(View.VISIBLE);
                mOknoRejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        if (mPytania != null) {
            startPytania(0);
        } else {
            brakKonkursu();
        }
    }

    private void startPytania(final int nrPytania) {
        final Button[] odpS = {mOdp1, mOdp2, mOdp3, mOdp4};
        if (nrPytania < mPytania.length) {
            Boolean[] status = {false, false, false, false};
            String[] odpowiedzi = {mPytania[nrPytania].getmODP1(), mPytania[nrPytania].getmODP2()
                    , mPytania[nrPytania].getmODP3(), mPytania[nrPytania].getmODP4()};
            mNRpytania.setText(getString(R.string.pytanie) + " " + String.valueOf(nrPytania + 1) + "/" + String.valueOf
                    (mPytania.length));
            mPytanie.setText(mPytania[nrPytania].getmPYTANIE());
            View.OnClickListener listener = new View.OnClickListener() {
                int temp_nrPytania = nrPytania;

                @Override
                public void onClick(View v) {
                    Integer id = v.getId();
                    switch (id) {
                        case R.id.konkurs_odp1:
                            mPytania[temp_nrPytania].setmNRodp(1);
                            break;
                        case R.id.konkurs_odp2:
                            mPytania[temp_nrPytania].setmNRodp(2);
                            break;
                        case R.id.konkurs_odp3:
                            mPytania[temp_nrPytania].setmNRodp(3);
                            break;
                        case R.id.konkurs_odp4:
                            mPytania[temp_nrPytania].setmNRodp(4);
                            break;
                    }
                    startPytania(temp_nrPytania + 1);
                }
            };
            for (int i = 0; i < 4; i++) {
                Random x = new Random();
                int los = x.nextInt(4);
                while (status[los]) {
                    los = x.nextInt(4);
                }
                status[los] = true;
                odpS[i].setText(odpowiedzi[los]);
                odpS[i].setOnClickListener(listener);
                odpS[i].setVisibility(View.VISIBLE);
                switch (los) {
                    case 0:
                        odpS[i].setId(R.id.konkurs_odp1);
                        break;
                    case 1:
                        odpS[i].setId(R.id.konkurs_odp2);
                        break;
                    case 2:
                        odpS[i].setId(R.id.konkurs_odp3);
                        break;
                    case 3:
                        odpS[i].setId(R.id.konkurs_odp4);
                        break;
                }
            }

        } else {
            String wynik = "";
            for (int i = 0; i < mPytania.length; i++)
                wynik += mPytania[i].getmNRodp();
            if (isNetworkAvailable())
                new wyslijOdpowiedz(wynik).execute();
            else {
                Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast.LENGTH_LONG)
                        .show();
                startPytania(0);
            }
        }
    }

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private class czyKonkurs extends AsyncTask<Void, Void, JSONObject> {
        public static final String TAG_MESSAGES = "status";
        final String URL = "http://137.74.171.255/konkurs_start.php";
        final String TAG_SUCCESS = "OK";
        JSONParser jsonParser = new JSONParser();

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String, String> nie = new HashMap<>();
                nie.put("kod", "kod");
                JSONObject json = jsonParser.makeHttpRequest(URL, "GET", nie);
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
            String wynik;
            if (jsonObject != null) {
                try {
                    wynik = jsonObject.getString(TAG_MESSAGES);
                    if (wynik.equals(TAG_SUCCESS)) {
                        handler.sendEmptyMessage(1);//sprawdzenie czy zarejstrował się użytkownik;
                    } else {
                        brakKonkursu();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("KONKURS", "Błąd pobierania informacji o stanie konkursu: " + e.toString());
                }
            } else
                brakInternetu();
        }
    }

    private class czyZarejstrowany extends AsyncTask<Void, Void, JSONObject> {
        public static final String TAG_MESSAGES = "status";
        final String URL = "http://137.74.171.255/db_sprawdz_rejstracja.php";
        final String TAG_SUCCESS = "OK";
        JSONParser jsonParser = new JSONParser();
        private String kod;

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                kod = dajKod();
                HashMap<String, String> nie = new HashMap<>();
                nie.put("kod", kod);
                JSONObject json = jsonParser.makeHttpRequest(URL, "GET", nie);
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
            String wynik;
            if (jsonObject != null) {
                try {
                    wynik = jsonObject.getString(TAG_MESSAGES);
                    if (wynik.equals(TAG_SUCCESS)) {
                        handler.sendEmptyMessage(2);//zapytaj czy uzytkownik wypełnił odpowiedzi już
                    } else {
                        pokazRejstracje();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("KONKURS", "Błąd pobierania informacji o rejstracji użytkownika: " + e.toString());
                }
            } else
                brakInternetu();
        }
    }

    private class czyWypelnilOdpowiedzi extends AsyncTask<Void, Void, JSONObject> {
        public static final String TAG_MESSAGES = "status";
        final String URL = "http://137.74.171.255/db_czySkonczyl.php";
        final String TAG_SUCCESS = "OK";
        JSONParser jsonParser = new JSONParser();
        private String kod;

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                kod = dajKod();
                HashMap<String, String> nie = new HashMap<>();
                nie.put("kod", kod);
                JSONObject json = jsonParser.makeHttpRequest(URL, "GET", nie);
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
            String wynik;
            if (jsonObject != null) {
                try {
                    wynik = jsonObject.getString(TAG_MESSAGES);
                    if (wynik.equals(TAG_SUCCESS)) {
                        skonczonyKonkurs();
                    } else {
                        handler.sendEmptyMessage(99);//pobierz konkurs
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("KONKURS", "Błąd pobierania informacji o stanie odpowiedzi: " + e.toString());
                }
            } else
                brakInternetu();
        }
    }

    private class dajKonkurs extends AsyncTask<Void, Void, JSONObject> {
        public static final String TAG_MESSAGES = "status";
        final String URL = "http://137.74.171.255/db_dajKonkurs.php";
        final String TAG_SUCCESS = "OK";
        JSONParser jsonParser = new JSONParser();

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String, String> nie = new HashMap<>();
                nie.put("imie", "nie");
                JSONObject json = jsonParser.makeHttpRequest(URL, "GET", nie);
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
            String wynik;
            try {
                wynik = jsonObject.getString(TAG_MESSAGES);
                if (wynik.equals(TAG_SUCCESS)) {
                    program_array = jsonObject.getJSONArray("result");
                    mPytania = new konkursTask[program_array.length()];
                    for (int i = 0; i < program_array.length(); i++) {
                        program_object = program_array.getJSONObject(i);
                        mPytania[i] = new konkursTask(program_object.getInt("id"), program_object
                                .getString("pytanie"), program_object.getString("odp1"), program_object
                                .getString("odp2"), program_object.getString("odp3"), program_object
                                .getString("odp4"));

                    }
                } else {
                    mPytania = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mPytania != null)
                startKonkurs();
            else
                brakKonkursu();
        }
    }

    private class Rejstracja extends AsyncTask<Void, Void, JSONObject> {

        public static final String TAG_MESSAGES = "status";
        final String URL = "http://137.74.171.255/db_rejstracja.php";
        final String TAG_SUCCESS = "OK";
        JSONParser jsonParser = new JSONParser();

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                String kod = codeMACadress();
                HashMap<String, String> nie = new HashMap<>();
                nie.put("imie", mImie.getText().toString());
                nie.put("nazwisko", mNazwisko.getText().toString());
                nie.put("album", mAlbum.getText().toString());
                nie.put("kod", kod);
                JSONObject json = jsonParser.makeHttpRequest(URL, "GET", nie);
                if (json != null) {
                    return json;
                } else {
                    mUstawieniaKonkurs.edit().putString(mUstawieniaKonkursNazwa, kod).apply();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String wynik;
            if (jsonObject != null) {
                try {
                    wynik = jsonObject.getString(TAG_MESSAGES);
                    if (wynik.equals(TAG_SUCCESS)) {
                        handler.sendEmptyMessage(99);//pokazanie konkursu po rejstracji
                    } else {
                        Toast.makeText(getView().getContext(), getString(R.string
                                .nieudana_rejstracja), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("KONKURS", "Błąd pobierania kodu użytkownika: " + e.toString());
                }
            }
        }
    }

    private class wyslijOdpowiedz extends AsyncTask<Void, Void, JSONObject> {
        public static final String TAG_MESSAGES = "status";
        final String URL = "http://137.74.171.255/db_setOdpowiedzi.php";
        final String TAG_SUCCESS = "OK";
        JSONParser jsonParser = new JSONParser();
        private String odp;

        private wyslijOdpowiedz(String odp) {
            this.odp = odp;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                String test = dajKod();
                HashMap<String, String> nie = new HashMap<>();
                nie.put("kod", test);
                nie.put("odp", this.odp);
                JSONObject json = jsonParser.makeHttpRequest(URL, "GET", nie);
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
            String wynik;
            if (jsonObject != null) {
                try {
                    wynik = jsonObject.getString(TAG_MESSAGES);
                    if (wynik.equals(TAG_SUCCESS)) {
                        Toast.makeText(getView().getContext(), getString(R.string.konkurs_dziekujemy), Toast.LENGTH_LONG).show();
                        skonczonyKonkurs();
                    } else {
                        Toast.makeText(getView().getContext(), getString(R.string.konkurs_blad),
                                Toast.LENGTH_LONG).show();
                        startPytania(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("KONKURS", "Błąd wysyłania odpowiedzi: " + e.toString());
                }
            } else
                brakInternetu();
        }
    }

}
