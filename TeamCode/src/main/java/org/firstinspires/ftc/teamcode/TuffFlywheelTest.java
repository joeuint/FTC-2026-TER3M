package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp
public class TuffFlywheelTest extends LinearOpMode {
    DcMotor shooter;
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;

    private void initApriltag() {
        aprilTag = new AprilTagProcessor.Builder()
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.enableLiveView(true);

        builder.addProcessor(aprilTag);

        visionPortal = builder.build();
    }

    @Override
    public void runOpMode() {
        initApriltag();

        shooter = hardwareMap.get(DcMotor.class, "shooter");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        shooter.setPower(0.5);
        while (opModeIsActive()) {
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();

            for (AprilTagDetection tag : currentDetections) {
                // Depot Tag
                if (tag.id == 20) {
                    telemetry.addData("Distance (Range)", tag.ftcPose.range);
                }
            }
            if (gamepad1.x) {
                shooter.setPower(Math.abs(gamepad1.left_stick_x));
            }

            telemetry.addData("Shoot Power", shooter.getPower());
            telemetry.update();
        }
    }
}
