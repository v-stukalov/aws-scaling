package aws.scaling.thumbnail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/*
{"objectId":"71cb084b-0763-4001-9b71-61db0d7ab66c","imageUrl":"Screen Shot 2017-12-05 at 5.47.21 PM (2).png"}
*/
public class ThumbnailRequest {

    private String objectId;

    public ThumbnailRequest() {
    }

    private String imageUrl;

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

    public static ThumbnailRequest fromJSON(String json)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ThumbnailRequest.class);
    }

    public ThumbnailRequest(String objectId, String imageUrl) {
        this.imageUrl = imageUrl;
        this.objectId = objectId;
    }

    public String toJSON() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
