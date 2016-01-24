package com.mycompany.managedbeans;

import javax.servlet.http.Part;

public class ImageUploadeBean {
    
    private Part image;

    public Part getImage() {
        return image;
    }
    public void setImage(Part image) {
        this.image = image;
    }
}
