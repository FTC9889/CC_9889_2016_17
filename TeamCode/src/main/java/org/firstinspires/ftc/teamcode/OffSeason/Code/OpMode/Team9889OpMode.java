package org.firstinspires.ftc.teamcode.OffSeason.Code.OpMode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Drivebase;

/**
 * Created by joshua on 3/8/17.
 */

public abstract class Team9889OpMode extends OpMode{
    private double leftspeed, rightspeed, xvalue, yvalue;
    private int div = 1;

    //Autonomous Selector
    public String particlePref;
    public String beaconPref;
    public String capBallPref;
    public String parkingPref;
    public String alliance;

    protected void updateData(OpMode opMode){
        opMode.telemetry.update();
    }

    protected void gamepadsDrivetrain(Drivebase drivebase, double x, double y, boolean Slow){
        //Lower the max speed of the robot
        if (Slow){
            div = 3;
        }else {
            div = 1;
        }

        xvalue = x/div;
        yvalue = y/div;

        leftspeed =  yvalue - xvalue;
        rightspeed = yvalue + xvalue;

        drivebase.setLeftRightPower(leftspeed, rightspeed);
    }

    protected void getAutonomousPrefs(OpMode opMode) {
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
}
