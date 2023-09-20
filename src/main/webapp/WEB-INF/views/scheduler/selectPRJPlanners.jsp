<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
  <h2>프로젝트 플래너</h2>

  <form action="view" method="get" style="width: 100%">
  <div style="position: relative; display: inline-block; width: 25%;">
  <c:choose>
    <c:when test="${empty img.profileImgName}" >
    <div id="image_wrapper" style="position: relative; width: 200px; height: 200px; border-radius: 70%; overflow: hidden;">
    <img class="card-img-top" src="/image/profile-spap.png" alt="Card image" style="position:relative; width: 200px;">
    </div>
    </c:when>
    <c:otherwise>
    <br>
    <div id="image_wrapper" style="position: relative; width: 200px; height: 200px; border-radius: 70%; overflow: hidden;">
      <img id="profileImg" src="/api/profileImg" style="position: absolute; width: 100%; height: 100%; object-fit: cover;">
    </div>
    </c:otherwise>
    </c:choose>

    <br/>

   <div>
   <h3><b>${info.name}</b></h3>
   <h5>총 프로젝트: ${countTotal} 개</h5>
   <h5>활성화된 프로젝트: ${countActive} 개</h5>
   <h5>완료된 프로젝트: ${countCompleted} 개</h5>

   <button type="button" class="btn btn" onclick="location.href='/scheduler/createPRJPlanner'" style="display: block; background-color: #956be8; color: white; width: 200px; height: 60px;">플래너 생성</button>
   </div>
  </div>

  <div style="border-left: 4px dashed #d3d3d3; display: inline-block; height: 380px; margin-right: 75px;"></div>

  <div style="position:absolute; display: inline-block; width: 65%;">
  <c:choose>
      <c:when test="${empty projects.content}">
      </c:when>
      <c:otherwise>
          <c:forEach items="${projects.content}" var="project">
            <div class="select">
                <input type="text" class="form-control project_id" value="${project.id}" hidden>
                <div style="position: relative; border: 1px solid #dddddd; border-radius: 5px; padding: 20px; width: 70%;">
                    <h5 style="display: inline-block;"><b><a href="/scheduler/selectPRJPlanner/${project.id}">${project.title}</a></b></h4>
                    &nbsp;

                    <c:choose>
                    <c:when test="${project.activeYn == 'N'}">
                        <c:choose>
                            <c:when test="${project.completeYn == 'Y'}">
                                <span class="badge bg" style="background-color: #d3d3d3; font-size: 15px; color: white; width:13%">완료</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary" style="font-size: 15px; color: white; width:13%">비활성</span>
                            </c:otherwise>
                        </c:choose>
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
                        <c:when test="${project.activeYn == 'N'}">
                        <c:choose>
                            <c:when test="${project.completeYn == 'Y'}">
                                <button type="button" class="btn btn viewEndedPRJ" onclick="location.href='/scheduler/selectEndedPRJPlanner/${project.id}'" style="display: flex; background-color: gray; color: white; margin-left: auto;" id="btn-activePRJ">조회</button>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn btn activePRJ" style="display: flex; background-color: #956be8; color: white; margin-left: auto;" id="btn-activePRJ">활성화</button>
                            </c:otherwise>
                        </c:choose>
                        </c:when>
                        <c:otherwise>
                            <div style="display: flex; margin-left: auto;">
                            <button type="button" class="btn btn endPRJ" style="background-color: gray; color: white;" id="btn-endPRJ">마감</button>
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
      <div style="text-align: center; width: 65%;">
      <div style="display: inline-block;">
        <ul class="pagination" style="position:relative; width: 20%;">
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
      </div>
  </div>
  </form>
</div>

<script src="/js/scheduler.js"></script>

<%@ include file="../layout/user/footer.jsp"%>