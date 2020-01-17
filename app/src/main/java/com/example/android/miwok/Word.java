package com.example.android.miwok;

public class Word {

   private String mdefaulttranslation;
   private String mmiwoktranslation;
   private int mimageresourceid = NO_IMAGE_PROVIDED;
   private  static final int NO_IMAGE_PROVIDED= -1;
   private int maudioresourceid = NO_IMAGE_PROVIDED;

    public Word(String mdefaulttranslation,String mmiwoktranslation){
        this.mdefaulttranslation=mdefaulttranslation;
        this.mmiwoktranslation=mmiwoktranslation;
    }
   public Word(String mdefaulttranslation,String mmiwoktranslation, int maudioresourceid){
       this.mdefaulttranslation=mdefaulttranslation;
       this.mmiwoktranslation=mmiwoktranslation;
       this.maudioresourceid=maudioresourceid;
   }

    public Word(String mdefaulttranslation,String mmiwoktranslation, int mimageresourceid, int maudioresourceid){
        this.mdefaulttranslation=mdefaulttranslation;
        this.mmiwoktranslation=mmiwoktranslation;
        this.mimageresourceid=mimageresourceid;
        this.maudioresourceid= maudioresourceid;
    }



    public String getMdefaulttranslation() {
        return mdefaulttranslation;
    }

    public String getMmiwoktranslation() {
        return mmiwoktranslation;
    }

    public int getMimageresourceid(){
       return mimageresourceid;
    }

    public  int getMaudioresourceid(){ return  maudioresourceid;}

    // to return if the image is provided to display or not
    public boolean hasImage()
    {
        return  mimageresourceid != NO_IMAGE_PROVIDED ;
    }





}
