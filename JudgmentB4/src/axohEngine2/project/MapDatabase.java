/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: July 5, 2015
 * 
 * Title: Map Database
 * Description: A data handling class used for large projects. This class contains all of the spritesheets,
 * tiles, events, items, mobs and map creations since they all interlock together.
 * 
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.project;

//Imports
import java.awt.Graphics2D;
import java.io.File;  //imported for reading from file
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner; //imported for reading from file

import javax.swing.JFrame;

import axohEngine2.entities.Mob;
import axohEngine2.entities.SpriteSheet;
import axohEngine2.map.Event;
import axohEngine2.map.Map;
import axohEngine2.map.Tile;

public class MapDatabase {

	//SpriteSheets
	SpriteSheet misc;
	SpriteSheet buildings;
	SpriteSheet environment32;
	SpriteSheet extras2;
	SpriteSheet mainCharacter;
	SpriteSheet hints;
	
	
	//Tiles - Names are defined in the constructor for better identification
	Tile d;
	Tile g;
	Tile f;
	Tile b;
	Tile r;
	Tile e;
	Tile ro;
	Tile h;
	Tile hf;
	Tile c;
	
	//Events
	Event [] warp  = new Event[200];
	Event getPotion;
	Event getMpotion;
	
	//Items
	Item potion;
	Item mpotion;
	
	//NPC's and Monsters
	Mob npc;
	
	//Array of maps
	public Map[] maps;
	public Tile[][] mapTiles = new Tile[200][169];
	
	
	//Julian changes to allow reading from a file
	private Scanner fileInput;			//Used to read in the maze from a text file
	private int cur;
	private String nextTile;
	private int numMapFiles= 10;
	private int lastRandom;
	private int curRandom;
	
	/****************************************************************
	 * Constructor
	 * Instantiate all variables for the game
	 * 
	 * @param frame - JFrame Window for the map to be displayed on
	 * @param g2d - Graphics2D object needed to display images
	 * @param scale - Number to be multiplied by each image for correct on screen display
	 *******************************************************************/
	public MapDatabase(JFrame frame, Graphics2D g2d, int scale) {
		//Currently a maximum of 200 maps possible(Can be changed if needed)
		maps = new Map[200];
		
		//Checks the directory for the number of maps to use the random generator (minus the first and last map)
		numMapFiles = new File("maps/").listFiles().length-2;
		
		
		//Set up spriteSheets
		misc = new SpriteSheet("/textures/environments/environments.png", 16, 16, 16, scale);
		buildings = new SpriteSheet("/textures/environments/4x4buildings.png", 4, 4, 64, scale);
		environment32 = new SpriteSheet("/textures/environments/32SizeEnvironment.png", 8, 8, 32,scale);
		extras2 = new SpriteSheet("/textures/extras/extras2.png", 16, 16, 16, scale);
		mainCharacter = new SpriteSheet("/textures/characters/mainCharacter.png", 8, 8, 32, scale);
		
		//Set up tile blueprints and if they are animating
		d = new Tile(frame, g2d, "door", environment32, 0);
		f = new Tile(frame, g2d, "flower", misc, 11);
		g = new Tile(frame, g2d, "grass", misc, 0);
		b = new Tile(frame, g2d, "bricks", misc, 14, true);
		r = new Tile(frame, g2d, "walkWay", misc, 10);
		e = new Tile(frame, g2d, "empty", misc, 7);
		ro = new Tile(frame, g2d, "rock", misc, 2);
		h = new Tile(frame, g2d, "house", buildings, 0, true);
		hf = new Tile(frame, g2d, "floor", misc, 8);
		c = new Tile(frame, g2d, "chest", extras2, 0, true);
		

		Random rn = new Random();
		lastRandom = rn.nextInt(numMapFiles);
		
		for (int x=1; x<=10; x++){
			
			if (x==10)
			{
				mapFromFile("mapEnd.txt", mapTiles[x]);
				maps[x] =  new Map(frame, g2d, mapTiles[x], 13, 13, "map5");
			}
			
			else{
				curRandom = rn.nextInt(numMapFiles);
				while(curRandom == lastRandom) //Checks to see if the new random is the same as the last random
					curRandom = rn.nextInt(numMapFiles);  //if it is, it gets a new random
				
				lastRandom=curRandom;
		
				mapFromFile("map"+(curRandom)+".txt", mapTiles[x]);
				maps[x] =  new Map(frame, g2d, mapTiles[x], 13, 13, "map");
				warp[x] = new Event("ToNext", TYPE.WARP);
				warp[x] .setWarp(16, -300);
				maps[x].accessTile(8).addEvent(warp[x]);
			}
		}
		
	//Starting zone initialization	
		
		mapFromFile("mapStart.txt", mapTiles[0]);
		maps[0] =  new Map(frame, g2d, mapTiles[0], 13, 13, "map5");
		warp[0] = new Event("ToNext", TYPE.WARP);
		warp[0] .setWarp(16, -300);
		maps[0].accessTile(8).addEvent(warp[0]);
		

		
		
		//Put together all items (Dont forget to add these to the count and setup methods in inGameMenu.java)
	//	potion = new Item(frame, g2d, extras2, 2, "Potion", false);
	//	potion.setHealItem(25, false, "");
	//	mpotion = new Item(frame, g2d, extras2, 2, "Mega Potion", false);
	//	potion.setHealItem(50, false, "");
		
		
		//Item events
	//	getPotion = new Event("getPotion", TYPE.ITEM);
	//	getPotion.setItem(potion);
	//	getMpotion = new Event("getMpotion", TYPE.ITEM);
	//	getMpotion.setItem(mpotion);
		
		//Add the events to their specific tiles and maps
		//houses.accessTile(5).addEvent(warp1);
		//cityO.accessTile(92).addEvent(getPotion);
		//cityO.accessTile(242).addEvent(getPotion);
		//cityO.accessTile(328).addEvent(getPotion);
		//cityO.accessTile(327).addEvent(getMpotion);
		//cityO.accessTile(326).addEvent(getMpotion);
	    //cityO.accessTile(325).addEvent(getMpotion);
		//cityO.accessTile(93).addEvent(getMpotion);
		//cityO.accessTile(94).addEvent(getMpotion);
		//cityO.accessTile(95).addEvent(getMpotion);
		//cityO.accessTile(96).addEvent(getMpotion);
		
		//Set up Monsters and NPCs
		//npc = new Mob(frame, g2d, mainCharacter, 40, TYPE.ENEMY, "npc", false);
		//npc.setMultBounds(6, 50, 92, 37, 88, 62, 92, 62, 96);
		//npc.setMoveAnim(32, 48, 40, 56, 3, 8);
		//npc.setHealth(60);
		
		//Add the mobs to their tile home
		//maps[0].accessTile(16).addMob(npc);
	}
	
	
	//New method
	//Points to a filename (by it's name in the form of a string) 
	//Then iterates through the text file, reading each character and...
	// places it into an array of tiles)
	public void mapFromFile(String fileName, Tile[] map)
	{
		try{  //A try/catch block is needed in case the file it is looking for is not found.
			
			File currentMap = new File ("maps/"+fileName); //Uses the string to point to the actual file (/maps is where the files are held)
			fileInput = new Scanner(currentMap); //creates a scanner which will be used to iterate through the file

					for (cur = 0; cur < 169; cur++) { //currently, all our maps are a fixed size, 13 x 13.. 169 tiles total
						nextTile = fileInput.next(); //grabs the next character
						
						//Uses a switch statement to decide what tile type will be placed 
						 switch (nextTile) {
				         case "r":
				        	 map[cur] = r ;
				             break;
				         case "b":
				        	 map[cur] = b ;
				             break;
				         case "f":
				        	 map[cur] = f ;
				             break;
				         case "g":
				        	 map[cur] = g ;
				             break;
				         //other tiles can be added to this statement as neccesary
				 }
			}

			fileInput.close();				//Closes the file.
		
		}
		catch (FileNotFoundException e) { //Prints out an error if the file can't be found
			System.out.println(e);
			System.exit(1); // IO error; exit program
		} // end catch
	}
	
	
	/************************************************************
	 * Get a map back  based on its index in the array of maps
	 * 
	 * @param index - Position in the maps array
	 * @return - Map
	 *************************************************************/
	public Map getMap(int index) {
		return maps[index];
	}
}