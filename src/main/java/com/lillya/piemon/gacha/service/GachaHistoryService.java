package com.lillya.piemon.gacha.service;

import com.lillya.piemon.gacha.data.GachaItemRepository;
import com.lillya.piemon.gacha.model.GachaItem;
import com.lillya.piemon.gacha.model.GachaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.security.auth.message.AuthException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GachaHistoryService {

    @Autowired
    private GachaItemRepository gachaItemRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    private List<GachaType> getTypes() {
        return new ArrayList<>() {
            {
                add(new GachaType(100, "Beginners' Wish"));
                add(new GachaType(200, "Standard"));
                add(new GachaType(301, "Character Event"));
                add(new GachaType(302, "Weapon Event"));
            }
        };
    }

    public List<GachaItem> getAllItems() {
        return gachaItemRepository.findAll();
    }

    public List<GachaItem> getItemsByUid(Integer uid) {
        return gachaItemRepository.findAllByUid(uid);
    }

    public List<GachaItem> getItemsByUidAndType(Integer uid, Short gacha_type) {
        return gachaItemRepository.findAllByUidAndGachaType(uid, gacha_type);
    }

    /*
     * Types of errors:
     * - Authkey error:
     * {
     *      "data": null,
     *      "message": "authkey error",
     *      "retcode": -100
     * }
     */
    public GachaImportResponse importHistory(String url)
            throws MalformedURLException, SocketTimeoutException, InterruptedException, AuthException {
        UriComponentsBuilder builder =
                        UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("hk4e-api-os.mihoyo.com")
                        .path("event/gacha_info/api/getGachaLog");

        String msg = "";
        int retcode = 0;
        int page = 1;
        long end_id = 0;
        URL this_url = new URL(url);
        String query = this_url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            builder.queryParam(pair.substring(0, idx), pair.substring(idx + 1));
        }
        builder.replaceQueryParam("size", "20");
        builder.replaceQueryParam("init_type", "301");

        for (GachaType type : getTypes()) {
            page = 1;
            end_id = 0;
            builder.replaceQueryParam("page", String.valueOf(page));
            builder.replaceQueryParam("end_id", String.valueOf(end_id));
            builder.replaceQueryParam("gacha_type", String.valueOf(type.getId()));

            GachaHistoryResponse response =
                    restTemplate.getForObject(builder.build(true).toUri(), GachaHistoryResponse.class);

            if (response == null)
                throw new SocketTimeoutException("Request timeout!");

            retcode = response.getRetcode();
            msg = response.getMessage();

            while (response.getData() != null && response.getData().getList().size() > 0) {
                response.getData().getList().forEach(item -> gachaItemRepository.save(item));

                page++;
                end_id = response.getData().getList().get(response.getData().getList().size() - 1).getId();
                builder.replaceQueryParam("page", String.valueOf(page));
                builder.replaceQueryParam("end_id", String.valueOf(end_id));
                response = restTemplate.getForObject(builder.build(true).toUri(),
                                GachaHistoryResponse.class);
                Thread.sleep(1000);
            }
            Thread.sleep(2000);
        }

        return new GachaImportResponse(retcode, msg);
    }
}
