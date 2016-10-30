package pl.kebapp.byjacob.fows2016.FragmentyGlowne;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;

import pl.kebapp.byjacob.fows2016.Obiekty.Agenda_Button;
import pl.kebapp.byjacob.fows2016.R;
import pl.kebapp.byjacob.fows2016.SQLiteV2.PrelegenciDbAdapter;
import pl.kebapp.byjacob.fows2016.SQLiteV2.PrelegenciTask;
import pl.kebapp.byjacob.fows2016.SQLiteV2.ProgramDbAdapter;
import pl.kebapp.byjacob.fows2016.SQLiteV2.ProgramTask;

import static pl.kebapp.byjacob.fows2016.SharedPreferences.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class Agenda extends Fragment {

    private SharedPreferences preferences;

    public Agenda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Przycisk close male okno
        View.OnClickListener closeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                        .agenda_male_okno);
                int margines = (int) (50 * Resources.getSystem().getDisplayMetrics()
                        .density);
                TranslateAnimation animate = new TranslateAnimation(0, 0, 0,
                        male_okno
                                .getHeight() + margines);
                animate.setDuration(100);
                animate.setFillAfter(true);
                male_okno.startAnimation(animate);
                ImageView zdjecie = (ImageView) getView().findViewById(R.id.male_okno_zdjecie_agenda);
                zdjecie.setImageResource(R.color.transparent);
                //male_okno.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        male_okno.clearAnimation();
                        male_okno.setVisibility(View.INVISIBLE);
                    }
                }, 120);
            }
        };
        ImageButton close = (ImageButton) getView().findViewById(R.id.button_close_male_agenda);
        close.setOnClickListener(closeClickListener);
        ImageView close2 = (ImageView) getView().findViewById(R.id.imageView_close_male_agendaD);
        close2.setOnClickListener(closeClickListener);
        ImageView close3 = (ImageView) getView().findViewById(R.id.imageView_close_male_agendaG);
        close3.setOnClickListener(closeClickListener);
        ImageView close4 = (ImageView) getView().findViewById(R.id.imageView_close_male_agendaL);
        close4.setOnClickListener(closeClickListener);
        ImageView close5 = (ImageView) getView().findViewById(R.id.imageView_close_male_agendaP);
        close5.setOnClickListener(closeClickListener);
        //Ustawieanie czcionki
        TextView x21 = (TextView) getView().findViewById(R.id.textView21X);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        x21.setTypeface(face);
        TextView piatek_text = (TextView) getView().findViewById(R.id.textViewPiatek);
        piatek_text.setTypeface(face);

        TextView x22 = (TextView) getView().findViewById(R.id.textView22X);
        x22.setTypeface(face);
        TextView sobota_text = (TextView) getView().findViewById(R.id.textViewSobota);
        sobota_text.setTypeface(face);

        final LinearLayout piatek = (LinearLayout) getView().findViewById(R.id.agenda_piatek);
        final LinearLayout sobota = (LinearLayout) getView().findViewById(R.id.agenda_sobota);

        preferences = this.getActivity().getSharedPreferences(WERSJE_TABEL_SP, Activity
                .MODE_PRIVATE);

        ProgramDbAdapter programDBAdapter = new ProgramDbAdapter(getView().getContext());
        programDBAdapter.open(preferences.getInt(PROGRAM_SP, 0));
        int i = 1;
        while (programDBAdapter.getProgram(i) != null) {
            final ProgramTask task = programDBAdapter.getProgram(i);
            Integer id = task.getmDate();
            final Agenda_Button pierwszy = new Agenda_Button(getView().getContext());
            String data = task.getmStartH() + ":" + task.getmStartM() + "-" + task.getmEndH() +
                    ":" + task.getmEndM();
            if(task.getmStartH()<10)
                data = "0" + task.getmStartH() + ":";
            else
                data = task.getmStartH() + ":";
            if(task.getmStartM()<10)
                data += "0" + task.getmStartM() + "-";
            else
                data += task.getmStartM() + "-";
            if(task.getmEndH()<10)
                data += "0" + task.getmEndH() + ":";
            else
                data += task.getmEndH() + ":";
            if(task.getmEndM()<10)
                data += "0" + task.getmEndM();
            else
                data += task.getmEndM();

            final PrelegenciDbAdapter prelegenciDbAdapter = new PrelegenciDbAdapter(getView()
                    .getContext());
            prelegenciDbAdapter.open(preferences.getInt(PRELEGENCI_SP, 0));
            String imie = null;
            PrelegenciTask prelegenciTask = null;
            if (prelegenciDbAdapter.getPrelegenci(task.getmSpeakerID()) != null && task
                    .getmSpeakerID() > 0) {
                imie = prelegenciDbAdapter.getPrelegenci(task.getmSpeakerID()).getmNAME();
                prelegenciTask = prelegenciDbAdapter.getPrelegenci(task.getmSpeakerID());
            }
            prelegenciDbAdapter.close();
            pierwszy.create(data, task.getmLang(), task.getmTheme(), imie);
            final PrelegenciTask finalPrelegenciTask = prelegenciTask;
            final View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkAvailable()) {
                        ImageView zdjecie = (ImageView) getView().findViewById(R.id
                                .male_okno_zdjecie_agenda);
                        new DownloadImageTask(zdjecie).execute(finalPrelegenciTask.getmURLPICTURE());
                    }
                    TextView informacja = (TextView) getView().findViewById(R.id
                            .textView_male_okno_agenda);
                    informacja.setText(finalPrelegenciTask.getmINFO());
                    RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                            .agenda_male_okno);
                    int margines = (int) (50 * Resources.getSystem().getDisplayMetrics()
                            .density);
                    TranslateAnimation animate = new TranslateAnimation(0, 0, male_okno
                            .getHeight() + margines,
                            0);
                    animate.setDuration(100);
                    animate.setFillAfter(true);
                    male_okno.startAnimation(animate);
                    male_okno.setVisibility(View.VISIBLE);
                }
            };
            if (id.equals(21)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        piatek.addView(pierwszy);
                        piatek.addView(pierwszy.getPasek());
                        if (pierwszy.getmAuthorTextView() != null) {
                            pierwszy.getmAuthorTextView().setOnClickListener(clickListener);
                        }

                    }
                });
            } else if (id.equals(22)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sobota.addView(pierwszy);
                        sobota.addView(pierwszy.getPasek());
                        if (pierwszy.getmAuthorTextView() != null) {
                            pierwszy.getmAuthorTextView().setOnClickListener(clickListener);
                        }

                    }
                });
            }
            i++;
        }
        programDBAdapter.close();
        /*final Agenda_Button pierwszy = new Agenda_Button(getView().getContext());
        pierwszy.create("01:01-01:01", "pl", "Temat", "Autor");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                piatek.addView(pierwszy);
                piatek.addView(pierwszy.getPasek());

            }
        });*/
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (bmImage != null)
                bmImage.setImageBitmap(result);
        }
    }
}
