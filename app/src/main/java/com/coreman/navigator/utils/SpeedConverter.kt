package com.coreman.navigator.utils

/*
This class allow the user to convert the speed into the desired unit
Like km/h, m/s, mph and so on

If the app where to scale.
*/

class SpeedConverter {

    fun calculateSpeedInKilometers(speed: Double) : Double {
        // 1m/s = 3.6km/h
        return speed * 3.6
    }
}