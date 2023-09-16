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
    <button class="btn btn" id="btn-update" style="background-color: #956be8; color: white; width: 201px; float:right;">등록</button>
    </div>


    </div>
</div>

<script>

    $(document).ready(function() {

        var taskLog_id = $("#taskLog_id").val();

    	$("#btn-back").on("click", ()=>{

            window.history.back();
        });

        $("#btn-updateForm").on("click", ()=>{

            var taskLog_id = $("#taskLog_id").val();

            location.href = "/scheduler/manager/project/taskLog/updateForm/" + taskLog_id;
        });


      $('#summernote').summernote({
          height: 500,
          minHeight: null,
          maxHeight: null,
          focus: true,
          lang: "ko-KR",
          callbacks: {
              onImageUpload: function(files, editor, welEditable){
                  for (var i = files.length - 1; i >= 0; i--) {
                      uploadSummernoteImageFile(files[i], this);
                  }
               }
             }
        });

      	function uploadSummernoteImageFile(file, el) {
           data = new FormData();
           data.append("file", file);

           $.ajax({
              data: data,
              type: "POST",
              url: "/api/community/postImg/upload",
              cache: false,
              contentType: false,
              processData: false,
              enctype : 'multipart/form-data',
              success: function(data){
                  $(el).summernote('editor.insertImage', data);
               }
            });
          }

      $("#task").change(function () {
          var taskId = $(this).val();
          var subTaskSelect = $("#subTask");

          // 업무 선택에 따라 세부 업무 옵션을 동적으로 업데이트
          $.ajax({
              type: "GET",
              url: "/scheduler/manage/subTask",
              data: { task_id: taskId },
              dataType: "json",
              success: function (data) {

                  // 받아온 세부 업무 목록을 옵션으로 추가
                  $.each(data, function (index, subTask) {
                      subTaskSelect.append(new Option(subTask.name, subTask.name));
                  });

                  if (data.length === 0) {
                      subTaskSelect.append(new Option("-", ""));
                  }
              }
          });
      });

      // 세부 업무 선택 셀렉트 박스 변경 이벤트 처리
      $("#subTask").change(function () {
          var selectedValue = $(this).val();
          if (selectedValue === "direct") {
              $("#customSubTask").css("display", "inline-block");
          } else {
              $("#customSubTask").hide();
          }
      });

      $("#btn-update").click(function() {

          var project_id = $("#project_id").val();

          var title = $("#title").val();
          var content = $("textarea[name = content]").val();

          if(title == '') {

              alert("제목을 입력해주세요.");
              return false;
          }

          if(content == '') {

              alert("내용을 입력해주세요.");
              return false;
          }

          var taskSelectBox = document.getElementById("task");
          var taskSelectedIndex = taskSelectBox.selectedIndex;
          var taskSelectedValue = taskSelectBox.options[taskSelectedIndex].getAttribute("data-task");

          if (taskSelectedValue === null) {

              alert("업무 카테고리를 선택해주세요.");

              return;
          }

          var subTaskSelectBox = document.getElementById("subTask");
          var subTaskSelectedIndex = subTaskSelectBox.selectedIndex;
          var subTaskSelectedValue = subTaskSelectBox.options[subTaskSelectedIndex].value;

          if (subTaskSelectedValue === "하위업무 카테고리 선택") {

              alert("하위업무 카테고리를 선택해주세요.");

              return;
          }

          let data = {

                  title: title,
                  content: content,
                  taskCategory: taskSelectedValue,
                  subTaskCategory: subTaskSelectedValue
          };

          $.ajax({
              type: "PUT",
              url: "/api/scheduler/project/taskLog/" + taskLog_id,
              data: JSON.stringify(data),
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
      });

      $.ajax({
            url: "/api/scheduler/project/taskLog/category",
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
  });
</script>

<%@ include file="../layout/user/footer.jsp"%>