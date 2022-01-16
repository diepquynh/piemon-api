package com.lillya.piemon.gacha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;

@RestController
@RequestMapping("api/v1/gachaHistory")
public class GachaHistoryController {

    @Autowired
    private GachaHistoryService gachaHistoryService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<GachaItem> getAllItems() {
        return gachaHistoryService.getAllItems();
    }

    @RequestMapping(value = "/get/{uid}", method = RequestMethod.GET)
    public List<GachaItem> getItemsByUid(@PathVariable("uid") Integer uid) {
        return gachaHistoryService.getItemsByUid(uid);
    }

    @RequestMapping(value = "/get/{uid}/{type}", method = RequestMethod.GET)
    public List<GachaItem> getItemsByUidAndType(@PathVariable("uid") Integer uid, @PathVariable("type") Short gacha_type) {
        return gachaHistoryService.getItemsByUidAndType(uid, gacha_type);
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity importGachaHistory(@RequestBody String url)
            throws MalformedURLException, SocketTimeoutException, InterruptedException, AuthException {
        GachaImportResponse ret = gachaHistoryService.importHistory(url);

        return ResponseEntity.ok().body(ret);
    }
}
