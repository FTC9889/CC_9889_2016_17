package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
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
    DcMotor rightShoot;
    DcMotor leftShoot;

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

    //DcMotor Encoders
    static final double EncoderCounts=1120;
    static final double WheelDiameter=4.0;
    static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1416);


    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

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

        //Shooter Motors
        leftShoot = hwMap.dcMotor.get("LeftShoot");
        rightShoot = hwMap.dcMotor.get("RightShoot");

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

        //Tweaks to the hardware #Linsanity
        LDrive1.setDirection(DcMotor.Direction.REVERSE);
        RDrive1.setDirection(DcMotor.Direction.REVERSE);
        rightShoot.setDirection(DcMotorSimple.Direction.REVERSE);

        //Drive Mode
        LDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //rightShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        leftShoot.setMaxSpeed(26);
        rightShoot.setMaxSpeed(26);
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

    public void STOP(){

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void shot(double speed){
        leftShoot.setPower(speed);
        rightShoot.setPower(speed);
    }
}
