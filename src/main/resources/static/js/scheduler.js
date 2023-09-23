if (!window.schedulerLoaded) {

    window.schedulerLoaded = true;

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
                url: "/api/summernoteImg",
                cache: false,
                contentType: false,
                processData: false,
                enctype : 'multipart/form-data',
                success: function(data){
                    $(el).summernote('editor.insertImage', data);
             }
          });
        }
    });

    $('#btn-create').on('click', function() {
        var jsonData = {};

        var title = $("#title").val();
        var description = $("textarea[name = description]").val();
        var goal = $("#goal").val();
        var startPRJ = $('#startPRJ').val();
        var endPRJ = $('#endPRJ').val();

        let data = {
                title: title,
                description: description,
                goal: goal,
                startPRJ: startPRJ,
                endPRJ: endPRJ,
        };

        var tasks = [];
        $('#boxContainer input[data-id]').each(function() {
            var fieldName = $(this).data('id');
            var fieldValue = $(this).val();

            if(fieldValue == null) {

                alert("업무를 입력해주세요.");
                return false;
            }

            tasks.push({
                idx: fieldName,
                task: fieldValue,
                subTasks: []
            });
        });

        // 세부업무 정보 수집
        $('.sub-task-input').each(function() {
            var subTaskValue = $(this).val();
            if (subTaskValue.trim() !== '') {
                var boxGroup = $(this).closest('.box-group');
                var taskId = boxGroup.find('input[data-id]').data('id');
                tasks[taskId].subTasks.push(subTaskValue);
            }
        });

        data.tasks = tasks;

        if(title == '') {

            alert("제목을 입력해주세요.");
            return;
        }

        if(description == '') {

            alert("프로젝트 명세사항을 입력해주세요.");
            return;
        }

        if(goal == '') {

            alert("프로젝트 목표를 입력해주세요");
            return;
        }

         var startDate = $( "#startPRJ" ).val();
         var startDateArr = startDate.split('-');

         var endDate = $( "#endPRJ" ).val();
         var endDateArr = endDate.split('-');

         var startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1])-1, startDateArr[2]);
         var endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1])-1, endDateArr[2]);

         if(startDate === "" || startDate === null || endDate === "" || endDate === null) {

            alert("시작날짜와 종료날짜를 입력해주세요");

            return;
         }

         if(startDateCompare.getTime() > endDateCompare.getTime()) {

             alert("시작날짜와 종료날짜를 확인해 주세요.");

             return;
         }

        $.ajax({
            type: "POST",
            url: "/api/scheduler/project",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
                } else {
                location.href="/scheduler/selectPRJPlanners"
                alert(resp.message);
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    });

    $(document).on('click', '.updatePRJ', function() {
        var $project = $(this).parents(".select");
        var project_id = $project.find(".project_id").val();

        location.href = "/scheduler/updatePRJPlanner/" + project_id;
     });

    $(document).on('click', '.deletePRJ', function() {

        if (confirm("삭제를 진행하시겠습니까?")) {

        var $project = $(this).parents(".select");
        var project_id = $project.find(".project_id").val();

        $.ajax({
            type: "DELETE",
            url: "/api/scheduler/project/" + project_id,
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
            alert(resp.message);
            } else {
            location.href = "/scheduler/selectPRJPlanners"
            alert(resp.message);
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
        }
    });

    $(document).on('click', '.activePRJ', function() {
        var $project = $(this).parents(".select");
        var project_id = $project.find(".project_id").val();

        $.ajax({
            type: "PUT",
            url: "/api/scheduler/project/" + project_id + "/activity",
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
                } else {
                location.href = "/scheduler/selectPRJPlanners"
                alert(resp.message);
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    });

    $(document).on('click', '.endPRJ', function() {
        var $project = $(this).parents(".select");
        var project_id = $project.find(".project_id").val();

        if (confirm("프로젝트 플래너 마감을 진행하시겠습니까?\n마감을 진행하면 다시 활성화할 수 없습니다.")) {

        $.ajax({
            type: "PUT",
            url: "/api/scheduler/project/" + project_id + "/deadline",
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
                } else {
                location.href = "/scheduler/selectPRJPlanners"
                alert(resp.message);
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
        }
    });

    $(document).on('click', '.managePRJ', function() {
        var $project = $(this).parents(".select");
        var project_id = $project.find(".project_id").val();

        location.href = "/scheduler/managePRJPlanner/" + project_id;
    });


    $("#btn-updateForm").on("click", ()=>{

        var project_id = $("#project_id").val();
        var taskLog_id = $("#taskLog_id").val();

        location.href = "/scheduler/managePRJPlanner/" + project_id + "/updateTaskLog/" + taskLog_id;
    });

    $("#btn-delete").on("click", ()=>{

        if (confirm("삭제를 진행하시겠습니까?")) {

        var project_id = $("#project_id").val();
        var taskLog_id = $("#taskLog_id").val();

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
                location.href="/scheduler/managePRJPlanner/" + project_id;
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    }
    });

    $(document).on("click", ".subTask-checkbox", function() {

        var upperTaskId = $(this).data("uppertask-id");

        // 해당 상위 업무 ID와 연결된 모든 하위 업무 체크박스를 가져옴
        var subTaskCheckboxes = $(".subTask-checkbox[data-uppertask-id='" + upperTaskId + "']");

        // 모든 하위 업무 체크박스의 선택 여부가 true라면 상위 업무 체크박스도 체크
        var allSubTasksChecked = subTaskCheckboxes.length === subTaskCheckboxes.filter(":checked").length;

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
            type: "PUT",
            url: "/api/scheduler/task/checkStatus",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                // 성공 시 실행할 코드
                alert("체크박스 상태가 업데이트되었습니다.");
                location.href="/scheduler/managePRJPlanner/" + project_id;
            },
            error: function(error) {
                  // 오류 시 실행할 코드
                  alert("오류가 발생하였습니다.");
            }
        });
    });

    $("#btn-setEndedPRJ").click(function() {
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
            type: "PUT",
            url: "/api/scheduler/task/checkStatus",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                // 성공 시 실행할 코드
                alert("체크박스 상태가 업데이트되었습니다.");
                location.href="/scheduler/selectEndedPRJPlanner/" + project_id;
            },
            error: function(error) {
                  // 오류 시 실행할 코드
                  alert("오류가 발생하였습니다.");
            }
        });
    });

    $("#task").change(function () {
        var taskId = $(this).val();
        var subTaskSelect = $("#subTask");

        // 업무 선택에 따라 세부 업무 옵션을 동적으로 업데이트
        $.ajax({
            type: "GET",
            url: "/api/scheduler/task/subTasks",
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
            url: "/api/scheduler/project/" + project_id + "/taskLog",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
                } else {
                alert(resp.message);
                location.href="/scheduler/managePRJPlanner/" + project_id;
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    });

    $('#btn-update').on('click', function() {

        var project_id = $('#project_id').val();

        var taskElements = $('#boxContainer .box-group');

        var tasksToUpdate = []; // 업데이트할 업무 목록
        var tasksToAdd = [];    // 추가할 업무 목록

        var title = $("#title").val();
        var description = $("textarea[name = description]").val();
        var goal = $("#goal").val();

        taskElements.each(function() {
            var task_id = $(this).data('task-id');
            var task = $(this).find('input').val();

            var subTasks = [];
                $(this).find('.sub-task-input').each(function() {
                    var subTask = $(this).val();
                    if (subTask.trim() !== '') {
                        subTasks.push(subTask);
                    }
                });


            if(task_id) {

                tasksToUpdate.push({ idx: task_id, task: task, subTasks: subTasks });
            } else {

                tasksToAdd.push({ task: task, subTasks: subTasks });
            }
        });

        if(title == '') {

            alert("제목을 입력해주세요.");
            return false;
        }

        if(description == '') {

            alert("프로젝트 명세사항을 입력해주세요.");
            return false;
        }

        if(goal == '') {

            alert("프로젝트 목표를 입력해주세요");
            return false;
        }

         var startDate = $( "#startPRJ" ).val();
         var startDateArr = startDate.split('-');

         var endDate = $( "#endPRJ" ).val();
         var endDateArr = endDate.split('-');

         var startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1])-1, startDateArr[2]);
         var endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1])-1, endDateArr[2]);

         if(startDate === "" || startDate === null || endDate === "" || endDate === null) {

            alert("시작날짜와 종료날짜를 입력해주세요");

            return;
         }

         if(startDateCompare.getTime() > endDateCompare.getTime()) {

             alert("시작날짜와 종료날짜를 확인해주세요");

             return;
         }

        let data = {
            title: title,
            description: description,
            goal: goal,
            startPRJ: startDate,
            endPRJ: endDate,
            updatedTasks: tasksToUpdate,
            addedTasks: tasksToAdd,
            deletedTasks: tasksToDelete
        };

        $.ajax({
            type: "PUT",
            url: "/api/scheduler/project/" + project_id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
                } else {
                location.href = "/scheduler/selectPRJPlanners"
                alert(resp.message);
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    });

    $("#btn-updateTaskLog").click(function() {

        var project_id = $("#project_id").val();
        var taskLog_id = $("#taskLog_id").val();

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
                location.href="/scheduler/managePRJPlanner/" + project_id;
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    });

    let index = {
                init: function() {
                $("#btn-back").on("click", ()=>{
                       window.history.back();
                   });
                },
    }

index.init();
}