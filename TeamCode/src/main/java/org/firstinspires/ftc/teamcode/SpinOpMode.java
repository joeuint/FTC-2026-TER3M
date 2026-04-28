//package org.firstinspires.ftc.teamcode;
//
//
//import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.IMU;
//
//@Autonomous
//public class SpinOpMode extends LinearOpMode {
//    // var init
//    DcMotor FrontLeftDrive;
//    DcMotor FrontRightDrive;
//    DcMotor BackLeftDrive;
//    DcMotor BackRightDrive;
//    @Override
//        public void runOpMode(){
//            // initialization
//            FrontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeft");
//            FrontRightDrive = hardwareMap.get(DcMotor.class, "frontRight");
//            BackLeftDrive = hardwareMap.get(DcMotor.class, "backLeft");
//            BackRightDrive = hardwareMap.get(DcMotor.class, "backRight");
//
//            FrontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
//            BackRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
//
////
////            IMU imu = hardwareMap.get(IMU.class, "imu");
////            IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
////                RevHubOrientationOnRobot.LogoFacingDirection.UP,
////                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
////            imu.initialize(parameters);
//
//
//        // stops until it starts
//            waitForStart();
//
//            while (opModeIsActive()) {
//                // loop after initialization
//                if (gamepad1.){
//                    FrontLeftDrive.setPower(1);
//                    FrontRightDrive.setPower(1);
//                    BackLeftDrive.setPower(1);
//                    BackRightDrive.setPower(1);
//                    telemetry.addLine("A button is pressed: moving forward");
//                }
//                else if (gamepad1.) {
//                    FrontLeftDrive.setPower(0);
//                    FrontRightDrive.setPower(1);
//                    BackLeftDrive.setPower(0);
//                    BackRightDrive.setPower(1);
//                    telemetry.addLine("*** button is pressed: moving left");
//                }
//                else if (gamepad1.) {
//                    FrontLeftDrive.setPower(1);
//                    FrontRightDrive.setPower(0);
//                    BackLeftDrive.setPower(1);
//                    BackRightDrive.setPower(0);
//                    telemetry.addLine("*** button is pressed: moving right");
//                }
//                else{
//                    FrontLeftDrive.setPower(0);
//                    FrontRightDrive.setPower(0);
//                    BackLeftDrive.setPower(0);
//                    BackRightDrive.setPower(0);
//                    telemetry.addLine("No movement");
//                }
//            }
//    }
//}
