@echo off
pushd %~dp0

echo "Deleting *.class . . ."
del *.class /s > NUL

echo "Compiling . . . "
javac Main.java
jar cfe AiVariableTool.jar Main template.xml *.class ui/*.class converter/*.class
if -%1-==-run- (
	echo "Running . . . "
	java Main
)

popd