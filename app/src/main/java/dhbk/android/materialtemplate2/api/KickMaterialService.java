package dhbk.android.materialtemplate2.api;

import java.util.List;
import java.util.Map;

import dhbk.android.materialtemplate2.appjava.model.AccessToken;
import dhbk.android.materialtemplate2.appjava.model.Category;
import dhbk.android.materialtemplate2.appjava.model.DiscoverResponse;
import dhbk.android.materialtemplate2.appjava.model.EmailAndPass;
import dhbk.android.materialtemplate2.appjava.model.ProjectDetails;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by huynhducthanhphong on 8/24/16.
 */
public interface KickMaterialService {

    @GET("v1/categories")
    Call<List<Category>> getCategories();

    @GET("v1/discover")
    Call<DiscoverResponse> getDiscover(@QueryMap Map<String, String> parameters);

    @GET("v1/projects/{project_id}")
    Call<ProjectDetails> getProjectDetails(@Path("project_id") int projectId, @QueryMap Map<String, String> parameters);

    @POST("xauth/access_token")
    Call<AccessToken> postGetAccessToken(@Body EmailAndPass emailAndPass);
}
