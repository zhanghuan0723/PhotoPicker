package simon.com.photopicker;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by zhang.h on 2015/5/15
 */
public class ImagePagerFragment extends Fragment {

    private String uri;

    private FrameLayout rootLayout;
    private ProgressBar loading;
    private ImageView imageView;

    public void setData(String uriString) {
        uri = uriString;
    }

    public static ImagePagerFragment newInstance(String uri) {
        ImagePagerFragment fragment = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putString("uri", uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uri = getArguments().getString("uri");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_image_pager, container, false);
        rootLayout = (FrameLayout) view.findViewById(R.id.rootLayout);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        showPhoto();
    }

    private void showPhoto() {
        PhotoView photoView = (PhotoView) getActivity().getLayoutInflater().inflate(R.layout.imageview_touch, null);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().onBackPressed();
            }
        });

        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().onBackPressed();
            }
        });

        imageView = photoView;
        rootLayout.addView(imageView);

        ImageLoader.getInstance().displayImage(uri, imageView, App.getInstance().getOptionsForImage(R.drawable.image_not_exist, false), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "IO错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片编码错误";
                        break;
                    case NETWORK_DENIED:
                        message = "载入图片超时";
                        break;
                    case OUT_OF_MEMORY:
                        message = "内存不足";
                        break;
                    case UNKNOWN:
                        message = "未知错误";
                        break;
                    default:
                        message = "未知错误";
                        break;
                }

                loading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
                loading.setVisibility(View.GONE);
            }
        });
    }
}