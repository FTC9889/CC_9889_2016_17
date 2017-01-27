package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Subsystems.waitForTick;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Drivebase{

    //Drivetrain Motors
    DcMotor RDrive1, RDrive2, LDrive1, LDrive2;

    //Sensors
    OpticalDistanceSensor BackODS, FrontODS;
    ModernRoboticsI2cGyro gyro;
    UltrasonicSensor ultrasonic;

    VoltageSensor voltage;

    HardwareMap drivetrain = null;
    waitForTick waitForTick = new waitForTick();

    private ElapsedTime period  = new ElapsedTime();
    private ElapsedTime turnTimer = new ElapsedTime();
    private ElapsedTime timer   = new ElapsedTime();
    private boolean turnTimerLogic = true;
    private boolean timerlogic = true;

    //DcMotor Encoders
    static final float EncoderCounts=1120;
    static final float WheelDiameter=4;
    public static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1415926535897932384626433832795);

    //Value of white line
    private final double WhiteLineValue = 1.4;

    private ElapsedTime sleep = new ElapsedTime();//Used to wait when using the STOP function

    public Drivebase(){

    }

    public void init(HardwareMap drivetrain){
        waitForTick.init(drivetrain);

        //Drive Motors and Sensors
        LDrive1 = drivetrain.dcMotor.get("LDrive1");
        LDrive2 = drivetrain.dcMotor.get("LDrive2");
        RDrive1 = drivetrain.dcMotor.get("RDrive1");
        RDrive2 = drivetrain.dcMotor.get("RDrive2");
        BackODS = drivetrain.opticalDistanceSensor.get("OD1");
        FrontODS = drivetrain.opticalDistanceSensor.get("OD2");
        gyro = (ModernRoboticsI2cGyro)drivetrain.get("gyro");
        ultrasonic = drivetrain.ultrasonicSensor.get("ultra");

        //Tweaks to the hardware
        LDrive1.setDirection(DcMotor.Direction.REVERSE);
        LDrive2.setDirection(DcMotor.Direction.REVERSE);

        //Drive Mode
        LDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        STOP();
        resetEncoders();
        resetGyro();
    }

    public void setLeftRightPower(double left, double right){
        LDrive1.setPower(left);
        LDrive2.setPower(left);
        RDrive1.setPower(right);
        RDrive2.setPower(right);
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

    //Used to Stop the Drivetrain
    public void STOP(){
        setLeftRightPower(0.0,0.0);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        sleep.reset();

        while (sleep.milliseconds() < 50){//Used to make sure the robot is stopped
            setLeftRightPower(0.0,0.0);
            waitForTick.function(50);
        }

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    //Reset Gyro Sensor
    public void resetGyro(){
        gyro.resetZAxisIntegrator();
    }

    //Is Gyro Calibrating?
    public boolean GyroisCalibrating(){
        return gyro.isCalibrating();
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

    //Drive Striaght till white line FIX SAFETY
    public void DriveStraighttoWhiteLine(double speed, boolean forward){
        if(forward){
            if (!getBackODS_Detect_White_Line()){
                setLeftRightPower(-Math.abs(speed), -Math.abs(speed));
            }else {
                STOP();
            }
        }else {
            if(!getBackODS_Detect_White_Line()){
                setLeftRightPower(Math.abs(speed), Math.abs(speed));
            }else {
                STOP();
            }
        }
        waitForTick.function(50);
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

    //Drive for time
    public void DriveforTime(double left, double right, int TimeinMiliseconds){
        Range.clip(left, -1, 1);
        Range.clip(right, -1, 1);

        setLeftRightPower(left, right);
    }

    public boolean TimeAreWeThereYet(int TimeinMilliseconds){
        if(period.milliseconds() > TimeinMilliseconds){
            timer.reset();
            return false;
        }else {
            if (timerlogic){
                timer.reset();
            }
            return true;
        }
    }


    //Drive Straight Forward for a distance in inches
    public void DriveForwardtoDistance(double speed, double inches){
        Range.clip(speed, -1, 1);

        if(getLeftEncoderinInches() > -Math.abs(inches) && getRightEncoderinInches() > -Math.abs(inches)){
            STOP();
            resetEncoders();
        }else {
            setLeftRightPower(-Math.abs(speed), -Math.abs(speed));
        }

    }

    public boolean InchesAreWeThereYet(double inches){
        if(Math.abs(getRightEncoderinInches()) > Math.abs(inches)){
            return true;
        }else {
            return false;
        }
    }

    //Drive Straight Backward for a distance in inches
    public void DriveBackwardstoDistance(double speed, double inches){
        Range.clip(speed, -1, 1);
        while (getLeftEncoderinInches() < Math.abs(inches) && getRightEncoderinInches() < Math.abs(inches)){
            setLeftRightPower(Math.abs(speed), Math.abs(speed));
            waitForTick.function(50);
        }
        STOP();

        resetEncoders();
    }

    //Drive Straight till Ultrasonic sensor value
    public void DriveToUltrasonicDistance(double speed, int distance){
        if(getUltrasonic() > distance){
            setLeftRightPower(-Math.abs(speed), -Math.abs(speed));
        }else {
            setLeftRightPower(Math.abs(speed), Math.abs(speed));
        }
    }

    public void CenterOnWhiteLine(double speed, boolean red){
        if(!getFrontODS_Detect_White_Line()){
            if(red){
                setLeftRightPower(Math.abs(speed), -Math.abs(speed));
            }else {
                setLeftRightPower(-Math.abs(speed), Math.abs(speed));
            }
        }else {
            if(red){
                setLeftRightPower(-Math.abs(speed), Math.abs(speed));
            }else {
                setLeftRightPower(Math.abs(speed), -Math.abs(speed));
            }
        }
        waitForTick.function(50);
    }

    public void turnAbsolute(int target, int target_Time, double turnSpeed) {
        if(turnTimerLogic){
            turnTimer.reset();
            turnTimerLogic = false;
        }

        if(target_Time > turnTimer.milliseconds()){
            turnSpeed = Math.abs(turnSpeed) * 2;
            Range.clip(turnSpeed, 0, 0.3);
        }

        if(target > getGyro()) {
            setLeftRightPower(Math.abs(turnSpeed), -Math.abs(turnSpeed));
        }else if(target < getGyro()) {
            setLeftRightPower(-Math.abs(turnSpeed), Math.abs(turnSpeed));
        }

        waitForTick.function(50);
    }

    public boolean TurnAreWeThereYet(int target){

        if(Math.abs(target) - Math.abs(getGyro()) < 1){
            return false;
        }else {
            turnTimerLogic = true;
            return true;
        }
    }

}

