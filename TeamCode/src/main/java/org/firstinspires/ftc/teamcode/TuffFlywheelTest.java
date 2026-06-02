package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class TuffFlywheelTest extends LinearOpMode {
    DcMotor shooter;
    ElapsedTime dt = new ElapsedTime();
    double lastTicks = 0;

    @Override
    public void runOpMode() {
        shooter = hardwareMap.get(DcMotor.class, "shooter");

        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        dt.reset();
        while (opModeIsActive()) {
            shooter.setPower(1);
            double rotations = Math.abs(shooter.getCurrentPosition() / 280.0);
            telemetry.addData("EncoderTicks", rotations - lastTicks/ dt.seconds());

            telemetry.update();
            lastTicks = rotations;
            dt.reset();
        }
    }
}
