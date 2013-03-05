aerogear-android-integration-tests
==================================
## Getting started
To run the integration tests you need a running Android device or emulator with at least Android 4.2 Jelly Bean.

Once your emulator or device is running, `mvn clean install` will run the tests.

##Coverage report
This project relies on a fork of emma4it.  Install it by running mvn clean install on the repo at https://github.com/Appboy/emma4it-maven-plugin

Once emma4it 1.4-SNAPSHOT is installed, you can generate the report with `mvn clean install -Pemma`
