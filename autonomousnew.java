package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Autonomous OpMode Park ONLY", group="Production")
public class autonomousnew extends LinearOpMode {
    ColorSensor color;
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    DcMotor mArm;
    Servo motorGrab;
    BNO055IMU imu;
    String colorDet = "";
    double FWDSPD = 0.4;
    double TRNSPD = 0.25;
    double STRSPD = 0.3;
    double ENCSPD = 0.3;

    @Override
    public void runOpMode() {
        // CARSON THERE IS A CLASS FOR AUTONOMOUS MOTOR CONTROL!!! IN FACT, THERE'S THREE OF THEM!
        tLeft = hardwareMap.dcMotor.get("top_left");
        tRight = hardwareMap.dcMotor.get("top_right");
        bLeft = hardwareMap.dcMotor.get("back_left");
        bRight = hardwareMap.dcMotor.get("back_right");
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
        drivetrain motor = new drivetrain();
        motor.leftBackMotor = bLeft;
        motor.leftFrontMotor = tLeft;
        motor.rightBackMotor = bRight;
        motor.rightFrontMotor = tRight;
        motor.imu = imu;
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        color = hardwareMap.get(ColorSensor.class, "Color1");
        mArm = hardwareMap.dcMotor.get("motor_up");
        motorGrab = hardwareMap.servo.get("servo");
        motor.init();
        color = hardwareMap.get(ColorSensor.class, "Color1");
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        waitForStart();
        //---------------Motor_Stuff---------------
        motor.moveDistance((24.0*1.25), FWDSPD);
        mArm.setPower(1.0);
        sleep(500);
        mArm.setPower(0.0);
        sleep(5000);
        colorDet = colorCheck();
        if(colorDet.equals("red")) {
            telemetry.addData("Red: ", color.red());
            telemetry.update();
            motor.moveDistance(-(24.0*0.5), FWDSPD);
            sleep(5000);
            motor.rotate(-90,TRNSPD);
            sleep(5000);
            motor.moveDistance((24.0*0.5), FWDSPD);
            sleep(900);
            motor.stop();
        }
        else if(colorDet.equals("blue")) {
            telemetry.addData("Blue:", color.blue());
            telemetry.update();
            motor.moveDistance(-(24.0*0.5), FWDSPD);
            sleep(1000);
            motor.rotate(90, TRNSPD);
            sleep(600);
            motor.moveForward((24.0*0.5), STRSPD);
            sleep(850);
        }
        else {
            telemetry.addData("Green:", color.green());
            telemetry.update();
        }
        while (opModeIsActive()) {
            sleep(5000);
        }
    }
    public String colorCheck() {
        if (color.red() > color.blue() && color.red() > color.green())// If RED is greater than all other colors
        {
            return "red";
        }
        else if (color.green() > color.blue() && color.green() > color.red())// If GREEN is greater than all other colors
        {
            return "green";
        }
        else if (color.blue() > color.red() && color.blue() > color.green())// If BLUE is greater than all other colors
        {
            return "blue";
        } else {
            return "NaN";
        }
    }
}
