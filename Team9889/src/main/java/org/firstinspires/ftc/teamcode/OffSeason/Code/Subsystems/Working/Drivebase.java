package org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Drivebase{

    //Drivetrain Motors
    private DcMotor RDrive1, RDrive2, LDrive1, LDrive2;

    //Sensors
    private OpticalDistanceSensor BackODS, FrontODS;
    public ModernRoboticsI2cGyro gyro;
    public UltrasonicSensor ultrasonic;

    private DeviceInterfaceModule CDI;

    private org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.waitForTick waitForTick = new waitForTick();

    //DcMotor Encoders
    private static final float EncoderCounts=1120;
    private static final float WheelDiameter=4;
    private static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1415926535897932384626433832795);

    //Value of white line
    private final double WhiteLineValue = 0.6;

    public int AutonState = 1;

    public Drivebase(){

    }

    public enum DriveWithGyro{
        StraightWithGyro, Turn, ToWhiteLine
    }

    public enum Drive{
        Time, StraightWithPower, SingleMotor, Brake, PivotTurn
    }

    public enum DrivetrainSide{
        Left, Right, None
    }

    //Hardware Map
    public void init(HardwareMap drivetrain, boolean auton){

<<<<<<< HEAD:Team9889/src/main/java/org/firstinspires/ftc/teamcode/OffSeason/Code/Subsystems/Working/Drivebase.java
        //Drive Motors and Sensors
        this.LDrive1 = drivetrain.dcMotor.get("LDrive1");
        this.LDrive2 = drivetrain.dcMotor.get("LDrive2");
        this.RDrive1 = drivetrain.dcMotor.get("RDrive1");
        this.RDrive2 = drivetrain.dcMotor.get("RDrive2");
        this.BackODS = drivetrain.opticalDistanceSensor.get("OD1");
        this.FrontODS = drivetrain.opticalDistanceSensor.get("OD2");
        this.gyro = (ModernRoboticsI2cGyro)drivetrain.get("gyro");
        this.ultrasonic = drivetrain.ultrasonicSensor.get("ultra");
        this.CDI = drivetrain.deviceInterfaceModule.get("CDI");

        //Tweaks to the hardware
        this.LDrive1.setDirection(DcMotor.Direction.REVERSE);
        this.LDrive2.setDirection(DcMotor.Direction.REVERSE);

        //Drive Mode
        this.LDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.LDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.RDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.RDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
=======
        //Drive Motors
        try{
            LDrive1 = drivetrain.dcMotor.get("LDrive1");
            LDrive2 = drivetrain.dcMotor.get("LDrive2");
            //Tweaks to the hardware
            LDrive1.setDirection(DcMotor.Direction.REVERSE);
            LDrive2.setDirection(DcMotor.Direction.REVERSE);
            
            LDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            LDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        } catch (Exception p_exception){
            LDrive1 = null;
            Ldrive2 = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        
        try{
            RDrive1 = drivetrain.dcMotor.get("RDrive1");
            RDrive2 = drivetrain.dcMotor.get("RDrive2");
            //Tweaks to the hardware
            RDrive1.setDirection(DcMotor.Direction.REVERSE);
            RDrive2.setDirection(DcMotor.Direction.REVERSE);
            
            RDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            RDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        } catch (Exception p_exception){
            RDrive1 = null;
            Rdrive2 = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        
        try{
            BackODS = drivetrain.opticalDistanceSensor.get("OD1");
            FrontODS = drivetrain.opticalDistanceSensor.get("OD2");
        } catch(Exception p_execption){
            BackODS = null;
            FrontODS = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        
        try{
            gyro = (ModernRoboticsI2cGyro)drivetrain.get("gyro");
        } catch(Exception p_execption){
            gyro = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        
        try{
            ultrasonic = drivetrain.ultrasonicSensor.get("ultra");
        } catch(Exception p_execption){
            ultrasonic = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        
        try{
            CDI = drivetrain.deviceInterfaceModule.get("CDI");
        } catch(Exception p_execption){
            CDI = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
>>>>>>> origin/master:Team9889/src/main/java/org/firstinspires/ftc/teamcode/OffSeason/Code/Subsystems/Drivebase.java

        if(auton){
            resetEncoders();
            CalibrateGyro();
        }
    }

    //Set Left and Right Powers on Drivetrain
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

    public double Error(double WantedMeasure, double Measured){
        return Measured - WantedMeasure;
    }

    //Reset the drivetrain encoders
    private void resetEncoders(){
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

    //Get the Z-axis value of the gyro
    public int getGyro(){
        return gyro.getIntegratedZValue();
    }

    //Reset Gyro Sensor
    public void resetGyro(){
        gyro.resetZAxisIntegrator();
    }

    //Calibrate Gyro
    private void CalibrateGyro(){
        gyro.calibrate();
    }

    //Get the distance between the Lego Ultrasonic Sensor and an object
    public double getUltrasonic(){
        return ultrasonic.getUltrasonicLevel();
    }

    //Get raw value of back ods
    private double getBackODS(){
        return BackODS.getRawLightDetected();
    }

    //Return the value of true if white line is detected
    public boolean getBackODS_Detect_White_Line(){
        return (getBackODS() > WhiteLineValue);
    }

    public boolean getFrontODS_Detect_White_Line(){
        return getFrontODS() > WhiteLineValue;
    }

    //Get raw value of front ods
    private double getFrontODS(){
        return FrontODS.getRawLightDetected();
    }

    public void brake(){
        setLeftRightPower(0.0,0.0);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void DriveStraightWithGyro(int degree, Drivebase drivebase, double Firstpower){
        if(degree < getGyro()){
            drivebase.setLeftRightPower(-Math.abs(Firstpower)/2, -Math.abs(Firstpower));
        }else if(degree > getGyro()){
            drivebase.setLeftRightPower(-Math.abs(Firstpower), -Math.abs(Firstpower)/2);
        }else if(degree == getGyro()){
            drivebase.setLeftRightPower(-Math.abs(Firstpower), -Math.abs(Firstpower));
        }
    }

    private void Turn(int degree, Drivebase drivebase, double Power){
        if(degree < getGyro()){
            drivebase.setLeftRightPower(0, -Math.abs(Power));
        }else if(degree > getGyro()){
            drivebase.setLeftRightPower(-Math.abs(Power), 0);
        }else if(degree == getGyro()){
            drivebase.setLeftRightPower(0, 0);
            this.AutonState++;
        }
    }

    private void DriveToWhiteLine(int Gyro, double Power, Drivebase drivebase){
        drivebase.DriveStraightWithGyro(Gyro, drivebase, Power);
        if(drivebase.getBackODS_Detect_White_Line()){
            drivebase.brake();
            this.AutonState++;
        }
    }

    public void DriveWithGyro(DriveWithGyro driveControlState, int Gyro, double Power, Drivebase drivebase){
        switch (driveControlState){
            case StraightWithGyro:
                DriveStraightWithGyro(Gyro, drivebase, Power);
                break;
            case Turn:
                Turn(Gyro, drivebase, Power);
                break;
            case ToWhiteLine:
                DriveToWhiteLine(Gyro, Power, drivebase);
                break;
        }
    }

    public void Drive(Drive drive, double LeftPower, double RightPower, DrivetrainSide drivetrainSide, int Time, double Runtime, boolean Stop){
        switch (drive){
            case StraightWithPower:
                setLeftRightPower(-Math.abs(LeftPower), -Math.abs(RightPower));
                break;
            case Brake:
                STOP();
                break;
            case Time:
                setLeftRightPower(LeftPower, RightPower);
                if(Time > Runtime){
                    this.AutonState++;
                    if(Stop){
                        STOP();
                    }
                }
                break;
            case SingleMotor:
                if(drivetrainSide == DrivetrainSide.Left){
                    setLeftRightPower(LeftPower, 0.0);
                }else if(drivetrainSide == DrivetrainSide.Right){
                    setLeftRightPower(0.0, RightPower);
                }else if(drivetrainSide == DrivetrainSide.None){
                    setLeftRightPower(0.0, 0.0);
                }
                break;
            case PivotTurn:


        }
    }
}

