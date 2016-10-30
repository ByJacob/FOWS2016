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
public class Konkurs extends Fragment {

    private final String mUstawieniaKonkursNazwa = "fows2016_Konkurs";
    private final String mIdentyfikator = "identyfikator";
    private EditText mImie;
    private EditText mNazwisko;
    private EditText mAlbum;
    private SharedPreferences mUstawieniaKonkurs;
    private konkursTask[] mPytania = null;
    private int mNRpytania;

    public Konkurs() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_konkurs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //czcionka
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        TextView zarejestruj = (TextView) getView().findViewById(R.id.textViewkonkurs_zarejstruj);
        zarejestruj.setTypeface(face);
        //obiekty EditText
        mImie = (EditText) getView().findViewById(R.id.editTextkonkurs_imie);
        mNazwisko = (EditText) getView().findViewById(R.id.editTextkonkurs_nazwisko);
        mAlbum = (EditText) getView().findViewById(R.id.editTextkonkurs_album);
        //okno potwierdzenia zapisu
        final AlertDialog.Builder potwierdz_zapis = new AlertDialog.Builder(getView().getContext());
        potwierdz_zapis.setTitle(getString(R.string.zapisz));
        potwierdz_zapis.setPositiveButton(getString(R.string.akceptuje), new DialogInterface.OnClickListener() {
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
        potwierdz_zapis.setNegativeButton(getString(R.string.nieakceptuje), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //przycisk zapisz
        Button zapisz = (Button) getView().findViewById(R.id.buttonkonkurs_zapisz);
        zapisz.setOnClickListener(new View.OnClickListener() {
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
                                              potwierdz_zapis.setMessage(getString(R.string.rejstracja_mesage1) +
                                                      "." + "\n" + getString(R.string.imie) + ": " + mImie.getText().toString() +
                                                      "\n" + getString(R.string.nazwisko) + ": (" + mNazwisko.getText().toString() + "\n" + getString(R.string.album) + ": " + mAlbum.getText().toString());
                                              potwierdz_zapis.show();
                                          } else
                                              Toast.makeText(getView().getContext(), getString(R.string.niewypelnione_pola), Toast
                                                      .LENGTH_SHORT).show();
                                      }
                                  }

        );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUstawieniaKonkurs = getActivity().getSharedPreferences(mUstawieniaKonkursNazwa, Context
                .MODE_PRIVATE);
        if (isNetworkAvailable())
            new Status().execute();
        else {
            Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast.LENGTH_LONG).show();
            brakInternetu();
        }

    }

    private String codeMACadress() {
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

    private void startKonkurs() {
        final RelativeLayout oknorejstracji = (RelativeLayout) getView().findViewById(R.id
                .relativ_rejstracja);
        final RelativeLayout oknokonkursu = (RelativeLayout) getView().findViewById(R.id.relativ_konkurs);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-oknorejstracji.getWidth()
                * 1.5));
        animation.setDuration(1500);
        oknorejstracji.setVisibility(View.GONE);
        oknorejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oknokonkursu.setVisibility(View.VISIBLE);
                oknorejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        if (mPytania != null) {
            startPytania(0);
        } else {
            brakKonkursu();
        }
    }

    private void brakKonkursu() {
        final RelativeLayout oknorejstracji = (RelativeLayout) getView().findViewById(R.id.relativ_rejstracja);
        final RelativeLayout oknokonkursu = (RelativeLayout) getView().findViewById(R.id.relativ_konkurs);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-oknorejstracji.getWidth()
                * 1.5));
        animation.setDuration(1500);
        oknorejstracji.setVisibility(View.GONE);
        oknorejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oknokonkursu.setVisibility(View.VISIBLE);
                oknorejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        final TextView nrpytania = (TextView) getView().findViewById(R.id.textViewkonkurs_nrPytania);
        final TextView pytanie = (TextView) getView().findViewById(R.id.textViewkonkurs_pytanie);
        final Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        final Button odp1 = (Button) getView().findViewById(R.id.konkurs_odp1);
        final Button odp2 = (Button) getView().findViewById(R.id.konkurs_odp2);
        final Button odp3 = (Button) getView().findViewById(R.id.konkurs_odp3);
        final Button odp4 = (Button) getView().findViewById(R.id.konkurs_odp4);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nrpytania.setText(R.string.niekatywnyKonkurs_title);
                nrpytania.setTypeface(face);
                nrpytania.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                pytanie.setText(getString(R.string.nieaktywnyKonkurs_message) +" " +
                        getString(R.string.app_full_name));
                pytanie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                odp1.setVisibility(View.INVISIBLE);
                odp2.setVisibility(View.INVISIBLE);
                odp3.setVisibility(View.INVISIBLE);
                odp4.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void brakInternetu() {
        final RelativeLayout oknorejstracji = (RelativeLayout) getView().findViewById(R.id.relativ_rejstracja);
        final RelativeLayout oknokonkursu = (RelativeLayout) getView().findViewById(R.id.relativ_konkurs);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-oknorejstracji.getWidth()
                * 1.5));
        animation.setDuration(1500);
        oknorejstracji.setVisibility(View.GONE);
        oknorejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oknokonkursu.setVisibility(View.VISIBLE);
                oknorejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        final TextView nrpytania = (TextView) getView().findViewById(R.id.textViewkonkurs_nrPytania);
        final TextView pytanie = (TextView) getView().findViewById(R.id.textViewkonkurs_pytanie);
        final Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        final Button odp1 = (Button) getView().findViewById(R.id.konkurs_odp1);
        final Button odp2 = (Button) getView().findViewById(R.id.konkurs_odp2);
        final Button odp3 = (Button) getView().findViewById(R.id.konkurs_odp3);
        final Button odp4 = (Button) getView().findViewById(R.id.konkurs_odp4);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nrpytania.setText(getString(R.string.brak_internetu));
                nrpytania.setTypeface(face);
                nrpytania.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                pytanie.setText(R.string.brakinternetu_message);
                pytanie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                odp1.setVisibility(View.INVISIBLE);
                odp2.setVisibility(View.INVISIBLE);
                odp3.setVisibility(View.INVISIBLE);
                odp4.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void skonczonyKonkurs() {
        final RelativeLayout oknorejstracji = (RelativeLayout) getView().findViewById(R.id.relativ_rejstracja);
        final RelativeLayout oknokonkursu = (RelativeLayout) getView().findViewById(R.id.relativ_konkurs);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (float) (-oknorejstracji.getWidth()
                * 1.5));
        animation.setDuration(1500);
        oknorejstracji.setVisibility(View.GONE);
        oknorejstracji.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oknokonkursu.setVisibility(View.VISIBLE);
                oknorejstracji.setVisibility(View.INVISIBLE);
            }
        }, 1500);
        final TextView nrpytania = (TextView) getView().findViewById(R.id.textViewkonkurs_nrPytania);
        final Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        final TextView pytanie = (TextView) getView().findViewById(R.id.textViewkonkurs_pytanie);
        final Button odp1 = (Button) getView().findViewById(R.id.konkurs_odp1);
        final Button odp2 = (Button) getView().findViewById(R.id.konkurs_odp2);
        final Button odp3 = (Button) getView().findViewById(R.id.konkurs_odp3);
        final Button odp4 = (Button) getView().findViewById(R.id.konkurs_odp4);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nrpytania.setText(getString(R.string.zrobiony_title));
                nrpytania.setTypeface(face);
                nrpytania.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                pytanie.setText(getString(R.string.zrobiony_message));
                pytanie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                odp1.setVisibility(View.INVISIBLE);
                odp2.setVisibility(View.INVISIBLE);
                odp3.setVisibility(View.INVISIBLE);
                odp4.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void startPytania(final int nrPytania) {
        final TextView nrpytania = (TextView) getView().findViewById(R.id.textViewkonkurs_nrPytania);
        final TextView pytanie = (TextView) getView().findViewById(R.id.textViewkonkurs_pytanie);
        final Button odp1 = (Button) getView().findViewById(R.id.konkurs_odp1);
        final Button odp2 = (Button) getView().findViewById(R.id.konkurs_odp2);
        final Button odp3 = (Button) getView().findViewById(R.id.konkurs_odp3);
        final Button odp4 = (Button) getView().findViewById(R.id.konkurs_odp4);
        final Button[] odpS = {odp1, odp2, odp3, odp4};
        if (nrPytania < mPytania.length) {
            Boolean[] status = {false, false, false, false};
            String[] odpowiedzi = {mPytania[nrPytania].getmODP1(), mPytania[nrPytania].getmODP2()
                    , mPytania[nrPytania].getmODP3(), mPytania[nrPytania].getmODP4()};
            nrpytania.setText(getString(R.string.pytanie)+" " + String.valueOf(nrPytania + 1) + "/" + String.valueOf
                    (mPytania.length));
            pytanie.setText(mPytania[nrPytania].getmPYTANIE());
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
            if (jsonObject != null) {
                Toast.makeText(getView().getContext(), getString(R.string.dziekujemy_rejstracja),
                        Toast.LENGTH_SHORT).show();
                if (isNetworkAvailable())
                    new czyKonkurs().execute();
                else
                    Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast
                            .LENGTH_LONG)
                            .show();
            } else {
                Toast.makeText(getView().getContext(), getString(R.string.nieudana_rejstracja),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Status extends AsyncTask<Void, Void, JSONObject> {

        public static final String TAG_MESSAGES = "status";
        final String URL = "http://137.74.171.255/db_sprawdz_rejstracja.php";
        final String TAG_SUCCESS = "OK";
        final String TAG_FINISH = "FINISH";
        JSONParser jsonParser = new JSONParser();
        private String kod;

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                kod = mUstawieniaKonkurs.getString(mUstawieniaKonkursNazwa, "brak");
                if (kod.equals("brak"))
                    kod = getMacAddr();
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
                        mUstawieniaKonkurs.edit().putString(mUstawieniaKonkursNazwa, kod).apply();
                        if (isNetworkAvailable())
                            new czyKonkurs().execute();
                        else
                            Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast
                                    .LENGTH_LONG).show();
                    } else if (wynik.equals(TAG_FINISH)) {
                        skonczonyKonkurs();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("KONKURS", "Błąd pobierania kodu użytkownika: " + e.toString());
                }
            }
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
                if (isNetworkAvailable())
                    startKonkurs();
                else
                    Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast.LENGTH_LONG).show();
            else
                brakKonkursu();
        }
    }

    private class czyKonkurs extends AsyncTask<Void, Void, JSONObject> {
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
                    if (isNetworkAvailable())
                        new dajKonkurs().execute();
                    else
                        Toast.makeText(getView().getContext(), getString(R.string.brak_internetu), Toast.LENGTH_LONG).show();

                } else {
                    brakKonkursu();
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
                String test = mUstawieniaKonkurs.getString(mUstawieniaKonkursNazwa, "brak");
                if (test.equals("brak"))
                    test = getMacAddr();
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("KONKURS", "Błąd wysyłania odpowiedzi: " + e.toString());
                }
            }
        }
    }
}
