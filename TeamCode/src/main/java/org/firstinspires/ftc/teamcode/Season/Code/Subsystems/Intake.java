package org.firstinspires.ftc.teamcode.Season.Code.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Joshua H on 1/27/2017.
 */

public class Intake {
    //Intake Motor
    DcMotor DC;

    //Intake Servo
    CRServo Servo;

    public void init(HardwareMap hardware){
        DC = hardware.dcMotor.get("IntakeMotor");
        Servo = hardware.crservo.get("Intake");
    }

    //Used to control all of the Intake's actions
    public void setIntakeMode(int mode){
        if (mode == 0){//Mode 0 == Stop all motors
            Servo.setPower(0.0);
            DC.setPower(0.0);
        }else if(mode == 1){//Mode 1 == Shoot
            Servo.setPower(-1.0);
            DC.setPower(0.5);
        }else if(mode == 2){//Mode 2 == Intake
            Servo.setPower(1.0);
            DC.setPower(1.0);
        }else if(mode == 3){//Mode 3 == Outtake
            Servo.setPower(1.0);
            DC.setPower(-0.5);
        }else if(mode == 4){//Mode 4 == Keep particles away from flywheel
            Servo.setPower(1.0);
            DC.setPower(0.0);
        }
    }

}
