package com.lightning.master.ledbulb;

import android.location.Location;


public class TargetView {
    public interface Targetview {
        void locationchange(final Location location);
    }
}