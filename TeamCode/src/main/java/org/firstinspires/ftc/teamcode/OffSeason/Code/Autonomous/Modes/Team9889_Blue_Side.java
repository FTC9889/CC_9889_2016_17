package org.firstinspires.ftc.teamcode.OffSeason.Code.Autonomous.Modes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OffSeason.Code.OpMode.Team9889OpMode;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Beacon;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Drivebase;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Flywheel;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.waitForTick;

import static org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Drivebase.DriveWithGyro.StraightWithGyro;
import static org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Drivebase.DriveWithGyro.ToWhiteLine;

/**
 * Created by joshua on 3/8/17.
 */
@Autonomous(name = "Offseason Blue", group = "Auton")

public class Team9889_Blue_Side extends Team9889OpMode {
    org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Beacon Beacon = new Beacon();
    Drivebase Drivetrain = new Drivebase();
    Flywheel Flywheel_Intake = new Flywheel();
    org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.waitForTick waitForTick = new waitForTick();
    private ElapsedTime runtime = new ElapsedTime();

    private int state = 1;

    @Override
    public void init(){
        Drivetrain.init(hardwareMap, true);
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);
    }

    @Override
    public void init_loop(){
        if(!Drivetrain.gyro.isCalibrating()){
            telemetry.addData("Ready to Run", "");
            updateData(this);
        }else {
            telemetry.addData("Calibrating Gyro, Please Wait", "");
            updateData(this);
        }
    }

    @Override
    public void start(){
        Drivetrain.resetGyro();
        runtime.reset();
    }

    @Override
    public void loop(){
        //Start
        switch (Drivetrain.AutonState){
            case 0:
                requestOpModeStop();
                break;
            case 1:
                Drivetrain.DriveWithGyro(ToWhiteLine, 0, 0.4, Drivetrain);
                break;
            case 2:
                Drivetrain.DriveWithGyro(StraightWithGyro, 0, 0, Drivetrain);
                break;
            case 3:
                Drivetrain.setLeftRightPower(0.15, -0.15);
                if (Drivetrain.getFrontODS_Detect_White_Line()){
                    Drivetrain.brake();
                    runtime.reset();
                    Drivetrain.AutonState = 4;
                }
                break;
            case 4:
                Drivetrain.Drive(Drivebase.Drive.StraightWithPower, 0.0, -0.1, Drivebase.DrivetrainSide.Left, 700, runtime.milliseconds(), true);
                break;
            case 5:
                Drivetrain.setLeftRightPower(-0.1, -0.1);
                Beacon.BumperSynchronised(false);
                if(Drivetrain.getUltrasonic() < 18){
                    Drivetrain.AutonState = 6;
                    runtime.reset();
                }
                break;
            case 6:
                if(runtime.milliseconds() > 1000){
                    Drivetrain.AutonState = 0;
                }else if(runtime.milliseconds() > 100){
                    Beacon.HitButton(false);
                }
                break;
        }
        telemetry.addData("Step", " " + Drivetrain.AutonState);
        updateData(this);
    }

    @Override
    public void stop(){
        Drivetrain.brake();
    }
}
