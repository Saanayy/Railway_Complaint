package com.example.sanay.complaint.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sanay.complaint.Models.ModelComplaintDetail;
import com.example.sanay.complaint.R;
import com.example.sanay.complaint.StatusAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {


    private RecyclerView statusRecycler;
    private List<ModelComplaintDetail> complaints = null;
    private StatusAdapter statusAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;


    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        statusRecycler = view.findViewById(R.id.status_complain_recycler);
        complaints = new ArrayList<>();

        statusAdapter = new StatusAdapter(complaints);
        statusRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        statusRecycler.setAdapter(statusAdapter);
        statusRecycler.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser()!=null) {
            String TAG = "hello";
            Log.d(TAG, "onCreateView: status");
            String userid = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("users").document(userid).collection("complaints").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                        switch (documentChange.getType()) {
                            case ADDED:
                                complaints.add(documentChange.getDocument().toObject(ModelComplaintDetail.class));
                                statusAdapter.notifyDataSetChanged();
                                break;

                        }
                    }
                }
            });


        }

        return view;
    }

}
