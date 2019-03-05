import java.io.File;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Sound;
import lejos.hardware.Sounds;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.TouchAdapter;
import lejos.utility.Delay;

public class GoRobot {
	static int speed = 200;
	static TouchAdapter InnerSensor = null;
	static TouchAdapter OutterSensor = null;
	static EV3LargeRegulatedMotor InnerMotor = null;
	static EV3LargeRegulatedMotor OutterMotor = null;
	static boolean done = false;

	public static void main(String[] args) {
		EV3 brick = (EV3) BrickFinder.getDefault();
		Button.ESCAPE.addKeyListener(new lejos.hardware.KeyListener() {
			
			@Override
			public void keyReleased(Key k) {
				// TODO Auto-generated method stub
				done = true;
			}
			
			@Override
			public void keyPressed(Key k) {
				// TODO Auto-generated method stub
				
			}
		});
		try (
				EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(brick.getPort(("C")));
				EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(brick.getPort(("B")));
				EV3TouchSensor leftAdapter = new EV3TouchSensor(brick.getPort(("S3")));
				EV3TouchSensor rightAdapter = new EV3TouchSensor(brick.getPort(("S2")));) {
			
			Boolean leftOrRight = false;
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(speed);
			InnerSensor = new TouchAdapter(leftAdapter);
			OutterSensor = new TouchAdapter(rightAdapter);
			while (!done) {
				if(InnerSensor.isPressed() && !leftOrRight) {
					InnerMotor = leftMotor;
					OutterMotor = rightMotor;
					leftOrRight = true;
				}else if(OutterSensor.isPressed() &&!leftOrRight) {
					InnerSensor = new TouchAdapter(rightAdapter);
					OutterSensor = new TouchAdapter(leftAdapter);
					OutterMotor = leftMotor;
					InnerMotor = rightMotor;
					leftOrRight = true;
				}
				
				if (!InnerSensor.isPressed() && !OutterSensor.isPressed()) {
					leftMotor.forward();
					rightMotor.forward();
				} else {
					Sound.setVolume(Sound.VOL_MAX);
					Sound.playSample(new File("...\\RobloxSound.wav"), Sound.VOL_MAX);
					// Go back for 1,0s
					InnerMotor.backward();
					OutterMotor.backward();
					Delay.msDelay(1000);
					// Turn right and keep going for 1,0s
					InnerMotor.setSpeed((int)(speed * 1.5));
					OutterMotor.setSpeed(speed);
					InnerMotor.forward();
					OutterMotor.forward();
					Delay.msDelay(1000);
					// Turn into the left and smash into the wall
					InnerMotor.setSpeed(speed);
					OutterMotor.setSpeed((int)(speed * 1.5));
				}

			}

		}

	}

}


