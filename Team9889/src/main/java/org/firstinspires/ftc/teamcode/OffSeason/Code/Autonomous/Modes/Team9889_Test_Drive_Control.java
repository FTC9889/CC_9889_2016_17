package org.firstinspires.ftc.teamcode.OffSeason.Code.Autonomous.Modes;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OffSeason.Code.OpMode.Team9889OpMode;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Beacon;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Drivebase;
import org.firstinspires.ftc.teamcode.OffSeason.Code.Subsystems.Working.Flywheel;

/**
 * Created by Joshua H on 3/14/2017.
 */

public class Team9889_Test_Drive_Control extends Team9889OpMode{
    Beacon Beacon = new Beacon();
    Drivebase Drivetrain = new Drivebase();
    Flywheel Flywheel_Intake = new Flywheel();

    private ElapsedTime runtime = new ElapsedTime();

    private int state = 1;

    private int kP = 1;
    private int kI = 1;
    private int kD = 1;

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
        switch (state){
            case 1:
                if(Drivetrain.Error(5.0, Drivetrain.getRightEncoder()) < 1/10){
                    requestOpModeStop();
                    break;
                }else {
                    //Proportional Control
                    double Power = Drivetrain.Error(5.0, (Drivetrain.getRightEncoderinInches()+Drivetrain.getLeftEncoderinInches())/2)*(100/5.0);
                    Drivetrain.setLeftRightPower(Power, Power);
                }
        }
    }

    @Override
    public void stop(){
        Drivetrain.brake();
    }
}
