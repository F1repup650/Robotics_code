package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

@Autonomous
public class Test1 extends LinearOpMode {
    BNO055IMU imu;
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor bLeftMotor;
    DcMotor bRightMotor;
    double distance;
    double speed;
    double time;
    double currentSpeed;
    double error;
    double correction;

    @Override
    public void runOpMode() {
        // Initialize the IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // What is the IMU, and what is it doing?

        // Initialize motors
        leftMotor = hardwareMap.get(DcMotor.class, "top_left");
        rightMotor = hardwareMap.get(DcMotor.class, "top_right");
        bLeftMotor = hardwareMap.get(DcMotor.class, "back_left");
        bRightMotor = hardwareMap.get(DcMotor.class, "back_right");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        bLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        bRightMotor.setDirection(DcMotor.Direction.REVERSE);
        // Wait for the start button to be pressed
        waitForStart();

        // Set the distance and speed
        distance = 2; // 10 meters
        speed = 0.025; // 0.5 meters/second

        // Calculate the time needed to travel the distance
        time = distance / speed;

        // Set the motors to run for the calculated time
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
        bLeftMotor.setPower(speed);
        bRightMotor.setPower(speed);

        // Loop for the calculated time
        for (double i = 0; i < time; i += 0.25
        ) {
            // Get the current linear acceleration of the robot
            Acceleration linearAcceleration = imu.getLinearAcceleration();
            currentSpeed = (linearAcceleration).xAccel;

            // Calculate the error between the desired and current speeds
            error = speed - currentSpeed;

            // Calculate the correction based on the error
            correction = error / 10.0;

            // Apply the correction to the motor powers
            leftMotor.setPower(speed + correction);
            rightMotor.setPower(speed + correction);
            bRightMotor.setPower(speed + correction);
            bLeftMotor.setPower(speed + correction);

            // Sleep for 100 milliseconds
            sleep(100);
            telemetry.addData("speed:",currentSpeed);
            telemetry.addData("Time: ", i);
            telemetry.addData("Correction: ", correction);
            telemetry.addData("Error", error);
            telemetry.update();


        }

        // Stop the robot
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        bRightMotor.setPower(0);
        bLeftMotor.setPower(0);

    }
}
