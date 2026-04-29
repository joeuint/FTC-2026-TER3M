package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp
public class Teleop extends LinearOpMode {
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;

    IMU imu;

    boolean isFieldOriented = false;

@Override
    public void runOpMode() {
        telemetry.addData("Mode", "Robot");

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRight");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeft");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRight");

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        // imu config


        waitForStart();
        while(opModeIsActive()) {
            if (gamepad1.aWasPressed()){
                telemetry.addData("Mode", "Field");
                isFieldOriented = true;
            }
            else if (gamepad1.bWasPressed()) {
                telemetry.addData("Mode", "Robot");
                isFieldOriented = false;
            }
            if (isFieldOriented) {
                driveFieldOriented(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            }
            else{
                drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            }
            telemetry.update();

        }

    }
    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);
    }

    public void driveFieldOriented(double forward, double right, double rotate){

    }

}