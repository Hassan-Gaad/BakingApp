package com.example.baking.fragments;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.baking.R;
import com.example.baking.models.Steps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

public class VideoFragment extends Fragment {

    private List<Steps> mStepList;
    private int stepListIndex;

    private ImageView  imageViewThumbnail;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private static String STEPS_LIST_KEY="stepsList";
    private static String STEP_LIST_INDEX="stepListIndex";

    private static String PLAYER_POSITION="position";
    private static String PLAYER_STATE="player_state";
    private static String TAG =DescriptionFragment.class.getSimpleName();
    Uri videoUri;
    Uri imgUri;

    long position ;
    boolean isPlayWhenReady;
    public VideoFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState!=null){
            mStepList=savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            stepListIndex=savedInstanceState.getInt(STEP_LIST_INDEX);
            position=savedInstanceState.getLong(PLAYER_POSITION, C.TIME_UNSET);
            isPlayWhenReady=savedInstanceState.getBoolean(PLAYER_STATE,true);
        }

        View view=inflater.inflate(R.layout.video_fragment,container,false);
        exoPlayerView=view.findViewById(R.id.simpleExoPlayerView);
        imageViewThumbnail=view.findViewById(R.id.imageViewThumbnail);
        exoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.no_video));
        videoUri=Uri.parse(mStepList.get(stepListIndex).getVideoUrl());


        imgUri=Uri.parse(mStepList.get(stepListIndex).getThumbnailUrl());


        if (videoUri!=null&& mExoPlayer==null ){
            imageViewThumbnail.setVisibility(View.GONE);
            exoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(exoPlayerView,mStepList.get(stepListIndex));
        }else if (videoUri==null && imgUri!=null){
            exoPlayerView.setVisibility(View.GONE);
            imageViewThumbnail.setVisibility(View.VISIBLE);
            initializeThumbnail(imgUri);
        }else{
            exoPlayerView.setVisibility(View.GONE);
            imageViewThumbnail.setVisibility(View.VISIBLE);
            imageViewThumbnail.setImageResource(R.drawable.placeholder);
        }


            return view;
    }


    private void initializeThumbnail(Uri imgUri){

        if (imgUri.toString().endsWith(".png")||imgUri.toString().endsWith(".jpg")){
            Picasso.get()
                    .load(imgUri)
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.error_happend)
                    .into(imageViewThumbnail);
        }


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggleVideoFullScreen(newConfig);
    }

    private void toggleVideoFullScreen(Configuration newConfig) {
        // This method is used to show video in fullscreen when device is rotated
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            exoPlayerView.setLayoutParams(params);
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            }
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            exoPlayerView.setLayoutParams(params);
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }


    private void initializePlayer(SimpleExoPlayerView playerView, Steps selectedStep) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "BakingVideo");

            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(selectedStep.getVideoUrl()),
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(position);
            mExoPlayer.setPlayWhenReady(isPlayWhenReady);



    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            position=mExoPlayer.getCurrentPosition();
            isPlayWhenReady=mExoPlayer.getPlayWhenReady();
            releasePlayer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer!=null){
            mExoPlayer.setPlayWhenReady(isPlayWhenReady);
            mExoPlayer.seekTo(position);
        }

    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STEPS_LIST_KEY, (ArrayList<? extends Parcelable>) mStepList);
        outState.putInt(STEP_LIST_INDEX,stepListIndex);
        outState.putLong(PLAYER_POSITION,position);
        outState.putBoolean(PLAYER_STATE,isPlayWhenReady);
    }

    public void setStepList(List<Steps> stepList){
        mStepList =stepList;
    }

    public void setStepListIndex(int stepListIndex) {
        this.stepListIndex = stepListIndex;
    }
}
