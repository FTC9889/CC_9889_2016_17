package org.firstinspires.ftc.teamcode.Season.Code.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Beacon;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.waitForTick;
import org.firstinspires.ftc.teamcode.Season.Code.Subsystems.*;

/**
 * Created by Jin on 9/30/2016.
 */

@Autonomous(name="Blue", group="Blue")
public class CC9889_AltAutoBlue extends LinearOpMode {

    //Classes
    private Flywheel Flywheel_Intake          = new Flywheel();
    private Drivebase Drivetrain              = new Drivebase();
    private org.firstinspires.ftc.teamcode.Season.Code.Subsystems.Beacon Beacon                     = new Beacon();
    private org.firstinspires.ftc.teamcode.Season.Code.Subsystems.waitForTick waitForTick           = new waitForTick();

    //If we drive in to a wall it will automatically stop the robot to prevent damage to the robot or field
    private ElapsedTime emergencystop         = new ElapsedTime();
    private boolean emergency = true;

    //randomnumberthatweneedforsomething is for the program chooser
    private int randomnumberthatweneedforsomething = 1;

    @Override
    public void runOpMode() throws InterruptedException{

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
        Drivetrain.init(hardwareMap,true);

        //Program Chooser
        while (!gamepad1.a) {
            if (gamepad1.dpad_up) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 1;
                telemetry.addData("Autonomous 1", "= Shoot and Park on Center");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            } else if(gamepad1.dpad_right){
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 2;
                telemetry.addData("Autonomous 2", "= 2 Beacon");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            }else if (gamepad1.dpad_down) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 3;
                telemetry.addData("Autonomous 3","= 1  Beacon and Ramp");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            }else if(gamepad1.dpad_left) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 4;
                telemetry.addData("Autonomous 4", "= 1 Beacon and Hit Cap Ball");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            }
            telemetry.update();
        }

        //Add telemetry
        telemetry.clearAll();
        telemetry.addData("Auton", " Selected");
        telemetry.update();

        waitForStart();

        //Make sure the gyro is calibrated.
        while (!opModeIsActive() && Drivetrain.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }

        //Show that we are running Autonomous
        telemetry.clearAll();
        telemetry.addData("Running Auton", " ");
        telemetry.update();

        //Reset all the things
        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();

        if (randomnumberthatweneedforsomething == 1){//Shoot and Park on Center Auton

            //Wait for partner to move out of the way
            sleep(20000);

            //Check if OpMode is still running
            if (opModeIsActive()){
                //Start Flywheel
                Flywheel_Intake.setFlywheel(true);
            }

            //Drive Straight For 35 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(35)){
                Drivetrain.setLeftRightPower(-0.6, -0.6);
            }

            Drivetrain.STOP();

            //Wait for robot to settle
            sleep(500);

            //Shoot particles
            if(opModeIsActive()){
                Drivetrain.resetEncoders();
                Flywheel_Intake.setIntakeMode(1);
                sleep(2000);
                Flywheel_Intake.setIntakeMode(0);
                Flywheel_Intake.setFlywheel(false);
            }

            //Park
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(35)){
                Drivetrain.setLeftRightPower(-0.6, -0.6);
            }

            Drivetrain.STOP();

        }else {///////////////////////Base One Beacon///////////////////////

            //Drive Straight For 15 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(15)){
                Drivetrain.setLeftRightPower(-0.3, -0.3);
                updateData();
            }

            //Turn on Flywheel
            if(opModeIsActive()){
                Flywheel_Intake.AutoShoot(true, false);
            }

            //Turn to the goal
            while (opModeIsActive() && Drivetrain.getGyro() < 3){
                Drivetrain.setLeftRightPower(-0.2, 0.2);
                updateData();
            }
            while (opModeIsActive() && Drivetrain.getGyro() < 14){
                Drivetrain.setLeftRightPower(-0.1, 0.1);
                updateData();
            }

            Drivetrain.STOP();

            //Shoot particles
            if(opModeIsActive()){
                sleep(400);
                Flywheel_Intake.AutoShoot(true, true);
                sleep(300);
                Flywheel_Intake.AutoShoot(true, false);
                sleep(700);
                Flywheel_Intake.AutoShoot(true, true);
                sleep(1000);
                Flywheel_Intake.AutoShoot(false, false);
                Flywheel_Intake.setIntakeMode(0);
            }

            //Turn to face first beacon
            while (opModeIsActive() && Drivetrain.getGyro() > -45){
                Drivetrain.setLeftRightPower(0.5, -0.5);
                sleep(10);
                Drivetrain.STOP();
                updateData();
            }
            Drivetrain.STOP();

            //Drive Straight till white line
            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.DriveStraighttoWhiteLine(0.3, true);
                if(Drivetrain.getUltrasonic() < 20){
                    if (emergency = true){
                        emergencystop.reset();
                        emergency = false;
                    }

                    if(emergencystop.seconds() > 3){
                        super.stop();
                    }
                }else {
                    emergencystop.reset();
                }
                updateData();
            }

            Drivetrain.STOP();

            sleep(100);

            //Back up slowly to line
            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.setLeftRightPower(0.2, 0.2);
                sleep(40);
                Drivetrain.STOP();
            }


            //Turn to goal
            while (opModeIsActive() && Drivetrain.getGyro() > -80){
                Drivetrain.setLeftRightPower(0.2, -0.2);
            }
            Drivetrain.STOP();

            sleep(100);

            while (opModeIsActive() && Drivetrain.getGyro() < -90){
                Drivetrain.setLeftRightPower(-0.2, 0.2);
                sleep(10);
                Drivetrain.STOP();
            }
            Drivetrain.STOP();


            //Back up if to close to goal
            while (opModeIsActive() && Drivetrain.getUltrasonic() < 20){
                Drivetrain.setLeftRightPower(0.2, 0.2);
            }

            Drivetrain.STOP();

            //Lower Beacon pressers
            Beacon.BumperSynchronised(false);


            //Drive until really close to beacon
            while (opModeIsActive() && Drivetrain.getUltrasonic() > 18){
                Drivetrain.setLeftRightPower(-0.1, -0.1);
            }

            Drivetrain.STOP();

            //Detect the color and raise the appropriate presser
            Beacon.HitButton(true);

            sleep(500);

            //Press beacon
            Drivetrain.setLeftRightPower(-0.2, -0.2);
            sleep(500);
            Drivetrain.resetEncoders();

            //Back away from goal
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(5)){
                Drivetrain.setLeftRightPower(0.2, 0.2);
            }

            Drivetrain.STOP();

            //Raise Beacon pressers
            Beacon.BumperSynchronised(true);

            ////////////////////////////////////////////////////////
            /////       Auton Picker                        ////////
            ////////////////////////////////////////////////////////

            if (randomnumberthatweneedforsomething == 2){
                //2 BEACON AUTONOMOUS
                //Turn to the Beacon
                while (opModeIsActive() && Drivetrain.getGyro() < -7){
                    Drivetrain.setLeftRightPower(-0.3, 0.3);
                    sleep(40);
                    Drivetrain.STOP();
                }

                //Go Straight for time to get away from first line, then start looking for second line
                Drivetrain.setLeftRightPower(-0.5, -0.5);
                sleep(500);
//Drive Straight till white line
                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.DriveStraighttoWhiteLine(0.3, true);
                    if(Drivetrain.getUltrasonic() < 20){
                        if (emergency = true){
                            emergencystop.reset();
                            emergency = false;
                        }

                        if(emergencystop.seconds() > 3){
                            super.stop();
                        }
                    }else {
                        emergencystop.reset();
                    }
                    updateData();
                }

                Drivetrain.STOP();

                sleep(100);

                //Back up slowly to line
                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.setLeftRightPower(0.2, 0.2);
                    sleep(40);
                    Drivetrain.STOP();
                }


                //Turn to goal
                while (opModeIsActive() && Drivetrain.getGyro() > -80){
                    Drivetrain.setLeftRightPower(0.2, -0.2);
                }
                Drivetrain.STOP();

                sleep(100);

                while (opModeIsActive() && Drivetrain.getGyro() < -90){
                    Drivetrain.setLeftRightPower(-0.2, 0.2);
                    sleep(10);
                    Drivetrain.STOP();
                }
                Drivetrain.STOP();


                //Back up if to close to goal
                while (opModeIsActive() && Drivetrain.getUltrasonic() < 20){
                    Drivetrain.setLeftRightPower(0.2, 0.2);
                }

                Drivetrain.STOP();

                //Lower Beacon pressers
                Beacon.BumperSynchronised(false);


                //Drive until really close to beacon
                while (opModeIsActive() && Drivetrain.getUltrasonic() > 18){
                    Drivetrain.setLeftRightPower(-0.1, -0.1);
                }

                Drivetrain.STOP();

                //Detect the color and raise the appropriate presser
                Beacon.HitButton(true);

                sleep(500);

                //Press beacon
                Drivetrain.setLeftRightPower(-0.2, -0.2);
                sleep(500);
                Drivetrain.resetEncoders();

                //Raise Beacon pressers
                Beacon.BumperSynchronised(true);

                //Curve to park on center vortex
                Drivetrain.setLeftRightPower(0.3, 1.0);
                sleep(2000);
                Drivetrain.setLeftRightPower(0.0, 0.0);

                //Raise Beacon pressers
                Beacon.BumperSynchronised(true);

                super.stop();

            }else if (randomnumberthatweneedforsomething == 3){//Park on Ramp

                //Turn to face the ramp
                while (opModeIsActive() && Drivetrain.getGyro() < 0){
                    Drivetrain.setLeftRightPower(-0.1, 0.1);
                    updateData();
                }
                Drivetrain.STOP();

                //Backup on to the ramp and stop
                Drivetrain.setLeftRightPower(0.3, 0.3);
                sleep(1500);
                Drivetrain.STOP();

            }else if (randomnumberthatweneedforsomething == 4){//Cap Ball Park
                Drivetrain.STOP();
                Drivetrain.resetEncoders();

                //Drive Backward
                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(30)){
                    Drivetrain.DriveBackwardstoDistance(0.3, 30);
                }
                Drivetrain.STOP();

                Drivetrain.setLeftRightPower(0.4, -0.4);
                sleep(1000);
                Drivetrain.STOP();
                Drivetrain.resetEncoders();
                sleep(1000);

                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(10)){
                    Drivetrain.setLeftRightPower(-0.4, -0.4);
                }
                Drivetrain.STOP();

            }else {
                telemetry.addData("Invald", " Autonomous Mode");
                telemetry.update();
                sleep(15000);
            }
        }
        super.stop();
    }

    //Method for adding telemetry on the DS screen
    private void updateData(){
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