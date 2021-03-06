package edu.illinois.finalproject.PlayerGuides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import edu.illinois.finalproject.R;

public class PlayerGuides extends AppCompatActivity {
  private static final int RC_SIGN_IN = 123;
  public static FirebaseUser user = null;
  private Context context = this;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player_guides);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final Button viewGuides = findViewById(R.id.View_Guides_Button);
    final Button createGuides = findViewById(R.id.Create_Guides_Button);
    final Button editGuides = findViewById(R.id.editGuidesButton);

    viewGuides.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent viewGuideIntent = new Intent(context, ChampionSelect.class);
        viewGuideIntent.putExtra("key1", "view");
        context.startActivity(viewGuideIntent);
      }
    });


    createGuides.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Context context = view.getContext();
        Intent guideIntent = new Intent(context, ChampionSelect.class);
        if (user != null) {
          guideIntent.putExtra("key1", "create");
          context.startActivity(guideIntent);
        } else {
          // code from
          // https://firebase.google.com/docs/auth/android/firebaseui
          FirebaseApp.initializeApp(context);


          // Choose authentication providers
          List<AuthUI.IdpConfig> providers = Arrays.asList(
                  new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
//                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

          // Create and launch sign-in intent
          startActivityForResult(
                  AuthUI.getInstance()
                          .createSignInIntentBuilder()
                          .setAvailableProviders(providers)
                          .build(),
                  RC_SIGN_IN);
        }
      }
    });

    editGuides.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (user != null) {
          Intent editGuides = new Intent(context, EditGuides.class);
          context.startActivity(editGuides);
        } else {
          // code from
          // https://firebase.google.com/docs/auth/android/firebaseui
          FirebaseApp.initializeApp(context);


          // Choose authentication providers
          List<AuthUI.IdpConfig> providers = Arrays.asList(
                  new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
//                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

          // Create and launch sign-in intent
          startActivityForResult(
                  AuthUI.getInstance()
                          .createSignInIntentBuilder()
                          .setAvailableProviders(providers)
                          .build(),
                  321);
        }
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == ResultCodes.OK) {
        // Successfully signed in
        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent createGuideIntent = new Intent(context, ChampionSelect.class);
        createGuideIntent.putExtra("key1", "create");
        context.startActivity(createGuideIntent);
      } else {
        // Sign in failed, check response for error code
        // ...
      }
    }
    if (requestCode == 321) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == ResultCodes.OK) {
        // Successfully signed in
        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent editGuides = new Intent(context, EditGuides.class);
        context.startActivity(editGuides);
      } else {
        // Sign in failed, check response for error code
        // ...
      }
    }
  }

}
