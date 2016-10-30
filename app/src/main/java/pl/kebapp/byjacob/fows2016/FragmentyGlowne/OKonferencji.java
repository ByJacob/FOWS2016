package pl.kebapp.byjacob.fows2016.FragmentyGlowne;


import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.kebapp.byjacob.fows2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OKonferencji extends Fragment {


    public OKonferencji() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_okonferencji, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View.OnClickListener closeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                        .okonferencji_male_okno);
                int margines = (int) (50 * Resources.getSystem().getDisplayMetrics()
                        .density);
                TranslateAnimation animate = new TranslateAnimation(0, 0, 0,
                        male_okno
                                .getHeight() + margines);
                animate.setDuration(100);
                animate.setFillAfter(true);
                male_okno.startAnimation(animate);
                ImageView zdjecie = (ImageView) getView().findViewById(R.id.male_okno_zdjecie_okonferencji);
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
        ImageButton cancel = (ImageButton) getView().findViewById(R.id.button_close_male_okonferencji);
        cancel.setOnClickListener(closeListener);
        ImageView close2 = (ImageView) getView().findViewById(R.id.imageView_close_male_okonferencjiD);
        close2.setOnClickListener(closeListener);
        ImageView close3 = (ImageView) getView().findViewById(R.id.imageView_close_male_okonferencjiG);
        close3.setOnClickListener(closeListener);
        ImageView close4 = (ImageView) getView().findViewById(R.id.imageView_close_male_okonferencjiL);
        close4.setOnClickListener(closeListener);
        ImageView close5 = (ImageView) getView().findViewById(R.id.imageView_close_male_okonferencjiP);
        close5.setOnClickListener(closeListener);
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        TextView view = (TextView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.setBackgroundResource(R.drawable.rounded_edges_pressed);
                        view.invalidate();
                        v.callOnClick();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        TextView view = (TextView) v;
                        //clear the overlay
                        view.setBackgroundResource(R.drawable.rounded_edges_normal);
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        };
        TextView fows2010 = (TextView) getView().findViewById(R.id.textView_fows2010);
        fows2010.setOnTouchListener(touchListener);
        fows2010.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView zdjecie = (ImageView) getView().findViewById(R.id
                        .male_okno_zdjecie_okonferencji);
                zdjecie.setImageResource(R.drawable.fows2010);

                TextView informacja = (TextView) getView().findViewById(R.id
                        .textView_male_okno_okonferencji);
                informacja.setText(R.string.okonferencji_fows2010);
                RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                        .okonferencji_male_okno);
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
        });

        TextView fows2011 = (TextView) getView().findViewById(R.id.textView_fows2011);
        fows2011.setOnTouchListener(touchListener);
        fows2011.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView zdjecie = (ImageView) getView().findViewById(R.id
                        .male_okno_zdjecie_okonferencji);
                zdjecie.setImageResource(R.drawable.fows2011);

                TextView informacja = (TextView) getView().findViewById(R.id
                        .textView_male_okno_okonferencji);
                informacja.setText(R.string.okonferencji_fows2011);
                RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                        .okonferencji_male_okno);
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
        });
        TextView fows2012 = (TextView) getView().findViewById(R.id.textView_fows2012);
        fows2012.setOnTouchListener(touchListener);
        fows2012.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView zdjecie = (ImageView) getView().findViewById(R.id
                        .male_okno_zdjecie_okonferencji);
                zdjecie.setImageResource(R.drawable.fows2012);

                TextView informacja = (TextView) getView().findViewById(R.id
                        .textView_male_okno_okonferencji);
                informacja.setText(R.string.okonferencji_fows2012);
                RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                        .okonferencji_male_okno);
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
        });

        TextView fows2013 = (TextView) getView().findViewById(R.id.textView_fows2013);
        fows2013.setOnTouchListener(touchListener);
        fows2013.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView zdjecie = (ImageView) getView().findViewById(R.id
                        .male_okno_zdjecie_okonferencji);
                zdjecie.setImageResource(R.drawable.fows2013);

                TextView informacja = (TextView) getView().findViewById(R.id
                        .textView_male_okno_okonferencji);
                informacja.setText(R.string.okonferencji_fows2013);
                RelativeLayout male_okno = (RelativeLayout) getView().findViewById(R.id
                        .okonferencji_male_okno);
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
        });
        //ładowanie tekstu
        WebView text1 = (WebView) getView().findViewById(R.id.webview_text1);
        String text1_string = "<html>" +
                "<body>" +
                "<p style=\"font-family: 'Lucida Console'; text-align: justify; font-size:90%;\">" +
                getString(R.string.okonferencji_1) +
                "</p>" +
                "</body>\n" +
                "</html>\n";
        text1.loadData(text1_string, "text/html; charset=utf-8", "utf-8");
        WebView text2 = (WebView) getView().findViewById(R.id.webview_text2);
        String text2_string = "<html>" +
                "<body>" +
                "<p style=\"font-family: 'Lucida Console'; text-align: justify; font-size:90%;\">" +
                getString(R.string.okonferencji_2) +
                "</p>" +
                "</body>\n" +
                "</html>\n";
        text2.loadData(text2_string, "text/html; charset=utf-8", "utf-8");
        WebView text3 = (WebView) getView().findViewById(R.id.webview_text3);
        String text3_string = "<html>" +
                "<body>" +
                "<p style=\"font-family: 'Lucida Console'; text-align: justify; font-size:90%;\">" +
                getString(R.string.okonferencji_3) +
                "</p>" +
                "</body>\n" +
                "</html>\n";
        text3.loadData(text3_string, "text/html; charset=utf-8", "utf-8");

    }
}
