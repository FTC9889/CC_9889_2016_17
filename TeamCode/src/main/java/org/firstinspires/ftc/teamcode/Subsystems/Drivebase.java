package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Drivebase{

    //Drivetrain Motors
    public DcMotor RDrive1, RDrive2, LDrive1, LDrive2;

    //Sensors
    public OpticalDistanceSensor BackODS, FrontODS;
    public ModernRoboticsI2cGyro gyro;
    public UltrasonicSensor ultrasonic;

    public VoltageSensor voltage;
    public DeviceInterfaceModule CDI;

    public waitForTick waitForTick = new waitForTick();

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

    public Drivebase(){

    }

    public void init(HardwareMap drivetrain){

        //Drive Motors and Sensors
        LDrive1 = drivetrain.dcMotor.get("LDrive1");
        LDrive2 = drivetrain.dcMotor.get("LDrive2");
        RDrive1 = drivetrain.dcMotor.get("RDrive1");
        RDrive2 = drivetrain.dcMotor.get("RDrive2");
        BackODS = drivetrain.opticalDistanceSensor.get("OD1");
        FrontODS = drivetrain.opticalDistanceSensor.get("OD2");
        gyro = (ModernRoboticsI2cGyro)drivetrain.get("gyro");
        ultrasonic = drivetrain.ultrasonicSensor.get("ultra");
        CDI = drivetrain.deviceInterfaceModule.get("CDI");

        //Tweaks to the hardware
        LDrive1.setDirection(DcMotor.Direction.REVERSE);
        LDrive2.setDirection(DcMotor.Direction.REVERSE);

        //Drive Mode
        LDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
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
        return (getBackODS() > WhiteLineValue);
    }

    //Get raw value of front ods
    public double getFrontODS(){
        return FrontODS.getRawLightDetected();
    }

    //Return the value of true if white line is detected
    public boolean getFrontODS_Detect_White_Line(){
        return (getFrontODS() > WhiteLineValue);
    }

    public boolean InchesAreWeThereYet(double inches){
        return !(Math.abs(getRightEncoderinInches()) > Math.abs(inches));
    }

    //Drive Straight Backward for a distance in inches
    public void DriveBackwardstoDistance(double speed, double inches){
        Range.clip(speed, -1, 1);
        while (getLeftEncoderinInches() > Math.abs(inches) && getRightEncoderinInches() > Math.abs(inches)){
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

    public void turnAbsolute(int target, double turnSpeed) {
        if(target < getGyro()) {
            setLeftRightPower(Math.abs(turnSpeed), -Math.abs(turnSpeed));
        }else if(target > getGyro()) {
            setLeftRightPower(-Math.abs(turnSpeed), Math.abs(turnSpeed));
        }else if(target == getGyro()){
            setLeftRightPower(0.0, 0.0);
        }

        waitForTick.function(50);
    }

    public boolean TurnAreWeThereYet(int target){
        return !((Math.abs(getGyro()) - Math.abs(target)) < 1);
    }

}

