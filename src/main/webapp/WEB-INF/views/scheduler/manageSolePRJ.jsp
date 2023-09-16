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
        <a class="nav-link" data-toggle="tab" href="#dailyTask">업무 일지 관리</a>
      </li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">

      <div class="tab-pane container active" id="outline">

      <br/>
      <span><h4>${project.title}</h4></span>
      <p style="color: gray;"><small>${project.startPRJ} ~ ${project.endPRJ}</small></p>

      <hr><br/>

      <div>
      <div style="display: inline-block;">
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

          <div style="position: relative; display: inline-block; left: 30%;">
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
      </div>

      <div style="position: absolute; display: inline-block; left: 50%;">
      <div style="text-align: center; width: 500px; height: 500px; overflow: auto;">
      <form>
        <table class="table table-hover" style="text-align:center; width: 100%; height: 100%; border-collapse: collapse;">
          <thead>
            <tr>
                <th>작성일자</th>
                <th>제목</th>
                <th>업무</th>
                <th>하위업무</th>
            </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty taskLogs.content}">
                <tr>
                    <td colspan="4">조회결과가 없습니다.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${taskLogs.content}" var="taskLog" varStatus="loop">
                    <tr>
                        <th style="font-weight: lighter;">
                            <c:set var="createdAt" value="${taskLog.createdAt}" />
                            <c:out value="${createdAt.year}-${createdAt.monthValue}-${createdAt.dayOfMonth}" />
                        </th>
                        <th style="font-weight: lighter;"><a href="/scheduler/manage/project/taskLog/${taskLog.id}">${taskLog.title}</a></th>
                        <th style="font-weight: lighter;">${taskLog.taskCategory}</th>
                        <th style="font-weight: lighter;">${taskLog.subTaskCategory}</th>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
        </table>
      </form>
        <div style="display: inline-block;">
        <ul class="pagination" style="position:relative; width: 20%;">
                <c:choose>
                    <c:when test="${taskLogs.first}">
                        <li class="page-item disabled"><a class="page-link" href="?page=${taskLogs.number-1}"><</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="?page=${taskLogs.number-1}"><</a></li>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="1" end="${taskLogs.totalPages}">
                    <li class="page-item"><a class="page-link" href="?page=${i-1}">${i}</a></li>
              </c:forEach>

                <c:choose>
                    <c:when test="${taskLogs.last}">
                        <li class="page-item disabled"><a class="page-link" href="?page=${taskLogs.number+1}">></a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="?page=${taskLogs.number+1}">></a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
            </div>
      </div>
      </div>
      </div>

    <br/><br/>

    <button type="button" class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>

    </div>


    <div class="tab-pane container active fade" id="manageTask">

          <br/>
          <span><h4>업무 수행 관리</h4></span>
          <p style="color: gray;"><small>업무 리스트에서 수행 완료된 업무를 체크해보세요</small></p>

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
    <div>
    <br/>
    <form method="post">
      <span><h4>업무 일지 관리</h4></span>
      <p style="color: gray;"><small>오늘 수행한 업무 일지를 작성해보세요</small></p>
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

      </div><br/>
      <input type="text" class="form-control" placeholder="제목" id="title" style="width:100%;"> <br/>
      <textarea id="summernote" name="content"></textarea> <br/>
    </form>
    <button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
    <button class="btn btn" id="btn-save" style="background-color: #956be8; color: white; width: 201px; float:right;">등록</button>
    </div>
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

      	$("#btn-back").on("click", ()=>{
              window.history.back();
          });

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

        $("#btn-save").click(function() {

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

            console.log(subTaskSelectedValue);

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
                type: "POST",
                url: "/api/scheduler/project/taskLog/" + project_id,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(resp) {
                if(resp.statusCode == 400 || resp.statusCode == 500){
                    alert(resp.message);
                    } else {
                    alert(resp.message);
                    location.href="/scheduler/manage/project/" + project_id;;
                }
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
        });
  });
</script>

<%@ include file="../layout/user/footer.jsp"%>