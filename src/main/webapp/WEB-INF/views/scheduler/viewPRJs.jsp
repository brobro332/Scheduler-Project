<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
  <h2>프로젝트 플래너</h2>

  <form action="view" method="get" style="width: 100%">
  <div style="position: relative; display: inline-block; width: 30%;">
  <c:choose>
    <c:when test="${empty img.profileImgName}" >
    <img class="card-img-top" src="/image/profile-spap.png" alt="Card image" style="position:relative; width: 200px; left: 100px;">
    </c:when>
    <c:otherwise>
    <br>
    <div id="image_wrapper" style="position: relative; width: 200px; height: 200px; border-radius: 70%; overflow: hidden;">
      <img id="profileImg" src="/api/user/info/profileImg" style="position: absolute; width: 100%; height: 100%; object-fit: cover;">
    </div>
    </c:otherwise>
    </c:choose>

    <br/>

   <div>
   <h3><b>${info.name}</b></h3>
   <h5>총 프로젝트: ${count} 개</h5>
   <h5>진행중인 프로젝트: N 개</h5>
   <h5>완료된 프로젝트: N 개</h5>

   <button type="button" class="btn btn" onclick="location.href='/scheduler/create'" style="display: block; background-color: #956be8; color: white; width: 200px; height: 60px;">플래너 생성</button>
   </div>
  </div>
  <div style="position:absolute; display: inline-block; width: 50%;">
  <c:choose>
      <c:when test="${empty projects.content}">
      </c:when>
      <c:otherwise>
          <c:forEach items="${projects.content}" var="project">
            <div class="select">
                <input type="text" class="form-control project_id" value="${project.id}" hidden>
                <div style="position: relative; border: 1px solid #dddddd; border-radius: 5px; padding: 10px; width: 70%; left:15%;">
                    <h5 style="display: inline-block;"><b><a href="/scheduler/view/project/${project.id}">${project.title}</a></b></h4>
                    &nbsp;

                    <c:choose>
                    <c:when test="${project.active_yn == 'N'}">
                        <span class="badge bg-secondary" style="font-size: 15px; color: white; width:13%">비활성</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-info" style="font-size: 15px; color: white; width:13%">활성</span>
                    </c:otherwise>
                    </c:choose>

                    <div class="btn-group" style="display: inline-block; position:relative; float:right;">
                        <button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
                           ◾◾◾
                        </button>
                        <div class="dropdown-menu">
                          <button type="button" class="dropdown-item updatePRJ" id="btn-updatePRJ">수정</button>
                          <button type="button" class="dropdown-item deletePRJ" id="btn-deletePRJ">삭제</button>
                        </div>
                    </div>
                    <h6>${project.startPRJ} ~ ${project.endPRJ}</h6>
                    <c:choose>
                        <c:when test="${project.active_yn == 'N'}">
                            <button type="button" class="btn btn activePRJ" style="display: flex; background-color: #956be8; color: white; margin-left: auto;" id="btn-activePRJ">활성화</button>
                        </c:when>
                        <c:otherwise>
                            <div style="display: flex; margin-left: auto;">
                                <button type="button" class="btn btn managePRJ" style="display: flex; background-color: #956be8; color: white; margin-left: auto;" id="btn-managePRJ">관리</button>
                                <button type="button" class="btn btn activePRJ" style="display: flex; background-color: #956be8; color: white; margin-left: 2px;" id="btn-activePRJ">비활성화</button>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <p></p>
                </div>
          </c:forEach>
      </c:otherwise>
      </c:choose>
  </div>
  </form>
  <ul class="pagination" style="position:relative; left:45%; width: 20%;">
    	<c:choose>
    		<c:when test="${projects.first}">
    			<li class="page-item disabled"><a class="page-link" href="?page=${projects.number-1}"><</a></li>
    		</c:when>
    		<c:otherwise>
    			<li class="page-item"><a class="page-link" href="?page=${projects.number-1}"><</a></li>
    		</c:otherwise>
    	</c:choose>

    	<c:forEach var="i" begin="1" end="${projects.totalPages}">
            <li class="page-item"><a class="page-link" href="?page=${i-1}">${i}</a></li>
      </c:forEach>

    	<c:choose>
    		<c:when test="${projects.last}">
    			<li class="page-item disabled"><a class="page-link" href="?page=${projects.number+1}">></a></li>
    		</c:when>
    		<c:otherwise>
    			<li class="page-item"><a class="page-link" href="?page=${projects.number+1}">></a></li>
    		</c:otherwise>
    	</c:choose>
    </ul>
</div>

<script>
$(document).ready(function() {

     $(document).on('click', '.updatePRJ', function() {
            var $project = $(this).parents(".select");
            var project_id = $project.find(".project_id").val();

            location.href = "/scheduler/update/project/" + project_id;
     });

     $(document).on('click', '.deletePRJ', function() {

            if (confirm("삭제를 진행하시겠습니까?")) {

            var $project = $(this).parents(".select");
            var project_id = $project.find(".project_id").val();

            $.ajax({
                type: "DELETE",
                url: "/api/scheduler/project/delete/" + project_id,
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(resp) {
                if(resp.statusCode == 400 || resp.statusCode == 500){
                    alert(resp.message);
                    } else {
                    location.href = "/scheduler/view"
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
                 url: "/api/scheduler/project/active/" + project_id,
                 contentType: "application/json; charset=utf-8",
                 dataType: "json"
             }).done(function(resp) {
                 if(resp.statusCode == 400 || resp.statusCode == 500){
                     alert(resp.message);
                     } else {
                     location.href = "/scheduler/view"
                     alert(resp.message);
                 }
             }).fail(function(error) {
                 alert(JSON.stringify(error));
             });
         });
     });
</script>

<%@ include file="../layout/user/footer.jsp"%>