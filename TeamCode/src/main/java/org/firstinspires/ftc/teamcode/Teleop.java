package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    // April Tag
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;

    // Robot Components
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    DcMotor intake;
    DcMotor shooter;
    IMU imu;

    // Auto Align
    final double AUTO_ALIGN_BEARING_SETPOINT = 0.0;
    final double AUTO_ALIGN_BEARING_ERROR = 4.0;
    final double AUTO_ALIGN_ROTATION_SPEED = 0.3;
    private double autoAlignBearing = 0.0;

    // Drive
    boolean isFieldOriented = false;

    boolean autoAlignEnabled = false;

    /**
     * AprilTag Initialization & Configuration
     *
     * <p>
     * Does basic configuration and initialization of AprilTagProcessor and VisionPortal
     * </p>
     */
    private void initApriltag() {
        aprilTag = new AprilTagProcessor.Builder()
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.enableLiveView(true);

        builder.addProcessor(aprilTag);

        visionPortal = builder.build();
    }

    /**
     * Returns a string corresponding to the detected obelisk AprilTag
     *
     * <p>
     * Filters through a list of all detections and filters out specific obelisk AprilTag ids
     * </p>
     * @param tagDetections A list of AprilTags to filter through
     * @return obeliskValue The string value of the obelisk
     */
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

    /**
     * A helper method that stops the drivetrain
     */
    private void resetDrive() {
        drive(0, 0, 0);
    }

    /**
     * Detects april tags and passes it off to autoalign and obelisk systems
     *
     * <p>
     * Meant to be run in the main opMode loop. Main method for handling AprilTag detections
     * </p>
     */
    private void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        resetDrive();

        for (AprilTagDetection tag : currentDetections) {
            telemetry.addData("Detected ID", tag.id);
            if (tag.id == 20) {
                telemetry.addData("Depot Bearing", tag.ftcPose.bearing);
                autoAlignBearing = tag.ftcPose.bearing;
            }

        }

        Optional<String> obelisk = checkObelisk(currentDetections);
        obelisk.ifPresent(s -> telemetry.addData("Obelisk", s));

        telemetry.addData("# AprilTags Detected", currentDetections.size());
    }

    /**
     * Main OpMode loop
     */
    @Override
    public void runOpMode() {
        initApriltag();

        telemetry.addData("Mode", "Robot");

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRight");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeft");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRight");
        intake = hardwareMap.get(DcMotor.class, "intake");
        shooter = hardwareMap.get(DcMotor.class, "shooter");

        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

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

        while (opModeIsActive()) {
            telemetryAprilTag();

            if (gamepad1.aWasPressed()) {
                telemetry.addData("Mode", "Field");
                isFieldOriented = true;
            } else if (gamepad1.bWasPressed()) {
                telemetry.addData("Mode", "Robot");
                isFieldOriented = false;
            }

            if (gamepad1.x) {
                autoAlignEnabled = true;
            } else {
                autoAlignEnabled = false;
            }

            if (gamepad1.left_trigger > 0.1) {
                intake.setPower(1.0);
            } else {
                intake.setPower(0.0);
            }

            if (gamepad1.right_trigger > 0.1) {
                shooter.setPower(gamepad1.right_trigger);
            } else {
                shooter.setPower(0.0);
            }


            if (autoAlignEnabled) {
                autoAlign(autoAlignBearing);
            } else {
                if (isFieldOriented) {
                    driveFieldOriented(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
                } else {
                    drive(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
                }
            }
            telemetry.update();

            if (gamepad1.y) {
                imu.resetYaw();
            }

            if (gamepad1.dpad_down) {
                shooter.setPower(-0.5);
            }
        }
    }

    /**
     * Runs the mecanum drivetrain robot-oriented
     *
     * <p>
     * Runs the drivetrain in a robot oriented way by determining the power to move in the specified
     * vector
     * </p>
     * @param forward forward-backward vector
     * @param right right-left vector
     * @param rotate x rotation
     */
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

    /**
     * Runs the mecanum drivetrain field-oriented
     *
     * <p>
     * This method uses trigonometry to determine where the robot wants to go based on the imu values.
     * <br>
     * <b>Passes the power to the existing drive function for robot-oriented</b>
     * </p>
     * @param forward
     * @param right
     * @param rotate
     */
    public void driveFieldOriented(double forward, double right, double rotate) {
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);
        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);
        drive(newForward, newRight, rotate);
    }

    /**
     * Attempts to align with the bearing of an april tag
     *
     * <p>
     * Uses a bang-bang controller to reach the setpoint within a specified setpoint a tolerance
     * </p>
     * @param bearing
     */
    public void autoAlign(double bearing) {
        if (bearing > AUTO_ALIGN_BEARING_SETPOINT + AUTO_ALIGN_BEARING_ERROR) {
            drive(0, 0, AUTO_ALIGN_ROTATION_SPEED);
        } else if (bearing < AUTO_ALIGN_BEARING_SETPOINT - AUTO_ALIGN_BEARING_ERROR) {
            drive(0, 0, -AUTO_ALIGN_ROTATION_SPEED);
        }
    }
}