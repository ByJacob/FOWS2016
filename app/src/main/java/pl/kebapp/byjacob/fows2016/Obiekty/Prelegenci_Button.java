package pl.kebapp.byjacob.fows2016.Obiekty;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import pl.kebapp.byjacob.fows2016.R;

/**
 * Created by ByJacob on 2016-10-04.
 */

public class Prelegenci_Button extends LinearLayout {
    private String mImie;
    private String mFirma;
    private Context mContext;

    private long mEventDuration;

    public Prelegenci_Button(Context context) {
        super(context);
        mContext = context;
    }

    public void create(String aImie, String aFirma, Activity mActivity) {
        mImie = aImie;
        mFirma = aFirma;
        int marginesy_okna = (int) (10 * Resources.getSystem().getDisplayMetrics().density);
        LinearLayout.LayoutParams parametry_super = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametry_super.setMargins(marginesy_okna, marginesy_okna / 2, marginesy_okna,
                0);
        super.setOrientation(VERTICAL);
        super.setLayoutParams(parametry_super);

        android.support.v7.widget.AppCompatTextView imie = new android.support.v7.widget
                .AppCompatTextView(super.getContext());
        imie.setClickable(false);
        imie.setText(mImie);
        TableRow.LayoutParams parametry_imie = new TableRow.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imie.setLayoutParams(parametry_imie);
        imie.setTextAppearance(super.getContext(), android.R.style
                .TextAppearance_DeviceDefault_Medium);
        imie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        Typeface face = Typeface.createFromAsset(mActivity.getAssets(),
                "fonts/josefinsans-regular.ttf");
        imie.setTypeface(face);
        super.addView(imie, 0);

        android.support.v7.widget.AppCompatTextView firma = new android.support.v7.widget
                .AppCompatTextView(super.getContext());
        firma.setClickable(false);
        firma.setText(mFirma);
        TableRow.LayoutParams parametry_firma = new TableRow.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        firma.setLayoutParams(parametry_firma);
        firma.setTextAppearance(super.getContext(), android.R.style
                .TextAppearance_DeviceDefault_Small);
        firma.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        firma.setTypeface(face);
        super.addView(firma, 1);

        int pasek_wysokosc = (int) (2 * Resources.getSystem().getDisplayMetrics().density);
        ImageView pasek_maly = new ImageView(super.getContext());
        pasek_maly.setClickable(false);
        LinearLayout.LayoutParams parametry_pasek = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, pasek_wysokosc);
        parametry_pasek.setMargins(2, 0, 2, 0);
        pasek_maly.setLayoutParams(parametry_pasek);
        pasek_maly.setBackgroundColor(getResources().getColor(R.color.pasek));
        super.addView(pasek_maly, 2);

        int pasek_wysokosc2 = (int) (5 * Resources.getSystem().getDisplayMetrics().density);
        ImageView pasek_duzy = new ImageView(super.getContext());
        pasek_duzy.setClickable(false);
        LinearLayout.LayoutParams parametry_pasek2 = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, pasek_wysokosc2);
        parametry_pasek2.setMargins(0, marginesy_okna / 2, 0, 0);
        pasek_duzy.setLayoutParams(parametry_pasek2);
        pasek_duzy.setBackgroundColor(getResources().getColor(R.color.pasek));
        //super.addView(pasek_duzy, 3);
        super.setClickable(true);
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                Prelegenci_Button.super.callOnClick();
            }
        };
        final Handler handler = new Handler();
        super.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        LinearLayout view = (LinearLayout) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.setBackgroundResource(R.drawable.rounded_edges_pressed);
                        view.invalidate();
                        handler.postDelayed(run, 50);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        LinearLayout view = (LinearLayout) v;
                        //clear the overlay
                        handler.removeCallbacks(run);
                        view.setBackgroundResource(R.drawable.rounded_edges_normal);
                        view.invalidate();
                        break;
                    }
                }

                return true;
            }
        });
    }


}
