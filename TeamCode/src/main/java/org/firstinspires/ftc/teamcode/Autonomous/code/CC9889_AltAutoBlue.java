package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.*;

/**
 * Created by Jin on 9/30/2016. #ObieDidHarambe
 */

@Autonomous(name="Blue", group="Blue")
public class CC9889_AltAutoBlue extends LinearOpMode {

    private Flywheel Flywheel_Intake          = new Flywheel();
    private Drivebase Drivetrain              = new Drivebase();
    private Beacon Beacon                     = new Beacon();
    private waitForTick waitForTick           = new waitForTick();

    private ElapsedTime emergencystop         = new ElapsedTime();

    private boolean emergency = true;
    private boolean breakout = false;

    private int randomnumberthatweneedforsomething = 1;

    @Override
    public void runOpMode() throws InterruptedException{
        //Init Hardwawre
        telemetry.addData("Beacon", "");
        telemetry.update();
        Beacon.init(hardwareMap);
        telemetry.addData("Flywheel", "");
        telemetry.update();
        Flywheel_Intake.init(hardwareMap);
        telemetry.addData("Drivetrain", "");
        telemetry.update();
        Drivetrain.init(hardwareMap);

        Drivetrain.CalibrateGyro();

        //Program Chooser
        while (!gamepad1.a) {
            if (gamepad1.dpad_up) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 1;
                telemetry.addData("Autonomous 1", "= Shoot and Park on Center");
            } else if(gamepad1.dpad_right){
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 2;
                telemetry.addData("Autonomous 2", "= 2 Beacon");
            }else if (gamepad1.dpad_down) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 3;
                telemetry.addData("Autonomous 3","= 1  Beacon and Ramp");
            }else if(gamepad1.dpad_left) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 4;
                telemetry.addData("Autonomous 4", "= 1 Beacon and Hit Cap Ball");
            }
            telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
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

        telemetry.clearAll();
        telemetry.addData("Running Auton", " ");
        telemetry.update();

        //Reset all the things
        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();

        if (randomnumberthatweneedforsomething == 1){//Shoot and Park on Center Auton

            ////////////////////////////////////////////////////////////////////
            ///                                                              ///
            ///     Brother, this is all one program                         ///
            ///     One program can do a lot                                 ///
            ///     This program holds several tasks that the robot can do   ///
            ///                                                              ///
            ////////////////////////////////////////////////////////////////////

            //Wait for partner to hit beacon
            sleep(20000);

            if (opModeIsActive()){
                //Start Flywheel
                Flywheel_Intake.setFlywheel(true);
            }

            //Drive Straight For 35 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(35)){
                Drivetrain.setLeftRightPower(-0.6, -0.6);
            }

            Drivetrain.STOP();

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
            ////////////////////////////////////////////////////////////////////
            ///                                                              ///
            ///                                                              ///
            ///                                                              ///
            ///                                                              ///
            ///                                                              ///
            ////////////////////////////////////////////////////////////////////

            //Drive Straight For 15 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(15)){
                Drivetrain.setLeftRightPower(-0.3, -0.3);
                updateData();
            }

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

            while (opModeIsActive() && Drivetrain.getGyro() > -45){
                Drivetrain.setLeftRightPower(0.5, -0.5);
                sleep(10);
                Drivetrain.STOP();
                updateData();
            }
            Drivetrain.STOP();

            //Go Straight till line
            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.DriveStraighttoWhiteLine(0.3, true);
                if(Drivetrain.getUltrasonic() < 10){
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

            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.setLeftRightPower(0.2, 0.2);
                sleep(40);
                Drivetrain.STOP();
            }



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

            Beacon.BumperSynchronised(false);

            //Drive to the beacon
            while (opModeIsActive() && Drivetrain.getUltrasonic() > 20){
                Drivetrain.setLeftRightPower(-0.3, -0.3);
            }

            Beacon.HitButton(true);

            sleep(700);

            Drivetrain.resetEncoders();

            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(5)){
                Drivetrain.setLeftRightPower(0.2, 0.2);
            }

            Drivetrain.STOP();

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

                //Go Straight till line
                Drivetrain.setLeftRightPower(-0.5, -0.5);
                sleep(1000);

                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.DriveStraighttoWhiteLine(0.3, true);
                    if(Drivetrain.getUltrasonic() < 10){
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

                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.setLeftRightPower(0.2, 0.2);
                    sleep(40);
                    Drivetrain.STOP();
                }

                while (opModeIsActive() && Drivetrain.getGyro() > -75){
                    Drivetrain.setLeftRightPower(0.2, -0.2);
                }
                Drivetrain.STOP();

                while (opModeIsActive() && Drivetrain.getGyro() < -90){
                    Drivetrain.setLeftRightPower(-0.1, 0.1);
                    sleep(10);
                    Drivetrain.STOP();
                }
                Drivetrain.STOP();

                Beacon.BumperSynchronised(false);

                //Drive to the beacon
                while (opModeIsActive() && Drivetrain.getUltrasonic() > 20){
                    Drivetrain.setLeftRightPower(-0.1, -0.1);
                }

                Beacon.HitButton(true);

                sleep(700);

                Drivetrain.resetEncoders();

                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(5)){
                    Drivetrain.setLeftRightPower(0.2, 0.2);
                }

                Drivetrain.STOP();

                Beacon.BumperSynchronised(true);


                super.stop();

            }else if (randomnumberthatweneedforsomething == 3){//Park on Ramp
                while (opModeIsActive() && Drivetrain.getGyro() < 0){
                    Drivetrain.setLeftRightPower(-0.1, 0.1);
                    updateData();
                }
                Drivetrain.STOP();

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