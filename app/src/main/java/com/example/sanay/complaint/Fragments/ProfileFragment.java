package com.example.sanay.complaint.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sanay.complaint.LoginActivity;
import com.example.sanay.complaint.Models.ModelUserData;
import com.example.sanay.complaint.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button logoutButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    EditText etName,etEmail,etPhone,etAddress;
    String TAG = "hello";
    
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logoutButton = view.findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d(TAG, "onCreateView: profileFragment");
        etName = view.findViewById(R.id.profile_name);
        etEmail = view.findViewById(R.id.profile_email);
        etAddress = view.findViewById(R.id.profile_address);
        etPhone = view.findViewById(R.id.profile_phone);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent signoutIntent = new Intent(container.getContext(), LoginActivity.class);
                startActivity(signoutIntent);
            }
        });

        if(mAuth.getCurrentUser()!=null)
        {
            String userid = mAuth.getCurrentUser().getUid();
            DocumentReference userdataRef = firebaseFirestore.collection("users").document(userid);
            userdataRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ModelUserData modelUserData = document.toObject(ModelUserData.class);
                            etName.setText(modelUserData.getName().toString());
                            etEmail.setText(modelUserData.getEmail().toString());
                            //etPhone.setText(resultMap.get("name").toString());
                            //etAddress.setText(resultMap.get("address").toString());

                        }
                    }
                }
            });

        }
        return view;
    }

}
