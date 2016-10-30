package pl.kebapp.byjacob.fows2016.FragmentyGlowne;


import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.kebapp.byjacob.fows2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Spons_Patron extends Fragment {


    public Spons_Patron() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spons_patr, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/josefinsans-regular.ttf");
        TextView glowny = (TextView) getView().findViewById(R.id.textView_spons_part_gl);
        glowny.setTypeface(face);
        TextView zwykli = (TextView) getView().findViewById(R.id.textView_spons_part_zwykli);
        zwykli.setTypeface(face);

    }
}
