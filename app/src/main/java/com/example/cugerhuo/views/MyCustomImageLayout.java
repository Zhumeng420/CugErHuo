package com.example.cugerhuo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.cugerhuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据ImageWatcher源码修改得来
 *
 * @author 小口口
 * 自定义显示图片（可显示一张、多张。如果大于9张最后一张会显示+n）
 */
public class MyCustomImageLayout extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {
    /**默认布局最多显示9个，超出显示+n*/
    public int maxDisplayCount = 9;
    /**image布局*/
    private LayoutParams lpChildImage = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    /**单张最大宽高*/
    private int mSingleMaxSize;
    /**多张图片间距*/
    private int mSpace;
    /**显示view的集合*/
    private final List<ImageView> iPictureList = new ArrayList<>();
    private final SparseArray<ImageView> mVisiblePictureList = new SparseArray<>();
    /**如果超出9个，最后一个+n显示的布局*/
    private final TextView tOverflowCount;
    /**图片点击事件回调*/
    private Callback mCallback;
    /**图片长按点击事件回调*/
    private OnImageLongClickListener clickListener;
    /**图片数据集合*/
    private List<String> mImageDataLists = new ArrayList<>();

    /**
     * 构造方法（用来处理view初始化）
     * @param context
     * @param attrs
     * @Author: 李柏睿
     * @Time: 2023/4/19 19:58
     */
    public MyCustomImageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //获取单张图片默认大小和间距
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        //本来是要也可以通过配置xml文件实现
        //这里用的是直接设置的
        //设置单个图片大小为200dp*200dp
        //这是多个图片之间的间距为5dp
        mSingleMaxSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, mDisplayMetrics));
        mSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mDisplayMetrics));


        //开始创建9个imageview
        for (int i = 0; i < maxDisplayCount; i++) {
            ImageView squareImageView = new SquareImageView(context);
            squareImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            squareImageView.setVisibility(View.GONE);
            //new图片的时候添加点击事件和长按事件
            squareImageView.setOnClickListener(this);
            squareImageView.setOnLongClickListener(this);
            addView(squareImageView);
            iPictureList.add(squareImageView);
        }

        //如果图片个数大于9张，这个就是第九个图片上面的+n的view创建
        tOverflowCount = new TextView(context);
        tOverflowCount.setTextColor(0xFFFFFFFF);
        tOverflowCount.setTextSize(24);
        tOverflowCount.setGravity(Gravity.CENTER);
        tOverflowCount.setBackgroundColor(0x66000000);
        tOverflowCount.setVisibility(View.GONE);
        addView(tOverflowCount);
    }

    /**
     * 这个是单张图片的调用方法（废弃了）
     * @param list
     * @Author: 李柏睿
     * @Time: 2023/4/19 19:59
     */
    public void setUrlData(String list) {
        mImageDataLists.clear();
        mImageDataLists.add(list);
        mSingleMaxSize = (int) (getWidth() * 1f - mSpace);
        notifyDataChanged();
    }

    /**
     * 设置图片数据方法
     * 目前是用的list集合，如果是数组的话请自行转
     * @param list
     * @Author: 李柏睿
     * @Time: 2023/4/19 19:59
     */
    public void setUrlListData(List<String> list) {
        mImageDataLists.clear();
        mImageDataLists = list;
        notifyDataChanged();
    }

    /**
     *
     */
    /**
     * 数据刷新
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:00
     */
    private void notifyDataChanged() {

        //图片的长度
        final int urlListSize = mImageDataLists.size();

        //如果图片不为空并且大于0，显示布局
        setVisibility(mImageDataLists.size() < 1 ? View.GONE : View.VISIBLE);
        if (mImageDataLists.size() < 1) {
            return;
        }

        //默认每行显示3个图片
        int column = 3;
        //判断每行需要显示的图片数量
        if (urlListSize == 1) {
            column = 1;
        } else if (urlListSize == 4) {
            column = 2;
        }

        //默认显示1行
        int row = 1;
        //判断需要显示的行数
        if (urlListSize > 6) {
            row = 3;
        } else if (urlListSize > 3) {
            row = 2;
        } else if (urlListSize > 0) {
            row = 1;
        }

        //确认每个图片显示的大小了
        int imageSize = 0;
        //如果是一个图片的话，就显示默认的单个图片大小
        if (urlListSize == 1) {
            imageSize = mSingleMaxSize;}
        //如果是两张或者四张的话，每行显示2个，显示2行
        else if (urlListSize == 2 || urlListSize == 4) {
            imageSize = (int) ((getWidth() * 1f - mSpace) / 2);}
        //否则显示3个
        else {
            imageSize = (int) ((getWidth() * 1f - mSpace) / 3);
        }

        //将图片大小给到子布局（宽高设置为一样的）
        lpChildImage.width = imageSize;
        lpChildImage.height = imageSize;

        //如果图片数量大于MAX_DISPLAY_COUNT值，显示+n布局
        tOverflowCount.setVisibility(urlListSize > maxDisplayCount ? View.VISIBLE : View.GONE);
        tOverflowCount.setText("+ " + (urlListSize - maxDisplayCount));
        tOverflowCount.setLayoutParams(lpChildImage);

        //加载图片之前，清空已显示的图片集合
        mVisiblePictureList.clear();

        //遍历图片集合
        for (int i = 0; i < iPictureList.size(); i++) {
            //加载每一个图片
            final ImageView iPicture = iPictureList.get(i);
            if (i < urlListSize) {
                iPicture.setVisibility(View.VISIBLE);
                mVisiblePictureList.put(i, iPicture);
                iPicture.setLayoutParams(lpChildImage);
                iPicture.setBackgroundResource(R.drawable.default_avatar_1);
                Glide.with(getContext()).load(mImageDataLists.get(i)).into(iPicture);
                iPicture.setTranslationX((i % column) * (imageSize + mSpace));
                iPicture.setTranslationY((i / column) * (imageSize + mSpace));
                //设置tag，在图片点击事件的时候可以当作图片的下标
                iPicture.setTag(i);
            } else {
                iPicture.setVisibility(View.GONE);
            }

            //第九个单独处理
            if (i == maxDisplayCount - 1) {
                tOverflowCount.setTranslationX((i % column) * (imageSize + mSpace));
                tOverflowCount.setTranslationY((i / column) * (imageSize + mSpace));
            }
        }

        //根据自布局显示整体布局的高
        getLayoutParams().height = imageSize * row + mSpace * (row - 1);
    }

    /**
     * 设置回调接口
     * @param callback
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:00
     */
    public void setOnImageClickListener(Callback callback) {
        mCallback = callback;
    }

    /**
     * 图片的点击事件
     * @param v
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:00
     */
    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            mCallback.onImageClick((ImageView) v, mVisiblePictureList, mImageDataLists);
        }
    }

    /**
     * 回调事件接口
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:01
     */
    public interface Callback {
        void onImageClick(ImageView i, SparseArray<ImageView> imageGroupList, List<String> urlList);
    }

    /**
     * 长按事件
     * @param v
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:01
     */
    @Override
    public boolean onLongClick(View v) {
        if (clickListener != null) {
            clickListener.onImageLongClick((ImageView) v, mVisiblePictureList, mImageDataLists);
        }
        return true;
    }

    /**
     * 回调长按事件接口
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:01
     */
    public interface OnImageLongClickListener {
        void onImageLongClick(ImageView i, SparseArray<ImageView> imageGroupList, List<String> urlList);
    }

    /**
     * 设置长按事件回调接口
     * @param callback
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:02
     */
    public void setOnImageLongClickListener(OnImageLongClickListener callback) {
        clickListener = callback;
    }

    /**
     * 一旦计算出宽高，刷新数据
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:02
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        notifyDataChanged();
    }

    /**
     * 设置图片的最大数量
     * @param count
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:03
     */
    public void setMaxCount(int count) {
        this.maxDisplayCount = count;
    }

}
