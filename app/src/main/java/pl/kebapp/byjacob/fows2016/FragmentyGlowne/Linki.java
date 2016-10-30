package pl.kebapp.byjacob.fows2016.FragmentyGlowne;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.kebapp.byjacob.fows2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Linki extends Fragment {


    public Linki() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_linki, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        TextView wireles = (TextView) getView().findViewById(R.id.textViewWirelessGroup);
        wireles.setTypeface(face);
        TextView fowsText = (TextView) getView().findViewById(R.id.textViewFOWS);
        fowsText.setTypeface(face);
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.setBackgroundResource(R.drawable.rounded_edges_pressed);
                        view.invalidate();
                        v.callOnClick();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.setBackgroundResource(R.drawable.rounded_edges_normal);
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        };
        ImageView wg_normal = (ImageView) getView().findViewById(R.id.wg_normal);
        wg_normal.setOnTouchListener(touchListener);
        wg_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // e.g. if your URL is https://www.facebook.com/EXAMPLE_PAGE, you should put EXAMPLE_PAGE at the end of this URL, after the ?
                String YourPageURL = "http://wireless-group.pwr.wroc.pl";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });
        ImageView wg_fb = (ImageView) getView().findViewById(R.id.wg_fb);
        wg_fb.setOnTouchListener(touchListener);
        wg_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String YourPageURL = "https://www.facebook.com/n/WirelessGroup";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });
        ImageView fows_normal = (ImageView) getView().findViewById(R.id.fows_normal);
        fows_normal.setOnTouchListener(touchListener);
        fows_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // e.g. if your URL is https://www.facebook.com/EXAMPLE_PAGE, you should put EXAMPLE_PAGE at the end of this URL, after the ?
                String YourPageURL = "http://fows.pwr.edu.pl/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });
        ImageView fows_fb = (ImageView) getView().findViewById(R.id.fows_fb);
        fows_fb.setOnTouchListener(touchListener);
        fows_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String YourPageURL = "https://www.facebook.com/n/events/1756638801243829/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });

        ImageView open_kebapp = (ImageView) getView().findViewById(R.id.open_kebapp);
        open_kebapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String YourPageURL = "http://kebapp.pl/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
                startActivity(browserIntent);
            }
        });
    }


}
