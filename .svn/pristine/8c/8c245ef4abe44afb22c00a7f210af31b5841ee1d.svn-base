<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<script type="text/javascript">


<c:if test="${result eq 'insert_true'}">
//bootbox.alert("Insert Success ");
</c:if>

<c:if test="${result eq 'insert_false'}">
//bootbox.alert("Insert Failed ");
</c:if>

<c:if test="${result eq 'update_true'}">
//alert("Update Success ");
//bootbox.alert("Update Success ");
</c:if>

<c:if test="${result eq 'update_false'}">
//bootbox.alert("Update Failed ");
</c:if>

  
</script>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">알림</h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <div class="form-group">
            <label for="field-1" class="col-sm-12 control-label-action">
                <c:if test="${result eq 'insert_true'}">
                    저장되었습니다.
                </c:if>
                
                <c:if test="${result eq 'insert_false'}">
                    저장 실패하였습니다.
                </c:if>
                
                <c:if test="${result eq 'update_true'}">
                    수정되었습니다.
                </c:if>
                
                <c:if test="${result eq 'update_false'}">
                    수정 실패하였습니다.
                </c:if>
                
                <c:if test="${result eq 'duplicate'}">
                    코드가 이미 존재합니다.
                </c:if>

                <c:if test="${result eq 'delete_true'}">
                    삭제되었습니다.
                </c:if>

                <c:if test="${result eq 'delete_false'}">
                    삭제 실패하였습니다.
                </c:if>

                <c:if test="${result eq 'insert_false_seqNum_duplication'}">
                    저장 실패하였습니다. (SEQ_NUM 값에 중복된 값이 있습니다.)
                </c:if>

                <c:if test="${result eq 'insert_false_seqNum_null'}">
                    저장 실패하였습니다. (SEQ_NUM에 빈값이 있습니다.)
                </c:if>

                <c:if test="${result eq 'insert_false_code_no_null'}">
                    저장 실패하였습니다. (CODE_NO에 빈값이 있습니다.)
                </c:if>

                <c:if test="${result eq 'insert_false_code_null'}">
                    저장 실패하였습니다. (CODE에 빈값이 있습니다.)
                </c:if>

                <c:if test="${result eq 'insert_false_text1_null'}">
                    저장 실패하였습니다. (TEXT1에 빈값이 있습니다.)
                </c:if>
            </label>
        </div>
    </div>
</div>

<div class="modal-footer">
    <a href="javascript:void(0)" onclick="modalCloseAndReflesh();"><button data-bb-handler="ok" type="button" class="btn btn-info">확인</button></a>
</div>

