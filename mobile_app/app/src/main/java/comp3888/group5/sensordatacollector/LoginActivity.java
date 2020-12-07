package comp3888.group5.sensordatacollector;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.Executor;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button bLogin;
    Button bRegister;
    Button gSign;
    int RC_SIGN_IN = 1;

    FirebaseAuth fAuth;

    GoogleSignInClient mGoogleSignInClient;

    String admin = "admin@google.com";
    String adminpass = "admin123";

    EditText etUsername;

    EditText etPassword;

    ImageView mFingerprint;

    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    String lastEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lastEmail = null;
        etUsername = (EditText) findViewById(R.id.Email);
        etPassword = (EditText) findViewById(R.id.Password);

        bLogin = (Button) findViewById(R.id.login_button);
        bRegister = (Button) findViewById(R.id.bRegister);
        gSign = findViewById(R.id.googleSign);
        mFingerprint = (ImageView) findViewById(R.id.fingerprint);

        mFingerprint.setOnClickListener(this);
        gSign.setOnClickListener(this);
        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fAuth = FirebaseAuth.getInstance();


        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this,"Error: "+errString,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                SharedPreferences sp1= getSharedPreferences("LastLogin", MODE_PRIVATE );
                String lastemail = sp1.getString("Eml",null);
                String lastPass = sp1.getString("Psw",null);

                if(lastemail == null) {
                    Toast.makeText(LoginActivity.this,"You have to login once", Toast.LENGTH_SHORT).show();
                    return;
                }



                super.onAuthenticationSucceeded(result);

                //auth the user
                fAuth.signInWithEmailAndPassword(lastemail,lastPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivityForResult(new Intent(getApplicationContext(), ProfileActivity.class), 1);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,"Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                Toast.makeText(LoginActivity.this,"Auth succeed!",Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(getApplicationContext(), ProfileActivity.class), 1);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this,"Auth Failed!",Toast.LENGTH_SHORT).show();
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint authentication")
                .setNegativeButtonText("Cancel")
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if already logged in
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
    }

    private void signIn() {
        Log.i("signin","signin");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            if (requestCode == RC_SIGN_IN)
            {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        } else{
            return;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            FirebaseGoogleAuth(account);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w("Google Sign SignIn Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);
        }
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        fAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Sign in Successful With Google", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = fAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private  void updateUI(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();

            Toast.makeText(LoginActivity.this,"Weclome "+ personName + personEmail ,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bRegister) {
            startActivityForResult(new Intent(this, RegisterActivity.class), 1);
        }
        else if (v.getId() == R.id.login_button) {
            String email = etUsername.getText().toString();
            final String passW = etPassword.getText().toString();

            if(TextUtils.isEmpty(email))  {
                etUsername.setError("Email is Required");
                return;
            }

            if(TextUtils.isEmpty(passW))  {
                etPassword.setError("Password is Required");
                return;
            }


            //auth the user
            fAuth.signInWithEmailAndPassword(email,passW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,"Logged in Successfully", Toast.LENGTH_SHORT).show();
                        //store it locally
                        String email = etUsername.getText().toString();
                        String passW = etPassword.getText().toString();

                        SharedPreferences perferences = getSharedPreferences("MyPrefers",MODE_PRIVATE);
                        SharedPreferences.Editor editor = perferences.edit();
                        editor.putString(email,"");
                        editor.apply();


                        SharedPreferences sp=getSharedPreferences("LastLogin", 0);
                        SharedPreferences.Editor Ed=sp.edit();
                        Ed.clear();
                        Ed.putString("Eml",email );
                        Ed.putString("Psw",passW);
                        Ed.commit();


                        startActivityForResult(new Intent(getApplicationContext(), ProfileActivity.class), 1);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,"Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (v.getId() == R.id.googleSign) {
            signIn();

        } else if(v.getId() == R.id.fingerprint) {
            biometricPrompt.authenticate(promptInfo);
        }
    }

}