package org.usfirst.frc.team548.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift {
	private static Lift instance;
	private static TalonSRX liftMotor;
	private static TalonSRX liftFollower;
	
	public static Lift getInstance() {
		if (instance == null)
			instance = new Lift();
		return instance;
	}
	
	public Lift() {
	
	}
	
	
	public void move(double speed) {
	
		
	}
	public static void goToSwitchHeight() {
		//The switch is the little one.
		
	}
	public static void goToScaleHeight() {
		//The scale is the big one.
		//The scale has three different positions, up, down, and level. It could be useful for autonomous.
		
	}
}
