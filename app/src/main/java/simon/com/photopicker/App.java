package simon.com.photopicker;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class App extends Application {

    public static float sScale;
    public static int sWidthDp;
    public static int sWidthPix;
    public static int sHeightPix;

    private Context mContext;
    private static App singleton;

    public static App getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    /**
     * Intialize
     */
    private void init() {
        singleton = this;
        mContext = this.getApplicationContext();

        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;
        sWidthDp = (int) (sWidthPix / sScale);

        initImageLoader(mContext);
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)//
                .threadPriority(Thread.NORM_PRIORITY - 2) // 线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()//
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 名称用MD5加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)//
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public DisplayImageOptions getOptionsForImage(int defImg, boolean isRounded) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(defImg); // 设置图片在下载期间显示的图片
        builder.showImageForEmptyUri(defImg); // 设置图片Uri为空或是错误的时候显示的图片
        builder.showImageOnFail(defImg); // 设置图片加载/解码过程中错误时候显示的图片
        builder.cacheInMemory(true); // 设置下载的图片是否缓存在内存中
        builder.cacheOnDisk(true); // 设置下载的图片是否缓存在SD卡中
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        if (isRounded)
            builder.displayer(new RoundedBitmapDisplayer(30)); // 是否设置为圆角，弧度为多少
        return builder.build();
    }
}