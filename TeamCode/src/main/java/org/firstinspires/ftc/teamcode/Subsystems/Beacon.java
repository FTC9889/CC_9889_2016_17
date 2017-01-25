package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Beacon {

    //Beacon-pushing Servos
    Servo RightBumper, LeftBumper;

    //MR Color Sensor
    ColorSensor Color;

    public Beacon(){

    }

    HardwareMap beacon  = null;

    public void init(HardwareMap hardwareMap){
        //Servos
        RightBumper = hardwareMap.servo.get("RBump");
        LeftBumper = hardwareMap.servo.get("LBump");

        //Color Sensor
        Color = hardwareMap.colorSensor.get("colorsensor");
    }

    //Controller for all bumper actions that are synchronised
    public void BumperSynchronised(boolean updown){
        if(updown == true){
            LeftBumper.setPosition(1.0);
            RightBumper.setPosition(0.4);
        }else if(updown == false){
            LeftBumper.setPosition(0.3);
            RightBumper.setPosition(0.9);
        }
    }

    //Function for Hitting the proper color in autonomous
    //Bumper Lift left or right (value of true will lift right servo)
    public void BumperBeacon(boolean right){
        if (right){
            RightBumper.setPosition(0.4);
        }else {
            LeftBumper.setPosition(1.0);
        }
    }

    //Get Color of the left side of the beacon (Returns true if it is == to the color red)
    public boolean getColor(){
        if (Color.red() > Color.blue()){
            return true;
        }else {
            return false;
        }
    }

    public void HitButton(boolean color){//Here the robot decides which beacon button to press.
        if(color == true){//Go for red
            if (getColor()){
                BumperBeacon(true);
            }else {
                BumperBeacon(false);
            }
        }else {//Go for blue
            if (getColor() == false){
                BumperBeacon(true);
            }else {
                BumperBeacon(false);
            }
        }}

}
