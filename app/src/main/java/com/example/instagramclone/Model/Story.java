package com.example.instagramclone.Model;

public class Story
{

    private String imageUrl;
    private long timeStart;
    private long timeEnd;
    private String storyId;
    private String userId;

    public Story(String imageUrl, long timeStart, long timeEnd, String storyId, String userId)
    {
        this.imageUrl = imageUrl;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.storyId = storyId;
        this.userId = userId;
    }

    public Story()
    {

    }


    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setTimeStart(long timeStart)
    {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(long timeEnd)
    {
        this.timeEnd = timeEnd;
    }

    public void setStoryId(String storyId)
    {
        this.storyId = storyId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public long getTimeStart()
    {
        return timeStart;
    }

    public long getTimeEnd()
    {
        return timeEnd;
    }

    public String getStoryId()
    {
        return storyId;
    }

    public String getUserId()
    {
        return userId;
    }
}
