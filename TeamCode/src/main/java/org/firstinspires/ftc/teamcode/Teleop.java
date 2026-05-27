package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;


import java.util.List;
import java.util.Optional;

@TeleOp
public class Teleop extends LinearOpMode {
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;

    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;

    IMU imu;

    final double AUTO_ALIGN_BEARING_SETPOINT = 0.0;
    final double AUTO_ALIGN_BEARING_ERROR = 4.0;
    boolean isFieldOriented = false;
    private void initApriltag() {
        aprilTag = new AprilTagProcessor.Builder()
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.enableLiveView(true);

        builder.addProcessor(aprilTag);

        visionPortal = builder.build();
    }

    private Optional<String> checkObelisk(List<AprilTagDetection> tagDetections) {
        for (AprilTagDetection tag : tagDetections) {
            switch (tag.id) {
                case 21:
                    return Optional.of("GPP");
                case 22:
                    return Optional.of("PGP");
                case 23:
                    return Optional.of("PPG");
            }
        }

        return Optional.empty();
    }

    private void resetDrive() {
        drive(0, 0, 0);
    }


    private void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        resetDrive();

        for (AprilTagDetection tag : currentDetections) {
            telemetry.addData("Detected ID", tag.id);
            if (tag.id == 20) {
                telemetry.addData("Depot Bearing", tag.ftcPose.bearing);
                autoAlign(tag.ftcPose.bearing);
            }

        }

        Optional<String> obelisk = checkObelisk(currentDetections);
        obelisk.ifPresent(s -> telemetry.addData("Obelisk", s));

        telemetry.addData("# AprilTags Detected", currentDetections.size());
    }

    @Override
    public void runOpMode() {
        initApriltag();

        telemetry.addData("Mode", "Robot");

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRight");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeft");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRight");

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        RevHubOrientationOnRobot orientationOnRobot = new
            RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        waitForStart();

        while(opModeIsActive()) {
            telemetryAprilTag();

            if (gamepad1.aWasPressed()){
                telemetry.addData("Mode", "Field");
                isFieldOriented = true;
            }
            else if (gamepad1.bWasPressed()) {
                telemetry.addData("Mode", "Robot");
                isFieldOriented = false;
            }
//            if (isFieldOriented) {
//                driveFieldOriented(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
//            }
//            else{
//                drive(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
//            }
            telemetry.update();

            if (gamepad1.y){
                imu.resetYaw();
            }
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
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);
        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);
        drive(newForward, newRight, rotate);
    }

    public void autoAlign(double bearing){
        if (bearing > AUTO_ALIGN_BEARING_SETPOINT + AUTO_ALIGN_BEARING_ERROR) {
            drive(0, 0, 0.3);
        } else if (bearing < AUTO_ALIGN_BEARING_SETPOINT - AUTO_ALIGN_BEARING_ERROR) {
            drive(0, 0, -0.3);
        }
    }

//    private double goalPID(double setpoint) {
//
//    }

//    private double getYawToGoal() {
//
//    }
}