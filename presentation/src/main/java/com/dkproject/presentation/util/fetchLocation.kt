package com.dkproject.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.dkproject.presentation.R
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun fetchLocation(context: Context): Location =
    suspendCancellableCoroutine { cont ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cont.resumeWithException(Exception(context.getString(R.string.failgps)))
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { cont.resume(it) }
        }.addOnFailureListener { exception ->
            cont.resumeWithException(Exception(context.getString(R.string.failgps)))
        }
    }