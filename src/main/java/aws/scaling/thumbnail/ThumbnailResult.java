package aws.scaling.thumbnail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
{
"objectId":"12345678abcdefg",
"imageUrl":"s3://mybucket/images/image1.jpg",
"thumbnailUrl":"s3://mybucket/thumbnails/image1_thumbnail.jpg"
}
*/
public class ThumbnailResult {
    public ThumbnailResult() {
    }

    private String objectId;
    private String imageUrl;
    private String thumbnailUrl;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public ThumbnailResult(ThumbnailRequest request, String thumbnailUrl) {
        this.objectId = request.getObjectId();
        this.imageUrl = request.getImageUrl();
        this.thumbnailUrl = thumbnailUrl;
    }

    public String toJSON() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
