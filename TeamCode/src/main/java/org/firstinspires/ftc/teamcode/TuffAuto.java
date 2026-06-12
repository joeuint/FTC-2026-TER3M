package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class TuffAuto extends LinearOpMode {
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;

    /**
     * Main OpMode loop
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        // setup all the motors
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRight");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeft");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRight");
        // inversions
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        // wait for start
        waitForStart();

        // Go full power
        frontLeftDrive.setPower(1.0);
        frontRightDrive.setPower(1.0);
        backLeftDrive.setPower(1.0);
        backRightDrive.setPower(1.0);

        // Sleep
        Thread.sleep(2000);

        // Stop
        frontLeftDrive.setPower(0.0);
        frontRightDrive.setPower(0.0);
        backLeftDrive.setPower(0.0);
        backRightDrive.setPower(0.0);

        // Spin lock
        while (opModeIsActive()) {
        }
    }
}
