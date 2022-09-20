/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boid;

import static boid.Boid.BOID_SIZE;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author breco
 */
public class Rock implements Runnable {

    //Width and heigh
    private int x;
    private int y;
    public double dx, dy;
    

    //The x y dimensions of the world.
    private int height;
    private int width;
    //The x y dimensions of the world.
    static int WORLD_WIDTH;
    static int WORLD_HEIGHT;

    public boolean isAlive;

    public Rock() {
        isAlive = true;
        Random rand = new Random();
        //Set the boids starting position
        setX(rand.nextInt(WORLD_WIDTH));
        setY(rand.nextInt(WORLD_HEIGHT));
        width = 10;
        height = 10;
        dx = rand.nextInt(5) - rand.nextInt(4);
        dy = rand.nextInt(4) - rand.nextInt(5);
        if (dx == 0) {
            dx = 1;
        }
        if (dy == 0) {
            dy = 1;
        }
    }

    public void draw(Graphics g) {

        Graphics2D stroke = (Graphics2D) g;
        stroke.setStroke(new BasicStroke(4));
        stroke.setColor(Color.black);
        stroke.drawRect(getX(), getY(), getHeight(), getWidth());

    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the WORLD_WIDTH
     */
    public static int getWORLD_WIDTH() {
        return WORLD_WIDTH;
    }

    /**
     * @return the WORLD_HEIGHT
     */
    public static int getWORLD_HEIGHT() {
        return WORLD_HEIGHT;
    }

    @Override
    public void run() {

        while (isAlive) {

            if (y > WORLD_HEIGHT - height) {
                dy -= 2 * dy;

            }
            if (y < 0) {
                dy -= 2 * dy;
            }
            if (x > WORLD_WIDTH - width) {
                dx -= 2 * dx;
            }
            if (x < 0) {
                dx -= 2 * dx;
            }

            x += dx;
            y += dy;

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                Logger.getLogger(Rock.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
