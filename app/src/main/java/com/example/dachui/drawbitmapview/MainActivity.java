package com.example.dachui.drawbitmapview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 使用属性动画实现图片从左到右缓慢加载显示
 */
public class MainActivity extends AppCompatActivity {
    private DrawBitmapView mDrawBitmapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawBitmapView = (DrawBitmapView) findViewById(R.id.bitmap);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawBitmapView.startMove(2000, new DecelerateInterpolator());
            }
        });

        mDrawBitmapView.setOnAnimEndListener(new DrawBitmapView.OnAnimEndListener() {
            @Override
            public void endMove() {
                Log.d("====", "endmove");
            }
        });
    }
}
