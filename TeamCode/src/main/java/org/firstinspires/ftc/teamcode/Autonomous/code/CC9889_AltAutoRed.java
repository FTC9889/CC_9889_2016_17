package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Subsystems.*;

/**
 * Created by Joshua H on 1/21/2017.
 */

@Autonomous(name="Red", group="Red")

public class CC9889_AltAutoRed extends LinearOpMode{

    Flywheel_Intake Flywheel_Intake   = new Flywheel_Intake();
    Drivebase Drivetrain              = new Drivebase();
    Beacon Beacon                     = new Beacon();
    waitForTick waitForTick           = new waitForTick();
    boolean breakout = false;

    @Override
    public void runOpMode(){

        int randomnumberthatweneedforsomething = 1;

        //Init the robot
        Flywheel_Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap);
        Beacon.init(hardwareMap);
        waitForTick.init();

        Drivetrain.resetEncoders();


        /*while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }
        */


        while (breakout == false) {
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
            }else if (gamepad1.a){
                breakout = true;
            }
            telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            telemetry.update();
        }
        //Add telemetry
        telemetry.clearAll();
        telemetry.addData("Auton", " Selected");

        waitForStart();

        telemetry.clearAll();
        telemetry.addData("Runnig Auton", " ");
        telemetry.update();

        //Reset all the things
        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();
        sleep(100);

        if (randomnumberthatweneedforsomething == 1){//Shoot and Park on Center Auton

            //Wait for partner to hit beacon
            sleep(20000);

            if (opModeIsActive()){
                //Start Flywheel
                Flywheel_Intake.setFlywheel(true);
            }

            //Drive Straight For 35 inches
            Drivetrain.DriveForwardtoDistance(0.6, 35);

            //Shoot particles
            if(opModeIsActive()){
                sleep(500);
                Flywheel_Intake.setIntake(1);
                sleep(2000);
                Flywheel_Intake.setIntake(0);
                Flywheel_Intake.setFlywheel(false);
            }

            //Park
            Drivetrain.DriveForwardtoDistance(0.6, 35);


        }else {///////////////////////Base One Beacon///////////////////////

            //Drive Straight For 22 inches
            Drivetrain.DriveForwardtoDistance(0.6, 22);

            Flywheel_Intake.setFlywheel(true);

            //Turn to the goal
            /*while (opModeIsActive() && robot.getGyro() > -6){
                robot.Drivetrain(0.2, -0.2);
                updateData();
                robot.waitForTick(25);
            }
            while (opModeIsActive() && robot.getGyro() > -15){
                robot.Drivetrain(0.1, -0.1);
                updateData();
                robot.waitForTick(25);
            }*/

            Drivetrain.STOP();

            //Shoot particles
            if(opModeIsActive()){
                sleep(500);
                Flywheel_Intake.setIntake(1);
                sleep(2000);
                Flywheel_Intake.setIntake(0);
                Flywheel_Intake.setFlywheel(false);
            }

            /*while (opModeIsActive() && robot.getGyro() < 5)
                robot.Drivetrain(-0.2, 0.2);
                updateData();
                robot.waitForTick(25);
            }
            while (opModeIsActive() && robot.getGyro() < 30){
                robot.Drivetrain(-0.1, 0.1);
                updateData();
                robot.waitForTick(25);
            }*/

            Drivetrain.STOP();

            //Go Straight till line
            Drivetrain.DriveStraighttoWhiteLine(0.7, 2500);

            Drivetrain.CenterOnWhiteLine(0.1, true);

            /*
            while (opModeIsActive() && robot.getFrontODS() < 1.0){
                robot.Drivetrain(-0.1, 0.1);
                updateData();
                robot.waitForTick(50);
            }
            while (opModeIsActive() && robot.getFrontODS() > 1.0){
                robot.Drivetrain(0.1, -0.1);
                updateData();
                robot.waitForTick(50);
            }
            */

            Drivetrain.STOP();

            Beacon.BumperSynchronised(false);

            //Drive to the beacon
            Drivetrain.DriveforTime(-0.1, -0.1, 600);

            Beacon.HitButton(false);

            sleep(700);

            Drivetrain.resetEncoders();

            while (opModeIsActive() && Drivetrain.AreWeThereYet(5)){
                Drivetrain.DriveBackwardstoDistance(0.4, 5);
                waitForTick.function(50);
            }

            Drivetrain.STOP();

            Beacon.BumperSynchronised(true);

            ////////////////////////////////////////////////////////
            /////       Auton Picker                        ////////
            ////////////////////////////////////////////////////////
/*
            if (randomnumberthatweneedforsomething == 2){
                //2 BEACON AUTONOMOUS
                //Turn to the Beacon
                while (opModeIsActive() && robot.getGyro() < 70){
                    robot.Drivetrain(-0.2, 0.2);
                    updateData();
                    robot.waitForTick(25);
                }
                while (opModeIsActive() && robot.getGyro() < 6){
                    robot.Drivetrain(-0.1, 0.1);
                    updateData();
                    robot.waitForTick(25);
                }
                robot.STOP();


                robot.Drivetrain(-1.0, -1.0);
                sleep(1000);

                while (opModeIsActive() && robot.getBackODS() < 1.0){
                    if(robot.getGyro() < -1){
                        robot.Drivetrain(-0.3, -0.5);
                    }else if(robot.getGyro() > 1){
                        robot.Drivetrain(-0.5, -0.3);
                    }else if (robot.getGyro() == 0){
                        robot.Drivetrain(-0.7, -0.7);
                    }
                    updateData();
                    robot.waitForTick(50);
                }

                robot.STOP();

                while (opModeIsActive() && robot.getBackODS() < 1.0){
                    robot.Drivetrain(0.1, 0.1);
                    updateData();
                    robot.waitForTick(50);
                }
                robot.STOP();

                while (opModeIsActive() && robot.getFrontODS() < 0.6){
                    robot.Drivetrain(-0.2, 0.2);
                    updateData();
                    robot.waitForTick(50);
                }

                robot.STOP();

                while (opModeIsActive() && robot.getFrontODS() < 0.6){
                    robot.Drivetrain(0.1, -0.1);
                    updateData();
                    robot.waitForTick(50);
                }

                robot.STOP();

                robot.BumperControl(false);

                //Drive to the beacon
                robot.Drivetrain(-0.2, -0.2);
                sleep(600);
                HitButton(true);

                robot.resetEncoders();

            }else if (randomnumberthatweneedforsomething == 3){//Park on Ramp
                //Turn the ramp
                while (opModeIsActive() && robot.getGyro() > 80){
                    robot.Drivetrain(0.2, -0.2);
                    updateData();
                    robot.waitForTick(25);
                }
                while (opModeIsActive() && robot.getGyro() > 10){
                    robot.Drivetrain(0.1, -0.1);
                    updateData();
                    robot.waitForTick(25);
                }
                robot.STOP();

                robot.Drivetrain(0.5, 0.5);
                sleep(1000);

                robot.STOP();

            }else if (randomnumberthatweneedforsomething == 4){//Cap Ball Park

                robot.Drivetrain.STOP();
                robot.Drivetrain.resetEncoders();

                //Drive Backward
                while (opModeIsActive() && robot.getLeftEncoderinInches() < 30 && robot.getRightEncoderinInches() < 30){
                    robot.Drivetrain(0.4, 0.4);
                    updateData();
                    robot.waitForTick(50);
                }
                robot.STOP();

                robot.Drivetrain(0.4, -0.4);
                sleep(1000);
                robot.STOP();
                sleep(1000);

                while (opModeIsActive() && robot.getLeftEncoderinInches() > -10 && robot.getRightEncoderinInches() > -10){
                    robot.Drivetrain(-0.4, -0.4);
                    updateData();
                    robot.waitForTick(50);
                }
                robot.STOP();


            }else {
                telemetry.addData("Invald", " Autonomous Mode");
                telemetry.update();
                sleep(15000);
            }*/
        super.stop();
        }
    }
}