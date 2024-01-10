package com.pointage1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pointage1.Model.Utilisateur;
import com.squareup.picasso.Picasso;/*
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;*/

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
//implements IPickResult
public class ProfileActivity extends AppCompatActivity  {

    private Button buttonLogin;
    private Button buttonRegister;
    private LinearLayout profileLinearLayout;
    private LinearLayout loginAcceuilLinearLayout;
    private TextView textViewNomEtPrenom;
    private ImageView imageViewImageProfile;
    private ImageView imageViewCover;
    private TextView textViewEmail;
    private TextView textViewTelephone;
    private TextView textViewCin;
    private TextView textViewMatricule;
    private LinearLayout matriculeLinearLayout;
    private TextView textViewDateDeNaissance;
    private Button buttonLogout;
    private FloatingActionButton fab;
    private ProgressDialog pd;


    //Firebase
    private FirebaseDatabase userDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private DatabaseReference reference;
    private String firebaseId;
    //storage
    StorageReference storageReference;


    private boolean connectedUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDBRef;


    //permissions constants
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE=300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE=400;

    //arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];

    //Uri of picked image
    Uri image_uri;

    // for checking profile or cover photo
    String profileOrCoverPhoto;

    //path where images of user profile and cover will be stored
    String storagePath= "Users_Profile_Cover_Imgs/";

    private String uriGallerie;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        profileLinearLayout = findViewById(R.id.profileLinearLayout);
        loginAcceuilLinearLayout = findViewById(R.id.loginAcceuilLinearLayout);
        textViewNomEtPrenom =(TextView) findViewById(R.id.textViewNomEtPrenom);
        imageViewImageProfile =(ImageView) findViewById(R.id.imageViewImageProfile);
        imageViewCover =(ImageView) findViewById(R.id.imageViewCover);
        textViewEmail =(TextView) findViewById(R.id.textViewEmail);
        textViewTelephone =(TextView) findViewById(R.id.textViewTelephone);
        textViewCin =(TextView) findViewById(R.id.textViewCin);
        textViewMatricule =(TextView) findViewById(R.id.textViewMatricule);
        matriculeLinearLayout =(LinearLayout) findViewById(R.id.matriculeLinearLayout);
        textViewDateDeNaissance =(TextView) findViewById(R.id.textViewDateDeNaissance);
        buttonLogout =(Button) findViewById(R.id.buttonLogout);
        fab=(FloatingActionButton) findViewById(R.id.fab);




        firebaseAuth=FirebaseAuth.getInstance();
        checkUserStatus();
        System.out.println("3");
        System.out.println("connectedUser"+connectedUser);
        if (connectedUser==false) {
            //Cette partie si l'utilisateur n'est pas connecté
            profileLinearLayout.setVisibility(View.GONE);
            loginAcceuilLinearLayout.setVisibility(View.VISIBLE);


            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });




        }else {
            //Cette partie si l'utilisateur est connecté
            profileLinearLayout.setVisibility(View.VISIBLE);
            loginAcceuilLinearLayout.setVisibility(View.GONE);






            userDatabase = FirebaseDatabase.getInstance();
            reference = userDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            firebaseId =user.getUid();

            //firebase storage reference
            storageReference = FirebaseStorage.getInstance().getReference();


            //init progress dialog
            pd=new ProgressDialog(this);
            //pd=new ProgressDialog(getActivity());

            //inti array of permission
            cameraPermissions= new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            storagePermissions= new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


            firebaseAuth=FirebaseAuth.getInstance();
            checkUserStatus();

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    checkUserStatus();
                    Intent im0 = new Intent(ProfileActivity.this, AcceuilActivity.class);
                    startActivity(im0);
                }
            });




            stateListener();
            gettingDatabase();

        }

        //Bottom Menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // do stuff
            switch (item.getItemId()) {
                case R.id.page_1_acceuil:
                    startActivity(new Intent(ProfileActivity.this, AcceuilActivity.class));
                    return true;
                case R.id.page_2_profile:
                    //startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                    return true;

            }
            return true;
        });
    }

    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            //user is signed in stay here
            connectedUser=true;
            //myUID=user.getUid();
        }else{
            //user not signed in, go to MainActivity
            //startActivity(new Intent(this, MainActivity.class));
            //finish();

            connectedUser=false;
        }
    }

    private void stateListener() {
        stateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public  void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){
                    //Toast.makeText(ProfileUtilisateurActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),"Logged out",Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                    finish();
                    //getActivity().finish();
                }
            }
        };
    }


    private void gettingDatabase(){
        System.out.println("firebaseId3 :"+firebaseId);





        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        System.out.println("fUser.getEmail():"+fUser.getEmail());

        //get all data ref =reference
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Utilisateur modelUser=ds.getValue(Utilisateur.class);

                    /*Conditions to fulfil search :
                      1) User not current user
                      2) The user name or email contains text entered in SearchView (case insensitive)
                     */

                    //get all searched users except currently signed in user
                    System.out.println("modelUser.getFirstName():"+modelUser.getNom());
                    System.out.println("fUser.getUid():"+fUser.getUid());
                    if(modelUser.getId().equals(fUser.getUid())){
                        //Utilisateur information = new Utilisateur(id, email, nom, prenom, cin, matricule, motdepasse, telephone, image, cover, date_naissance, date_inscription);
                        Utilisateur information = new Utilisateur();
                        information.setNom(modelUser.getNom());
                        information.setPrenom(modelUser.getPrenom());
                        information.setEmail(modelUser.getEmail());
                        information.setTelephone(modelUser.getTelephone());
                        information.setCin(modelUser.getCin());
                        information.setMatricule(modelUser.getMatricule());
                        information.setImage(modelUser.getImage());
                        information.setCover(modelUser.getCover());
                        information.setDate_inscription(modelUser.getDate_inscription());
                        information.setDate_naissance(modelUser.getDate_naissance());
                        information.setId(modelUser.getId());

                        textViewNomEtPrenom.setText(information.getNom()+" "+information.getPrenom());
                        textViewEmail.setText(information.getEmail());
                        textViewCin.setText(information.getCin());
                        textViewTelephone.setText(information.getTelephone());
                        textViewDateDeNaissance.setText(information.getDate_naissance());


                        if(!information.getMatricule().equals("")){
                            textViewMatricule.setText(information.getMatricule());
                            matriculeLinearLayout.setVisibility(View.VISIBLE);
                        }else {
                            matriculeLinearLayout.setVisibility(View.GONE);
                        }


                        if(!information.getImage().equals("")){
                            try{
                                //if image is received then set
                                Picasso.get().load(information.getImage()).into(imageViewImageProfile);
                            } catch (Exception e) {
                                e.printStackTrace();
                                //if there is any exception while getting image then set default
                                Picasso.get().load(R.drawable.ic_baseline_photo_camera_24).into(imageViewImageProfile);
                            }
                        }


                        if(!information.getCover().equals("")){
                            try{
                                //if image is received then set
                                Picasso.get().load(information.getCover()).into(imageViewCover);
                            } catch (Exception e) {
                                e.printStackTrace();
                                //if there is any exception while getting image then set default
                                //Picasso.get().load(R.drawable.ic_baseline_face_white_24).into(coverIv);
                            }
                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //fab button click
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showEditProfileDialog();
            }
        });
    }


    private void showEditProfileDialog(){
        //pas la peine d'ajouter la photo de couverture
        // option to show in dialog
        String options[] = {"Modifier Photo de Profile",  "Modifier Photo de Couverture",  "Modifier Prénom","Modifier Nom",  "Modifier Téléphone","Modifier Carte d'identité","Modifier Date de naissance"};
        //String options[] = {"Edit Profile Picture",  "Edit Cover photo",  "Edit First Name","Edit Last Name",  "Edit Phone"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //set Title
        builder.setTitle("Choose Action");

        builder.setItems(options,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(which==0){
                    //Edit Profile Picture
                    //pd.setMessage("Updating Profile Picture");
                    pd.setMessage("Modification Photo de Profile");
                    profileOrCoverPhoto="image"; //changing profile picture, make sure to assign same value
                    showImagePicDialog();
                }else if(which==1){
                    //Edit Cover Photo
                    //pd.setMessage("Updating Cover photo");
                    pd.setMessage("Modification Photo de Couverture");
                    profileOrCoverPhoto="cover";//changing cover photo, make sure to assign same value
                    showImagePicDialog();
                }else  if(which==2){
                    //Edit Name
                    //pd.setMessage("Updating FirstName");
                    pd.setMessage("Modification Prénom");
                    //calling method and pass key "name" as parameter to update it's value in database
                    showNamePhoneUpdateDialog("Prénom");
                }else  if(which==3){
                    //Edit Name
                    //pd.setMessage("Updating LastName");
                    pd.setMessage("Modification Nom");
                    //calling method and pass key "name" as parameter to update it's value in database
                    showNamePhoneUpdateDialog("Nom");
                }else if(which==4){
                    //Edit Phone
                    //pd.setMessage("Updating Phone");
                    pd.setMessage("Modification Téléphone");
                    showNamePhoneUpdateDialog("Téléphone");
                }else if(which==5){
                    //Edit Phone
                    //pd.setMessage("Updating Phone");
                    pd.setMessage("Modification Carte d'identité");
                    showNamePhoneUpdateDialog("Carte d'identité");
                }else if(which==6){
                    //Edit Phone
                    //pd.setMessage("Updating Phone");
                    pd.setMessage("Modification Date de naissance");
                    showNamePhoneUpdateDialog("Date de naissance");
                }
            }
        });

        //create and show dialog
        builder.create().show();

    }

    private void showNamePhoneUpdateDialog(final String key) {
        /*
        parameter "key" will contain value:
        either "name" which is key in user's database which is used to update user's name
        or     "phone" which is key in user's database which is used to update user's phone

         */

        //custom dialog
        //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUtilisateurActivity.this);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        //builder.setTitle("Update "+key);//e.g Update name Or Update phone
        builder.setTitle("Modifier "+key);//e.g Update name Or Update phone
        //set  layout of dialog
        LinearLayout linearLayout = new LinearLayout(this);
        //LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        final EditText editText = new EditText(this);
        //final EditText editText = new EditText(getActivity());
        //editText.setHint("Enter "+key);//hint e.g Edit name Or Edit phone
        editText.setHint("Entrer "+key);//hint e.g Edit name Or Edit phone
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add button in dialog to update
        //builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
        builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                final String value = editText.getText().toString().trim();
                //Validate if user has entered something or not
                if(!TextUtils.isEmpty(value)){
                    //pd.show();
                    //HashMap<String,Object> result = new HashMap<>();
                    //result.put(key,value);



                    reference.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                            for(DataSnapshot ds:dataSnapshot.getChildren()){

                                if (key=="Téléphone"){
                                    reference.child("users").child(firebaseId).child("telephone").setValue(value);
                                }
                                if (key=="Prénom"){
                                    reference.child("users").child(firebaseId).child("prenom").setValue(value);
                                }
                                if (key=="Nom"){
                                    reference.child("users").child(firebaseId).child("nom").setValue(value);
                                }
                                if (key=="Carte d'identité"){
                                    reference.child("users").child(firebaseId).child("cin").setValue(value);
                                }
                                if (key=="Date de naissance"){
                                    reference.child("users").child(firebaseId).child("date_naissance").setValue(value);
                                }



                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError)    {
                            //Toast.makeText(ProfileUtilisateurActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(ProfileActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    });

                    finish();
                    //getActivity().finish();
                    startActivity(getIntent());
                    //startActivity(getActivity().getIntent());

                }
                else {
                    //Toast.makeText(ProfileUtilisateurActivity.this,"Please enter "+key,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this,"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add button in dialog to cancel
        //builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //create and show dialog
        builder.create().show();
    }


    private void showImagePicDialog() {
        //show dialog containing options Camera and Gallery to pick the image
        // option to show in dialog
        String options[] = {"Camera",  "Gallery"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //set Title
        //builder.setTitle("Pick Image From");
        builder.setTitle("Choisi Image Depuis");

        builder.setItems(options,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(which==0){
                    //Camera clicked
                    //pd.setMessage("Updating Profile Picture");
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        //pickFromCamera();
                        //openSomeActivityForResult1();
                        dispatchTakePictureIntent();
                    }

                }else if(which==1){
                    //Gallery clicked
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        //pickFromGallery();
                        openSomeActivityForResult();
                    }
                }
            }
        });

        //create and show dialog
        builder.create().show();

        /* for picking image from:
        Camera  [Camera and storage permission required]
        Gallery [Storage permission required]
         */

    }



    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        /*boolean result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);*/

        return result;
    }

    private void requestStoragePermission(){
        System.out.println("requestStoragePermission2222222");
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermissions,STORAGE_REQUEST_CODE);
        //ActivityCompat.requestPermissions(getActivity(), storagePermissions,STORAGE_REQUEST_CODE);
        System.out.println("requestStoragePermission33333333");

    }

    private boolean checkCameraPermission(){

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        /*boolean result = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);*/
        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
        //ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);

    }


    public void openSomeActivityForResult() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //Intent intent = new Intent(this, AddPostActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK ) {
                        // There are no request codes
                        Intent data = result.getData();
                        System.out.println("data:"+data.getDataString());
                        uriGallerie=data.getDataString();


                        System.out.println("Uri.parse(uriGallerie):"+Uri.parse(uriGallerie));
                        if(profileOrCoverPhoto=="image"){
                            imageViewImageProfile.setImageURI(Uri.parse(uriGallerie));
                        }else{
                            imageViewCover.setImageURI(Uri.parse(uriGallerie));
                        }



                        //image is picked from gallery, get uri of image
                        image_uri=data.getData();
                        System.out.println("image_uri:"+image_uri);
                        uploadProfileCoverPhoto(image_uri,false);


                        //doSomeOperations();

                    }
                }
            });

    public void openSomeActivityForResult1() {

        /*PickSetup setup = new PickSetup();

        PickImageDialog dialog = PickImageDialog.build(setup)
                .setOnClick(new IPickClick() {
                    @Override
                    public void onGalleryClick() {
                        //Toast.makeText(SampleActivity.this, "Gallery Click!", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), "Gallerie Cliquée!", Toast.LENGTH_LONG).show();
                        Toast.makeText(ProfileActivity.this, "Gallerie Cliquée!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCameraClick() {
                        //Toast.makeText(SampleActivity.this, "Camera Click!", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), "Camera Cliquée!", Toast.LENGTH_LONG).show();
                        Toast.makeText(ProfileActivity.this, "Camera Cliquée!", Toast.LENGTH_LONG).show();
                    }
                }).show(this);*/
                //}).show(getActivity());
        //dialog.dismiss();
    }


    private void uploadProfileCoverPhoto(Uri uri,Boolean fromCamera) {
        //show progress
        pd.show();

        /*Instead of creating separate function for Profile Picture and Cover Photo
        I am doing work for both in same function

        To add check i will add a string variable and assign it value "image" when user clicks
        "Edit Profile Picture", and assign it value "cover" when user clicks "Edit Cover Photo"
        Here : image is the key in each user containing url of user's profile picture
               cover is the key in each user containing url of user's cover photo
         */

        /* The parameter "image_uri" contains the uri of image picked either from camera or gallery
        we will use UID of the currently signed in user as name of the image so there will be only one image
        profile and one image cover for each user
         */

        //path and name of image to be stored in firebase storage
        //e.g. Users_Profile_Cover_Imgs/image_e12f3456f789.jpg
        //String filePathAndName = storagePath + ""+profileOrCoverPhoto+"_"+user.getUid();
        String filePathAndName = storagePath + ""+profileOrCoverPhoto+"_"+user.getEmail();

        System.out.println("uri:"+uri.toString());


        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        if(!fromCamera) {
            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded to storage, now get it's url and store in user's database
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();
                            System.out.println("downloadUri:" + downloadUri);


                            //check if image is uploaded or not and url is received
                            if (uriTask.isSuccessful()) {
                                //image uploaded
                                //add/update url in user's database
                                //HashMap<String,Object> results = new HashMap<>();
                            /*
                            First Parameter is profileOrCoverPhoto that has value "image" or "cover"
                            which are keys in user's database where url of image will be saved in one
                            of them
                            Second Parameter contains the url of the image stored in firebase storage, this
                            url will be saved as value against key "image" or "cover"
                             */
                                //results.put(profileOrCoverPhoto, downloadUri.toString());

                                //reference.child(user.getUid()).updateChildren(results)
                                reference.child("users").child(firebaseId).child(profileOrCoverPhoto).setValue(downloadUri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //url in database of user is added successfully
                                                //dismiss progress bar
                                                pd.dismiss();
                                                //Toast.makeText(ProfileUtilisateurActivity.this,"Image Updated...",Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(getActivity(),"Mise à jour de l'image avec succés...",Toast.LENGTH_SHORT).show();
                                                Toast.makeText(ProfileActivity.this, "Mise à jour de l'image avec succés...", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //error adding url in database of user
                                                //dismiss progress bar
                                                pd.dismiss();
                                                //Toast.makeText(ProfileUtilisateurActivity.this,"Error Updating Image ...",Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(getActivity(),"Erreur lors de la mise à jour de l'image... ...",Toast.LENGTH_SHORT).show();
                                                Toast.makeText(ProfileActivity.this, "Erreur lors de la mise à jour de l'image... ...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                //error
                                pd.dismiss();
                                //Toast.makeText(ProfileUtilisateurActivity.this,"Some error occured",Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(),"Une erreur s'est produite ",Toast.LENGTH_SHORT).show();
                                Toast.makeText(ProfileActivity.this, "Une erreur s'est produite ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // there were some error(s) , get and show error message, dismiss progress dialog
                    pd.dismiss();
                    //Toast.makeText(ProfileUtilisateurActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            //Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException e:" + e);
                System.out.println("bmp:" + bmp);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            storageReference2nd.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded to storage, now get it's url and store in user's database
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();
                            System.out.println("downloadUri:" + downloadUri);


                            //check if image is uploaded or not and url is received
                            if (uriTask.isSuccessful()) {
                                //image uploaded
                                //add/update url in user's database
                                //HashMap<String,Object> results = new HashMap<>();
                            /*
                            First Parameter is profileOrCoverPhoto that has value "image" or "cover"
                            which are keys in user's database where url of image will be saved in one
                            of them
                            Second Parameter contains the url of the image stored in firebase storage, this
                            url will be saved as value against key "image" or "cover"
                             */
                                //results.put(profileOrCoverPhoto, downloadUri.toString());

                                //reference.child(user.getUid()).updateChildren(results)
                                reference.child("users").child(firebaseId).child(profileOrCoverPhoto).setValue(downloadUri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //url in database of user is added successfully
                                                //dismiss progress bar
                                                pd.dismiss();
                                                //Toast.makeText(ProfileUtilisateurActivity.this,"Image Updated...",Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(getActivity(),"Mise à jour de l'image avec succés...",Toast.LENGTH_SHORT).show();
                                                Toast.makeText(ProfileActivity.this, "Mise à jour de l'image avec succés...", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //error adding url in database of user
                                                //dismiss progress bar
                                                pd.dismiss();
                                                //Toast.makeText(ProfileUtilisateurActivity.this,"Error Updating Image ...",Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(getActivity(),"Erreur lors de la mise à jour de l'image... ...",Toast.LENGTH_SHORT).show();
                                                Toast.makeText(ProfileActivity.this, "Erreur lors de la mise à jour de l'image... ...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                //error
                                pd.dismiss();
                                //Toast.makeText(ProfileUtilisateurActivity.this,"Some error occured",Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(),"Une erreur s'est produite ",Toast.LENGTH_SHORT).show();
                                Toast.makeText(ProfileActivity.this, "Une erreur s'est produite ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // there were some error(s) , get and show error message, dismiss progress dialog
                    pd.dismiss();
                    //Toast.makeText(ProfileUtilisateurActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }






    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Do your code from onActivityResult
                    //imageViewCover.setImageURI(Uri.parse(currentPhotoPath));


                    //reduceImageSize(currentPhotoPath);


                    if(profileOrCoverPhoto=="image"){
                        imageViewImageProfile.setImageURI(Uri.parse(currentPhotoPath));
                    }else{
                        imageViewCover.setImageURI(Uri.parse(currentPhotoPath));
                    }


                    //uploadProfileCoverPhoto(Uri.parse(currentPhotoPath));
                    //uploadProfileCoverPhoto(Uri.fromFile(new File(currentPhotoPath)),);
                    System.out.println("currentPhotoPath:"+currentPhotoPath);
                    System.out.println("Uri.fromFile(new File(currentPhotoPath)):"+Uri.fromFile(new File(currentPhotoPath)));
                    uploadProfileCoverPhoto(Uri.fromFile(new File(currentPhotoPath)),true);
                }
            });


    private byte[] reduceImageSize(String currentPhotoPath) throws IOException {

        Bitmap bmp = null;
        try {

            //bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(currentPhotoPath));

        } catch (IOException e) {

            //Log.d("PrintIOExeception","*****     "+e.toString());
            System.out.println("PrintIOExeception *****     :"+e.toString());
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();

        return data;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        //if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(getActivity(),
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.seip.day23b1.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mLauncher.launch(takePictureIntent);
            }

            /*image_uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
            if (image_uri != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                mLauncher.launch(takePictureIntent);
            }*/

        }
    }



    @Override
    //protected void onStart(){
    public void onStart(){
        super.onStart();

        if(connectedUser==true) {
            mAuth.addAuthStateListener(stateListener);
        }
    }
    @Override
    //protected void onStop(){
    public void onStop(){
        super.onStop();
        if(connectedUser==true) {
            if (stateListener != null) {
                mAuth.addAuthStateListener(stateListener);
            }
        }
    }


}