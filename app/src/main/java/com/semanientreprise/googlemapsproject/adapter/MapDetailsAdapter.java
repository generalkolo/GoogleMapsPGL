package com.semanientreprise.googlemapsproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semanientreprise.googlemapsproject.R;
import com.semanientreprise.googlemapsproject.model.MapDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapDetailsAdapter extends RecyclerView.Adapter<MapDetailsAdapter.MapDetailsViewHolder> {

    private List<MapDetails> arrayList;
    private Context context;

    public MapDetailsAdapter(List<MapDetails> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MapDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_layout, null);
        final MapDetailsViewHolder MapDetailsViewHolder = new MapDetailsViewHolder(view);

        return MapDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(MapDetailsViewHolder holder, int position) {
        holder.mapLatitude.setText("Latitude "+arrayList.get(position).getLatitude());
        holder.mapLongitude.setText("Longitude "+arrayList.get(position).getLongitude());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MapDetailsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.map_latitude) TextView mapLatitude;
        @BindView(R.id.map_longitude) TextView mapLongitude;

        public MapDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
