package com.example.mylib_test.activity.animal;

import com.example.mylib_test.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class AniPro extends Activity {
	private ImageView button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_anipro);
		button2=(ImageView) findViewById(R.id.button2);
	}
    public void rotateyAnimRun(View view)  
    {  
         ObjectAnimator//  
         .ofFloat(view, "rotationX", 0.0F, 360.0F)//  
         .setDuration(500)//  
         .start();  
    }  
    
    public void togetherRun(View view)  
    {  
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(button2, "scaleX",  
                1.0f, 2f);  
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(button2, "scaleY",  
                1.0f, 2f); 
        AnimatorSet animSet = new AnimatorSet();  
        animSet.setDuration(2000);  
        animSet.setInterpolator(new LinearInterpolator());  
        //��������ͬʱִ��  
        animSet.playTogether(anim1, anim2);  
        animSet.start();  
    }  
  
    /** 
     * ������ 
     * @param view 
     */  
    public void paowuxian(View view)  
    {  
  
        ValueAnimator valueAnimator = new ValueAnimator();  
        valueAnimator.setDuration(3000);  
        valueAnimator.setObjectValues(new PointF(0, 0));  
        valueAnimator.setInterpolator(new LinearInterpolator());  
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>()  
        {  
            // fraction = t / duration  
            @Override  
            public PointF evaluate(float fraction, PointF startValue,  
                    PointF endValue)  
            {  
                // x����200px/s ����y����0.5 * 10 * t  
                PointF point = new PointF();  
                point.x = 200 * fraction * 3;  
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);  
                return point;  
            }  
        });  
  
        valueAnimator.start();  
        valueAnimator.addUpdateListener(new AnimatorUpdateListener()  
        {  
            @Override  
            public void onAnimationUpdate(ValueAnimator animation)  
            {  
                PointF point = (PointF) animation.getAnimatedValue();  
                button2.setX(point.x);  
                button2.setY(point.y);  
  
            }  
        });  
    }  
}
