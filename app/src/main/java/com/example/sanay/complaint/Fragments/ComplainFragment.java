package com.example.sanay.complaint.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sanay.complaint.Models.ModelComplaintDetail;
import com.example.sanay.complaint.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import id.zelory.compressor.Compressor;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComplainFragment extends Fragment {

    private Spinner categorySpinner, subcategorySpinner;
    private ImageView complaintImage;
    private EditText complaintDesc;
    private Button complaintSubmit;
    private Uri imageuri = null;
    private String downloadUrl;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private Bitmap compressedImageFile;
    private ProgressBar mProgress;
    private FirebaseAuth mAuth;

    public ComplainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complain, container, false);
        categorySpinner = view.findViewById(R.id.category_spinner);
        subcategorySpinner = view.findViewById(R.id.subcategory_spinner);
        complaintImage = view.findViewById(R.id.complaint_image);
        complaintDesc = view.findViewById(R.id.complaint_text);
        complaintSubmit = view.findViewById(R.id.submit_complaint);
        mProgress = view.findViewById(R.id.complaint_progress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        final List<String> categories = new ArrayList<String>();
        categories.add("None");
        categories.add("Mason");
        categories.add("Plumber");
        categories.add("Carpenter");
        categories.add("Road Work");
        categories.add("Periodic");
        categories.add("Urgent");

        final List<List<String>> subcategories = new ArrayList<List<String>>();
        subcategories.add(Arrays.asList("None"));
        subcategories.add(Arrays.asList("aitem1", "item2", "item3", "item4"));
        subcategories.add(Arrays.asList("bitem1", "item2", "item3", "item4"));
        subcategories.add(Arrays.asList("citem1", "item2", "item3", "item4"));
        subcategories.add(Arrays.asList("ditem1", "item2", "item3", "item4"));
        subcategories.add(Arrays.asList("eitem1", "item2", "item3", "item4"));
        subcategories.add(Arrays.asList("fitem1", "item2", "item3", "item4"));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    subcategorySpinner.setVisibility(View.GONE);
                } else {
                    subcategorySpinner.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item, subcategories.get(position));
                    subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subcategorySpinner.setAdapter(subCategoryAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        complaintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(container.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(container.getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });
        final String TAG = "hello";
        complaintSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cat = categorySpinner.getSelectedItem().toString();
                final String subcat = subcategorySpinner.getSelectedItem().toString();
                final String desc = complaintDesc.getText().toString();


                if (!TextUtils.isEmpty(cat) && !TextUtils.isEmpty(subcat) && !TextUtils.isEmpty(desc) && imageuri != null) {
                    mProgress.setVisibility(View.VISIBLE);
                    final String randomName = UUID.randomUUID().toString();
                    complaintSubmit.setEnabled(false);
                    // PHOTO UPLOAD
                    File newImageFile = new File(imageuri.getPath());
                    try {

                        compressedImageFile = new Compressor(getContext())
                                .setMaxHeight(200)
                                .setMaxWidth(200)
                                .setQuality(10)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();
                    final UploadTask filePath = storageReference.child("complaint-images").child(randomName + ".jpg").putBytes(imageData);
                    final StorageReference sref = storageReference.child("complaint-images").child(randomName + ".jpg");
                    Task<Uri> task = filePath.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                            return sref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadUrl = task.getResult().toString();
                                Log.d(TAG, "onSuccess: hua h" + downloadUrl);
                                ModelComplaintDetail modelComplaintDetail = new ModelComplaintDetail(cat, subcat, downloadUrl, desc, false);
                                String complaintId = UUID.randomUUID().toString();
                                firebaseFirestore.collection("complaints").document(complaintId).set(modelComplaintDetail);
                                String userid = mAuth.getCurrentUser().getUid();
                                firebaseFirestore.collection("users").document(userid).collection("complaints")
                                        .document(complaintId).set(modelComplaintDetail);

                                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                mProgress.setVisibility(View.GONE);
                                complaintSubmit.setEnabled(true);
                                categorySpinner.setSelection(0);
                                imageuri = null;
                                complaintImage.setImageResource(R.drawable.primary_color_btn);
                                complaintDesc.getText().clear();

                            }
                        }
                    });


                } else {
                    mProgress.setVisibility(View.GONE);
                    complaintSubmit.setEnabled(true);
                }
            }
        });


        return view;
    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(getContext(), this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                imageuri = result.getUri();
                complaintImage.setImageURI(imageuri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }


}
