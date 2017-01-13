package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
@Autonomous(name="Auton", group="Blue")
@Disabled
public class CC9889_AltAutoBlue extends LinearOpMode {

    AutoHardware9889 robot           = new AutoHardware9889();              // Use a K9'shardware

    @Override
    public void runOpMode () {

        robot.init(hardwareMap);

        waitForStart();

        sleep(5000);

        robot.Drivetrain(-1.0, -1.0);
        sleep(860);

        robot.STOP();

        robot.flyWheel.setPower(-0.8);
        sleep(2000);
        robot.Intake.setPower(0.5);
        robot.IntakeServo.setPower(-1.0);
        sleep(2000);
        robot.IntakeServo.setPower(0.2);
        sleep(5000);
        robot.flyWheel.setPower(0.0);
        robot.IntakeServo.setPower(0.0);
        robot.Intake.setPower(0.0);
        robot.Drivetrain(-1.0,-1.0);
        sleep(2000);
        super.stop();

    }
}
