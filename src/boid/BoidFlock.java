/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boid;

import java.awt.Graphics;
import java.util.List;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author breco
 */
public class BoidFlock {

    private List<Boid> boids;
    private List<Rock> rocks;
    
    public BoidFlock() {
        //Using a array list as we want to access each boid within multiple times but only add or remove from this whole list infrequintly
        this.boids = new ArrayList();
        this.rocks=new ArrayList();
    }

    //Adds a boid 
    public synchronized void addBoid(Boid boid) {
        this.boids.add(boid);
    }
    
    //Adding a rock for boids to avoid
    public void addRock(Rock rock){
        this.rocks.add(rock);
    }

    /**
     * Removes the last Boid to be added or first?
     *
     * @return
     */
    public synchronized Boid removeBoid() {
        if (this.size() > 0) {

            return this.boids.remove(this.size() - 1);
        } else {
            return null;
        }
    }


    /*
 
     */
    public synchronized List<Boid> getNeighbours(Boid boid) {

        double distance;
        //Using a linked list here as we are adding and removing frequently
        List<Boid> newFlock = new LinkedList();

        for (Boid i : boids) {
            //Distance between two boids
            distance = sqrt((pow(boid.getPositionX() - i.getPositionX(), 2)) + (pow(boid.getPositionY() - i.getPositionY(), 2)));

            //If the boid is close to the neighboor it gets added to boid
            if (distance <= boid.RADIUS_DETECTION) {
                newFlock.add(i);
            }

        }
        
        return newFlock;

    }
    
    public synchronized List<Rock> getRocks(Boid boid)
    {
      
         double distance;
        //Using a linked list here as we are adding and removing frequently
        List<Rock> newFlock = new LinkedList();

        for (Rock i : rocks) {
            //Distance between two boids
            distance = sqrt((pow(boid.getPositionX() - i.getX(), 2)) + (pow(boid.getPositionY() - i.getY(), 2)));

            //If the boid is close to the neighboor it gets added to boid
            if (distance <= boid.RADIUS_DETECTION) {
                newFlock.add(i);
            }

        }
        
        return newFlock;

    }

    /**
     * Returns the size of the boids array.
     *
     * @return
     */
    public int size() {
        return boids.size();

    }

    public void drawBoids(Graphics g) {
        //Draw all boids
        for (Boid i : boids) {
            i.draw(g);
        }
    }
    
    public void drawRocks(Graphics g){
        for(Rock r:rocks)
        {
            r.draw(g);
        }
    }
}
