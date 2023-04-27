<%--
/***********************************************
 * <pre>
 * 업무구분명 : 
 * 세부업무구분명 : 
 * 작성자 : 
 * 설명 : 설정 > FDS정책설정 > bypass
 * ----------------------------------------------
 * 변경이력
 * ----------------------------------------------
 * NO    날짜              작성자    내용
 *  1                      gslee     초기생성
 * </pre>
 ***********************************************/
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.tangosol.util.extractor.ChainedExtractor"%>
<%@page import="java.math.BigDecimal,java.util.*"%>
<%@page import="com.nonghyup.fds.pof.*,com.tangosol.util.extractor.*"%>
<%@ page import="com.nonghyup.fds.pof.Message" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="com.tangosol.net.CacheFactory" %>
<%@ page import="com.tangosol.net.NamedCache" %>
<%@ page import="com.tangosol.util.*" %>
<%@ page import="com.tangosol.util.filter.*" %>
<%@ page import="java.util.concurrent.*" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>


<%
    String keyvalue = "fdsUsedKey";
    String s1 = "";
    String coment ="";
    try {
        NamedCache cache = CacheFactory.getCache("fds-control-cache");
        s1 = (String)cache.get(keyvalue);
        if(s1.equals("Y")){
            coment = "<button class=\"btn btn-green btn-xs\"> 사용 </button>";
//          coment = "FDS 사용";
        }else{
            coment = "<button class=\"btn btn-red btn-xs\"> 미사용 </button>";
//          coment = "FDS 미사용";
        }
    } catch (NullPointerException e){
        coment = "본서비스는 점검중입니다.";
    } catch (Exception e) {
        coment = "본서비스는 점검중입니다.";
    }
    
    String eskeyvalue = "esConnection";
    String esConnectionState ="";
    String esComent ="";
    try {
        NamedCache cache = CacheFactory.getCache("fds-control-cache");
        esConnectionState = (String)cache.get(eskeyvalue);
        if(esConnectionState.indexOf("19202") > -1){
            esComent = "<button class=\"btn btn-green btn-xs\"> 운영 </button>";
//          esComent = "운영 수집서버";
        }else{
            esComent = "<button class=\"btn btn-red btn-xs\"> 장애 </button>";
//          esComent = "장애대응 수집서버";
        }
    } catch (NullPointerException e){
        esComent = "연결설정이 필요합니다.";
    } catch (Exception e) {
        esComent = "연결설정이 필요합니다.";
    }
    
    String cachekeyvalue = "scoreBackupControl";
    String cacheConnectionState ="";
    String cacheComent ="";
    try {
        NamedCache cache = CacheFactory.getCache("fds-control-cache");
        cacheConnectionState = (String)cache.get(cachekeyvalue);
        if(cacheConnectionState.equals("Y")){
            cacheComent = "<button class=\"btn btn-green btn-xs\"> 사용 </button>";
        }else{
            cacheComent = "<button class=\"btn btn-red btn-xs\"> 미사용 </button>";
        }
    } catch (NullPointerException e){
        cacheComent = "연결설정이 필요합니다.";
    } catch (Exception e) {
        cacheComent = "연결설정이 필요합니다.";
    }
    
    String oepkeyvalue = "oepBackupControl";
    String oepConnectionState ="";
    String oepComent ="";
    try {
        NamedCache cache = CacheFactory.getCache("fds-control-cache");
        oepConnectionState = StringUtils.trimToEmpty((String)cache.get(oepkeyvalue));
        if(oepConnectionState.indexOf("backup_copy_of_oep_") > -1){
            oepComent = "<button class=\"btn btn-red btn-xs\"> 장애 </button>";
        }else{
            oepComent = "<button class=\"btn btn-green btn-xs\"> 운영 </button>";
        }
    } catch (NullPointerException e){
        oepComent = "연결설정이 필요합니다.";
    } catch (Exception e) {
        oepComent = "연결설정이 필요합니다.";
    }
    
    String fdsSynckeyvalue = "fdsSyncUseYn";
    String fdsSyncConnectionState ="";
    String fdsSyncComent ="";
    try {
        NamedCache cache = CacheFactory.getCache("fds-control-cache");
        fdsSyncConnectionState = StringUtils.trimToEmpty((String)cache.get(fdsSynckeyvalue));
        if("N".equals(fdsSyncConnectionState)){
//             fdsSyncComent = "<button class=\"btn btn-green btn-xs\"> ASync 사용 </button>";
            fdsSyncComent = "<button class=\"btn btn-green btn-xs\"> 비동기방식 </button>";
        } else {
//             fdsSyncComent = "<button class=\"btn btn-green btn-xs\"> Sync 사용 </button>";
            fdsSyncComent = "<button class=\"btn btn-green btn-xs\"> 동기방식 </button>";
        }
    } catch (NullPointerException e){
        fdsSyncComent = "연결설정이 필요합니다.";
    } catch (Exception e) {
        fdsSyncComent = "연결설정이 필요합니다.";
    }
    
//      cache.put(keyvalue, "Y");

%>


<script type="text/javascript">
jQuery(document).ready(function($) {
    jQuery("#fdsUse_Yn").val("<%=s1 %>");
    jQuery("#esConnection_Yn").val("<%=esConnectionState %>");
    jQuery("#cacheStoreUse_Yn").val("<%=cacheConnectionState %>");
    jQuery("#oepCacheStoreUse_Yn").val("<%=oepConnectionState %>");
    jQuery("#fdsSyncUse_Yn").val("<%=fdsSyncConnectionState %>");
});

<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
function smallContentAjaxSubmit(url, form) { 
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDSPop",
        success      : function() {
            jQuery('#commonBlankSmallModalForNFDSPop').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function cohreStateOn(){
    
    if(jQuery("#fdsUse_Yn").val() == "Y"){
        bootbox.alert("실행중입니다");
        return;
    }
    bootbox.confirm("시작하시겠습니까?", function(result) {
        if(result == true){
            jQuery("#fdsUse_Yn").val("Y");
            smallContentAjaxSubmit('/servlet/setting/fds_policy_managment/setFdscontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
    });

}

function cohreStateOff(){
        
        if(jQuery("#fdsUse_Yn").val() == "N"){
            bootbox.alert("미사용상태입니다");
            return;
        }
        bootbox.confirm("변경하시겠습니까?", function(result) {
    
        if(result == true){
        jQuery("#fdsUse_Yn").val("N");
            smallContentAjaxSubmit('/servlet/setting/fds_policy_managment/setFdscontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
        });
}

function esConnectionStateOn(){
	var esCheck = jQuery("#esConnection_Yn").val();
    if(esCheck.indexOf("19202") > -1){
        bootbox.alert("운영서버 접속중입니다.");
        return;
    }
    bootbox.confirm("변경하시겠습니까?", function(result) {
        if(result == true){
            jQuery("#esConnection_Yn").val("operation");
            smallContentAjaxSubmit('/servlet/nfds/engine/setEsConnectionControl.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
    });

}

function esConnectionStateOff(){
        var esCheck = jQuery("#esConnection_Yn").val();
		if(esCheck.indexOf("19300") > -1){
            bootbox.alert("장애대응서버로 접속중입니다.");
            return;
        }
        bootbox.confirm("변경하시겠습니까?", function(result) {
    
        if(result == true){
            jQuery("#esConnection_Yn").val("accident");
            smallContentAjaxSubmit('/servlet/nfds/engine/setEsConnectionControl.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
        });
}

function cacheStoreOn(){
    
    if(jQuery("#cacheStoreUse_Yn").val() == "Y"){
        bootbox.alert("사용중입니다");
        return;
    }
    bootbox.confirm("사용하시겠습니까?", function(result) {
        if(result == true){
            jQuery("#cacheStoreUse_Yn").val("Y");
            smallContentAjaxSubmit('/servlet/nfds/engine/setCohcontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
    });

}

function cacheStoreOff(){
        
        if(jQuery("#cacheStoreUse_Yn").val() == "N"){
            bootbox.alert("사용중지입니다");
            return;
        }
        bootbox.confirm("사용중지하시겠습니까?", function(result) {
    
        if(result == true){
            jQuery("#cacheStoreUse_Yn").val("N");
            smallContentAjaxSubmit('/servlet/nfds/engine/setCohcontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
        });
}

function oepCacheStoreOn(){
    
    if(jQuery("#oepCacheStoreUse_Yn").val() != ""){
        bootbox.alert("장애대응서버로 접속중입니다.");
        return;
    }
    bootbox.confirm("변경하시겠습니까?", function(result) {
        if(result == true){
            jQuery("#oepCacheStoreUse_Yn").val("Y");
            smallContentAjaxSubmit('/servlet/nfds/engine/setOepCohcontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
    });

}

function oepCacheStoreOff(){
        
        if(jQuery("#oepCacheStoreUse_Yn").val() == ""){
            bootbox.alert("운영서버 접속중입니다.");
            return;
        }
        bootbox.confirm("변경하시겠습니까?", function(result) {
    
        if(result == true){
            jQuery("#oepCacheStoreUse_Yn").val("");
            smallContentAjaxSubmit('/servlet/nfds/engine/setOepCohcontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
        });
}

function fdsSyncOn(){
    
    if(jQuery("#fdsSyncUse_Yn").val() != "N"){
        bootbox.alert("FDS에서 Sync를 사용 중입니다");
        return;
    }
    bootbox.confirm("FDS에서 Sync를 사용하시겠습니까?", function(result) {
        if(result == true){
            jQuery("#fdsSyncUse_Yn").val("Y");
            smallContentAjaxSubmit('/servlet/nfds/engine/setFdsSyncCohcontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
    });

}

function fdsSyncOff(){
        
        if(jQuery("#fdsSyncUse_Yn").val() == "N"){
            bootbox.alert("FDS에서 ASync를 사용 중입니다");
            return;
        }
        bootbox.confirm("FDS에서 ASync를 사용하시겠습니까?", function(result) {
    
        if(result == true){
            jQuery("#fdsSyncUse_Yn").val("N");
            smallContentAjaxSubmit('/servlet/nfds/engine/setFdsSyncCohcontrolState.fds', 'control_d');
            location.href = "/servlet/setting/fds_policy_managment/setFdscontrol.fds";
        }else{
            bootbox.alert("취소되었습니다.");
        }
        });
}




<% } // end of [if] - 'admin' 그룹만 실행가능 %>
</script>

<div class="contents-body">
<div class="col-md-6">
		<h3 class="fl">정책설정</h3>
	</div>
    <div id="contents-table" class="contents-table" >
        <table class="table tile table-hover table-bordered datatable" id="tableData">
            <colgroup>
                <col width="35%">
                <col width="25%">
                <col width="40%">
            </colgroup>
            <thead>
                <tr>
                    <th>서비스명 </th>
                    <th>서비스상태</th>
                    <th>상태변경</th>
                    </tr>
            </thead>
            <tbody>
                    <tr>
                        <td>FDS 사용여부</td>
                        <td><%=coment%></td>
                        <td>
                            <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
                                <div><a href="javascript:cohreStateOn();">사용</a> | <a href="javascript:cohreStateOff();">미사용</a></div>
                            <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                        </td>
                    </tr>
                    <tr>
                        <td>스토어캐쉬 사용여부 </td>
                        <td><%=cacheComent%></td>
                        <td>
                            <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
                                <div><a href="javascript:cacheStoreOn();">사용</a> | <a href="javascript:cacheStoreOff();">미사용</a></div>
                            <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                        </td>
                    </tr>
                    <tr>
                        <td>FDS Sync 사용여부</td>
                        <td><%=fdsSyncComent%></td>
                        <td>
                            <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<!--                                 <div><a href="javascript:fdsSyncOn();">Sync 사용</a> | <a href="javascript:fdsSyncOff();">ASync 사용</a></div> -->
                                <div><a href="javascript:fdsSyncOn();">동기방식</a> | <a href="javascript:fdsSyncOff();">비동기방식</a></div>
                            <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                        </td>
                    </tr>
            </tbody>
        </table>
    </div>
    <div>
    <div class="col-md-6" style="margin-top:50px">
		<h3 class="fl">장애대응</h3>
	</div>
	    <table class="table tile table-hover table-bordered datatable" id="tableData">
	            <colgroup>
	                <col width="35%">
	                <col width="25%">
	                <col width="40%">
	            </colgroup>
	            <thead>
	                <tr>
	                    <th>서비스명 </th>
	                    <th>서비스상태</th>
	                    <th>상태변경</th>
	                    </tr>
	            </thead>
	       		 <tr>
	                 <td>elasticsearch 장애대응</td>
	                 <td><%=esComent%></td>
	                 <td>
	                     <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	                         <div><a href="javascript:esConnectionStateOn();">운영</a> | <a href="javascript:esConnectionStateOff();">장애대응</a></div>
	                     <% } // end of [if] - 'admin' 그룹만 실행가능 %>
	                 </td>
	             </tr>
	             <tr>
	                 <td>탐지서버 장애대응</td>
	                 <td><%=oepComent%></td>
	                 <td>
	                     <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	                         <div><a href="javascript:oepCacheStoreOff();">운영</a> | <a href="javascript:oepCacheStoreOn();">장애대응</a></div>
	                     <% } // end of [if] - 'admin' 그룹만 실행가능 %>
	                 </td>
	             </tr>
	        </table>
    </div>
                        <form name="control_d" id="control_d" role="form" >   
                            <input type="hidden" id="fdsUse_Yn"   name="fdsUse_Yn"  value="" />
                            <input type="hidden" id="esConnection_Yn"   name="esConnection_Yn"  value="" />
                            <input type="hidden" id="cacheStoreUse_Yn"   name="cacheStoreUse_Yn"  value="" />
                            <input type="hidden" id="oepCacheStoreUse_Yn"   name="oepCacheStoreUse_Yn"  value="" />
                            <input type="hidden" id="fdsSyncUse_Yn"   name="fdsSyncUse_Yn"  value="" />
                       </form>
                       
</div>



