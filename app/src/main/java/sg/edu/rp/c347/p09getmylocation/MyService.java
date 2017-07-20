package sg.edu.rp.c347.p09getmylocation;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileWriter;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        ,LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    String folderLocation;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Lat : " + location.getLatitude() + " Lng : " +
                location.getLongitude(), Toast.LENGTH_SHORT).show();
        File targetFile = new File(folderLocation, "locations.txt");

        try {
            FileWriter writer = new FileWriter(targetFile, true);
            writer.write("Last known location when this Activity started" + "\n" + "Lat : " + mLocation.getLatitude() + "\n" +
                    " Lng : " + mLocation.getLongitude() + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MyService.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MyService.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        } else {
            mLocation = null;
            Toast.makeText(MyService.this,
                    "Permission not granted to retrieve location info",
                    Toast.LENGTH_SHORT).show();
        }
        if (mLocation != null) {
            Toast.makeText(this, "Lat : " + mLocation.getLatitude() +
                            " Lng : " + mLocation.getLongitude(),
                    Toast.LENGTH_SHORT).show();

            File targetFile = new File(folderLocation, "locations.txt");
            try {
                FileWriter writer = new FileWriter(targetFile, true);
                writer.write("Last known location when this Activity started" + "\n" + "Lat : " + mLocation.getLatitude() + "\n" +
                        " Lng : " + mLocation.getLongitude() + "\n");
                writer.flush();
                writer.close();
            } catch (Exception e) {
                Toast.makeText(MyService.this, "Failed to write!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest
                    .PRIORITY_BALANCED_POWER_ACCURACY);
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setSmallestDisplacement(100);
           // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
