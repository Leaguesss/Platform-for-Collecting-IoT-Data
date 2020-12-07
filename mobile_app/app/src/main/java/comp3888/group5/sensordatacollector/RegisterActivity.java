package comp3888.group5.sensordatacollector;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.app.Application;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comp3888.group5.sensordatacollector.firebase.FUserDao;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    Button bCancel;
    Button bRegister;
    EditText typeEmail;
    EditText typePassword;
    EditText confirmPassword;
    FirebaseAuth fAuth;
    Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        typeEmail =  (EditText) findViewById(R.id.typeEmail);
        typePassword =  (EditText) findViewById(R.id.typePassword);
        confirmPassword =  (EditText) findViewById(R.id.confirmPassword);

        fAuth = FirebaseAuth.getInstance();

        bRegister = (Button) findViewById(R.id.buttonTwo);
        bCancel = (Button) findViewById(R.id.buttonOne);

        bRegister.setOnClickListener(this);
        bCancel.setOnClickListener(this);

        final Drawable myIcon = getResources().getDrawable(R.drawable.my_tick);
        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());


        typeEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newEmail = typeEmail.getText().toString();
                String newPassword = typePassword.getText().toString();
                String newConfirmpass = confirmPassword.getText().toString();

                if(!isEmailValid(newEmail)) {
                    typeEmail.setError("Wrong email format");

                } else{
                    fAuth.fetchSignInMethodsForEmail(newEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.getResult().getSignInMethods().size() == 0){
                                // email not existed

                                typeEmail.setError("Good",myIcon);
                                check = true;
                            }else {
                                typeEmail.setError("Email Already exists");
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }



            }

        });

    }



    public  boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Boolean checkPassLength(String newPassword) {
        return newPassword.length() > 6 && newPassword.length() < 16;

    }

    public Boolean checkPassMatch(String newConfirmpass, String newPassword) {
        return newPassword.equals(newConfirmpass);
    }




    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonTwo){

            String newEmail = typeEmail.getText().toString();
            String newPassword = typePassword.getText().toString();
            String newConfirmpass = confirmPassword.getText().toString();


            if(TextUtils.isEmpty(newEmail))  {
                typeEmail.setError("Email is Required");
                return;
            }

            if(!check) {
                return;
            }


            if(!checkPassLength(newPassword)) {
                typePassword.setError("Password should between 6 and 16 digits");
                return;
            }
            if(!checkPassMatch(newConfirmpass,newPassword)){
                confirmPassword.setError("Password is not match");
                return;
            }

            if(check == true) {
                // register the user in firebase
                fAuth.createUserWithEmailAndPassword(newEmail,newPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Add new user to Firebase realtime db
                            FUserDao.createNewUser(FirebaseAuth.getInstance().getCurrentUser());
                            Toast.makeText(RegisterActivity.this,"User Created", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,"Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                finish();

            }

        }else if(v.getId() == R.id.buttonOne){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}