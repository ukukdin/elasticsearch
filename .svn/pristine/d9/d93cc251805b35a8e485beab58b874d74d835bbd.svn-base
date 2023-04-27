<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String result = (String)request.getAttribute("RESULT");
%>
<script type="text/javascript">
// 	jQuery(document).ready(function(){
<%-- 		if('<%=result%>' == "insert_true"){ --%>
// 			bootbox.alert("등록되었습니다.");
<%-- 		}else if('<%=result%>' == "insert_false"){ --%>
// 			bootbox.alert("등록 실패하였습니다.");
<%-- 		}else if('<%=result%>' == "delete_true"){ --%>
// 			bootbox.alert("삭제 되었습니다.");
<%-- 		}else if('<%=result%>' == "delete_false"){ --%>
// 			bootbox.alert("삭제 실패하였습니다.");
<%-- 		}else if('<%=result%>' == "update_true"){ --%>
// 			bootbox.alert("수정되었습니다.", function() {
// 				modalCloseAndReflesh();
// 	        });
<%-- 		}else if('<%=result%>' == "update_false"){ --%>
// 			bootbox.alert("수정 실패하였습니다.");
<%-- 		}else if('<%=result%>' == "exist"){ --%>
// 			bootbox.alert("이미 등록된 사용자입니다.",function(){
// 				return true;
// 				modalCloseAndReflesh();
// 			});
// 		}
// 		modalCloseAndReflesh();
// 	});
</script>
<div class="modal fade custom-width in" data-backdrop="static" id="commonBlankSmallModalForNFDS111" aria-hidden="false" style="display: block;">
    <div class="modal-dialog" style="width:50%;top:50px;margin-top:140.5px;">
        <div class="modal-content" id="commonBlankSmallModalForNFDS">
            <div class="modal-header">
                <a href="javascript:void(0)" onload="modalCloseAndReflesh();"><button type="button" class="close" data-dismiss="modal" aria-hidden="true" >&times;</button></a>
                <h4 class="modal-title">알림</h4>
            </div>
            <div class="col-md-12">
                <div class="panel-body">
                    <div class="form-group">
                        <label for="field-1" class="col-sm-12 control-label">
                        <%if(result.equals("insert_true")){%>
                                                        등록되었습니다.
                        <%}else if(result.equals("insert_false")){%>
                                                        등록 실패하였습니다.
                        <%}else if(result.equals("delete_true")){%>
                                                        삭제되었습니다.
                        <%}else if(result.equals("delete_false")){%>
                                                        삭제 실패하였습니다.
                        <%}else if(result.equals("update_true")){%>
                                                        수정되었습니다.
                        <%}else if(result.equals("update_false")){ %>
                                                        수정 실패하였습니다.
                        <%}else if(result.equals("exist")){%>
                                                        이미 등록된 사용자입니다.
                        <%} %>
                        </label>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0)" onclick="modalCloseAndReflesh();"><button data-bb-handler="ok" type="button" class="btn btn-info">확인</button></a>
            </div>
        </div>
    </div>
</div>

