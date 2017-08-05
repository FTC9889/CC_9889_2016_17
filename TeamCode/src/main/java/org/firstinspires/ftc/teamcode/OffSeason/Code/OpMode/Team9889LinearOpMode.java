package org.firstinspires.ftc.teamcode.OffSeason.Code.OpMode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by joshua on 3/8/17.
 */

public abstract class Team9889LinearOpMode extends LinearOpMode {
    public String particlePref;
    public String beaconPref;
    public String capBallPref;
    public String parkingPref;
    public String alliance;

    protected void getAutonomousPrefs(LinearOpMode opMode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(hardwareMap.appContext);
        this.particlePref = preferences.getString("How Many Particles Should We Shoot?", "");
        this.beaconPref = preferences.getString("Which beacons should we activate?", "");
        this.capBallPref = preferences.getString("Should we bump the cap ball off the center vortex?", "");
        this.parkingPref = preferences.getString("Where should we park?", "");
        this.alliance = preferences.getString("Which alliance are we on?", "");

        opMode.telemetry.addLine("Particles: " + particlePref);
        opMode.telemetry.addLine("Beacons: " + beaconPref);
        opMode.telemetry.addLine("Cap Ball: " + capBallPref);
        opMode.telemetry.addLine("Parking: " + parkingPref);
        opMode.telemetry.addLine("Alliance: " + alliance);
        opMode.telemetry.update();
    }

    public void HoldPosition(double throttle, double turning, boolean holdHeading){

    }
}
