package org.firstinspires.ftc.teamcode.OffSeason.Code.Teleop;

import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OffSeason.Code.OpMode.Team9889OpMode;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Beacon;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Drivebase;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Flywheel;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.waitForTick;

/**
 * Created by Joshua H on 3/25/2017.
 */

@TeleOp(name = "TeleopSimple")
public class Teleop_Simple extends Team9889OpMode {
    private org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Beacon Beacon = new Beacon();
    private Drivebase Drivetrain = new Drivebase();
    private Flywheel Flywheel_Intake = new Flywheel();
    private org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.waitForTick waitForTick = new waitForTick();
    private ElapsedTime beacontimer = new ElapsedTime();
    private boolean deploy = false;

    private ElapsedTime shot = new ElapsedTime();
    boolean SmartShot = false;

    private ElapsedTime stop = new ElapsedTime();


    @Override
    public void init() {
        Drivetrain.init(hardwareMap, false);
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);

        stop.reset();
    }

    @Override
    public void loop() {
        updateData(this);
        gamepadsDrivetrain(Drivetrain, -gamepad1.right_stick_x, gamepad1.left_stick_y, true);

        if(stop.seconds() > 120){
            requestOpModeStop();
        }

        //Smart Shot
        if (gamepad1.right_trigger > 0.1) {

            //Prevent Particles from getting stuck in between bumpers
            Beacon.BumperSynchronised(true);

            if (SmartShot) {
                shot.reset();
                SmartShot = false;
            }

            if (shot.milliseconds() > 1000) {
                Flywheel_Intake.AutoShoot(true, true);
                if (shot.milliseconds() > 2000) {
                    SmartShot = true;
                }
            } else {
                Flywheel_Intake.AutoShoot(true, false);
            }

        } else {
            SmartShot = true;

            //Flywheel
            Flywheel_Intake.setFlywheel(gamepad2.a);

            //Intake ctrl
            if (Math.abs(gamepad2.right_trigger) > 0.01) {
                Flywheel_Intake.setIntakeMode(2);
            } else if (gamepad2.left_bumper) {
                Flywheel_Intake.setIntakeMode(3);
            } else {
                Flywheel_Intake.setIntakeMode(4);
            }

            //Beacon pressing
            if (Drivetrain.getUltrasonic() < 35) {
                if (beacontimer.milliseconds() > 20) {
                    deploy = true;
                }
            } else {
                deploy = false;
                beacontimer.reset();
            }
            Beacon.BumperSynchronised(true);
        }
    }

    @Override
    public void stop(){
        Drivetrain.STOP();
    }
}
