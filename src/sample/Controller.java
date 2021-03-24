package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/*
Notes: the box names start at 1, the code indexes start at 0;
  ie. box 1 (top left) is the box indexed 0 therefore is controlled by;
  music[0], enable[0] etc...
 */
public class Controller {
//Application Content Variables
    String artistName = "Harry Carney";
    //TODO: string for path of mp3 audio file for box if applicable
    /*init*/       String initAudio = "src/Music/countryRoads.mp3";
    /*TopLeft*/    String box1audio = "src/Music/countryRoads.mp3";
    /*MidLeft*/    String box2audio = "src/Music/countryRoads.mp3";
    /*BotLeft*/    String box3audio = "src/Music/cotb.mp3";
    /*BigCenter*/  String box4audio = "src/Music/LunarWhispersMinor.mp3";
    /*LeftCenter*/ String box5audio = "src/Music/countryRoads.mp3";
    /*RightCenter*/String box6audio = "src/Music/countryRoads.mp3";
    /*TopRight*/   String box7audio = "src/Music/Zombie.mp3";
    /*MidRight*/   String box8audio = "src/Music/hhp.mp3";
    /*BotRight*/   String box9audio = "src/Music/cotb.mp3";

    int maxDistance = 400; //Sets the maximum distance in pixels for which a box will play its audio


//Application Framework Variables
    //Run Variables
    public Rectangle introBox; //FXML Box for Intro Text
    public Rectangle box1,box2,box3,box4,box5,box6,box7,box8,box9; //FXML Boxes for audio zones

    public Button introButton; //FXML button to dismiss intro text

    public Text IntroBoxText; //String to be displayed in intro text (set later)

    public MediaPlayer mp3Player1,mp3Player2,mp3Player3,mp3Player4,mp3Player5,mp3Player6,mp3Player7,mp3Player8,mp3Player9; //Media Players for respective boxes
    public MediaView vid; //Media Player for video (not currently in use)

    public Boolean boxEnable1 = false,boxEnable2 = false,boxEnable3 = false,boxEnable4 = false,boxEnable5 = false,boxEnable6 = false,boxEnable7 = false,boxEnable8 = false,boxEnable9 = false; //Boolean indicating if each box contains content or not, modified automatically by code
    public Boolean initAudioPresent = !initAudio.isEmpty(); //activates audio first time if background init audio supplied
    public Boolean playersInitialized = false; //kicks application to run sequence once players initialized
    public Boolean introDone = false; //kicks application to main screen once user dismisses introduction text

    int numberBoxes = 9; //Number of interactive zones
    int currClosest = 0; //ID of closest box
    int nextClosest=0; //ID of second closest box
    int lastClosest = -1; //ID of the box user was closest to prior to this box

    double maxVolume = 1; //maximum volume, given as a decimal (ie 1 = 100%, 0.5 = 50%)
    double[] volume = new double[numberBoxes]; //volume of each box, controlled dynamically
    double balanceShift = 0.75; //Balance shift for left hand side boxes, and right hand side boxes (0.0 is center, 1 = fully shifted to the appropriate side). side pictures shifted by 0.5 this value.
    double[] x = new double[numberBoxes]; //Array of x coordinate values for the center of each box (pixels)
    double[] y = new double[numberBoxes]; //Array of y coordinate values for the center of each box (pixesl)
    double[] d = new double[numberBoxes]; //array of distances to each box coordinates, from mouse (pixels)
    double mX, mY; //x coordinate, and y coordinate of the mouse location (pixels)
    double dClosest, d2Closest; //distances to the closest two boxes

    String bip = ""; //Instantaneous variable for creating audio players during init

    String[] music = {box1audio,box2audio,box3audio,box4audio,box5audio,box6audio,box7audio,box8audio,box9audio}; //array for ease of access to above defined audio strings
    Boolean[] enable = {boxEnable1,boxEnable2, boxEnable3, boxEnable4, boxEnable5, boxEnable6, boxEnable7, boxEnable8, boxEnable9}; //array for ease of access to enable for each box



    //Debug Variables
    Boolean debugEnable = false; //TODO: Debug flag
    public Text debugText1, debugText2, debugText3; //Debug text variables for current mouse location, distance to nearest box and id of nearest box
    public Text staticMouseLocation; //static text describing something

    //Variables for enabling video (to be implemented later)
    /*TopLeft*/    Boolean b1v = false;
    /*MidLeft*/    Boolean b2v = false;
    /*BotLeft*/    Boolean b3v = false;
    /*BigCenter*/  Boolean b4v = false;
    /*LeftCenter*/ Boolean b5v = false;
    /*RightCenter*/Boolean b6v = false;
    /*TopRight*/   Boolean b7v = false;
    /*MidRight*/   Boolean b8v = false;
    /*BotRight*/   Boolean b9v = false;
    Boolean[] isVideo = {b1v,b2v,b3v,b4v,b5v,b6v,b7v,b8v,b9v};


    public Controller(){
    }

    @FXML
    public void initialize() {
        String Temp = "Welcome to the Interactive New England Jazz Hall of Fame Poster on " + artistName + "\n Moving your mouse will activate various soundtracks depending on your proximity to various portions of the poster";
        IntroBoxText.setText(Temp); //sets intro box text to above string
        getLocationsOfObjects(); //gets the coords of each box
        setInvisible(-1); //sets all boxes invisible
        setEnables(); //enables boxes according to their strings

        //enable debug texts if debug mode on
        staticMouseLocation.setVisible(debugEnable);
        debugText1.setVisible(debugEnable);
        debugText2.setVisible(debugEnable);
        debugText3.setVisible(debugEnable);


    }

    //Update application whenever the mouse gets moved
    @FXML
    public void mouseMoveDetected(MouseEvent mouseEvent) {
        if (introDone) {

            //Get the xy coordinates of the mouse
            mX = mouseEvent.getX();
            mY = mouseEvent.getY();

            //Calculate the current distances from mouse
            distanceFromMouse();
            currClosest = closest();

            //If the closest box is within range, set the current closest distance
            if (currClosest != -1) {
                dClosest = d[currClosest];
            }

            //If the second closest box is within range, set the second closest distance
            if (nextClosest != -1) {
                d2Closest = d[nextClosest];
            }

            //If no boxes are within range, set all boxes invisible
            if (currClosest == -1) {
                setInvisible(-1);
            }

            //Set the boxes invisible when mouse leaves
            else if ((!enable[currClosest])) {
                setInvisible(-1);
            }

            //Make only the current box visible
            setVisible(currClosest);


            //Print debug text if its enabled
            if (debugEnable) {
                debugText(currClosest);
            }

            //Set the volumes of each box
            for (int i = 0; i < numberBoxes; i++) {
                //Check if this box is within minimum range
                if (d[i] < maxDistance) {
                    //if within range, set volumes (left hand side boxes)
                    if (i == 0 || i == 1 || i == 2) {
                        volume[i] = (-d[i] / 220) + 1.2;
                    }
                    //if within range, set volume (focus image)
                    if (i == 3) {
                        volume[i] = (-d[i] / 300) + 1.5;
                    }
                    //if within range, set volume (non focus images)
                    if (i == 4 || i == 5) {
                        volume[i] = (-d[i] / 100) + 1.5;
                    }
                    //if within range, set volume (right hand side boxes)
                    if (i == 6 || i == 7 || i == 8) {
                        volume[i] = (-d[i] / 100) + 1.6;
                    }
                } else {
                    //if not within range, set box volume to 0
                    volume[i] = 0;
                }
                //if volume exceeds max volume, set volume to max volume
                if (volume[i] > 1) {
                    volume[i] = 1;
                }
                //if volume is under minimum volume, set volume to 0
                if (volume[i] < 0) {
                    volume[i] = 0;
                }
            }

            //Initialization for each player:
        /*
        intermediate variable
        sets the player to play its specified media
        starts the player
        sets the player to loop indefinitely
        set the left right balance
        set the initial volume
         */
            if (!playersInitialized) {
                if (enable[0]) {
                    Media hit1 = new Media(Paths.get(music[0]).toUri().toString());
                    mp3Player1 = new MediaPlayer(hit1);
                    mp3Player1.play();
                    mp3Player1.setCycleCount(-1);
                    mp3Player1.setBalance(-1 * balanceShift);
                    mp3Player1.setVolume(volume[0]);
                }
                if (enable[1]) {
                    Media hit2 = new Media(Paths.get(music[1]).toUri().toString());
                    mp3Player2 = new MediaPlayer(hit2);
                    if (isVideo[1]) {
                        vid = new MediaView(mp3Player2);
                    }
                    mp3Player2.play();
                    mp3Player2.setCycleCount(-1);
                    mp3Player2.setBalance(-1 * balanceShift);
                    mp3Player2.setVolume(volume[1]);
                }
                if (enable[2]) {
                    Media hit3 = new Media(Paths.get(music[2]).toUri().toString());
                    mp3Player3 = new MediaPlayer(hit3);
                    mp3Player3.play();
                    mp3Player3.setCycleCount(-1);
                    mp3Player3.setBalance(-1 * balanceShift);
                    mp3Player3.setVolume(volume[2]);

                }
                if (enable[3]) {
                    Media hit4 = new Media(Paths.get(music[3]).toUri().toString());
                    mp3Player4 = new MediaPlayer(hit4);
                    mp3Player4.play();
                    mp3Player4.setCycleCount(-1);
                    mp3Player4.setVolume(volume[3]);
                }
                if (enable[4]) {
                    Media hit5 = new Media(Paths.get(music[4]).toUri().toString());
                    mp3Player5 = new MediaPlayer(hit5);
                    mp3Player5.play();
                    mp3Player5.setCycleCount(-1);
                    mp3Player5.setBalance(-0.5 * balanceShift);
                    mp3Player5.setVolume(volume[4]);

                }
                if (enable[5]) {
                    Media hit6 = new Media(Paths.get(music[5]).toUri().toString());
                    mp3Player6 = new MediaPlayer(hit6);
                    mp3Player6.play();
                    mp3Player6.setCycleCount(-1);
                    mp3Player6.setBalance(0.5 * balanceShift);
                    mp3Player6.setVolume(volume[5]);

                }
                if (enable[6]) {
                    Media hit7 = new Media(Paths.get(music[6]).toUri().toString());
                    mp3Player7 = new MediaPlayer(hit7);
                    mp3Player7.play();
                    mp3Player7.setCycleCount(-1);
                    mp3Player7.setBalance(balanceShift);
                    mp3Player7.setVolume(volume[6]);

                }
                if (enable[7]) {
                    Media hit8 = new Media(Paths.get(music[7]).toUri().toString());
                    mp3Player8 = new MediaPlayer(hit8);
                    mp3Player8.play();
                    mp3Player8.setCycleCount(-1);
                    mp3Player8.setBalance(balanceShift);
                    mp3Player8.setVolume(volume[7]);

                }
                if (enable[8]) {
                    Media hit9 = new Media(Paths.get(music[8]).toUri().toString());
                    mp3Player9 = new MediaPlayer(hit9);
                    mp3Player9.play();
                    mp3Player9.setCycleCount(-1);
                    mp3Player9.setBalance(balanceShift);
                    mp3Player9.setVolume(volume[8]);

                }
                playersInitialized = true;
            }

            //Sets the volume of each player (if it is active)
            if (enable[0]) {
                mp3Player1.setVolume(volume[0]);
            }
            if (enable[1]) {
                mp3Player2.setVolume(volume[1]);
            }
            if (enable[2]) {
                mp3Player3.setVolume(volume[2]);
            }
            if (enable[3]) {
                mp3Player4.setVolume(volume[3]);
            }
            if (enable[4]) {
                mp3Player5.setVolume(volume[4]);
            }
            if (enable[5]) {
                mp3Player6.setVolume(volume[5]);
            }
            if (enable[6]) {
                mp3Player7.setVolume(volume[6]);
            }
            if (enable[7]) {
                mp3Player8.setVolume(volume[7]);
            }
            if (enable[8]) {
                mp3Player9.setVolume(volume[8]);
            }
        }
    }

    //Debug texts
    public void debugText(int i) {
        String output1,output2,output3;
        if(i == -1){
            output1 = "X:" + mX + "\nY:" + mY;
            output2 = "notClose";
            output3 = "b" + i;
        }
        else {
            output1 = "X:" + mX + "\nY:" + mY;
            output2 = "dist" + d[i] + "\n vol" + volume[0] + " "+ volume[1] + " "+ volume[2] + " "+ volume[3] + " "+ volume[4] + " "+ volume[5] + " "+ volume[6] + " "+ volume[7] + " "+ volume[8];
            output3 = "b" + i + enable[i];
        }
        debugText1.setText(output1);
        debugText2.setText(output2);
        debugText3.setText(output3);

    }

    //Helper function that calls function to get the coords of the center of each box
    @FXML
    public void getLocationsOfObjects(){
        getCenter(box1,0);
        getCenter(box2,1);
        getCenter(box3,2);
        getCenter(box4,3);
        getCenter(box5,4);
        getCenter(box6,5);
        getCenter(box7,6);
        getCenter(box8,7);
        getCenter(box9,8);
    }

    //retrieves the coordinates of the center of the specified box
    @FXML
    public void getCenter(Rectangle rect, int index){
        x[index] = rect.getLayoutX() + (rect.getWidth()/2);
        y[index] = rect.getLayoutY() + (rect.getHeight()/2);

    }

    //Function that determines the distance between two points, given in pixels
    public double distanceFormula(double x1, double y1, double x2, double y2){
        return Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));

    }

    //identifies the distance of each point from mouse
    public void distanceFromMouse(){
        for(int i = 0;i<numberBoxes;i++){
            d[i] = distanceFormula(mX,mY,x[i],y[i]);
        }
    }

    //returns the index integer identifying the closest location given the distances from mouse
    // if further than defined max distance, returns -1
    public int closest() {
        int index = getSmallest(d);

        if (d[index] > maxDistance) {
            dClosest = d[index];
            return (-1);
        }
        else {
            return(index);
        }
    }

    //Returns the index of smallest distance
    //automatic function, no update necessary on new object
    public static int getSmallest(double[] array) {
        int index = 0;
        double min = array[index];

        for (int i = 1; i < array.length; i++) {
            if (array[i] <= min) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }

    //Sets the enable booleans for each box if there is a path string present for that box
    public void setEnables(){
        for(int i = 0; i<9;i++){
            enable[i] = !music[i].isEmpty();
        }
    }

    //make the box with the given index visible
    @FXML
    public void setVisible(int currClosest){
        if(currClosest == -1) {
            setInvisible(-1);
        }
        else if (enable[currClosest]) {
                if (currClosest == 0) {
                    box1.setVisible(true);
                }
                if (currClosest == 1) {
                    box2.setVisible(true);
                }
                if (currClosest == 2) {
                    box3.setVisible(true);
                }
                if (currClosest == 3) {
                    box4.setVisible(true);
                }
                if (currClosest == 4) {
                    box5.setVisible(true);
                }
                if (currClosest == 5) {
                    box6.setVisible(true);
                }
                if (currClosest == 6) {
                    box7.setVisible(true);
                }
                if (currClosest == 7) {
                    box8.setVisible(true);
                }
                if (currClosest == 8) {
                    box9.setVisible(true);
                }

                setInvisible(currClosest);
        }
    }

    //sets all boxes except the box with the given index invisible
    @FXML
    public void setInvisible(int currClosest){
        if((currClosest == -1)) {
            box1.setVisible(false);
            box2.setVisible(false);
            box3.setVisible(false);
            box4.setVisible(false);
            box5.setVisible(false);
            box6.setVisible(false);
            box7.setVisible(false);
            box8.setVisible(false);
            box9.setVisible(false);
        }
        if (currClosest != 0){
            box1.setVisible(false);
        }
        if (currClosest != 1){
            box2.setVisible(false);
        }
        if (currClosest != 2){
            box3.setVisible(false);
        }
        if (currClosest != 3){
            box4.setVisible(false);
        }
        if (currClosest != 4){
            box5.setVisible(false);
        }
        if (currClosest != 5){
            box6.setVisible(false);
        }
        if (currClosest != 6){
            box7.setVisible(false);
        }
        if (currClosest != 7){
            box8.setVisible(false);
        }
        if (currClosest != 8){
            box9.setVisible(false);
        }

    }


    public void closeIntro(ActionEvent actionEvent) {
        introBox.setVisible(false);
        introButton.setVisible(false);
        IntroBoxText.setVisible(false);
        introDone = true;
    }
}
