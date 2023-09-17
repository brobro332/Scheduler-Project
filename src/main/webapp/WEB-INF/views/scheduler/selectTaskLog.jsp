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

<script src="/js/scheduler.js"></script>

<%@ include file="../layout/user/footer.jsp"%>