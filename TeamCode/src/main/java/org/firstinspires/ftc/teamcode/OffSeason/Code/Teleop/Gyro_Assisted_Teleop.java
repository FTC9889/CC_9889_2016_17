package org.firstinspires.ftc.teamcode.OffSeason.Code.Teleop;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OffSeason.Code.OpMode.Team9889OpMode;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.*;

/**
 * Created by Joshua H on 3/20/2017.
 */

public class Gyro_Assisted_Teleop extends Team9889OpMode{
    private org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Beacon Beacon = new Beacon();
    private Drivebase Drivetrain = new Drivebase();
    private Flywheel Flywheel_Intake = new Flywheel();
    private org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.waitForTick waitForTick = new waitForTick();
    private ElapsedTime beacontimer = new ElapsedTime();
    private boolean deploy = false;

    private ElapsedTime shot = new ElapsedTime();
    boolean SmartShot = false;

    @Override
    public void init() {
        Drivetrain.init(hardwareMap, false);
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);
    }
    @Override
    public void loop() {

    }
}
