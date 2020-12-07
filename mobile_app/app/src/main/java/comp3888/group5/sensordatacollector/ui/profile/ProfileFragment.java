package comp3888.group5.sensordatacollector.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import comp3888.group5.sensordatacollector.R;
import comp3888.group5.sensordatacollector.LoginActivity;
import comp3888.group5.sensordatacollector.firebase.FUser;
import comp3888.group5.sensordatacollector.firebase.FUserDao;
//import comp3888.group5.sensordatacollector.util.StringMapConverter;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    View root;
    private TextView detailedInfoTextView;
    private TextView profileDescriptionTextView;
    private Button resetPasswordBtn;
    private Button logoutBtn;
    //private Button saveBtn;
    private Button cancelBtn;
    private Button editBtn;
    private FirebaseUser currentUser;
    private String name;
    private String age;
    private String weight;
    private String occupation;

    private Map<String, String> profileInfoMap;
//    private Map<String, String> detailedInfoMap;
    private EditText nameEdit;
    private EditText ageEdit;
    private EditText weightEdit;
    private EditText occupationEdit;

    private TextView nameText;
    private TextView ageText;
    private TextView weightText;
    private TextView occupationText;
    private TextView notification;
    private ProgressBar spinner;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.i("PROFILE FRAGMENT", "CREATED");
        profileDescriptionTextView = root.findViewById(R.id.profileDescriptionTextView);

        nameText = (TextView) root.findViewById(R.id.usernameText);
        ageText = (TextView) root.findViewById(R.id.ageText);
        weightText = (TextView) root.findViewById(R.id.weightText);
        occupationText = (TextView) root.findViewById(R.id.occupationText);
        notification = (TextView) root.findViewById(R.id.notification);
        notification.setText("You get 10 points for completing each information");
        nameEdit = (EditText) root.findViewById(R.id.username);
        ageEdit = (EditText) root.findViewById(R.id.age);
        weightEdit = (EditText) root.findViewById(R.id.weight);
        occupationEdit = (EditText) root.findViewById(R.id.occupation);

        resetPasswordBtn = root.findViewById(R.id.resetPasswordBtn);
        logoutBtn = root.findViewById(R.id.logoutBtn);
        editBtn = root.findViewById(R.id.editBtn);
        cancelBtn = root.findViewById(R.id.cancelBtn);

        spinner = (ProgressBar)root.findViewById(R.id.progressBar1);
        resetPasswordBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        spinner.setVisibility(View.VISIBLE);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    currentUser = firebaseAuth.getCurrentUser();
                    // Busy wait for result
                    while(!currentUser.getIdToken(false).isComplete()){
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!Objects.equals(Objects.requireNonNull(
                            currentUser.getIdToken(false).getResult()).getSignInProvider(), "google.com"))
                    {
                        resetPasswordBtn.setVisibility(View.VISIBLE);
                    }
                    getProfileInfo(currentUser);
                    getUserInfoFromFirebase();
                }
            }
        });
    }

    private void getUserInfoFromFirebase(){
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        users.orderByChild("uid").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // if user does not has entry in database
                        if(snapshot.getValue() == null){
                            Log.i("Profile Fragment", "creating new user instance in Firebase");
                            FUserDao.createNewUser(currentUser);
                            getdetailedInfo(root, null);
                            spinner.setVisibility(View.GONE);
                            return;
                        }

                        Map<?,?> userInfo = (HashMap) ((HashMap) snapshot.getValue()).get(currentUser.getUid());
                        getdetailedInfo(root, userInfo);
                        spinner.setVisibility(View.GONE);
                        Log.i("Profile Fragment", "get datasnap"+ userInfo.get("email"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        spinner.setVisibility(View.GONE);
                        Log.i("Profile Fragment", error.getMessage());
                    }
        });
    }

    // TODO: get profile info from remote database and update profile textview
    private void getProfileInfo(FirebaseUser currentUser){
         // Mock
        profileInfoMap = new LinkedHashMap<>();
        if(currentUser != null){
            profileInfoMap.put("Email", currentUser.getEmail());
            Date date = new Date(currentUser.getMetadata().getCreationTimestamp());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Australia/Sydney"));
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            profileInfoMap.put("Date joined", day + "." + month + "." + year);
        }
        profileDescriptionTextView.setText(mapToText(profileInfoMap));
    }

    // TODO: get detailed info from local database and update detail textview
    private void getdetailedInfo(View root, Map<?,?> userInfo){
        // Mock
        if(userInfo != null){
            name = (String) userInfo.get("userName");
            age = (String) userInfo.get("yob");
            weight = (String) userInfo.get("weight");
            occupation = (String) userInfo.get("occupation");
        }

        nameEdit.setText(name);
        ageEdit.setText(age);
        weightEdit.setText(weight);
        occupationEdit.setText(occupation);


        if (name == null){
            nameText.setText("None");
        }else {
            nameText.setText(name);
        }

        if (age == null){
            ageText.setText("None");
        }else {
            ageText.setText(age);
        }

        if (weight == null){
            weightText.setText("None");
        }else {
            weightText.setText(weight);
        }

        if (occupation == null){
            occupationText.setText("None");
        }else {
            occupationText.setText(occupation);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editBtn:
                if (editBtn.getText().equals("Edit")){
                editBtnOnClick();
                }else{
                saveBtnOnClick();
                }
                break;
            case R.id.cancelBtn:
                cancelBtnOnClick();
                break;
            case R.id.resetPasswordBtn:
                resetBtnOnClick();
                break;
            case R.id.logoutBtn:
                logoutBtnOnClick();
                break;
        }
    }




    public boolean checkNameLength(String name){
        return name.length() != 0 && name.length() <= 12;
    }

    public boolean checkOccupation(String occupation){
        return occupation.length() <= 30;
    }

    public boolean checkAge(String age){
        try {

            int year=Integer.parseInt(age);
            if(year > 2020||year<1800){
                return false;
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }

    }

    public boolean checkWeight(String weight){
        try {
            int i=Integer.parseInt(weight);
            return (i>12 && i<500);
        }
        catch(Exception e) {
            return false;
        }

    }

    private void cancelBtnOnClick() {
        nameEdit.setVisibility(View.INVISIBLE);
        ageEdit.setVisibility(View.INVISIBLE);
        weightEdit.setVisibility(View.INVISIBLE);
        occupationEdit.setVisibility(View.INVISIBLE);

        // restore to original value
        nameEdit.setText(name);
        nameEdit.setError(null);
        ageEdit.setText(age);
        ageEdit.setError(null);
        weightEdit.setText(weight);
        weightEdit.setError(null);
        occupationEdit.setText(occupation);
        occupationEdit.setError(null);

        nameText.setVisibility(View.VISIBLE);
        ageText.setVisibility(View.VISIBLE);
        weightText.setVisibility(View.VISIBLE);
        occupationText.setVisibility(View.VISIBLE);
        editBtn.setText("Edit");
        cancelBtn.setVisibility(View.INVISIBLE);
    }

    private void saveBtnOnClick() {
        String newName = String.valueOf(nameEdit.getText());
        String newAge = String.valueOf(ageEdit.getText());
        String newWeight = String.valueOf(weightEdit.getText());
        String newOccupation = String.valueOf(occupationEdit.getText());
        if (!checkNameLength(newName)){
            if(name != null && newName.length() == 0) {
                nameEdit.setError("New name can not be none");
                return;
            }else if (checkNameLength(newName)){
                nameEdit.setError("Invalid name");
            }
            return;
        }


        String ageCheck = ageEdit.getText().toString();
        if(age != null && ageCheck.length() == 0) {
            ageEdit.setError("New date can not be none");
            return;
        }else if (!checkAge(ageCheck)){
            ageEdit.setError("Invalid year");
            return;
        }


        String weightCheck = weightEdit.getText().toString();
        if(weight != null && weightCheck.length() == 0) {
            weightEdit.setError("New weight can not be none");
            return;
        }else if (!checkWeight(weightCheck)){
            weightEdit.setError("Invalid weight");
            return;
        }


        String occupationCheck = occupationEdit.getText().toString();
        if(occupation != null && occupationCheck.length() == 0) {
            occupationEdit.setError("New occupation can not be none");
            return;
        }else if (!checkOccupation(occupationCheck)){
            occupationEdit.setError("Invalid occupation");
            return;
        }

        name = newName;
        age = newAge;
        weight = newWeight;
        if (newOccupation.length() != 0) {
            occupation = newOccupation;
            occupationEdit.setText(occupation);
            occupationText.setText(occupation);
        }else {
            occupation = null;
            occupationEdit.setText("");
            occupationText.setText("None");
        }
        nameEdit.setVisibility(View.INVISIBLE);
        nameEdit.setText(name);
        nameEdit.setError(null);
        ageEdit.setVisibility(View.INVISIBLE);
        ageEdit.setText(age);
        ageEdit.setError(null);
        weightEdit.setVisibility(View.INVISIBLE);
        weightEdit.setText(weight);
        weightEdit.setError(null);
        occupationEdit.setVisibility(View.INVISIBLE);
        nameText.setVisibility(View.VISIBLE);
        nameText.setText(name);
        ageText.setVisibility(View.VISIBLE);
        ageText.setText(age);
        weightText.setVisibility(View.VISIBLE);
        weightText.setText(weight);
        occupationText.setVisibility(View.VISIBLE);
        editBtn.setText("Edit");
        cancelBtn.setVisibility(View.INVISIBLE);
    }

    private void editBtnOnClick(){
        nameEdit.setVisibility(View.VISIBLE);
        ageEdit.setVisibility(View.VISIBLE);
        weightEdit.setVisibility(View.VISIBLE);
        occupationEdit.setVisibility(View.VISIBLE);

        nameText.setVisibility(View.INVISIBLE);
        ageText.setVisibility(View.INVISIBLE);
        weightText.setVisibility(View.INVISIBLE);
        occupationText.setVisibility(View.INVISIBLE);
        editBtn.setText("Save");
        cancelBtn.setVisibility(View.VISIBLE);

    }


    private void logoutBtnOnClick(){
        FirebaseAuth.getInstance().signOut(); //; log out firebase

        GoogleSignIn.getClient(  //google sign out
                getContext(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void resetBtnOnClick(){
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(currentUser.getEmail())
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ResetPasswordError", e.toString());
            }
        })
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("ResetPassword", "Receive response from Firebase Console");
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("ResetPassword", "Reset password email has been successfully sent to the email");
            }
        });
    }

    private String mapToText(Map<String, String> infoMap){
        StringBuilder infoBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : infoMap.entrySet()){
            infoBuilder
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue()==null?"-":entry.getValue())
                    .append("\n");
        }
        return infoBuilder.toString();
    }
}