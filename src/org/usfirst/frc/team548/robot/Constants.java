package org.usfirst.frc.team548.robot;

public class Constants {
	public final static int XBOX_PORT = 0;
	
	/*
	 * Drive Train
	 */
	//Big Bird A
	public final static int DT_A_DRIVE_TALON_ID = 7;
	public final static int DT_A_TURN_TALON_ID = 8;
	public final static double DT_A_ABS_ZERO = 0.532;
	//Big Horse - B
	public final static int DT_B_DRIVE_TALON_ID = 1;
	public final static int DT_B_TURN_TALON_ID = 2;
	public final static double DT_B_ABS_ZERO = .827;
	//Big Giraffe - C
	public final static int DT_C_DRIVE_TALON_ID = 3;
	public final static int DT_C_TURN_TALON_ID = 4;
	public final static double DT_C_ABS_ZERO = .9;
	//Big Sushi - D
	public final static int DT_D_DRIVE_TALON_ID = 5;
	public final static int DT_D_TURN_TALON_ID = 6;
	public final static double DT_D_ABS_ZERO = .913;
	
	//Rot PID .01, 0.0001, 0.008,
	public final static double DT_ROT_PID_P = .007;
	public final static double DT_ROT_PID_I =.0004;
	public final static double DT_ROT_PID_D= .000;
	public final static double DT_ROT_PID_IZONE = 18;
}
