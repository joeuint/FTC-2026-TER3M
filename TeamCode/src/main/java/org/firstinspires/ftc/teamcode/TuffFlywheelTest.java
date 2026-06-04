package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class TuffFlywheelTest extends LinearOpMode {
    DcMotor shooter;
    ElapsedTime dt = new ElapsedTime();

    @Override
    public void runOpMode() {
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        shooter.setPower(0.5);
        dt.reset();
        while (opModeIsActive()) {
            if (gamepad1.x) {
                shooter.setPower(Math.abs(gamepad1.left_stick_x));
            }
            telemetry.addData("Shoot Power", shooter.getPower());
            telemetry.update();
        }
    }
}
