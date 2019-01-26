package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;
import org.usfirst.frc.team2590.utils.DelayedBoolean;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import settings.HornSettings;

/**
 * Mechanism that provides a warning to nearby people to avoid injury
 * 
 * @author Harsh Padhye, Chinmay Savanur
 *
 */
public class Horn implements RobotMap, HornSettings {

	// singleton
	private static Horn hornInstance = null;

	public static Horn getHornInstance() {
		if (hornInstance == null) {
			hornInstance = new Horn();
		}
		return hornInstance;
	}

	private hornStates hornState = hornStates.SILENT;

	private enum hornStates {
		SILENT, HONK, SHORT_HONK, LONG_HONK
	}

	private Relay honkSolenoid; // the solenoid which flows air into the horn
	private DelayedBoolean timeToHonk; // the amount of time until the honk ends

	public Horn() {
		timeToHonk = new DelayedBoolean(0);
		honkSolenoid = new Relay(hornSolenoid);
	}

	public void update() {
		switch (hornState) {

		// no noise
		case SILENT:
			honkSolenoid.set(Value.kOff);
			//timeToHonk.resetBoolean();
			break;

		// A quick squeak
		case HONK:
			honkSolenoid.set(Value.kForward);
			break;

		// start the horn, wait a small amount of time, stop it
		case SHORT_HONK:
			timeToHonk.setDelayTime(shortHonk);
			handleVariableHonk();
			break;

		// start the horn, wait a long amount of time, stop it
		case LONG_HONK:
			timeToHonk.setDelayTime(longHonk);
			handleVariableHonk();
			break;

		default:
			break;
		}
	}

	/**
	 * Stops the horn honking
	 */
	public void stopHonking() {
		hornState = hornStates.SILENT;
	}

	/**
	 * starts honking unless told otherwise
	 */
	public void startHonking() {
		hornState = hornStates.HONK;
	}

	/**
	 * a quick honk
	 */
	public void shortHonk() {
		hornState = hornStates.SHORT_HONK;
	}

	/**
	 * A longer honk
	 */
	public void longHonk() {
		hornState = hornStates.LONG_HONK;
	}

	/**
	 * Starts honking for an x amount of time
	 */
	private void handleVariableHonk() {
		//honkSolenoid.set(Value.kOn);
		if (timeToHonk.isReady()) {
			hornState = hornStates.SILENT;
		}
	}

}
