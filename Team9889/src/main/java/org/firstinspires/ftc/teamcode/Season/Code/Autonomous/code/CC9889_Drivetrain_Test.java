package org.firstinspires.ftc.teamcode.Season.Code.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Beacon;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.waitForTick;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.*;



/**
 * Created by Joshua H on 1/28/2017.
 */

@Autonomous(name = "Drivetest",  group = "Test")
@Disabled
public class CC9889_Drivetrain_Test extends LinearOpMode{

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
        Drivetrain.init(hardwareMap, true);

        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();

        telemetry.addData("Ready to roll","");
        telemetry.update();

        waitForStart();

        //Drive Straight For 22 inches
        while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(22)){
            Drivetrain.setLeftRightPower(-0.1, -0.1);
            updateData();
        }

        Drivetrain.STOP();

        while (opModeIsActive() && Drivetrain.TurnAreWeThereYet(-15)){
            Drivetrain.setLeftRightPower(0.1, -0.1);
            updateData();
        }

        Drivetrain.STOP();
        sleep(100000);

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
