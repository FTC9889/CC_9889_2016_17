package org.firstinspires.ftc.teamcode.Season.Code.Test.Code;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.*;

/**
 * Created by Joshua H on 2/20/2017.
 */

@TeleOp(name="TeleopTest", group="Teleop_2_Controllers")
@Disabled
public class TeleopLoopType extends OpMode {

    /* Declare OpMode members. */
    private Flywheel Flywheel_Intake          = new Flywheel();
    private Drivebase Drivetrain              = new Drivebase();
    private org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Beacon Beacon                     = new Beacon();
    private org.firstinspires.ftc.teamcode.Season.Code.Subsystems.waitForTick waitForTick           = new waitForTick();
    private LED_Control led_control           = new LED_Control();

    private ElapsedTime runtime               = new ElapsedTime();
    private ElapsedTime shot                  =new ElapsedTime();


    private boolean SmartShot = false;

    private double leftspeed, rightspeed, xvalue, yvalue;
    private int div = 1;

    @Override
    public void init(){
        //////////////////////////////////////////////////////////////////
        //   Note:                                                      //
        //      To see the methods called please refer below.           //
        //==============================================================//
        //    ____________________________________________________      //
        //   | Name                ==      Class                  |     //
        //   |--------------------------------------------------- |     //
        //   | Beacon              ==      Subsystem.Beacon       |     //
        //   | Flywheel_Intake     ==      Subsystems.Flywheel    |     //
        //   | Drivetrain          ==      Subsystems.Drivebas    |     //
        //   | waitForTick         ==      Subsystems.waitForTick |     //
        //   ------------------------------------------------------     //
        //////////////////////////////////////////////////////////////////

        // Init Hardwawre
        // note: waitForTick does not have a hardware map
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap, false);
        led_control.init(hardwareMap);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Robot", " Running");    //
        telemetry.update();

        Drivetrain.STOP();
    }

    @Override
    public void start(){
        //Reset the time to allow for timer to stop automatically
        runtime.reset();
    }

    @Override
    public void loop(){
        if(runtime.seconds()<120){
            xvalue = -gamepad1.right_stick_x/div;
            yvalue = gamepad1.left_stick_y/div;

            leftspeed =  yvalue - xvalue;
            rightspeed = yvalue + xvalue;

            Drivetrain.setLeftRightPower(leftspeed, rightspeed);

            //Lower the max speed of the robot
            if (gamepad1.left_trigger > 0.2){
                div = 4;
            }else {
                div = 1;
            }

            //Beacon pressing
            Beacon.BumperSynchronised(!(Drivetrain.getUltrasonic() < 35 || gamepad1.right_bumper));

            //Smart Shot
            if(gamepad1.right_trigger > 0.2){
                if (SmartShot) {
                    shot.reset();
                    SmartShot = false;
                }

                if(shot.milliseconds() > 700){
                    Flywheel_Intake.AutoShoot(true, true);
                    if(shot.milliseconds() > 1400){
                        SmartShot = true;
                    }
                    led_control.setLedMode(true);
                }else {
                    led_control.setLedMode(false);
                    Flywheel_Intake.AutoShoot(true, false);
                }

            }else {
                SmartShot = true;

                //Flywheel
                Flywheel_Intake.setFlywheel(gamepad2.a);

                //Intake ctrl
                if(Math.abs(gamepad2.right_trigger) > 0.01){
                    Flywheel_Intake.setIntakeMode(2);
                }else if(gamepad2.left_bumper) {
                    Flywheel_Intake.setIntakeMode(3);
                }else {
                    Flywheel_Intake.setIntakeMode(4);
                }

                led_control.setLedMode(true);
            }

            updateData();
        }else {
            super.stop();
        }

        // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
        waitForTick.function(40);
    }

    @Override
    public void stop(){
        Drivetrain.STOP();
        Beacon.BumperSynchronised(false);
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

