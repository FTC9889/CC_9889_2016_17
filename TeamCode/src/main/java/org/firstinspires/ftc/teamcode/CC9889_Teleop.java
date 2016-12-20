package org.firstinspires.ftc.teamcode;

import android.media.SoundPool;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Joshua H on 10/15/2016
 */
@TeleOp(name="Teleop", group="Teleop")
public class CC9889_Teleop extends LinearOpMode {

    //Motors
    DcMotor rightShoot;
    DcMotor leftShoot;
    DcMotor RDrive;
    DcMotor LDrive;

    //Servos
    Servo RightBumper;
    Servo LeftBumper;

    //Sensors
    OpticalDistanceSensor RWhiteLine;
    OpticalDistanceSensor LWhiteLine;
    ColorSensor Color;
    GyroSensor Gyro;



    private ElapsedTime period  = new ElapsedTime();
    @Override
    public void runOpMode (){

        setup();

        waitForStart();

        double leftspeed = 0.0;
        double rightspeed = 0.0;

        while (opModeIsActive()){
            leftspeed = -gamepad1.left_stick_y;
            rightspeed = -gamepad1.right_stick_y;

            LDrive.setPower(leftspeed);
            RDrive.setPower(rightspeed);

            waitForTick(10);
        }
    }

    //Functions

    //Setup
    public void setup(){
               //Drive Motors
        LDrive =hardwareMap.dcMotor.get("leftdrive");
        RDrive = hardwareMap.dcMotor.get("rightdrive");

        //Shooter Motors
        leftShoot = hardwareMap.dcMotor.get("flywheelleft");
        rightShoot = hardwareMap.dcMotor.get("flywheelright");

        //Servos
        RightBumper = hardwareMap.servo.get("r");
        LeftBumper = hardwareMap.servo.get("l");

        //Sensors
        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        Color = hardwareMap.colorSensor.get("colorsensor");
        RWhiteLine = hardwareMap.opticalDistanceSensor.get("rods");
        LWhiteLine = hardwareMap.opticalDistanceSensor.get("lods");

        //Tweaks to the hardware #Linsanity
        LDrive.setDirection(DcMotor.Direction.REVERSE);
        rightShoot.setDirection(DcMotorSimple.Direction.REVERSE);

        //Drive Mode
        LDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        RDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        //Drive Wheels no power
        LDrive.setPowerFloat();
        RDrive.setPowerFloat();

        //Flywheel no power
        leftShoot.setPowerFloat();
        rightShoot.setPowerFloat();

        leftShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Servo Movement
        BumperControl(true);
        // start calibrating the gyro.
        /**
        telemetry.addData(">", "Gyro Calibrating. Do Not move! (Please or Josh will keel you.)");
        telemetry.update();
       gyro.calibrate();

        // make sure the gyro is calibrated.
        while (!isStopRequested() && gyro.isCalibrating())  {
            sleep(50);
            idle();
        }
        gyro.resetZAxisIntegrator();
        //Message About Gyro
        telemetry.addData(">", "Gyro Calibrated. Wooooooooooooooo");
        telemetry.update();
         **/
    }
    
    //Controller for all bumper actions
    public void BumperControl(boolean updown){
        if(updown == true){
            LeftBumper.setPosition(0.8);
            RightBumper.setPosition(0.2);
        }else if(opModeIsActive() && updown == false){
            LeftBumper.setPosition(0.12);
            RightBumper.setPosition(0.88);
        }
    }
    
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

}
