package com.ankit_1107.project.bookyourcar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.io.Files.getFileExtension;

public class LendCarActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private static int SELECT_MAIN_PICTURE_FROM_GALLERY = 200;
    private static int SELECT_ADDITIONAL_PICTURE_FROM_GALLERY = 201;

    private Button buttonMainCarImage;
    private ImageView imageMainCar;
    private RecyclerView additionalCarImagesRecyclerView;
    private Button buttonAdditionalCarImages;
    private EditText brandNameLendCar;
    private EditText modelNameLendCar;
    private EditText vehicleNumberLendCar;
    private EditText priceLendCar;
    private Spinner spinnerEngineType;
    private Spinner spinnerNumberOfSeats;
    private Spinner spinnerCitiesLendCar;
    private Button button_lend_your_car;
    private Switch switchAC;

    private GoogleMap mMap;
    private Marker currentMarker;

    ArrayList<Uri> additionalImageUriList;
    AdditionalmageAdapter additionalmageAdapter;

    private List<City> cities;
    private SpinnerAdapter spinnerAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri mainCarImageUri =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_car);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

         buttonMainCarImage = findViewById(R.id.button_main_car_img_lend);
         imageMainCar = findViewById(R.id.main_car_img_lend);
         additionalCarImagesRecyclerView = findViewById(R.id.additional_car_images_recycler_view);
         buttonAdditionalCarImages = findViewById(R.id.button_additional_car_images);
         brandNameLendCar = findViewById(R.id.brand_name_lend_car);
         modelNameLendCar = findViewById(R.id.model_name_lend_car);
         vehicleNumberLendCar = findViewById(R.id.vehicle_number_lend_car);
         priceLendCar = findViewById(R.id.price_lend_car);
         spinnerEngineType = findViewById(R.id.spinner_engine_type);
         spinnerNumberOfSeats = findViewById(R.id.spinner_no_of_seats);
         spinnerCitiesLendCar = findViewById(R.id.spinner_city_lend_car);
         button_lend_your_car = findViewById(R.id.button_lend_your_car);
         switchAC = findViewById(R.id.switch_ac);

        //Main Image part
        buttonMainCarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,SELECT_MAIN_PICTURE_FROM_GALLERY);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_lend_car);
        mapFragment.getMapAsync(this);

        //Additional Image Part
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        additionalCarImagesRecyclerView.setLayoutManager(linearLayoutManager);
        additionalImageUriList = new ArrayList<Uri>();
        additionalmageAdapter = new AdditionalmageAdapter(this,additionalImageUriList);
        additionalCarImagesRecyclerView.setAdapter(additionalmageAdapter);
        buttonAdditionalCarImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,SELECT_ADDITIONAL_PICTURE_FROM_GALLERY);
            }
        });

        //Cities Spinner
        cities = new ArrayList<City>();
        init();
        spinnerCitiesLendCar = findViewById(R.id.spinner_city_lend_car);
        spinnerCitiesLendCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City currentCity = (City) parent.getAdapter().getItem(position);
                moveMarker(currentCity.latitide , currentCity.longitude);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,new
                ArrayList<String>(Arrays.asList("petrol","Diesel")));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEngineType.setAdapter(adapter);
        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                new ArrayList<String>(Arrays.asList("3","4","5","6","7","8","9","10","11","12")));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumberOfSeats.setAdapter(adapter);

        //LendYourCarButton pressed
        button_lend_your_car = findViewById(R.id.button_lend_your_car);
        button_lend_your_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataWhenLendYourCarPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_MAIN_PICTURE_FROM_GALLERY){
            if(resultCode == Activity.RESULT_OK && data!=null){
                mainCarImageUri = data.getData();
                Picasso.get().load(mainCarImageUri).into(imageMainCar);
            }
        }
        else if(requestCode == SELECT_ADDITIONAL_PICTURE_FROM_GALLERY){
            if(resultCode == Activity.RESULT_OK && data!=null){
                Uri imageUri = data.getData();
                additionalImageUriList.add(imageUri);
                additionalmageAdapter.notifyDataSetChanged();
            }
        }
    }

    void init(){
        db.collection("cities")
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                cities.add(documentSnapshot.toObject(City.class));
                            }
                            spinnerAdapter = new SpinnerAdapter(LendCarActivity.this,cities);
                            //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCitiesLendCar.setAdapter(spinnerAdapter);
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Ranchi and move the camera
        LatLng ranchi = new LatLng(23.3441, 85.3096);
        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(ranchi)
                .title("Marker in Ranchi").draggable(true));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ranchi,14));
        mMap.setOnMapLongClickListener(LendCarActivity.this);
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        currentMarker.remove();
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("You are here").draggable(true));
    }

    public void moveMarker(double latitude,double longitude){
        currentMarker.remove();
        currentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude))
                .title("You are here").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),14));
    }

    String tempString =null;
    void sendDataWhenLendYourCarPressed(){
        String brandName = brandNameLendCar.getText().toString();
        String modelName = modelNameLendCar.getText().toString();
        final String vehicleNumber = vehicleNumberLendCar.getText().toString();
        String engineType = spinnerEngineType.getSelectedItem().toString();
        int seatCount = Integer.parseInt(spinnerNumberOfSeats.getSelectedItem().toString());
        Double locationLatitude = currentMarker.getPosition().latitude;
        Double locationLongitude = currentMarker.getPosition().longitude;
        City selectedCity = (City) spinnerCitiesLendCar.getSelectedItem();
        String city = selectedCity.getName();
        String ownerId = firebaseUser.getUid();
        int price = Integer.parseInt(priceLendCar.getText().toString());
        Boolean checkAC = switchAC.isChecked();

        //must check for validation
        //**************CHECK ****************//



        StorageReference additionalImagesRef = storageReference.child("cars").child(vehicleNumber).child("additionalImages");
        for(Uri uri : additionalImageUriList){
            StorageReference ref = additionalImagesRef.child(System.currentTimeMillis()+getFileExtension(uri.toString()));
            ref.putFile(uri);

        }


        final Car carToBeLended = new Car(null,ownerId,brandName,modelName,vehicleNumber,engineType,seatCount,city,locationLatitude,locationLongitude,
                new ArrayList<String>(),price,checkAC,true,null,"9999000999",new ArrayList<Long>());

       final DocumentReference documentReference =db.collection("cars").document();
        documentReference.set(carToBeLended)
                .addOnSuccessListener(this,new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        documentReference.update("carId",documentReference.getId());
                        db.collection("users").document(carToBeLended.getOwnerId())
                                .update("carsOwnedByPerson", FieldValue.arrayUnion(documentReference.getId()));

                        //uploading images of car to Firebase storage
                        final StorageReference mainImageRef = storageReference.child("cars").child(vehicleNumber).child("mainImage");//+getFileExtension(mainCarImageUri.toString())
                        mainImageRef.putFile(mainCarImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mainImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUriString = uri.toString();
                                        documentReference.update("lendCarUri",downloadUriString);
                                    }
                                });
                            }
                        });
                        Intent i =new Intent();
                        i.putExtra("LendCarResult",1);
                        setResult(RESULT_OK,i);
                        finish();

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LendCarActivity.this,"Failed to Upload Data \n Try again...",Toast.LENGTH_SHORT).show();
            }
        });


    }

}