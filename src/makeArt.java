import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import acm.program.*;
import acm.graphics.*;
import acm.io.IODialog;
import javax.swing.*;
import java.util.*;
import java.awt.Color;
import java.awt.Font;								//Imports to make everything work
import java.awt.image.*;

/**
 * This class permits the user to utilize an environment designed to assist in graphical design
 * at the level of accessing individual pixels that can be resized for greater control.
 * 
 * Uses a combination of the acm and javax.swing api's for functionality, extending the
 * GraphicsProgram class for base functionality.
 * 
 * @author Mac Clevinger
 * @version 1.3
 * @latest 2018-04-03
 *
 */

//Drawing type: Rectangle, Circle

public class makeArt extends GraphicsProgram{
													/** Constant value for maintaining consistency in UI elements sizing */ 
	private final int SIZE_UI = 10;					/** Constant value for the rate at which RGB color values are adjusted */
	private final int COLOR_CHANGE_RATE = 3;		/** Constant value for height adjustment regarding the header */		
	private final int VERTICAL_ADJUSTMENT = 59;		/** Constant value for reserving vertical space for UI elements */
	private final int VERTICAL_UI_SPACE = 60;		/** Constant value for reserving horizontal space for UI elements */
	private final int HORIZONTAL_UI_SPACE = 120;		/** Constant values defining the X positions of UI elements */
	private final int[] UI_SPACING = {0, 45, 91, 136, 190, 238, 293, 368, 448, 540};	/** Constant value defining the Font used for all text */
	private final Font FONT_DEFAULT = new Font("Sans Serif", Font.BOLD, 14);	/** Constant value defining the Y position of UI elements */
	private final int UI_HEIGHT = 18;				/** Constant value defining the width of the screen when in the Help menu */
	private final int HELP_X = 825;					/** Constant value defining the height of the screen when in the Help menu */
	private final int HELP_Y = 810;					/** Constant value for default screen width */
	private final int SCREEN_X_DEFAULT = 700;		/** Constant value for default screen height */
	private final int SCREEN_Y_DEFAULT = 725;
													/** gridSizeX - Current size of drawing grid Width */
	private int gridSizeX;							/** gridSizeY - Current size of drawing grid Height */
	private int gridSizeY;							/** screenX - Current size of the overall screen Width */
	private int screenX;							/** screenY - Current size of the overall screen Height */
	private int screenY;							/** drawColor - Currently selected draw color of the ten saved Color objects, indexes an array */
	private int drawColor;							/** sqrSize - Current size of the side-lengths of a square in the drawing grid */
	private int sqrSize;							/** drawingSize - Current size of the 'pen'; describes size of region that is affected by drawing */
	private int drawingSize;						/** dragX - Starting X location of a drag event (user is selecting a region of space) */
	private int dragX;								/** dragY - Starting Y location of a drag event (user is selecting a region of space) */
	private int dragY;								/** grid - boolean state describing whether the grid-lines are shown on the drawing grid */
	private boolean grid;							/** help - boolean state describing whether the help screen is being portrayed */
	private boolean help;							/** showTrans - boolean state describing whether the transparency of each grid position is being shown */
	private boolean showTrans;						/** shadeIn - boolean state describing whether the user is able to overwrite the color black or not */	
	private boolean shadeIn;						/** colorPick - boolean state permitting the user to select a color in their drawing space to copy */
	private boolean colorPick;						/** dragShape - boolean state permitting the user to define a rectangular region to fill */
	private boolean dragShape;						/** dragShapeHollow - boolean state permitting the user to define a rectangular region to outline */
	private boolean dragShapeHollow;				/** relocate - boolean state permitting the user to define a region to copy */
	private boolean relocate;						/** replace - boolean state permitting the user to paste the region previously copied via relocate */
	private boolean replace;						/** toggleShape - boolean state cycling through drawing shapes: Square, Circle */
	private boolean toggleShape;					/** scaledSave - boolean state for whether the system should request a scaling value when saving */
	private boolean scaledSave;						/** drawCanvas - 2D array of Pixel objects (custom class) that records the user's drawing*/
	private Pixel[][] drawCanvas;					/** subImage - sub-space of drawCanvas's 2D array of pixels defined by the user for reprinting */
	private Pixel[][] subImage;						/** savedColors - 1D array of Colors that records the currently saved */
	private Color[] savedColors;
	
	/**
	 * This method initiates the construction of the interface by calling start().
	 * 
	 * @param args
	 */
	
	public static void main(String[] args){	
		makeArt art = new makeArt();
		art.start(args);
	}
	
	/**
	 * This method constructs the interface, initializes values, and adds listeners for user interaction.
	 * 
	 */
	
	public void run(){	
		addMouseListeners();
		addKeyListeners();
		initialize();
		setSize(screenX + HORIZONTAL_UI_SPACE, screenY + VERTICAL_ADJUSTMENT + VERTICAL_UI_SPACE);
	}
	
	/**
	 * This method initializes the values used for simulating the program's behaviors, requesting user
	 * input for the default size of the grid and using that input to calculate the size of each grid-tile
	 * based on the relation of the screen's size to how many there are.
	 * Further, it fills the drawCanvas array which contains a representation of the image the user is
	 * drawing with default, transparent values, and defines the default colors the user can access.
	 * Finally, it calls reInit, which is composed of the values and actions that can be set to reset the program
	 * to a default state at a new size, used here for initiating. The numerous states are mostly set to 'off', the grid
	 * being an exception. Calculations are performed, derived from the values so far defined, to finalize the program's
	 * preperations.
	 * 
	 */
	
	private void initialize(){	
		screenX = SCREEN_X_DEFAULT;
		screenY = SCREEN_Y_DEFAULT;
		drawColor = 0;
		drawingSize = 1;
		gridSizeX = Integer.parseInt(JOptionPane.showInputDialog("Width of the grid."));
		gridSizeY = Integer.parseInt(JOptionPane.showInputDialog("Height of the grid."));
		sqrSize = screenX/gridSizeX;
		drawCanvas = new Pixel[gridSizeX][gridSizeY];
		savedColors = new Color[]{Color.BLACK, Color.WHITE, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY, Color.MAGENTA, new Color(255, 255, 255,0)};
		for(int i = 0; i < gridSizeX; i++){
		  for(int j = 0; j < gridSizeY; j++){
			  drawCanvas[i][j] = new Pixel(255, 255, 255, true);
		  }
		}
		reInit();
	}

	/**
	 * This method contains the behaviors necessary to 'reset' the program when appropriate, and
	 * is also used in the initial set-up of the program. The various states are returned to default values
	 * (equating to 'off', save for the grid display), and values are recalculated according to their
	 * relations to other defined values.
	 * 
	 */
	
	private void reInit(){
		grid = true;
		help = false;
		shadeIn = false;
		showTrans = false;
		updatesqrSize();
		gridOverlay();
		rebuildScreen();
		displayStates();
	}
	
	/**
	 * This method handles user input in the form of a mouse press, checking what
	 * state the program is in to know how to interpret the input. Input is filtered
	 * into a format communicable with the drawCanvas Pixel[][] array, finding where the
	 * value corresponds based on the current size of the grid-tiles. 
	 * If a drag event is occurring, it stores the location of the click in dragX/Y to be
	 * used when the mouse click is released.
	 * If the current state is the pasting of a previously saved image, then at the
	 * click location the program redraws the image.
	 * Otherwise, it performs a 'normal' clickEvent that alters the color of the affected
	 * grid-tile by calling mouseAction(int, int). 
	 * 
	 * @parameter e This parameter is a MouseEvent, storing information about the user's click action.
	 */
	
	public void mousePressed(MouseEvent e){
		int x = e.getX() / (sqrSize);
		int y = (e.getY() - VERTICAL_UI_SPACE) / (sqrSize);
		System.out.println(x + " " + y);
		if(dragShape || dragShapeHollow || relocate){
			dragX = x;
			dragY = y;
		}
		else if(replace){
			replace = false;
			for(int i = x; i < x + subImage.length; i++){
				for(int j = y; j < y + subImage[0].length; j++){
					if(i >= drawCanvas.length || j >= drawCanvas[0].length || i < 0 || j < 0)
					  continue;
					drawCanvas[i][j] = subImage[i - x][j - y];
				}
			}
		}
		else{
		    mouseAction(x, y);
		}
	}
	
	/**
	 * This method handles user input in the form of the release of a mouse press, recording
	 * the location where it was released and converting it to indexes of the drawCanvas array.
	 * Based on the state of the program, the method may either draw a rectangle from the pressed location
	 * to the release location, draw a hollow rectangle in that region, or store the values in that region into
	 * a secondary array for later printing.
	 * 
	 * 
	 * @parameter e This parameter is a MouseEvent, storing information about the user's click action.
	 */
	
	public void mouseReleased(MouseEvent e){
		int x = e.getX() / (sqrSize);
		int y = (e.getY() - VERTICAL_UI_SPACE) / (sqrSize);
		if(x < dragX){
			int copy = x;
			x = dragX;
			dragX = copy;
		}
		if(y < dragY){
			int copy = y;
			y = dragY;
			dragY = copy;
		}
		if(dragShape){
			for(int i = dragX; i < x+1; i++){
				for(int j = dragY; j < y+1; j++){
					drawCanvas[i][j] = new Pixel(savedColors[drawColor]);
				}
			}
			dragShape = false;
		}
		else if(dragShapeHollow){
			for(int i = dragX; i < x+1; i++){
				for(int j = dragY; j < y+1; j++){
					if((i == dragX || i == x || j == dragY || j == y) && i < gridSizeX && j < gridSizeY && i >= 0 && j >= 0)
					  drawCanvas[i][j] = new Pixel(savedColors[drawColor]);
				}
			}
			dragShapeHollow = false;
		}
		else if(relocate){
			subImage = new Pixel[x - dragX][y - dragY];
			for(int i = dragX; i < x; i++){
				for(int j = dragY; j < y; j++){
					subImage[i - dragX][j - dragY] = drawCanvas[i < drawCanvas.length ? i : drawCanvas.length][j < drawCanvas[0].length ? j : drawCanvas[0].length];
				}
			}
			replace = true;
			relocate = false;
		}
		rebuildScreen();
		displayStates();
	}
	
	/**
	 * This method handles the user input in the form of a mouse being dragged while
	 * the click is held. Based on the state of the program, this input may be ignored
	 * if the user is defining a space to to act upon; otherwise, wherever input is picked up,
	 * the program will draw the currently selected color via mouseAction(int, int).
	 * 
	 * @parameter e This parameter is a MouseEvent, storing information about the user's click action.
	 */
	
	public void mouseDragged(MouseEvent e){		
		int x = e.getX() / (sqrSize);
		int y = (e.getY() - VERTICAL_UI_SPACE) / (sqrSize);
		if(!dragShape && !dragShapeHollow && !relocate && x < gridSizeX && y < gridSizeY){
		  mouseAction(x, y);	
		}
	}
	
	/**
	 * This method is called from a mouseEvent handling method, receiving the location
	 * of the event in terms of the drawCanvas array indexes.
	 * Regular operation is to alter the color of the selected grid-tiles, the span
	 * so defined by the current drawingSize centered around the selected tile.
	 * If the state of the program is such that colorPick is active, then the program
	 * takes the grid-tile that was selected and copies its color into the currently
	 * active Color slot.
	 * 
	 * @param x This parameter represents the x position that the user accessed in the drawCanvas array
	 * @param y This parameter represents the y position that the user accessed in the drawCanvas array
	 */
	
	private void mouseAction(int x, int y){	
		if(colorPick){
			Color stolen = drawCanvas[x][y].getColor();
			savedColors[drawColor] = stolen;
			colorPick = !colorPick;
			rebuildScreen();
			displayStates();
			return;
		}
		for(int i = (drawingSize) / -2; i < (drawingSize+1)/2; i++){
			for(int j = (drawingSize) / -2; j < (drawingSize+1)/2; j++){
				if(x + i < gridSizeX && y + j < gridSizeY && x + i >= 0 && y + j >= 0 && (!shadeIn || !drawCanvas[x + i][y + j].getColor().equals(Color.black))){
				  GRect rect = new GRect((x + i)*gridSizeX, (y + j)*gridSizeY, sqrSize, sqrSize);
				  rect.setFillColor(savedColors[drawColor]);
				  rect.setColor(savedColors[drawColor]);
				  rect.setFilled(true);
				  if(!toggleShape || (double)Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2)) < ((double)drawingSize/2)-.1){
				    if(x + i < gridSizeX && y + j < gridSizeY && x + i >= 0 && y + j >= 0){
				      drawCanvas[x + i][y + j] = new Pixel(savedColors[drawColor]);
				    }
				    add(rect);
				  }
				}
			}
		}
		rebuildScreen();
		displayStates();
	}
	
	/**
	 * This method handles user input in the form of a keystroke, being programmed to recognize
	 * certain keys as significant and react accordingly to their input. A considerable level of
	 * functionality is implemented here, the features described to the user in the help-page
	 * accessed by pressing '?'.
	 * 
	 * @parameter e This parameter is a KeyEvent, storing information about the user's key press action
	 */
	
	public void keyPressed(KeyEvent e){	
		switch(e.getKeyChar()){
		  case '?': help();				//Opens a help screen
		  			return;
		  case '.': grid = !grid;		//Flips the state of grid
		  			rebuildScreen();
		  			break;
		  case '`': printOut();			//Lets the user save the image they've made
		  			break;
		  case '~': readIn();			//Lets the user feed an image into the program to edit
		  			break;
		  case '1': drawColor = 0; 		//Number keys allow the user to select which color they want to use
		  			break;
		  case '2': drawColor = 1; 
		  			break;
		  case '3': drawColor = 2;  
					break;
		  case '4': drawColor = 3; 
		  			break;
		  case '5': drawColor = 4;  
					break;
		  case '6': drawColor = 5;  
					break;
		  case '7': drawColor = 6;  
					break;
		  case '8': drawColor = 7;  		//Following are the adjustment-of-color keys, 'q' 'w' affecting red, 'a' 's' affecting green, and 'z' 'x' for blue 
					break;
		  case '9': drawColor = 8;  		//The former of each reduces that component of the color by an amount defined by the constant COLOR_CHANGE_RATE
					break;
		  case '0': drawColor = 9;  		//The latter increases that component in the same manner.
					break;
		  case 'q': if(savedColors[drawColor].getRed() - COLOR_CHANGE_RATE >= 0)
			  			savedColors[drawColor] = new Color(savedColors[drawColor].getRed() - COLOR_CHANGE_RATE, savedColors[drawColor].getGreen(), savedColors[drawColor].getBlue());
		  			break;
		  case 'w': if(savedColors[drawColor].getRed() + COLOR_CHANGE_RATE <= 255)
			  			savedColors[drawColor] = new Color(savedColors[drawColor].getRed() + COLOR_CHANGE_RATE, savedColors[drawColor].getGreen(), savedColors[drawColor].getBlue());
		   			break;
		  case 'a': if(savedColors[drawColor].getGreen() - COLOR_CHANGE_RATE >= 0)
			  			savedColors[drawColor] = new Color(savedColors[drawColor].getRed(), savedColors[drawColor].getGreen() - 3, savedColors[drawColor].getBlue());
		  			break;
		  case 's': if(savedColors[drawColor].getGreen() + COLOR_CHANGE_RATE <= 255)
			  			savedColors[drawColor] = new Color(savedColors[drawColor].getRed(), savedColors[drawColor].getGreen() + 3, savedColors[drawColor].getBlue());
		  			break;
		  case 'z': if(savedColors[drawColor].getBlue() - COLOR_CHANGE_RATE >= 0)
			  			savedColors[drawColor] = new Color(savedColors[drawColor].getRed(), savedColors[drawColor].getGreen(), savedColors[drawColor].getBlue() - 3);
		  			break;
		  case 'x': if(savedColors[drawColor].getBlue() + COLOR_CHANGE_RATE <= 255)
			  			savedColors[drawColor] = new Color(savedColors[drawColor].getRed(), savedColors[drawColor].getGreen(), savedColors[drawColor].getBlue() + 3);
		  			break;
		  case 'r': gridSizeX = Integer.parseInt(JOptionPane.showInputDialog("New width of grid"));
		  			gridSizeY = Integer.parseInt(JOptionPane.showInputDialog("New height of grid"));
		  			adjustRebuild();
		  			break;
		  case '-': gridSizeX++;				//Three options are present for adjusting the size of the grid: The user can press 'r' to submit new 
		  			break;
		  case '_': if(gridSizeX - 1 > 0)		//values by typing in their size, or can use '_' and '-' to adjust the width (reduce and increase respectively)
			  		  gridSizeX--;
		  			break;
		  case '=': gridSizeY++;				//or use '+' and '=' to do the same for its height.
		  			break;
		  case '+': if(gridSizeY - 1 > 0)
			  			gridSizeY--;
		  			break;
		  case '<': if (screenX - 50 > 0)		//The user can also adjust the screen's size, which may change the size of the tiles
			  			screenX -= 50;
		  			break;
		  case '>': screenX += 50;
		  			break;
		  case '[': if(screenY - 50 > 0)
				  		screenY -= 50; 
		  			break;
		  case ']': screenY += 50;
		  			break;
		  case 'k': drawingSize -= drawingSize - 1 > 0 ? 1 : 0;		//'k' and 'l' allow for the user to adjust their pen size, to draw smaller/larger
		  			break;
		  case 'l': drawingSize += (drawingSize < gridSizeX && drawingSize < gridSizeY) ? 1 : 0;
		  			break;
		  case 't': showTrans = !showTrans;		//All following keys are for affecting the state and performing special functions in the program
		  			break;						//Show transparency of tiles?
		  case 'g': shadeIn = !shadeIn;
		  			break;						//Make black un-changeable for outlining?
		  case 'b': colorPick = !colorPick;
		  			break;						//Select a color on screen to overwrite current selection?
		  case 'f': dragShape = !dragShape;
		  			dragShapeHollow = false;	//Select a region to fill in with current color?
		  			relocate = false;
		  			break;
		  case 'v': dragShapeHollow = !dragShapeHollow;
		  			dragShape = false;			//Select a region to outline with current color?
		  			relocate = false;
		  			break;
		  case 'y': relocate = !relocate;
		  			dragShape = false;			//Select a region to store and print elsewhere?
		  			dragShapeHollow = false;
		  			break;
		  case 'h': toggleShape = !toggleShape;
		  			break;
		  case 'n': scaledSave = !scaledSave;
		  			break;
		  case 'm': mirrorImage();
		  			break;
		  case 'c': randomColor();
		  			break;
		  default: break;
		}
		updatesqrSize();										//If values have changed, may need to re-adjust the tile size for visibility
		if(gridSizeX != drawCanvas.length || gridSizeY != drawCanvas[0].length){	
		  adjustRebuild();}										//If values have changed, storage arrays may need to be adjusted to fit all data
		setSize(screenX + HORIZONTAL_UI_SPACE, screenY + VERTICAL_ADJUSTMENT + VERTICAL_UI_SPACE);		//Resizes screen to adhere to current values
		rebuildScreen();										//Repaints the drawing with respect to changes	
		displayStates();											//Show the RGB for the currently selected Color
	}
	
	/**
	 * This method accesses the savedColors array to display the currently
	 * stored Colors to the user, and signal to them which Color is the
	 * actively selected one.
	 * 
	 */
	
	public void randomColor(){
		Random rand = new Random();
		Color newCol = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		savedColors[drawColor] = newCol;
	}
	
	private void displaySaved(){
		GRect rect;
		for(int i = 0; i < savedColors.length; i++){
			rect = new GRect(screenX + sqrSize, SIZE_UI * i*2, SIZE_UI*2, SIZE_UI*2);
			rect.setFillColor(savedColors[i]);
			rect.setColor(savedColors[i]);
			rect.setFilled(true);
			add(rect);
			if(i == drawColor){
				  GRect border;
				  border = new GRect(rect.getX(), rect.getY(), SIZE_UI/2, SIZE_UI/2);
				  border.setFilled(true);
				  border.setFillColor(Color.white);
				  add(border);
			}
		}
	}
		
	/**
	 * This method constructs and adds to the screen three GLabels containing the RGB
	 * information of the currently selected Color.
	 * 
	 */
	
	private void displayStates(){	
		GLabel[] printStuff = new GLabel[10];
		String[] currentStates = {"R:" + Integer.toString(savedColors[drawColor].getRed()),
								  "G:" + Integer.toString(savedColors[drawColor].getGreen()),
								  "B:" + Integer.toString(savedColors[drawColor].getBlue()),
								  "x: " + gridSizeX,
								  "y: " + gridSizeY,
								  "Pen: " + drawingSize,
								  "Trans: " + (showTrans ? "On" : "Off"),
								  "Shade: " + (shadeIn ? "On" : "Off"),
								  "Drag: " + (dragShape ? "Fill" : dragShapeHollow ? "Outline" : relocate ? "Select" : replace ? "Paste" : "Off"),
								  "Shape: " + (toggleShape ? "Circ" : "Rect"),
								  };
		for(int i = 0; i < printStuff.length; i++){
			printStuff[i] = new GLabel(currentStates[i], UI_SPACING[i], UI_HEIGHT);
			printStuff[i].setFont(FONT_DEFAULT);
			add(printStuff[i]);
		}
		displaySaved();
	}

	/**
	 * This method draws the grid overlay if so desired, outlining each
	 * tile in the grid with intersecting GLine objects.
	 * 
	 */

	private void gridOverlay(){	
		for(int i = 0; i <= gridSizeY; i++){
		  add(new GLine(0, sqrSize*i + VERTICAL_UI_SPACE, sqrSize * gridSizeX, sqrSize*i + VERTICAL_UI_SPACE));
		  if(i != gridSizeX){
		    for(int j = 0; j <= gridSizeX; j++){
			  add(new GLine(sqrSize*j,VERTICAL_UI_SPACE, sqrSize*j, sqrSize * gridSizeY + VERTICAL_UI_SPACE));}
		    }
		}
	}
	
	/**
	 * This method checks the ratios of the width and height of the screen to the
	 * width and height of the drawing-grid to find the smaller ratio of the two to
	 * assign as the tile size; this ensures that the grid will fit on the screen.
	 * 
	 */	
	
	private void updatesqrSize(){	
	  if(sqrSize < screenX/gridSizeX && sqrSize < screenY/gridSizeY)
		sqrSize = 0;
	  if(screenX/gridSizeX < screenY/gridSizeY)
		sqrSize = screenX/gridSizeX;
	  else
		sqrSize = screenY/gridSizeY;
	}

	/**
	 * This method is called when the drawCanvas 2D array needs to be resized, transferring
	 * the old contents to a new Pixel[][] array before overwriting the old array with the
	 * new one. If made larger, the new tiles are given the default color of transparent white,
	 * otherwise the bottom and right side edges get sheared off in the new array.
	 * 
	 */

	private void adjustRebuild(){
		Pixel[][] newCanvas = new Pixel[gridSizeX][gridSizeY];
		for(int i = 0; i < newCanvas.length; i++){
			for(int j = 0; j < newCanvas[0].length; j++){
				newCanvas[i][j] = (i < drawCanvas.length && j < drawCanvas[0].length) ? drawCanvas[i][j] : new Pixel(255, 255, 255, true);
			}
		}
		drawCanvas = newCanvas.clone();
	}
	
	/**
	 * This method wipes the current contents away from the screen and redraws the possibly
	 * altered image to it, alongside any UI elements if so desired by the current state.
	 * The UI is handled by method displayStates() and displaySaved(), while the image itself is
	 * performed by iterating through drawCanvas and, based on the state of showTrans, will either
	 * display colors as normal or display any transparent tiles as the color stored in the 0'th
	 * index of savedColors[].
	 * 
	 */

	private void rebuildScreen(){
		removeAll();
		for(int i = 0; i < gridSizeX; i++){
		  for(int j = 0; j < gridSizeY; j++){
			if(showTrans && drawCanvas[i][j].getAlpha() == true){
			  GRect tran = new GRect(i * sqrSize, j * sqrSize + VERTICAL_UI_SPACE, sqrSize, sqrSize);
			  tran.setFilled(true);
			  tran.setColor(savedColors[0]);
			  tran.setFillColor(savedColors[0]);
			  add(tran);
			}
			else{
			  GRect norm = new GRect(i * sqrSize, j * sqrSize + VERTICAL_UI_SPACE, sqrSize, sqrSize);
			  norm.setFilled(true);
			  norm.setColor(drawCanvas[i][j].getColor());
			  norm.setFillColor(drawCanvas[i][j].getColor());
			  add(norm);
			}
		  }
		}
		if(grid == true)
		  gridOverlay();
		displayStates();
	}
	
	/**
	 * This method saves the image to a location defined by the user with a name also
	 * defined by the user, requested via a file chooser and a text prompt. This is performed by 
	 * generating a 1D integer array that is filled with the RGBA values from drawCanvas' Pixel
	 * objects, passing that array to the WritableRaster for a BufferedImage which is then
	 * saved via ImageIO as a .png file.
	 * The grid is turned off before this operation is performed, and reverted to the original state
	 * afterwards.
	 * 
	 * 
	 */
	
	private void printOut(){	
		boolean origState = grid;
		int scaleSave = 1;
		grid = false;
		rebuildScreen();
		IODialog dialog = new IODialog();
		String name = dialog.readLine("Name your file.");
		String scale = "1";
		if(scaledSave){
			scale = dialog.readLine("What scale do you want to save the image at? Submit an Integer value greater than 0. If illegal entry, will use 1.");
		}
		try{
			scaleSave = Integer.parseInt(scale);
		}
		catch(Exception e){
			e.printStackTrace();
			scaleSave = 1;
		}
	    JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
	    chooser.showSaveDialog(null);
	    String location = chooser.getSelectedFile().getAbsolutePath();
	    Pixel[][] temp;
		try{
		  int[] myPixelData = new int[drawCanvas.length * drawCanvas[0].length * 4 * scaleSave * scaleSave];
		  temp = new Pixel[gridSizeX * scaleSave][gridSizeY * scaleSave];
		  for(int i = 0; i < temp.length; i++){
			  for(int j = 0; j < temp[0].length; j++){
				  temp[i][j] = new Pixel(Color.BLACK);
			  }
		  }
		  for(int i = 0; i < gridSizeX; i++){
			  for(int j = 0; j < gridSizeY; j++){
				  for(int k = 0; k < scaleSave; k++){
					  for(int l = 0; l < scaleSave; l++){
						  temp[i * scaleSave + k][j * scaleSave + l] = drawCanvas[i][j];
					  }
				  }
			  }
		  }
		  for(int i = 0; i < temp[0].length; i++){
			  for(int j = 0; j < temp.length; j++){
				    myPixelData[4 * i * temp.length + j * 4] = temp[j][i].getRed();
				    myPixelData[4 * i * temp.length + j * 4 + 1] = temp[j][i].getGreen();
				    myPixelData[4 * i * temp.length + j * 4 + 2] = temp[j][i].getBlue();
				    myPixelData[4 * i * temp.length + j * 4 + 3] = temp[j][i].getAlpha() ? 0 : 255;

			  }
		  }
		  BufferedImage image = new BufferedImage(temp.length, temp[0].length, BufferedImage.TYPE_INT_ARGB);
		  WritableRaster raster = (WritableRaster) image.getRaster();
		  raster.setPixels(0, 0, temp.length, temp[0].length, myPixelData);
		  ImageIO.write(image, "png", new File(location + "\\" + name + ".png"));
		} catch(Exception e){
			e.printStackTrace();
		}
		grid = origState;
		rebuildScreen();
	}
	
	/**
	 * This method lets the user specify a file in their file system to read into
	 * the program and edit graphically, reading it in as a BufferedImage and using
	 * its metadata to describe the size of an array to contain it which then
	 * creates new Pixel objects to populate itself based on the individual color
	 * values of each pixel in the BufferedImage.
	 * After, reInit() is called to initialize the program back to a default state.
	 * 
	 */
	
	private void readIn(){
		BufferedImage inputImage = null;
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(null);
	    chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
		try{
			inputImage = ImageIO.read(chooser.getSelectedFile());
		}
		catch(Exception e){
			return; 
		}
		gridSizeX = inputImage.getWidth();
		gridSizeY = inputImage.getHeight();
		drawCanvas = new Pixel[gridSizeX][gridSizeY];
		for(int i = 0; i < gridSizeX; i++){
			for(int j = 0; j < gridSizeY; j++){
				drawCanvas[i][j] = new Pixel(new Color(inputImage.getRGB(i, j), true));
			}
		}
		reInit();
	}
	
	/**
	 * This method is used to display the help screen to the user, accessed via the
	 * key prompt '?' which writes over the image and UI to display a list of key commands
	 * and what they do. Pressing the key again exits out of the help screen.
	 * 
	 */
	
	private void mirrorImage(){
		Pixel[][] mirroredCopy = new Pixel[drawCanvas.length][drawCanvas[0].length];
		for(int i = 0; i < drawCanvas.length; i++){
		  for(int j = 0; j < drawCanvas[i].length; j++){
			  mirroredCopy[drawCanvas.length - 1 - i][j] = drawCanvas[i][j];
		  }
		}
		drawCanvas = mirroredCopy;
	}
	
	private void help(){
		if(!help){
		  help = true;
		  removeAll();
		  setSize(HELP_X, HELP_Y);
		  String[] hold = new String[]{"` - Requests a name for your file and a location to save it to.",  "~ - Requests a file to read into the program for editing. (Saving does not overwrite the old image).", 
		  "1-9 - Switch between saved colors. (0 is for transparency)", "q/w - Increase/Decrease amount of Red in selected color.", 
		  "a/s - Increase/Decrease amount of Green in selected color.", "z/x - Increase/Decrease amount of Blue in selected color.", 
		  "-/_ - Increase/Decrease the Number of Horizontal Grids", "+/= - Increase/Decrease the Number of Vertical Grids", 
		  "</> - Increase/Decrease the Screen Width of the Application.", "[/] = Increase/Decrease the Screen Height of the Application.", 
		  "k - Shrink Pen Size by 1", "l - Increase Pen Size by 1", "r - Resize the Grid's X and Y components by Text Input. (retains image still within bounds)", 
		  ". - Turns On/Off the Overlaying Grid from the Screen.", "t - Turns On/Off displaying the Transparent Tiles as the Color in slot 1. (default Black)",
		  "g - Turns on/off shading mode, where black can't be overwritten", "b - Activates/Deactivates Color Picker Mode; turns current Color Slot into that of chosen Pixel",
		  "f - Begin selecting a Region to Fill; click and release to define two corners", "v - Begin selecting a Region to Outline; click and release to define two corners.", 
		  "y - Begin selecting a Region to Copy; click and release to define two corners. The next Mouseclick will paste it.", "h - Cycle between Square and Circular Pen shapes",
		  "n - Toggles saving the image at a scale you may specify after pressing `","? - Help screen toggle. Press any key to exit the Help Screen.", };
		  GLabel label = new GLabel(hold[0]);
		  double height = label.getHeight();
		  for(int i = 0; i < hold.length; i++){
			label = new GLabel(hold[i], 20, 20 + i * height * 2);
			label.setFont(FONT_DEFAULT);
			add(label);
		}
	  }
		else{
			help = false;
			rebuildScreen();
		}
	}
}
