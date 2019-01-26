package org.usfirst.frc.team2590.robot;

public interface RobotMap {

	//Drive PWMs
	public static int leftDrivePWM = 0;
	public static int rightDrivePWM = 1;
	
	//Arm
	public static int armPWM = 2;
	public static int armPOTENTIOMETER = 1;
	public static int armLowerLimitSwitch = 0;
	public static int armUpperLimitSwitch = 70;
	
	//Cannon
	//public static int chargeSolenoid = 1; //Check what this is
	public static int cannonSolenoid = 0; //relay
	//public static int cannonArmPWM = 0; //cant find if this is used anywhere
	
	//Revolver 
	public static int revolverPWM = 3;
	public static int lockerSolenoid = 0; //solenoid, PCM
	public static int revolverEncoderA = 0;
	public static int revolverEncoderB = 1;

	//Horn
	public static int hornSolenoid = 1;
	  
	//Looper Cycle Times
	public static double enabledLooperCycle = 0.01;
	public static double disabledLooperCycle = 0.1;
	  
	//Joystick Settings
	public static double joystickXDead = 0.1;
	public static double joystickYDead = 0.1;
	
}
