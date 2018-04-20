package com.wenwen.mycustomview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sww on 2018/4/20.
 * 自定义开关 (是在Activity的onResume()方法中进行的。 )
 * Android的界面绘制流程
 *          测量           摆放        绘制
 *          onMeasure --> onLayout --> onDraw
 * View：
 * onMeasure() 在这个方法里指定自己的宽高  --》onDraw()绘制自己的内容；
 *
 * ViewGroup
 * onMeasure() 指定自己的宽高，所有子View的宽高 --》onLayout摆放所有子控件； --》onDraw()进行绘制；
 *
 */

public class ToggleView extends View {

    Bitmap bitmapBackground;
    Bitmap bitmapSlide;
    int slideWidth;
    int slideHeight;
    boolean switchState=false;
    Paint paint;
    /**
     * 不含AttributeSet自定义属性，可以直接通过Java代码new出来；
     * @param context
     */
    public ToggleView(Context context) {
        this(context,null);
    }

    /**
     * 含有AttributeSet就可以了。
     * @param context
     * @param attrs
     */
    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init() {
        paint=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1.先计算值；
        int width=bitmapBackground.getWidth();
        int height=bitmapBackground.getHeight();
        slideWidth=bitmapSlide.getWidth();
        slideHeight=bitmapSlide.getHeight();
        //2.然后设置自己的值；(此时，canvas大小就固定了)
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapBackground,0,0,paint);
        int midActive=canvas.getWidth()-bitmapSlide.getWidth();
        if (isTouchMode){
            float newLeft=currentX<midActive?currentX:midActive;
            canvas.drawBitmap(bitmapSlide,newLeft,0,paint);
        }else{
            if (switchState){//开
                canvas.drawBitmap(bitmapSlide,canvas.getWidth()-bitmapSlide.getWidth(),0,paint);
            }else{//关；
                canvas.drawBitmap(bitmapSlide,0,0,paint);
            }
        }

    }

    /**
     * 1.设置View的背景；
     * 设置后就是CustomView的渲染了:onMeasure()-->onLayout()-->onDraw();
     * @param switch_background 就是一个R.drawable.iv；
     */
    public void setSwitchBackgroudResource(int switch_background) {
        //1.第一步通过BitmapFactory和R.drawable.iv 拿到bitmap;
        bitmapBackground= BitmapFactory.decodeResource(getResources(),switch_background);
    }

    /**
     * 2.设置滑块的图片资源；
     * 设置后就是CustomView的渲染了:onMeasure()-->onLayout()-->onDraw();
     * @param slide_button
     */
    public void setSlideButtonResource(int slide_button) {
        bitmapSlide=BitmapFactory.decodeResource(getResources(),slide_button);
    }

    /**
     * 3.设置滑块的转化资源；
     * 设置后就是CustomView的渲染了:onMeasure()-->onLayout()-->onDraw();
     * @param b
     */
    public void setSwitchState(boolean b) {
        switchState=b;
    }

    boolean isTouchMode=false;
    float currentX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouchMode=true;
                currentX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchMode=false;
                currentX=event.getX();
                switchState=currentX>bitmapBackground.getWidth()/2?true:false;
                break;
            default:
                break;
        }
        invalidate();//它会引发 view的onDraw()方法。
        return true;
    }

}
