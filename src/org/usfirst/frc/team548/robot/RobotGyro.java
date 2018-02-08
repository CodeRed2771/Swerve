package org.usfirst.frc.team548.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;

public class RobotGyro {
	private static RobotGyro instance;
	private static AHRS mGyro;
    
	public static RobotGyro getInstance() {
		if (instance == null)
			instance = new RobotGyro();
		return instance;
	}
	
	public RobotGyro() {
		mGyro = new AHRS(SerialPort.Port.kUSB);
	}
	
	public static AHRS getGyro() {
		return mGyro;
	}
	
	public static double getAngle() {
		return mGyro.getAngle();
	}
	
	public static void reset() {
		mGyro.reset();
	}
	
	public static double pidGet() {
		return mGyro.pidGet();
	}
	
	public static double getGyroAngleInRad() {
		double adjustedAngle = -Math.floorMod((long)mGyro.getAngle(), 360);
		if (adjustedAngle>180) 
			adjustedAngle = -(360-adjustedAngle);
		
		return adjustedAngle * (Math.PI / 180d);
	}
	

}
