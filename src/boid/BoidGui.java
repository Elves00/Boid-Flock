/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author breco
 */
public class BoidGui extends JPanel implements ActionListener, ChangeListener {

    private JButton addBoidButton;
    private JButton addLots;
    private JButton addRemoveButton;

    private JLabel addCountLabel;
    private JLabel addCohesionLabel;
    private JLabel addSeperationLabel;
    private JLabel addAlignmentLabel;
    private JLabel addRadiusLabel;

    private JSlider addCohesionSlider;
    private JSlider addSeperationSlider;
    private JSlider addAlignmentSlider;
    private JSlider addRadiusSlider;

    private BoidFlock fly;

    private DrawPanel drawPanel;

    private Timer timer;

    public BoidGui() {
        super(new BorderLayout());

        //Boid flock
        this.fly = new BoidFlock();

        //Panel for button options
        JPanel southPanel = new JPanel(new GridLayout(2, 8));

        //buttons
        this.addBoidButton = new JButton("Add Boid");
        this.addLots = new JButton("Add Lots");
        this.addRemoveButton = new JButton("Remove");

        //SETS BOID VALUES TO A STARTING FLOCKING BEHAVIOUR
        Boid.RADIUS_DETECTION = 50;
        Boid.COHESION_WEIGHT = 0.1f;
        Boid.SEPARATION_WEIGHT = 1;
        Boid.ALIGNMENT_WEIGHT = 0.1f;

        //Labels
        this.addCountLabel = new JLabel("Boids:" + fly.size(),JLabel.CENTER);
       this.addCountLabel.setOpaque(true);
        this.addCohesionLabel = new JLabel("Cohesion " + Boid.COHESION_WEIGHT);
        this.addAlignmentLabel = new JLabel("Alignment: " + Boid.ALIGNMENT_WEIGHT);
        this.addSeperationLabel = new JLabel("Seperation: " + Boid.SEPARATION_WEIGHT);
        this.addRadiusLabel = new JLabel("Radius: " + Boid.RADIUS_DETECTION);

        //Sliders
        southPanel.add(this.addCohesionLabel);
        southPanel.add(this.addSeperationLabel);
        southPanel.add(this.addAlignmentLabel);
        southPanel.add(this.addRadiusLabel);

        //Adds button to bottom panel
        southPanel.add(this.addBoidButton);
        southPanel.add(this.addLots);
        southPanel.add(this.addRemoveButton);

        //Set up cohesion slider and intial starting position
        this.addCohesionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        southPanel.add(addCohesionSlider);
        addCohesionSlider.addChangeListener(this);
        //Set up serparation slider and intial starting position
        this.addSeperationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        southPanel.add(addSeperationSlider);
        addSeperationSlider.addChangeListener(this);
        //Set up allignment slider and intal starting position
        this.addAlignmentSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        southPanel.add(addAlignmentSlider);
        addAlignmentSlider.addChangeListener(this);
        //set up radius slider and intial starting position
        this.addRadiusSlider = new JSlider(JSlider.HORIZONTAL, 10, 110, 50);
        southPanel.add(addRadiusSlider);
        addRadiusSlider.addChangeListener(this);

        //Add 3 JLabels to ensure sliders allign properly.
        southPanel.add(new JLabel(""));
        southPanel.add(new JLabel(""));
        southPanel.add(new JLabel(""));

        //button listeners
        addBoidButton.addActionListener(this);
        addLots.addActionListener(this);
        addRemoveButton.addActionListener(this);

        //establishes draw panel
        drawPanel = new DrawPanel();

        //Puts panels in position
        this.add(drawPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(addCountLabel, BorderLayout.NORTH);

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        Object source = ae.getSource();

        if (source == addBoidButton) {
            //Create Boid
            Boid arrow = new Boid(fly);
            //Create thread
            Thread thread = new Thread(arrow);
            thread.start();
            //Increase label count
            addCountLabel.setText("Boids:" + fly.size());
        }

        if (source == addLots) {
            //Add 50 boids was used mostly for testing
            for (int i = 0; i < 50; i++) {

                Boid arrow = new Boid(fly);
                Thread thread = new Thread(arrow);
                thread.start();

            }

            addCountLabel.setText("Boids:" + fly.size());
        }

        if (source == addRemoveButton) {
            //Stop thread by setting thread is alive to false
            fly.removeBoid().requestStop();

            addCountLabel.setText("Boids:" + fly.size());

        }
        //repaint panel.
        drawPanel.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object sourceA = e.getSource();

        JSlider source = (JSlider) e.getSource();
        //Slider for Cohesion change value from 0-0.1
        if (sourceA == addCohesionSlider) {
            if (!source.getValueIsAdjusting()) {
                Boid.COHESION_WEIGHT = (float) source.getValue() / 1000;
                addCohesionLabel.setText("Cohesion:" + Boid.COHESION_WEIGHT);

            }
        } //Slider for seperation change value from 0-1
        else if (sourceA == addSeperationSlider) {
            if (!source.getValueIsAdjusting()) {
                Boid.SEPARATION_WEIGHT = (float) source.getValue() / 100;
                addSeperationLabel.setText("Seperation:" + Boid.SEPARATION_WEIGHT);

            }
            //Slider for allignment change value from 0-0.1
        } else if (sourceA == addAlignmentSlider) {
            if (!source.getValueIsAdjusting()) {
                Boid.ALIGNMENT_WEIGHT = (float) source.getValue() / 1000;
                addAlignmentLabel.setText("Alignment:" + Boid.ALIGNMENT_WEIGHT);

            }
        } else if (sourceA == addRadiusSlider) {
            if (!source.getValueIsAdjusting()) {
                Boid.RADIUS_DETECTION = (int) source.getValue();
                addRadiusLabel.setText("Radius:" + Boid.RADIUS_DETECTION);

            }
        }

    }

    private class DrawPanel extends JPanel {

        //Sets up the starting draw panel
        public DrawPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(500, 500));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Boid.WORLD_WIDTH = getWidth();
            Boid.WORLD_HEIGHT = getHeight();

            //Draw it.
            fly.drawBoids(g);

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Boid Flock");
        // kill all threads when frame closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new BoidGui());
        frame.pack();
        // position the frame in the middle of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width - frameDimension.width) / 2,
                (screenDimension.height - frameDimension.height) / 2);
        //Frame starts showing
        frame.setVisible(true);
        // now display something while the main thread is still alive

    }

}
