import java.awt.Color;

/**
 * This class allows for the construction of an object that contains information
 * pertaining to a Pixel, utilized by the class makeArt for storing drawing
 * information.
 * 
 * Uses Color components as defined by the awt api for translating into usable formats.
 * 
 * @author Mac Clevinger
 * @version 1.2
 * @latest 2018-04-04
 */

public class Pixel {
							/** This instance variable contains the red component of a color defined by RGBA */
	private int red;		/** This instance variable contains the green component of a color defined by RGBA */
	private int green;		/** This instance variable contains the blue component of a color defined by RGBA */
	private int blue;		/** This instance variable contains a boolean representing whether a Pixel is transparent or not */
	private boolean alpha;
	
	/**
	 * This constructor takes in 3 integer values to assign to the Pixel object,
	 * defaulting the Alpha component to false.
	 * 
	 * @param r	- This parameter represents the Red component of the color defined by the Pixel object
	 * @param g	- This parameter represents the Green component of the color defined by the Pixel object
	 * @param b	- This parameter represents the Blue component of the color defined by the Pixel object
	 */
	
	public Pixel(int r, int g, int b){
		red = r >= 0 && r < 256 ? r : 0;
		green = g >= 0 && g < 256 ? g : 0;
		blue = b >= 0 && b < 256 ? b : 0;
		alpha = false;
	}
	
	/**
	 * This constructor takes in 3 integer values to assign to the Pixel object,
	 * and a boolean to assign to the Alpha component of the Pixel object.
	 * 
	 * @param r	- This parameter represents the Red component of the color defined by the Pixel object
	 * @param g	- This parameter represents the Green component of the color defined by the Pixel object
	 * @param b	- This parameter represents the Blue component of the color defined by the Pixel object
	 * @param a - This parameter represents the state of Alpha for the color defined by the Pixel object
	 */
	
	public Pixel(int r, int g, int b, boolean a){
		  setRed(r);
		  setGreen(g);
		  setBlue(b);
		  alpha = true;
	}
	
	/**
	 * This constructor takes a pre-existing Color object to strip the RGBA values from to
	 * assign to the Pixel object. Alpha is checked as a binary, converted to true or false
	 * corresponding to whether Alpha is 255 or not.
	 * 
	 * @param in - This parameter is a Color object translated to a Pixel object
	 */
	
	public Pixel(Color in){
		setRed(in.getRed());
		setGreen(in.getGreen());
		setBlue(in.getBlue());
		setAlpha(in.getAlpha() == 255 ? false : true);
	}
	
	/**
	 * This constructor takes in a pre-existing Color object to strip the RGB values from to assign
	 * to the Pixel object, and a boolean to assign to the Alpha value denoting whether the object
	 * is transparent or not.
	 * 
	 * @param in - This parameter is a Color object translated to the RGB components of the Pixel object
	 * @param alphaIn - This parameter is a boolean describing the Alpha value of the Pixel object
	 */
	
	public Pixel(Color in, boolean alphaIn){
		setRed(in.getRed());
		setGreen(in.getGreen());
		setBlue(in.getBlue());
		setAlpha(alphaIn);
	}
	
	/**
	 * This method returns the RGB components of the Pixel object as a Color object,
	 * assigning the Alpha value to 255 in all cases as transparency is handled by
	 * checking the boolean value, not actually representing partial transparency.
	 * 
	 * @return - Returns a Color object composed of the RGB elements of the Pixel object.
	 */
	
	public Color getColor(){
		return new Color(red, green, blue, 255);
	}
	
	/**
	 * This method returns the Red component of the Pixel object.
	 * 
	 * @return - Returns an integer representing the Red component of the Pixel object.
	 */
	
	public int getRed(){
		return red;
	}
	
	/**
	 * This method returns the Blue component of the Pixel object.
	 * 
	 * @return - Returns an integer representing the Blue component of the Pixel object.
	 */
	
	public int getGreen(){
		return green;
	}
	
	/**
	 * This method returns the Green component of the Pixel object.
	 * 
	 * @return - Returns an integer representing the Green component of the Pixel object.
	 */
	
	public int getBlue(){
		return blue;
	}
	
	/**
	 * This method returns the Alpha component of the Pixel object as a boolean.
	 * 
	 * @return - Returns a boolean representing the Alpha component of the Pixel object.
	 */
	
	public boolean getAlpha(){
		return alpha;
	}
	
	/**
	 * This method takes in a new integer value to assign to the red instance variable.
	 * 
	 * @param r - This parameter represents the new Red value for the Pixel object.
	 */
	
	public void setRed(int r){
		red = r >= 0 && r < 256 ? r : 0;
	}

	/**
	 * This method takes in a new integer value to assign to the green instance variable.
	 * 
	 * @param r - This parameter represents the new Green value for the Pixel object.
	 */
	
	public void setGreen(int g){
		green = g >= 0 && g < 256 ? g : 0;
	}

	/**
	 * This method takes in a new integer value to assign to the blue instance variable.
	 * 
	 * @param r - This parameter represents the new Blue value for the Pixel object.
	 */
	
	public void setBlue(int b){
		blue = b >= 0 && b < 256 ? b : 0;
	}

	/**
	 * This method takes in a new double value to assign to the alpha instance variable.
	 * 
	 * @param r - This parameter represents the new Alpha value for the Pixel object as a boolean.
	 */
	
	public void setAlpha(boolean a){
		alpha = a;
	}
}
