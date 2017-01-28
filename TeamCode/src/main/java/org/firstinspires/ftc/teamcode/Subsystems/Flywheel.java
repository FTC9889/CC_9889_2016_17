package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Flywheel {

    //Flywheel Motors
    DcMotor flyWheel;

    //Intake Motor
    DcMotor DC;

    //Intake Servo
    CRServo Servo;


    //Used for controlling the intake
    private int IntakeVariable = 0;

    ElapsedTime AutoShootTime = new ElapsedTime();

    private boolean Shoot = false;

    HardwareMap hardware = null;
    Intake Intake = null;

    /* Constructor */
    public Flywheel() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap hardware) {

        //Intake
        DC = hardware.dcMotor.get("IntakeMotor");
        Servo = hardware.crservo.get("Intake");

        //Shooter Motors
        flyWheel = hardware.dcMotor.get("flywheel");
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        setIntakeMode(0);
        setFlywheel(false);
    }

    //Used to control all of the flywheel's actions
    public void setFlywheel(boolean on){
        if (on == true){
            flyWheel.setPower(-0.5);
        }else {
            flyWheel.setPower(0.0);
        }
    }

    //Auto Shoot after flywheel is up to speed
    public void AutoShoot(boolean on){
        if(on == true){
            setFlywheel(true);
            if (AutoShootTime.milliseconds() > 2000){
                setIntakeMode(1);
            }
        }else {
            setFlywheel(false);
            setIntakeMode(4);
            AutoShootTime.reset();
        }
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