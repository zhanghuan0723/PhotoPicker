package simon.com.photopicker;

import java.io.Serializable;

/**
 * Created by zhang.h on 2015/5/15
 */
public class PhotoDataSerializable implements Serializable {
    String uriString = "";
    String serviceUri = "";

    public PhotoDataSerializable(PhotoData data) {
        uriString = data.uri.toString();
        serviceUri = data.serviceUri;
    }
}
