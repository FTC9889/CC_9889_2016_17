package org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Test;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Test.util.*;

/**
 * Created by Joshua H on 3/20/2017.
 */

public abstract class Drivebase extends Subsystems {
    private DriveControlState driveControlState_;

    private Constants Constants = new Constants();

    public enum DriveControlState {
        OPEN_LOOP, BASE_LOCKED, VELOCITY_SETPOINT, VELOCITY_HEADING_CONTROL, PATH_FOLLOWING_CONTROL
    }

    private DcMotor RDrive1, RDrive2, LDrive1, LDrive2;

    //Sensors
    private OpticalDistanceSensor BackODS, FrontODS;
    public ModernRoboticsI2cGyro gyro;
    public UltrasonicSensor ultrasonic;

    private DeviceInterfaceModule CDI;



    public void zeroSensors(){

    }

    public void Drive(HardwareMap drivetrain) {
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
        resetEncoders();
    }

    //Set Left and Right Powers on Drivetrain
    public void setLeftRightPower(double left, double right){
        LDrive1.setPower(left);
        LDrive2.setPower(left);
        RDrive1.setPower(right);
        RDrive2.setPower(right);
    }

    public synchronized void resetEncoders() {
        this.LDrive1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.LDrive2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.RDrive1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.LDrive2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.LDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.LDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.RDrive1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.RDrive2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public synchronized DriveControlState getControlState(){
        return driveControlState_;
    }

    public double getLeftDistanceInches() {
        return rotationsToInches(LDrive1.getCurrentPosition());
    }

    public double getRightDistanceInches() {
        return rotationsToInches(RDrive1.getCurrentPosition());
    }

    public double getGyro() {
        return gyro.rawZ();
    }

    public synchronized Rotation2d getGyroAngle() {
        return Rotation2d.fromDegrees(gyro.rawZ());
    }

    private double rotationsToInches(double rotations) {
        return rotations * (Constants.kDriveWheelDiameterInches * Math.PI);
    }

    private double inchesToRotations(double inches) {
        return inches / (Constants.kDriveWheelDiameterInches * Math.PI);
    }

}
