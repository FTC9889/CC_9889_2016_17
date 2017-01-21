package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.qualcomm.robotcore.util.Range.clip;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
@Autonomous(name="AutoRed", group="Red")
@Disabled
public class CC9889_Autonomo_Red extends LinearOpMode {

    /* Declare OpMode members. */
    AutoHardware9889 robot           = new AutoHardware9889();

    int randomnumberthatweneedforsomething = 0;
    boolean breakout = false;
    @Override
    public void runOpMode () {
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        robot.resetEncoders();
        while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }


        while (breakout == false) {
            if (gamepad1.dpad_up) {
                randomnumberthatweneedforsomething++;
                sleep(1000);
            } else if (gamepad1.dpad_right) {
                ++randomnumberthatweneedforsomething;
                sleep(1000);
            }else if(gamepad1.a) {
                breakout = true;
            }

            switch(randomnumberthatweneedforsomething){
                case 1:
                    telemetry.clear();
                    telemetry.addData("Autonomous 1", "= Shoots and parks center");
                    break;

                case 2:
                    telemetry.clear();
                    telemetry.addData("Autonomous 2", "= 1 Beacon and Park on Ramp");
                    break;

                case 3:
                    telemetry.clear();
                    telemetry.addData("Autonomous 3","= Hits two beacons and stops");
                    break;

                case 4:
                    telemetry.clear();
                    telemetry.addData("Autonomous 4", "= 1 Beacon and Hit Cap Ball");
                    break;
            }
            telemetry.addData(">", "Gyro Calibrated. ¯\\_(ツ)_/¯");
            telemetry.update();
        }

        telemetry.addData("Now running Autonomous #", randomnumberthatweneedforsomething);
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            updateData();
        }

        robot.STOP();

        if (randomnumberthatweneedforsomething == 1){
            sleep(15000);
            EncoderDrive(0.7,-30,-30);
            robot.Flywheel(true);
            robot.resetEncoders();
            sleep(1200);
            robot.IntakeServo.setPower(-1.0);
            robot.Intake.setPower(1.0);
            sleep(2100);
            robot.IntakeServo.setPower(0.0);
            robot.Intake.setPower(0.0);
            robot.Flywheel(false);
            //EncoderDrive(0.7,-20,-20);
            robot.Drivetrain(-0.5, 0.5);
            sleep(2000);
            robot.STOP();

        }else {
            //Robot drives forward 20 inches
            EncoderDrive(1.0, -20, -20);

            updateData();

            while (opModeIsActive() && robot.gyro.getIntegratedZValue() > -10) {
                robot.Drivetrain(0.3, 0.3);
                robot.waitForTick(50);
            }

            robot.STOP();

            updateData();

            robot.Flywheel(true);
            sleep(1200);
            robot.IntakeServo.setPower(-1.0);
            robot.Intake.setPower(1.0);
            sleep(2100);
            robot.IntakeServo.setPower(0.0);
            robot.Intake.setPower(0.0);
            robot.Flywheel(false);


            while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 20) {
                robot.Drivetrain(-0.2, -0.2);
                sleep(4);
                robot.waitForTick(50);
            }

            robot.STOP();

            robot.Drivetrain(0.7, -0.7);
            sleep(1500);

            while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 1.5){
                sleep(4);
                robot.waitForTick(50);
            }

            robot.STOP();


            robot.Drivetrain(-0.1, 0.0);

            while (opModeIsActive() && robot.gyro.getIntegratedZValue() < 90) {
                sleep(4);
                robot.waitForTick(50);
            }

            robot.STOP();

            HitButton(false);

            robot.STOP();

            switch (randomnumberthatweneedforsomething) {
                case 2:
                    //1 Beacon and Park on Ramp
                    robot.Drivetrain(0.1, 0.1);

                    while (opModeIsActive() && robot.gyro.getIntegratedZValue() > 0) {
                        sleep(4);
                        robot.waitForTick(50);
                    }
                    robot.STOP();

                    robot.Drivetrain(-0.8, 0.8);
                    sleep(1000);
                    robot.STOP();

                    break;

                case 3:
                    //2 Beacon and Stop
                    robot.Drivetrain(0.1, 0.1);

                    while (opModeIsActive() && robot.gyro.getIntegratedZValue() > 0) {
                        sleep(4);
                        robot.waitForTick(50);
                    }
                    robot.STOP();

                    robot.Drivetrain(0.7, -0.7);
                    sleep(1600);
                    robot.STOP();

                    while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 1.5) {
                        sleep(4);
                        robot.waitForTick(50);
                    }

                    robot.STOP();

                    robot.Drivetrain(-0.4, -0.4);

                    while (opModeIsActive() && robot.FrontODS.getRawLightDetected() < 1.5) {
                        sleep(4);
                        robot.waitForTick(50);
                    }
                    robot.STOP();

                    HitButton(true);

                    break;

                case 4:
                    //1 Beacon and Hit Cap Ball
                    robot.Drivetrain(-0.8, 0.8);

                    sleep(1500);

                    robot.STOP();

                    robot.Drivetrain(0.3, -0.3);

                    sleep(1250);

                    robot.STOP();

                    robot.Drivetrain(-0.3, 0.3);
                    sleep(3000);
                    robot.STOP();

                    break;
            }
        }

        super.stop();
    }

    //Go to white line

    public void FindWhiteTape(double speed, boolean color){
        robot.Drivetrain(Math.abs(speed), -Math.abs(speed));
        sleep(1500);

        while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 1.8){
            sleep(4);
            robot.waitForTick(50);
        }

        robot.STOP();

        //The robot lines itself up with the white line.
        robot.Drivetrain(0.0, 0.4);
        while (opModeIsActive() && robot.FrontODS.getRawLightDetected() < 1.5){
            sleep(4);
            robot.waitForTick(50);
        }

        robot.STOP();

    }

    //Follow Line and Press Button

    public void HitButton(boolean color){
        //Here the robot decides which beacon button to press.
        robot.BumperControl(false);
        robot.Drivetrain(0.3,-0.3);
        sleep(1000);
        if(color == true){
            if (robot.Color.red() > robot.Color.blue()){
                robot.RightBumper.setPosition(0.1);
            }else {
                robot.LeftBumper.setPosition(0.9);
            }
        }else {
            if (robot.Color.red() < robot.Color.blue()){
                robot.RightBumper.setPosition(0.1);
            }else {
                robot.LeftBumper.setPosition(0.9);
            }
        }
        sleep(700);

        robot.STOP();

        robot.Drivetrain(-0.3, 0.3);
        sleep(1000);
        robot.STOP();

        robot.BumperControl(true);


    }

    public void updateData() {
        telemetry.addData("Right Speed", robot.RDrive1.getPower());
        telemetry.addData("Left Speed", robot.LDrive1.getPower());
        telemetry.addData("Right Encoder", robot.getRightEncoder());
        telemetry.addData("Left Encoder", robot.getLeftEncoder());
        telemetry.addData("Right Encoder pos", robot.RDrive1.getCurrentPosition());
        telemetry.addData("Left Encoder pos", robot.LDrive1.getCurrentPosition());
        telemetry.addData("Right Encoder in Inches", robot.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", robot.getLeftEncoderinInches());
        telemetry.addData("Gyro Z-axis", robot.gyro.getIntegratedZValue());
        telemetry.addData("Back ODS", robot.BackODS.getRawLightDetected());
        telemetry.addData("Front ODS", robot.FrontODS.getRawLightDetected());

        telemetry.update();
    }

    public void EncoderDrive(double speed, int left, int right) {

        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {

            newLeftTarget = robot.LDrive2.getCurrentPosition() + (int)(-left * robot.CountsPerInch);
            newRightTarget = robot.RDrive2.getCurrentPosition() + (int)(right * robot.CountsPerInch);

            if (newLeftTarget < 0 && newRightTarget < 0) {
                robot.Drivetrain(-Math.abs(speed),-Math.abs(speed));
                while (opModeIsActive() && newLeftTarget < robot.LDrive2.getCurrentPosition() && newRightTarget < robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }else if (newLeftTarget > 0 && newRightTarget <0) {
                robot.Drivetrain(Math.abs(speed),-Math.abs(speed));
                while (opModeIsActive() && newLeftTarget > robot.LDrive2.getCurrentPosition() && newRightTarget < robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }else if (newLeftTarget <0 && newRightTarget > 0) {
                robot.Drivetrain(-Math.abs(speed),Math.abs(speed));
                while (opModeIsActive() && newLeftTarget < robot.LDrive2.getCurrentPosition() && newRightTarget > robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }else if (newLeftTarget > 0 && newRightTarget > 0) {
                robot.Drivetrain(Math.abs(speed),Math.abs(speed));
                while (opModeIsActive() && newLeftTarget > robot.LDrive2.getCurrentPosition() && newRightTarget > robot.RDrive2.getCurrentPosition()) {
                    updateData();
                    sleep(4);
                    robot.waitForTick(50);
                }
            }
            robot.STOP();
        }

    }
}