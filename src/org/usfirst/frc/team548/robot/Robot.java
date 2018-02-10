
package org.usfirst.frc.team548.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	private XboxController xbox;
	
	//---AUTO IMPORTS---
	SendableChooser<String> autoChooser;
	final String autoDriveDoubleDiamond = "Auto Drive Double Diamond";
	final String autoRotateTest = "Auto Rotate Test";
	final String calibrateSwerveModules = "Calibrate Swerve Modules";
	final String deleteSwerveCalibration = "Delete Swerve Calibration";
	final String autoSwitch = "Auto Switch";
	final String autoCubeFollow = "Auto Cube Follow";
	final String autoBaseLine = "Auto Base Line";
	final String visionAuto = "Vision Auto";
	String autoSelected;
	AutoBaseClass mAutoProgram;
	
    public void robotInit() {
    	xbox = new XboxController(Calibration.XBOX_PORT);
    	RobotGyro.getInstance();
    	DriveTrain.getInstance();
      	DriveAuto.getInstance();
    	Lift.getInstance();
      	Calibration.loadSwerveCalibration();
      	
      	RobotGyro.reset();  // this may need to be done later to give Gyro time to initialize
      	
      	autoChooser = new SendableChooser<String>();
      	autoChooser.addDefault(autoBaseLine, autoBaseLine);
      	autoChooser.addObject(calibrateSwerveModules, calibrateSwerveModules);
      	autoChooser.addObject(deleteSwerveCalibration, deleteSwerveCalibration);
      	autoChooser.addObject(autoDriveDoubleDiamond, autoDriveDoubleDiamond);
      	autoChooser.addObject(autoRotateTest, autoRotateTest);
      	//autoChooser.addObject(autoCubeFollow, autoCubeFollow);
      	autoChooser.addObject(autoSwitch, autoSwitch);
      	autoChooser.addObject(visionAuto, visionAuto);
      	
      	SmartDashboard.putNumber("Auto P:", Calibration.AUTO_DRIVE_P);
    	SmartDashboard.putNumber("Auto I:", Calibration.AUTO_DRIVE_I);
    	SmartDashboard.putNumber("Auto D:", Calibration.AUTO_DRIVE_D);
    	
    	SmartDashboard.putNumber("Robot Position", 1);
      	      	
      	SmartDashboard.putData("Auto choices", autoChooser);
    }
    
    public void autonomousInit() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	int mRobotPosition = (int) SmartDashboard.getNumber("Robot Position",1);
    	autoSelected = (String) autoChooser.getSelected();
    	SmartDashboard.putString("Auto Selected: ", autoSelected);
    	SmartDashboard.putString("GameData", gameData);
    	
    	mAutoProgram = null;
    	
    	switch(autoSelected){
    	    case autoDriveDoubleDiamond:
        		mAutoProgram = new AutoCalibrateDrive(mRobotPosition);
        		break;
    	    case autoRotateTest:
    	    	mAutoProgram = new AutoRotateTest(mRobotPosition);
    	    	break;
    	    case calibrateSwerveModules:
    	    	double[] pos = DriveTrain.getAllAbsoluteTurnOrientations();
    	    	Calibration.saveSwerveCalibration(pos[0], pos[1], pos[2], pos[3]);
    	    	break;
    	    case deleteSwerveCalibration:
    	    	Calibration.resetSwerveDriveCalibration();
    	    	break;
    	    case autoSwitch:
    	    	mAutoProgram = new AutoMainSwitch(mRobotPosition);
        		break;
    	    case autoCubeFollow:
    	    	mAutoProgram = new AutoCubeFollow(mRobotPosition);
    	    	break;
    	    case autoBaseLine:
    	    	mAutoProgram = new AutoBaseLine(mRobotPosition);
    	    	break;
    	    case visionAuto:
    	    	mAutoProgram = new VisionAuto(mRobotPosition);
    	} 

    	DriveAuto.reset();
    	DriveTrain.setAllTurnOrientiation(0);
        	
    	if (mAutoProgram != null) {
    		mAutoProgram.start();
    	}
    }
    
    public void autonomousPeriodic() {
    
    	if (mAutoProgram != null) {
        	mAutoProgram.tick();
            DriveAuto.tick();
            DriveAuto.showEncoderValues();
    	}
    	
    	DriveTrain.setDriveModulesPIDValues(SmartDashboard.getNumber("Auto P:", 0), SmartDashboard.getNumber("Drive I:", 0), SmartDashboard.getNumber("Auto D:", 0));
    	SmartDashboard.putNumber("Drive Error", DriveTrain.getAverageError());
    }
    
    public void teleopPeriodic() {    	
    	
    	DriveTrain.fieldCentricDrive(xbox.getLeftStickYAxis(), -xbox.getLeftStickXAxis(), powTwoThing(xbox.getRightStickXAxis()));
    	
    	SmartDashboard.putBoolean("Mod A Turn Encoder", DriveTrain.isModuleATurnEncConnected());
    	SmartDashboard.putBoolean("Mod B Turn Encoder", DriveTrain.isModuleBTurnEncConnected());
    	SmartDashboard.putBoolean("Mod C Turn Encoder", DriveTrain.isModuleCTurnEncConnected());
    	SmartDashboard.putBoolean("Mod D Turn Encoder", DriveTrain.isModuleDTurnEncConnected());
    	SmartDashboard.putNumber("Gyro angle", RobotGyro.getAngle());
    	SmartDashboard.putNumber("Gyro radians",  RobotGyro.getGyroAngleInRad());
    	SmartDashboard.putNumber("Avg. Error", DriveTrain.getAverageError());
    	
    }
    
    public void disabledInit() {
    	DriveTrain.allowTurnEncoderReset(); // allows the turn encoders to be reset once during disabled periodic
    	DriveTrain.resetDriveEncoders();
    }
    
    public void disabledPeriodic() {
    	DriveTrain.resetTurnEncoders();   // happens only once because a flag prevents multiple calls
    	DriveTrain.disablePID();
    }
  
    @Override
    public void teleopInit() {
    	DriveAuto.disable();
    }
    
    public void testInit() {
//		double[] orientations = DriveTrain.getInstance().getAllTurnOrientations();
//		Calibration.saveSwerveCalibration(orientations[0], orientations[1], orientations[2], orientations[3]);
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
