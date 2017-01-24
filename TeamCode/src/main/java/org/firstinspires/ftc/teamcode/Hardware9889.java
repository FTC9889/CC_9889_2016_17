package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 *
 */

public class Hardware9889
{

    //Flywheel Motors
    DcMotor flyWheel;

    //Drivetrain Motors
    DcMotor RDrive1, RDrive2, LDrive1, LDrive2;

    //Intake Motor
    DcMotor Intake;

    //Beacon-pushing Servos
    Servo RightBumper, LeftBumper;

    //Intake CRservo
    CRServo IntakeServo;

    //Sensors
    OpticalDistanceSensor BackODS;
    OpticalDistanceSensor FrontODS;
    ColorSensor Color;
    public ModernRoboticsI2cGyro gyro;

    //Legacy Sensors
    UltrasonicSensor ultrasonic;

    //Modules
    VoltageSensor voltage;
    LegacyModule legacyModule;

    //DcMotor Encoders
    static final float EncoderCounts=1120;
    static final float WheelDiameter=4;
    public static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1415926535897932384626433832795);

    //Used for controlling the intake
    private int IntakeVariable = 0;

    //Value of white line
    private final double WhiteLineValue = 1.4;

    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();
    private ElapsedTime sleep = new ElapsedTime();//Used to wait when using the STOP function

    /* Constructor */
    public Hardware9889() {
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

        //Tweaks to the hardware #Linsanity
        LDrive1.setDirection(DcMotor.Direction.REVERSE);
        LDrive2.setDirection(DcMotor.Direction.REVERSE);

        //Drive Mode
        LDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        resetEncoders();

        //Shooter Motors
        flyWheel = hwMap.dcMotor.get("flywheel");
        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //Intake Motor
        Intake = hwMap.dcMotor.get("IntakeMotor");

        //Servos
        RightBumper = hwMap.servo.get("RBump");
        LeftBumper = hwMap.servo.get("LBump");
        IntakeServo = hwMap.crservo.get("Intake");

        //Sensors
        Color = hwMap.colorSensor.get("colorsensor");
        BackODS = hwMap.opticalDistanceSensor.get("OD1");
        FrontODS = hwMap.opticalDistanceSensor.get("OD2");
        gyro = (ModernRoboticsI2cGyro)hwMap.get("gyro");

        //Legacy Sensors
        ultrasonic = hwMap.ultrasonicSensor.get("ultra");

        //Modules
        legacyModule = hwMap.legacyModule.get("Legacy Module");
        voltage = hwMap.voltageSensor.get("Motor Controller 6");

        IntakeControl(0);

        BumperControl(true);
    }

    //Built-in function by FIRST. Put in all loops
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

    //Drivetrain
    public void Drivetrain(double left, double right){
        LDrive1.setPower(left);
        LDrive2.setPower(left);
        RDrive1.setPower(right);
        RDrive2.setPower(right);
    }

    //Controller for all bumper actions
    public void BumperControl(boolean updown){
        if(updown == true){
            LeftBumper.setPosition(1.0);
            RightBumper.setPosition(0.4);
        }else if(updown == false){
            LeftBumper.setPosition(0.15);
            RightBumper.setPosition(0.9);
        }
    }

    //Bumper Lift left or right (value of true will lift right servo)
    public void BumperBeacon(boolean right){
        if (right){
            RightBumper.setPosition(0.4);
        }else {
            LeftBumper.setPosition(1.0);
        }
    }

    //Used to Stop the Drivetrain
    public void STOP(){
        Drivetrain(0.0,0.0);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        sleep.reset();

        while (sleep.milliseconds() < 50){//Used to make sure the robot is stopped
            Drivetrain(0.0,0.0);
            waitForTick(50);
        }

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    //Used to control all of the flywheel's actions
    public void Flywheel(boolean on){
        if (on == true){
            flyWheel.setPower(-0.3);
        }else {
            flyWheel.setPower(0.0);
        }

    }

    //Used to control all of the different states of the intake
    public void IntakeControl(int mode){
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
        }
    }

    //Get position of left side of the drivetrain
    public int getLeftEncoder() {
        return LDrive2.getCurrentPosition();
    }

    //Get position of right side of the drivetrain
    public int getRightEncoder() {
        return RDrive2.getCurrentPosition();
    }

    //Get position of left side of the drivetrain in inches
    public double getLeftEncoderinInches() {
        return getLeftEncoder()/CountsPerInch;
    }

    //Get position of right side of the drivetrain in inches
    public double getRightEncoderinInches() {
        return getRightEncoder()/CountsPerInch;
    }

    //Return the power applied to the left side of the drivetrain
    public double getLeftPower(){
        return (LDrive1.getPower()+LDrive2.getPower())/2;
    }

    //Return the power applied to the right side of the drivetrain
    public double getRightPower(){
        return (RDrive1.getPower()+RDrive2.getPower())/2;
    }

    //Get the Z-axis value of the gyro
    public int getGyro(){
        return gyro.getIntegratedZValue();
    }

    //Get the distance between the Lego Ultrasonic Sensor and an object
    public double getUltrasonic(){
        return ultrasonic.getUltrasonicLevel();
    }

    //Get raw value of back ods
    public double getBackODS(){
        return BackODS.getRawLightDetected();
    }

    //Return the value of true if white line is detected
    public boolean getBackODS_Detect_White_Line(){
        if(getBackODS() > WhiteLineValue){
            return true;
        }else {
            return false;
        }
    }

    //Get raw value of front ods
    public double getFrontODS(){
        return FrontODS.getRawLightDetected();
    }

    //Return the value of true if white line is detected
    public boolean getFrontODS_Detect_White_Line(){
        if(getFrontODS() > WhiteLineValue){
            return true;
        }else {
            return false;
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

    //Reset Gyro Sensor
    public void resetGyro(){
        gyro.resetZAxisIntegrator();
    }

    //Reset the drivetrain encoders
    public void resetEncoders(){
        STOP();
        RDrive1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RDrive2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LDrive1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}