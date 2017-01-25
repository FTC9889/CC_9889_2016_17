package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.*;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class Basic_Linear_OpMode extends LinearOpMode{

    Flywheel_Intake Flywheel_Intake   = new Flywheel_Intake();
    Drivebase Drivetrain             = new Drivebase();
    Beacon Beacon                     = new Beacon();
    waitForTick waitForTick           = new waitForTick();

    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode(){
        //Init the robot
        Flywheel_Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap);
        Beacon.init(hardwareMap);
        waitForTick.init();

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
