import java.util.*;
import rxtxrobot.*;
public class Robot {
    
    public static RXTXRobot sensors = new ArduinoNano();
    public static RXTXRobot motors = new ArduinoNano();
    public static double angle = 90.0;
    public static final int servo1Pin = 7;
    public static final int servo2Pin = 10;
    public static final int servo3Pin = 4;
    public static final int frontPingPin = 8;
    public static final int leftPingPin = 9;
    public static final int rightPingPin = 11;
    public static final int shielded = 0; // thermistor(temperature)
    public static final int exposed = 1; // thermistor(temperature)
    public static final double MOTOR1_RATIO = 0.67;
    public static final double MOTOR2_RATIO = 1.0;
    public static final double ENCODER1_RATIO = 0.85;
    public static final double ENCODER2_RATIO = 1.0;
    public static final double ticksPerInch = 7.16197243913529;
    public static Scanner kb = new Scanner(System.in);
    
    /*
    public static void main(String[] args) { // TESTING
        attachEverything();
        
        sensors.moveServo(RXTXRobot.SERVO2, 156);
        sensors.moveServo(RXTXRobot.SERVO1, 180);
        sensors.moveServo(RXTXRobot.SERVO3, 180);
        int option = -1;
        while (option != 0) {
            System.out.println("\nSelect an operation: \n"
                + "0:  Exit\n"
                + "1:  CONDUCTIVITY\n"
                + "2:  PING\n"
                + "3:  MOTOR\n"
                + "4:  ELEVATED ARM\n"
                + "5:  SPIN\n"
                + "6:  BUMP\n"
                + "7:  CRYSTAL\n"
                + "8:  WIND SPEED\n");
            
            System.out.print("Choice: ");
            option = kb.nextInt();
            
            switch(option) {
                
                case 0: // EXIT
                    break;
                                   
                case 1: // CONDUCTIVITY
                    sensors.moveServo(RXTXRobot.SERVO1, 0);
                    getConductivity(1);
                    sensors.moveServo(RXTXRobot.SERVO1, 180);
                    break;
                    
                case 2: // PING
                    for(int k = 0; k < 30; k++) {
                        getPing(leftPingPin, 1);
                        sensors.sleep(300);
                    }
                    //driveUntilPing(ping1Pin, 50);
                    //int[] pings = scanPing();
                    //driveToPing(pings);
                    break;
                    
                case 3: // MOTOR
                    runEncodedMotors(250, 2000);
                    //runTimedMotors(250, 6000);
                    //spin(55);
                    //runEncodedMotor(250);
                    break;
                    
                case 4: // ELEVATED ARM
                    sensors.moveServo(RXTXRobot.SERVO2, 48);
                    double exposedTemp = getTemperature(exposed);
                    double shieldedTemp = getTemperature(shielded);
                    System.out.println("Unshielded temp is: " + exposedTemp + " degrees C");
                    System.out.println("Shielded temp is: " + shieldedTemp + " degrees C");
                    sensors.moveServo(RXTXRobot.SERVO2, 156);
                    break;
                    
                case 5: // SPIN
                    spin(90);
                    break;
                    
                case 6: // BUMP
                    for (int i=0; i<100; i++) {
                        sensors.refreshAnalogPins();
                        System.out.println("Bump sensor reading: " 
                                + sensors.getAnalogPin(2).getValue());
                    }
                    break;
                    
                case 7: // CRYSTAL
                    sensors.moveServo(RXTXRobot.SERVO3, 0);
                    break;
                    
                case 8: // WIND SPEED
                    double tempDiff = getWindSpeed(shielded, exposed);
                    System.out.println("Wind speed is: " + tempDiff + " m/s");
                    break;
                    
                default:
                    System.out.println("Invalid input");
                    break;
            } // end switch
        } // end while       
        
        sensors.close();
        motors.close();
    } // end main */

    // /*
    public static void main(String args[]) { // FINAL COMPETITION
        attachEverything();
        sensors.moveServo(RXTXRobot.SERVO2, 156);
        sensors.moveServo(RXTXRobot.SERVO1, 180);
        sensors.moveServo(RXTXRobot.SERVO3, 180);
        
        Scanner kb = new Scanner(System.in);
        System.out.print("1 - NE\n"
                + "2 - NW\n"
                + "3 - SW\n"
                + "4 - SE\n"
                + "5 - HALFWAY NW\n"
                + "6 - SEMI AUTONOMOUS\n"
                + "Aite doe what box u in fam?: ");
        int quadrant = kb.nextInt();
        
        switch (quadrant) {
            case 1: // NE
                runEncodedMotors(300, bricksToTicks(11)); // leave box
                spin(80);
                runEncodedMotors(300, bricksToTicks(30)); // drive to ramp
                spinCC(45);
                runEncodedMotors(300, bricksToTicks(10));
                sensors.moveServo(RXTXRobot.SERVO2, 48);
                double exposedTemp = getTemperature(exposed);
                double shieldedTemp = getTemperature(shielded);
                System.out.println("Unshielded temp is: " + exposedTemp + " degrees C");
                System.out.println("Shielded temp is: " + shieldedTemp + " degrees C");
                double tempDiff = getWindSpeed(shielded, exposed);
                System.out.println("Wind speed is: " + tempDiff + " m/s");
                sensors.moveServo(RXTXRobot.SERVO2, 156);
                runEncodedMotors(250, bricksToTicks(10)); // drive down ramp
                spinCC(135);
                runEncodedMotors(300, bricksToTicks(13)); // drive down ramp
                spin(90);
                runEncodedMotors(300, bricksToTicks(18)); // drive down ramp
                sensors.moveServo(RXTXRobot.SERVO1, 0);
                getConductivity(1);
                sensors.moveServo(RXTXRobot.SERVO1, 180);
                runEncodedMotors(-250, bricksToTicks(2)); // back away from sand
                spin(90);
                runEncodedMotors(250, bricksToTicks(26)); // exit field
                spin(90);
                runEncodedMotors(250, bricksToTicks(29)); // drive across outside of field
                spin(90);
                runEncodedMotors(250, bricksToTicks(10)); // enter field
                spinCC(90);
                runEncodedMotors(250, bricksToTicks(7)); // enter charge station
                
                break;
            case 2: // NW
                runEncodedMotors(250, bricksToTicks(9)); // leave area
                spinCC(45);
                runEncodedMotors(300, bricksToTicks(12)); // go up ramp
                sensors.moveServo(RXTXRobot.SERVO2, 48);
                exposedTemp = getTemperature(exposed);
                shieldedTemp = getTemperature(shielded);
                System.out.println("Unshielded temp is: " + exposedTemp + " degrees C");
                System.out.println("Shielded temp is: " + shieldedTemp + " degrees C");
                tempDiff = getWindSpeed(shielded, exposed);
                System.out.println("Wind speed is: " + tempDiff + " m/s");
                sensors.moveServo(RXTXRobot.SERVO2, 156);
                runEncodedMotors(200, bricksToTicks(12)); // go down ramp and to sand
                spin(45);
                runEncodedMotors(200, bricksToTicks(16));
                sensors.moveServo(RXTXRobot.SERVO1, 0);
                getConductivity(1);
                sensors.moveServo(RXTXRobot.SERVO1, 180);
                runEncodedMotors(-300, bricksToTicks(2)); // back away from sand
                spinCC(80);
                runEncodedMotors(300, bricksToTicks(7));
                spin(85);
                runEncodedMotors(300, bricksToTicks(23)); // drive laterally to charge
                spinCC(82);
                runEncodedMotors(300, bricksToTicks(37)); 
                spin(75);
                runEncodedMotors(300, bricksToTicks(9)); // enter charge station
                break; 
            
            case 3: // SW
                runEncodedMotors(300, bricksToTicks(12)); // leave box
                spin(83);
                runEncodedMotors(200, bricksToTicks(9)); // go toward sand
                sensors.moveServo(RXTXRobot.SERVO1, 0);
                getConductivity(1);
                sensors.moveServo(RXTXRobot.SERVO1, 180);
                runEncodedMotors(-250, bricksToTicks(1)); // leave sand
                spinCC(90);
                //runEncodedMotors(300, bricksToTicks(10)); // go toward ramp
                //spin(87);
                //runEncodedMotors(300, bricksToTicks(2)); // avoid barrier
                //spinCC(87);
                runEncodedMotors(300, bricksToTicks(32)); // go up ramp
                sensors.moveServo(RXTXRobot.SERVO2, 48);
                exposedTemp = getTemperature(exposed);
                shieldedTemp = getTemperature(shielded);
                System.out.println("Unshielded temp is: " + exposedTemp + " degrees C");
                System.out.println("Shielded temp is: " + shieldedTemp + " degrees C");
                tempDiff = getWindSpeed(shielded, exposed);
                System.out.println("Wind speed is: " + tempDiff + " m/s");
                sensors.moveServo(RXTXRobot.SERVO2, 156);
                runEncodedMotors(200, bricksToTicks(20)); // drive down ramp
                spinCC(90);
                runEncodedMotors(300, bricksToTicks(12)); // enter charge station                
                break;
            
            case 4: // SE
                runEncodedMotors(300, bricksToTicks(29)); // exit charge
                spinCC(70);
                runEncodedMotors(300, bricksToTicks(12)); // go toward sand
                sensors.moveServo(RXTXRobot.SERVO1, 0);
                getConductivity(1);
                sensors.moveServo(RXTXRobot.SERVO1, 180);
                runEncodedMotors(-300, bricksToTicks(5)); // leave sand
                spin(85);
                runEncodedMotors(300, bricksToTicks(23)); // drive toward ramp
                //spin(90);
                //runEncodedMotors(300, bricksToTicks(7)); // drive up ramp
                sensors.moveServo(RXTXRobot.SERVO2, 48);
                exposedTemp = getTemperature(exposed);
                shieldedTemp = getTemperature(shielded);
                System.out.println("Unshielded temp is: " + exposedTemp + " degrees C");
                System.out.println("Shielded temp is: " + shieldedTemp + " degrees C");
                tempDiff = getWindSpeed(shielded, exposed);
                System.out.println("Wind speed is: " + tempDiff + " m/s");
                sensors.moveServo(RXTXRobot.SERVO2, 156);
                runEncodedMotors(200, bricksToTicks(15)); // drive off ramp
                spinCC(90);
                runEncodedMotors(300, bricksToTicks(35)); // drive laterally outside field
                spinCC(90);
                runEncodedMotors(300, bricksToTicks(10)); // reenter field
                spin(90);
                runEncodedMotors(300, bricksToTicks(12)); // enter charge station
                break;
                
            case 5: // HALFWAY NW
                runEncodedMotors(-300, (int)(178*ticksPerInch)); // back away from sand
                spinCC(100);
                runEncodedMotors(300, (int)(12*29*ticksPerInch)); // go up ramp
                spinCC(85);
                runEncodedMotors(300, (int)(210*ticksPerInch)); // leave area
                break;
                
            case 6: // SEMI AUTONOMOUS
                runEncodedMotors(250, (int)(70*ticksPerInch));
                spin(90);
                runEncodedMotors(200, (int)(20*ticksPerInch));
                runMotorsForever(120);
                int startPos = motors.getEncodedMotorPosition(RXTXRobot.MOTOR2);
                int currentPos = 0;
                int frontPing = 0;
                int sidePing = 0;
                int posDifference = 0;
                do {
                    sidePing = getPing(leftPingPin, 1);
                    frontPing = getPing(frontPingPin, 1);
                    currentPos = motors.getEncodedMotorPosition(RXTXRobot.MOTOR2);
                    posDifference = currentPos - startPos;
                } while((sidePing > 40) || (posDifference > (230*ticksPerInch)) || (frontPing > 25) );
                stopMotors();
                if (posDifference <= 230*ticksPerInch) {
                    runEncodedMotors(250, (int)(10*ticksPerInch));
                    spinCC(95);
                    driveUntilPing(frontPingPin, 15, 200);
                    sensors.moveServo(RXTXRobot.SERVO1, 0);
                    getConductivity(1);
                    sensors.moveServo(RXTXRobot.SERVO1, 180);
                } else {
                    spinCC(90);
                    runEncodedMotors(250, (int)(30*ticksPerInch));
                    spinCC(90);
                    startPos = motors.getEncodedMotorPosition(RXTXRobot.MOTOR2);
                    currentPos = 0;
                    runMotorsForever(120);
                    do {
                        sidePing = getPing(rightPingPin, 1);
                        frontPing = getPing(frontPingPin, 1);
                        currentPos = motors.getEncodedMotorPosition(RXTXRobot.MOTOR2);
                        posDifference = currentPos - startPos;
                    } while((sidePing > 40) || (posDifference > (230*ticksPerInch)) || (frontPing > 25) );
                    stopMotors();
                    if (posDifference <= 230*ticksPerInch) {
                        runEncodedMotors(250, (int)(10*ticksPerInch));
                        spinCC(95);
                        driveUntilPing(frontPingPin, 15, 200);
                        sensors.moveServo(RXTXRobot.SERVO1, 0);
                        getConductivity(1);
                        sensors.moveServo(RXTXRobot.SERVO1, 180);
                    }   
                }
                
                break;
                
            default:
                System.out.println("Invalid choice");
        }
                
        sensors.close();
        motors.close();
    } // end main */
    
    
    public static void attachEverything() {
        sensors.setPort("COM4");
        sensors.connect();
        motors.setPort("COM6");
        motors.connect();
        sensors.attachServo(RXTXRobot.SERVO1, servo1Pin);
        sensors.attachServo(RXTXRobot.SERVO2, servo2Pin);
        sensors.attachServo(RXTXRobot.SERVO3, servo3Pin);
        motors.attachMotor(RXTXRobot.MOTOR1, 5);
        motors.attachMotor(RXTXRobot.MOTOR2, 6);
    }
    
    public static void celebrate() {
        int x = 1;
        motors.runMotor(RXTXRobot.MOTOR1, 200, RXTXRobot.MOTOR2, 200, 0);
        while(x == 1) {
            sensors.moveServo(RXTXRobot.SERVO1, 0);
            getTemperature(shielded, 6);
            sensors.moveServo(RXTXRobot.SERVO1, 180);
            sensors.moveServo(RXTXRobot.SERVO2, 0);
            getTemperature(shielded, 7);
            sensors.moveServo(RXTXRobot.SERVO2, 156);
        }
    }
 
    /***************************************************************************
    CONDUCTIVITY METHODS
    */
    public static double getConductivity(int numOfTests) {
        ArrayList<Double> conductReadings = new ArrayList<>();
        for (int i=0; i<numOfTests; i++) {
            double conductivity = porterConductivity(); // TODO conductivity implementation
            conductReadings.add(conductivity);
        }
        double sum = 0.0;
        for(int i = 0; i < numOfTests; i++) {
            sum += conductReadings.get(i);
        }
        System.out.print("Conductivity (avg of " + (numOfTests*9) + "): " + sum/numOfTests);
        return sum/numOfTests;
    }
    
    public static double porterConductivity() {
        int len = 2;
        double con[] = new double[len];
        double avg = 0;
        double conF;
        for(int i = -1; i < len; i++) {
            if(i != -1) {
                con[i] = sensors.getConductivity();
                avg += con[i];
            }
        }
        avg /= len;
        conF = 100 * ((-.0074 * avg) + 7.4983) - 3.9;
        return conF;
    }
        
    // end conductivity methods
    //**************************************************************************
    
    /***************************************************************************
    PING METHODS
    */    
    
    public static int getPing(int pin, int readings) {
        int pingSum = 0;
        for(int i = 0; i < readings; i++) {
            pingSum += sensors.getPing(pin);
            sensors.sleep(300);
        }
        System.out.println("Ping: " + (pingSum/readings) + "cm");
        return (pingSum/readings);
    }
    
    public static void driveUntilPing(int pin, int stopPing, int speed) {
        runMotorsForever(speed);
        while(getPing(pin, 1) > stopPing) {
        }
        stopMotors();
    }
    
    public static void driveToPing(int[] pings) {
        int minimum = Integer.MAX_VALUE;
        int index = 0;
        for (int i=0; i<pings.length; i++) {
            if (pings[i] < minimum) {
                minimum = pings[i];
                index = i;
            }
        }
        spin(index*20);
        runEncodedMotors(300, (int)(2.81967*(double)minimum));
    }
    // end ping methods
    //**************************************************************************
    
    /***************************************************************************
    MOTOR METHODS
    */
    public static void runEncodedMotors(int speed, int ticks) {
        motors.runEncodedMotor(RXTXRobot.MOTOR1, (int)(-speed*MOTOR1_RATIO), 
                (int)(ticks*ENCODER1_RATIO), RXTXRobot.MOTOR2, 
                (int)(speed*MOTOR2_RATIO), (int)(ticks*ENCODER2_RATIO));
        //motors.runEncodedMotor(RXTXRobot.MOTOR1, 200, 5);
    }
    
    public static void runEncodedMotor(int speed) {
        for(int ticks = 0; ticks < 500; ticks+=40) {
        motors.runEncodedMotor(RXTXRobot.MOTOR1, (int)(-speed*MOTOR1_RATIO), 
                (int)(ticks*ENCODER1_RATIO), RXTXRobot.MOTOR2, 
                (int)(speed*MOTOR2_RATIO), (int)(ticks*ENCODER2_RATIO));
        System.out.println("Ticks: " + sensors.getEncodedMotorPosition(RXTXRobot.MOTOR2));
        }
    }
        
    public static void runTimedMotors(int speed, int time) {
        motors.runMotor(RXTXRobot.MOTOR1, (int)(-speed*MOTOR1_RATIO), 
                RXTXRobot.MOTOR2, (int)(speed*MOTOR2_RATIO), time);
    }
    
    public static void runMotorsForever(int speed) {
        motors.runMotor(RXTXRobot.MOTOR1, (int)(-speed*MOTOR1_RATIO), 
                RXTXRobot.MOTOR2, (int)(speed*MOTOR2_RATIO), 0);
    }
    
    public static void stopMotors() {
        motors.runMotor(RXTXRobot.MOTOR1, 1, RXTXRobot.MOTOR2, 1, 1);
    }

    public static void spin(int degrees) { // Must spin clockwise
        int ticks = (int)(degrees*1.4);
        motors.runEncodedMotor(RXTXRobot.MOTOR1, 217, (int)(ticks*ENCODER1_RATIO), 
                RXTXRobot.MOTOR2, 200, (int)(ticks*ENCODER2_RATIO));
        angle += degrees;
        angle = angle%360;
    }
    
    public static void spinCC(int degrees) {
        int ticks = (int)(degrees*1.4);
        motors.runEncodedMotor(RXTXRobot.MOTOR1, -217, (int)(ticks*ENCODER1_RATIO), 
                RXTXRobot.MOTOR2, -200, (int)(ticks*ENCODER2_RATIO));
        angle += (360-degrees);
        angle = angle%360;
    }
    
    public static int bricksToTicks(int bricks) {
        return (int)((74.788*bricks) - 66.391);
    }
            
    // end motor methods
    //**************************************************************************
    
    /***************************************************************************
    TEMPERATURE METHODS
    */
    public static double getTemperature(int pin) {
        int sum = 0;
        int readingCount = 30;

        for (int i = 0; i < readingCount; i++) {
        sensors.refreshAnalogPins();
        int reading = sensors.getAnalogPin(pin).getValue();
        sum += reading;
        }
        
        int ADC = sum/readingCount;
        
        if(pin == shielded)
            return (((double)ADC - 954.6510) / -7.0344);
        if(pin == exposed)
            return (((double)ADC - 938.2759) / -7.4843);
        else {
            System.out.println("Error invalid wire color for conversion");
            return 0.0;
        }
    }
    
    public static double getTemperature(int pin, int rc) {
        int sum = 0;
        int readingCount = rc;

        for (int i = 0; i < readingCount; i++) {
        sensors.refreshAnalogPins();
        int reading = sensors.getAnalogPin(pin).getValue();
        sum += reading;
        }
        
        int ADC = sum/readingCount;
        
        if(pin == shielded)
            return (((double)ADC - 954.6510) / -7.0344); //954.6510
        if(pin == exposed)
            return (((double)ADC - 938.2759) / -7.4843);
        else {
            System.out.println("Error invalid wire color for conversion");
            return 0.0;
        }
    }
    
    public static double getWindSpeed(int pin1, int pin2) {
        double tempDif = getTemperature(pin1) - getTemperature(pin2);
        tempDif -=1.53;
        tempDif /= .449;
        return tempDif;
    }
    
    // end temperature methods
    //**************************************************************************
    
} // end class