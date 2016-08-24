package dhbk.android.materialtemplate2.model;

import android.support.annotation.StringRes;

import java.util.Locale;

import dhbk.android.materialtemplate2.R;
import dhbk.android.materialtemplate2.utils.EnumDtoHelper;

/**
 * Created by Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 31.03.15.
 */
public enum SortTypes {
    MAGIC(R.string.sort_magic),
    POPULARITY(R.string.sort_popularity),
    NEWEST(R.string.sort_newest),
    END_DATE(R.string.sort_end_date),
    MOST_FUNDED(R.string.sort_most_funded);

    SortTypes(@StringRes int nameResId) {
        this.apiName = name().toLowerCase(Locale.ENGLISH);
        this.displayName = EnumDtoHelper.getDisplayName(nameResId, apiName);
    }

    private final String displayName;
    private final String apiName;

    public String getApiName() {
        return apiName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
