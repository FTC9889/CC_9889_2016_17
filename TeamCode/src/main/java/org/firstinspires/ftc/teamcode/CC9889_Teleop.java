package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
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
    GyroSensor Gyro;

    //DcMotor Encoders
    static final double EncoderCounts=1120;
    static final double WheelDiameter=4.0;
    static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1416);
    static final double DriveSpeed=0.7;
    static final double TurnSpeed=0.1;

    private ElapsedTime period  = new ElapsedTime();


    //Command Loop Breakout
    boolean breakout = false;

    @Override
    public void runOpMode (){

        setup();

        waitForStart();

        double leftspeed, rightspeed, xvalue, yvalue;
        int div = 1;

        while(opModeIsActive()){
            while (opModeIsActive() && breakout == false){
                xvalue = gamepad1.right_stick_x/div;
                yvalue = gamepad1.left_stick_y;

                leftspeed =  yvalue - xvalue;
                rightspeed = yvalue + xvalue;

                Drivetrain(-leftspeed, rightspeed);

                waitForTick(10);

                if (gamepad1.right_bumper == true){
                    div = 4;
                }else {
                    div = 1;
                }

                //Flywheel
                if(gamepad2.a){
                    rightShoot.setPower(-0.5);
                    leftShoot.setPower(-0.5);
                }else if(gamepad2.x){
                    rightShoot.setPower(-0.6);
                    leftShoot.setPower(-0.6);
                }else if(gamepad2.y){
                    rightShoot.setPower(-0.8);
                    leftShoot.setPower(-0.8);
                }else {
                    rightShoot.setPower(0.0);
                    leftShoot.setPower(0.0);
                }

                //Beacon pressing
                if(gamepad1.right_bumper){
                    BumperControl(false);
                }else {
                    BumperControl(true);
                }

                //Intake ctrl
                if(Math.abs(gamepad2.right_trigger) > 0.3){
                    Intake.setPower(0.5);
                }else if(Math.abs(gamepad2.left_trigger) > 0.3){
                    Intake.setPower(-1.0);
                }else if(gamepad2.right_bumper){
                    IntakeServo.setPower(-1.0);
                    Intake.setPower(0.2);
                }else {
                    IntakeServo.setPower(1.0);
                    Intake.setPower(0.0);
                }

            }
        }
    }

    //Functions

    //Setup
    public void setup(){
        //Drive Motors
        LDrive1 = hardwareMap.dcMotor.get("LDrive1");
        LDrive2 = hardwareMap.dcMotor.get("LDrive2");
        RDrive1 = hardwareMap.dcMotor.get("RDrive1");
        RDrive2 = hardwareMap.dcMotor.get("RDrive2");

        //Shooter Motors
        leftShoot = hardwareMap.dcMotor.get("LeftShoot");
        rightShoot = hardwareMap.dcMotor.get("RightShoot");

        //Intake Motor
        Intake = hardwareMap.dcMotor.get("IntakeMotor");

        //Servos
        RightBumper = hardwareMap.servo.get("RBump");
        LeftBumper = hardwareMap.servo.get("LBump");
        IntakeServo = hardwareMap.crservo.get("Intake");

        //Sensors
        Color = hardwareMap.colorSensor.get("colorsensor");
        RWhiteLine = hardwareMap.opticalDistanceSensor.get("OD1");
        LWhiteLine = hardwareMap.opticalDistanceSensor.get("OD2");

        //Tweaks to the hardware #Linsanity
        LDrive1.setDirection(DcMotor.Direction.REVERSE);
        RDrive1.setDirection(DcMotor.Direction.REVERSE);
        rightShoot.setDirection(DcMotorSimple.Direction.REVERSE);

        //Drive Mode
        LDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        RDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        //Drive Wheels no power
        LDrive1.setPowerFloat();
        RDrive1.setPowerFloat();
        LDrive2.setPowerFloat();
        RDrive2.setPowerFloat();

        //Flywheel no power
        leftShoot.setPowerFloat();
        rightShoot.setPowerFloat();

        leftShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightShoot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        leftShoot.setMaxSpeed(26);
        rightShoot.setMaxSpeed(26);

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
            LeftBumper.setPosition(0.7);
            RightBumper.setPosition(0.3);
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

    //Drive
    public void Drivetrain(double left, double right){
        LDrive1.setPower(left);
        LDrive2.setPower(left);
        RDrive1.setPower(right);
        RDrive2.setPower(right);
    }

}
