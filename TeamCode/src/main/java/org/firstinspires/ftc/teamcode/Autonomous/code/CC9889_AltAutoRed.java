package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Hardware9889;

/**
 * Created by Joshua H on 1/21/2017.
 */

@Autonomous(name="Red", group="Red")
public class CC9889_AltAutoRed extends LinearOpMode{

    //OpMode Members
    Hardware9889 robot          = new Hardware9889();
    boolean breakout = false;

    @Override
    public void runOpMode(){

        int randomnumberthatweneedforsomething = 1;

        robot.init(hardwareMap);

        robot.resetEncoders();

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
        updateData();

        waitForStart();

        //Reset all the things
        robot.resetEncoders();
        robot.resetGyro();
        sleep(100);

        telemetry.addData("Runnig Auton", " ");
        telemetry.update();

        if (randomnumberthatweneedforsomething == 1){//Shoot and Park on Center Auton

            //Wait for partner to hit beacon
            sleep(20000);

            if (opModeIsActive()){
                //Start Flywheel
                robot.Flywheel(true);
            }

            //Drive Straight For 35 inches
            while (opModeIsActive() && robot.getLeftEncoderinInches() > -35 && robot.getRightEncoderinInches() > -35){
                robot.Drivetrain(-0.6, -0.6);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

            robot.resetEncoders();

            //Shoot particles
            if(opModeIsActive()){
                sleep(500);
                robot.IntakeControl(1);
                sleep(2000);
                robot.IntakeControl(0);
                robot.Flywheel(false);
            }

            //Park
            while (opModeIsActive() && robot.getLeftEncoderinInches() > -35 && robot.getRightEncoderinInches() > -35){
                robot.Drivetrain(-0.6, -0.6);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();


        }else {///////////////////////Base One Beacon///////////////////////

            //Drive Straight For 22 inches
            while (opModeIsActive() && robot.getLeftEncoderinInches() > -18 && robot.getRightEncoderinInches() > -18){
                robot.Drivetrain(-0.6, -0.6);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

            robot.Flywheel(true);

            //Turn to the goal
            while (opModeIsActive() && robot.getGyro() > -6){
                robot.Drivetrain(0.2, -0.2);
                updateData();
                robot.waitForTick(25);
            }
            while (opModeIsActive() && robot.getGyro() > -15){
                robot.Drivetrain(0.1, -0.1);
                updateData();
                robot.waitForTick(25);
            }

            robot.STOP();

            //Shoot particles
            if(opModeIsActive()){
                sleep(500);
                robot.IntakeControl(1);
                sleep(2000);
                robot.IntakeControl(0);
                robot.Flywheel(false);
            }

            while (opModeIsActive() && robot.getGyro() < 5)
                robot.Drivetrain(-0.2, 0.2);
                updateData();
                robot.waitForTick(25);
            }
            while (opModeIsActive() && robot.getGyro() < 30){
                robot.Drivetrain(-0.1, 0.1);
                updateData();
                robot.waitForTick(25);
            }

            robot.STOP();

            //Go Straight till line
            while (opModeIsActive() && robot.getBackODS() < 0.5){
                robot.Drivetrain(-0.9, -0.9);
                updateData();
                robot.waitForTick(50);
            }

            robot.STOP();

            while (opModeIsActive() && robot.getBackODS() < 0.5){
                robot.Drivetrain(0.15, 0.15);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

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
            /*while (opModeIsActive() && robot.FrontODS.getRawLightDetected() < 1.0){
                robot.Drivetrain(-0.1, 0.1);
                updateData();
                robot.waitForTick(50);
            }
            while (opModeIsActive() && robot.FrontODS.getRawLightDetected() > 1.0){
                robot.Drivetrain(0.1, -0.1);
                updateData();
                robot.waitForTick(50);
            }*/

            robot.STOP();

            robot.BumperControl(false);

            //Drive to the beacon
            robot.Drivetrain(-0.1, -0.1);
            sleep(600);
            HitButton(false);

            robot.resetEncoders();

            while (opModeIsActive() && robot.getLeftEncoderinInches() < 5 && robot.getRightEncoderinInches() < 5){
                robot.Drivetrain(0.4, 0.4);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

            robot.BumperControl(true);

            ////////////////////////////////////////////////////////
            /////       Auton Picker                        ////////
            ////////////////////////////////////////////////////////

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

                robot.STOP();
                robot.resetEncoders();

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
            }
        super.stop();
    }

    //Follow Line and Press Button
    public void HitButton(boolean color){
        //Here the robot decides which beacon button to press.
        if(color == true){//Go for red
            if (robot.getColor()){
                robot.BumperBeacon(true);
            }else {
                robot.BumperBeacon(false);
            }
        }else {//Go for blue
            if (robot.getColor() == false){
                robot.BumperBeacon(true);
            }else {
                robot.BumperBeacon(false);
            }
        }
        sleep(700);

        robot.STOP();
    }

    public void updateData(){
        telemetry.addData("Right Speed", robot.getRightPower());
        telemetry.addData("Left Speed", robot.getRightPower());
        telemetry.addData("Right Encoder", robot.getRightEncoder());
        telemetry.addData("Left Encoder", robot.getLeftEncoder());
        telemetry.addData("Right Encoder in Inches", robot.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", robot.getLeftEncoderinInches());
        telemetry.addData("Ultrasonic Sensor", robot.getUltrasonic());
        telemetry.addData("Gyro Z-axis", robot.getGyro());
        telemetry.addData("Back ODS", robot.getBackODS());
        telemetry.addData("Front ODS", robot.getFrontODS());
        telemetry.update();
    }
}