package simon.com.photopicker;

import android.net.Uri;

import java.io.File;

/**
 * Created by zhang.h on 2015/5/15
 */
public class PhotoData {
    Uri uri = Uri.parse("");
    String serviceUri = "";

    public PhotoData(File file) {
        uri = Uri.fromFile(file);
    }

    public PhotoData(PhotoDataSerializable data) {
        uri = Uri.parse(data.uriString);
        serviceUri = data.serviceUri;
    }
}
