package akaa.com.hearthstonecompanion.rest;

import akaa.com.hearthstonecompanion.model.CardResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CardApiService {
    @Headers("X-Mashape-Key: KFMQNdZO8VmshUT6mSptyA6FjqErp1U8skjjsnid13BjKsIb10")
    @GET("cards")
    Call<CardResponse> getCards();

}
