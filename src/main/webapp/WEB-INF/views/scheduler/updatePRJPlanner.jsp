<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div>
<form>
  <span><h2>개인프로젝트 플래너 수정</h2></span>
  <input type="text" class="form-control" placeholder="제목을 입력해주세요" id="title" value="${project.title}" style="width:100%;"> <br/>
  <input type="text" id="project_id" value="${project.id}" hidden>

  <hr/><br/>

  <div style="display: inline-block; width: 20%;">
  <span><h5>프로젝트 시작일</h5></span>
  <input type="date" style="border: 1px solid #d3d3d3; border-radius:5px;" value="${project.startPRJ}" id="startPRJ">
  </div>
  <div style="display: inline-block; width: 20%;">
  <span><h5>프로젝트 종료일</h5></span>
  <input type="date" style="border: 1px solid #d3d3d3; border-radius:5px;" value="${project.endPRJ}" id="endPRJ">
  </div><br/>

  <br/><hr/><br/>

  <span><h5>목표</h5></span>
  <textarea id="goal" placeholder="프로젝트를 통해 이루고자 하는 목표를 간략하게 입력해주세요" style="width: 100%; resize: none; border: 1px solid #d3d3d3; border-radius:5px;">${project.goal}</textarea><br/>

  <br/><hr/><br/>

  <span><h5>업무 &nbsp; <button class="btn btn" style="background-color: #956be8; color: white;" value="${project.goal}" id="addMajorTask">+</button></h5></span>

      <div id="boxContainer">
          <c:forEach items="${project.tasks}" var="task">
          <div class="box-group" data-task-id="${task.id}">
              <input type="text" class="form-control" style="width: 53%; display: inline-block;" value="${task.task}" placeholder="목표 달성을 위한 업무를 입력해주세요">
              <div style="display: inline-block;">
                  <button type="button" class="btn subTask-box-group">세부업무 추가</button>
                  <button type="button" class="btn remove-box-group">제거</button>
              </div>
              <c:forEach items="${task.subTasks}" var="subTask">
              <div>
                <input style='width: 50%; position: relative; display: inline-block; left: 3%;' type='text' class='form-control sub-task-input' value="${subTask.name}" placeholder='업무에 해당하는 세부업무를 입력해주세요'>
                <button style="position: relative; display: inline-block; left: 3%;" type="button" class="btn remove-sub-task">제거</button>
              </div>
              </c:forEach>
              </div>
          </c:forEach>
          <!-- 여기에 추가된 박스가 표시됨 -->
      </div>

  <br/><hr/><br/>

  <textarea id="summernote" name="description">${project.description}</textarea> <br/>
</form>
<br/><br/>

<button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
<button class="btn btn" id="btn-update" style="background-color: #956be8; color: white; width: 201px; float:right;">수정</button>
</div>
</div>

<script>

var boxCount = 0;
var tasksToDelete = [];

$(document).ready(function() {

    // + 버튼을 클릭할 때 박스를 추가하는 JavaScript 코드
    $('#addMajorTask').click(function(event) {
        event.preventDefault();

        var boxGroup = $('<div>').addClass('box-group');

        var inputElement = $('<input>').attr({
            type: 'text',
            class: 'form-control',
            style: 'width: 53%; display:inline-block;',
            placeholder: '목표 달성을 위한 업무를 입력해주세요',
            'data-id': boxCount
        });

        var subTaskButton = $('<button type="button">').addClass('btn btn subTask-box-group').text('세부업무 추가');
        var removeButton = $('<button type="button">').addClass('btn btn remove-box-group').text('제거');

        // 각 그룹에 입력 요소와 제거 버튼 추가
        boxGroup.append(inputElement).append(subTaskButton).append(removeButton);

        // 박스 그룹을 컨테이너에 추가
        $('#boxContainer').append(boxGroup);

        boxCount++;
    });

    // "제거" 버튼 클릭 이벤트 핸들러를 이벤트 위임하여 설정
    $(document).on('click', '.remove-box-group', function(event) {
        event.preventDefault();

        var clickedButton = $(event.target);
        var boxGroup = clickedButton.closest('.box-group'); // 박스 그룹을 찾습니다.

        var task_id = boxGroup.data('task-id');

        if(task_id) {

            tasksToDelete.push({ idx: task_id });
        }

        boxGroup.remove(); // 박스 그룹을 삭제합니다.
    });

    // "세부업무 추가" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.subTask-box-group', function(event) {
        event.preventDefault();

        var clickedButton = $(event.target);
        var boxGroup = clickedButton.closest('.box-group'); // 클릭된 버튼이 속한 박스 그룹을 찾습니다.

        // 새로운 세부업무 입력 요소 생성
        var subTaskInput = $('<input>').attr({
            type: 'text',
            class: 'form-control sub-task-input col-6',
            placeholder: '업무에 해당하는 세부업무를 입력해주세요',
        }).css('left', '3%').css('display', 'inline-block').css('margin-right', '4px');

        // 새로운 제거 버튼 생성
            var removeSubTaskButton = $('<button>').attr({
                type: 'button',
                style: 'display: inline-block; position: relative; left: 3%;',
                class: 'btn btn remove-sub-task', // 새로운 클래스 추가
            }).text('제거');

        var subTaskInputGroup = $('<div>').addClass('input-group')
        .append(subTaskInput).append(removeSubTaskButton);

        // 세부업무 입력 요소를 박스 그룹에 추가
        boxGroup.append(subTaskInputGroup);
    });

    // "세부업무 제거" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.remove-sub-task', function(event) {
        event.preventDefault();

        var clickedButton = $(event.target);
        var subTaskInput = clickedButton.prev();
        subTaskInput.remove(); // 세부업무 입력 요소 삭제
        clickedButton.remove(); // 제거 버튼 삭제
    });
});
</script>
<script src="/js/scheduler.js"></script>

<%@ include file="../layout/user/footer.jsp"%>