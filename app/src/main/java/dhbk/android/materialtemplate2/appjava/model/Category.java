package dhbk.android.materialtemplate2.appjava.model;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import dhbk.android.materialtemplate2.appjava.utils.ColorMixer;


/**
 * @author Pawel Karczewski <pawel.karczewski at byoutline.com> on 2015-03-24
 *
 * when you click the fab icon in main activity, it's will show a list of categories.
 */
@Parcel(Parcel.Serialization.FIELD)
public class Category {

    public static final int ALL_CATEGORIES_ID = -1;
    public final int categoryId;
    @StringRes
    public final int nameResId;
    @ColorRes
    public final int colorResId;
    @DrawableRes
    public final int drawableResId;
    @ColorRes
    int bgColor = Color.WHITE;

    // FIXME: 8/24/16 add @annotation to check the resource, add ParcelConstructor to construct the parcel to send via Intent
    @ParcelConstructor
    public Category(int categoryId, @StringRes int nameResId, @ColorRes int colorResId, @DrawableRes int drawableResId) {
        this.categoryId = categoryId;
        this.nameResId = nameResId;
        this.colorResId = colorResId;
        this.drawableResId = drawableResId;
    }

    public void setBgColor(@Nullable Integer color) {
        if (color == null) {
            bgColor = Color.WHITE;
        } else {
            bgColor = ColorMixer.mixTwoColors(color, Color.WHITE, 0.20f);
        }
    }

    @ColorRes
    public int getBgColor() {
        return bgColor;
    }
}



