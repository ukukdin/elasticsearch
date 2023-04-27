<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>


<%-- common modal for NFDS::BEGIN --%>
<!--
[공통 모달 사용법]
jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:true}); // 'backdrop:true' 는 모달이 open 되었을 때 검은 배경처리  
-->
<div class="modal fade custom-width" id="commonBlankModalForNFDS">
    <div class="modal-dialog" style="width:60%; top:50px;">
        <div class="modal-content" id="commonBlankContentForNFDS">
            
            <%-- // sample
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Custom Width Modal</h4>
            </div>
            
            <div class="modal-body">
                Any type of width can be applied.
                
            </div>
            
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-info">Save changes</button>
            </div>
             --%>
        </div>
    </div>
</div>
<%-- common modal for NFDS::BEGIN --%>


<div class="modal fade custom-width" data-backdrop="static" id="commonBlankSmallModalForNFDS">
    <div class="modal-dialog" style="width: 40%;top:50px;">
        <div class="modal-content" id="commonBlankSmallContentForNFDS">
            
        </div>
    </div>
</div>

<div class="modal fade custom-width" data-backdrop="static" id="commonBlankSmallModalForNFDSPop">
    <div class="modal-dialog" style="width: 40%;top:10px;">
        <div class="modal-content" id="commonBlankSmallContentForNFDSPop">
            
        </div>
    </div>
</div>


<%-- 공통modal(wide형)::BEGINNING --%>
<div class="modal fade custom-width" id="commonBlankWideModalForNFDS" aria-hidden="false">
    <div class="modal-dialog" style="width:80%; top:50px; margin-top:50.5px;">
        <div class="modal-content">
        </div>
    </div>
</div>
<%-- 공통modal(wide형)::END --%>


<%-- common modal for Ajax Error Info::BEGIN --%>
<!-- Ajax 에러발생시 안내 메시지 출력 모달 (2014.07.16 : scseo) -->
<div class="modal fade" id="commonModalForAjaxErrorInfo" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title">
                    <i class="fa fa-warning" style="color:#CC2424;"></i> 안내메시지
                </h3>
            </div>
            <div class="modal-body" id="modalBodyInCommonModalForAjaxErrorInfo">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-blue" data-dismiss="modal">확인</button>
            </div>
        </div>
    </div>
</div>
<%-- common modal for Ajax Error Info::END --%>



<!--설정 :: 권한그룹관리 -->
<!-- Modal 6 (Long Modal)-->
<div class="modal fade" id="modal-6">
    <div class="modal-dialog">
        <div class="modal-content">
            
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">권한그룹 상세</h4>
            </div>
            
            <div class="modal-body">
            
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="in_group_name" class="control-label">그룹명</label>
                            <input type="text" class="form-control" id="in_group_name" value="">
                        </div>  
                        
                    </div>
                
                </div>
            
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="in_group_comment" class="control-label">그룹설명</label>
                            <input type="text" class="form-control" id="in_group_comment" value="">
                        </div>  
                        
                    </div>
                </div>
           
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-info" id="btn_proc" onclick="f_update_proc();"><span id="txt_dial_auth">저장하기</span></button>
            </div>
        </div>
    </div>
</div>
</div>


<!--설정 :: 권한그룹관리끝 -->



<!-- 설정 :: 사용자관리-->
<div class="modal fade" id="modal-user">
    <div class="modal-dialog">
        <div class="modal-content">
            
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">이용자 신규등록</h4>
            </div>
            
            <form id="f_check" method="post">
            
            <div class="modal-body">
            	<div class="row">
                    <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="in_userId" class="control-label">사용자id</label>
                            <input type="text" class="form-control" id="in_userId" name="in_userId" placeholder="아이디">
                        </div>  
                        
                    </div>
                    
                     <div class="col-md-6">
                        <div class="form-group">
                            <label for="in_userName" class="control-label">사용자명</label>
                            <input type="text" class="form-control" id="in_userName" placeholder="사용자명" style="ime-mode: active;">
                        </div>  
                    </div>
                    
                    
                </div>
                
                <div class="row" id="div_pass">
                    <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="field-1" class="control-label">비밀번호</label>
                            <input type="password" class="form-control" id="in_userPass" placeholder="아이디" autocomplete="off"/>
                        </div>  
                        
                    </div>
                    
                     <div class="col-md-6">
                        <div class="form-group">
                            <label for="field-1" class="control-label">비밀번호확인</label>
                            <input type="password" class="form-control" id="in_userPassChk" placeholder="사용자명" autocomplete="off"/>
                        </div>  
                    </div>
                </div>
                
            
            	<div class="row">
                    <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="field-2" class="control-label">아이디 사용여부</label>
                            
                            <div class="make-switch" data-on="primary" data-off="info">
									<input type="checkbox" id="in_idUseYn" name="in_idUseYn" checked>
							</div>
                        </div>  
                    
                    </div>
                </div>
            
            
            	<div class="row">
            
            	  <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="field-2" class="control-label">권한그룹</label>
                            &nbsp;&nbsp;&nbsp;
                            <SPAN id="disp_group"></SPAN>
                        </div>  
                    
                    </div>
               </div>
                    
            
            	<div class="row">
                    <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="field-1" class="control-label">E-MAIL</label>
                            <input type="text" class="form-control" id="in_userEmail" placeholder="e-mail">
                        </div>  
                        
                    </div>
                    
                    <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="field-2" class="control-label">연락처</label>
                            
                            <input type="text" id="in_tel1" size="4" maxlength="4">-
                            <input type="text" id="in_tel2" size="4" maxlength="4">-
                            <input type="text" id="in_tel3" size="4" maxlength="4">
                        </div>  
                    
                    </div>
                </div>
                
                
                <div class="row">
                    <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="field-1" class="control-label">접속허용IP</label>
                            
                            <input type="text" class="form-control" id="field-1" placeholder="">
                        </div>  
                        
                    </div>
                </div>
            
            	<div class="row">
                    <div class="col-md-6">
                        
                        <div class="form-group">
                            <label for="field-2" class="control-label">접속허용기능 사용여부</label>
                            <div class="make-switch" data-on="primary" data-off="info">
									<input type="checkbox" id="in_userAcpYn" name="in_userAcpYn" checked>
							</div>
                        </div>  
                    
                    </div>
                </div>
            
            </div>
            
            </form>
            
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-info" onclick="f_update_proc();">저장</button>
            </div>
        </div>
    </div>
</div>

<!-- 설정 :: 사용자관리끝-->







