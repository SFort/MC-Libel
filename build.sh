if [ -n "$JAVA8_HOME" ]; then
	export JAVA_HOME=$JAVA8_HOME
fi
./build-features.sh
rm -rf build/libs
echo Building Fabrication...
./gradlew clean build
rm build/libs/*-dev.jar
fabrication=$(echo build/libs/fabrication*.jar)
zip -d "$fabrication" com/mrcrayfish/* svenhjol/charm/*
echo Done
