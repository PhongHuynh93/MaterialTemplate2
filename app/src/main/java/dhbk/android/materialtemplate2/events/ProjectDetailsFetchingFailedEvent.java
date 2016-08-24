package dhbk.android.materialtemplate2.events;

import com.byoutline.ottocachedfield.events.ResponseEventWithArgImpl;

import dhbk.android.materialtemplate2.appjava.model.ProjectIdAndSignature;


/**
 * Created by Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 26.03.15.
 */
public class ProjectDetailsFetchingFailedEvent extends ResponseEventWithArgImpl<Exception, ProjectIdAndSignature> {
}