package com.reactnativenavigation.views.element;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.reactnativenavigation.utils.UiUtils;

import static com.reactnativenavigation.utils.ColorUtils.labToColor;
import static com.reactnativenavigation.utils.TextViewUtils.setSpanColor;

public class Element extends FrameLayout {
    private String elementId;
    @Nullable private SpannableString spannableText;

    public Element(@NonNull Context context) {
        super(context);
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public View getChild() {
        return getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ((ViewGroup) getParent()).setClipChildren(false);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        UiUtils.runOnPreDrawOnce(child, () -> {
            if (child instanceof TextView) {
                SpannedString spannedText = new SpannedString(((TextView) child).getText());
                spannableText = new SpannableString(spannedText);
                ((TextView) child).setText(spannableText);
            }
        });
    }

    @Keep
    public void setClipBounds(Rect clipBounds) {
        getChild().setClipBounds(clipBounds);
    }

    @Keep
    public void setMatrixTransform(float value) {
        GenericDraweeHierarchy hierarchy = ((DraweeView<GenericDraweeHierarchy>) getChild()).getHierarchy();
        if (hierarchy.getActualImageScaleType() != null) {
            ((ScalingUtils.InterpolatingScaleType) hierarchy.getActualImageScaleType()).setValue(value);
            getChild().invalidate();
        }
    }

    @Keep
    public void setTextColor(double[] color) {
        if (spannableText != null) {
            setSpanColor(spannableText, labToColor(color));
            ((TextView) getChild()).setText(spannableText);
        }
    }
}
