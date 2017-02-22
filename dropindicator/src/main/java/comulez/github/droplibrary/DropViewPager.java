package comulez.github.droplibrary;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Ulez on 2017/2/19.
 * Email：lcy1532110757@gmail.com
 */

public class DropViewPager extends ViewPager {

    private boolean touchable = true;

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public DropViewPager(Context context) {
        super(context);
    }

    public DropViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (touchable)
            return super.onTouchEvent(ev);
        else
            return true;
    }
}
