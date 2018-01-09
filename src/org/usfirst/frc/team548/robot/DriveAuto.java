package org.usfirst.frc.team548.robot;

import com.coderedrobotics.libs.PIDSourceFilter;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAuto {
//	private PIDControllerAIAO drivePID;
//    private PIDControllerAIAO rotDrivePID;
	private PIDController drivePID;
    private PIDController rotDrivePID;

    private AHRS gyro;
    private double minDriveStartPower = .1;

    private double maxPowerAllowed = 1;
    private double curPowerSetting = 1;
    
    public void DriveAuto() {
    	DriveTrain.getInstance();
    	this.gyro = DriveTrain.getgyro();
    	PIDSourceFilter pidInputForDrive; 

    	pidInputForDrive = new PIDSourceFilter((double value) -> -(DriveTrain.getDriveEnc()));
    	
//       drivePID = new PIDControllerAIAO(0, 0, 0, pidInputForDrive, speed -> DriveTrain.autoSetDrive(speed), false, "autodrive");
//       rotDrivePID = new PIDControllerAIAO(Calibration.AUTO_ROT_P, Calibration.AUTO_ROT_I, Calibration.AUTO_ROT_D, gyro, rot -> DriveTrain.autoSetRot(rot), false, "autorot (gyro)");
       drivePID = new PIDController(0, 0, 0, pidInputForDrive, speed -> DriveTrain.autoSetDrive(speed));
       rotDrivePID = new PIDController(Calibration.AUTO_ROT_P, Calibration.AUTO_ROT_I, Calibration.AUTO_ROT_D, gyro, rot -> DriveTrain.autoSetRot(rot));

       drivePID.setAbsoluteTolerance(Calibration.DRIVE_DISTANCE_TICKS_PER_INCH);  // 1" tolerance
       rotDrivePID.setAbsoluteTolerance(1.5);  // degrees off 
       
       //rotDrivePID.setToleranceBuffer(3);        
       //drivePID.setToleranceBuffer(3); 
       
       drivePID.setSetpoint(0);
       drivePID.reset();    
       
    }
    
	public void driveInches(double inches, double angle,  double maxPower, double startPowerLevel) {
        maxPowerAllowed = maxPower;
        curPowerSetting = startPowerLevel;  // the minimum power required to start moving.  (Untested)

        SmartDashboard.putNumber("DRIVE INCHES", inches);
        
        setPowerOutput(curPowerSetting);
        
        DriveTrain.setTurnOrientation(DriveTrain.angleToLoc(angle), DriveTrain.angleToLoc(angle),
        		DriveTrain.angleToLoc(angle), DriveTrain.angleToLoc(angle));

        drivePID.setSetpoint(drivePID.getSetpoint() + convertToTicks(inches));
    }
    
    public void driveInches(double inches, double angle,  double maxPower) {
    	driveInches(inches, angle, maxPower, minDriveStartPower);
    }

    public void reset() {
    	drivePID.reset();
    	drivePID.setSetpoint(0);
    	rotDrivePID.reset();
    	rotDrivePID.setSetpoint(0);
    	gyro.reset();
     	drivePID.enable();
    	rotDrivePID.enable();
    }
    
    public void stop() {
    	drivePID.setSetpoint(drivePID.get());
    	rotDrivePID.setSetpoint(rotDrivePID.get());
    }
    
    public void turnDegrees(double degrees, double maxPower) {
    	// Turns using the Gyro, relative to the current position
    	// Use "turnCompleted" method to determine when the turn is done

    	SmartDashboard.putNumber("TURN DEGREES CALL", degrees);
    	
    	maxPowerAllowed = maxPower;
       	curPowerSetting = .18;
            	
        rotDrivePID.setSetpoint(rotDrivePID.getSetpoint() + degrees);
        
        setPowerOutput(curPowerSetting);
    }

    public void tick() {
    	// this is called roughly 50 times per second
    	
    	// check for ramping up
        if (curPowerSetting < maxPowerAllowed) {  // then increase power a notch 
            curPowerSetting += .02; // was .007 evening of 4/5 // to figure out how fast this would be, multiply by 50 to see how much it would increase in 1 second.
            if (curPowerSetting > maxPowerAllowed) {
            	curPowerSetting = maxPowerAllowed;
            }
        }
        // now check if we're ramping down
        if (curPowerSetting > maxPowerAllowed) {
        	curPowerSetting -= .03; 
        	if (curPowerSetting < 0) {
        		curPowerSetting = 0;
        	}
        }
        setPowerOutput(curPowerSetting);
        
        SmartDashboard.putNumber("CurPower", curPowerSetting);

        rotDrivePID.setPID(SmartDashboard.getNumber("ROT P",Calibration.AUTO_ROT_P), SmartDashboard.getNumber("ROT I", Calibration.AUTO_ROT_I), SmartDashboard.getNumber("ROT D", Calibration.AUTO_ROT_D));
        drivePID.setPID(SmartDashboard.getNumber("AUTO DRIVE P", Calibration.AUTO_DRIVE_P), SmartDashboard.getNumber("AUTO DRIVE I", Calibration.AUTO_DRIVE_I), SmartDashboard.getNumber("AUTO DRIVE D", Calibration.AUTO_DRIVE_D));
       // drivePID.setSetpoint(SmartDashboard.getNumber("AUTO SETPOINT", 0));
    }

    private void setPowerOutput(double powerLevel) {
        drivePID.setOutputRange(-powerLevel, powerLevel);
        rotDrivePID.setOutputRange(-powerLevel, powerLevel);
    }

    public void setMaxPowerOutput(double maxPower) {
        maxPowerAllowed = maxPower;
        // "tick" will take care of implementing this power level
    }

    public double getDistanceTravelled() {
        return Math.abs(convertTicksToInches(DriveTrain.getDriveEnc()));
    }

    public boolean hasArrived() {
        return drivePID.onTarget() && rotDrivePID.onTarget();
    }

    public boolean turnCompleted() {
        return hasArrived();
    }

    public void setPIDstate(boolean isEnabled) {
        if (isEnabled) {
            drivePID.enable();
            rotDrivePID.enable();
        } else {
            drivePID.disable();
            rotDrivePID.disable();
        }
    }

    private int convertToTicks(double inches) {
        return (int) (inches * Calibration.DRIVE_DISTANCE_TICKS_PER_INCH);
    }

    private double convertTicksToInches(int ticks) {
        return ticks / Calibration.DRIVE_DISTANCE_TICKS_PER_INCH;
    }

    public void showEncoderValues() {
        SmartDashboard.putNumber("Drive PID Setpoint: ", drivePID.getSetpoint());
        SmartDashboard.putNumber("Drive PID Get: ", drivePID.get());
        SmartDashboard.putNumber("Drive PID Error: ", drivePID.getError());
        SmartDashboard.putBoolean("Drive On Target", drivePID.onTarget());
        
        SmartDashboard.putNumber("Gyro", round2(gyro.getAngle()));
        SmartDashboard.putNumber("Gyro PID Setpoint", rotDrivePID.getSetpoint());
        SmartDashboard.putNumber("Gyro PID error", round2(rotDrivePID.getError()));

  //      SmartDashboard.putNumber("Left Drive Encoder Raw: ", -mainDrive.getLeftEncoderObject().get());
  //      SmartDashboard.putNumber("Right Drive Encoder Raw: ", -mainDrive.getRightEncoderObject().get());
       
        //		SmartDashboard.putNumber("Right PID error", rightDrivePID.getError());
        //   	SmartDashboard.putNumber("Left Drive Encoder Get: ", mainDrive.getLeftEncoderObject().get());
        //  	SmartDashboard.putNumber("Right Drive Encoder Get: ", mainDrive.getRightEncoderObject().get());
        //     	SmartDashboard.putNumber("Left Drive Distance: ", leftEncoder.getDistance());
        //     	SmartDashboard.putNumber("Right Drive Distance: ", rightEncoder.getDistance());
        //		SmartDashboard.putNumber("Right Drive Encoder Raw: ", rightEncoder.getRaw());
        //		SmartDashboard.putNumber("Right Setpoint: ", rightDrivePID.getSetpoint());

    }
    
    private Double round2(Double val) { 
    	return val;
    	//new BigDecimal(val.toString()).setScale(2,RoundingMode.HALF_UP).doubleValue(); 
    	}
}
