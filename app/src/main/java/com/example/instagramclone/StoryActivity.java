package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Model.Story;
import com.example.instagramclone.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener
{

    int counter = 0;
    long pressTime = 0L;
    long limit = 500L;


    StoriesProgressView storiesProgressView;
    ImageView image;
    ImageView storyPhoto;
    TextView storyUsername;

    List<String> images;
    List<String> storyIds;
    String userId;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener()           // swiping up and down on the screen
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;

                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }


            return false;
        }
    };










    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);



        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image_story);
        storyPhoto = findViewById(R.id.story_photo);
        storyUsername = findViewById(R.id.story_username);


        userId = getIntent().getStringExtra("userId");



        getStories(userId);
        userInfo(userId);



        // reverse - tapping on left side of the screen
        View reverse = findViewById(R.id.reverse);

        reverse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                storiesProgressView.reverse();

            }
        });

        reverse.setOnTouchListener(onTouchListener);





        // skip - tapping on the right side of the screen
        View skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                storiesProgressView.skip();

            }
        });

        skip.setOnTouchListener(onTouchListener);










    }


    @Override
    public void onNext()
    {
        Glide.with(getApplicationContext()).load(images.get(++counter)).into(image);

    }

    @Override
    public void onPrev()
    {
        if ((counter - 1) < 0)
        {
            return;
        }

        Glide.with(getApplicationContext()).load(images.get(--counter)).into(image);
    }

    @Override
    public void onComplete()
    {
        finish();

    }


    @Override
    protected void onDestroy()
    {
        storiesProgressView.destroy();

        super.onDestroy();
    }


    @Override
    protected void onPause()
    {
        storiesProgressView.pause();


        super.onPause();
    }


    @Override
    protected void onResume()
    {
        storiesProgressView.resume();
        super.onResume();
    }


    private void getStories(String userId)
    {
        images = new ArrayList<>();
        storyIds = new ArrayList<>();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                images.clear();
                storyIds.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Story story = snapshot.getValue(Story.class);

                    long timeCurrent = System.currentTimeMillis();

                    if (timeCurrent > story.getTimeStart() && timeCurrent < story.getTimeEnd())
                    {
                        images.add(story.getImageURL());
                        storyIds.add(story.getStoryId());


                        System.out.println("image url: " + story.getImageURL());
                        System.out.println("story id: " + story.getStoryId());
                    }
                }


                storiesProgressView.setStoriesCount(images.size());
                storiesProgressView.setStoryDuration(5000L);
                storiesProgressView.setStoriesListener(StoryActivity.this);
                storiesProgressView.startStories(counter);


                Glide.with(getApplicationContext()).load(images.get(counter)).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }





    private void userInfo(String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);


        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);

                Glide.with(getApplicationContext()).load(user.getImageURL()).into(storyPhoto);

                storyUsername.setText(user.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


}
