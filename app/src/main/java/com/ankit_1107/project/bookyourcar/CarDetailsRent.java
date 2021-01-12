package com.ankit_1107.project.bookyourcar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CarDetailsRent extends AppCompatActivity implements OnMapReadyCallback {
    public static final String[] daysOfWeek =new String[]{"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
    public static final String[] monthsOfYear= new String[]{"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

    ImageView carRentDetailImageView;
    ImageView leftImage,rightImage;
    TextView vehicleNumberTextView;
    TextView numberOfSeatsTextView;
    TextView fuelTextView;
    TextView acTextView;
    TextView dateFromTextView,dateToTextView,dayTextView;
    TextView basePriceTextView,driverPriceTextView,totalPriceTextView;
    CheckBox driverCheckBok;
    Button buttonConfirmBooking;
    TextView carNameTextView;
    TextView averageRatingTextView;
    EditText addReviewEditText;
    RatingBar addRatingBar,averageRatingBar;
    RecyclerView ratingRecyclerView;
    Button buttonAddReview;

    Calendar calFrom,calTo;
    Car itemCar;
    long noOfDays;


    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private GoogleMap mMap;
    private Marker personMarker,carMarker;
    double personLatitude,personLongitude;

    List<Uri> imagesUriList = new ArrayList<Uri>();
    int currImageIndex;

    List<CarReview> reviewList = new ArrayList<CarReview>();
    ReviewCarAdapter reviewAdapter;

    Person person;

    float ratingSum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details_rent);

        carRentDetailImageView = findViewById(R.id.images_view_rent_detail);
        leftImage = findViewById(R.id.left_image_change);
        rightImage = findViewById(R.id.right_image_change);
        vehicleNumberTextView = findViewById(R.id.vehicle_number_rent_detail);
        numberOfSeatsTextView = findViewById(R.id.seat_number_rent_detail);
        fuelTextView = findViewById(R.id.fuel_rent_detail);
        acTextView = findViewById(R.id.ac_rent_detail);
        dateFromTextView = findViewById(R.id.date_from_rent_detail);
        dateToTextView = findViewById(R.id.date_to_rent_detail);
        dayTextView = findViewById(R.id.days_rent_detail);
        basePriceTextView = findViewById(R.id.base_price_rent_detail);
        driverCheckBok = findViewById(R.id.checkbox_driver_rent_detail);
        driverPriceTextView = findViewById(R.id.driver_price_rent_detail);
        totalPriceTextView = findViewById(R.id.total_price_rent_detail);
        buttonConfirmBooking =findViewById(R.id.button_confirm_booking);
        carNameTextView = findViewById(R.id.car_name_rent_detail);
        averageRatingBar = findViewById(R.id.car_rating_rent_detail);
        averageRatingTextView = findViewById(R.id.average_rating_text_view);
        addReviewEditText = findViewById(R.id.add_review_rent_detail);
        addRatingBar = findViewById(R.id.add_rating_rent_detail);
        ratingRecyclerView = findViewById(R.id.recycler_view_review_rent_detail);
        buttonAddReview = findViewById(R.id.button_send_review);


        driverCheckBok.setChecked(false);
        driverPriceTextView.setText("0");

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        initialize();
        getImageUriListFromStorage();
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightImageButtonClicked();
            }
        });
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftImageButtonClicked();
            }
        });

        //Review RecyclerView
        reviewAdapter = new ReviewCarAdapter(this,reviewList);
        ratingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        ratingRecyclerView.setAdapter(reviewAdapter);

        setDatabaseListener();

        buttonAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });
        firebaseFirestore.collection("users").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        person = documentSnapshot.toObject(Person.class);
                        buttonConfirmBooking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //validate again and continue booking else return to MainActivity
                                validateAgainCarAvailability();
                            }
                        });
                    }
                });


    }

    void initialize(){
        Intent intent = getIntent();
        calFrom = (Calendar) intent.getSerializableExtra("calFrom");
        calTo = (Calendar) intent.getSerializableExtra("calTo");
        itemCar = (Car) intent.getSerializableExtra("itemCar");
        personLatitude = intent.getDoubleExtra("personLatitude",0);
        personLongitude  = intent.getDoubleExtra("personLongitude",0);

        carNameTextView.setText(itemCar.getBrandName()+" "+itemCar.getModelName());
        vehicleNumberTextView.setText(itemCar.getVehicleNumber());
        numberOfSeatsTextView.setText(String.valueOf(itemCar.getSeatCount()));
        fuelTextView.setText(itemCar.getFuelType());
        if(itemCar.getCheckAC()){
            acTextView.setText("AC");
        }
        else{
            acTextView.setText("NON-AC");
        }
        basePriceTextView.setText(String.valueOf(itemCar.getPrice()));

        dateFromTextView.setText(convertCalenderToString(calFrom));
        dateToTextView.setText(convertCalenderToString(calTo));

        noOfDays=  DaysCountUtil.daysBetweenDates(calTo.get(Calendar.YEAR),calTo.get(Calendar.MONTH),calTo.get(Calendar.DAY_OF_MONTH),
                calFrom.get(Calendar.YEAR),calFrom.get(Calendar.MONTH),calFrom.get(Calendar.DAY_OF_MONTH));
        dayTextView.setText(String.valueOf(noOfDays));

        //Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_rent_detail);
        mapFragment.getMapAsync(this);

        //checkbox
        long totalPrice = Integer.parseInt(basePriceTextView.getText().toString())*(noOfDays)
                +Integer.parseInt(driverPriceTextView.getText().toString());

        totalPriceTextView.setText(String.valueOf(totalPrice));

        driverCheckBok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    driverPriceTextView.setText("100");
                }
                else{
                    driverPriceTextView.setText("0");
                }

                long totalPrice = Integer.parseInt(basePriceTextView.getText().toString())*(noOfDays)
                        +Integer.parseInt(driverPriceTextView.getText().toString());

                totalPriceTextView.setText(String.valueOf(totalPrice));
            }
        });



    }

    void setDatabaseListener(){
        databaseReference.child("carReviews").child(itemCar.getCarId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CarReview addedReview = snapshot.getValue(CarReview.class);
                reviewList.add(addedReview);
                reviewAdapter.notifyDataSetChanged();
                ratingSum+=addedReview.getRating();
                float avgRatingCurr = (float) (Math.round(10* ratingSum/(float)reviewList.size())/10.0);
                averageRatingTextView.setText(String.valueOf(avgRatingCurr));
                averageRatingBar.setRating(avgRatingCurr);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Ranchi and move the camera
        LatLng personLatLng = new LatLng(personLatitude,personLongitude);
        personMarker = mMap.addMarker(new MarkerOptions()
                .position(personLatLng)
                .title("You are here"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(personLatLng,14));
        addPersonMarkerInMap(personMarker);

        carMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(itemCar.getLocationLatitude(),itemCar.getLocationLongitude()))
                .title(itemCar.getBrandName()+"\n"+itemCar.getModelName()));
        addCarMarkerInMap(carMarker);

    }

    public void addPersonMarkerInMap(Marker marker){
        BitmapDrawable bd= (BitmapDrawable) getResources().getDrawable(R.drawable.map_person_icon);
        Bitmap b=bd.getBitmap();
        Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/5,b.getHeight()/5, false);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bhalfsize));
    }
    public void addCarMarkerInMap(Marker marker){
        BitmapDrawable bd= (BitmapDrawable) getResources().getDrawable(R.drawable.map_car_icon);
        Bitmap b=bd.getBitmap();
        Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/5,b.getHeight()/5, false);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bhalfsize));
    }
    private String convertCalenderToString(Calendar calendar){
        String result = String.valueOf(calendar.get(calendar.DAY_OF_MONTH)) +" "+monthsOfYear[calendar.get(calendar.MONTH)]+" "+ String.valueOf(calendar.get(calendar.YEAR));
        return result;
    }
    private void getImageUriListFromStorage(){
        StorageReference ref = storageReference.child("cars").child(itemCar.getVehicleNumber());
        ref.child("mainImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imagesUriList.add(uri);
                Picasso.get().load(uri).into(carRentDetailImageView);
            }
        });
        currImageIndex = 0;

        ref = ref.child("additionalImages");
        ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference item : listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagesUriList.add(uri);
                        }
                    });
                }
            }
        });

    }
    private void leftImageButtonClicked(){
        currImageIndex =(currImageIndex+imagesUriList.size()-1)%imagesUriList.size();
        Picasso.get().load(imagesUriList.get(currImageIndex)).into(carRentDetailImageView);
    }
    private void rightImageButtonClicked(){
        currImageIndex =(currImageIndex+imagesUriList.size()+1)%imagesUriList.size();
        Picasso.get().load(imagesUriList.get(currImageIndex)).into(carRentDetailImageView);
    }

    void addReview(){
        String review = addReviewEditText.getText().toString().trim();
        float rating = addRatingBar.getRating();
        if(review.equals("")) {
            Toast.makeText(this,"Please write some review",Toast.LENGTH_SHORT).show();
            return;
        }
        String date,month,year;
        Calendar currCalendar = Calendar.getInstance();
        date=String.valueOf(currCalendar.get(Calendar.DAY_OF_MONTH));
        month=String.valueOf(monthsOfYear[currCalendar.get(Calendar.MONTH)]);
        year=String.valueOf(currCalendar.get(Calendar.YEAR));
        CarReview itemReview = new CarReview(null,firebaseUser.getUid(),itemCar.getCarId(),rating,date,month,year,review);
        DatabaseReference ref = databaseReference.child("carReviews").child(itemCar.getCarId());
        DatabaseReference reqRef = ref.push();
        itemReview.setReviewId(reqRef.getKey());
        reqRef.setValue(itemReview);
        addReviewEditText.getText().clear();
    }

    public Long findNoDays(Calendar cal){
        Long res = Long.valueOf(DaysCountUtil.daysBetweenDates(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH) ,1971,1,1));
        return res;
    }

    private void validateAgainCarAvailability(){
        firebaseFirestore.collection("cars").document(itemCar.getCarId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                itemCar = documentSnapshot.toObject(Car.class);
                Long noDateFrom = findNoDays(calFrom),noDateTo = findNoDays(calTo);
                int flag=0;
                for(Long bookedNo : itemCar.getBookedNoDate()){
                    if(bookedNo>=noDateFrom && bookedNo<=noDateTo){
                        flag=1;
                        break;
                    }
                }

                if(flag == 1){
                    //unavailable
                    Toast.makeText(CarDetailsRent.this,"Car is booked by someone else...\nPlease try again",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    //available
                    completeBookingAfterValidation();
                }
            }
        });

    }

    private void completeBookingAfterValidation(){

        final Booking booking =  new Booking(null,person.getUserId(),itemCar.getCarId(),
                dateFromTextView.getText().toString(),dateToTextView.getText().toString(),
                findNoDays(calFrom),findNoDays(calTo),
                Integer.parseInt(totalPriceTextView.getText().toString()),driverCheckBok.isChecked(),
                0,person.getPhoneNumber(),person.getName(),itemCar.getVehicleNumber(),
                "9109100000",person.getMainPhotoUri(),itemCar.getLendCarUri(),
                itemCar.getBrandName()+" "+itemCar.getModelName(),itemCar.getOwnerId());

        final DocumentReference docRef = firebaseFirestore.collection("bookings").document();
        docRef.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CarDetailsRent.this,"Booking Request Sent",Toast.LENGTH_SHORT).show();
                booking.setBookingId(docRef.getId());
                docRef.update("bookingId",docRef.getId());
                Task task1 = firebaseFirestore.collection("users").document(person.getUserId())
                        .update("bookingHistoryPerson", FieldValue.arrayUnion(booking.getBookingId()));
                Task task2 = firebaseFirestore.collection("cars").document(itemCar.getCarId())
                        .update("bookingHistoryCar",FieldValue.arrayUnion(booking.getBookingId()));
                Task allTasks = Tasks.whenAllSuccess(task1,task2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        List<Long> bookedNoDate = itemCar.getBookedNoDate();
                        for(Long i = booking.getNoDateFrom() ;i<= booking.getNoDateTo(); ++i){
                            bookedNoDate.add(i);
                        }
                        //Notification for Car owner
                        RequestQueue requestQueue = Volley.newRequestQueue(CarDetailsRent.this);
                        String bodyNotification = "New booking request for"+itemCar.getBrandName()+" "+itemCar.getModelName();
                        FCMUtil.sendNotification(requestQueue,itemCar.getOwnerId(),bodyNotification);

                        firebaseFirestore.collection("cars").document(itemCar.getCarId())
                                .update("bookedNoDate",bookedNoDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                    }
                                });
                    }
                });

            }
        });
    }

}