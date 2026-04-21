package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class ChudOpMode extends LinearOpMode {
    private DcMotorEx motorTest1;
    private DcMotorEx motorTest2;
    private DcMotorEx motorTest3;
    private DcMotorEx motorTest4;

    @Override
    public void runOpMode() {
        motorTest1 = (DcMotorEx)hardwareMap.get(DcMotor.class, "motorTest1");
        motorTest2 = (DcMotorEx)hardwareMap.get(DcMotor.class, "motorTest2");
        motorTest3 = (DcMotorEx)hardwareMap.get(DcMotor.class, "motorTest3");
        motorTest4 = (DcMotorEx)hardwareMap.get(DcMotor.class, "motorTest4");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            motorTest1.setPower(1.0);
            motorTest2.setPower(1.0);
            motorTest3.setPower(1.0);
            motorTest4.setPower(1.0);
            telemetry.update();
        }

        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}
