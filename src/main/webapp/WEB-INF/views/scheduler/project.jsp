<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div>
/개요
<div>
<form>
  <span><h4>${project.title}</h4></span>

  <hr><br/>

  <div style="display: inline-block;">
  <span class="badge bg-secondary" style="color: white;"><h6>프로젝트 기간</h6></span>
  &nbsp;
  ${project.startPRJ} ~ ${project.endPRJ}
  </div>

  <br/><br/>

  <span class="badge bg-secondary" style="color: white;"><h6>프로젝트 목표</h6></span>
  &nbsp;
  <div style="border:1px solid #d3d3d3; border-radius: 5px; width: auto; display: inline-block;">
  <a>${project.goal}</a>
  </div>

  <br/><br/>

  <span class="badge bg-secondary" style="color: white;"><h6>업무 리스트&nbsp;</h6></span>

  &nbsp;

  <c:forEach items="${project.tasks}" var="task">
      <div style="border:1px solid #d3d3d3; border-radius: 5px; width: auto; display: inline-block;">
      ${task.task}
      </div>
      &nbsp;
  </c:forEach>

  <br/><br/>

  <div style="border:1px solid #d3d3d3; border-radius: 5px;">
  ${project.description}
  </div>
</form>

<br/><br/>

<button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
</div>
</div>
</div>

<script></script>

<%@ include file="../layout/user/footer.jsp"%>