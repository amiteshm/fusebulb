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
import com.insite.fusebulb.Models.Tour;
import com.insite.fusebulb.R;
import com.insite.fusebulb.SlideTourActivity;
import com.insite.fusebulb.UserSettingsActivity;

public class TourCardAdapter extends RecyclerView.Adapter<TourCardAdapter.ViewHolder> {

    private Context context;
    private List<Tour> tourList;
    private String languagePref;
    private Resources resources;
    private TourPreviewPlayer previewPlayer;


    public TourCardAdapter(Context context, String language_pref, MediaPlayer mp, List<Tour> tourList) {
        this.context = context;
        this.tourList = tourList;
        this.languagePref = language_pref;
        this.resources = context.getResources();
        this.previewPlayer = new TourPreviewPlayer();
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
//        holder.tourTypeView.setText(getTourTypeString(tour, languagePref));
//        holder.tourPrice.setText(formateTourPrice(tour.getPrice()));
//        if (tour.getMediaType() == Tour.AUDIO_TYPE) {
//            holder.tourPreview.setText(formatPreviewText(languagePref, true));
//        } else {
//            holder.tourPreview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fusebulb, 0, 0, 0);
//        }
//
//        holder.tourDirection.setText(formatDirectionText(languagePref));
        holder.tourOverView.setVisibility(View.GONE);
        //holder.tourHideOverViewBtn.setVisibility(View.GONE);
        holder.tourOverView.setText(tour.getSummary());


//        holder.tourPreview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (tour.getMediaType() == Tour.AUDIO_TYPE) {
//                    String filePath = tour.getPreviewSource();
//                    File mediaFile = new File(Downloader.getAppFolder(), filePath);
//                    previewPlayer.playOrStopPreviewFor(holder.tourPreview, mediaFile);
//                }
//            }
//        });

        holder.tourShowOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tourOverView.getVisibility() == View.VISIBLE) {
                    collapse(holder.tourOverView);
                } else {
                    expand(holder.tourOverView);
                }

            }
        });


//        holder.tourDirection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", tour.getLocation().getLongitude(), tour.getLocation().getLatitude(), tour.getName());
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                try {
//                    context.startActivity(intent);
//                } catch (ActivityNotFoundException ex) {
//                    try {
//                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                        context.startActivity(unrestrictedIntent);
//                    } catch (ActivityNotFoundException innerEx) {
//                        Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
    }


    public String formatPreviewText(String languagePref, boolean play) {


        String tag = "";
        if (play) {
            if (languagePref.equals(UserSettingsActivity.lang_hi)) {
                tag = resources.getString(R.string.hi_preview);
            } else {
                tag = resources.getString(R.string.en_preview);
            }
        } else {
            if (languagePref.equals(UserSettingsActivity.lang_hi)) {
                tag = resources.getString(R.string.hi_stop_player);
            } else {
                tag = resources.getString(R.string.en_stop_player);
            }
        }
        return tag;

    }

    public String formatDirectionText(String languagePref) {
        String tag = "";
        if (languagePref.equals(UserSettingsActivity.lang_hi)) {
            tag = resources.getString(R.string.hi_direction);
        } else {
            tag = resources.getString(R.string.en_direction);
        }
        return tag;
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImage;
        public TextView titleView;
//        public TextView tourTypeView;
//        public Button tourPrice;
//        public Button tourDirection;
//        public Button tourPreview;
        public FloatingActionButton tourShowOverViewBtn;
        //public Button tourHideOverViewBtn;
        public TextView tourOverView;

        public ViewHolder(View view) {
            super(view);
            thumbnailImage = (ImageView) view.findViewById(R.id.tour_img_thumbnail);
            titleView = (TextView) view.findViewById(R.id.tour_title);
//            tourTypeView = (TextView) view.findViewById(R.id.tour_type);
//            tourDirection = (Button) view.findViewById(R.id.tour_distance);
//            tourPreview = (Button) view.findViewById(R.id.tour_preview);
//            tourPrice = (Button) view.findViewById(R.id.tour_price_tag);

//            tourShowOverViewBtn = (Button) view.findViewById(R.id.tour_overview_arrow_down_btn);
//            tourHideOverViewBtn = (Button) view.findViewById(R.id.tour_overview_arrow_up_btn);

            tourShowOverViewBtn = (FloatingActionButton) view.findViewById(R.id.showOverview);
            tourOverView = (TextView) view.findViewById(R.id.tour_overview_text);
            tourOverView.setVisibility(View.GONE);
            //tourHideOverViewBtn.setVisibility(View.GONE);

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
        Intent intent = new Intent(context, SlideTourActivity.class);
        intent.putExtra("TOUR_SOURCE", tour_source);
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


    public static void expand(final TextView v) {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private class TourPreviewPlayer {
        private MediaPlayer mediaPlayer;
        private Button currentView;

        private MediaPlayer getMediaPlayer() {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            return mediaPlayer;
        }

        private void setCurrentView(Button view) {
            this.currentView = view;
            if (currentView != null) {
                view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_player, 0, 0, 0);
                view.setText(formatPreviewText(languagePref, false));
                getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopCurrentPreview();
                    }
                });
            }
        }

        private void playOrStopPreviewFor(Button view, File file) {
            boolean play = view != currentView;
            stopCurrentPreview();
            if (play) {
                setCurrentView(view);
                MediaPlayer mp = getMediaPlayer();
                try {
                    mp.reset();
                    mp.setDataSource(context, Uri.fromFile(file));
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void stopCurrentPreview() {
            if (currentView != null) {
                MediaPlayer mp = getMediaPlayer();
                mp.stop();
                currentView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_preview, 0, 0, 0);
                currentView.setText(formatPreviewText(languagePref, true));
                setCurrentView(null);
            }
        }
    }
}
