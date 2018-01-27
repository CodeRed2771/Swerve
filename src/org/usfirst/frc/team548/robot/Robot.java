
package org.usfirst.frc.team548.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	private XboxController xbox;
	private DriverStation dt;
	
	//---AUTO IMPORTS---
	DriveAuto driveAuto;
	SendableChooser<String> autoChooser;
	final String autoCalibrateDrive = "Auto Calibrate Drive";
	final String calibrateSwerveModules = "Calibrate Swerve Modules";
	final String deleteSwerveCalibration = "Delete Swerve Calibration";
	final String autoSwitch = "Auto Switch";
	final String autoCubeFollow = "Auto Cube Follow";
	final String autoBaseLine = "Auto Base Line";
	String autoSelected;
	AutoBaseClass mAutoProgram;
	
    public void robotInit() {
    	xbox = new XboxController(Calibration.XBOX_PORT);
    	DriveTrain.getInstance();
    	dt = DriverStation.getInstance();
      	Calibration.loadSwerveCalibration();
  	  
      	driveAuto = new DriveAuto();
      	autoChooser = new SendableChooser<String>();
      	autoChooser.addDefault(autoBaseLine, autoBaseLine);
      	autoChooser.addObject(calibrateSwerveModules, calibrateSwerveModules);
      	autoChooser.addObject(deleteSwerveCalibration, deleteSwerveCalibration);
      	autoChooser.addObject(autoCalibrateDrive, autoCalibrateDrive);
      	//autoChooser.addObject(autoCubeFollow, autoCubeFollow);
      	autoChooser.addObject(autoSwitch, autoSwitch);
      	
      	SmartDashboard.putData("Auto choices", autoChooser);
    }
    
    public void autonomousInit() {
    	autoSelected = (String) autoChooser.getSelected();
    	SmartDashboard.putString("Auto Selected: ", autoSelected);
    	
    	mAutoProgram = null;
    	
    	switch(autoSelected){
    	    case autoCalibrateDrive:
        		mAutoProgram = new AutoCalibrateDrive(driveAuto, 1);
        		break;
    	    case calibrateSwerveModules:
    	    	double[] pos = DriveTrain.getAllTurnOrientations();
    	    	Calibration.saveSwerveCalibration(pos[0], pos[1], pos[2], pos[3]);
    	    	break;
    	    case deleteSwerveCalibration:
    	    	Calibration.resetSwerveDriveCalibration();
    	    	break;
    	    case autoSwitch:
    	    	mAutoProgram = new AutoSwitch(driveAuto, 1);
        		break;
    	    case autoCubeFollow:
    	    	mAutoProgram = new AutoCubeFollow(driveAuto, 1);
    	    	break;
    	    case autoBaseLine:
    	    	mAutoProgram = new AutoBaseLine(driveAuto, 1);
    	    	break;
    	} 

    	driveAuto.reset();
    	DriveTrain.setAllTurnOrientiation(0);
    	mAutoProgram.start();
    
    }
    
    public void autonomousPeriodic() {
    
    	if (mAutoProgram != null) {
        	mAutoProgram.tick();
            driveAuto.tick();
            driveAuto.showEncoderValues();
    	}
    }
    
    public void disabledInit() {
    	DriveTrain.resetOffSet();
    	DriveTrain.resetDriveEncoders();
    }
    
    public void disabledPeriodic() {
    	DriveTrain.setOffSets();
    	xbox.setRightRumble(0);
    	xbox.setLeftRumble(0);
    	DriveTrain.disablePID();
    }
  
    @Override
    public void teleopInit() {
    	driveAuto.disable();
    }
    
    public void teleopPeriodic() {    	
    	DriveTrain.fieldCentricDrive(xbox.getLeftStickYAxis(), -xbox.getLeftStickXAxis(), powTwoThing(xbox.getRightStickXAxis()));
    	//DriveTrain.pidDrive(xbox.getLeftStickYAxis(), xbox.getLeftStickXAxis(), changeAngle(xbox.getRightStickXAxis(), xbox.getRightStickYAxis()));
    	// DriveTrain.tankDrive(xbox.getLeftStickYAxis(), xbox.getRightStickYAxis());
    	//DriveTrain.humanDrive(xbox.getLeftStickYAxis(), xbox.getLeftStickXAxis(), Math.pow(xbox.getRightStickXAxis(), 3));
    	
    	SmartDashboard.putBoolean("Mod A Turn Encoder", DriveTrain.isModuleATurnEncConnected());
    	SmartDashboard.putBoolean("Mod B Turn Encoder", DriveTrain.isModuleBTurnEncConnected());
    	SmartDashboard.putBoolean("Mod C Turn Encoder", DriveTrain.isModuleCTurnEncConnected());
    	SmartDashboard.putBoolean("Mod D Turn Encoder", DriveTrain.isModuleDTurnEncConnected());
    	SmartDashboard.putNumber("Gyro angle", DriveTrain.getgyroAngle());
    	SmartDashboard.putNumber("Gyro radians",  DriveTrain.getGyroAngleInRad());
    	SmartDashboard.putNumber("Avg. Error", DriveTrain.getAverageError());
    	
    	xbox.setRightRumble(Math.pow(DriveTrain.getAverageError()/1300d, 2));
    	xbox.setLeftRumble((dt.isBrownedOut() ? 1 : 0));
    }
    
    public void testInit() {
		double[] orientations = DriveTrain.getInstance().getAllTurnOrientations();
		Calibration.saveSwerveCalibration(orientations[0], orientations[1], orientations[2], orientations[3]);
    }
    
    public void testPeriodic() {
//    	LiveWindow.addSensor("DriveSystem", "Hyro", DriveTrain.getgyro());
    }
    
    
    
    private double powTwoThing(double v) {
    	return (v > 0 ) ? Math.pow(v, 2) : -Math.pow(v, 2);
    }
    
    double lastAngle = 0;
    public double changeAngle(double x, double y) {
    	if (Math.sqrt((x*x)+(y*y)) > .25) {
    		lastAngle = Math.atan2(x, y)*(180d/Math.PI);
			return lastAngle;
		} else {
			return lastAngle;
		}
    }
    
}
