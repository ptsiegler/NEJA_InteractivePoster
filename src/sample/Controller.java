package sample;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Controller {
    //TODO: string for path of mp3 audio file for box if applicable
    /*init*/       String initAudio = "src/music/LIB.mp3";
    /*TopLeft*/    String box1audio = "src/Music/countryRoads.mp3";
    /*MidLeft*/    String box2audio = ""; //src/Video/CNS.mp4
    /*BotLeft*/    String box3audio = "src/Music/cotb.mp3";
    /*BigCenter*/  String box4audio = "src/Music/LunarWhispersMinor.mp3";
    /*LeftCenter*/ String box5audio = "";
    /*RightCenter*/String box6audio = "";
    /*TopRight*/   String box7audio = "src/Music/Zombie.mp3";
    /*MidRight*/   String box8audio = "src/Music/hhp.mp3";
    /*BotRight*/   String box9audio = "src/Music/cotb.mp3";

    //TODO: If a box is video, set that box to true
    /*TopLeft*/    Boolean b1v = false;
    /*MidLeft*/    Boolean b2v = true;
    /*BotLeft*/    Boolean b3v = false;
    /*BigCenter*/  Boolean b4v = false;
    /*LeftCenter*/ Boolean b5v = false;
    /*RightCenter*/Boolean b6v = false;
    /*TopRight*/   Boolean b7v = false;
    /*MidRight*/   Boolean b8v = false;
    /*BotRight*/   Boolean b9v = false;


    int maxDistance = 400;


    //Variables to not Touch
    public Text currMouseLocation, distFromMouse, closestToMouse;
    public Rectangle box1,box2,box3,box4,box5,box6,box7,box8,box9;
    public MediaPlayer mp3Player;
    public MediaPlayer mp3Player1,mp3Player2,mp3Player3,mp3Player4,mp3Player5,mp3Player6,mp3Player7,mp3Player8,mp3Player9;
    public MediaView vid;
    Boolean boxEnable1 = false,boxEnable2 = false,boxEnable3 = false,boxEnable4 = false,boxEnable5 = false,boxEnable6 = false,boxEnable7 = false,boxEnable8 = false,boxEnable9 = false;
    Boolean initAudioPresent = !initAudio.isEmpty(), audioPlaying = false,audio2Playing = false;
    Boolean debugEnable = true;
    Boolean started = false;

    boolean start = true;
    int numberBoxes = 9, currClosest = 0,lastClosest = -1, nextClosest=0;
    double maxVolume = 1,volume = 0;
    double balanceShift = 0.75;
    double[] newVol = new double[numberBoxes];

    String[] music = {box1audio,box2audio,box3audio,box4audio,box5audio,box6audio,box7audio,box8audio,box9audio};
    Boolean[] enable = {boxEnable1,boxEnable2, boxEnable3, boxEnable4, boxEnable5, boxEnable6, boxEnable7, boxEnable8, boxEnable9};
    Boolean[] isVideo = {b1v,b2v,b3v,b4v,b5v,b6v,b7v,b8v,b9v};
    String bip = "";

    double[] x = new double[numberBoxes];
    double[] y = new double[numberBoxes];
    double[] d = new double[numberBoxes];
    double mX, mY;
    double dClosest, d2Closest;

    public Controller(){
    }

    @FXML
    public void initialize(){
        getLocationsOfObjects();
        setInvisible(-1);
        setEnables();

    }

    //Main calculation function. WIP
    @FXML
    public void mouseMoveDetected(MouseEvent mouseEvent) throws InterruptedException {
        mX = mouseEvent.getX();
        mY = mouseEvent.getY();

        distanceFromMouse();
        currClosest = closest();
        if (currClosest != -1) {
            dClosest = d[currClosest];
        }
        if (nextClosest != -1) {
            d2Closest = d[nextClosest];
        }
        if(currClosest==-1){
            setInvisible(-1);
        }
        else if((!enable[currClosest])){
            setInvisible(-1);
        }

        setVisible(currClosest);

        if (debugEnable) {
            debugText(currClosest);
        }

/*  Single Fade
        if(currClosest != lastClosest) {
            lastClosest = currClosest;
            if(start){
                bip = initAudio;
                start = false;
                if(initAudioPresent) {
                    Media hit = new Media(Paths.get(bip).toUri().toString());
                    mp3Player = new MediaPlayer(hit);
                    mp3Player.play();
                    audioPlaying = true;
                }
            }
            else if(currClosest == -1){
                mp3Player.stop();
                audioPlaying = false;
            }
            else {
                if(enable[currClosest]) {
                    bip = music[currClosest];
                    Media hit = new Media(Paths.get(bip).toUri().toString());
                    mp3Player = new MediaPlayer(hit);
                    mp3Player.play();
                    audioPlaying = true;
                }
            }


        }
            if(audioPlaying) {
            volume = (-dClosest / 200) + 2;
            if (volume > maxVolume) {
                volume = maxVolume;
            }
            if (volume < 0) {
                volume = 0;
            }
            System.out.println(currClosest + " " + dClosest + " " + volume);
            mp3Player.setVolume(volume);
        }
*/
        for (int i = 0; i < numberBoxes; i++) {
            if (d[i] < maxDistance) {
                if (i == 0 || i == 1 || i == 2) {
                    newVol[i] = (-d[i] / 220) + 1.2;
                }
                if (i == 3) {
                    newVol[i] = (-d[i] / 300) + 1.5;
                }
                if (i == 4 || i == 5) {
                    newVol[i] = (-d[i] / 100) + 1.5;
                }
                if (i == 6 || i == 7 || i == 8) {
                    newVol[i] = (-d[i] / 100) + 1.6;
                }
            } else {
                newVol[i] = 0;
            }
            if (newVol[i] > 1) {
                newVol[i] = 1;
            }
            if (newVol[i] < 0) {
                newVol[i] = 0;
            }
            //System.out.print(newVol[i] + " ");
        }
        //System.out.println("");
        if (!started) {
            if (enable[0]) {
                Media hit1 = new Media(Paths.get(music[0]).toUri().toString());
                mp3Player1 = new MediaPlayer(hit1);
                mp3Player1.play();
                mp3Player1.setCycleCount(-1);
                mp3Player1.setBalance(-1*balanceShift);
                mp3Player1.setVolume(newVol[0]);
            }
            if (enable[1]) {
                Media hit2 = new Media(Paths.get(music[1]).toUri().toString());
                mp3Player2 = new MediaPlayer(hit2);
                if(isVideo[1]){
                    vid = new MediaView(mp3Player2);
                }
                mp3Player2.play();
                mp3Player2.setCycleCount(-1);
                mp3Player2.setBalance(-1*balanceShift);
                mp3Player2.setVolume(newVol[1]);
            }
            if (enable[2]) {
                Media hit3 = new Media(Paths.get(music[2]).toUri().toString());
                mp3Player3 = new MediaPlayer(hit3);
                mp3Player3.play();
                mp3Player3.setCycleCount(-1);
                mp3Player3.setBalance(-1*balanceShift);
                mp3Player3.setVolume(newVol[2]);

            }
            if (enable[3]) {
                Media hit4 = new Media(Paths.get(music[3]).toUri().toString());
                mp3Player4 = new MediaPlayer(hit4);
                mp3Player4.play();
                mp3Player4.setCycleCount(-1);
                mp3Player4.setVolume(newVol[3]);
            }
            if (enable[4]) {
                Media hit5 = new Media(Paths.get(music[4]).toUri().toString());
                mp3Player5 = new MediaPlayer(hit5);
                mp3Player5.play();
                mp3Player5.setCycleCount(-1);
                mp3Player5.setBalance(-0.5*balanceShift);
                mp3Player5.setVolume(newVol[4]);

            }
            if (enable[5]) {
                Media hit6 = new Media(Paths.get(music[5]).toUri().toString());
                mp3Player6 = new MediaPlayer(hit6);
                mp3Player6.play();
                mp3Player6.setCycleCount(-1);
                mp3Player6.setBalance(0.5*balanceShift);
                mp3Player6.setVolume(newVol[5]);

            }
            if (enable[6]) {
                Media hit7 = new Media(Paths.get(music[6]).toUri().toString());
                mp3Player7 = new MediaPlayer(hit7);
                mp3Player7.play();
                mp3Player7.setCycleCount(-1);
                mp3Player7.setBalance(balanceShift);
                mp3Player7.setVolume(newVol[6]);

            }
            if (enable[7]) {
                Media hit8 = new Media(Paths.get(music[7]).toUri().toString());
                mp3Player8 = new MediaPlayer(hit8);
                mp3Player8.play();
                mp3Player8.setCycleCount(-1);
                mp3Player8.setBalance(balanceShift);
                mp3Player8.setVolume(newVol[7]);

            }
            if (enable[8]) {
                Media hit9 = new Media(Paths.get(music[8]).toUri().toString());
                mp3Player9 = new MediaPlayer(hit9);
                mp3Player9.play();
                mp3Player9.setCycleCount(-1);
                mp3Player9.setBalance(balanceShift);
                mp3Player9.setVolume(newVol[8]);

            }
            started = true;
        }

        if (enable[0]) {mp3Player1.setVolume(newVol[0]);}
        if (enable[1]) {mp3Player2.setVolume(newVol[1]);}
        if (enable[2]) {mp3Player3.setVolume(newVol[2]);}
        if (enable[3]) {mp3Player4.setVolume(newVol[3]);}
        if (enable[4]) {mp3Player5.setVolume(newVol[4]);}
        if (enable[5]) {mp3Player6.setVolume(newVol[5]);}
        if (enable[6]) {mp3Player7.setVolume(newVol[6]);}
        if (enable[7]) {mp3Player8.setVolume(newVol[7]);}
        if (enable[8]) {mp3Player9.setVolume(newVol[8]);}
    }

    public void debugText(int i) {
        String output1,output2,output3;
        if(i == -1){
            output1 = "X:" + mX + "\nY:" + mY;
            output2 = "notClose";
            output3 = "b" + i;
        }
        else {
            output1 = "X:" + mX + "\nY:" + mY;
            output2 = "dist" + d[i] + "\n vol" + newVol[0] + " "+newVol[1] + " "+newVol[2] + " "+newVol[3] + " "+newVol[4] + " "+newVol[5] + " "+newVol[6] + " "+newVol[7] + " "+newVol[8];
            output3 = "b" + i + enable[i];
        }
        currMouseLocation.setText(output1);
        distFromMouse.setText(output2);
        closestToMouse.setText(output3);

    }

    //gets the xy coords of each location
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

    //sets the xy coords of each object, where rect is the object, index is the loc number
    //automatic function, no update necessary on new object
    @FXML
    public void getCenter(Rectangle rect, int index){
        x[index] = rect.getLayoutX() + (rect.getWidth()/2);
        y[index] = rect.getLayoutY() + (rect.getHeight()/2);

    }

    //calculates the distance between two points
    //automatic function, no update necessary on new object
    public double distanceFormula(double x1, double y1, double x2, double y2){
        return Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));

    }

    //identifies the distance of each point from mouse
    //automatic function, no update necessary on new object
    public void distanceFromMouse(){
        for(int i = 0;i<numberBoxes;i++){
            d[i] = distanceFormula(mX,mY,x[i],y[i]);
        }
    }

    //returns the index integer identifying the closest location given the distances from mouse
    // if further than defined max distance, returns -1
    //automatic function, no update necessary on new object
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

    public void setEnables(){
        for(int i = 0; i<9;i++){
            enable[i] = !music[i].isEmpty();
        }
    }

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

}
