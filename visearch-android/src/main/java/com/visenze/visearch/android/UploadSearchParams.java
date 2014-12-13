package com.visenze.visearch.android;


import com.visenze.visearch.android.util.Image;

import java.util.Map;

/**
 * Set upload search parameters
 * Upload search can be initialised with an image attached or an image url.
 *
 */
public class UploadSearchParams extends SearchParams {

    private Image image;

    private String imageUrl;

    public UploadSearchParams() {
        super();
    }

    /**
     * Construct with an image url.
     * @param imageUrl image url.
     */
    public UploadSearchParams(String imageUrl) {
        super();
        this.imageUrl = imageUrl;
    }

    /**
     * Construct with an {@link com.visenze.visearch.android.util.Image Image}
     * @param image {@link com.visenze.visearch.android.util.Image Image} instance, handles image decode and optimisation
     */
    public UploadSearchParams(Image image) {
        super();
        this.image = image;
    }

    /**
     * Set the {@link com.visenze.visearch.android.util.Image Image} to upload
      * @param image {@link com.visenze.visearch.android.util.Image Image} instance.
     * @return this instance.
     */
    public UploadSearchParams setImage(Image image) {
        //free memory
        if (this.image != null)
            this.image.recycle();

        this.image = image;

        return this;
    }

    /**
     * Set image url
     * @param imageUrl image url.
     * @return this instance.
     */
    public UploadSearchParams setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * Get {@link com.visenze.visearch.android.util.Image Image} that is set to search
     * @return {@link com.visenze.visearch.android.util.Image Image} instance
     */
    public Image getImage() {
        return image;
    }

    /**
     * Get image url that is set to search
     * @return image url.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();

        if (image.getBox() != null) {
            if (image.getBox().getX1() != null && image.getBox().getX2() != null &&
                    image.getBox().getY1() != null && image.getBox().getY2() != null) {
                map.put("box", image.getBox().getX1() + "," + image.getBox().getY1() + "," + image.getBox().getX2() + "," + image.getBox().getY2());
            }
        }

        if(imageUrl != null) {
            map.put("im_url", imageUrl);
        }
        return map;
    }

}
