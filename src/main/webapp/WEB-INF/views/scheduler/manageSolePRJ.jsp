<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">

    <!-- Nav tabs -->
    <ul class="nav nav-tabs">
      <li class="nav-item active">
        <a class="nav-link" data-toggle="tab" href="#outline">개요</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#manageTask">업무 달성 관리</a>
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

      <!-- 업무 달성률 -->
      <div>
          <div style="position: absolute; display: inline-block;">
            <span class="badge bg " style="background-color: #956be8; color: white; display: inline-block;">
                <h6>업무 달성률&nbsp;</h6>
            </span>
          </div>

        &nbsp;

          <div style="position: relative; display: inline-block; left: 8%;">
              <c:forEach items="${project.tasks}" var="task">
                  <div style="border:0; border-radius: 5px; background-color: #d3d3d3;">
                     ${task.task}
                  </div>
                  <a style="display: inline-block;"><kbd id="kbd">XX%</kbd> 하위업무 N개 중 M개 완료</a>
                  <br/><br/>
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
                    <h6>업무 달성 관리&nbsp;</h6>
                </span>
              </div>

            &nbsp;

              <div style="position: relative; display: inline-block; left: 10%;">
                  <c:forEach items="${project.tasks}" var="task">
                      <div style="border:0; border-radius: 5px; background-color: #d3d3d3;">
                         <label class="custom-checkbox">${task.task}<input type="checkbox" class="task-checkbox dynamicCheckbox" data-task-id="${task.id}" data-task-type="task"></label>
                      </div>
                  <c:forEach items="${task.subTasks}" var="subTask">
                      <div>
                         <label class="custom-checkbox">◾ ${subTask.name}<input type="checkbox" class="subTask-checkbox dynamicCheckbox" data-task-id="${subTask.id}" data-task-type="subTask"></label>
                      </div>
                  </c:forEach>
                  <br/>
                  </c:forEach>
              </div>
          </div>

        <br/><br/>

        <button type="button" class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>

    </div>


    <div class="tab-pane container active fade" id="dailyTask">
    </div>


    </div>
</div>


<script>
$(document).ready(function() {

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

// 동적으로 생성된 체크박스에 대한 이벤트 핸들러 등록
  $(document).ready(function() {
      $(document).on("change", ".dynamicCheckbox", function() {
          var taskId = $(this).data("task-id");
          var isChecked = $(this).prop("checked");

          if (isChecked) {
              alert("체크된 상태 - Task ID: " + taskId);
          } else {
              alert("언체크된 상태 - Task ID: " + taskId);
          }
      });
  });
  });
</script>

<%@ include file="../layout/user/footer.jsp"%>