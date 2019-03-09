package custom_font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by one on 3/12/15.
 */
public class MyTextVieww extends android.support.v7.widget.AppCompatTextView {

    public MyTextVieww(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextVieww(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextVieww(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }

}