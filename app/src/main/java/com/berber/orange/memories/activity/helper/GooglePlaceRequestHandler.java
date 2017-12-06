package com.berber.orange.memories.activity.helper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.berber.orange.memories.activity.details.DetailsActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * ya yin
 * Created by orange on 2017/12/6.
 */

public class GooglePlaceRequestHandler {
    private GoogleApiClient mGoogleApiClient;

    public GooglePlaceRequestHandler(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public void openPickerPlaceDialog(AppCompatActivity activity, int request_code) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            activity.startActivityForResult(builder.build(activity), request_code);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void doPlacePhotoRequest(AppCompatActivity activity, String placeId, String anniversaryId) {
        PlacePhotoRequest placePhotoRequest = new PlacePhotoRequest(activity, mGoogleApiClient);
        placePhotoRequest.execute(placeId, anniversaryId);

    }

    private static class PlacePhotoRequest extends AsyncTask<String, Void, List<PlacePhotoRequest.AttributedPhoto>> {

        private final WeakReference<AppCompatActivity> mTarget;

        private GoogleApiClient mGoogleApiClient;

        PlacePhotoRequest(AppCompatActivity activity, GoogleApiClient GoogleApiClient) {
            mTarget = new WeakReference<>(activity);
            this.mGoogleApiClient = GoogleApiClient;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to some progress work when ready get place image

        }

        @Override
        protected void onPostExecute(List<AttributedPhoto> attributedPhotos) {
            super.onPostExecute(attributedPhotos);
            //notify the activity to scan the photo and display in the activity
            if (mTarget.get() instanceof DetailsActivity) {
                DetailsActivity detailsActivity = (DetailsActivity) mTarget.get();
                detailsActivity.setPlacePhotoBanner();
            }
            Log.e("TAG", "download google location task finish");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<PlacePhotoRequest.AttributedPhoto> doInBackground(String... strings) {
            if (strings.length != 2) {
                return null;
            }
            //get place id
            final String placeId = strings[0];
            final String anniversaryId = strings[1];

            AttributedPhoto attributedPhoto = null;

            PlacePhotoMetadataResult result = Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId).await();
            List<AttributedPhoto> list = new ArrayList<>();
            if (result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadata = result.getPhotoMetadata();
                if (photoMetadata.getCount() > 0 && !isCancelled()) {

                    int count = photoMetadata.getCount();
                    for (int i = 0; i < count; i++) {
                        PlacePhotoMetadata placePhotoMetadata = photoMetadata.get(i);
                        CharSequence attributions = placePhotoMetadata.getAttributions();
                        Bitmap bitmap = placePhotoMetadata.getPhoto(mGoogleApiClient).await().getBitmap();
                        attributedPhoto = new AttributedPhoto(attributions, bitmap);
                        list.add(attributedPhoto);
                        final File file = ImageUtils.getFile(mTarget.get().getApplicationContext(), Long.valueOf(anniversaryId), "place");
                        if (!file.exists()) {
                            file.exists();
                        }
                        try {
                            ImageUtils.saveBitmap(mTarget.get().getApplicationContext(), bitmap, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                photoMetadata.release();
            }
            return list;
        }

        class AttributedPhoto {
            final CharSequence attribution;
            final Bitmap bitmap;

            AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
                this.attribution = attribution;
                this.bitmap = bitmap;
            }
        }
    }


}
