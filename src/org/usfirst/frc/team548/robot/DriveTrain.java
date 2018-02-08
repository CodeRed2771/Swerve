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
	private static PIDController pidControllerRot;

	public static DriveTrain getInstance() {
		if (instance == null)
			instance = new DriveTrain();
		return instance;
	}

	// define robot dimensions. L=wheel base W=track width
	private static final double l = 21, w = 21, r = Math.sqrt((l * l) + (w * w));

	private DriveTrain() {
		moduleA = new Module(Calibration.DT_A_DRIVE_TALON_ID,
				Calibration.DT_A_TURN_TALON_ID, Calibration.AUTO_DRIVE_P, Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, 4.20, 0.01, 0, 200);
		moduleB = new Module(Calibration.DT_B_DRIVE_TALON_ID,
				Calibration.DT_B_TURN_TALON_ID, Calibration.AUTO_DRIVE_P, Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, 4.20, 0.01, 0, 200);
		moduleC = new Module(Calibration.DT_C_DRIVE_TALON_ID,
				Calibration.DT_C_TURN_TALON_ID, Calibration.AUTO_DRIVE_P, Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, 4.20, 0.01, 0, 200);
		moduleD = new Module(Calibration.DT_D_DRIVE_TALON_ID,
				Calibration.DT_D_TURN_TALON_ID, Calibration.AUTO_DRIVE_P, Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, 4.20, 0.01, 0, 200);
																	
		//PID is for PID drive not for the modules
		pidControllerRot = new PIDController(Calibration.DT_ROT_PID_P,
				Calibration.DT_ROT_PID_I, Calibration.DT_ROT_PID_D, RobotGyro.getGyro(), this);
		pidControllerRot.setInputRange(-180.0f, 180.0f);
		pidControllerRot.setOutputRange(-1.0, 1.0);
		pidControllerRot.setContinuous(true);
		
		LiveWindow.addActuator("DriveSystem", "RotateController", pidControllerRot);

	}

	public static void setDrivePower(double modAPower, double modBPower, double modCPower, double modDPower) {
		moduleA.setDrivePower(modAPower);
		moduleB.setDrivePower(modBPower);
		moduleC.setDrivePower(modCPower);
		moduleD.setDrivePower(modDPower);
	}

	public static void setTurnPower(double modAPower, double modBPower, double modCPower, double modDPower) {
		moduleA.setTurnPower(modAPower);
		moduleB.setTurnPower(modBPower);
		moduleC.setTurnPower(modCPower);
		moduleD.setTurnPower(modDPower);
	}

	public static void setTurnOrientation(double modAPosition, double modBPosition, double modCPosition, double modDPosition) {
		// position is a value from 0 to 1 that indicates
		// where in the rotation of the module the wheel should be set.
		// e.g. a value of .5 indicates a half turn from the zero position
		moduleA.setTurnOrientation(modAPosition);
		moduleB.setTurnOrientation(modBPosition);
		moduleC.setTurnOrientation(modCPosition);
		moduleD.setTurnOrientation(modDPosition);
	}

	public static void setAllTurnOrientiation(double loc) {
		setTurnOrientation(loc, loc, loc, loc);
	}

	public static void setAllDrivePosition(int position) {
		setDrivePosition(position,position,position,position);
	}
	
	public static void setDrivePosition(int modAPosition, int modBPosition, int modCPosition, int modDPosition) {
		moduleA.setDrivePIDToSetPoint(modAPosition);
		moduleB.setDrivePIDToSetPoint(modBPosition);
		moduleC.setDrivePIDToSetPoint(modCPosition);
		moduleD.setDrivePIDToSetPoint(modDPosition);
	}

	public static int getDriveEnc() {
		return (moduleA.getDriveEnc() + moduleB.getDriveEnc() + moduleC.getDriveEnc() + moduleD.getDriveEnc())/4;
	}
	
	public static void autoSetRot(double rot) {
		swerveDrive(0,0,rot);
	}

	public static void setAllTurnPower(double power) {
		setTurnPower(power, power, power, power);
	}

	public static void setAllDrivePower(double power) {
		setDrivePower(power, power, power, power);
	}

	
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

	public static void resetDriveEncoders() {
		moduleA.resetDriveEnc();
		moduleB.resetDriveEnc();
		moduleC.resetDriveEnc();
		moduleD.resetDriveEnc();
	}

	public static void stopDrive() {
		moduleA.stopDrive();
		moduleB.stopDrive();
		moduleC.stopDrive();
		moduleD.stopDrive();
	}

	public static double angleToLoc(double angle) {
		if (angle < 0) {
			return .5d + ((180d - Math.abs(angle)) / 360d);
		} else {
			return angle / 360d;
		}
	}

	private static boolean offSetSet = false;

	public static void setOffSets() {
		if (!offSetSet) {
			double modAOff = 0, modBOff = 0, modCOff = 0, modDOff = 0;
			moduleA.setTurnPower(0);
			moduleC.setTurnPower(0);
			moduleB.setTurnPower(0);
			moduleD.setTurnPower(0);

			modAOff = DriveTrain.moduleA.getAbsPos();
			modBOff = DriveTrain.moduleB.getAbsPos();
			modCOff = DriveTrain.moduleC.getAbsPos();
			modDOff = DriveTrain.moduleD.getAbsPos();

//			resetAllEnc(); removed 1/8/18
			moduleA.setEncPos((int) (locSub(modAOff, Calibration.GET_DT_A_ABS_ZERO()) * 4095d));
			moduleB.setEncPos((int) (locSub(modBOff, Calibration.GET_DT_B_ABS_ZERO()) * 4095d));
			moduleC.setEncPos((int) (locSub(modCOff, Calibration.GET_DT_C_ABS_ZERO()) * 4095d));
			moduleD.setEncPos((int) (locSub(modDOff, Calibration.GET_DT_D_ABS_ZERO()) * 4095d));
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

	public static void setDriveBrakeMode(boolean b) {
		moduleA.setBrakeMode(b);
		moduleB.setBrakeMode(b);
		moduleC.setBrakeMode(b);
		moduleD.setBrakeMode(b);
	}

	public static double getAverageError() {
		return (Math.abs(moduleA.getError()) + 
				Math.abs(moduleB.getError()) + 
				Math.abs(moduleC.getError()) + 
				Math.abs(moduleD.getError())) / 4d;
	}
	
	public static double getDriveError(){
		return moduleA.getDriveError();
	}

	/*
	 * 
	 * Drive methods
	 */
	public static void swerveDrive(double fwd, double strafe, double rot) {
		double a = strafe - (rot * (l / r));
		double b = strafe + (rot * (l / r));
		double c = fwd - (rot * (w / r));
		double d = fwd + (rot * (w / r));

		double ws1 = Math.sqrt((b * b) + (c * c));  // front_right  (CHECK THESE AGAINST OUR BOT)
		double ws2 = Math.sqrt((b * b) + (d * d));  // front_left
		double ws3 = Math.sqrt((a * a) + (d * d));	// rear_left
		double ws4 = Math.sqrt((a * a) + (c * c)); 	// rear_right

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
		
		SmartDashboard.putNumber("swerve a", a);
		SmartDashboard.putNumber("swerve b", b);
		SmartDashboard.putNumber("swerve c", c);
		SmartDashboard.putNumber("swerve d", d);
		SmartDashboard.putNumber("swerve wa1", wa1);
		SmartDashboard.putNumber("swerve wa2", wa2);
		SmartDashboard.putNumber("swerve wa3", wa3);
		SmartDashboard.putNumber("swerve wa4", wa4);
		
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

	public static void pidDrive(double fwd, double strafe, double angle) {
		double temp = (fwd * Math.cos(RobotGyro.getGyroAngleInRad()))
				+ (strafe * Math.sin(RobotGyro.getGyroAngleInRad()));
		strafe = (-fwd * Math.sin(RobotGyro.getGyroAngleInRad()))
				+ (strafe * Math.cos(RobotGyro.getGyroAngleInRad()));
		fwd = temp;
		if (!pidControllerRot.isEnabled())
			pidControllerRot.enable();
		if (Math.abs(fwd) < .15 && Math.abs(strafe) < .15) {
			pidFWD = 0;
			pidSTR = 0;
		} else {
			setDriveBrakeMode(false);
			pidFWD = fwd;
			pidSTR = strafe;
		}
		pidControllerRot.setSetpoint(angle);
	}

	public static void fieldCentricDrive(double fwd, double strafe, double rot) {
		double temp = (fwd * Math.cos(RobotGyro.getGyroAngleInRad()))
				+ (strafe * Math.sin(RobotGyro.getGyroAngleInRad()));
		strafe = (-fwd * Math.sin(RobotGyro.getGyroAngleInRad()))
				+ (strafe * Math.cos(RobotGyro.getGyroAngleInRad()));
		fwd = temp;
		humanDrive(fwd, strafe, rot);
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
		if (Math.abs(pidControllerRot.getError()) < Calibration.DT_ROT_PID_IZONE) {
			pidControllerRot.setPID(Calibration.DT_ROT_PID_P,
					Calibration.DT_ROT_PID_I, Calibration.DT_ROT_PID_D);
		} else {
			// I Zone
			pidControllerRot.setPID(Calibration.DT_ROT_PID_P, 0,
					Calibration.DT_ROT_PID_D);
			pidControllerRot.setContinuous(true);
		}
		swerveDrive(pidFWD, pidSTR, output);
	}

	public static void disablePID() {
		pidControllerRot.disable();
	}
	
	public static double[] getAllTurnOrientations() {
		return new double[] {moduleA.getTurnOrientation(), moduleB.getTurnOrientation(),
				moduleC.getTurnOrientation(), moduleD.getTurnOrientation()};
	}
	
	public static void setDriveModulesPIDValues(double p, double i, double d){
		moduleA.setDrivePIDValues(p, i, d);
		moduleB.setDrivePIDValues(p, i, d);
		moduleC.setDrivePIDValues(p, i, d);
		moduleD.setDrivePIDValues(p, i, d);
	}

	private static volatile double pidFWD = 0, pidSTR = 0;
}