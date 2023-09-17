<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
    <input type="text" class="form-control" value="${taskLog.id}" id="taskLog_id" hidden>
    <input type="text" class="form-control" value="${project.id}" id="project_id" hidden>
    <div>
    <br/>
    <form method="post">
      <span><h4>업무 일지 수정</h4></span>
      <p style="color: gray;"><small>업무 일지를 수정하는 페이지입니다</small></p>
      <hr><br/>

        <div>
            <select id="task" name="task">
                <option disabled selected>업무 카테고리 선택</option>
                <c:forEach items="${project.tasks}" var="task">
                    <option value="${task.id}" data-task="${task.task}">${task.task}</option>
                </c:forEach>
            </select>

            <select id="subTask" name="subTask">
              <option disabled selected>하위업무 카테고리 선택</option>
            </select>

            &nbsp;&nbsp;&nbsp;
            <div style="position: relative; display: inline-block; border-left: 1px solid gray; height: 20px; top: 4px;" class="vertical-line"></div>
            &nbsp;&nbsp;&nbsp;

            <p style="display: inline-block; color: gray;">기존 카테고리</p>&nbsp;
            <p style="display: inline-block; color: gray; border: 1px solid gray; border-radius: 5px;" id="categoryNotice"></p>
      </div><br/>
      <input type="text" class="form-control" placeholder="제목" id="title" style="width:100%;" value="${taskLog.title}"> <br/>
      <textarea id="summernote" name="content">${taskLog.content}</textarea> <br/>
    </form>
    <button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
    <button class="btn btn" id="btn-updateTaskLog" style="background-color: #956be8; color: white; width: 201px; float:right;">등록</button>
    </div>


    </div>
</div>

<script>
var taskLog_id = $("#taskLog_id").val();

$.ajax({
    url: "/api/scheduler/project/taskLog/categories",
    type: "GET",
    data: { taskLog_id: taskLog_id },
    dataType: "json",
    success: function(response) {
        var data = response;
        var element = document.getElementById("categoryNotice");

        element.innerText = "/" + data.taskCategory + "/" + data.subTaskCategory;
    },
    error: function(error) {
        alert(JSON.stringify(error));
    }
});
</script>
<script src="/js/scheduler.js"></script>

<%@ include file="../layout/user/footer.jsp"%>