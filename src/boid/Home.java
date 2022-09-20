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
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author breco
 */
public class Home implements Comparable<Home> {

    Stack<Boid> house;
    //position
    int x, y;

    //The x y dimensions of the world.
    static int WORLD_WIDTH;
    static int WORLD_HEIGHT;

    public Home() {
        this.house = new Stack<Boid>();

        //Randomise starting locations add a variable to controll size
        x = 200;
        y = 200;
        //Random generation
        Random rand = new Random();
        //Set the boids starting position
        x = (rand.nextInt(WORLD_WIDTH));
        y = (rand.nextInt(WORLD_HEIGHT));
    }

    /**
     * *
     * Hides a Boid inside the home
     *
     * @param boid
     */
    public synchronized void addBoid(Boid boid) {

        this.house.add(boid);
    }

    /**
     *
     * @return boid
     */
    public synchronized Boid leaveHome() {
        Boid b = house.pop();
        return b;
    }

    public void draw(Graphics g) {

        Graphics2D stroke = (Graphics2D) g;
        stroke.setStroke(new BasicStroke(4));
        stroke.setColor(Color.black);
        stroke.drawOval(x, y, 5, 5);

    }

    /**
     * Compares homes first by there location in the x then by there location in
     * the y.
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(Home other) {
        if (this.x > other.x) {

        } else if (this.x < other.x) {
            return -1; // smaller
        } else if (this.x == other.x) {
            if (this.y > other.y) {
                return 1;
            } else if (this.y < other.y) {
                return -1;
            }
        }
        return 0;
    }
}
