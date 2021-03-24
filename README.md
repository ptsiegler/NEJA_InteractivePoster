# NEJA_InteractivePoster
2020-2021 IQP involving Jazz History Database's Jazz Hall of Fame Posters

Patrick Siegler ptsiegler@wpi.edu

Professor Scott Barton sdbarton@wpi.edu

# 1.0 Introduction
This source code encompases the design code for creating interactive New England Jazz Hall of Fame Posters.
The code given generates a single poster with the given image and audio as a .jar file
Packaging of the various posters must be done outside the integrated development environment
The Interactive Posters can be found at https://drive.google.com/drive/folders/1fNHvo5jfv1AeWp4k_ydUZyZzBqJX2BML
    
# 2.0 Required Software
## 2.1 Integrated Development Environment (IDE)
The base integrated development environment (IDE) for this project is Jet Brains' Intellij Idea for Java and can be found at https://www.jetbrains.com/idea/
While the source code is written in Java SDK version 11.0.6 and is editable in any java supported IDE, these instructions assume the use of Intellij
## 2.2 Java Software Development Kit (SDK)
This source code utilizes Java SDK 11.0.6 and can be found at https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
## 2.3 Java Runtime Environment
Using the application and associated .jar files requires installing Java and can be found at https://www.java.com/en/download/manual.jsp
## 2.4 JavaFX
#### 2.4.1 The visual components of this project are controlled by JavaFX, which can be found at https://gluonhq.com/products/javafx/
#### 2.4.2 The graphical interface editor of the visual components can be found at https://gluonhq.com/products/scene-builder/ Please note this is not required, as the graphical components can be edited in code, however this is reccomended
    
# 3.0 Editing
## 3.1 First Time Set-up
Assuming all of the above software is installed
1) Open Intellij
2) Create a new Project. A popup box will prompt you for a URL
```
Top Menu Bar > File > New > Project > Project From Version Control
In the popup box, set the version control to "Git"
Paste "https://github.com/ptsiegler/NEJA_InteractivePoster" as the URL
Make note of the directory as this will be important later
```
4) Click Clone
5) Congrats, you now have a copy of the master code

## 3.1 Creating a New Poster
#### 3.1.1 Adding the New Files
1) Drag the poster image to the src/images folder in intellij
<img src="https://i.imgur.com/nQzh5x7.png">
2) In the following popup, select "Refactor"
3) Drag the desired .mp3 audio files to the src/Music folder in intellij
<img src="https://i.imgur.com/1aX8HuD.png">
4) In the following popup, select "Refactor"

#### 3.1.2 Setting the new poster image
Open the "sample.fxml" file in JavaFX Scenebuilder by right clicking the file
<img src="https://i.imgur.com/visQ1d1.png">
1) Click the Background image
2) in the Inspector pane (right hand side), expand the Properties menu
3) edit the Image box to read "..\images\[newImageNameHere]"

```
ex. "..\images\harryCarney.png"
```
<img src="https://i.imgur.com/qjCY2HW.png">

4) The audio bounding boxes can be resized by dragging the corner of the box 

#### 3.1.3 Setting the new audio
1) In intellij, open the main code by double clicking "Controller" under src/sample/Controller
<img src="https://i.imgur.com/3tgIqXx.png">
2) Scroll to line 22
<img src="https://i.imgur.com/Qp0YrQ5.png">
3) set the string artistName to the name of the artist (make sure to include the quotation marks)

```
ex. "String artistName = "Harry Carney"";
```

4) change the target audio files by adjusting the strings for each box.

```
leave the majority of the variable and string alone, only edit after the last "/" in the path
ex. if we want to set the Right hand side middle image to play an .mp3 file named "beethoven9th.mp3" that we placed in src/Music/ in step 3.1.1
we would change line 32 from "    /*MidRight*/   String box8audio = "src/Music/hhp.mp3";" to "    /*MidRight*/   String box8audio = "src/Music/beethoven9th.mp3";"
```

#### 3.1.4 Export the updated poster
1) See Section 4

## 3.2 Editing an Existing Poster
It is currently easier to rebuild the poster than to edit an existing poster, however this may change in the future. Please see section 3.1


# 4 Export Poster
## 4.1 Create the executable
1) Open the Project Structure (Ctrl+Alt+Shift+S)

2) Set the project compiler output to your desired output folder (somewhere you know preferably, like your desktop)

3) Under src/META-INF delete the file "MANIFEST.MF"
<img src="https://i.imgur.com/t8zjNWw.png">

4) Under the Artifacts tab, create an artifact: click the plus > JAR > From Modules with Dependancies
<img src="https://i.imgur.com/3KhYsit.png">

5) In the Following popup, click the folder next to the "Main Class" and select "Main (sample)" and hit OK

6) Make a note of the Output Directory, as this is where the executable file will be stored

7) Hit OK on the project structure window

8) In the main menu bar, go to:  Build > Build Artifacts and hit build on the popup

## 4.2 Move the executable to the run location
0) if you are editing an existing poster, only do step 4

1) The run location can be downloaded from https://drive.google.com/drive/folders/1fNHvo5jfv1AeWp4k_ydUZyZzBqJX2BML

2) Unzip the run location "InteractivePosters"

3) Open the "SourceMaterial" folder and make a copy of the "MasterSample" folder and rename it to the new artist

4) Replace the existing "mouseTrackingIQP.jar" file in the artist folder with the new jar you created in part 4.1

5) Return to the "InteractivePosters" folder and make a copy of the "MasterSample.bat" file

6) Right click and rename your copy to "Run [insert artist name here] Interactive Poster"

7) Right click and edit the renamed .bat file in notepad

8) On line 3, change "cd MasterSample" to "cd [name of artist folder here]"

9) Open your project directory (from part 3.0 step 2), navigate to your project and copy the src folder

10) Replace the src folder in the artist folder of the run location with the src folder copied from the project directory

