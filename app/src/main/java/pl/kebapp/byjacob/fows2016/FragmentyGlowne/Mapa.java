package pl.kebapp.byjacob.fows2016.FragmentyGlowne;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import pl.kebapp.byjacob.fows2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mapa extends Fragment {


    public Mapa() {
        // Required empty public constructor
    }

    MapView mapView;
    GoogleMap map;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    final LatLng D20 = new LatLng(51.110349, 17.059767);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (MapView) getView().findViewById(R.id.map2);
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    if (checkPermissions()) {
                        if (ActivityCompat.checkSelfPermission(getView().getContext(), Manifest.permission
                                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getView().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        else {
                            map.setMyLocationEnabled(true);
                        }
                    }
                    map.addMarker(new MarkerOptions().position(D20).title("D-20").snippet("Centrum Kongresowe Politechniki Wrocławskiej\nJaniszewskiego 8\n50-370 Wrocław"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(D20);
                    map.moveCamera(cameraUpdate);
                    //map.animateCamera(CameraUpdateFactory.zoomIn());
                    map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
                }
            });
        }
        RelativeLayout openmap = (RelativeLayout) getView().findViewById(R.id.relativ_open_maps);
        openmap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        RelativeLayout view = (RelativeLayout) v;
                        //overlay is black with transparency of 0x77 (119)
                        v.setBackgroundResource(R.drawable
                                .rounded_edges_pressed);
                        v.invalidate();
                        v.callOnClick();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        RelativeLayout view = (RelativeLayout) v;
                        //clear the overlay
                        v.setBackgroundResource(R.drawable.rounded_edges_normal);
                        v.invalidate();
                        break;
                    }
                }

                return true;
            }
        });
        openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=51.110349,17.059767");
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getView().getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new
                    String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
