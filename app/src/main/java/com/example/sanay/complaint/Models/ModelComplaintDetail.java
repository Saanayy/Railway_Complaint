package com.example.sanay.complaint.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ModelComplaintDetail {
    String category,subcategory,imagedownloadurl,desc;
    Date timestamp;
    Boolean status;

    public ModelComplaintDetail() {
    }

    public ModelComplaintDetail(String category, String subcategory, String imagedownloadurl, String desc, Boolean status) {
        this.category = category;
        this.subcategory = subcategory;
        this.imagedownloadurl = imagedownloadurl;
        this.desc = desc;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getImagedownloadurl() {
        return imagedownloadurl;
    }

    public void setImagedownloadurl(String imagedownloadurl) {
        this.imagedownloadurl = imagedownloadurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
