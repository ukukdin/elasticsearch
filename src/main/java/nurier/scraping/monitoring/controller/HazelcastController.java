package nurier.scraping.monitoring.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HazelcastController {
    
    @RequestMapping("/monitoring/hazelcast/cacheList")
    public ModelAndView hazelcastCacheList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/monitoring/hazelcast/cachelist.tiles");
        return mav;
    }

    @RequestMapping("/monitoring/hazelcast/cacheDetail")
    public ModelAndView hazelcastCacheDetail(@RequestParam Map<String,String> reqParamMap) {
        ModelAndView mav = new ModelAndView();
        
        mav.setViewName("scraping/monitoring/hazelcast/cachedetail_modal");
        mav.addObject("cachename",   StringUtils.defaultIfBlank(reqParamMap.get("cachename"),  "") );
        
        return mav;
    }

}


