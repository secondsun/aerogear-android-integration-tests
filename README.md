aerogear-android-integration-tests [![Build Status](https://travis-ci.org/aerogear/aerogear-android-integration-tests.png)](https://travis-ci.org/aerogear/aerogear-android-integration-tests)
==================================
## Getting started
To run the integration tests you need a running Android device or emulator with at least Android 4.2 Jelly Bean.

Once your emulator or device is running, `mvn clean install` will run the tests.

##Coverage report
This project relies on a fork of emma4it.  Install it by running mvn clean install on the repo at https://github.com/sonatype/emma4it-maven-plugin

Once emma4it 1.4-SNAPSHOT is installed, you can generate the report with `mvn clean install -Pemma`

## Troubleshooting

### java.lang.IllegalArgumentException: dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)

This is a regression in Android 4.3.  For now the tests build and run fine on 4.2.2.  
