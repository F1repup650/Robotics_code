package org.firstinspires.ftc.teamcode;

import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
@Disabled
@Autonomous(name="Autonomous OpMode DONT USE - Park ONLY", group="Production")
public class autonomous1 extends LinearOpMode {
    // Define a variable for our color sensor
    ColorSensor color;
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    double FWDSPD = 0.25;
    double TRNSPD = 0.25;
    double STRSPD = 0.4;
    double ENCSPD = 0.3;
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    View relativeLayout;

    @Override
    public void runOpMode() {
        mecanumcontrol motor = new mecanumcontrol();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        tLeft = hardwareMap.dcMotor.get("top_left");
        tRight = hardwareMap.dcMotor.get("top_right");
        bLeft = hardwareMap.dcMotor.get("back_left");
        bRight = hardwareMap.dcMotor.get("back_right");
        DcMotor motorExtension = hardwareMap.dcMotor.get("motor_up");
        motor.init();
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        // Get the color sensor from hardwareMap
        color = hardwareMap.get(ColorSensor.class, "Color1");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

// Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
// on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
// and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

// make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        telemetry.update();
        // Wait for the Play button to be pressed
        waitForStart();
        // Go forward for 2.5 seconds
        motor.forward(FWDSPD);
        motorExtension.setPower(.5);
        sleep(1000);
        motorExtension.setPower(0);
        sleep(1500);
        motor.stop();

        sleep(1000);
        // Read sensor below (done)
        // Go to parking spot depending on whats read after going forward for 2.5 seconds
        while (opModeIsActive()) {
            if (color.red() > color.blue() && color.red() > color.green())// If RED is greater than all other colors
            {
                telemetry.addData("Red: ", color.red());
                telemetry.update();
                motor.reverse(FWDSPD);
                sleep(500);
                motor.stop();
                sleep(1000);
                tLeft.setPower(STRSPD);
                tRight.setPower(-STRSPD);
                bLeft.setPower(-STRSPD);
                bRight.setPower(STRSPD);
                sleep(600);
                motor.stop();
                motor.forward(STRSPD);
                sleep(900);
                motor.stop();
                break;
            }
            if (color.green() > color.blue() && color.green() > color.red())// If GREEN is greater than all other colors
            {
                telemetry.addData("green", color.green());
                telemetry.update();
                motor.reverse(FWDSPD);
                sleep(600);
                motor.stop();
                sleep(1000);
                break;

            }
            if (color.blue() > color.red() && color.blue() > color.green())// If BLUE is greater than all other colors
            {
                telemetry.addData("Blue:", color.blue());
                telemetry.update();
                motor.reverse(FWDSPD);
                sleep(500);
                motor.stop();
                sleep(1000);
                tLeft.setPower(-STRSPD);
                tRight.setPower(STRSPD);
                bLeft.setPower(STRSPD);
                bRight.setPower(-STRSPD);
                sleep(600);
                motor.stop();
                motor.forward(STRSPD);
                sleep(850);
                motor.stop();
                break;

            }


            /*
            Cone colors:
            1. Red
            2. Blue
            3. Green
            ----->The colors have changed per the conditions of the sensor.<-----
            (The sensor is more consistent if we use red, blue, and green)
            */

        }

    }

    private void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     *
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle() {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    private double getAccelX() {
        Acceleration acceleration = imu.getLinearAcceleration();
        double speed = acceleration.yAccel;
        return speed;
    }

    private double getAccelY() {
        Acceleration acceleration = imu.getLinearAcceleration();
        double speed = acceleration.zAccel;
        return speed;
    }

    private double getAccelZ() {
        Acceleration acceleration = imu.getLinearAcceleration();
        double speed = acceleration.xAccel;
        return speed;
    }

    private double getAngularSpeed() {
        AngularVelocity angularVelocity = imu.getAngularVelocity();
        double speed = -angularVelocity.zRotationRate;
        return speed;
    }


    /**
     * See if we are moving in a straight line and if not return a power correction value.
     *
     * @return Power adjustment, + is adjust left - is adjust right.
     */

    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     *
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power) {
        bLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0) {   // turn right.
            leftPower = power;
            rightPower = power;
        } else if (degrees > 0) {   // turn left.
            leftPower = -power;
            rightPower = -power;
        } else return;

        // set power to rotate.
        bLeft.setPower(leftPower);
        tLeft.setPower(leftPower);
        bRight.setPower(rightPower);
        tRight.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {

            }

            while (opModeIsActive() && getAngle() > degrees) {

            }
        } else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {

            }

        // turn the motors off.
        bLeft.setPower(0);
        tLeft.setPower(0);
        bRight.setPower(0);
        tRight.setPower(0);


        // reset angle tracking on new heading.
        resetAngle();
    }


        private double checkAcceleration()
        {
            // The gain value determines how sensitive the correction is to direction changes.
            // You will have to experiment with your robot to get small smooth direction changes
            // to stay on a straight line.
            double correction, xAcc, gain = .04;

            xAcc = getAccelX();

            if (xAcc == 0)
                correction = 0;             // no adjustment.
            else
                correction = -xAcc;        // reverse sign of angle for correction.

            correction = correction * gain;

            return correction;
        }
/**
 * See if we are moving in a straight line and if not return a power correction value.
 * @return Power adjustment, + is adjust left - is adjust right.
 */
        private double checkDirection()
        {
            // The gain value determines how sensitive the correction is to direction changes.
            // You will have to experiment with your robot to get small smooth direction changes
            // to stay on a straight line.
            double correction, angle, gain = .04;

            angle = getAngle();

            if (angle == 0)
                correction = 0;             // no adjustment.
            else
                correction = -angle;        // reverse sign of angle for correction.

            correction = correction * gain;

            return correction;
        }

}