package dhbk.android.materialtemplate2;

import android.support.annotation.VisibleForTesting;

import com.byoutline.androidstubserver.AndroidStubServer;
import com.byoutline.ibuscachedfield.util.RetrofitHelper;
import com.byoutline.mockserver.NetworkType;
import com.byoutline.secretsauce.BaseApp;
import com.byoutline.secretsauce.utils.ViewUtils;
import com.squareup.otto.Bus;

import dhbk.android.materialtemplate2.dagger.AppComponent;
import dhbk.android.materialtemplate2.dagger.AppModule;
import dhbk.android.materialtemplate2.dagger.DaggerAppComponent;
import dhbk.android.materialtemplate2.dagger.DaggerGlobalComponent;
import dhbk.android.materialtemplate2.dagger.GlobalComponent;
import dhbk.android.materialtemplate2.dagger.GlobalModule;
import dhbk.android.materialtemplate2.managers.AccessTokenProvider;
import timber.log.Timber;

/**
 * Created by huynhducthanhphong on 8/24/16.
 */
public class KickMaterialApp extends BaseApp {
    public static GlobalComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        // fixme - add timber here
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

//       fixme - declare http server
        AndroidStubServer.start(this, NetworkType.UMTS);
        RetrofitHelper.MSG_DISPLAYER = msg -> ViewUtils.showToast(msg, true);
        resetComponents();
    }

    public void resetComponents() {
        AppComponent appComponent = createAppComponent();
        GlobalComponent mainComponent = createGlobalComponent(appComponent.getBus(), appComponent.getAccessTokenProvider());
        setComponents(mainComponent, appComponent);
    }

    @Override
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    private GlobalComponent createGlobalComponent(Bus bus, AccessTokenProvider accessTokenProvider) {
        return DaggerGlobalComponent.builder()
                .globalModule(new GlobalModule(this, bus, accessTokenProvider))
                .build();
    }

    private AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @VisibleForTesting
    public synchronized void setComponents(GlobalComponent mainComponent, AppComponent appComponent) {
        component = mainComponent;
        init(appComponent);
//        component.inject(this);
    }
}
