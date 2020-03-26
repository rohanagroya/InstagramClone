package com.example.instagramclone.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment
{

    ImageView imageProfile;
    ImageView options;
    TextView posts;
    TextView followers;
    TextView following;
    TextView fullName;
    TextView bio;
    TextView username;
    Button editProfile;

    FirebaseUser firebaseUser;
    String profileId;

    ImageButton myPhotos;
    ImageButton savedPhotos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        // Shared Preference object points to a file containing key value pairs and provides simple methods to read and write them.
        // can be though of as a dictionary or key/value pair.


        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        //profileId = prefs.getString("profileId", "none");
        profileId = prefs.getString("profileId", FirebaseAuth.getInstance().getUid());




        imageProfile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullName = view.findViewById(R.id.fullName);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        editProfile = view.findViewById(R.id.edit_profile);
        myPhotos = view.findViewById(R.id.my_photos);
        savedPhotos = view.findViewById(R.id.saved_photos);


        userInfo();
        getFollowers();
        getNumberOfPosts();

        if (profileId.equals(firebaseUser.getUid()))
        {
            editProfile.setText("Edit Profile");
        }
        else
        {
            checkFollow();
            savedPhotos.setVisibility(View.GONE);
        }




        // for when the user clicks " Edit Profile" on the profile screen
        editProfile.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String button = editProfile.getText().toString();


                if (button.equals("Edit Profile"))
                {
                    // go to edit profile
                }
                else if (button.equals("follow"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").child(profileId).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Followers").child(firebaseUser.getUid()).setValue(true);
                }
                else if (button.equals("following"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").child(profileId).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Followers").child(firebaseUser.getUid()).removeValue();

                }

            }
        });

        return view;
    }








    private void userInfo()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileId);

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (getContext() == null)
                {
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageURL()).into(imageProfile);


                username.setText(user.getUserName());
                fullName.setText(user.getFullName());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }




    private void checkFollow()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");



        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(profileId).exists())
                {
                    editProfile.setText("following");
                }
                else
                {
                    editProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }






    private void getFollowers()
    {
        // get the number of followers
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers");

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });




        // get the number users the current user is following
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("following");

        reference1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }





    private void getNumberOfPosts()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                int i = 0;

                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId))
                    {
                        i++;
                    }
                }

                posts.setText("" + i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


}
