package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import controllers.PID;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import settings.ArmSettings;

/**
 * Articulating mechanism that changes the angle of the barrel to adjust shooting range
 * @author Harsh Padhye, Chinmay Savanur
 *
 */
public class Arm implements RobotMap, ArmSettings {

	//Singleton
	private static Arm armInstance = null;
	public static Arm getArmInstance() {
		if (armInstance == null) {
			armInstance = new Arm();
		}
		return armInstance;
	}
	
	private final double TOLERANCE = 5; //Tolerance for variation from angle

	private double setpoint; //target location along the range of the arm's motion
	private double manualPower; //how quickly the arm moves manually (positive or negative)
	private Victor armVictor; //controls the vertical motion of the arm
	private PID armController; //controls the motion of the arm through Proportion, Integral, and Derivative Gains
	private AnalogPotentiometer armPot; //measures the angle of the arm

	//States that the arm could be in, either fully stopped or moving between setpoints
	private armStates armState = armStates.STATIONARY;
	private enum armStates {
		STATIONARY, AUTOMATIC, MANUAL
	}
	
	/**
	 * Constructor that initializes the Victor, Potentiometer, and PID Controller
	 */
	public Arm() {
		armVictor = new Victor(armPWM);
		armPot = new AnalogPotentiometer(armPOTENTIOMETER, 360, 0);
		armController = new PID(armKP, armKI, armKD, armKR);
		
	}
	
	/**
	 * Changes state of the arm according to input from controller
	 */
	public void update() {
		System.out.println("ANGLE :: " + armPot.get()); //outputs angle of arm
		switch(armState) {
			case AUTOMATIC: //state where shoulder is moving
				//armVictor.set(armController.getOutput(armPot.get(), 0.00, 0.01)); //sets the power of the motor to move the arm to desired setpoint
				
				//if(Math.abs(armPot.get() - setpoint) < TOLERANCE || !allowedToMove()) {
					//armState = armStates.STATIONARY;
				//}
				break;
			case MANUAL:
				armVictor.set(manualPower);
				
				//code stop switch
				if(!allowedToMove()) {
					armState = armStates.STATIONARY;
				}
				
				break;
			case STATIONARY: //state where shoulder is not moving
				armVictor.set(0);
				break;
		}
		
	}
	
	/**
	 * Moves the arm from the current setpoint to the next setpoint
	 * @param nextSetpoint: the new setpoint for the arm
	 */
	public void setSetpoint(double nextSetpoint) {
		if(nextSetpoint > armMin && nextSetpoint < armMax) {
			setpoint = nextSetpoint;
			armState = armStates.AUTOMATIC;
		}
	}
	
	public void setStationary() {
		armState = armStates.STATIONARY;
	}
	
	public void liftManually(double power){
		manualPower = Math.abs(power); //ensures that no goon inserts a negative parameter erroneously
		armState = armStates.MANUAL;
	}
	
	public void lowerManually(double power){
		manualPower = -Math.abs(power); //ensures that no goon inserts a positive parameter erroneously
		armState = armStates.MANUAL;
	}

	/**
	 * Checks if arm is allowed to move given distance from min/max
	 * @return if above tolerance for min and below tolerance for max
	 */
	private boolean allowedToMove() {
		boolean angleOutsideToleranceOfMin = Math.abs(armPot.get() - armMin) > TOLERANCE;
		boolean angleOutsideToleranceOfMax = Math.abs(armMax - armPot.get()) > TOLERANCE;
		return angleOutsideToleranceOfMin && angleOutsideToleranceOfMax;
	}
}
