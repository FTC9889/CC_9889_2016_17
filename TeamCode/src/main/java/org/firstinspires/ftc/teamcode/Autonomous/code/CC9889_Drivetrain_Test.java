package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.*;


/**
 * Created by Joshua H on 1/28/2017.
 */

@Autonomous(name = "Drivetest",  group = "Test")
public class CC9889_Drivetrain_Test extends LinearOpMode{

    Flywheel Flywheel_Intake   = new Flywheel();
    Intake Intake       = new Intake();
    Drivebase Drivetrain             = new Drivebase();
    Beacon Beacon                     = new Beacon();
    waitForTick waitForTick           = new waitForTick();

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
        Drivetrain.init(hardwareMap);

        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();

        telemetry.addData("Ready to roll","");
        telemetry.update();

        waitForStart();

        //Drive Straight For 22 inches
        while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(22) && !gamepad1.a){
            Drivetrain.setLeftRightPower(-0.1, -0.1);
            updateData();
        }

        while (opModeIsActive() && Drivetrain.TurnAreWeThereYet(0)){
            Drivetrain.turnAbsolute(0, 0.1);
            updateData();
        }

        Drivetrain.STOP();

        telemetry.clearAll();

        super.stop();
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
