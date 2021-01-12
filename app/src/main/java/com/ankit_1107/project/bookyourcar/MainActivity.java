package com.ankit_1107.project.bookyourcar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener , DatePickerDialog.OnDateSetListener {
    private static final String TAG ="Main Activity";
    public static final String[] daysOfWeek =new String[]{"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
    public static final String[] monthsOfYear= new String[]{"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    public static final int CAR_LEND_ACTIVITY = 120;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener authStateListener;

    private GoogleMap mMap;
    public Marker currentMarker;

    private List<City> cities;
    private SpinnerAdapter spinnerAdapter;
    Spinner spinner;

    Button buttonFindCar;
    CardView pickerFrom,pickerTo;
    Calendar calFrom,calTo;
    int whichPicker=-1; //0== FROM 1==TO

    TextView dayFrom,dayTo;
    TextView dateFrom,dateTo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //for navigation drawer
    SNavigationDrawer sNavigationDrawer;
    int color1=0;
    public static Fragment fragment;
    LendCarFragment lendCarFragment;
    RentCarFragment rentCarFragment;
    RentBookingFragment rentBookingFragment;
    ProfileFragment profileFragment;
    FrameLayout frameLayout;

    //mainRecyclerView
    RecyclerView mainRecyclerView;
    List<Car> dataList;
    CarListAdapter carListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mainRecyclerView SetUp
        mainRecyclerView = findViewById(R.id.recyclerViewMainActivity);
        dataList = new ArrayList<Car>();
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        carListAdapter = new CarListAdapter(this,dataList);
        mainRecyclerView.setAdapter(carListAdapter);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(MainActivity.this,SigninActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    FirebaseMessaging.getInstance().subscribeToTopic(firebaseAuth.getUid());
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);

        //Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Calendar
        calFrom = Calendar.getInstance();
        calTo = Calendar.getInstance();
        calFrom.set(calFrom.get(Calendar.YEAR) , calFrom.get(Calendar.MONTH) , calFrom.get(Calendar.DAY_OF_MONTH) , 0 ,0,0);
        calTo.set(calTo.get(Calendar.YEAR) , calTo.get(Calendar.MONTH) , calTo.get(Calendar.DAY_OF_MONTH) , 0 ,0,0);

        dayFrom = findViewById(R.id.day_from);
        dayTo = findViewById(R.id.day_to);
        dateFrom = findViewById(R.id.date_from);
        dateTo = findViewById(R.id.date_to);
        setDateInitially();
        //Date picker
        pickerFrom = findViewById(R.id.date_picker_from);
        pickerTo = findViewById(R.id.date_picker_to);
        pickerTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichPicker=1;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker to");
            }
        });
        pickerFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichPicker=0;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker from");
            }
        });


        //Cities Spinner
        cities = new ArrayList<City>();
        init();
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                emptyRecyclerView();
                City currentCity = (City) parent.getAdapter().getItem(position);
                moveMarker(currentCity.latitide , currentCity.longitude);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        {
            //FOR NAVIGATION DRAWER
            frameLayout = findViewById(R.id.frameLayout);
            sNavigationDrawer = findViewById(R.id.navigationDrawer);

            //ADDING MENU
            final List<MenuItem> menuItems = new ArrayList<>();

            menuItems.add(new MenuItem("Rent Car", R.mipmap.ic_launcher));
            menuItems.add(new MenuItem("Booking History", R.mipmap.ic_launcher));
            menuItems.add(new MenuItem("Lend Car", R.mipmap.ic_launcher));
            menuItems.add(new MenuItem("Profile", R.mipmap.ic_launcher));
//            menuItems.add(new MenuItem("Sign In", R.mipmap.ic_launcher));
//            menuItems.add(new MenuItem("Sign Out", R.mipmap.ic_launcher));

            sNavigationDrawer.setMenuItemList(menuItems);

            lendCarFragment = new LendCarFragment();
            rentCarFragment = new RentCarFragment();
            rentBookingFragment = new RentBookingFragment();
            profileFragment = new ProfileFragment();

            sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
                @Override
                public void onMenuItemClicked(int position) {
                    switch (position){
                        case 0:{
                            fragment = rentCarFragment;
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                    .replace(R.id.frameLayout,fragment).commit();
                            break;
                        }
                        case 1:{
                            fragment = rentBookingFragment;
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                    .replace(R.id.frameLayout,fragment).commit();
                            break;
                        }
                        case 2:{
                            fragment = lendCarFragment;
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                    .replace(R.id.frameLayout,fragment).commit();
                            break;
                        }
                        case 3:{
                            fragment = profileFragment;
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                    .replace(R.id.frameLayout,fragment).commit();
                            break;
                        }
                    }
                }
            });

        }

        buttonFindCar = findViewById(R.id.find_car);
        buttonFindCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findCarFunction();

            }
        });

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
                            spinnerAdapter = new SpinnerAdapter(MainActivity.this,cities);
                            //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerAdapter);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Ranchi and move the camera
        LatLng ranchi = new LatLng(23.3441, 85.3096);
        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(ranchi)
                .title("Marker in Ranchi"));
        addPersonMarkerInMap(currentMarker);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ranchi,14));
        mMap.setOnMapLongClickListener(MainActivity.this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        currentMarker.remove();
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        addPersonMarkerInMap(currentMarker);
    }

    public void moveMarker(double latitude,double longitude){
        currentMarker.remove();
        currentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude))
        .title("You are here"));
        addPersonMarkerInMap(currentMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),14));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        emptyRecyclerView();

        Calendar c = Calendar.getInstance();
        c.set(year,month,dayOfMonth,0,0,0);

        String formattedDate =String.valueOf(dayOfMonth)+" "+monthsOfYear[month]+" "+String.valueOf(year);
        if(whichPicker ==0){
            //FROM
            calFrom = c;
            dayFrom.setText(daysOfWeek[c.get(Calendar.DAY_OF_WEEK)-1]);
            dateFrom.setText(formattedDate);
        }
        else{
            calTo = c;
            dayTo.setText(daysOfWeek[c.get(Calendar.DAY_OF_WEEK)-1]);
            dateTo.setText(formattedDate);
        }

    }
    private void setDateInitially(){
        String formattedDate = String.valueOf(calFrom.get(Calendar.DAY_OF_MONTH))+" "+monthsOfYear[calFrom.get(Calendar.MONTH)]+" "+String.valueOf(calFrom.get(Calendar.YEAR));
        dateFrom.setText(formattedDate);
        dateTo.setText(formattedDate);
        dayFrom.setText(daysOfWeek[calFrom.get(Calendar.DAY_OF_WEEK)-1]);
        dayTo.setText(daysOfWeek[calFrom.get(Calendar.DAY_OF_WEEK)-1]);
    }

    public void findCarFunction(){
        dataList.clear();
        carListAdapter.notifyDataSetChanged();
        City city= spinnerAdapter.getItem(spinner.getSelectedItemPosition());
        String selectedCity = city.getName();
        carListAdapter.setLatlng(city.getLatitide(),city.getLongitude());
        carListAdapter.setCal(calFrom,calTo);

        db.collection("cars")
                .whereEqualTo("city",selectedCity)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    mMap.clear();
                    currentMarker = mMap.addMarker(new MarkerOptions().position(currentMarker.getPosition())
                    .title("You are here").draggable(true));
                    addPersonMarkerInMap(currentMarker);
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Car itemCar = documentSnapshot.toObject(Car.class);

                        //Validate car if it is in range of given calender dates
                        Long noDateFrom = findNoDays(calFrom),noDateTo = findNoDays(calTo);
                        int flag=0;
                        for(Long bookedNo : itemCar.getBookedNoDate()){
                            if(bookedNo>=noDateFrom && bookedNo<=noDateTo){
                                flag=1;
                                break;
                            }
                        }

                        if(flag == 1){
                            continue;
                        }

                        dataList.add(itemCar);
                        carListAdapter.notifyDataSetChanged();
                        Marker itemMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(itemCar.getLocationLatitude(),itemCar.getLocationLongitude()))
                        .title(itemCar.getBrandName()+"\n"+itemCar.getModelName()));
                        addCarMarkerInMap(itemMarker);
                    }
                }
            }
        });
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

    public Long findNoDays(Calendar cal){
        Long res = Long.valueOf(DaysCountUtil.daysBetweenDates(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH) ,1971,1,1));
        return res;
    }

    private void emptyRecyclerView(){
        dataList.clear();
        carListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CarListAdapter.CAR_RENT_DETAIL_REQUEST){
            findCarFunction();
        }
    }

}


//    void init(){
//        cities.add(new City("ranchi",new LatLng(23.3441, 85.3096) ));
//        cities.add(new City("patratu",new LatLng(23.6329, 85.3033) ));
//        cities.add(new City("patna",new LatLng(25.612677, 85.158875) ));
//        cities.add(new City("dhanbad",new LatLng(23.8143, 86.4412) ));
//        cities.add(new City("delhi",new LatLng(28.7041, 77.1025) ));
//        cities.add(new City("mumbai",new LatLng(19.0760, 72.8777) ));
//        cities.add(new City("kolkata",new LatLng(22.5726, 88.3639) ));
//        cities.add(new City("chennai",new LatLng(13.0827, 80.2707) ));
//        cities.add(new City("new york",new LatLng(40.7128, 74.0060) ));
//        cities.add(new City("london",new LatLng(51.5074, 0.1278) ));
//        cities.add(new City("silicon valley",new LatLng(37.3875, 122.0575) ));
//        cities.add(new City("arrah",new LatLng(25.5541, 84.6665) ));
//
//    }


//    //Firebase Push Notification P2P
//    private RequestQueue requestQueue;
//    private String URL = "https://fcm.googleapis.com/fcm/send";
//
//    void initFirebasePushNotification(){
//        requestQueue = Volley.newRequestQueue(this);
//        FirebaseMessaging.getInstance().subscribeToTopic("news");
//    }
//
//    void sendNotification(){
//        JSONObject mainObj = new JSONObject();
//        try {
//            mainObj.put("to","/topics/"+"news");
//            JSONObject notificationObj = new JSONObject();
//            notificationObj.put("title","any title");
//            notificationObj.put("body","any body");
//            mainObj.put("notification",notificationObj);
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
//                    mainObj,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    }){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String,String> header = new HashMap<>();
//                    header.put("content-type","application/json");
//                    header.put("authorization","key=AAAAgWBE1xw:APA91bGgOzwnhZ4VGlvTChuI2Q5y8MB6BdzOM_lQQK-vTab_AFOEIKXdrxUjFtAgZM2Hmcd_d9ygf7cX6XHAXch1eMX36s7svFZ8vvmWvLPlVgXReyIOdgsc1KcBN7ptyS-sOQ4EJKAN");
//
//                    return header;
//                }
//            };
//
//            requestQueue.add(request);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }