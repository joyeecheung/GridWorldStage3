##Dependencies
There are some required resources/libraries that aren't inclueded in this repo.

To build the projects, their directory structure should be as follows.

###ImageReader

    ImageReader/
    ├── bmptest
    │   ├── 1.bmp
    │   ├── 2.bmp
    │   └── goal
    │       ├── 1_blue_goal.bmp
    │       ├── 1_gray_goal.bmp
    │       ├── 1_green_goal.bmp
    │       ├── 1_red_goal.bmp
    │       ├── 2_blue_goal.bmp
    │       ├── 2_gray_goal.bmp
    │       ├── 2_green_goal.bmp
    │       └── 2_red_goal.bmp
    ├── build.xml
    ├── lib
    │   ├── appframework-1.0.3.jar
    │   ├── hamcrest-core-1.3.jar
    │   ├── ImageReader.jar
    │   ├── junit-4.11.jar
    │   └── swing-worker-1.1.jar
    ├── src
    │   ├── BMPImageIO.java
    │   ├── BMPImageProcessor.java
    │   └── ImageReaderRunner.java
    └── test
        ├── BMPImageIOTest.java
        └── BMPImageProcessorTest.java

###Mazebug

    MazeBug/
    ├── build.xml
    ├── lib
    │   ├── gridworld.jar
    │   ├── hamcrest-core-1.3.jar
    │   └── junit-4.11.jar
    ├── map
    │   ├── EasyMaze.txt
    │   ├── FinalMaze01.txt
    │   ├── FinalMaze02.txt
    │   ├── FinalMaze03.txt
    │   ├── FinalMaze04.txt
    │   ├── FinalMaze05.txt
    │   └── OneRoadMaze.txt
    ├── src
    │   └── info
    │       └── gridworld
    │           └── maze
    │               ├── MazeBug.java
    │               ├── MazeBug.java2
    │               └── MazeBugRunner.java
    └── test
        └── MazeBugTest.java

Notice: the `gridworld.jar` uses the modified `WorldFrame`

###Jigsaw

    Jigsaw
    ├── build.xml
    └── src
        ├── jigsaw
        │   ├── Jigsaw.java
        │   ├── Jigsaw.java2
        │   └── JigsawNode.java
        └── Runners
            ├── RunnerDemo.java
            ├── RunnerPart1.java
            └── RunnerPart2.java

##Ant Targets
* `ant`: compile
* `ant run`: run the program
* `ant test`: run JUnit tests
* `ant report`: generate JUnit test reports

##About
* Author: Joyee Cheung
* Time: 2014 Summer
