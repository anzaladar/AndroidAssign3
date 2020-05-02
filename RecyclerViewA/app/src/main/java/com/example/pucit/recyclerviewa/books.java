package com.example.pucit.recyclerviewa;

import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;

public  class books {
    private String title;
    private String url;
    private String level;
    private String info;
    private String cover;

    public  books()
    {

    }
    public books(String title,String level,String info, String  cover,String url)
    {
        this.title=title;
        this.url=url;
        this.level=level;
        this.info=info;
        this.cover=cover;

    }
    public String getTitle()
    {
        return title;
    }
    /*public  String getAuthor()
    {
        return author;
    }

    public  String getAuthorUrl()
    {
        return authorUrl;
    }*/
    public  String getLevel()
    {
        return level;
    }
    public  String getInfo()
    {
        return info;
    }
    public String getCover()
    {
        return cover;
    }
    public String getUrl()
    {
        return url;
    }





}
