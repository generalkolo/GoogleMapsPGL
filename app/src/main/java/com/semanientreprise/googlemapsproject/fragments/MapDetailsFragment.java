package com.semanientreprise.googlemapsproject.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.semanientreprise.googlemapsproject.R;
import com.semanientreprise.googlemapsproject.adapter.MapDetailsAdapter;
import com.semanientreprise.googlemapsproject.model.MapDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapDetailsFragment extends DialogFragment {
    @BindView(R.id.mapDetailsRecView)
    RecyclerView mapDetailsRecView;
    Unbinder unbinder;
    @BindView(R.id.takenImage)
    ImageView takenImage;
    private List<MapDetails> details = new ArrayList<>();

    public MapDetailsFragment() {
    }

    public static MapDetailsFragment newInstance(String latitude, String longitude, byte[] imageByte) {
        MapDetailsFragment frag = new MapDetailsFragment();
        Bundle args = new Bundle();
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);
        args.putByteArray("image", imageByte);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_details, container, false);
        unbinder = ButterKnife.bind(this, v);

        initDetails();

        mapDetailsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mapDetailsRecView.setAdapter(new MapDetailsAdapter(details, getActivity()));
        mapDetailsRecView.setHasFixedSize(true);

        return v;
    }

    private void initDetails() {
        for (int i = 0; i < 10; i++) {
            String latitude = getArguments().getString("latitude", "Latitude");
            String longitude = getArguments().getString("longitude", "Longitude");
            MapDetails mapDetails = new MapDetails(latitude, longitude);
            details.add(mapDetails);
        }
        byte[] byteArray = getArguments().getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        takenImage.setImageBitmap(bmp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
