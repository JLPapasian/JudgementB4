/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 0.53
 * Date: June 14, 2015
 * 
 * 
 * Title: Judgement(The Game)
 * Description: This class extends 'Game.java' in order to run a 2D game with specificly defined
 *  sprites, animatons, and actions.
 *  
 * 
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package name
package axohEngine2;

//Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import axohEngine2.entities.AnimatedSprite;
import axohEngine2.entities.Bullet;
import axohEngine2.entities.ImageEntity;
import axohEngine2.entities.Mob;
import axohEngine2.entities.SpriteSheet;
import axohEngine2.map.Map;
import axohEngine2.map.Tile;
import axohEngine2.project.InGameMenu;
import axohEngine2.project.MapDatabase;
import axohEngine2.project.OPTION;
import axohEngine2.project.STATE;
import axohEngine2.project.TYPE;
import axohEngine2.project.TitleMenu;
import axohEngine2.sound.Audio;
import  sun.audio.*;    //import the sun.audio package

import  java.io.*;
import java.awt.Rectangle;//temporary modification?

//Start class by also extending the 'Game.java' engine interface
public class Judgement extends Game {
	//For serializing (The saving system)
	private static final long serialVersionUID = 1L;
	
	/****************** Variables **********************/
	//--------- Screen ---------
	//SCREENWIDTH - Game window width
	//SCREENHEIGHT - Game window height
	//CENTERX/CENTERY - Center of the game window's x/y
	static int SCREENWIDTH = 1600;
	static int SCREENHEIGHT = 900;
	public static int CENTERX = SCREENWIDTH / 2;
	public static int CENTERY = SCREENHEIGHT / 2;

	
	//--------- Miscelaneous ---------
	//booleans - A way of detecting a pushed key in game
	//random - Use this to generate a random number
	//state - Game states used to show specific info ie. pause/running
	//option - In game common choices at given times
	//Fonts - Variouse font sizes in the Arial style for different in game text
	boolean keyLeft, keyRight, keyUp, keyDown, keyInventoryOpen, keyInventoryClose, keyAction, keyBack, keyEnter, keySpace = false;
	boolean keyInventoryDown=false;
	
	Random random = new Random();
	STATE state; 
	OPTION option;
	private Font simple = new Font("Arial", Font.PLAIN, 72);
	private Font bold = new Font("Arial", Font.BOLD, 72);
	private Font bigBold = new Font("Arial", Font.BOLD, 96);
	
	//--------- Player and scale ---------
	//scale - All in game art is 16 x 16 pixels, the scale is the multiplier to enlarge the art and give it the pixelated look
	//mapX/mapY - Location of the camera on the map
	//playerX/playerY - Location of the player on the map
	//startPosX/startPosY - Starting position of the player in the map
	//playerSpeed - How many pixels the player moves in a direction each update when told to
	private int scale;
	private int mapX;
	private int mapY;
	public static int playerX;
	public static int playerY;
	private int startPosX;
	private int startPosY;
	private int playerSpeed;
	
	
	//----------- Map and input --------
	//currentMap - The currently displayed map the player can explore
	//currentOverlay - The current overlay which usually contains houses, trees, pots, etc.
	//mapBase - The database which contains all variables which pertain to specific maps(NPCs, monsters, chests...)
	//inputWait - How long the system waits for after an action is done on the keyboard
	//confirmUse - After some decisions are made, a second question pops up, true equals continue action from before.
	private Map currentMap;
	private Map currentOverlay;
	private MapDatabase mapBase;
	private int inputWait = 5;
	private int attackWait = 0; //Modification
	private int bulletSpawnTime = 0; //Modification
	private boolean confirmUse = false;
	
	private int currentMapIndex =0;
	
	//----------- Menus ----------------
	//inX/inY - In Game Menu starting location for default choice highlight
	//inLocation - Current choice in the in game menu represented by a number, 0 is the top
	//sectionLoc - Current position the player could choose after the first choice has been made in the in game menu(Items -> potion), 0 is the top.
	//titleX, titleY, titleX2, titleY2 - Positions for specific moveable sprites at the title screen (arrow/highlight).
	//titleLocation - Current position the player is choosing in the title screen(File 1, 2, 3) 0 is top
	//currentFile - Name of the currently loaded file
	//wasSaving/wait/waitOn - Various waiting variables to give the player time to react to whats happening on screen
	private int inX = 90, inY = 90;
	private int inLocation;
	private int sectionLoc;
	private int titleX = 530, titleY = 610;
	private int titleX2 = 340, titleY2 = 310;
	private int titleLocation;
	private String currentFile;
	private int wait;
	private boolean waitOn = false;
	
	private int escapeDown = 0;
	
	
	//----------- Game  -----------------
	//SpriteSheets (To be split in to multiple smaller sprites)
	SpriteSheet extras1;
	SpriteSheet mainCharacter;
	SpriteSheet bullets; //Modification
	
	//ImageEntitys (Basic pictures)
	ImageEntity inGameMenu;
	ImageEntity titleMenu;
	ImageEntity titleMenu2;
	ImageEntity healthBar;
	ImageEntity healthBarOutline;
	
	//Menu classes
	TitleMenu title;
	InGameMenu inMenu;
	
	//Animated sprites
	AnimatedSprite titleArrow;
	
	//Player and NPCs
	Mob playerMob;
	Mob randomNPC;
	Mob bullet; //Modification
	
	private int testNPCSpawnTime; //temp
	private int testNPCLocationX; //testing
	private int testNPCLocationY; //temporary (Testing)
	Mob mobNPC;	//TEMP
	
	
	//Projectile Variables
	
	boolean arrow, bulletSpawned; //MODIFICATION
	private int bulletLifeSpan=50;
	private int bulletSpeed =7; 
	
	private List<Mob> bulletsArr = new ArrayList<>();
	
	//Audio variables
	public static AudioStream titleMusic;
	private String menuBlipSnd = "blip.wav";
	private String startGameSnd = "startGame.wav";
	private String titleSnd = "2.au";
	private String shootSnd = "shoot.wav";
	private String bulletColSnd = "bulletCol.wav";
	
	
	/*********************************************************************** 
	 * Constructor
	 * 
	 * Set up the super class Game and set the window to appear

	 ******************************************************************/
	 
	 public Judgement() {
		super(130, SCREENWIDTH, SCREENHEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/****************************************************************************
	 * Inherited Method
	 * This method is called only once by the 'Game.java' class, for startup
	 * Initialize all non-int variables here
	 ********************
	 *
	 *sw*********************************************************/
	void gameStartUp() {
		/****************************************************************
		 * The "camera" is the mapX and mapY variables. These variables 
		 * can be changed in order to move the map around, simulating the
		 * camera. The player is moved around ONLY when at an edge of a map,
		 * otherwise it simply stays at the center of the screen as the "camera"
		 * is moved around.
		 ****************************************************************/
		//****Initialize Misc Variables
		state = STATE.TITLE;
		option = OPTION.NONE;
		startPosX = 500; //TODO: Make a method that takes a tile index and spits back an x or y coordinate of that tile
		startPosY = 0;
		mapX = 0;
		mapY = 32;
		scale = 4;
		playerSpeed = 3;
		
		
			try {
				Audio.loadMuted();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		Audio.StartTitleMusic("2.au");
		

		
	
		
		//****Initialize spriteSheets*********************************************************************
		extras1 = new SpriteSheet("/textures/extras/extras1.png", 8, 8, 32, scale);
		mainCharacter = new SpriteSheet("/textures/characters/mainCharacter.png", 8, 8, 32, scale);
		bullets = new SpriteSheet("/textures/weaponsArmors/Bullets.png", 16, 16, 16, 1); //MODIFICATION

		//****Initialize and setup AnimatedSprites*********************************************************
		titleArrow = new AnimatedSprite(this, graphics(), extras1, 0, "arrow");
		titleArrow.loadAnim(4, 10);
		sprites().add(titleArrow);
		
		//****Initialize and setup image entities**********************************************************
		inGameMenu = new ImageEntity(this);
		titleMenu = new ImageEntity(this);
		titleMenu2 = new ImageEntity(this);
		healthBar = new ImageEntity(this);
		healthBarOutline = new ImageEntity(this);
		inGameMenu.load("/menus/ingamemenu.png");
		titleMenu.load("/menus/titlemenu1.png");
		titleMenu2.load("/menus/titlemenu2.png");
		healthBar.load("/menus/healthbar.png");
		healthBarOutline.load("/menus/healthbarOutline.png");
		
		//*****Initialize Menus***************************************************************************
		title = new TitleMenu(titleMenu, titleMenu2, titleArrow, SCREENWIDTH, SCREENHEIGHT, simple, bold, bigBold);
		inMenu = new InGameMenu(inGameMenu, SCREENWIDTH, SCREENHEIGHT);
		
		//****Initialize and setup Mobs*********************************************************************
		playerMob = new Mob(this, graphics(), mainCharacter, 40, TYPE.PLAYER, "mainC", true);
		playerMob.setMultBounds(6, 50, 95, 37, 88, 62, 92, 62, 96);
		playerMob.setMoveAnim(32, 48, 40, 56, 3, 8);
		playerMob.addAttack("sword", 0, 5);
		playerMob.getAttack("sword").addMovingAnim(17, 25, 9, 1, 3, 8);
		playerMob.getAttack("sword").addAttackAnim(20, 28, 12, 4, 3, 6);
		playerMob.getAttack("sword").addInOutAnim(16, 24, 8, 0, 1, 10);
		playerMob.setCurrentAttack("sword"); //Starting attack
		playerMob.setHealth(20); //If you change the starting max health, dont forget to change it in inGameMenu.java max health also
		sprites().add(playerMob);

		//Testing Purposes Only
		mobNPC = new Mob(this, graphics(), mainCharacter, 40, TYPE.ENEMY, "testingNPC", false);
		mobNPC.setHealth(5);
		sprites().add(mobNPC);

		
		//Projectile
		bullet = new Mob(this, graphics(), bullets, 0, TYPE.BULLET, "aBullet", false);
		sprites().add(bullet);
		
		
		mapBase = new MapDatabase(this, graphics(), scale);
		//Get Map from the database
	
		currentMap = mapBase.getMap(currentMapIndex);

		//Add the tiles from the map to be updated each system cycle
		for(int i = 0; i < currentMap.getHeight() * currentMap.getHeight(); i++){
			addTile(currentMap.accessTile(i));
		//	addTile(currentOverlay.accessTile(i));
			if(currentMap.accessTile(i).hasMob()) sprites().add(currentMap.accessTile(i).mob());
		//	if(currentOverlay.accessTile(i).hasMob()) sprites().add(currentOverlay.accessTile(i).mob());
			currentMap.accessTile(i).getEntity().setX(-300);
		//	currentOverlay.accessTile(i).getEntity().setX(-300);
		}
		
		playerX=startPosX;
		playerY=startPosY;

		playerMob.updatePlayer(false, false, true, false); 
		//Faces the character forward and gets around the glitch that occurred when a player attacked before moving
		
		
		requestFocus(); //Make sure the game is focused on
		reset();
		start(); //Start the game loop
	}
	
	/**************************************************************************** 
	 * Inherited Method
	 * Method that updates with the default 'Game.java' loop method
	 * Add game specific elements that need updating here
	 *****************************************************************************/
	void gameTimedUpdate() {
		checkInput(); //Check for user input
		//Update certain specifics based on certain game states
		if(state == STATE.TITLE) title.update(option, titleLocation); //Title Menu update
		if(state == STATE.INGAMEMENU) inMenu.update(option, sectionLoc, playerMob.health()); //In Game Menu update
		updateData(currentMap, currentMap, playerX, playerY); //Update the current file data for saving later
//System.out.println(frameRate()); //Print the current framerate to the console
		if(waitOn) wait--;
	}
	
	/**
	 * Inherited Method
	 * Obtain the 'graphics' passed down by the super class 'Game.java' and render objects on the screen
	 */
	void gameRefreshScreen() {		
		/*********************************************************************
		* Rendering images uses the java class Graphics2D
		* Each frame the screen needs to be cleared and an image is setup as a back buffer which is brought 
		* to the front as a full image at the time it is needed. This way the screen is NOT rendered pixel by 
		* pixel in front of the user, which would have made a strange lag effect.
		* 
		* 'graphics' objects have parameters that can be changed which effect what it renders, two are font and color
		**********************************************************************/
		Graphics2D g2d = graphics();
		g2d.clearRect(0, 0, SCREENWIDTH, SCREENHEIGHT); 
		g2d.setFont(simple);
		
		//GUI rendering for when a specific state is set, only specific groups of data is drawn at specific times
		if(state == STATE.GAME) {
			//Render the map, the player, any NPCs or Monsters and the player health or status
			currentMap.render(this, g2d, mapX, mapY);
			//currentOverlay.render(this, g2d, mapX, mapY);
			playerMob.renderMob(CENTERX - playerX, CENTERY - playerY);
			
			Audio.StopTitleMusic(); //Stops the music clip
			
			


			int playerLocationX = CENTERX - playerX;	//temp
			int playerLocationY = CENTERY - playerY;	//temp
			playerMob.renderMob(CENTERX - playerX, CENTERY - playerY);
			
			Audio.StopTitleMusic(); //Stops the music clip
			
			if(testNPCSpawnTime == 0) {	//This small section was added
				testNPCSpawnTime++;
				testNPCLocationX = CENTERX - playerX+220;
				testNPCLocationY = CENTERY - playerY-250;
				mobNPC.setSpeed(1);
			} else {
				if(mobNPC.alive())
				mobNPC.renderMob(testNPCLocationX, testNPCLocationY);
				
			}

			mobNPC.chase(playerLocationX, playerLocationY);
			
			

			
			

			bulletsArr.stream().forEach(Mob -> Mob.renderMob(Mob.bulletX,Mob.bulletY));
			bulletsArr.stream().forEach(Mob -> Mob.updateMob());
			
			
			 for(int i =0; i<bulletsArr.size()-1; i++){
			 
			       if(bulletsArr.get(i).alive()==false){
			          bulletsArr.remove(i);
			          System.out.println("remove");
			       }
			 }
			if(bulletsArr.size()>300)
				bulletsArr.removeAll(bulletsArr);
			
			
			
			//Drawing the health bar
			g2d.drawImage(healthBarOutline.getImage(), 1000, 700, 300, -600, this.rootPane);
			g2d.drawImage(healthBar.getImage(), 1000, 700, 300, -playerMob.getHealth()*30, this.rootPane); 

			
			g2d.setColor(Color.RED);
			g2d.drawString("Health: " + playerMob.getHealth(), CENTERX+200, CENTERY - 350);
			g2d.setColor(Color.BLUE);
			//g2d.drawString("Magic: " + inMenu.getMagic(), CENTERX - 280, CENTERY - 350);
			//g2d.setColor(Color.YELLOW);
			
			
			//NPC IS CURRENTLY REMOVED
			//g2d.drawString("NPC health: " + currentMap.accessTile(16).mob().health(), CENTERX + 200, CENTERY - 350);
			
			
			
			if(playerMob.getHealth()<=0){
				reset();
				state= STATE.TITLE;
			}
			
		}
		if(state == STATE.INGAMEMENU){
			//Render the in game menu and specific text
			inMenu.render(this, g2d, inX, inY);
			g2d.setColor(Color.red);
			if(confirmUse) g2d.drawString("Use this?", CENTERX, CENTERY);
		}
		if(state == STATE.TITLE) {
			//Render the title screen
			title.render(this, g2d, titleX, titleY, titleX2, titleY2);
		}
	}
	
	/*******************************************************************
	 * The next four methods are inherited
	 * Currently these methods are not being used, but they have
	 * been set up to go off at specific times in a game as events.
	 * Actions that need to be done during these times can be added here.
	 ******************************************************************/
	void gameShutDown() {		
	}

	void spriteUpdate(AnimatedSprite sprite) {		
	}

	void spriteDraw(AnimatedSprite sprite) {		
	}

	void spriteDying(AnimatedSprite sprite) {		
	}

	/*************************************************************************
	 * @param AnimatedSprite
	 * @param AnimatedSprite
	 * @param int
	 * @param int
	 * 
	 * Inherited Method
	 * Handling for when a SPRITE contacts a SPRITE
	 * 
	 * hitDir is the hit found when colliding on a specific bounding box on spr1 and hitDir2
	 * is the same thing applied to spr2
	 * hitDir is short for hit direction which can give the data needed to move the colliding sprites
	 * hitDir is a number between and including 0 and 3, these assignments are taken care of in 'Game.java'.
	 * What hitDir is actually referring to is the specific hit box that is on a multi-box sprite.
	 *****************************************************************************/
	void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2, int hitDir, int hitDir2) {
		//Get the smallest possible overlap between the two problem sprites
		double leftOverlap = (spr1.getBoundX(hitDir) + spr1.getBoundSize() - spr2.getBoundX(hitDir2));
		double rightOverlap = (spr2.getBoundX(hitDir2) + spr2.getBoundSize() - spr1.getBoundX(hitDir));
		double topOverlap = (spr1.getBoundY(hitDir) + spr1.getBoundSize() - spr2.getBoundY(hitDir2));
		double botOverlap = (spr2.getBoundY(hitDir2) + spr2.getBoundSize() - spr1.getBoundY(hitDir));
		double smallestOverlap = Double.MAX_VALUE; 
		double shiftX = 0;
		double shiftY = 0;

		if(leftOverlap < smallestOverlap) { //Left
			smallestOverlap = leftOverlap;
			shiftX -= leftOverlap; 
			shiftY = 0;
		}
		if(rightOverlap < smallestOverlap){ //right
			smallestOverlap = rightOverlap;
			shiftX = rightOverlap;
			shiftY = 0;
		}
		if(topOverlap < smallestOverlap){ //up
			smallestOverlap = topOverlap;
			shiftX = 0;
			shiftY -= topOverlap;
		}
		if(botOverlap < smallestOverlap){ //down
			smallestOverlap = botOverlap;
			shiftX = 0;
			shiftY = botOverlap;
		}
		
		
		if(spr1.spriteType() == TYPE.BULLET && spr2.spriteType() == TYPE.ENEMY) {
			spr1.setBounds(0, 0, 0);
			spr1.setSpriteSize(0);
			((Mob) spr1).setAlive(false);
			Audio.PlaySound(bulletColSnd);
			((Mob) spr2).damageMob(1);
			System.out.println("enemy Collision - "+mobNPC.getHealth());
			if(((Mob) spr2).getHealth()<1){
				((Mob) spr2).setAlive(false);
				((Mob) spr2).setHealth(5);
			}
			
		}

		//Handling very specific collisions
		if(spr1.spriteType() == TYPE.PLAYER && state == STATE.GAME){
			
			if(spr2.spriteType()==TYPE.ENEMY){
				((Mob) spr2).stop();
				//added player damage
				playerMob.damageMob(1);
				
			//This piece of code is commented out because I still need the capability of getting a tile from an xand y position
			/*if(((Mob) spr1).attacking() && currentOverlay.getFrontTile((Mob) spr1, playerX, playerY, CENTERX, CENTERY).getBounds().intersects(spr2.getBounds())){
				((Mob) spr2).takeDamage(25);
				//TODO: inside of take damage should be a number dependant on the current weapon equipped, change later
			}*/
			
			//Handle simple push back collision
			if(playerX != 0) playerX -= shiftX;
			if(playerY != 0) playerY -= shiftY;
			if(playerX == 0) playerX -= shiftX;
			if(playerY == 0) playerY -= shiftY;
			
			}
	}}
	
	/***********************************************************************
	* @param AnimatedSprite
	* @param Tile
	* @param int
	* @param int
	* 
	* Inherited Method
	* Set handling for when a SPRITE contacts a TILE, this is handy for
	* dealing with Tiles which contain Events. When specifying a new
	* collision method, check for the type of sprite and whether a tile is
	* solid or breakable, both, or even if it contains an event. This is
	* mandatory because the AxohEngine finds details on collision and then 
	* returns it for specific handling by the user.
	* 
	* For more details on this method, refer to the spriteCollision method above
	*************************************************************************/
	void tileCollision(AnimatedSprite spr, Tile tile, int hitDir, int hitDir2) {
		double leftOverlap = (spr.getBoundX(hitDir) + spr.getBoundSize() - tile.getBoundX(hitDir2));
		double rightOverlap = (tile.getBoundX(hitDir2) + tile.getBoundSize() - spr.getBoundX(hitDir));
		double topOverlap = (spr.getBoundY(hitDir) + spr.getBoundSize() - tile.getBoundY(hitDir2));
		double botOverlap = (tile.getBoundY(hitDir2) + tile.getBoundSize() - spr.getBoundY(hitDir));
		double smallestOverlap = Double.MAX_VALUE; 
		double shiftX = 0;
		double shiftY = 0;
		
		if(spr.spriteType() == TYPE.BULLET) {
			
			System.out.println("bullet COllision");
			spr.setBounds(0, 0, 0);
			spr.setSpriteSize(0);
			((Mob) spr).setAlive(false);
			Audio.PlaySound(bulletColSnd);
		}
		else{

		if(leftOverlap < smallestOverlap) { //Left
			smallestOverlap = leftOverlap;
			shiftX -= leftOverlap; 
			shiftY = 0;
		}
		if(rightOverlap < smallestOverlap){ //right
			smallestOverlap = rightOverlap;
			shiftX = rightOverlap;
			shiftY = 0;
			
		}
		if(topOverlap < smallestOverlap){ //up
			smallestOverlap = topOverlap;
			shiftX = 0;
			shiftY -= topOverlap;
		}
		if(botOverlap < smallestOverlap){ //down
			smallestOverlap = botOverlap;
			shiftX = 0;
			shiftY = botOverlap;	
		}		
		
		}
		
		//Deal with a tiles possible event property
		if(tile.hasEvent()){
			if(spr.spriteType() == TYPE.PLAYER) {
				//Warp Events(Doors)
				if(tile.event().getEventType() == TYPE.WARP) {
					tiles().clear();
					sprites().clear();
					sprites().add(playerMob);
					sprites().add(mobNPC);
					mobNPC.setAlive(true);
					sprites().add(bullet);
					sprites().add(titleArrow);
					bulletsArr.removeAll(bulletsArr);
					//Get the new map
					
					currentMapIndex=currentMapIndex+1;
					currentMap = mapBase.getMap(currentMapIndex);				
					
					//Load in the new maps Tiles and Mobs
					for(int i = 0; i < currentMap.getWidth() * currentMap.getHeight(); i++){
						addTile(currentMap.accessTile(i));
				//		addTile(currentOverlay.accessTile(i));
						if(currentMap.accessTile(i).hasMob()) sprites().add(currentMap.accessTile(i).mob());
				//		if(currentOverlay.accessTile(i).hasMob()) sprites().add(currentOverlay.accessTile(i).mob());
					}
					//Move the player to the new position
					//playerX = tile.event().getNewX();
					//moves the bullet off screen
					playerY = tile.event().getNewY();
				}	
			} //end warp
			//Item exchange event
			if(spr.spriteType() == TYPE.PLAYER && tile.event().getEventType() == TYPE.ITEM && keyAction){
				if((tile._name).equals("chest")) tile.setFrame(tile.getSpriteNumber() + 1); //Chests should have opened and closed version next to each other
				inMenu.addItem(tile.event().getItem()); //Add item to inventory
				tile.endEvent();
			}
		}//end check events
		
		//If the tile is solid, move the player off of it and exit method immediately
		if(spr.spriteType() == TYPE.PLAYER && tile.solid() && state == STATE.GAME) {
			if(playerX != 0) playerX -= shiftX;
			if(playerY != 0) playerY -= shiftY;
			if(playerX == 0) playerX -= shiftX;
			if(playerY == 0) playerY -= shiftY;
			return;
		}
		//If an npc is intersecting a solid tile, move it off
		if(spr.spriteType() != TYPE.PLAYER && tile.solid() && state == STATE.GAME){
			if(spr instanceof Mob) {
				((Mob) spr).setLoc((int)shiftX, (int)shiftY);
				((Mob) spr).resetMovement();
			}
		}
	}//end tileCollision method
	
	/*****************************************************************
	 * @param int
	 * @param int
	 * 
	 *Method to call which moves the player. The player never moves apart from the map
	 *unless the player is at an edge of the generated map. Also, to simulate the movement
	 *of the space around the player like that, the X movement is flipped. 
	 *Which means to move right, you subtract from the X position.
	 ******************************************************************/
	public void movePlayer(int xa, int ya) {
		if(xa > 0) {
		 playerX += xa; //left +#
		}
		if(xa < 0) {
		 playerX += xa; //right -#
		}
		if(ya > 0) {
			playerY += ya; //up +#
		}
		if(ya < 0  && playerY >-300) {
			 playerY += ya; //down -#
		}
	}
	/**********************************************************
	 * Main
	 * 
	 * @param args
	 ********************************************************/
	public static void main(String[] args) { new Judgement(); }
	
	/**********************************************************
	 * The Depths of Judgement Lies Below
	 * 
	 *             Key events - Mouse events
	 *                            
	 ***********************************************************/
	
	/****************************************************************
	 * Check specifically defined key presses which do various things
	 ****************************************************************/
	public void checkInput() {
		int xa = 0;
		int ya = 0;
		
		/********************************************
		 * Special actions for In Game
		 *******************************************/
		if(state == STATE.GAME && inputWait < 0) { 
			if(keyLeft) {
				xa = xa + 1 + playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			if(keyRight) {
				xa = xa - 1 - playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			if(keyUp) {
				ya = ya + 1 + playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			if(keyDown) {
				ya = ya - 1 - playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			
			//No keys are pressed
			if(!keyLeft && !keyRight && !keyUp && !keyDown) {
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			movePlayer(xa, ya);
			
			if(arrow) { //MODIFICATION
				playerMob.attack();	
			}
		
			
			//I(Inventory)
			if(keyInventoryOpen) {
				//Opens the in game menu
				state = STATE.INGAMEMENU;
				inputWait =	1;
			}
			
			//SpaceBar(action button)a
			if(keySpace) {
				playerMob.inOutItem();
				inputWait = 10;
			}
		}//end in game choices
		
		/*****************************************
		 * Special actions for the Title Menu
		 *****************************************/
		if(state == STATE.TITLE && inputWait < 0){
			    
			    
			//For when no initial choice has been made
			if(option == OPTION.NONE){
				//S or down arrow(Change selection)
				if(keyDown && titleLocation < 1) {
					titleX -= 105;
					titleY += 100;
					titleLocation++;
					inputWait = 1;
					Audio.PlaySound(menuBlipSnd);
				}
				//W or up arrow(Chnage selection
				if(keyUp && titleLocation > 0){
					titleX += 105;
					titleY -= 100;
					titleLocation--;
					inputWait = 1;
					Audio.PlaySound(menuBlipSnd); //plays the blip sound when moving between options
				}
				//Enter key(Make a choice)
				if(keyEnter) {
					if(titleLocation == 0){
						option = OPTION.NEWGAME;
						titleLocation = 0;
						inputWait = 10;
						keyEnter = false;
						Audio.PlaySound(startGameSnd); //plays the start game sound
					}
					if(titleLocation == 1){
						System.exit(0);
					}
				}
			}//end option none
			
			//After choosing an option
			if(option == OPTION.NEWGAME){
				state = STATE.GAME;
				option = OPTION.NONE;
				setGameState(STATE.GAME);
				//Enter key(Make a choice)
				if(keyEnter && !title.isGetName()) {
					if(option == OPTION.NEWGAME) {
						
						
						
						state = STATE.GAME;
						option = OPTION.NONE;
						setGameState(STATE.GAME);
					
					
					}
				}//end enter key
				
				//The following is for when a new file needs to be created - Typesetting
				
			}//end new/load option
		}//end title state
			
			
		
		
		/******************************************
		 * Special actions for In Game Menu
		 ******************************************/
		if(state == STATE.INGAMEMENU && inputWait < 0) {
				//W or up arrow(Move selection)
				if(keyUp) {
					if(inLocation > 0) {
						inY -= 108;
						inLocation--;
						inputWait = 2;
						Audio.PlaySound(menuBlipSnd); //plays the blip sound when moving between options
					}
				}
				//S or down arrow(move selection)
				if(keyDown) {
					if(inLocation < 3) {
						inY += 108;
						inLocation++;
						inputWait = 2;
						Audio.PlaySound(menuBlipSnd); //plays the blip sound when moving between options
					}
				}
				//Enter key(Make a choice)
				if(keyEnter) {
					if(inLocation == 0){
						Audio.ToggleAudio();
						option = OPTION.TOGGLEAUDIO;
						inputWait = 5;
					}
					if(inLocation == 1){
						option = OPTION.EQUIPMENT;
						inputWait = 5;
					}
					if(inLocation == 2){
						state = STATE.GAME;
						option = OPTION.NONE;
						inLocation = 0;
						inY = 90;
						inputWait = 1;
						
					}
					if(inLocation == 3){
						reset();
						inputWait = 1;
						option = OPTION.NONE;
						state = STATE.TITLE;
					}
					keyEnter = false;
				}
			
			//Set actions for specific choices in the menu
			//Items
			if(option == OPTION.ITEMS) {
				//W or up arrow(move selection)
				if(keyUp){
					if(sectionLoc == 0) inMenu.loadOldItems();
					if(sectionLoc - 1 != -1) sectionLoc--;
					inputWait = 8;
				}
				//S or down arrow(move selection)
				if(keyDown) {
					if(sectionLoc == 3) inMenu.loadNextItems();
					if(inMenu.getTotalItems() > sectionLoc + 1 && sectionLoc < 3) sectionLoc++;
					inputWait = 8;
				}
				//Enter key(Make a choice)
				if(keyEnter){
					if(confirmUse) {
						inMenu.useItem(); //then use item
						confirmUse = false;
						keyEnter = false;
					}
					if(inMenu.checkCount() > 0 && keyEnter) confirmUse = true;
					inputWait = 10;
				}
				//Back space(Go back on your last choice)
				if(keyBack) confirmUse = false;
			}
			
			//Equipment
			if(option == OPTION.EQUIPMENT) {
				//W or up arrow(move selection)
				if(keyUp){
					if(sectionLoc == 0) inMenu.loadOldItems();
					if(sectionLoc - 1 != -1) sectionLoc--;
					inputWait = 8;
				}
				//S or down arrow(move selection)
				if(keyDown) {
					if(sectionLoc == 3) inMenu.loadNextEquipment();
					if(inMenu.getTotalEquipment() > sectionLoc + 1 && sectionLoc < 3) sectionLoc++;
					inputWait = 8;
				}
			}
			
			//Backspace(if a choice has not been made, this closes the inventory)
			if(keyInventoryClose){
				state = STATE.GAME;
				option = OPTION.NONE;
				inLocation = 0;
				sectionLoc = 0;
				inY = 90;
				inputWait = 1;
			}
		}
		inputWait--;
		attackWait--; //Modification
	}
	
	/**
	 * Inherited method
	 * @param keyCode
	 * 
	 * Set keys for a new game action here using a switch statement
	 * dont forget gameKeyUp
	 */
	
	void gameKeyDown(int keyCode) {
		switch(keyCode) {
	        case KeyEvent.VK_A:
	        	keyLeft = true;
	        	break;
	        case KeyEvent.VK_D:
	        	keyRight = true;
	        	break;
	        case KeyEvent.VK_W:
	        	keyUp = true;
	        	break;
	        case KeyEvent.VK_S:
	        	keyDown = true;
	        	break;
	        case KeyEvent.VK_ESCAPE: 
	        	escapeDown=escapeDown+1;
	        	inLocation = 0;
				inY = 90;
	        	if(escapeDown ==1 && state==STATE.GAME)
	        	{
	        	keyInventoryOpen = true;
	        	escapeDown=escapeDown+1;
	        	}
	        	
	        	
	        	else if(escapeDown ==1 && state == STATE.INGAMEMENU)
	        	{
	        	keyInventoryClose = true;
	        	escapeDown=escapeDown+1;
	        	}
	        	
	        	else{
	        		keyInventoryClose=false;
	        		keyInventoryOpen=false;
	        	}	        	
	        	break;
	        case KeyEvent.VK_F:
	        	keyAction = true;
	        	break;
	        case KeyEvent.VK_ENTER:
	        	keyEnter = true;
	        	break;
	        case KeyEvent.VK_BACK_SPACE:
	        	keyBack = true;
	        	break;
	        case KeyEvent.VK_SPACE:
	        	keySpace = true;
	        	break;
	        case KeyEvent.VK_9:
	        	playerMob.healMob(1);
	        	break;
	        case KeyEvent.VK_6:
	        	if(state == STATE.GAME)
	        	playerMob.damageMob(1);
	        	break;
	        case KeyEvent.VK_B:
	        	break;
	        	
	        case KeyEvent.VK_DOWN: {//MODIFICATION_START
		    	if(attackWait <= 0 && state==STATE.GAME) {
		    		attackWait = 5;
		    		arrow = true;
		    	}
	            break;
		    } case KeyEvent.VK_UP: {
		    	System.out.println(currentMapIndex);
		    	if(attackWait <= 0 && state==STATE.GAME) {
		    		attackWait = 5;
		    		arrow = true;
		    	}
	            break;
		    } case KeyEvent.VK_RIGHT: {
		    	if(attackWait <= 0 && state==STATE.GAME) {
		    		attackWait = 5;
		    		arrow = true;
		    	}
	            break;
		    } case KeyEvent.VK_LEFT: {
		    	if(attackWait <= 0  && state==STATE.GAME) {
		    		attackWait = 5;
		    		arrow = true;
		    	}
	            break;	 //MODIFICATION_END
	        	
	        	
	        	
        }
		}
	}

	/**
	 * Inherited method
	 * @param keyCode
	 * 
	 * Set keys for a new game action here using a switch statement
	 * Dont forget gameKeyDown
	 */
	void gameKeyUp(int keyCode) {
		switch(keyCode) {
        case KeyEvent.VK_A:
        	keyLeft = false;
        	break;
        case KeyEvent.VK_D:
        	keyRight = false;
        	break;
        case KeyEvent.VK_W:
        	keyUp = false;
        	break;
        case KeyEvent.VK_S:
        	keyDown = false;
        	break;
        case KeyEvent.VK_LEFT: {	//MODIFICATION_START
        	if(arrow == true) {
        		arrow = false;
				bulletSpawned = true;
				Audio.PlaySound(shootSnd);
				bulletsArr.add(new Mob(this, graphics(), bullets, 0, TYPE.BULLET, "asBullet", false,-5,0));
				sprites().add(bulletsArr.get(bulletsArr.size()-1));
        	}
            break;
        } case KeyEvent.VK_RIGHT: {
        	if(arrow == true) {
        		arrow = false;
				bulletSpawned = true;
				Audio.PlaySound(shootSnd);
				bulletsArr.add(new Mob(this, graphics(), bullets, 0, TYPE.BULLET, "asBullet", false,5,0));
				sprites().add(bulletsArr.get(bulletsArr.size()-1));
        	}
            break;
        } case KeyEvent.VK_UP: {
        	if(arrow == true) {
        		arrow = false;
				bulletSpawned = true;
				Audio.PlaySound(shootSnd);
				bulletsArr.add(new Mob(this, graphics(), bullets, 0, TYPE.BULLET, "asBullet", false, 0,-5));
				sprites().add(bulletsArr.get(bulletsArr.size()-1));
        	}
            break;
        } case KeyEvent.VK_DOWN: {
        	if(arrow == true) {
        		arrow = false;
				bulletSpawned = true;
				Audio.PlaySound(shootSnd);
				bulletsArr.add(new Mob(this, graphics(), bullets, 0, TYPE.BULLET, "asBullet", false, 0,5));
				sprites().add(bulletsArr.get(bulletsArr.size()-1));
        	}
            break;
        }						
        case KeyEvent.VK_ESCAPE:
	    	escapeDown=0;
        	keyInventoryOpen = false;
        	keyInventoryClose = false;
	    	break;
	    case KeyEvent.VK_F:
	    	keyAction = false;
	    	break;
	    case KeyEvent.VK_ENTER:
	    	keyEnter = false;
	    	break;
	    	
	    case KeyEvent.VK_BACK_SPACE:
	    	keyBack = false;
	    	break;
	    case KeyEvent.VK_SPACE:
	    	keySpace = false;
	    	break;
		}
	}
	
	//Is called to reset variables and reselect random maps
	public void reset() {
	
		Audio.StartTitleMusic("2.au");  //restarts title music. Checks for mute within Audio.java
		tiles().clear(); //Clears the current tiles
		mapBase = new MapDatabase(this, graphics(), scale); //Builds a new map database
		currentMap = mapBase.getMap(0); //Sets the current map to 0  The first map

		// Add the tiles from the map to be updated each system cycle
		for (int i = 0; i < currentMap.getHeight() * currentMap.getHeight(); i++) {
			addTile(currentMap.accessTile(i));
			if (currentMap.accessTile(i).hasMob())
				sprites().add(currentMap.accessTile(i).mob());
			currentMap.accessTile(i).getEntity().setX(-300);
		}
			//Resets several Variables...
		bulletSpawned = false;
		bulletSpawnTime = 0;
		System.out.println("Reset");
		playerMob.setHealth(20);
		playerMob.setAlive(true);
		playerX = startPosX;
		playerY = startPosY;
		currentMapIndex = 0;
	}

	/**
	 * Inherited method
	 * Currently unused
	 */
	void gameMouseDown() {	
		
	}

	/**
	 * Inherited method
	 * Currently if the game is running and the sword is out, the player attacks with it
	 */
	void gameMouseUp() {
		
	}

	/**
	 * Inherited Method
	 * Currently unused
	 */
	void gameMouseMove() {
		
	}
	 
	 //From the title screen, load a game file by having the super class get the data,
	 // then handling where the pieces of the data will be assigned here.
	/**
	 * Inherited Method
	 * 
	 * The title screen calls this method when a currently existing file is chosen
	 * Add new saved game details here as well as in the 'Data.java' class
	 * 
	 * Currently only the player x and y location and the current map is saved
	 */
	} //end class