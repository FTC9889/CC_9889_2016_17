package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Flywheel_Intake {

    //Flywheel Motors
    DcMotor flyWheel;

    //Intake Motor
    DcMotor Intake;

    //Intake Servo
    CRServo IntakeServo;

    //Used for controlling the intake
    private int IntakeVariable = 0;

    HardwareMap hardware = null;

    /* Constructor */
    public Flywheel_Intake() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap hardware) {
        //Shooter Motors
        flyWheel = hardware.dcMotor.get("flywheel");
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Intake = hardware.dcMotor.get("IntakeMotor");
        IntakeServo = hardware.crservo.get("Intake");
    }

    //Used to control all of the flywheel's actions
    public void setFlywheel(boolean on){
        if (on == true){
            flyWheel.setPower(-0.3);
        }else {
            flyWheel.setPower(0.0);
        }
    }

    //Used to control all of the Intake's actions
    public void setIntake(int mode){
        if (mode == 0){//Mode 0 == Stop all motors
            IntakeServo.setPower(0.0);
            Intake.setPower(0.0);
        }else if(mode == 1){//Mode 1 == Shoot
            IntakeServo.setPower(-1.0);
            Intake.setPower(0.5);
        }else if(mode == 2){//Mode 2 == Intake
            IntakeServo.setPower(1.0);
            if(IntakeVariable < 1000){//Control so the longer it is used, the slower it turns
                Intake.setPower(1.0);
                IntakeVariable++;
            }else {
                Intake.setPower(0.5);
                if(IntakeVariable > 3000){
                    IntakeVariable = 0;
                }
            }
        }else if(mode == 3){//Mode 3 == Outtake
            IntakeServo.setPower(1.0);
            Intake.setPower(-0.5);
        }else if(mode == 4){//Mode 4 == Keep particles away from flywheel
            IntakeServo.setPower(1.0);
            Intake.setPower(0.0);
        }
    }

}
