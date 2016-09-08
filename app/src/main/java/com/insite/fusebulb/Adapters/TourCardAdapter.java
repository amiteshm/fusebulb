package com.insite.fusebulb.Adapters;

/**
 * Created by amiteshmaheshwari on 27/08/16.
 */

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


import com.insite.fusebulb.Helpers.Downloader;
import com.insite.fusebulb.Helpers.ViewAnimationUtils;
import com.insite.fusebulb.Models.Tour;
import com.insite.fusebulb.PlayTourActivity;
import com.insite.fusebulb.R;
import com.insite.fusebulb.SlideTourActivity;
import com.insite.fusebulb.Support.UserPreference;
import com.insite.fusebulb.UserSettingsActivity;

public class TourCardAdapter extends RecyclerView.Adapter<TourCardAdapter.ViewHolder> {

    private Context context;
    private List<Tour> tourList;
    private UserPreference.Language languagePref;
    private Resources resources;


    public TourCardAdapter(Context context, UserPreference.Language language_pref, List<Tour> tourList) {
        this.context = context;
        this.tourList = tourList;
        this.languagePref = language_pref;
        this.resources = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Tour tour = tourList.get(position);
        holder.titleView.setText(tour.getName());
        holder.thumbnailImage.setImageURI(Uri.fromFile(tour.getPicture()));
        holder.tourOverView.setVisibility(View.GONE);
        holder.tourOverView.setText(tour.getSummary());
        holder.tourShowOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tourOverView.getVisibility() == View.VISIBLE) {
                    ViewAnimationUtils.collapse(holder.tourOverView);
                } else {
                    ViewAnimationUtils.expand(holder.tourOverView, false);
                }
            }
        });

        holder.directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", tour.getLocation().getLongitude(), tour.getLocation().getLatitude(), tour.getName());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImage;
        public TextView titleView;
        public ImageView directionBtn;
        public FloatingActionButton tourShowOverViewBtn;
        public TextView tourOverView;

        public ViewHolder(View view) {
            super(view);
            thumbnailImage = (ImageView) view.findViewById(R.id.tour_img_thumbnail);
            titleView = (TextView) view.findViewById(R.id.tour_title);
            tourShowOverViewBtn = (FloatingActionButton) view.findViewById(R.id.showOverview);
            tourOverView = (TextView) view.findViewById(R.id.tour_overview_text);
            tourOverView.setVisibility(View.GONE);
            directionBtn = (ImageView) view.findViewById(R.id.tour_direction);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Tour tour = tourList.get(pos);
                    startPlayTourActivity(tour.getTourSource());
                }
            });
        }
    }

    private void startPlayTourActivity(String tour_source) {
        Intent intent = new Intent(context, PlayTourActivity.class);
        intent.putExtra("TOUR_SOURCE", tour_source);
//        Intent intent = new Intent(context, SlideTourActivity.class);
//        intent.putExtra("TOUR_SOURCE", tour_source);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String getTourTypeString(Tour tour, String languagePref) {
        int tourType = tour.getTourType();
        String tag = "";

        if (languagePref.equals(UserSettingsActivity.lang_hi)) {
            if (tourType == Tour.ATTRACTION) {
                tag = resources.getString(R.string.hi_tour_type_attraction);
            } else if (tourType == Tour.CITY_TOUR) {
                tag = resources.getString(R.string.hi_tour_type_city_tour);
            }
        } else {
            if (tourType == Tour.ATTRACTION) {
                tag = resources.getString(R.string.en_tour_type_attraction);
            } else if (tourType == Tour.CITY_TOUR) {
                tag = resources.getString(R.string.en_tour_type_city_tour);
            }
        }
        return tag;
    }

    private String formateTourPrice(int tourPrice) {
        String tag = "";
        if (tourPrice == 0) {
            tag = resources.getString(R.string.free_tour);
        } else {
            tag = String.format(resources.getString(R.string.tour_price), tourPrice);
        }
        return tag;
    }
}
