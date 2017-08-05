package org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Beacon {

    //Beacon-pushing Servos
    private Servo RightBumper, LeftBumper;

    //MR Color Sensor
    public ColorSensor Color;

    public Beacon(){

    }

    //Hardware Map
    public void init(HardwareMap hardwareMap){
        //Servos
        RightBumper = hardwareMap.servo.get("RBump");
        LeftBumper = hardwareMap.servo.get("LBump");

        LeftBumper.setDirection(Servo.Direction.REVERSE);

        //Color Sensor
        Color = hardwareMap.colorSensor.get("colorsensor");
        Color.enableLed(false);

        //Servo Movement
        BumperSynchronised(true);
    }

    //Controller for all bumper actions that are synchronised
    public void BumperSynchronised(boolean updown){
        if(updown){
            RightBumper.setPosition(0.4);
            LeftBumper.setPosition(0.4);
        }else {
            RightBumper.setPosition(1.0);
            LeftBumper.setPosition(1.0);
        }
    }

    //Bumper Lift left or right (value of true will lift right servo)
    public void BumperBeacon(boolean right){
        if (right){
            RightBumper.setPosition(0.4);
            LeftBumper.setPosition(0.95);
        }else {
            RightBumper.setPosition(1.0);
            LeftBumper.setPosition(0.4);
        }
    }

    //Get Color of the left side of the beacon (Returns true if it is == to the color red)
    public boolean getColor(){
        return Color.red() > Color.blue();
    }

    //Based on boolean argument to determine, used to hit the proper side of hte beacon in autonomous
    public void HitButton(boolean color){//Here the robot decides which beacon button to press.
        if(color){//Go for blue
            if (getColor()){
                BumperBeacon(true);
            }else {
                BumperBeacon(false);
            }
        }else {//Go for red
            if (!getColor()){
                BumperBeacon(true);
            }else {
                BumperBeacon(false);
            }
        }
    }
}