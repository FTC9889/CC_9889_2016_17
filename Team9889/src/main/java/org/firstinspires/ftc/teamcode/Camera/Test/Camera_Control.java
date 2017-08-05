package org.firstinspires.ftc.teamcode.Camera.Test;

import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Joshua H on 3/21/2017.
 */

public abstract class Camera_Control extends OpMode {
    public void cameraSettings(){
        Camera.open(1);

    }

}
