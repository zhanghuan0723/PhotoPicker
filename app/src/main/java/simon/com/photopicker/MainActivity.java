package simon.com.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhang.h on 2015/5/15
 */
public class MainActivity extends Activity {

    public static final int RESULT_REQUEST_IMAGE = 1001;
    public static final int RESULT_REQUEST_PICK_PHOTO = 1003;
    public static final int PHOTO_MAX_COUNT = 6;

    private GridView gridView;
    private ArrayList<PhotoData> mData = new ArrayList();

    private ImageSize mSize;
    private int imageWidthPx;

    private PhotoOperate photoOperate = new PhotoOperate(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageWidthPx = Global.dpToPx(this, 100);
        mSize = new ImageSize(imageWidthPx, imageWidthPx);

        gridView = (AutoHeightGridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mData.size()) {
                    int count = PHOTO_MAX_COUNT - mData.size();
                    if (count <= 0) {
                        return;
                    }

                    Intent intent = new Intent(MainActivity.this, PhotoPickActivity.class);
                    intent.putExtra(PhotoPickActivity.EXTRA_MAX, count);
                    startActivityForResult(intent, RESULT_REQUEST_PICK_PHOTO);
                } else {
                    Intent intent = new Intent(MainActivity.this, ImagePagerActivity.class);
                    ArrayList<String> arrayUri = new ArrayList<>();
                    for (PhotoData item : mData) {
                        arrayUri.add(item.uri.toString());
                    }
                    intent.putExtra("mArrayUri", arrayUri);
                    intent.putExtra("mPagerPosition", position);
                    intent.putExtra("needEdit", true);
                    startActivityForResult(intent, RESULT_REQUEST_IMAGE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_REQUEST_PICK_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    ArrayList<ImageInfo> pickPhots = (ArrayList<ImageInfo>) data.getSerializableExtra("data");
                    for (ImageInfo item : pickPhots) {
                        Uri uri = Uri.parse(item.path);
                        File outputFile = photoOperate.scal(uri);
                        mData.add(new PhotoData(outputFile));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == RESULT_REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> delUris = data.getStringArrayListExtra("mDelUrls");
                for (String item : delUris) {
                    for (int i = 0; i < mData.size(); ++i) {
                        if (mData.get(i).uri.toString().equals(item)) {
                            mData.remove(i);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    BaseAdapter adapter = new BaseAdapter() {

        public int getCount() {
            return mData.size() + 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public ArrayList<ViewHolder> holderList = new ArrayList<>();

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                holder.image = (ImageView) getLayoutInflater().inflate(R.layout.image_make_maopao, parent, false);
                holderList.add(holder);
                holder.image.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == getCount() - 1) {
                if (getCount() == (PHOTO_MAX_COUNT + 1)) {
                    holder.image.setVisibility(View.INVISIBLE);
                } else {
                    holder.image.setVisibility(View.VISIBLE);
                    holder.image.setImageResource(R.drawable.make_maopao_add);
                    holder.uri = "";
                }
            } else {
                holder.image.setVisibility(View.VISIBLE);
                PhotoData photoData = mData.get(position);
                Uri data = photoData.uri;
                holder.uri = data.toString();

                ImageLoader.getInstance().loadImage(data.toString(), mSize, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        for (ViewHolder item : holderList) {
                            if (item.uri.equals(imageUri)) {
                                item.image.setImageBitmap(loadedImage);
                            }
                        }
                    }
                });
            }
            return holder.image;
        }

        class ViewHolder {
            ImageView image;
            String uri = "";
        }
    };
}