<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div>
<div>
<form>
  <span><h4>${project.title}</h4></span>

  <hr><br/>

  <div style="display: inline-block;">
  <span class="badge bg" style="background-color: #956be8; color: white;"><h6>프로젝트 기간</h6></span>
  &nbsp;
  ${project.startPRJ} ~ ${project.endPRJ}
  </div>

  <br/><br/>

  <span class="badge bg" style="background-color: #956be8; color: white;"><h6>프로젝트 목표</h6></span>
  &nbsp;
  <a>${project.goal}</a>

  <br/><br/>

  <div>
      <div style="position: absolute; display: inline-block;">
        <span class="badge bg " style="background-color: #956be8; color: white; display: inline-block;"><h6>업무 리스트&nbsp;</h6></span>
      </div>

  &nbsp;
      <div style="position: relative; display: inline-block; left: 8%;">
          <c:forEach items="${project.tasks}" var="task">
                  <div style="border:0; border-radius: 5px; background-color: #d3d3d3;">
                     ${task.task}
                     </div>
                     <c:forEach items="${task.subTasks}" var="subTask">
                        <div>
                        ◾ ${subTask.name}
                        </div>
                     </c:forEach>
              &nbsp;
          </c:forEach>
      </div>
  </div>

  <br/><br/>

  <div style="border:1px solid #d3d3d3; border-radius: 5px; padding: 2%;">
  ${project.description}
  </div>
</form>

<br/><br/>

<button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
</div>
</div>
</div>

<script src="/js/scheduler.js"></script>

<%@ include file="../layout/user/footer.jsp"%>