<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
        	return false;
jQuery("#btnExcelDownload").bind("click", function() {
		if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
	jQuery("#startTime").css("display","none");
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
	jQuery("#btnNowMonth").bind("click", function() {
	jQuery("#btnbeforeMonth").bind("click", function() {
});

function executeSearch(){
	if((endDate.getTime()-startDate.getTime()) / (24 * 60 * 60 * 1000) > 30)
    if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리^M
    	return false;
    var defaultOptions = {
function formatDate(Digital) {
function dayZero(date) {

function beforeWeek() {
	  var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek-6); 	//한주전의 시작주간
};
function nowWeek() {
	  Target1_value = formatDate(weekStartDate); 
	  jQuery("#startDateFormatted").val(Target1_value);
// function nowMonth() {
// 	  Target1_value = formatDate(weekStartDate); 
// function beforeMonth() {
// 	  nowYear += (nowYear < 2000) ? 1900 : 0;
// 	  Target1_value = formatDate(weekStartDate); 
// 	  jQuery("#startDateFormatted").val(Target1_value);
<style>