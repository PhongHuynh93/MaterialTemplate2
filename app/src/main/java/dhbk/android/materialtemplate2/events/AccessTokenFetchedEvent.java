package dhbk.android.materialtemplate2.events;

import com.byoutline.ottocachedfield.events.ResponseEventWithArgImpl;

import dhbk.android.materialtemplate2.appjava.model.AccessToken;
import dhbk.android.materialtemplate2.appjava.model.EmailAndPass;

/**
 * Created by Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 31.03.15.
 */
public class AccessTokenFetchedEvent extends ResponseEventWithArgImpl<AccessToken, EmailAndPass> {
}
