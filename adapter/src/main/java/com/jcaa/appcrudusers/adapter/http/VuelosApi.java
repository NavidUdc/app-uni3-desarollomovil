package com.jcaa.appcrudusers.adapter.http;

import com.jcaa.appcrudusers.adapter.dto.request.CreateVueloRequest;
import com.jcaa.appcrudusers.adapter.dto.request.UpdateVueloRequest;
import com.jcaa.appcrudusers.adapter.dto.response.VueloResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VuelosApi {
    String AUTHORIZATION_HEADER = "Authorization";
    String ID_PATH_PARAMETER = "id";
    String VUELOS_ENDPOINT = "api/vuelos";
    String VUELO_BY_ID_ENDPOINT = "api/vuelos/{id}";

    @POST(VUELOS_ENDPOINT)
    Call<VueloResponse> create(
            @Header(AUTHORIZATION_HEADER) String authorization,
            @Body CreateVueloRequest request);

    @GET(VUELO_BY_ID_ENDPOINT)
    Call<VueloResponse> getById(
            @Header(AUTHORIZATION_HEADER) String authorization,
            @Path(ID_PATH_PARAMETER) String id);

    @GET(VUELOS_ENDPOINT)
    Call<List<VueloResponse>> list(@Header(AUTHORIZATION_HEADER) String authorization);

    @PUT(VUELO_BY_ID_ENDPOINT)
    Call<VueloResponse> update(
            @Header(AUTHORIZATION_HEADER) String authorization,
            @Path(ID_PATH_PARAMETER) String id,
            @Body UpdateVueloRequest request);

    @DELETE(VUELO_BY_ID_ENDPOINT)
    Call<Void> delete(
            @Header(AUTHORIZATION_HEADER) String authorization,
            @Path(ID_PATH_PARAMETER) String id);
}
