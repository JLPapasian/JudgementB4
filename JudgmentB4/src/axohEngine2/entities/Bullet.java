/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: June 29, 2015
 * 
 * Title: Mob
 * Description: A class which adds additional abilities to a possibly animated image. used for monsters or 
 * normal characters. This class has a lot of room for improvement.
 * 
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.entities;

//Imports
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

import axohEngine2.project.TYPE;

public class Bullet extends AnimatedSprite{


	private int health;
	private TYPE ai;
	private int xx;
	private int yy;
	
	//Graphics and Window objects the mob needs for display
	private Graphics2D g2d;
	private JFrame frame;
	
	
	private int bulletX;
	private int bulletY;
	private int bulletXDelta;
	private int bulletYDelta;
	private int bulletSpeed =7; 
	public int bulletSpawnTime = 0;
	
	/************************************************************************
	 * Constructor
	 *  
	 * @param frame - JFrame window for display
	 * @param g2d - Graphics2D object used for displaying an image on a JFrame
	 * @param sheet - The spriteSheet the animation or image is taken from
	 * @param spriteNumber - The position on the spriteSheet the animation is or image starts
	 * @param ai - A TYPE enum depicting the ai's kind (radom, set path, player, etc)
	 * @param name - The character name in a String
	 * @param hostility - Boolean, is the mob going to attack the player?
	 *************************************************************************/
	public Bullet(JFrame frame, Graphics2D g2d, SpriteSheet sheet, int spriteNumber, TYPE ai, String name, boolean hostility) {
		super(frame, g2d, sheet, spriteNumber, name);
		this.frame = frame;
		this.g2d = g2d;
		this.ai = ai;
		
		setName(name);
		health = 0;
		setSolid(true);
		setAlive(true);
		setSpriteType(ai);
	}
	
	//Getters for name and ai type
	public String getName() { return super._name; }
	public TYPE getType() { return ai; }
	
	//Setters for current health, ai, name and 
	public void setHealth(int health) { this.health = health; }
	public void setAi(TYPE ai) { this.ai = ai; }
	public void setName(String name) { super._name = name; }

	
	/***************************************************************
	 * Method used to change a bullet's position.
	 * 
	 * @param xa - Int movement in pixels on the x axis
	 * @param ya - Int movement in pixels on the y axis
	 ****************************************************************/
	public void moveBullet(int xa, int ya) { 
			bulletX -= xa; 
			bulletY -= ya;
	}	
	
	
	/***************************************************
	 * Get the x or y location of the mob in the room or
	 * set a new x or y location relative to it's current position
	 * 
	 * @return - x or y int of location
	 ***************************************************/
	public double getXLoc() { return entity.getX(); }
	public double getYLoc() { return entity.getY(); }
	public void setLoc(int x, int y) { //Relative to current position
		xx = xx + x;
		yy = yy + y;
	}

	/**********************************************
	 * Render the Mob in the game room at anx and y location
	 * 
	 * @param x - Int x position
	 * @param y - Int y position
	 ***********************************************/
	public void renderMob(int x, int y) {
		g2d.drawImage(getImage(), x + xx, y + yy, getSpriteSize(), getSpriteSize(), frame);
		entity.setX(x);
		entity.setY(y);
	}
	
	public void setLocation(int x, int y){
		bulletX=x;
		bulletY=y;
	}
	
	public int getX(){
		return bulletX;
	}
	public int getY(){
		return bulletY;
		}
			
	//Used for damaging characters
	public void damageMob(int damage)
	{
		this.health=this.health-damage;
	}
	
	//Used for healing mobs
	public void healMob(int healthUp)
	{
		this.health=this.health+healthUp;
	}
	
	public int getHealth(){
		return health;
	}
	
}