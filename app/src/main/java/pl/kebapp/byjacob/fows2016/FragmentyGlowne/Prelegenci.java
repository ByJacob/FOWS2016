package pl.kebapp.byjacob.fows2016.FragmentyGlowne;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;

import pl.kebapp.byjacob.fows2016.Obiekty.Prelegenci_Button;
import pl.kebapp.byjacob.fows2016.R;
import pl.kebapp.byjacob.fows2016.SQLiteV2.PrelegenciDbAdapter;
import pl.kebapp.byjacob.fows2016.SQLiteV2.PrelegenciTask;

import static pl.kebapp.byjacob.fows2016.SharedPreferences.PRELEGENCI_SP;
import static pl.kebapp.byjacob.fows2016.SharedPreferences.WERSJE_TABEL_SP;

/**
 * A simple {@link Fragment} subclass.
 */
public class Prelegenci extends Fragment{

    private SharedPreferences preferences;

    public Prelegenci() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prelegenci, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //Przycisk close male okno
        View.OnClickListener closeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                        .prelegenci_male_okno);
                int margines = (int) (50 * Resources.getSystem().getDisplayMetrics()
                        .density);
                TranslateAnimation animate = new TranslateAnimation(0, 0, 0,
                        male_okno
                                .getHeight()+margines);
                animate.setDuration(100);
                animate.setFillAfter(true);
                male_okno.startAnimation(animate);
                ImageView zdjecie = (ImageView) getView().findViewById(R.id.male_okno_zdjecie);
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
        ImageButton close = (ImageButton) getView().findViewById(R.id.button_close_male_prelegenci);
        close.setOnClickListener(closeClickListener);
        ImageView close2 = (ImageView) getView().findViewById(R.id
                .imageView_close_male_prelegenciG);
        close2.setOnClickListener(closeClickListener);
        ImageView close3 = (ImageView) getView().findViewById(R.id
                .imageView_close_male_prelegenciD);
        close3.setOnClickListener(closeClickListener);
        ImageView close4 = (ImageView) getView().findViewById(R.id
                .imageView_close_male_prelegenciL);
        close4.setOnClickListener(closeClickListener);
        ImageView close5 = (ImageView) getView().findViewById(R.id
                .imageView_close_male_prelegenciP);
        close5.setOnClickListener(closeClickListener);


        preferences = this.getActivity().getSharedPreferences(WERSJE_TABEL_SP, Activity
                .MODE_PRIVATE);
        PrelegenciDbAdapter prelegenciDbAdapter = new PrelegenciDbAdapter(getView().getContext());
        prelegenciDbAdapter.open(preferences.getInt(PRELEGENCI_SP, 0));
        int i = 1;
        while (prelegenciDbAdapter.getPrelegenci(i) != null) {
            final PrelegenciTask task = prelegenciDbAdapter.getPrelegenci(i);
            final Prelegenci_Button button = new Prelegenci_Button(getView().getContext());
            button.create(task.getmNAME(), task.getmCOMPANY(), getActivity());
            final LinearLayout agenda = (LinearLayout) getView().findViewById(R.id.scroll_agenda);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    agenda.addView(button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isNetworkAvailable()) {
                                ImageView zdjecie = (ImageView) getView().findViewById(R.id.male_okno_zdjecie);
                                new DownloadImageTask(zdjecie).execute(task.getmURLPICTURE());
                            }
                            TextView informacja = (TextView) getView().findViewById(R.id.textView_male_okno);
                            informacja.setText(task.getmINFO());
                            RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                                    .prelegenci_male_okno);
                            int margines = (int) (50 * Resources.getSystem().getDisplayMetrics()
                                    .density);
                            TranslateAnimation animate = new TranslateAnimation(0, 0, male_okno
                                    .getHeight()+margines,
                                    0);
                            animate.setDuration(100);
                            animate.setFillAfter(true);
                            male_okno.startAnimation(animate);
                            male_okno.setVisibility(View.VISIBLE);
                        }
                    });
                    button.setClickable(true);

                }
            });
            i++;
        }
        prelegenciDbAdapter.close();

        super.onActivityCreated(savedInstanceState);
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
