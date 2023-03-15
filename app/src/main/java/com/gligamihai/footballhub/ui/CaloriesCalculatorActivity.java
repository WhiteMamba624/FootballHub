package com.gligamihai.footballhub.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CaloriesCalculatorActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText ageUser;
    EditText weightUser;
    EditText heightUser;
    Button calculateCalories;
    EditText caloriesToGainWeight;
    EditText caloriesToLoseWeight;
    EditText caloriesToMaintainWeight;
    Spinner activityType;
    CheckBox checkBoxMale;
    CheckBox checkBoxFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_calculator);
        ageUser=findViewById(R.id.editTextAge);
        weightUser=findViewById(R.id.editTextCaloriesWeight);
        heightUser=findViewById(R.id.editTextCaloriesHeight);
        calculateCalories=findViewById(R.id.calculateCaloriesButton);
        activityType=findViewById(R.id.spinnerActivityType);
        caloriesToGainWeight=findViewById(R.id.editTextCaloriesToGainWeight);
        caloriesToLoseWeight=findViewById(R.id.editTextCaloriesToLoseWeight);
        caloriesToMaintainWeight=findViewById(R.id.editTextCaloriesToMaintainWeight);
        checkBoxMale=findViewById(R.id.checkBoxMale);
        checkBoxFemale=findViewById(R.id.checkBoxFemale);
        String[] activityTypeList={"sedentary","light","moderate","heavy","hardcore"};
        String[] activitiesTypes={"Sedentary - Little or no exercise","Light Exercise (1 or 3 days/week", "Moderate Exercise (3 to 5 days/week","Heavy Exercise (6 to 7 days/week", "Hardcore exercise (daily)"};
        activityType.setAdapter(new ArrayAdapter<>(CaloriesCalculatorActivity.this,android.R.layout.simple_spinner_dropdown_item, activitiesTypes));
        getUserData();
        checkBoxMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBoxFemale.setClickable(false);
                }else
                    checkBoxFemale.setClickable(true);
            }
        });
        checkBoxFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxMale.setClickable(false);
                }else
                    checkBoxMale.setClickable(true);
            }
        });
        calculateCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=activityType.getSelectedItemPosition();
                int age= Integer.parseInt(ageUser.getText().toString().trim());
                int height=Integer.parseInt(heightUser.getText().toString().trim());
                double weight=Double.parseDouble(weightUser.getText().toString().trim());
                if(checkBoxMale.isChecked()) {
                    double bmr = calculateBMR("male", height, weight, age);
                    if(activityTypeList[i].equals("sedentary")){
                        double tdee=1.2*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("light")){
                        double tdee=1.375*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("moderate")){
                        double tdee=1.55*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("heavy")){
                        double tdee=1.725*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("hardcore")){
                        double tdee=1.9*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    }
                } else if(checkBoxFemale.isChecked()){
                    double bmr = calculateBMR("female", height, weight, age);
                    if(activityTypeList[i].equals("sedentary")){
                        double tdee=1.2*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("light")){
                        double tdee=1.375*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("moderate")){
                        double tdee=1.55*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("heavy")){
                        double tdee=1.725*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    } else if(activityTypeList[i].equals("hardcore")){
                        double tdee=1.9*bmr;
                        caloriesToMaintainWeight.setText(String.valueOf(Math.round(tdee)));
                        caloriesToLoseWeight.setText(String.valueOf(Math.round(tdee-(tdee*0.12))));
                        caloriesToGainWeight.setText(String.valueOf(Math.round(tdee+(tdee*0.15))));
                    }
                }
            }
        });
    }

public double calculateBMR(String gender,int height,double weight,int age){
        double bmr=0;
        if(gender.equals("male")){
            bmr=(10*weight +(6.25*height)-(5*age)+5);
        } else if(gender.equals("female")){
            bmr=(10*weight +(6.25*height)-(5*age)-161);
        }
        return bmr;
}
    public void getUserData(){
        DocumentReference docRef = db.collection("Users")
                .document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user=documentSnapshot.toObject(User.class);
                heightUser.setText(String.valueOf(user.getHeight()));
                weightUser.setText(String.valueOf(user.getWeight()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CaloriesCalculatorActivity.this,MainActivity.class));
    }
}