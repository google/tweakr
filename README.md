Tweakr
======

*A Library for Remote Debugging, Autogenerating Settings/Preference UIs, and Wizard-of-Ozing*
by [odbol](http://odbol.com)

Sick of tweaking one value in your animation and having to wait minutes to compile and see your change? Ever want to hand someone a prototype, and let them try it with various options you can adjust on the fly? Do you get tingles when someone mentions "one-line solution"? Then Tweakr might be right for you!

Tweakr is an Android library that lets you annotate fields and methods in your code, and then automatically generates a UI to change those elements locally or remotely. It can use Firebase and a web-based UI to alter values and change settings in your app on the fly, instantly. It can also autogenerate a Preferences UI screen using SharedPreferences local to the phone.

Literally write one line of code: just annotate the thing you want to change with @Tweak, and it will handle the rest!

[![](images/tweakr-teaser-short.gif)](http://www.youtube.com/watch?v=CgeW_q7NgfI "Tweakr Demo Video")

[Demo video](https://youtu.be/CgeW_q7NgfI)


Include in your build
=====================

[![](https://jitpack.io/v/google/tweakr.svg)](https://jitpack.io/#google/tweakr)

1. Add Jitpack to your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
2. Add the Tweakr dependency to your app module's build.gradle file:
```
dependencies {
  // Required for local SharedPreferences or Firebase
  implementation 'com.github.google.tweakr:core:2.2.3'

  // Optional: Include this if you want Firebase support.
  implementation 'com.github.google.tweakr:firebase:2.2.3'
}
```


Quickstart Instructions (Local SharedPreferences Repo)
=====================

This autogenerates a PreferenceFragment settings UI with all your Tweaks so you can easily change them locally in your
app via a settings screen.

1. Initialize the Tweakr repo in your Application onCreate():
```
public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable local Android preference screen for Tweakr.
        Tweakr.setRepo(new TweakrPreferencesRepo(this));
    }
}
```
2. Annotate a field or method of your class with `@Tweak`. (It must be public).
```
  @Tweak
  public float radius = 50;

  @Tweak
  public void setTextShown(boolean showText) {
```
3. Call `Tweakr.register(this)` in the class's constructor so that it will register all the annotated fields with the Tweakr repo and begin listening for changes.
4. Expose an Activity like the [TweakrPreferencesActivity](https://github.com/google/tweakr/blob/master/android-sample/app/src/main/java/com/google/tweakr/sample/preferences/TweakrPreferencesActivity.java) that will auto-generate a PreferenceScreen UI for your Tweaks.
5. You must run the main Activity first before any of the Tweaks will show up in the Preferences UI. After that, they will persist between launches.


Quickstart Instructions (Firebase)
=======================

This allows you to remote control your Tweaks via a web UI. Requires Firebase.

1. Initialize the Tweakr repo with TweakrFirebaseRepo in your Application onCreate():
```
public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Uses the default no-auth Firebase Repo.
        Tweakr.setRepo(new TweakrFirebaseRepo());
    }
}
```
2. Annotate a field or method of your class with `@Tweak`. (It must be public).
```
  @Tweak
  public float radius = 50;

  @Tweak
  public void setTextShown(boolean showText) {
```
3. Call `Tweakr.register(this)` in the class's constructor so that it will register all the annotated fields with the Tweakr repo and begin listening for changes.
5. Use our free hosted [Easyserver](#web---easyserver) to tweak your parameters via a remote web UI (or [use your own server](#custom-server-setup)).
5. You must run the main Activity first before any of the Tweaks will show up in the web UI. After that, they will persist between launches and update the latest values from the server automatically.
4. (Optional): you may increase the security of your app by [enabling authentication](#enabling-authentication).

Set up Firebase – Android
-------------------------

1. Set up a Firebase project: https://console.firebase.google.com/
1. In the Firebase console, add a new Android app using your own package name:
	e.g. `com.[your-domain].tweakr.sample`
1. Follow the instructions to download the "google-services.json" file.
1. Copy the "google-services.json" file into your app module directory (e.g. in the sample: `android-sample/app/`).
1. Add the Firebase dependencies to your gradle file as directed.
1. Skip confirming the connection to Firebase. We'll do that later once we update the applicationId in the Android app.
1. In the Firebase console, click "Develop->Database", and scroll down to create a new "Real-Time database". (**NOT** a Cloud Firestore database!)
1. Set up [Firebase Authentication](https://firebase.google.com/docs/database/security/quickstart#sample-rules). The quickest way is to allow full public access:
````
// These rules give anyone, even people who are not users of your app,
// read and write access to your database
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
````

Running the Sample Android App
-----------

Make sure your applicationId matches the one you created in the Firebase console.

Follow these directions to try the sample app:

1. Open the `android-sample` project.
2. Open the `app/build.gradle` file and change the `applicationId` to the package name you used to set up Firebase, e.g.: `com.[your-domain].tweakr.sample`
3. Run the app.


Web - Easyserver
---------------------------

The easiest way to start tweaking values in your app is to use our pre-hosted web UI. If you'd rather set up your own server, skip the next section.

_Note: If you are using authentication to increase the security of your app, this method is not recommended._

1. Go to [https://google.github.io/tweakr/easyserver/](https://google.github.io/tweakr/easyserver/)  and follow the instructions to allow the Easyserver to access your Firebase.
We never store any of your information, but keep in mind to use this you need to open your Firebase to the public so do so at your own risk.


Custom Server Setup
===================

These instructions are for hosting your own web server UI. For quick-n-dirty prototyping situations, use the [Easyserver](https://google.github.io/tweakr/easyserver/) instead (see previous section).

Set up Firebase – Web
-------------------

1. In the Firebase console, Click "Project Overview" and add a new Web app.
2. It will provide you with some Javascript code. Copy only the object which contains your API values (after `var firebaseConfig = `).
3. Open `web-server/tweakr-server/src/environments/environment.ts` and replace the placeholder values with the object from step 2.


UI / Web Server
---------------

1. Download and install the [Angular CLI](https://angular.io/guide/quickstart).
1. If you haven't already done so, open `web-server/tweakr-server/src/environments/environment.ts` and replace the placeholder values with values you download from the Firebase console.
2. Open a command line to the `web-server/tweakr-server/` directory.
3. Run `npm install`
4. Run `ng serve --open`
5. Run the sample Android app. The Tweaks should show up in the web UI instantly and you can alter them and see them change in the app in real-time.


Using the API
=============

Tweakr's API is (hopefully) simple:

1. Annotate any field or method of your object with `@Tweak`.
2. Call `Tweakr.register(yourObject)` on your object. It will look for all the members of that object annotated with `@Tweak` and register them with the server. It will automatically unregsister them when the object is garbage collected.

- Most primitives are supported automatically. If you want to tweak custom types, either write a custom ValueType (see `ColorValueType.java` for an example), or create a method that does what you want and then annotate the method.
- You may also tweak child members of an object (in case you don't have access to the object's class). See `MainActivity.introText` in the sample app for an example.
- You may call `Tweakr.addListener()` to be notified when your object's values have been modified. See `CircleView.java` in the sample for an example.

Documentation
=============

See [Tweakr's class documentation](https://google.github.io/tweakr/docs/html/classcom_1_1google_1_1tweakr_1_1_tweakr.html) for full usage info.


Enabling Authentication
=======================

The default implementation is insecure and allows anyone to alter values in your Tweakr database. You can secure it by using authentication.
The sample includes code demonstrating how to enable email authentication, but you could also use OAuth or other methods if you like.


Android App
-----------

1. Uncomment the indicated line in SampleApplication.java
2. Change the `DEFAULT_EMAIL` and `DEFAULT_PASSWORD` fields in the `TweakrFirebaseRepoEmailAuth.java` file to your own desired values.


UI / Web Server
---------------

1. Change the `DEFAULT_EMAIL` and `DEFAULT_PASSWORD` constants in the `app.component.ts` file to match the values in the Android app.


Firebase Console
----------------

1. In the Firebase console, click "Develop->Authentication", then set up email authentication with the email and password from the Android app.
2. To restrict database access to authenticated users, open "Database->Real-Time Database->Rules".
3. Change the Rules to something like:
````
{
  "rules": {
    ".read": "auth.uid != null",
    ".write": "auth.uid != null"
  }
}
````
4. Note that anyone can sign up using any email / password combo they like. More work is needed to truly lock down your app to outside users.


Feature Roadmap / TODO
======================

- Fix buttons to not run on initial register.
- Support explicit min/max value setting in @Tweak annotation.
- Support custom ordering of Tweaks.
- Add ability to save/load custom Tweak settings, like presets, or the ability to export / import Tweak values as a JSON file.
- Support Tweak groups
- Support multiple children in @Tweak annotation
- Support array types
- Support registering additional ValueTypeConverters
- Binary file ValueType for uploading images/audio remotely and displaying in your app


Notes
======

Note: This is not an officially supported Google product.
