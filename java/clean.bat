@echo off
pushd %~dp0

echo "Deleting *.class . . ."
del *.class /s > NUL

popd
