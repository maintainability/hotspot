Hotspot detector.

Necesary software to compile and run:
- Java Developer Kit. I used version 1.7.0_79.
- Maven. I used version 3.3.3.
- SVN client. I used TortoiseSVN version 1.8.11.
All the bin directories should be available in the PATH.

Compile with command:
mvn clean install

The results are copied into folder:
hotareadetector-assembly/target/hotareadetector-assembly-bin/deployment 

Copy the 3 jar files into an arbitrary directory, and execute the hotareadetector-main-x.y.z.jar file with appropriate parameters, e.g. as follows:
java -jar hotareadetector-main-1.0.1.jar -extensions=java -includePrefixes=/ant/core/tags/ANT_13 -excludePrefixes=/ant/core/tags/ANT_13/src/testcases -dirName=Ant -client=svn.exe -url=http://svn.apache.org/repos/asf/ant -revision=268747 -deepAnalysis=false -outputFileName="hotarea-ant-1.3.csv"

Execute it without parameters for help:
java -jar hotareadetector-main-1.0.1.jar
