package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 *
 */
public class AutoHardware9889
{

    //Flywheel Motors
    DcMotor flyWheel;

    //Drivetrain Motors
    DcMotor RDrive1;
    DcMotor RDrive2;
    DcMotor LDrive1;
    DcMotor LDrive2;

    //Intake Motor
    DcMotor Intake;

    //Beacon-pushing Servos
    Servo RightBumper;
    Servo LeftBumper;

    //Intake CRservo
    CRServo IntakeServo;

    //Sensors
    OpticalDistanceSensor RWhiteLine;
    OpticalDistanceSensor LWhiteLine;
    ColorSensor Color;
    LightSensor light;
    ModernRoboticsI2cGyro gyro;

    //DcMotor Encoders
    static final float EncoderCounts=1120;
    static final float WheelDiameter=4;
    static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1415926535897932384626433832795);


    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public AutoHardware9889() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        //Drive Motors
        LDrive1 = hwMap.dcMotor.get("LDrive1");
        LDrive2 = hwMap.dcMotor.get("LDrive2");
        RDrive1 = hwMap.dcMotor.get("RDrive1");
        RDrive2 = hwMap.dcMotor.get("RDrive2");

        //Shooter Motors
        flyWheel = hwMap.dcMotor.get("flywheel");

        //Intake Motor
        Intake = hwMap.dcMotor.get("IntakeMotor");

        //Servos
        RightBumper = hwMap.servo.get("RBump");
        LeftBumper = hwMap.servo.get("LBump");
        IntakeServo = hwMap.crservo.get("Intake");

        //Sensors
        Color = hwMap.colorSensor.get("colorsensor");
        RWhiteLine = hwMap.opticalDistanceSensor.get("OD1");
        LWhiteLine = hwMap.opticalDistanceSensor.get("OD2");
        light = hwMap.lightSensor.get("ltbl");
        gyro =(ModernRoboticsI2cGyro)hwMap.gyroSensor.get("gyro");

        //Tweaks to the hardware #Linsanity
        RDrive1.setDirection(DcMotor.Direction.REVERSE);
        RDrive2.setDirection(DcMotor.Direction.REVERSE);

        //Drive Mode
        LDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RDrive2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        BumperControl(true);

        gyro.calibrate();

    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
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

    //Controller for all bumper actions
    public void BumperControl(boolean updown){
        if(updown == true){
            LeftBumper.setPosition(0.7);
            RightBumper.setPosition(0.3);
        }else if(updown == false){
            LeftBumper.setPosition(0.12);
            RightBumper.setPosition(0.88);
        }
    }

    //Drive
    public void Drivetrain(double left, double right){
        LDrive1.setPower(-left);
        LDrive2.setPower(-left);
        RDrive1.setPower(right);
        RDrive2.setPower(right);
    }

    public void EncoderDrive(double speed, double left, double right, boolean quickstop) {



    }

    public void STOP(){
        Drivetrain(0.0,0.0);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void Flywheel(boolean on){
        if (on == true){
            flyWheel.setPower(1.0);
        }else {
            flyWheel.setPower(0.5);
        }

    }

    public float getLeftEncoder() {
        return LDrive2.getCurrentPosition();
    }

    public float getRightEncoder() {
        return RDrive2.getCurrentPosition();
    }

    public double getLeftEncoderinInches() {
        return getLeftEncoder()/CountsPerInch;
    }

    public double getRightEncoderinInches() {
        return getRightEncoder()/CountsPerInch;
    }

    public double getAverageDistance(){
        return (getLeftEncoder()*getRightEncoder())/2.0;
    }

    public void driveSpeedTurn(double speed, double turn) {
        double left = speed + turn;
        double right = speed - turn;
        Drivetrain(left, right);
    }

    public void resetEncoders(){
        RDrive2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RDrive2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }




}
