<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">

    <input type="text" value="${project_id}" id="project_id" hidden>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs">
      <li class="nav-item active">
        <a class="nav-link" data-toggle="tab" href="#outline">개요</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#manageTask">업무 수행 관리</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#dailyTask">일일 수행 기록</a>
      </li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">

      <div class="tab-pane container active" id="outline">

      <br/>
      <span><h4>${project.title}</h4></span>
      <p style="color: gray;"><small>${project.startPRJ} ~ ${project.endPRJ}</small></p>

      <hr><br/>

      <!-- 프로젝트 D-Day -->
      <div style="display: inline-block;">
        <span class="badge bg" style="background-color: #956be8; color: white;">
            <h6>프로젝트 D-DAY</h6>
        </span>
      &nbsp;
      ${d_day}
      </div>

      <br/><br/>

      <!-- 업무 수행률 -->
      <div>
          <div style="position: absolute; display: inline-block;">
            <span class="badge bg " style="background-color: #956be8; color: white; display: inline-block;">
                <h6>업무 수행률&nbsp;</h6>
            </span>
          </div>

        &nbsp;

          <div style="position: relative; display: inline-block; left: 8%;">
              <c:forEach items="${project.tasks}" var="task" varStatus="loop">
                  <div>
                  <c:if test="${!loop.first}">
                      <hr style="border-top: 4px dashed #d3d3d3;">
                  </c:if>
                     <h5>${task.task}</h5>
                  </div>
                  <h6><a style="display: inline-block;"> ◾ <kbd id="kbd">${task.taskPercentage}%</kbd> 하위업무 ${task.totalSubTasks}개 중 ${task.checkedSubTasks}개 완료</a></h6>
              </c:forEach>
          </div>
      </div>

    <br/><br/>

    <button type="button" class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>

    </div>


    <div class="tab-pane container active fade" id="manageTask">

          <br/>
          <span><h4>${project.title}</h4></span>
          <p style="color: gray;"><small>${project.startPRJ} ~ ${project.endPRJ}</small></p>

          <hr><br/>

          <div>
          <div style="position: absolute; display: inline-block;">
             <span class="badge bg " style="background-color: #956be8; color: white; display: inline-block;">
                    <h6>업무 리스트&nbsp;</h6>
                </span>
              </div>

            &nbsp;

              <div style="position: relative; display: inline-block; left: 10%; width: 35%">
                  <c:forEach items="${project.tasks}" var="task" varStatus="loop">
                         <div>
                         <c:if test="${!loop.first}">
                             <hr style="border-top: 4px dashed #d3d3d3;">
                         </c:if>
                         <c:choose>
                             <c:when test="${task.check_yn == 'Y'}">
                                 <label class="custom-checkbox"><h5 style="display: inline-block;">${task.task}&nbsp;&nbsp;<input type="checkbox" class="task-checkbox dynamicCheckbox" data-task-id="${task.id}" data-task-type="task" checked="checked">
                                    <span class="checkmark"></span></h5>
                                 </label>
                             </c:when>
                             <c:otherwise>
                                 <label class="custom-checkbox"><h5 style="display: inline-block;">${task.task}&nbsp;&nbsp;<input type="checkbox" class="task-checkbox dynamicCheckbox" data-task-id="${task.id}" data-task-type="task">
                                    <span class="checkmark"></span></h5>
                                 </label>
                             </c:otherwise>
                         </c:choose>
                      </div>
                  <c:forEach items="${task.subTasks}" var="subTask">
                      <div>
                      <c:choose>
                          <c:when test="${subTask.check_yn == 'Y'}">
                              <label class="custom-checkbox">&nbsp;&nbsp;&nbsp; ◾ <h6 style="display: inline-block;">${subTask.name}&nbsp;&nbsp;<input type="checkbox" class="subTask-checkbox dynamicCheckbox" data-task-id="${subTask.id}" data-uppertask-id="${task.id}" data-task-type="subTask" checked="checked">
                                 <span class="checkmark"></span></h6>
                              </label>
                          </c:when>
                          <c:otherwise>
                              <label class="custom-checkbox">&nbsp;&nbsp;&nbsp; ◾ <h6 style="display: inline-block;">${subTask.name}&nbsp;&nbsp;<input type="checkbox" class="subTask-checkbox dynamicCheckbox" data-task-id="${subTask.id}" data-uppertask-id="${task.id}" data-task-type="subTask">
                                 <span class="checkmark"></span></h6>
                              </label>
                          </c:otherwise>
                      </c:choose>
                      </div>
                  </c:forEach>
                  </c:forEach>
              </div>
          </div>

        <br/><br/>

        <button type="button" class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
        <button type="button" class="btn btn" id="btn-set" style="float: right; background-color: #956be8; color: white; width: 201px;">등록</button>

    </div>


    <div class="tab-pane container active fade" id="dailyTask">
    </div>


    </div>
</div>


<script>
$(document).ready(function() {
     $('#outline').show(); // outline 탭 콘텐츠 표시
     $('#manageTask').hide(); // manageTask 탭 콘텐츠 숨김
     $('#dailyTask').hide(); // dailyTask 탭 콘텐츠 숨김

     $(".task-checkbox").click(function() {
         var isChecked = $(this).prop("checked");
         var taskId = $(this).data("task-id");
         var taskType = $(this).data("task-type");

         if (taskType === "task") {
             // 업무 체크박스 처리
             // 해당 업무의 하위 업무 체크박스 선택 여부 변경
             $(".subTask-checkbox[data-uppertask-id='" + taskId + "']").prop("checked", isChecked);
         }
     });

    $(document).on("click", ".subTask-checkbox", function() {

        var upperTaskId = $(this).data("uppertask-id");
        console.log(upperTaskId);

        // 해당 상위 업무 ID와 연결된 모든 하위 업무 체크박스를 가져옴
        var subTaskCheckboxes = $(".subTask-checkbox[data-uppertask-id='" + upperTaskId + "']");

        // 모든 하위 업무 체크박스의 선택 여부가 true라면 상위 업무 체크박스도 체크
        var allSubTasksChecked = subTaskCheckboxes.length === subTaskCheckboxes.filter(":checked").length;

        console.log(subTaskCheckboxes.length);
        console.log(subTaskCheckboxes.filter(":checked").length);
        console.log(allSubTasksChecked);

        $(".task-checkbox[data-task-id='" + upperTaskId + "']").prop("checked", allSubTasksChecked);
    });

    $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {

      var target = $(e.target).attr("href");

      if (target === "#outline") {

         e.preventDefault(); // 링크 클릭 동작을 중지

         $('#outline').show(); // outline 탭 콘텐츠 표시
         $('#manageTask').hide(); // manageTask 탭 콘텐츠 숨김
         $('#dailyTask').hide(); // dailyTask 탭 콘텐츠 숨김
        // 개요 탭이 선택되었을 때 실행할 코드 추가
      } else if (target === "#manageTask") {

       e.preventDefault(); // 링크 클릭 동작을 중지

       $('#outline').hide(); // outline 탭 콘텐츠 숨김
       $('#manageTask').show(); // manageTask 탭 콘텐츠 표시
       $('#dailyTask').hide(); // dailyTask 탭 콘텐츠 숨김
        // 관리 탭이 선택되었을 때 실행할 코드 추가
      } else if (target === "#dailyTask") {

       e.preventDefault(); // 링크 클릭 동작을 중지

       $('#outline').hide(); // outline 탭 콘텐츠 숨김
       $('#manageTask').hide(); // manageTask 탭 콘텐츠 숨김
       $('#dailyTask').show(); // dailyTask 탭 콘텐츠 표시
        // 일일 탭이 선택되었을 때 실행할 코드 추가
      }
    });

      $("#btn-set").click(function() {
          var project_id = $("#project_id").val();
          var data = { taskIds: [], taskTypes: [], checkYnList: [] }; // 체크박스 상태 업데이트 정보를 저장할 배열

          // 모든 체크박스를 순회하며 상태를 수집
          $(".dynamicCheckbox").each(function() {
              var taskId = $(this).data("task-id");
              var isChecked = $(this).prop("checked");
              var taskType = $(this).data("task-type");
              var check_yn = isChecked ? "Y" : "N";

              data.taskIds.push(taskId);
              data.taskTypes.push(taskType);
              data.checkYnList.push(check_yn);
          });

          // 서버로 업데이트 정보 배열을 전송
          $.ajax({
              type: "POST",
              url: "/api/scheduler/task/update/checkStatus",
              data: JSON.stringify(data),
              contentType: "application/json; charset=utf-8",
              dataType: "json",
              success: function(response) {
                  // 성공 시 실행할 코드
                  alert("체크박스 상태가 업데이트되었습니다.");
                  location.href="/scheduler/manage/project/" + project_id;
              },
              error: function(error) {
                  // 오류 시 실행할 코드
                  alert("오류가 발생하였습니다.");
              }
          });
      });
  });
</script>

<%@ include file="../layout/user/footer.jsp"%>