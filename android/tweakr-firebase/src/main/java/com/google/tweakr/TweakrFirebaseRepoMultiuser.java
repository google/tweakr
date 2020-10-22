package com.google.tweakr;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import java.util.concurrent.CompletableFuture;

/**
 * A TweakrFirebaseRepo that automatically creates unique, simple User Keys for each device so that
 * the user only can Tweak their own values, not other people's.
 *
 * Use {@link #getUserKey()} to display the User Key to the user as a "PIN Code", then send the user
 * to your Tweakr URL with "?promptForUserKey=true" appended. It will prompt them for their
 * "PIN Code" and let them Tweak those values.
 *
 * You can also specify a custom prompt message with the "?promptMessage=[message]" query string
 * parameter. This would be a good place to give your user instructions on where to find the user
 * key in your app. e.g.:
 *
 *   tweakr/index.html#/tweak?promptForUserKey=true&promptMessage=Enter%20pin%20from%20settings%20screen
 *
 *  Note: this is not at all secure. Users can simply click a different tab to alter other users'
 *  values. If you want to restrict users to their own data, try authenticating first, then return
 *  the userId from the getUserKey() method. Then alter your firebase rules to restrict read/write
 *  access to only that user key.
 */
public class TweakrFirebaseRepoMultiuser extends TweakrFirebaseRepo {

  private static final String PREF_USER_KEY = "PREF_USER_KEY";

  private final SharedPreferences prefs;
  private CompletableFuture<String> userKeyResult;

  public TweakrFirebaseRepoMultiuser(Context context) {
    this.prefs = context.getApplicationContext()
        .getSharedPreferences(TAG + "_USER_KEY", Context.MODE_PRIVATE);
  }

  @Override
  public synchronized CompletableFuture<String> getUserKey() {
    if (userKeyResult != null) return userKeyResult;

    String existingUserKey = loadUserKey();
    if (existingUserKey != null) {
      userKeyResult = CompletableFuture.completedFuture(existingUserKey);
    } else {
      userKeyResult = fetchNewUserKey();
    }

    return userKeyResult;
  }

  private CompletableFuture<String> fetchNewUserKey() {
    CompletableFuture<String> result = new CompletableFuture<>();

    DatabaseReference root = getDatabase()
        .getReference(getRootCollectionKey());

    root.runTransaction(new Transaction.Handler() {
      private String userKey;

      @Override
      @NonNull
      public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
        int lastId = 41; // Soon we will know the answer of the universe.
        Iterable<MutableData> children = mutableData.getChildren();
        for (MutableData child : children) {
          try {
            lastId = Math.max(lastId, Integer.parseInt(child.getKey()));
          } catch (NumberFormatException e) {
            Log.w(TAG, "Warning: non-integer values present as user keys. Skipping " + child.getKey());
          }
        }

        lastId++;

        // Set value and report transaction success
        userKey = Integer.toString(lastId);
        mutableData.child(userKey).setValue("empty");
        return Transaction.success(mutableData);
      }

      @Override
      public void onComplete(
          DatabaseError databaseError, boolean committed, DataSnapshot currentData) {
        if (committed) {
          //String userKey = currentData.getValue(String.class);
          saveUserKey(userKey);
          result.complete(userKey);
        } else {
          result.completeExceptionally(databaseError == null ? new Exception("Unknown error creating user.") : databaseError.toException());
        }
      }
    });

    return result;
  }

  private void saveUserKey(String userKey) {
    prefs.edit()
        .putString(PREF_USER_KEY, userKey)
        .apply();
  }


  private String loadUserKey() {
    return prefs.getString(PREF_USER_KEY, null);
  }
}
