package pl.kebapp.byjacob.fows2016;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.text.method.TransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import pl.kebapp.byjacob.fows2016.FragmentyGlowne.Agenda;
import pl.kebapp.byjacob.fows2016.FragmentyGlowne.Konkurs;
import pl.kebapp.byjacob.fows2016.FragmentyGlowne.KonkursV2;
import pl.kebapp.byjacob.fows2016.FragmentyGlowne.Linki;
import pl.kebapp.byjacob.fows2016.FragmentyGlowne.Mapa;
import pl.kebapp.byjacob.fows2016.FragmentyGlowne.OKonferencji;
import pl.kebapp.byjacob.fows2016.FragmentyGlowne.Prelegenci;
import pl.kebapp.byjacob.fows2016.FragmentyGlowne.Spons_Patron;
import pl.kebapp.byjacob.fows2016.Obiekty.ScrollTextView;

public class OknoGlowne extends AppCompatActivity {

    ArrayList<Integer> fragmenty_lista = new ArrayList<Integer>();
    Handler handler_news = null;
    private final String DEBUG_TAG = "MainWindow";

    public OknoGlowne() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okno_glowne);
        Toolbar toolbar = (Toolbar) findViewById(R.id.glowny_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(R.string.app_full_name);
        if (fragmenty_lista.isEmpty()) {
            Fragment fragment = new Agenda();
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_glowny, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            getSupportActionBar().setTitle("Agenda - FoWS2016");
            fragmenty_lista.add(R.id.menu_agenda);
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onStart() {
        ScrollTextView news = (ScrollTextView) findViewById(R.id.textViewNEWS);
        news.startScroll();
        super.onStart();
        handler_news = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                new GetNews().execute();
                handler_news.sendEmptyMessageDelayed(0, 30000);
            }
        };
        handler_news.sendEmptyMessage(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.glowne_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (fragmenty_lista.size() < 2) {
            Toast.makeText(getApplicationContext(), "FoWS2016: Do widzenia :)", Toast
                    .LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            }, 1000);
        } else {
            fragmenty_lista.remove(fragmenty_lista.size() - 1);
            switch (fragmenty_lista.get(fragmenty_lista.size() - 1)) {
                case R.id.menu_agenda:
                    getSupportActionBar().setTitle(getString(R.string.menu_agenda) +" - FoWS2016");
                    break;
                case R.id.menu_linki:
                    getSupportActionBar().setTitle(getString(R.string.menu_linki)+" - FoWS2016");
                    break;
                case R.id.menu_mapa:
                    getSupportActionBar().setTitle(getString(R.string.menu_mapa)+" - FoWS2016");
                    break;
                case R.id.menu_o_konferencji:
                    getSupportActionBar().setTitle(getString(R.string.menu_o_konferencji)+" - " +
                            "FoWS2016");
                    break;
                case R.id.menu_prelegenci:
                    getSupportActionBar().setTitle(getString(R.string.menu_prelegenci)+" - " +
                            "FoWS2016");
                    break;
                case R.id.menu_spons_patr:
                    getSupportActionBar().setTitle(getString(R.string.menu_sponsorzy_i_patroni)+"" +
                            " - FoWS2016");
                    break;
                case R.id.menu_konkurs:
                    getSupportActionBar().setTitle(getString(R.string.menu_konkurs)+"" +
                            " - FoWS2016");
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        android.app.FragmentTransaction transaction;
        switch (item.getItemId()) {
            case R.id.menu_agenda:
                fragment = new Agenda();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_glowny, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle(getString(R.string.menu_agenda)+" - FoWS2016");
                fragmenty_lista.add(R.id.menu_agenda);
                break;
            case R.id.menu_prelegenci:
                fragment = new Prelegenci();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_glowny, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle(getString(R.string.menu_prelegenci)+" - FoWS2016");
                fragmenty_lista.add(R.id.menu_prelegenci);
                break;
            case R.id.menu_spons_patr:
                fragment = new Spons_Patron();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_glowny, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle(getString(R.string.menu_sponsorzy_i_patroni)+" - " +
                        "FoWS2016");
                fragmenty_lista.add(R.id.menu_spons_patr);
                break;
            case R.id.menu_o_konferencji:
                fragment = new OKonferencji();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_glowny, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle(getString(R.string.menu_o_konferencji)+" - " +
                        "FoWS2016");
                fragmenty_lista.add(R.id.menu_o_konferencji);
                break;
            case R.id.menu_linki:
                fragment = new Linki();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_glowny, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle(getString(R.string.menu_linki)+" - FoWS2016");
                fragmenty_lista.add(R.id.menu_linki);
                break;
            case R.id.menu_mapa:
                fragment = new Mapa();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_glowny, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle(getString(R.string.menu_mapa)+" - FoWS2016");
                fragmenty_lista.add(R.id.menu_mapa);
                break;
            case R.id.menu_konkurs:
                fragment = new KonkursV2();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_glowny, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle(getString(R.string.menu_konkurs)+" - FoWS2016");
                fragmenty_lista.add(R.id.menu_konkurs);
                break;
            case R.id.menu_rejstracja:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://fows.pwr.edu.pl/rejestracja"));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetNews extends AsyncTask<Void, Void, JSONObject> {

        JSONParser jsonParser = new JSONParser();
        final String URL = "http://137.74.171.255/db_dajNews.php";
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
            String[] wiadomosci;
            String tekst = getString(R.string.witamy_news);
            if (jsonObject != null) {
                try {
                    tekst = ".::";
                    tabele_array = jsonObject.getJSONArray("result");
                    wiadomosci = new String[tabele_array.length()];
                    for (int i = 0; i < tabele_array.length(); i++) {
                        tabele_object = tabele_array.getJSONObject(i);
                        if(Locale.getDefault().toString().equals("pl_PL")) {
                            tekst += tabele_object.getString("text");
                        }
                        else
                        {
                            tekst += tabele_object.getString("text_en");
                        }
                        if(i<tabele_array.length()-1)
                        {
                            tekst += " :: ";
                        }
                        else
                        {
                            tekst += "::.";
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(DEBUG_TAG, "Błąd pobierania news: " + e.toString());
                }
                ScrollTextView news = (ScrollTextView) findViewById(R.id.textViewNEWS);
                news.setText(tekst);
                news.startScroll();
            }
            if (jsonObject == null) //jeżeli nie uda się pobrać json pokazuje błąd i nie
            // próbuje aktualizować plików
            {

            }
        }
    }
}
