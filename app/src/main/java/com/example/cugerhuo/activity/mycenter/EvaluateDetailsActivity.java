package com.example.cugerhuo.activity.mycenter;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import com.example.cugerhuo.R;

/**
 * 商品评价页
 * @author carollkarry
 * @time 2023/5/9
 */
public class EvaluateDetailsActivity extends AppCompatActivity {
  private Button goodComment;
  private Button badComment;
  private Button middleComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_details);
        goodComment=findViewById(R.id.good_comment);
        //获取Drawable
        Drawable drawable = getResources().getDrawable(R.drawable.good_comment);

        //想改变高度就修改top或bottom,改变宽度就修改left或right.
        drawable.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪一侧,分别是左上右下
        goodComment.setCompoundDrawables(drawable, null,null, null);

        badComment=findViewById(R.id.bad_comment);

        Drawable drawable1 = getResources().getDrawable(R.drawable.bad_comment);
        //想改变高度就修改top或bottom,改变宽度就修改left或right.
        drawable1.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪一侧,分别是左上右下
        badComment.setCompoundDrawables(drawable1, null,null, null);

        middleComment=findViewById(R.id.middle_comment);
        Drawable drawable2 = getResources().getDrawable(R.drawable.middle_comment);
        //想改变高度就修改top或bottom,改变宽度就修改left或right.
        drawable2.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪一侧,分别是左上右下
        middleComment.setCompoundDrawables(drawable2, null,null, null);
    }
}