package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class ChudOpMode extends LinearOpMode {
    private DcMotorEx motorTest;

    @Override
    public void runOpMode() {
        motorTest = (DcMotorEx)hardwareMap.get(DcMotor.class, "motorTest");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            motorTest.setPower(1.0);
            telemetry.update();
        }

        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}
