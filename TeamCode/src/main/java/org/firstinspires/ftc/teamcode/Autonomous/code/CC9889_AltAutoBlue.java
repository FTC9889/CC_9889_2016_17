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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Hardware9889;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
@Autonomous(name="Auton", group="Blue")
@Disabled
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

    Hardware9889 robot           = new Hardware9889();              // Use a K9'shardware

    @Override
    public void runOpMode () {

        robot.init(hardwareMap);

        waitForStart();

        sleep(5000);

        robot.Drivetrain(-1.0, -1.0);
        sleep(860);

        robot.STOP();

        leftShoot.setPower(-0.8);
        rightShoot.setPower(-0.8);
        sleep(2000);
        Intake.setPower(0.5);
        IntakeServo.setPower(-0.5);
        sleep(2000);
        IntakeServo.setPower(0.2);
        sleep(5000);
        rightShoot.setPower(0.0);
        leftShoot.setPower(0.0);
        IntakeServo.setPower(0.0);
        Intake.setPower(0.0);
        robot.Drivetrain(-1.0,-1.0);
        sleep(2000);
        super.stop();

    }
}
