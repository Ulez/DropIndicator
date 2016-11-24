package comulez.github.dropindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ulez on 2016/11/24.
 * Email：lcy1532110757@gmail.com
 */
public class DropIndicator extends ViewGroup {
    private int circleColor;
    private int clickColor;
    private Paint mClickPaint;
    private int duration;
    private Paint mPaintCircle;
    private Paint mPaint;
    private Path mPath = new Path();
    private float ratio = 50;
    private final double c = 0.552284749831;
    private final int r = 1;
    private int mWidth;
    private int mHeight;
    private float startX;
    private int startY;
    private float totalOff;//总偏移量
    private float distance;
    private int currOff;//当前偏移量
    private float mCurrentTime;
    private int tabNum = 0;
    private XPoint p2, p4;
    private YPoint p1, p3;
    private float mc;
    private float radius;
    private int[] roundColors = new int[4];//默认支持四种颜色；
    private float div;
    private float scale = 0.8f;

    private int indiCurrPos = 1;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private float indiCurrPosF;
    private int viewPagerState;
    public static final int SCROLL_STATE_IDLE = 0;//空闲；Indicates that the pager is in an idle, settled state. The current page is fully in view and no animation is in progress.
    public static final int SCROLL_STATE_DRAGGING = 1;//拖动；Indicates that the pager is currently being dragged by the user.
    public static final int SCROLL_STATE_SETTLING = 2;//设置过程中；Indicates that the pager is in the process of settling to a final position.

    public DropIndicator(Context context) {
        this(context, null);
    }

    public DropIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropIndicator);
        roundColors[0] = typedArray.getColor(R.styleable.DropIndicator_color1, Color.parseColor("#B04285F4"));
        roundColors[1] = typedArray.getColor(R.styleable.DropIndicator_color2, Color.parseColor("#B0EA4335"));
        roundColors[2] = typedArray.getColor(R.styleable.DropIndicator_color3, Color.parseColor("#B0FBBC05"));
        roundColors[3] = typedArray.getColor(R.styleable.DropIndicator_color4, Color.parseColor("#B034A853"));
        clickColor = typedArray.getColor(R.styleable.DropIndicator_click_color, Color.WHITE);
        circleColor = typedArray.getColor(R.styleable.DropIndicator_circle_color, Color.GRAY);
        radius = typedArray.getDimension(R.styleable.DropIndicator_radius, 50);//单位像素；
        duration = typedArray.getInteger(R.styleable.DropIndicator_duration, 1000);
        scale = typedArray.getFloat(R.styleable.DropIndicator_scale, 0.8f);
        typedArray.recycle();
        ratio = radius;
        mc = (float) (c * ratio);
        mPaintCircle = new Paint();
        mPaintCircle.setColor(circleColor);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStrokeWidth(3);

        mClickPaint = new Paint();
        mClickPaint.setColor(clickColor);
        mClickPaint.setStyle(Paint.Style.STROKE);
        mClickPaint.setAntiAlias(true);
        mClickPaint.setStrokeWidth(radius / 2);

        mPaint = new Paint();
        startColor = roundColors[0];
        mPaint.setColor(startColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        div = (mWidth - 2 * tabNum * radius) / (tabNum + 1);
        startX = div + radius;
        startY = mHeight / 2;
        totalOff = (tabNum - 1) * (2 * radius + div) - radius;

        if (indiCurrPos == 1) {
            radius = r * ratio;
            mc = (float) (c * ratio);
            p1 = new YPoint(0, radius, mc);
            p3 = new YPoint(0, -radius, mc);
            p2 = new XPoint(radius, 0, mc);
            p4 = new XPoint(-radius, 0, mc);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        if (x > div + 2 * radius) {
            int yy2 = (int) ((x - div - 2 * radius) / (div + 2 * radius));
            if (yy2 + 2 != indiCurrPos && yy2 + 2 <= tabNum)
                startAniTo(yy2 + 2);
        } else if (x > div) {
            if (indiCurrPos != 1)
                startAniTo(1);
        }
        return super.onTouchEvent(event);
    }

    boolean direction = true; //向右为true，向左为false；

    private boolean startAniTo(int toPos) {
        startColor = roundColors[(indiCurrPos - 1) % 4];
        endColor = roundColors[(toPos - 1) % 4];
        p1.setY(radius);
        p1.setX(0);
        p1.setMc(mc);

        p3.setY(-radius);
        p3.setX(0);
        p3.setMc(mc);

        p2.setY(0);
        p2.setX(radius);
        p2.setMc(mc);

        p4.setY(0);
        p4.setX(-radius);
        p4.setMc(mc);

//        p1 = new YPoint(0, radius, mc);
//        p3 = new YPoint(0, -radius, mc);
//        p2 = new XPoint(radius, 0, mc);
//        p4 = new XPoint(-radius, 0, mc);

        direction = toPos - indiCurrPos > 0 ? true : false;
        startX = div + radius + (indiCurrPos - 1) * (div + 2 * radius);
        distance = (toPos - indiCurrPos) * (2 * radius + div) + (direction ? -radius : radius);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentTime = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
        if (mViewPager != null) {
            mViewPager.setCurrentItem(toPos - 1);
        }
//        MoveAnimation animation = new MoveAnimation();
//        animation.setDuration(duration);
//        animation.setInterpolator(new AccelerateDecelerateInterpolator());
//        startAnimation(animation);
        indiCurrPos = toPos;
        return true;
    }

    private int startColor;
    private int endColor;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();//保存画布；
        mPath.reset();
        tabNum = getChildCount();
        for (int i = 0; i < tabNum; i++) {
            canvas.drawCircle(div + radius + i * (div + 2 * radius), startY, radius, mPaintCircle);
        }
        if (mCurrentTime >= 0 && mCurrentTime <= 0.2) {
            canvas.drawCircle(div + radius + (indiCurrPos - 1) * (div + 2 * radius), startY, radius * 1.0f * 5 * mCurrentTime, mClickPaint);
            mPaint.setColor(startColor);
            canvas.translate(startX, startY);
            if (direction) {
                p2.setX(radius + 2 * 5 * mCurrentTime * radius / 2);//注：1.08为修正值，真实应该是1；
//            Log.e("LCY", "mCurrentTime=" + mCurrentTime);//mCurrentTime=0.18493307;只到了这里；刷新间隔0.02s；
            } else {
                p4.setX(-radius - 2 * 5 * mCurrentTime * radius / 2);
            }
        } else if (mCurrentTime > 0.2 && mCurrentTime <= 0.5) {
            canvas.translate(startX + (mCurrentTime - 0.2f) * distance / 0.7f, startY);
            if (direction) {
                p2.setX(2 * radius);
                p1.setX(0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
                p3.setX(0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
                p2.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
                p4.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
            } else {
                p4.setX(-2 * radius);
                p1.setX(-0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
                p3.setX(-0.5f * radius * (mCurrentTime - 0.2f) / 0.3f);
                p2.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
                p4.setMc(mc + (mCurrentTime - 0.2f) * mc / 4 / 0.3f);
            }
        } else if (mCurrentTime > 0.5 && mCurrentTime <= 0.8) {
            canvas.translate(startX + (mCurrentTime - 0.2f) * distance / 0.7f, startY);
            if (direction) {
                p1.setX(0.5f * radius + 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
                p3.setX(0.5f * radius + 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
                p2.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
                p4.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
            } else {
                p1.setX(-0.5f * radius - 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
                p3.setX(-0.5f * radius - 0.5f * radius * (mCurrentTime - 0.5f) / 0.3f);
                p2.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
                p4.setMc(1.25f * mc - 0.25f * mc * (mCurrentTime - 0.5f) / 0.3f);
            }
        } else if (mCurrentTime > 0.8 && mCurrentTime <= 0.9) {
            p2.setMc(mc);
            p4.setMc(mc);
            canvas.translate(startX + (mCurrentTime - 0.2f) * distance / 0.7f, startY);
            if (direction) {
                p4.setX(-radius + 1.6f * radius * (mCurrentTime - 0.8f) / 0.1f);//r+r;
            } else {
                p2.setX(radius - 1.6f * radius * (mCurrentTime - 0.8f) / 0.1f);//r+r;
            }
        } else if (mCurrentTime > 0.9 && mCurrentTime <= 1) {
            mPaint.setColor(endColor);

            if (direction) {
                p1.setX(radius);
                p3.setX(radius);
                canvas.translate(startX + distance, startY);
                p4.setX(0.6f * radius - 0.6f * radius * (mCurrentTime - 0.9f) / 0.1f);
            } else {
                p1.setX(-radius);
                p3.setX(-radius);
                canvas.translate(startX + distance, startY);
                p2.setX(-0.6f * radius + 0.6f * radius * (mCurrentTime - 0.9f) / 0.1f);
            }
        }
        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();//合并图层
        super.dispatchDraw(canvas);
    }

    private double g2 = 1.41421;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        tabNum = getChildCount();
        for (int i = 0; i < tabNum; i++) {
            View child = getChildAt(i);
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        tabNum = getChildCount();
        for (int i = 0; i < tabNum; i++) {
            View child = getChildAt(i);
            child.layout((int) (div + (1 - scale * 1 / g2) * radius + i * (div + 2 * radius)), (int) (startY - scale * radius / g2), (int) (div + (1 + scale * 1 / g2) * radius + i * (div + 2 * radius)), (int) (startY + scale * radius / g2));
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                startAniTo(position + 1);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 滚动
                scroll(position, positionOffset);
                // 回调
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                viewPagerState = state;
                // 回调
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    private void scroll(int position, float positionOffset) {
        indiCurrPosF = position + positionOffset + 1;
//        double decimalPart = indiCurrPosF - (int)indiCurrPosF;
//        Log.e("lcy","indiCurrPosF="+indiCurrPosF+",小数部分="+decimalPart);
    }

    /**
     * x相同
     */
    class XPoint {
        public float x;//中心x；
        public float y;//中心y；
        public float mc;
        public PointF bottom;
        public PointF top;

        public XPoint(float x, float y, float mc) {
            this.x = x;
            this.y = y;
            this.mc = mc;
            if (bottom == null)
                bottom = new PointF();
            if (top == null)
                top = new PointF();
            bottom.y = y + mc;
            top.y = y - mc;
            bottom.x = x;
            top.x = x;
        }

        public void setMc(float mc) {
            this.mc = mc;
            bottom.y = y + mc;
            top.y = y - mc;
        }

        public void setY(float y) {
            this.y = y;
            bottom.y = y + mc;
            top.y = y - mc;
        }

        public void setX(float x) {
            this.x = x;
            bottom.x = x;
            top.x = x;
        }

    }

    /**
     * y相同
     */
    class YPoint {
        public float x;//中心x；
        public float y;//中心y；
        public float mc;
        public PointF left;
        public PointF right;

        public YPoint(float x, float y, float mc) {
            this.x = x;
            this.y = y;
            this.mc = mc;
            if (left == null)
                left = new PointF();
            if (right == null)
                right = new PointF();
            right.x = x + mc;
            left.x = x - mc;
            left.y = y;
            right.y = y;
        }

        public void setMc(float mc) {
            this.mc = mc;
            right.x = x + mc;
            left.x = x - mc;
        }

        public void setX(float x) {
            this.x = x;
            right.x = x + mc;
            left.x = x - mc;
        }

        public void setY(float y) {
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void setLeftX(float leftX) {
            left.x = leftX;
            x = (left.x + right.x) / 2;
        }
    }

    public int dip2px(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}


