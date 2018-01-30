package org.usfirst.frc.team548.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionAuto extends AutoBaseClass {
	public VisionAuto(DriveAuto driveAuto, int robotPosition) {
		super(driveAuto, robotPosition);
	}


	NetworkTable table = NetworkTableInstance.getDefault().getTable("Limelight");
	
	NetworkTableEntry tx = table.getEntry("tx");
	NetworkTableEntry ty = table.getEntry("ty");
	NetworkTableEntry ta = table.getEntry("ta");
	
	double x = tx.getDouble(0);
	double y = ty.getDouble(0);
	double a = ta.getDouble(0);
	
	
	public void tick() {
		if (isRunning()) {		
			this.driveAuto().showEncoderValues();
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			while (true) { //TODO make real while loop :)
				if (a >= 50) {
					driveInches(-5, 0, 1);// TODO change this
				}
				
			}
		}
	}
}
