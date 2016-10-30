package pl.kebapp.byjacob.fows2016.Obiekty;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import pl.kebapp.byjacob.fows2016.R;

/**
 * Created by ByJacob on 2016-10-03.
 */

public class Agenda_Button extends LinearLayout {
    private String mData;
    private String mLang;
    private String mTheme;
    private String mAuthor;

    private ImageView mPasek = null;
    private android.support.v7.widget.AppCompatTextView mAuthorTextView = null;

    private long mEventDuration;


    public Agenda_Button(Context context) {
        super(context);
    }

    public void create(String aData, String aLang, String aTheme, String aAuthor) {
        mData = aData;
        mLang = aLang;
        mTheme = aTheme;
        mAuthor = aAuthor;
        int all_margin_dol = (int) (10 * Resources.getSystem().getDisplayMetrics().density);
        LinearLayout.LayoutParams parametry_super = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametry_super.setMargins(all_margin_dol, all_margin_dol, all_margin_dol, all_margin_dol);
        super.setLayoutParams(parametry_super);
        if (mData.length() > 1) {
            LinearLayout gora = new LinearLayout(super.getContext());
            gora.setOrientation(VERTICAL);
            LayoutParams parametry_gora = new LayoutParams(0, ViewGroup
                    .LayoutParams.MATCH_PARENT);
            parametry_gora.weight = (float) 0.25;
            gora.setLayoutParams(parametry_gora);
            TableRow.LayoutParams parametry_godzina = new TableRow.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            android.support.v7.widget.AppCompatTextView godzina = new android.support.v7.widget.AppCompatTextView(super.getContext());
            godzina.setText(mData);
            godzina.setLayoutParams(parametry_godzina);
            godzina.setTextAppearance(super.getContext(), android.R.style
                    .TextAppearance_DeviceDefault_Small);
            godzina.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            godzina.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            gora.addView(godzina);


            if (aLang != null) {
                ImageView imageLang = new ImageView(super.getContext());
                LinearLayout.LayoutParams parametry_lang = new LinearLayout.LayoutParams(ViewGroup
                        .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                parametry_lang.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
                imageLang.setLayoutParams(parametry_lang);
                switch (mLang) {
                    case "pl":
                        imageLang.setImageResource(R.drawable.flaga_polska);
                        gora.addView(imageLang);
                        break;
                    case "en":
                        imageLang.setImageResource(R.drawable.gb);
                        gora.addView(imageLang);
                        break;
                }
            }
            super.addView(gora);
        }

        LinearLayout dol = new LinearLayout(super.getContext());
        dol.setOrientation(VERTICAL);
        LinearLayout.LayoutParams parametry_dol = new LinearLayout.LayoutParams(0, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        parametry_dol.weight = (float) 0.75;
        int left = (int) (3 * Resources.getSystem().getDisplayMetrics().density);
        parametry_dol.setMargins(left, 0, 0, 0);
        dol.setLayoutParams(parametry_dol);

        android.support.v7.widget.AppCompatTextView theme = new android.support.v7.widget
                .AppCompatTextView(super.getContext());
        theme.setText(mTheme);
        TableRow.LayoutParams parametry_theme = new TableRow.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        theme.setLayoutParams(parametry_theme);
        theme.setTextAppearance(super.getContext(), android.R.style.TextAppearance_DeviceDefault_Large);
        theme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        dol.addView(theme, 0);
        if (mAuthor != null) {
            mAuthorTextView = new android.support.v7.widget
                    .AppCompatTextView(super.getContext());
            mAuthorTextView.setText(mAuthor);
            TableRow.LayoutParams parametry_author = new TableRow.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mAuthorTextView.setLayoutParams(parametry_author);
            mAuthorTextView.setTextAppearance(super.getContext(), android.R.style
                    .TextAppearance_DeviceDefault_Small);
            mAuthorTextView.setClickable(true);
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    mAuthorTextView.callOnClick();
                }
            };
            final Handler handler = new Handler();
            mAuthorTextView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            TextView view = (TextView) v;
                            //overlay is black with transparency of 0x77 (119)
                            Agenda_Button.super.setBackgroundResource(R.drawable
                                    .rounded_edges_pressed);
                            Agenda_Button.super.invalidate();
                            handler.postDelayed(run, 30);
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            TextView view = (TextView) v;
                            //clear the overlay
                            handler.removeCallbacks(run);
                            Agenda_Button.super.setBackgroundResource(R.drawable.rounded_edges_normal);
                            Agenda_Button.super.invalidate();
                            break;
                        }
                    }

                    return true;
                }
            });
            theme.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            TextView view = (TextView) v;
                            //overlay is black with transparency of 0x77 (119)
                            Agenda_Button.super.setBackgroundResource(R.drawable
                                    .rounded_edges_pressed);
                            Agenda_Button.super.invalidate();
                            handler.postDelayed(run, 30);
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            TextView view = (TextView) v;
                            //clear the overlay
                            handler.removeCallbacks(run);
                            Agenda_Button.super.setBackgroundResource(R.drawable.rounded_edges_normal);
                            Agenda_Button.super.invalidate();
                            break;
                        }
                    }

                    return true;
                }
            });
            theme.setLongClickable(true);
            dol.addView(mAuthorTextView, 1);
        }

        super.addView(dol);

        int pasek_wysokosc = (int) (2 * Resources.getSystem().getDisplayMetrics().density);
        int pasek_margines = (int) (15 * Resources.getSystem().getDisplayMetrics().density);
        mPasek = new ImageView(super.getContext());
        LinearLayout.LayoutParams parametry_pasek = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, pasek_wysokosc);
        parametry_pasek.setMargins(pasek_margines, 0, pasek_margines, 0);
        mPasek.setLayoutParams(parametry_pasek);
        mPasek.setBackgroundColor(getResources().getColor(R.color.pasek));
        //super.addView(pasek);

    }

    public ImageView getPasek() {
        return mPasek;
    }

    public AppCompatTextView getmAuthorTextView() {
        return mAuthorTextView;
    }
}
