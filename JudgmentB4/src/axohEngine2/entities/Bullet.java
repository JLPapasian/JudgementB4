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
	
	/*************
	 * Variables
	 *************/
	//random - Use to obtain a randomly generated number
	//attacks - A list of attacks that can be used by a Mob
	//hostile - Can the mob attack the player?
	//health - HP
	/*ai - An enum which sets the ai of an npc, that ai needs to be written in a method and then added as
	   a TYPE in the TYPE.java class before it can be used. Right now only random ai is possible, and PLAYER.*/
	//xx and yy - Variables used as a starting position from a spawn point
	//speed - How fast the mob can move(Default 2 pixels per update)
	//attacking - A possible state the mob could be in, for many kinds of checks
	//takenOut - Boolean to see if the mob has it's weapon out
	//currentAttack - The currently selected attack to use from the list of Mob attacks
	private Random random = new Random();
	private LinkedList<Attack> attacks;
	private boolean hostile;
	private int health;
	private TYPE ai;
	private int xx;
	private int yy;
	private int speed = 2;
	private boolean attacking;
	private boolean takenOut = false;
	private Attack currentAttack;

	//Four variable booleans depicting the last direction the mob was moving(This could be phased out of the system)
	private boolean wasRight = false;
	private boolean wasLeft = false;
	private boolean wasUp = false;
	private boolean wasDown = false;

	//moveDir - Direction the mob was moving
	//direction - The direction the Mob is facing
	//randomDir - The random choice of a direction used in random movements
	private DIRECTION moveDir;
	private DIRECTION direction;
	private DIRECTION randomDir;
	
	//Wait timers
	private boolean waitOn = false;
	private int wait;
	
	//Graphics and Window objects the mob needs for display
	private Graphics2D g2d;
	private JFrame frame;
	
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
		attacks = new LinkedList<Attack>();
		this.frame = frame;
		this.g2d = g2d;
		this.ai = ai;
		
		hostile = hostility;
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
	public void setSpeed(int speed) { this.speed = speed; }

	
	/***************************************************************
	 * Method used to change a bullet's position.
	 * 
	 * @param xa - Int movement in pixels on the x axis
	 * @param ya - Int movement in pixels on the y axis
	 ****************************************************************/
	public void moveBullet(int xa, int ya) { 
		if(xa < 0) { //left
			xx += xa; 
		} else if(xa > 0) { //right
			xx += xa; 
		}
		if(ya < 0) {  //up
			yy += ya;
		} else if(ya > 0) { //down
			yy += ya;
		}
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
		entity.setX(x + xx);
		entity.setY(y + yy);
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