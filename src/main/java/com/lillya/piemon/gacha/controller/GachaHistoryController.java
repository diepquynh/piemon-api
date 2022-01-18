package com.lillya.piemon.gacha.controller;

import com.lillya.piemon.gacha.service.GachaHistoryService;
import com.lillya.piemon.gacha.service.GachaImportResponse;
import com.lillya.piemon.gacha.model.GachaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<GachaItem> getAllItems() {
        return gachaHistoryService.getAllItems();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/get/{uid}", method = RequestMethod.GET)
    public List<GachaItem> getItemsByUid(@PathVariable("uid") Integer uid) {
        return gachaHistoryService.getItemsByUid(uid);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/get/{uid}/{type}", method = RequestMethod.GET)
    public List<GachaItem> getItemsByUidAndType(@PathVariable("uid") Integer uid, @PathVariable("type") Short gacha_type) {
        return gachaHistoryService.getItemsByUidAndType(uid, gacha_type);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity importGachaHistory(@RequestBody String url)
            throws MalformedURLException, SocketTimeoutException, InterruptedException, AuthException {
        GachaImportResponse ret = gachaHistoryService.importHistory(url);

        return ResponseEntity.ok().body(ret);
    }
}
