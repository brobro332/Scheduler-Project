<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
    <input type="text" value="${taskLog.project.id}" id="project_id" hidden>
    <div class="card" style="padding: 12px;">
      <div class="card-body">
      <input type="text" class="form-control" value="${taskLog.id}" id="taskLog_id" hidden>
      <a>/${taskLog.taskCategory}/${taskLog.subTaskCategory}</a>
      <span><h1>${taskLog.title}</h1></span>
          <div style="display:inline-block;">
          <c:set var="createdAt" value="${taskLog.createdAt}" />
            <p><c:out value="${createdAt.year}-${createdAt.monthValue}-${createdAt.dayOfMonth}" /></p>
          </div>
      <hr>
      ${taskLog.content}
      <br/>
      </div>
   </div>

    <br/><br/>

    <button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
    <button class="btn btn" id="btn-delete" style="background-color: #956be8; color: white; width: 201px; float: right;">삭제</button>
    <button class="btn btn" id="btn-updateForm" style="background-color: #956be8; color: white; width: 201px; float: right; margin-right: 2px;">수정</button>

</div>

<script>

    $(document).ready(function() {

        var project_id = $("#project_id").val();
        var taskLog_id = $("#taskLog_id").val();

    	$("#btn-back").on("click", ()=>{

            window.history.back();
        });

        $("#btn-updateForm").on("click", ()=>{

            location.href = "/scheduler/manage/project/" + project_id + "/taskLog/updateForm/" + taskLog_id;
        });

        $("#btn-delete").on("click", ()=>{

            if (confirm("삭제를 진행하시겠습니까?")) {

              $.ajax({
                  type: "DELETE",
                  url: "/api/scheduler/project/taskLog/" + taskLog_id + "?project_id=" + project_id,
                  contentType: "application/json; charset=utf-8",
                  dataType: "json"
              }).done(function(resp) {
                  if(resp.statusCode == 400 || resp.statusCode == 500){
                      alert(resp.message);
                      } else {
                      alert(resp.message);
                      location.href="/scheduler/manage/project/" + project_id;
                  }
              }).fail(function(error) {
                  alert(JSON.stringify(error));
              });
            }
          });
    });
</script>

<%@ include file="../layout/user/footer.jsp"%>