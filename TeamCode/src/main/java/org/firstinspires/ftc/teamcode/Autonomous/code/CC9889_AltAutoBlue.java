package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
@Autonomous(name="AltAutoBlue", group="Blue")
public class CC9889_AltAutoBlue extends LinearOpMode {


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

    //Intake Servo
    CRServo IntakeServo;

    //Sensors
    OpticalDistanceSensor RWhiteLine;
    OpticalDistanceSensor LWhiteLine;
    ColorSensor Color;
    GyroSensor Gyro;

    //DcMotor Encoders
    static final double EncoderCounts=1120;
    static final double WheelDiameter=4.0;
    static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1415926535897932384626433832795);
    static final double DriveSpeed=0.7;
    static final double TurnSpeed=0.1;

    private ElapsedTime period  = new ElapsedTime();
    private ElapsedTime runtime=new ElapsedTime();

    //Flywheel
    double Flywheelnum = 0.0;


    @Override
    public void runOpMode () {

        setup();

        waitForTick(50);

        waitForStart();

        sleep(5000);

        Drivetrain(-1.0, -1.0);
        sleep(860);

        STOP();

        leftShoot.setPower(-0.8);
        rightShoot.setPower(-0.8);
        sleep(2000);
        Intake.setPower(0.5);
        IntakeServo.setPower(-0.5);
        sleep(2000);
        IntakeServo.setPower(0.2);
        sleep(5000);

        Drivetrain(-1.0,-1.0);
        sleep(2000);
        super.stop();

    }

    //Functions
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

     //Go to white line
    public void FindWhiteTape(double speed, boolean color){
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        while (opModeIsActive() && RWhiteLine.getRawLightDetected() < 1.5 && LWhiteLine.getRawLightDetected() < 1.5){
            Drivetrain(speed,speed);            waitForTick(10);
        }
        Drivetrain(0,0);

        sleep(5000);

        if (opModeIsActive() && color == true){
            while (opModeIsActive() && LWhiteLine.getRawLightDetected() < 1.5){
                Drivetrain(0.5,-0.5);
                waitForTick(10);
            }
        }else {
            while (opModeIsActive() && RWhiteLine.getRawLightDetected() < 1.5){
                Drivetrain(-0.5,0.5);
                waitForTick(10);
            }
        }
        Drivetrain(0,0);
        sleep(500);

        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    //Follow Line and Press Button
    public void HitButton(boolean color){
        Drivetrain(0,0);

        if(opModeIsActive() && color == true){
            Drivetrain(0.1,0.1);

            sleep(1050);

            if(Color.red() < Color.blue()){
                LeftBumper.setPosition(0.8);
            }else {
                RightBumper.setPosition(0.2);
            }

            sleep(1000);

            Drivetrain(0,0);
        }
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

    //Flywheel Control
    public void Flywheel(int Position){
        if(Position == 0){
            Flywheelnum = 0.0;
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
        }else {
            Flywheelnum = 1.0;
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
            sleep(500);
            if(Position == 1){
                Flywheelnum = 0.34;
            }else if(Position == 2){
                Flywheelnum = 0.60;
            }else if(Position == 4){
                Flywheelnum = 0.8;
            }else {
                Flywheelnum = 0.0;
            }
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
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
        LDrive1.setPower(-left);
        //LDrive2.setPower(-left);
        RDrive1.setPower(right);
        //RDrive2.setPower(right);
    }

    public double getLeftEncoder() {
        return LDrive1.getCurrentPosition();
    }

    public double getRightEncoder() {
        return RDrive1.getCurrentPosition();
    }

    public double getLeftEncoderinInches(){
        return LDrive1.getCurrentPosition()*CountsPerInch;
    }

    public double getRightEncoderinInches(){
        return RDrive1.getCurrentPosition()*CountsPerInch;
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
        sleep(100);
        RDrive1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LDrive1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void STOP(){
        sleep(100);

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Drivetrain(0.0, 0.0);

        sleep(300);

        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
}