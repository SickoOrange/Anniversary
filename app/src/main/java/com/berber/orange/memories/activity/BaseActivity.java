package com.berber.orange.memories.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.berber.orange.memories.APP;
import com.berber.orange.memories.activity.helper.AnniversaryDaoUtils;
import com.berber.orange.memories.activity.helper.FilebaseStorageHelper;
import com.berber.orange.memories.activity.helper.GooglePlaceRequestHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.storage.FirebaseStorage;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * ya yin
 * Created by orange on 2017/11/15.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, EasyPermissions.PermissionCallbacks {
    protected ImmersionBar mImmersionBar;
    private InputMethodManager imm;
    protected GoogleApiClient mGoogleApiClient;
    protected GooglePlaceRequestHandler mGooglePlaceRequestHandler;
    protected AnniversaryDaoUtils anniversaryDaoUtils;
    protected FilebaseStorageHelper filebaseStorageHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mGooglePlaceRequestHandler = new GooglePlaceRequestHandler(mGoogleApiClient);

        anniversaryDaoUtils = new AnniversaryDaoUtils((APP) getApplication());

        filebaseStorageHelper = new FilebaseStorageHelper(this);
        initView();
        // init immersion bar
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    protected void initView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imm = null;
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        // mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    protected abstract int setLayoutId();

    public boolean isImmersionBarEnabled() {
        return true;
    }

    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO: 2017/11/25  
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean hasPermissionToPickImage(String... perms) {
        return EasyPermissions.hasPermissions(this, perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        doTaskAfterPermissionsGranted(requestCode);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    protected abstract void doTaskAfterPermissionsGranted(int requestCode);


    /**
     * get parent file
     *
     * @param anniversaryId anniversary id
     * @param flag          place or picture
     * @return parent file path string
     */
    protected String getParentPath(Long anniversaryId, String flag) {
        return this.getFilesDir() + "/" + flag + "anniversary_" + String.valueOf(anniversaryId);
    }

}
