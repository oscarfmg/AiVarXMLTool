@echo off
pushd %~dp0

echo "Deleting *.pyc . . ."
del *.pyc /s > NUL

popd
