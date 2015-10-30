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
	
	//Maps
	Map city1;
	Map city2;
	Map city3;
	Map cityO;
	Map houses;
	Map housesO;
	
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
	Event warpTest;
	Event warpTest2;
	Event warpTest3;
	Event warpTest4;
	Event warpTest5;
	Event warp1;
	Event warp2;
	Event getPotion;
	Event getMpotion;
	
	//Items
	Item potion;
	Item mpotion;
	
	//NPC's and Monsters
	Mob npc;
	
	//Array of maps
	public Map[] maps;
	
	
	//Julian changes to allow reading from a file
	private Scanner fileInput;			//Used to read in the maze from a text file
	private int cur;
	private String nextTile;
	
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
		
		//Set up spriteSheets
		misc = new SpriteSheet("/textures/environments/environments1.png", 16, 16, 16, scale);
		buildings = new SpriteSheet("/textures/environments/4x4buildings.png", 4, 4, 64, scale);
		environment32 = new SpriteSheet("/textures/environments/32SizeEnvironment.png", 8, 8, 32,scale);
		extras2 = new SpriteSheet("/textures/extras/extras2.png", 16, 16, 16, scale);
		mainCharacter = new SpriteSheet("/textures/characters/mainCharacter.png", 8, 8, 32, scale);
		
		//Set up tile blueprints and if they are animating
		d = new Tile(frame, g2d, "door", environment32, 0);
		f = new Tile(frame, g2d, "flower", misc, 1);
		g = new Tile(frame, g2d, "grass", misc, 0);
		b = new Tile(frame, g2d, "bricks", misc, 16, true);
		r = new Tile(frame, g2d, "walkWay", misc, 6);
		e = new Tile(frame, g2d, "empty", misc, 7);
		ro = new Tile(frame, g2d, "rock", misc, 2);
		h = new Tile(frame, g2d, "house", buildings, 0, true);
		hf = new Tile(frame, g2d, "floor", misc, 8);
		c = new Tile(frame, g2d, "chest", extras2, 0, true);
		
		
		
		//Creates array of tiles for each map
		Tile[] city1Tiles = new Tile[169];
		Tile[] city2Tiles = new Tile[169];
		Tile[] city3Tiles = new Tile[169];

		
		//testing for random map placement
		Random rn = new Random();
		//for(int x=0; x<100 ;x++)
		//System.out.println(rn.nextInt(3)+1);
		
		
		//fills the arrays with tiles based off of text files.

		
		//mapFromFile("map"+(rn.nextInt(3)+1)+".txt", city1Tiles);
/// Commented out for now, but it looks for a random text file between numbers 1 and 3,		
		
		mapFromFile("map1.txt", city1Tiles);
		mapFromFile("map2.txt", city2Tiles);
		mapFromFile("map3.txt", city3Tiles);
		
		
		
		//Put the maps together and add them to the array of possible maps
		
		city1 = new Map(frame, g2d, city1Tiles, 13, 13, "city");
		city2 = new Map(frame, g2d, city2Tiles, 13, 13, "city2");
		city3 = new Map(frame, g2d, city3Tiles, 13, 13, "city3");
		
		maps[0] = city1;
		maps[1] = city2;
		maps[2] = city3;
		
	//	cityO = new Map(frame, g2d, cityOTiles, 40, 40, "cityO");
	//	maps[1] = cityO;
	//	houses = new Map(frame, g2d, houseTiles, 6, 6, "houses");
	//	maps[2] = houses;
	//	housesO = new Map(frame, g2d, houseOTiles, 6, 6, "housesO");
	//	maps[3] = housesO;
		
		//Put together all items (Dont forget to add these to the count and setup methods in inGameMenu.java)
	//	potion = new Item(frame, g2d, extras2, 2, "Potion", false);
	//	potion.setHealItem(25, false, "");
	//	mpotion = new Item(frame, g2d, extras2, 2, "Mega Potion", false);
	//	potion.setHealItem(50, false, "");
		
		//Put together all events
		//Warping events
			warpTest = new Event("To2", TYPE.WARP);
			warpTest.setWarp("city2", "city2", 0, -190);
			
			warpTest2 = new Event("To1", TYPE.WARP);
			warpTest2.setWarp("city", "city", 0, +460);
			
			warpTest3 = new Event("To3", TYPE.WARP);
			warpTest3.setWarp("city3", "city", 0, +460);
			
			warpTest4 = new Event("To1", TYPE.WARP);
			warpTest4.setWarp("city", "city", 0, -190);
			
			city1.accessTile(8).addEvent(warpTest);
			city1.accessTile(164).addEvent(warpTest3);
			city2.accessTile(164).addEvent(warpTest2);
			city3.accessTile(8).addEvent(warpTest4);
			
	//	warp1 = new Event("fromHouse", TYPE.WARP);
	//	warp1.setWarp("city", "cityO", 200, -50);
	//	warp2 = new Event("toHouse", TYPE.WARP);
	//	warp2.setWarp("houses", "housesO", 620, 250);
		
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
	//	npc = new Mob(frame, g2d, mainCharacter, 40, TYPE.RANDOMPATH, "npc", false);
	//	npc.setMultBounds(6, 50, 92, 37, 88, 62, 92, 62, 96);
	//	npc.setMoveAnim(32, 48, 40, 56, 3, 8);
	//	npc.setHealth(60);
		
		//Add the mobs to their tile home
	//	city1.accessTile(20).addMob(npc);
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