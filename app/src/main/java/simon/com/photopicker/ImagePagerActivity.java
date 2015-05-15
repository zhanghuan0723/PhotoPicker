package simon.com.photopicker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zhang.h on 2015/5/15
 */
public class ImagePagerActivity extends FragmentActivity {

    private ArrayList<String> mArrayUri;
    private int mPagerPosition;

    private PhotoViewPager imagePager;
    private ImagePagerAdapter pagerAdapter;

    private ArrayList<String> mDelUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);

        Bundle bundle = getIntent().getExtras();
        mArrayUri = bundle.getStringArrayList("mArrayUri");
        mPagerPosition = bundle.getInt("mPagerPosition");

        imagePager = (PhotoViewPager) findViewById(R.id.pager);
        pagerAdapter = new ImagePagerAdapter(getSupportFragmentManager());
        imagePager.setAdapter(pagerAdapter);
        imagePager.setCurrentItem(mPagerPosition);
    }

    View.OnClickListener onClickRight = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int selectPos = imagePager.getCurrentItem();
            AlertDialog dialog = new AlertDialog.Builder(ImagePagerActivity.this).setTitle("确定删除？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String s = mArrayUri.remove(selectPos);
                            mDelUrls.add(s);
                            if (mArrayUri.isEmpty()) {
                                onBackPressed();
                            } else {
                                pagerAdapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    };

    @Override
    public void onBackPressed() {
        if (mDelUrls.isEmpty()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra("mDelUrls", mDelUrls);
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    class ImagePagerAdapter extends FragmentPagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return ImagePagerFragment.newInstance(mArrayUri.get(i));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImagePagerFragment fragment = (ImagePagerFragment) super.instantiateItem(container, position);
            fragment.setData(mArrayUri.get(position));
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mArrayUri.size();
        }
    }
}

