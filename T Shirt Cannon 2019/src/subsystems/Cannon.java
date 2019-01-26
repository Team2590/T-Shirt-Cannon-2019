package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import settings.CannonSettings;

/**
 * Mechanism that shoots the T-shirts from the barrel
 * 
 * @author Harsh Padhye, Chinmay Savanur
 *
 */
public class Cannon implements RobotMap, CannonSettings {

	// singleton
	private static Cannon cannonInstance = null;

	public static Cannon getCannonInstance() {
		if (cannonInstance == null) {
			cannonInstance = new Cannon();
		}
		return cannonInstance;
	}

	private Relay cannonPiston;

	int cycleCount;

	private cannonStates cannonState = cannonStates.IDLE;

	private enum cannonStates {
		IDLE, FIRING
	}

	public Cannon() {
		cannonPiston = new Relay(cannonSolenoid);
		cycleCount = 0;
	}

	public void update() {
		switch (cannonState) {
		// Recharging, or simply idle (not firing)
		case IDLE:
			cannonPiston.set(Value.kOff);
			//System.out.println("Closing Relay");
			cycleCount = 0;
			break;

		// fire in the hole!
		case FIRING:
			// shoots the cannon
			cannonPiston.set(Value.kForward);

			// honk the horn
			// Robot.getHornInstance().startHonking();

			// sets the cannon to idle as it recharges
			if (cycleCount < cycleMax) {
				cycleCount++;
				System.out.println(cycleCount);
			} else {
				cannonState = cannonStates.IDLE;
			}
			break;
		}
	}

	/**
	 * sets the state to firing, fires cannon
	 */
	public void fireCannon() {
		cannonState = cannonStates.FIRING;
		System.out.println("Opening Relay");
	}

	/**
	 * In case of emergency, sets cannon to idle
	 */
	public void setIdle() {
		cannonState = cannonStates.IDLE;
	}
}
