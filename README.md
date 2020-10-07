Tweakr
======

*A Library for Remote Debugging, Autogenerating Settings/Preference UIs, and Wizard-of-Ozing*
by [odbol](http://odbol.com)

Sick of tweaking one value in your animation and having to wait minutes to compile and see your change? Ever want to hand someone a prototype, and let them try it with various options you can adjust on the fly? Do you get tingles when someone mentions "one-line solution"? Then Tweakr might be the right framework for you!

Tweakr is an Android library that lets you annotate fields and methods in your code, and then automatically generates a UI to change those elements locally or remotely. It can use Firebase and a web-based UI to alter values and change settings in your app on the fly, instantly. It can also autogenerate a Preferences UI local to the phone. Literally write one line of code: just annotate the thing you want to change with @Tweak, and it will handle the rest!

[![](http://img.youtube.com/vi/CgeW_q7NgfI/0.jpg)](http://www.youtube.com/watch?v=CgeW_q7NgfI "Tweakr Demo Video")

[Demo video](https://youtu.be/CgeW_q7NgfI)


Quickstart Instructions (Local Preference Repo)
=====================

First, clone the repo: `git clone https://github.com/google/tweakr.git`.

By default, the Sample app is set up to use the Local Preferences repo. For Firebase support, see the next section.

1. Expose an Activity like the TweakrPrefrencesActivity that will auto-generate a PreferenceScreen UI for your Tweaks.
2. You must run the main Activity first before any of the Tweaks will show up in the Preferences UI. After that, they will persist between launches.


Quickstart Instructions (Firebase)
=======================

Import Firebase module
----------------------

1. Search the Sample Android app project for `FIREBASE SUPPORT` and uncomment the lines indicated.


Set up Firebase – Android
-------------------------

1. Set up a Firebase project: https://console.firebase.google.com/
1. In the Firebase console, add a new Android app using your own package name:
	e.g. `com.[your-domain].tweakr.sample`
1. Follow the instructions to download the "google-services.json" file.
1. Copy the "google-services.json" file into the `android-sample/app/` directory.
1. You may skip the gradle file step since those libraries are already added to the Sample app.
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

Android App
-----------

1. Open the `android-sample` project.
2. Open the `app/build.gradle` file and change the `applicationId` to the package name you used to set up Firebase, e.g.: `com.[your-domain].tweakr.sample`
3. Run the app.
4. (Optional): you may increase the security of your app by [enabling authentication](#enabling-authentication).


Web - Easyserver
---------------------------

The easiest way to start tweaking values in your app is to use our pre-hosted web UI. If you'd rather set up your own server, skip the next section.

_Note: If you are enabling authentication to increase the security of your app, this method is not recommended._

1. Go to https://google.github.io/tweakr/easyserver/ and follow the instructions to allow the Easyserver to access your Firebase.


Custom Server Setup
===================

These instructions are for hosting your own web server UI. For quick-n-dirty prototyping situations, use the (Easyserver)[https://google.github.io/tweakr/easyserver/] instead (see previous section).

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
- See the Javadoc in the source for more detailed documentation.



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

- Support multiple children in @Tweak annotation
- Support array types
- Support registering additional ValueTypeConverters
- Binary file ValueType for uploading images/audio remotely and displaying in your app
- Write some tests, or something ;-P


Notes
======

Note: This is not an officially supported Google product.
