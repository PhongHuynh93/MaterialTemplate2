package dhbk.android.materialtemplate2.managers;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by huynhducthanhphong on 8/24/16.
 */
public class AccessTokenProvider  implements Provider<String> {
    @Nonnull
    private String accessToken = "";

    @Inject
    public AccessTokenProvider() {
    }

    public void set(String accessToken) {
        if (accessToken == null) {
            accessToken = "";
        }
        this.accessToken = accessToken;
    }

    @Override
    public String get() {
        return accessToken;
    }
}
