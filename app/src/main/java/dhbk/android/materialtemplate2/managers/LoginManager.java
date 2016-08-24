package dhbk.android.materialtemplate2.managers;

import com.byoutline.cachedfield.CachedFieldWithArg;
import com.byoutline.ottocachedfield.OttoCachedFieldWithArgBuilder;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import dhbk.android.materialtemplate2.api.KickMaterialService;
import dhbk.android.materialtemplate2.appjava.model.AccessToken;
import dhbk.android.materialtemplate2.appjava.model.EmailAndPass;
import dhbk.android.materialtemplate2.dagger.GlobalScope;
import dhbk.android.materialtemplate2.events.AccessTokenFetchedEvent;
import dhbk.android.materialtemplate2.events.AccessTokenFetchingFailedEvent;

import static com.byoutline.ibuscachedfield.util.RetrofitHelper.apiValueProv;

/**
 * Created by Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 31.03.15.
 */
@GlobalScope
public class LoginManager {
    private CachedFieldWithArg<AccessToken, EmailAndPass> accessToken;
    private final AccessTokenProvider accessTokenProvider;

    @Inject
    public LoginManager(KickMaterialService service, AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
        accessToken = new OttoCachedFieldWithArgBuilder<AccessToken, EmailAndPass>()
                .withValueProvider(apiValueProv(service::postGetAccessToken))
                .withSuccessEvent(new AccessTokenFetchedEvent())
                .withResponseErrorEvent(new AccessTokenFetchingFailedEvent())
                .withCustomSessionIdProvider(() -> "") // should be valid between sessions
                .build();
    }

    public void logIn(EmailAndPass emailAndPass) {
        accessToken.postValue(emailAndPass);
    }

    public void logOff() {
        accessTokenProvider.set("");
        accessToken.drop();
    }

    @Subscribe
    public void onAccessTokenFetched(AccessTokenFetchedEvent event) {
        accessTokenProvider.set(event.getResponse().accessToken);
    }
}
