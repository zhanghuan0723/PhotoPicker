package simon.com.photopicker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by zhang.h on 2015/5/15
 */
public class GridPhotoAdapter extends CursorAdapter {

    Context mContext;
    LayoutInflater mInflater;
    PhotoPickActivity mActivity;

    GridPhotoAdapter(Context context, Cursor c, boolean autoRequery, PhotoPickActivity activity) {
        super(context, c, autoRequery);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mActivity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.photopick_gridlist_item, parent, false);
        convertView.getLayoutParams().height = mContext.getResources().getDisplayMetrics().widthPixels / 3;

        GridViewHolder holder = new GridViewHolder();
        holder.icon = (ImageView) convertView.findViewById(R.id.icon);
        holder.iconFore = (ImageView) convertView.findViewById(R.id.iconFore);
        holder.check = (CheckBox) convertView.findViewById(R.id.check);
        GridViewCheckTag checkTag = new GridViewCheckTag(holder.iconFore);
        holder.check.setTag(checkTag);
        holder.check.setOnClickListener(mClickItem);
        convertView.setTag(holder);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        GridViewHolder holder;
        holder = (GridViewHolder) view.getTag();

        ImageLoader imageLoader = ImageLoader.getInstance();

        String path = ImageInfo.pathAddPreFix(cursor.getString(1));
        imageLoader.displayImage(path, holder.icon, PhotoPickActivity.optionsImage);

        ((GridViewCheckTag) holder.check.getTag()).path = path;

        boolean picked = mActivity.isPicked(path);
        holder.check.setChecked(picked);
        holder.iconFore.setVisibility(picked ? View.VISIBLE : View.INVISIBLE);
    }

    static class GridViewHolder {
        ImageView icon;
        ImageView iconFore;
        CheckBox check;
    }

    View.OnClickListener mClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActivity.clickPhotoItem(v);
        }
    };
}
