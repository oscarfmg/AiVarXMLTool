@echo off
pushd %~dp0

echo "Deleting *.pyc . . ."
del *.pyc /s > NUL

echo "Running . . . "
python Main.py

popd
