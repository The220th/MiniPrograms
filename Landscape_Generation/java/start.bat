del /s *.class
javac -encoding utf8 Landscape\*.java
chcp 1251
java Landscape.MapGenerator
Pause
CMD /Q /K