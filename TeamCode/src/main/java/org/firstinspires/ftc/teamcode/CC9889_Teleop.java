package org.firstinspires.ftc.teamcode;

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

    //DcMotors
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor leftShoot;
    DcMotor rightShoot;

    //Servos
    CRServo Color;
    Servo Shoot;
    Servo touchservo;

    //Sensors
    ModernRoboticsI2cGyro gyro;
    ColorSensor colorSensor;
    ModernRoboticsI2cRangeSensor rangeSensor;
    TouchSensor touchtouch;

    //Core Device Interface
    DeviceInterfaceModule CDI;


    private ElapsedTime period  = new ElapsedTime();
    @Override
    public void runOpMode (){

        setup();

        waitForStart();

        double leftspeed = 0.0;
        double rightspeed = 0.0;

        while (opModeIsActive()){

            //Color Servo Stay in
            Color.setPower(0.05);

            leftspeed = -gamepad1.left_stick_y;
            rightspeed = -gamepad1.right_stick_y;

            leftDrive.setPower(leftspeed);
            rightDrive.setPower(rightspeed);

            waitForTick(10);
        }
    }

    //Functions

    //Setup
    public void setup(){
        //Drive Motors
        leftDrive =hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");

        //Shooter Motors
        leftShoot = hardwareMap.dcMotor.get("leftShoot");
        rightShoot = hardwareMap.dcMotor.get("rightShoot");

        //Servos
        Color = hardwareMap.crservo.get("colorServo"); //This is crservo
        Shoot = hardwareMap.servo.get("ServoShoot");
        touchservo = hardwareMap.servo.get("touchservo");

        //Sensors
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        colorSensor = hardwareMap.colorSensor.get("color");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");
        touchtouch = hardwareMap.touchSensor.get("touch");

        //Core Device Interface
        CDI = hardwareMap.deviceInterfaceModule.get("Device Interface Module 1");

        //Tweaks to the hardware #Linsanity
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        leftShoot.setDirection(DcMotorSimple.Direction.REVERSE);

        //Drive Mode
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        //Drive Wheels no power
        leftDrive.setPowerFloat();
        rightDrive.setPowerFloat();

        //Flywheel no power
        leftShoot.setPowerFloat();
        rightDrive.setPowerFloat();

        //Servo Movement
        Shoot.setPosition(0.0);
        Color.setPower(0.0);


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
