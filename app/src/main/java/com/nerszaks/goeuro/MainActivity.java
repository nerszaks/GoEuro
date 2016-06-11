package com.nerszaks.goeuro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nerszaks.goeuro.fragments.BaseFragment;
import com.nerszaks.goeuro.fragments.SearchFragment;
import com.nerszaks.goeuro.utils.PermissionUtils;
import com.nerszaks.goeuro.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private LocationManager mLocationManager;
    private Location currentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceToFragment(new SearchFragment(), false);

        updateLocation(true);

    }

    private void replaceToFragment(BaseFragment newFragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.isOnline()) {
            Toast.makeText(getApplicationContext(), R.string.CheckInternetConnection, Toast.LENGTH_SHORT).show();
        }
        updateLocation(false);
    }

    private void updateLocation(boolean requestPermission) {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lastKnownLocationNetwork = null;
        Location lastKnownLocationGps = null;
        if (PermissionUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, requestPermission)) {
            lastKnownLocationNetwork = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lastKnownLocationGps = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (lastKnownLocationNetwork != null
                && (lastKnownLocationGps == null || lastKnownLocationNetwork.getTime() > lastKnownLocationGps.getTime())) {
            setCurrentLocation(lastKnownLocationNetwork);
        } else {
            setCurrentLocation(lastKnownLocationGps);
        }
        if (lastKnownLocationGps == null && lastKnownLocationNetwork == null) {
            Toast.makeText(getApplicationContext(), R.string.CheckLocationSettings, Toast.LENGTH_LONG).show();
        }
    }

    public synchronized void setCurrentLocation(Location currentLocation) {
        if (currentLocation != null) {
            this.currentLocation = currentLocation;
            BaseFragment currentFragment = getCurrentFragment();
            if (currentFragment != null) {
                currentFragment.onLocationUpdated();
            }
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.PERMISSIONS_REQUESTED: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    updateLocation(false);
                }
                return;
            }
        }
    }
}
