# GeoDroid

An Android library for building spatial applications.

## Setting up the Android SDK

Building the library requires the 
[Android SDK](http://developer.android.com/sdk/index.html). 

After installing the SDK some additional packages must be installed through 
the Android SDK Manager. Run the ``android`` command to start the SDK manager
and install the following packages.

* Android SDK Tools
* Android SDK SDK Platform-tools
* Android SDK Build-tools

And finally install the appropriate API package. Currently GeoDroid is built
against "Android 4.0.3 (API 15)".

## Building

Building the library from sources requires 
[Apache Maven](http://maven.apache.org/).

Once the Android SDK is set up and Maven is installed run the following command 
in the root of the project.

    android update project -p .

And finally run Maven to build the library.

    mvn install
