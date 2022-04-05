#!/bin/bash

echo "Generating release binaries..."

pushd web-server/tweakr-server
npm run github
popd

pushd android-sample/

./gradlew :tweakr:assembleRelease
cp ../android/tweakr/build/outputs/aar/tweakr-release.aar ../dist/

./gradlew :tweakr-firebase:assembleRelease
cp ../android/tweakr-firebase/build/outputs/aar/tweakr-firebase-release.aar ../dist/

popd

echo "Generating documentation..."
/Applications/Doxygen.app/Contents/Resources/doxygen doxygen.config
