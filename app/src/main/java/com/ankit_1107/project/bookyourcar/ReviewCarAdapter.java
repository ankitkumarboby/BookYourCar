package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReviewCarAdapter extends RecyclerView.Adapter<ReviewCarAdapter.ViewHolder> {
    Context context;
    List<CarReview> dataList;
    LayoutInflater layoutInflater;
    Person currPerson;
    Car currCar;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public ReviewCarAdapter(Context context , List<CarReview> dataList){
        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ReviewCarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.review_car_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewCarAdapter.ViewHolder holder, int position) {
        CarReview currReview = dataList.get(position);
        holder.textReviewCarReviewItem.setText(currReview.getReview());
        holder.ratingCarReviewItem.setRating(currReview.getRating());
        holder.dateCarReviewItem.setText(currReview.getDate()+" "+currReview.getMonth()+" "+currReview.getYear());

        firebaseFirestore.collection("users").document(currReview.getUserId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currPerson = documentSnapshot.toObject(Person.class);
                        holder.textNameCarReviewItem.setText(currPerson.getName());
                    }
                });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCarReviewItem;
        TextView textNameCarReviewItem,textReviewCarReviewItem,dateCarReviewItem;
        RatingBar ratingCarReviewItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCarReviewItem = itemView.findViewById(R.id.person_image_review_item);
            textNameCarReviewItem = itemView.findViewById(R.id.person_name_review_item);
            textReviewCarReviewItem = itemView.findViewById(R.id.review_text_review_item);
            dateCarReviewItem = itemView.findViewById(R.id.date_review_item);
            ratingCarReviewItem = itemView.findViewById(R.id.rating_bar_review_item);
        }
    }
}
