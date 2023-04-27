package nurier.scraping.monitoring.controller;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.hazelcast.map.IMap;

import nurier.cache.pof.JsonData;
import nurier.scraping.common.handler.HazelcastHandler;

@Configuration
@EnableWebSocketMessageBroker
@Controller
public class DashboardController extends AbstractWebSocketMessageBrokerConfigurer implements Job {

    //private static final String LOCALHOST = "http://localhost:9080";
    private static String LOCALHOST = null;
    private static SchedulerFactory schedulerFactory = null;
    private static Scheduler scheduler = null;
    private SimpMessagingTemplate messagingTemplate; 
    
    private static String todayDate = null;
    private static String responseRuleDateKeyString = "_responseRuleDate";
    
    @Autowired
    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @RequestMapping("/monitoring/Dashboard/dashboard")
    public ModelAndView dashboard(HttpServletRequest request) {
        if ( LOCALHOST == null ) {
            LOCALHOST = "http://" + request.getServerName() + ":" + request.getServerPort();
            System.out.println(">>>>>>>> " + LOCALHOST);
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/monitoring/dashboard/dashboard.tiles");
        return mav;
    }
    
    /*
     * ################################################################################################
     * ## Dashboard Info
     */

    @RequestMapping("/push/realtimeInfo")
    public @ResponseBody String pushRealtimeInfo() {
        try{
            messagingTemplate.setMessageConverter(new StringMessageConverter());
            messagingTemplate.convertAndSend("/monitoring/realtimeInfo", monitoringResult());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }
    
    @RequestMapping("/push/responseRuleData")
    public @ResponseBody String pushResponseRuleNameCount() {
        try{
            messagingTemplate.setMessageConverter(new StringMessageConverter());
            messagingTemplate.convertAndSend("/monitoring/responseRuleData", responseRuleNameCountResult());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    private String monitoringResult() {
        JsonData json = new JsonData();
        
        json.putJsonData(HazelcastHandler.getMonitoringMap("hardware"));
        json.putJsonData(HazelcastHandler.getMonitoringMap("esper"));
        
        json.putObject("type", "monitoringInfo");
        return json.toJSONString();
    }
    
    private String responseRuleNameCountResult() {
        JsonData json = new JsonData();
        
        JsonData ruleResult_json = new JsonData();
        //ruleResult_json.putJsonData(HazelcastHandler.getMonitoringMap(DateFormat.getNowDate() + responseRuleDateKeyString));
        ruleResult_json.putJsonData(HazelcastHandler.getMonitoringMap(responseRuleDateKeyString));
        
        json.putObject("ruleResult", ruleResult_json);

        JsonData blockingIP_json = new JsonData();
        IMap<String, String> blockingDeviceMap = HazelcastHandler.getBlockingDeviceMap();
        Iterator<String> iter = blockingDeviceMap.keySet().iterator();
        
        String key;
        while(iter.hasNext()) {
            key = iter.next();
            blockingIP_json.put(key, blockingDeviceMap.get(key));
        }

        json.putObject("blockingIP", blockingIP_json);

        
        json.putObject("type", "responseRuleNameCount");
        return json.toJSONString();
    }
    
    /*
     * ################################################################################################
     * ## scheduler
     */
    
    public static void schedulerStart() {
        if ( schedulerFactory == null ) {
            schedulerFactory = new StdSchedulerFactory();
            
            try {
                /**
                 * 
                 */
                JobDetail job = newJob(DashboardController.class).withIdentity("realtimeInfo", "Monitoring").build();
                Trigger trigger = newTrigger()                          //새로운 Trigger
                        .withIdentity("MonitoringTest", "Monitoring")   //이름과 그룹이름 지정
                        //.startNow()                                     //start함
                        .withSchedule(simpleSchedule()                  //스케줄 지정 
                                .withIntervalInSeconds(1)                   //1초마다
                                .repeatForever())                           //계속 반복함
                        .build();     
                /**
                 * 
                 */
                JobDetail responseJob = newJob(DashboardController.class).withIdentity("responseRuleNameCount", "Monitoring").build();
                Trigger responseTrigger = newTrigger()                  //새로운 Trigger
                        .withIdentity("responseRuleNameCount", "Monitoring")    //이름과 그룹이름 지정
                        .startNow()                             //start함
                        .withSchedule(simpleSchedule()          //스케줄 지정 
                            .withIntervalInMinutes(1)           //1분마다
                            //.withIntervalInSeconds(1)           //1초마다
                            .repeatForever())                   //계속 반복함
                        .build();     
                    
                scheduler = schedulerFactory.getScheduler();
                scheduler.scheduleJob(job, trigger);
                scheduler.scheduleJob(responseJob, responseTrigger);
                scheduler.start();
            } catch(Exception e) {
                e.printStackTrace();
            }      
        }
    }
    
    public static void schedulerShutDown() {
        try {
            if (scheduler != null ) {
                scheduler.shutdown();
            }
            
            /*
            if ( schedulerFactory != null ) {
                schedulerFactory.getScheduler().shutdown();
            }
            */
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            if ( LOCALHOST != null ) {
                if ( "MonitoringTest".equals(arg0.getTrigger().getKey().getName())) {
                    callUrlMapping(LOCALHOST + "/push/realtimeInfo");
                } else if ( "responseRuleNameCount".equals(arg0.getTrigger().getKey().getName())) {
                    callUrlMapping(LOCALHOST + "/push/responseRuleData");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void callUrlMapping(String urlString) {
        try {
            HttpURLConnection conn = null; 
            URL url = new URL(urlString);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write("");
            wr.flush();
            
            conn.getInputStream();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /*
     * ################################################################################################
     * ## WebSocketMessageBroker
     */
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/monitoringRecv")
            //.setAllowedOrigins("http://localhost:9080", "http://localhost:8080")
            .withSockJS();        
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/monitoring");
        config.setApplicationDestinationPrefixes("/webSocket");
    }


}