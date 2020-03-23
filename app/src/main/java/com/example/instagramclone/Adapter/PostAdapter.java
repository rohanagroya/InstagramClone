package com.example.instagramclone.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    public Context mContext;
    public List<Post> mPost;
    private FirebaseUser firebaseUser;



    public PostAdapter(Context mContext, List<Post> mPost)
    {
        this.mContext = mContext;
        this.mPost = mPost;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);

        return new PostAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPost.get(position);


        Glide.with(mContext).load(post.getPostImage()).into(holder.postImage);



        if (post.getDescription().equals(""))
        {
            holder.description.setVisibility(View.GONE);
        }
        else
        {
            holder.description.setVisibility(View.VISIBLE);

            holder.description.setText(post.getDescription());
        }




        publisherInfo(holder.imageProfile, holder.username, holder.publisher, post.getPublisher());


        isLiked(post.getPostId(), holder.like);
        numberOfLikes(holder.likes, post.getPostId());





        holder.like.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (holder.like.getTag().equals("like"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }

            }
        });



    }



    @Override
    public int getItemCount()
    {
        return mPost.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;

        public TextView username;
        public TextView likes;
        public TextView publisher;
        public TextView description;
        public TextView comments;





        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);



            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);



        }
    }




    private void isLiked(String postId, final ImageView imageView)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postId);


        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(firebaseUser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_liked);

                    imageView.setTag("liked");

                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_like);

                    imageView.setTag("like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }





    private void numberOfLikes(final TextView likes, String postId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);


        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                likes.setText(dataSnapshot.getChildrenCount() + " likes");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }












    private void publisherInfo(final ImageView imageProfile, final TextView username, final TextView publisher, String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);


                Glide.with(mContext).load(user.getImageURL()).into(imageProfile);

                username.setText(user.getUserName());

                publisher.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
