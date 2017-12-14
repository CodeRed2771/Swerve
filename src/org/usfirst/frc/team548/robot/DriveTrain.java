package org.usfirst.frc.team548.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain implements PIDOutput {

	private static DriveTrain instance;
	private static Module moduleA, moduleB, moduleC, moduleD;
	private static AHRS gyro;
	private static PIDController pidControllerRot;

	public static DriveTrain getInstance() {
		if (instance == null)
			instance = new DriveTrain();
		return instance;
	}

	private DriveTrain() {
		moduleA = new Module(Constants.DT_A_DRIVE_TALON_ID,
				Constants.DT_A_TURN_TALON_ID, 4.20, 0.01, 0, 200);
		moduleB = new Module(Constants.DT_B_DRIVE_TALON_ID,
				Constants.DT_B_TURN_TALON_ID, 4.20, 0.01, 0, 200); 
		moduleC = new Module(Constants.DT_C_DRIVE_TALON_ID,
				Constants.DT_C_TURN_TALON_ID, 4.20, 0.01, 0, 200); 
		moduleD = new Module(Constants.DT_D_DRIVE_TALON_ID,
				Constants.DT_D_TURN_TALON_ID, 4.20, 0.01, 0, 200); 
																	
		gyro = new AHRS(SerialPort.Port.kUSB);
		
		//PID is for PID drive not for the modules
		pidControllerRot = new PIDController(Constants.DT_ROT_PID_P,
				Constants.DT_ROT_PID_I, Constants.DT_ROT_PID_D, gyro, this);
		pidControllerRot.setInputRange(-180.0f, 180.0f);
		pidControllerRot.setOutputRange(-1.0, 1.0);
		pidControllerRot.setContinuous(true);
		
		LiveWindow.addActuator("DriveSystem", "RotateController", pidControllerRot);

	}

	public static AHRS getgyro() {
		return gyro;
	}

	public static void setDrivePower(double bbPower, double bhPower,
			double bgPower, double bsPower) {
		moduleA.setDrivePower(bbPower);
		moduleB.setDrivePower(bhPower);
		moduleC.setDrivePower(bgPower);
		moduleD.setDrivePower(bsPower);
	}

	public static void setTurnPower(double bbPower, double bhPower,
			double bgPower, double bsPower) {
		moduleA.setTurnPower(bbPower);
		moduleB.setTurnPower(bhPower);
		moduleC.setTurnPower(bgPower);
		moduleD.setTurnPower(bsPower);
	}

	public static void setTurnOrientation(double bbLoc, double bhLoc, double bgLoc,
			double bsLoc) {
		moduleA.setTurnOrientation(bbLoc);
		moduleB.setTurnOrientation(bhLoc);
		moduleC.setTurnOrientation(bgLoc);
		moduleD.setTurnOrientation(bsLoc);
	}

	public static void setAllTurnPower(double power) {
		setTurnPower(power, power, power, power);
	}

	public static void setAllDrivePower(double power) {
		setDrivePower(power, power, power, power);
	}

	public static void setAllTurnOrientiation(double loc) {
		setTurnOrientation(loc, loc, loc, loc);
	}

	private static final double l = 21, w = 21, r = Math.sqrt((l * l) + (w * w));

	public static boolean isModuleATurnEncConnected() {
		return moduleA.isTurnEncConnected();
	}

	public static boolean isModuleBTurnEncConnected() {
		return moduleB.isTurnEncConnected();
	}

	public static boolean isModuleCTurnEncConnected() {
		return moduleC.isTurnEncConnected();
	}

	public static boolean isModuleDTurnEncConnected() {
		return moduleD.isTurnEncConnected();
	}

	public static void resetAllEnc() {
		moduleA.restTurnEnc();
		moduleB.restTurnEnc();
		moduleC.restTurnEnc();
		moduleD.restTurnEnc();
	}

	public static void stopDrive() {
		moduleA.stopDrive();
		moduleB.stopDrive();
		moduleC.stopDrive();
		moduleD.stopDrive();
	}

	private static double angleToLoc(double angle) {
		if (angle < 0) {
			return .5d + ((180d - Math.abs(angle)) / 360d);
		} else {
			return angle / 360d;
		}
	}

	private static boolean offSetSet = false;

	public static void setOffSets() {
		if (!offSetSet) {
			double bbOff = 0, bhOff = 0, bgOff = 0, bsOff = 0;
			moduleA.setTurnPower(0);
			moduleC.setTurnPower(0);
			moduleB.setTurnPower(0);
			moduleD.setTurnPower(0);

			bbOff = DriveTrain.moduleA.getAbsPos();
			bhOff = DriveTrain.moduleB.getAbsPos();
			bgOff = DriveTrain.moduleC.getAbsPos();
			bsOff = DriveTrain.moduleD.getAbsPos();

			System.out.println("BBoff: " + bbOff);
			System.out.println("BHoff: " + bhOff);
			System.out.println("BGoff: " + bgOff);
			System.out.println("BSoff: " + bsOff);

			resetAllEnc();
			moduleA.setEncPos((int) (locSub(bbOff, Constants.DT_A_ABS_ZERO) * 4095d));
			moduleB.setEncPos((int) (locSub(bhOff, Constants.DT_B_ABS_ZERO) * 4095d));
			moduleC.setEncPos((int) (locSub(bgOff, Constants.DT_C_ABS_ZERO) * 4095d));
			moduleD.setEncPos((int) (locSub(bsOff, Constants.DT_D_ABS_ZERO) * 4095d));
			offSetSet = true;
		}
	}

	public static void resetOffSet() {
		offSetSet = false;
	}

	private static double locSub(double v, double c) {
		if (v - c > 0) {
			return v - c;
		} else {
			return (1 - c) + v;
		}
	}

	public static double getgyroAngle() {
		return gyro.getAngle();
	}

	public static double getGyroAngleInRad() {
		return gyro.getAngle() * (Math.PI / 180d);
	}

	public static void setDriveBrakeMode(boolean b) {
		moduleA.setBrakeMode(b);
		moduleB.setBrakeMode(b);
		moduleC.setBrakeMode(b);
		moduleD.setBrakeMode(b);
	}

	public static double getAverageError() {
		return (Math.abs(moduleA.getError()) + Math.abs(moduleB.getError())
				+ Math.abs(moduleC.getError()) + Math.abs(moduleD
				.getError())) / 4d;
	}

	/*
	 * 
	 * Drive methods
	 */
	public static void swerveDrive(double fwd, double str, double rot) {
		double a = str - (rot * (l / r));
		double b = str + (rot * (l / r));
		double c = fwd - (rot * (w / r));
		double d = fwd + (rot * (w / r));

		double ws1 = Math.sqrt((b * b) + (c * c));
		double ws2 = Math.sqrt((b * b) + (d * d));
		double ws3 = Math.sqrt((a * a) + (d * d));
		double ws4 = Math.sqrt((a * a) + (c * c));

		double wa1 = Math.atan2(b, c) * 180 / Math.PI;
		double wa2 = Math.atan2(b, d) * 180 / Math.PI;
		double wa3 = Math.atan2(a, d) * 180 / Math.PI;
		double wa4 = Math.atan2(a, c) * 180 / Math.PI;

		double max = ws1;
		max = Math.max(max, ws2);
		max = Math.max(max, ws3);
		max = Math.max(max, ws4);
		if (max > 1) {
			ws1 /= max;
			ws2 /= max;
			ws3 /= max;
			ws4 /= max;
		}
		DriveTrain.setDrivePower(ws4, ws2, ws1, ws3);
		DriveTrain.setTurnOrientation(angleToLoc(wa4), angleToLoc(wa2),
				angleToLoc(wa1), angleToLoc(wa3));
	}

	public static void humanDrive(double fwd, double str, double rot) {
		if (Math.abs(rot) < 0.01)
			rot = 0;

		if (Math.abs(fwd) < .15 && Math.abs(str) < .15 && Math.abs(rot) < 0.01) {
			// setOffSets();
			setDriveBrakeMode(true);
			stopDrive();
		} else {
			setDriveBrakeMode(false);
			swerveDrive(fwd, str, rot);
			// resetOffSet();
		}
	}

	public static void pidDrive(double fwd, double str, double angle) {
		double temp = (fwd * Math.cos(getGyroAngleInRad()))
				+ (str * Math.sin(getGyroAngleInRad()));
		str = (-fwd * Math.sin(getGyroAngleInRad()))
				+ (str * Math.cos(getGyroAngleInRad()));
		fwd = temp;
		if (!pidControllerRot.isEnabled())
			pidControllerRot.enable();
		if (Math.abs(fwd) < .15 && Math.abs(str) < .15) {
			pidFWD = 0;
			pidSTR = 0;
		} else {
			setDriveBrakeMode(false);
			pidFWD = fwd;
			pidSTR = str;
		}
		pidControllerRot.setSetpoint(angle);
	}

	public static void fieldCentricDrive(double fwd, double str, double rot) {
		// pretty sure str is the strafe amount (dvv)
		double temp = (fwd * Math.cos(getGyroAngleInRad()))
				+ (str * Math.sin(getGyroAngleInRad()));
		str = (-fwd * Math.sin(getGyroAngleInRad()))
				+ (str * Math.cos(getGyroAngleInRad()));
		fwd = temp;
		humanDrive(fwd, str, rot);
	}

	public static void tankDrive(double left, double right) {
		setAllTurnOrientiation(0);
		setDrivePower(right, left, right, left);
	}

	/*
	 * 
	 * PID Stuff
	 */

	@Override
	public void pidWrite(double output) {
		if (Math.abs(pidControllerRot.getError()) < Constants.DT_ROT_PID_IZONE) {
			pidControllerRot.setPID(Constants.DT_ROT_PID_P,
					Constants.DT_ROT_PID_I, Constants.DT_ROT_PID_D);
		} else {
			// I Zone
			pidControllerRot.setPID(Constants.DT_ROT_PID_P, 0,
					Constants.DT_ROT_PID_D);
			pidControllerRot.setContinuous(true);
		}
		swerveDrive(pidFWD, pidSTR, output);
	}

	public static void disablePID() {
		pidControllerRot.disable();
	}

	private static volatile double pidFWD = 0, pidSTR = 0;
}