package org.usfirst.frc.team548.robot;


// DVV - I feel like the code in fieldCentricDrive is wrong in how it's using radians instead of just angle
//       or is that the same? Accordign to the white paper, the value used in the sin/cos functions should
//		 be "the gyro angle".  What is that?  I would assume that if we're turned a quarter turn, the gyro
//		 angle would be "90".  If so, we need to determine if we should be using a full 360, or 0-180,and -180 to 0.
//

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

	// define robot dimensions. L=wheel base W=track width
	private static final double l = 21, w = 21, r = Math.sqrt((l * l) + (w * w));

	private DriveTrain() {
		moduleA = new Module(Calibration.DT_A_DRIVE_TALON_ID,
				Calibration.DT_A_TURN_TALON_ID, 4.20, 0.01, 0, 200);
		moduleB = new Module(Calibration.DT_B_DRIVE_TALON_ID,
				Calibration.DT_B_TURN_TALON_ID, 4.20, 0.01, 0, 200); 
		moduleC = new Module(Calibration.DT_C_DRIVE_TALON_ID,
				Calibration.DT_C_TURN_TALON_ID, 4.20, 0.01, 0, 200); 
		moduleD = new Module(Calibration.DT_D_DRIVE_TALON_ID,
				Calibration.DT_D_TURN_TALON_ID, 4.20, 0.01, 0, 200); 
																	
		gyro = new AHRS(SerialPort.Port.kUSB);
		
		//PID is for PID drive not for the modules
		pidControllerRot = new PIDController(Calibration.DT_ROT_PID_P,
				Calibration.DT_ROT_PID_I, Calibration.DT_ROT_PID_D, gyro, this);
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

	public static void setTurnOrientation(double modALoc, double modBLoc, double modCLoc,
			double modDLoc) {
		moduleA.setTurnOrientation(modALoc);
		moduleB.setTurnOrientation(modBLoc);
		moduleC.setTurnOrientation(modCLoc);
		moduleD.setTurnOrientation(modDLoc);
	}

	public static int getDriveEnc() {
		return moduleA.getDriveEnc();
	}
	
	public static void autoSetDrive(double speed) {
		SmartDashboard.putNumber("Auto Set Drive", speed);
		swerveAutoDrive(speed,0,0);
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

	public static void setAllTurnOrientiation(double loc) {
		setTurnOrientation(loc, loc, loc, loc);
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

//	public static void resetAllEnc() {
//		moduleA.restTurnEnc();
//		moduleB.restTurnEnc();
//		moduleC.restTurnEnc();
//		moduleD.restTurnEnc();
//	}

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

//			System.out.println("BBoff: " + modAOff);
//			System.out.println("BHoff: " + modBOff);
//			System.out.println("BGoff: " + modCOff);
//			System.out.println("BSoff: " + modDOff);

//			resetAllEnc(); removed 1/8/18
			moduleA.setEncPos((int) (locSub(modAOff, Calibration.DT_A_ABS_ZERO) * 4095d));
			moduleB.setEncPos((int) (locSub(modBOff, Calibration.DT_B_ABS_ZERO) * 4095d));
			moduleC.setEncPos((int) (locSub(modCOff, Calibration.DT_C_ABS_ZERO) * 4095d));
			moduleD.setEncPos((int) (locSub(modDOff, Calibration.DT_D_ABS_ZERO) * 4095d));
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
		double adjustedAngle = -Math.floorMod((long)gyro.getAngle(), 360);
		if (adjustedAngle>180) 
			adjustedAngle = -(360-adjustedAngle);
		
		return adjustedAngle * (Math.PI / 180d);
	}
//	public static double getGyroAngleInRad() {
//		return gyro.getAngle() * (Math.PI / 180d);
//	}

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

	/*
	 * 
	 * Drive methods
	 */
	public static void swerveDrive(double fwd, double str, double rot) {
		double a = str - (rot * (l / r));
		double b = str + (rot * (l / r));
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
	
	public static void swerveAutoDrive(double fwd, double str, double rot) {
		double a = str - (rot * (l / r));
		double b = str + (rot * (l / r));
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
		
		
		if(Math.abs(wa1) > 90){
			if(wa1 < 0){
				wa1 += 180;
				wa2 += 180;
				wa3 += 180;
				wa4 += 180;
			}else{
				wa1 -= 180;
				wa2 -= 180;
				wa3 -= 180;
				wa4 -= 180;
			}
			ws1 = -ws1;
			ws2 = -ws2;
			ws3 = -ws3;
			ws4 = -ws4;
			
		}
		
		SmartDashboard.putNumber("swerve a", a);
		SmartDashboard.putNumber("swerve b", b);
		SmartDashboard.putNumber("swerve c", c);
		SmartDashboard.putNumber("swerve d", d);
		SmartDashboard.putNumber("swerve wa1", wa1);
		SmartDashboard.putNumber("swerve wa2", wa2);
		SmartDashboard.putNumber("swerve wa3", wa3);
		SmartDashboard.putNumber("swerve wa4", wa4);
		
		SmartDashboard.putNumber("swerve ws1", ws1);
		SmartDashboard.putNumber("swerve ws2", ws2);
		SmartDashboard.putNumber("swerve ws3", ws3);
		SmartDashboard.putNumber("swerve ws4", ws4);
		
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
		double temp = (fwd * Math.cos(getGyroAngleInRad()))
				+ (strafe * Math.sin(getGyroAngleInRad()));
		strafe = (-fwd * Math.sin(getGyroAngleInRad()))
				+ (strafe * Math.cos(getGyroAngleInRad()));
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
		double temp = (fwd * Math.cos(getGyroAngleInRad()))
				+ (strafe * Math.sin(getGyroAngleInRad()));
		strafe = (-fwd * Math.sin(getGyroAngleInRad()))
				+ (strafe * Math.cos(getGyroAngleInRad()));
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

	private static volatile double pidFWD = 0, pidSTR = 0;
}