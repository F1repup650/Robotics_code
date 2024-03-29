package org.firstinspires.ftc.teamcode;
/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * This file contains an example of a Linear "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 *
 * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
 * This code will work with either a Mecanum-Drive or an X-Drive train.
 * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
 * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
 *
 * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
 *
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 *
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 *
 * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
 * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
 * the direction of all 4 motors (see code below).
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Disabled
@TeleOp(name="Driver With working", group="Linear Opmode")
//@Disabled
public class workingcontroller extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor turret = null;
    private DcMotor lifttop = null;
    private DcMotor extend = null;
    private Servo wrist = null;
    private Servo claw = null;
    private Servo push = null;
    BNO055IMU imu;
    Orientation             lastAngles = new Orientation();
    double                  globalAngle, power = .30, correction;
    View relativeLayout;
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = .8 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "top_left");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "back_left");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "top_right");
        rightBackDrive = hardwareMap.get(DcMotor.class, "back_right");
        //turret = hardwareMap.get(DcMotor.class, "turret");
        //liftbottom = hardwareMap.get(DcMotor.class, "liftbottom");
        //lifttop = hardwareMap.get(DcMotor.class,"lifttop");
        extend = hardwareMap.get(DcMotor.class,"motor_up");
        wrist = hardwareMap.get(Servo.class,"servo");
        //claw = hardwareMap.get(Servo.class,"claw");
        //push = hardwareMap.get(Servo.class, "push");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        telemetry.update();

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);


        //turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //lifttop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //liftbottom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //lifttop.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //liftbottom.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //turret.setTargetPosition(0);
        extend.setTargetPosition(0);
        double dec = 1;
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        runtime.reset();
        //encoderDrive(.1,5.9);
        // run until the end of the match (driver presses STOP)

        while (opModeIsActive()) {
            if(gamepad1.right_stick_x != 0 )
                correction = checkDirection();
            else {
                correction = 0;
                resetAngle();
            }

            telemetry.addData("1 imu heading", lastAngles.firstAngle);
            telemetry.addData("2 global heading", globalAngle);
            telemetry.addData("3 correction", correction);

            double max;
            //turret.setPower(.9);
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = -gamepad1.left_stick_x - correction;  // Note: pushing stick forward gives negative value
            double lateral = -gamepad1.right_stick_x;
            double yaw = -gamepad1.right_stick_y;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = axial - lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial + lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }
            if (gamepad1.left_bumper) {
                dec = .8;
            }
            if (gamepad1.right_bumper) {
                dec = .5;
            }
            if(gamepad1.a){
                //push .setPosition(0);
            }
            if(gamepad1.b){
                //push .setPosition(1);
            }
            if(gamepad1.x){
                rotate(135,1);
            }
            if(gamepad2.left_bumper){
                wrist.setPosition(.5);
            }
            if(gamepad2.dpad_up){
                wrist.setPosition(.5);
            }
            if(gamepad2.dpad_down){
                wrist.setPosition(-1);
            }
            if(gamepad2.right_bumper){
                wrist.setPosition(1);
            }
            if(gamepad2.a){
                turret.setTargetPosition(700);
            }
            if(gamepad2.b)
            {
                turret.setTargetPosition(1500);
            }
            if(gamepad2.y){
                turret.setTargetPosition(2250);
            }
            if(gamepad2.x)
            {
                turret.setTargetPosition(3000);
            }
            /*if((turret.getTargetPosition() != turret.getCurrentPosition())){
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                if(gamepad2.right_trigger >.1){
                    turret.setTargetPosition(turret.getCurrentPosition() + (int)(gamepad2.right_trigger * 150));
                }
                if(gamepad2.left_trigger >.1){
                    turret.setTargetPosition(turret.getCurrentPosition() - (int)(gamepad2.left_trigger * 150));
                }
            }
             */
            if(gamepad2.right_trigger >.1){
                turret.setTargetPosition(turret.getCurrentPosition() + (int)(gamepad2.right_trigger * 150));
            }
            if(gamepad2.left_trigger >.1){
                turret.setTargetPosition(turret.getCurrentPosition() - (int)(gamepad2.left_trigger * 150));
            }
            //liftbottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //lifttop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //liftbottom.setPower(gamepad2.left_stick_y*.1);
            //lifttop.setPower(gamepad2.left_stick_y*.1);
            extend.setPower(-gamepad2.right_stick_y);

//            if(gamepad2.right_stick_x < .1) {
//                turret.setMode((DcMotor.RunMode.RUN_WITHOUT_ENCODER));
//                while (gamepad2.right_stick_x < .1) {
//                    turret.setPower(gamepad2.right_stick_x);
//                }
//                turret.setPower(0);
//                turret.setMode((DcMotor.RunMode.RUN_USING_ENCODER));
//            }
            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.

            /*
            leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
            leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
            rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
            */

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower*dec);
            rightFrontDrive.setPower(rightFrontPower*dec);
            leftBackDrive.setPower(leftBackPower*dec);
            rightBackDrive.setPower(rightBackPower*dec);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Status", "Run Time: " + extend.getCurrentPosition());
            telemetry.addData("Status", "Run Time: " + extend.getTargetPosition());
            telemetry.update();
        }
        //turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle()
    {
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
    private double getAccelX(){
        Acceleration acceleration = imu.getLinearAcceleration();
        double speed = -acceleration.yAccel;
        return speed;
    }
    private double getAccelY(){
        Acceleration acceleration = imu.getLinearAcceleration();
        double speed = acceleration.zAccel;
        return speed;
    }
    private double getAccelZ(){
        Acceleration acceleration = imu.getLinearAcceleration();
        double speed = acceleration.xAccel;
        return speed;
    }
    private double getAunglerSpeed(){
        AngularVelocity angularVelocity = imu.getAngularVelocity();
        double speed = -angularVelocity.zRotationRate;
        return speed;
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

    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power)
    {
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double  leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0)
        {   // turn right.
            leftPower = power;
            rightPower = power;
        }
        else if (degrees > 0)
        {   // turn left.
            leftPower = -power;
            rightPower = -power;
        }
        else return;

        // set power to rotate.
        leftBackDrive.setPower(-leftPower);
        leftFrontDrive.setPower(-leftPower);
        rightBackDrive.setPower(-rightPower);
        rightFrontDrive.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {

            }

            while (opModeIsActive() && getAngle() > degrees) {

            }
        }
        else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {

            }

        // turn the motors off.
        leftBackDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
        rightFrontDrive.setPower(0);


        // reset angle tracking on new heading.
        resetAngle();
    }
//    public void encoderDrive(double speed,
//                             double leftInches)
//    {
//        int newLeftTarget;
//
//        // Ensure that the opmode is still active
//        if (opModeIsActive()) {
//
//            // Determine new target position, and pass to motor controller
//            newLeftTarget = extend.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
//            extend.setTargetPosition(newLeftTarget);
//
//            // Turn On RUN_TO_POSITION
//            extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            // reset the timeout time and start motion.
//            runtime.reset();
//            extend.setPower(Math.abs(speed));
//
//            // keep looping while we are still active, and there is time left, and both motors are running.
//            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
//            // its target position, the motion will stop.  This is "safer" in the event that the robot will
//            // always end the motion as soon as possible.
//            // However, if you require that BOTH motors have finished their moves before the robot continues
//            // onto the next step, use (isBusy() || isBusy()) in the loop test.
//
//            // Stop all motion;
//            extend.setPower(0);
//
//            // Turn off RUN_TO_POSITION
//            extend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//            sleep(250);   // optional pause after each move.
//        }
//    }
}
