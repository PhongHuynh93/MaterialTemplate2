package dhbk.android.materialtemplate2.dagger;

import com.byoutline.secretsauce.di.AppComponentInterface;

import dagger.Component;
import dhbk.android.materialtemplate2.managers.AccessTokenProvider;

/**
 * Created by huynhducthanhphong on 8/24/16.
 * contains token, font
 */
@AppScope
@Component(modules = AppModule.class)
public interface AppComponent extends AppComponentInterface {
    AccessTokenProvider getAccessTokenProvider();
}