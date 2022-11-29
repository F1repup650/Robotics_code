package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Autonomous OpMode (Direct Motor Control (DMC))", group="Tests")
//@Disabled
public class autotest1 extends LinearOpMode
{
    private final ElapsedTime runtime = new ElapsedTime();
    DcMotor tLeft;
    DcMotor tRight;
    DcMotor bLeft;
    DcMotor bRight;
    DcMotor mArm;
    double FWDSPD = 0.3;
    double TRNSPD = 0.25;
    double STRSPD = 0.3;

    @Override
    public void runOpMode() {
        //normalcontrol motor = new normalcontrol();
        mecanumcontrol motor = new mecanumcontrol();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        tLeft  = hardwareMap.dcMotor.get("top_left");
        tRight = hardwareMap.dcMotor.get("top_right");
        bLeft = hardwareMap.dcMotor.get("back_left");
        bRight = hardwareMap.dcMotor.get("back_right");
        mArm = hardwareMap.dcMotor.get("arm_motor");
        motor.init();
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        waitForStart();
        motor.forward(FWDSPD);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        motor.stop();
        motor.turnRight(TRNSPD);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.5)) {
            telemetry.addData("Path", "Leg 2: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        motor.stop();
        motor.turnLeft(TRNSPD);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.5)) {
            telemetry.addData("Path", "Leg 3: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        motor.stop();
        motor.reverse(FWDSPD);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 4: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        motor.stop();
        motor.strafeLeft(STRSPD);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.5)) {
            telemetry.addData("Path","Leg 5: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        motor.stop();
        motor.strafeRight(STRSPD);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.5)) {
            telemetry.addData("Path","Leg 6: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        motor.stop();
        runtime.reset();
        while (opModeIsActive()) {
            telemetry.addData("Path", "Done: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
    }
}
