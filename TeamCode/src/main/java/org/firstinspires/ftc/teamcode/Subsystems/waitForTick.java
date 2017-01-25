package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Joshua H on 1/24/2017.
 */

public class waitForTick {

    HardwareMap waitForTick = null;
    private ElapsedTime period  = new ElapsedTime();

    public waitForTick(){

    }

    public void init(){

    }

    //Built-in function by FIRST. Put in all loops
    public void function(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
