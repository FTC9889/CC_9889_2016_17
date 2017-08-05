package org.firstinspires.ftc.teamcode.Season.Code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.*;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Basic_Linear_OpMode extends LinearOpMode{

    Flywheel Flywheel_Intake   = new Flywheel();
    org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Intake Intake       = new Intake();
    Drivebase Drivetrain             = new Drivebase();
    org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Beacon Beacon                     = new Beacon();
    org.firstinspires.ftc.teamcode.Season.Code.Subsystems.waitForTick waitForTick           = new waitForTick();

    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException{
        //Init the robot
        telemetry.addData("Beacon", "");
        telemetry.update();
        Beacon.init(hardwareMap);
        telemetry.addData("Flywheel", "");
        telemetry.update();
        Flywheel_Intake.init(hardwareMap);
        telemetry.addData("Drivetrain", "");
        telemetry.update();
        Drivetrain.init(hardwareMap, false);

        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();

        waitForStart();

        while (opModeIsActive()){
            telemetry.addData("OpMode Running for:", runtime.milliseconds());

            updateData();

            telemetry.update();
        }
    }

    public void updateData(){
        telemetry.addData("Time Left", 120-runtime.seconds());
        telemetry.addData("Right Speed", Drivetrain.getRightPower());
        telemetry.addData("Left Speed", Drivetrain.getLeftPower());
        telemetry.addData("Right Encoder", Drivetrain.getRightEncoder());
        telemetry.addData("Left Encoder", Drivetrain.getLeftEncoder());
        telemetry.addData("Right Encoder in Inches", Drivetrain.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", Drivetrain.getLeftEncoderinInches());
        telemetry.addData("Gyro Z-axis", Drivetrain.getGyro());
        telemetry.addData("Ultrasonic Sensor Raw Value", Drivetrain.getUltrasonic());
        telemetry.addData("Back ODS", Drivetrain.getBackODS());
        telemetry.addData("Front ODS", Drivetrain.getFrontODS());
        telemetry.update();
    }
}
