package com.example.aburgess11.foodmood;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aburgess11.foodmood.models.Match;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.Collections;

import static com.example.aburgess11.foodmood.MatchesListActivity.appBarLayout;
import static com.example.aburgess11.foodmood.MatchesListActivity.matches;
import static com.example.aburgess11.foodmood.MatchesListActivity.swipeCount;

/**
 * Created by aburgess11 on 7/12/17.
 */


@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Profile mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getTimeofday());
        locationNameTxt.setText(mProfile.getLocation());
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn(){
        popUpList();
       // Toast.makeText(mContext, this.mProfile.getLocation(), Toast.LENGTH_SHORT ).show();
        findRestAndIncr(this.mProfile.getLocation());
        Collections.sort(matches);
        MatchesListActivity.adapter.notifyDataSetChanged();
        Log.d("EVENT", "onSwipedIn" + swipeCount);
    }


    public void findRestAndIncr(String name){

        for(Match m: matches){
            if ( m.getName().equals(name)){
                m.rank++;
                Toast.makeText(mContext,  m.rank + "" , Toast.LENGTH_SHORT ).show();

            }
        }

    }

    //Pops up the matches every
    public void popUpList(){
        // after every swipe, increment the swipeCount
        swipeCount++;

        // after 10 swipes, automatically pop up the matches page
        if (swipeCount == 10){

            MatchesListActivity.isAppBarExpanded = true;
            appBarLayout.setExpanded(false);
            appBarLayout.setFitsSystemWindows(true);

            Log.d("EVENT", swipeCount + "" );
            swipeCount=0;
        }
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}

