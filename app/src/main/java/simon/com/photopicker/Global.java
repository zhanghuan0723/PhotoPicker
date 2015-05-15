package simon.com.photopicker;

import android.content.Context;
import android.net.Uri;

/**
 * Created by zhang.h on 2015/5/15
 */
public class Global {

    public static int dpToPx(Context context, int dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static boolean isGif(String uri) {
        return uri.toLowerCase().endsWith(".gif");
    }

    public static String getPath(final Context context, final Uri uri) {
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}
