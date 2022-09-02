/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boid;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Boid class creates Boid objects which follow the behaviour of boids
 *
 * @author breco
 */
public class Boid implements Runnable {

    //The current x and y location of the boid.
    protected double x, y;
    //The current x and y velocity of the boid.
    protected double dx, dy;
    //The current state of the boid.
    protected boolean isAlive;
    //The array to hold the colors of each boid.
    protected Color[] colour;
    //The flock the boid are attached to.
    protected BoidFlock flock;

    //The x y dimensions of the world.
    static int WORLD_WIDTH;
    static int WORLD_HEIGHT;

    //Size of the boid
    static int BOID_SIZE = 30;
    //The raidus for locating neighbours
    static int RADIUS_DETECTION = 5;

    static float COHESION_WEIGHT = (float) 0.1;
    static float SEPARATION_WEIGHT = (float) 0.1;
    static float ALIGNMENT_WEIGHT = (float) 0.1;
    //Limits how fast boids can move.
    static float MAX_SPEED = 5;

    public Boid(BoidFlock flock) {

        //Random generation
        Random rand = new Random();

        //Boid flock
        this.flock = flock;
        flock.addBoid(this);

        //Set the boids starting position
        setPositionX(rand.nextInt(WORLD_WIDTH));
        setPositionY(rand.nextInt(WORLD_HEIGHT));

        //Set the boids movement speeds
        setMovementX(rand.nextInt(2 * (int) MAX_SPEED + 1) - MAX_SPEED);
        setMovementY(rand.nextInt(2 * (int) MAX_SPEED + 1) - MAX_SPEED);

        //Prevents frozen boids
        if (getMovementX() == 0 && getMovementY() == 0) {
            setMovementX(0.1);
            setMovementY(0.1);
        }

        //Establish the 3 colors of the boid.
        colour = new Color[3];
        colour[0] = Color.getHSBColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        colour[1] = Color.getHSBColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        colour[2] = Color.getHSBColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());

    }

    /**
     * Set is alive to false to end the thread.
     */
    public void requestStop() {
        this.isAlive = false;
    }

    /**
     * Get separation Velocity in the x direction
     *
     * @param group
     * @return
     */
    public double getvsX(List<Boid> group) {
        double vsX = 0;
        //for the whole flock sum sepration x
        for (Boid i : group) {
            if (i == this) {
                //Avoid this as you can divide by zero when there is a flock size of 1
            } else {
                vsX += (getPositionX() - i.getPositionX()) / sqrt(pow(getPositionX() - i.getPositionX(), 2) + pow(getPositionY() - i.getPositionY(), 2));

            }

        }
        return vsX;
    }

    /**
     * Get separation Velocity in the y direction
     *
     * @param group
     * @return
     */
    public double getvsY(List<Boid> group) {
        double vsY = 0;
        //for the whole flock sum sepration y
        for (Boid i : group) {
            if (i == this) {
                //Avoid this as you can divide by zero when there is a flock size of 1
            } else {

                vsY += (getPositionY() - i.getPositionY()) / sqrt(pow(getPositionX() - i.getPositionX(), 2) + pow(getPositionY() - i.getPositionY(), 2));

            }

        }
        return vsY;
    }

    /**
     * Get Cohesion velocity in the x direction
     *
     * @param group
     * @return
     */
    public double getvcX(List<Boid> group) {
        double centre = 0;
        int count = 0;
        double vcX;
        //Sum centre position
        for (Boid i : group) {

            centre += i.getPositionX();
            count++;

        }

        //Current x centre point
        centre = centre / count;

        //Current Vx after choesion
        vcX = centre - getPositionX();
        return vcX;
    }

    /**
     * Get Cohesion velocity in the y direction
     *
     * @param group
     * @return
     */
    public double getvcY(List<Boid> group) {
        double centre = 0;
        int count = 0;
        double vcY;
        //Cycling through calculating cohesion for y
        for (Boid i : group) {

            centre += i.getPositionY();
            count++;
        }
        //Current y centre point
        centre = centre / count;
        //Current Vx after choesion
        vcY = centre - getPositionY();
        return vcY;
    }

    /**
     * Get Alignment velocity in the x direction
     *
     * @param group
     * @return
     */
    public double getvaX(List<Boid> group) {
        double vaX;
        double vAVG = 0;
        double count = 0;
        //for the whole flock sum x velocity
        for (Boid i : group) {
            vAVG += i.getMovementX();
            count++;
        }

        //find avg x velocity
        vAVG = vAVG / count;

        //Current Vx after allignment
        vaX = vAVG - getMovementX();

        return vaX;
    }

    /**
     * Get Alignment velocity in the y direction
     *
     * @param group
     * @return
     */
    public double getvaY(List<Boid> group) {
        double vaY;
        double vAVG = 0;
        double count = 0;
        //for the whole flock sum x velocity
        for (Boid i : group) {
            vAVG += i.getMovementY();
            count++;
        }

        //find avg x velocity
        vAVG = vAVG / count;

        //Current Vx after allignment
        vaY = vAVG - getMovementY();
        return vaY;
    }

    public void avoidRocks(List<Rock> rocks) {
      
        double xAvg = 0;
        double yAvg = 0;

        for (Rock r : rocks) {
            if (r.getX() > this.x) {
                xAvg--;
            }
            if (r.getX() < this.x) {
                xAvg++;
            }
            if (r.getY() > this.y) {
                yAvg++;
            }
            if (r.getY() < this.y) {
                yAvg--;
            }
            

        }

        dx = this.dx - xAvg;
        dy = this.dy - yAvg;

        //Total velocity
        double velocity = sqrt(pow(dx, 2) + pow(dy, 2));

        //Slight velcoity increase to avoid flocks staying slow
        dx = dx * 1.1;
        dy = dy * 1.1;

        //Clamped velocity
        if (abs(velocity) > MAX_SPEED) {
//                    System.out.println("Clamp");
            dx = dx / velocity * MAX_SPEED;
            dy = dy / velocity * MAX_SPEED;
        }
        
        
    }

    @Override
    public void run() {

        //Turn it on
        isAlive = true;
        //Random generation
        Random rand = new Random();
        int perodicMove = 0;
        double random;
        //While the boid is alive bounce around the room
        while (isAlive) {
            perodicMove++;

            //Random periodic noise not exceding Max_Speed
            if (perodicMove > 300) {
                //Random speed within bounds.
                random = (rand.nextInt(2 * (int) MAX_SPEED + 1) - MAX_SPEED) * 0.1;

                if (dx + random > MAX_SPEED || dx + random < -MAX_SPEED) {
                    dx -= random;
                } else {
                    dx += random;
                }
                //Random y speed
                random = (rand.nextInt(2 * (int) MAX_SPEED + 1) - MAX_SPEED) * 0.1;
                if (dy + random > MAX_SPEED || dy + random < -MAX_SPEED) {
                    dy -= random;
                } else {
                    dy += random;
                }

                perodicMove = 0;
            }

            //If the boid is out of bounds in the y direction set it to be on the verry limit and change velocity direction
            if (y > WORLD_HEIGHT || y < 0) {

                if (y < 0) {
                    y = 0;
                }
                if (y > WORLD_HEIGHT) {
                    y = WORLD_HEIGHT;

                }

                //Switch the direction of velocity
                dy -= 2 * dy;

            }

            //If the boid is out of bounds in the x direction set it to be on the verry limit and change velocity direction
            if (x > WORLD_WIDTH || x < 0) {
                //Switch the direction of velocity
                if (x < 0) {
                    x = 0;
                }
                if (x > WORLD_WIDTH) {
                    x = WORLD_WIDTH;
                }
                dx -= 2 * dx;
            }

            //Add velocity to current position added extra multiplication so 
            //boids  do not clump and slow
            x += dx;
            y += dy;

            //If the boid is within the bounds apply velocity vectors
            if (!(y > WORLD_HEIGHT || y < 0 || x > WORLD_WIDTH || x < 0)) {

                //Get the neighbours of current boid.
                List<Boid> currentFlock = flock.getNeighbours(this);
                List<Rock> currentRocks = flock.getRocks(this);

                if (currentRocks.size() > 0) {
                    this.avoidRocks(currentRocks);
                } else { //Velocity seperation in x
                    double vsX = getvsX(currentFlock);
                    //Velocity seperation in y
                    double vsY = getvsY(currentFlock);
                    //Velocity allignment in x
                    double vaX = getvaX(currentFlock);
                    //Velocity allignment in y
                    double vaY = getvaY(currentFlock);
                    //Velocity cohesion in x
                    double vcX = getvcX(currentFlock);
                    //Velocity cohesion in y
                    double vcY = getvcY(currentFlock);

                    //multiply seperation,allignment and cohesion by static fields
                    vsX = vsX * SEPARATION_WEIGHT;
                    vsY = vsY * SEPARATION_WEIGHT;
                    vaX = vaX * ALIGNMENT_WEIGHT;
                    vaY = vaY * ALIGNMENT_WEIGHT;
                    vcX = vcX * COHESION_WEIGHT;
                    vcY = vcY * COHESION_WEIGHT;

                    //Velocity in x and y
                    double vx = (getMovementX() + vsX + vaX + vcX);
                    double vy = (getMovementY() + vsY + vaY + vcY);

                    //Total velocity
                    double velocity = sqrt(pow(vx, 2) + pow(vy, 2));

                    //Slight velcoity increase to avoid flocks staying slow
                    vx = vx * 1.1;
                    vy = vy * 1.1;

                    //Clamped velocity
                    if (abs(velocity) > MAX_SPEED) {
//                    System.out.println("Clamp");
                        vx = vx / velocity * MAX_SPEED;
                        vy = vy / velocity * MAX_SPEED;
                    }

                    //Vfinal
                    dx = vx;
                    dy = vy;
                }

            }

            try {
                Thread.sleep(20);

            } catch (InterruptedException ex) {
                Logger.getLogger(Boid.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public double getPositionX() {
        return x;
    }

    public double getPositionY() {
        return y;
    }

    public double getMovementX() {
        return dx;
    }

    public double getMovementY() {
        return dy;
    }

    public void setPositionX(double x) {
        this.x = x;
    }

    public void setPositionY(double y) {
        this.y = y;
    }

    /**
     * Sets the velocity in the x direction with a cap of + or - max speed
     *
     * @param dx Velocity in x direction.
     */
    public void setMovementX(double dx) {

        this.dx = dx;

    }

    /**
     * Sets the velocity in the y direction with a cap of + or - max speed
     *
     * @param dy Velocity in y direction.
     */
    public void setMovementY(double dy) {

        this.dy = dy;
    }

    public void draw(Graphics g) {

        if (isAlive) {

            //Speed is equal sqrt(dx^2 + dy^2)
            double speed = sqrt(pow(getMovementX(), 2) + pow(getMovementY(), 2));
            double velX = ((BOID_SIZE * getMovementX()) / (2 * speed));
            double velY = ((BOID_SIZE * getMovementY()) / (2 * speed));
            //Boid drawing

            Graphics2D stroke = (Graphics2D) g;
            stroke.setStroke(new BasicStroke(4));
            stroke.setColor(colour[0]);
            stroke.drawLine((int) getPositionX(), (int) getPositionY(), (int) (getPositionX() - velX + velY), (int) (getPositionY() - velX - velY));
            stroke.setColor(colour[1]);
            stroke.drawLine((int) getPositionX(), (int) getPositionY(), (int) (getPositionX() - 2 * velX), (int) (getPositionY() - velY * 2));
            stroke.setColor(colour[2]);
            stroke.drawLine((int) getPositionX(), (int) getPositionY(), (int) (getPositionX() - velX - velY), (int) (getPositionY() + velX - velY));

        }

    }

}
