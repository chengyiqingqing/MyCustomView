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
 * 自定义开关
 * 1，多了一个内部监听接口，内有回调方法；
 * 2，多了一个成员接口和一个方法set（接口）；
 * 3，多了一个interface.update()；在引发该语句的地方调用。如触摸事件的时候或者触摸引发绘制事件的时候。
 *
 * 因为是接口，在外面的类中调用的时候。会调用它的实现，形成回调。
 */

public class AttriToggleViewListener extends View {

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
    public AttriToggleViewListener(Context context) {
        this(context,null);
    }

    /**
     * 含有AttributeSet就可以了。
     * @param context
     * @param attrs
     */
    public AttriToggleViewListener(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AttriToggleViewListener(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(AttributeSet attrs) {
        paint=new Paint();
        String namespace="http://schemas.android.com/apk/res-auto";
        //拿到的是R文件的资源；
        int switchBackground=attrs.getAttributeResourceValue(namespace,"switch_background",-1);
        int slideButton=attrs.getAttributeResourceValue(namespace,"slide_button",-1);
        switchState=attrs.getAttributeBooleanValue(namespace,"switch_state",false);
        setSwitchBackgroudResource(switchBackground);
        setSlideButtonResource(slideButton);
        setSwitchState(switchState);
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
            //java中的接口回调；它肯定是有一个实现类的。
            switchStateUpdateListener.switchStateUpdate(switchState);
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

    public SwitchStateUpdateListener switchStateUpdateListener;

    public void setSwitchStateUpdateListener(SwitchStateUpdateListener switchStateUpdateListener) {
        this.switchStateUpdateListener = switchStateUpdateListener;
    }

    interface SwitchStateUpdateListener{
        void switchStateUpdate(boolean switchState);
    }

}
