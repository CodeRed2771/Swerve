package org.usfirst.frc.team548.robot;

public class Calibration {

public final static int XBOX_PORT = 0;
	
	/*
	 * Drive Train
	 */
	
	// Physical Module - A
	public final static int DT_A_DRIVE_TALON_ID = 1;
	public final static int DT_A_TURN_TALON_ID = 2;
	public final static double DT_A_ABS_ZERO = .663;
	// Physical Module - B
	public final static int DT_B_DRIVE_TALON_ID = 5;
	public final static int DT_B_TURN_TALON_ID = 6;
	public final static double DT_B_ABS_ZERO = .272;
	// Physical Module - C
	public final static int DT_C_DRIVE_TALON_ID = 3;
	public final static int DT_C_TURN_TALON_ID = 4;
	public final static double DT_C_ABS_ZERO = .048;
	// Physical Module - D
	public final static int DT_D_DRIVE_TALON_ID = 7;
	public final static int DT_D_TURN_TALON_ID = 8;
	public final static double DT_D_ABS_ZERO = 0.634;
	
	//Rot PID .01, 0.0001, 0.008,
	public final static double DT_ROT_PID_P = .007;
	public final static double DT_ROT_PID_I =.0004;
	public final static double DT_ROT_PID_D= .000;
	public final static double DT_ROT_PID_IZONE = 18;

	public static final double DRIVE_DISTANCE_TICKS_PER_INCH = 20.1;
	
	public static final double AUTO_ROT_P = 0.03; // increased from .022 on 3/20/17 dvv
	public static final double AUTO_ROT_I = 0;
	public static final double AUTO_ROT_D = 0.067;

	public static final double AUTO_DRIVE_P = 0.0023;
	public static final double AUTO_DRIVE_I = 0.7;
	public static final double AUTO_DRIVE_D = 0.013;


}
