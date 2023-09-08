<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
  <h2>프로젝트 플래너</h2>

  <form action="view" method="get" style="width: 100%">
  <div style="display: inline-block; width: 25%;">
   여기에 프로필 표시될 예정
   <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
   여기에 프로필 표시될 예정
  </div>
  <div style="position:relative; display: inline-block; width: 70%; left: 20%">
  <c:choose>
      <c:when test="${empty projects.content}">
      </c:when>
      <c:otherwise>
          <c:forEach items="${projects.content}" var="project">
                <div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px; width: 70%;">
                    <h5><b>${project.title}</b></h4>
                    <h6>${project.startPRJ} ~ ${project.endPRJ}</h6>
                </div>
                <p></p>
          </c:forEach>
      </c:otherwise>
      </c:choose>
  </div>
  </form>
</div>

<script>
$(document).ready(function() {

	$("#btn-back").on("click", ()=>{
        window.history.back();
    });
});
</script>

<%@ include file="../layout/user/footer.jsp"%>