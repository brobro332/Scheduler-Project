<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
  <h2>커뮤니티</h2>

  <form style="text-align:center;" action="view" method="get">
  <div style="display:inline-block; float:right;">
    <input type="text" style="display:inline; width:200px;" class="form-control" name="keyword">
    <button type="submit" class="btn btn mb-1 mr-sm-1"  class="btn btn" style="display:inline-block; background-color: #956be8; color: white;">검색</button>
  </div>
  <table class="table table-hover">
    <thead>
      <tr>
        <th></th>
        <th>제목</th>
        <th>작성자</th>
        <th>작성일</th>
        <th>조회</th>
      </tr>
    </thead>
    <tbody>
      <c:choose>
        <c:when test="${empty posts.content}">
            <tr>
                <td colspan="5" style="text-align:center;">조회결과가 없습니다.</td>
            </tr>
        </c:when>
    <c:otherwise>
        <c:forEach items="${posts.content}" var="post">
          <tr>
            <td>${post.id}</td>
            <td><a href="/community/selectPost/${post.id}">${post.title}</a></td>
            <td>
                <div id="image_wrapper" style="position: relative; display:inline-block; width: 25px; height: 25px; border-radius: 70%; overflow: hidden;">
                  <img id="profileImg" src="/api/profileImg/${post.user.email}" style="position: absolute; right: 0%; width: 100%; height: 100%; object-fit: cover;">
                </div>
                <a style="bottom:10%;">${post.user.name}</a>
            </td>
            <td>${post.createdAt}</td>
            <td>${post.view_cnt}</td>
          </tr>
          </c:forEach>
        </c:otherwise>
    </c:choose>
    </tbody>
  </table>
  </form>
  <button class="btn btn" style="display:inline-block; float:right; background-color: #956be8; color: white;" type="button" onclick="location.href='/community/createPost'">글쓰기</button>

  <ul class="pagination" style="position:relative; left:45%; width: 20%;">
  	<c:choose>
  		<c:when test="${posts.first}">
  			<li class="page-item disabled"><a class="page-link" href="?page=${posts.number-1}"><</a></li>
  		</c:when>
  		<c:otherwise>
  			<li class="page-item"><a class="page-link" href="?page=${posts.number-1}"><</a></li>
  		</c:otherwise>
  	</c:choose>

  	<c:forEach var="i" begin="1" end="${posts.totalPages}">
  	<c:choose>
      		<c:when test="${!empty param.keyword}">
      		    <li class="page-item"><a class="page-link" href="?page=${i-1}&keyword=${param.keyword}">${i}</a></li>
      		</c:when>
      		<c:otherwise>
      	        <li class="page-item"><a class="page-link" href="?page=${i-1}">${i}</a></li>
      		</c:otherwise>
      	</c:choose>
    </c:forEach>

  	<c:choose>
  		<c:when test="${posts.last}">
  			<li class="page-item disabled"><a class="page-link" href="?page=${posts.number+1}">></a></li>
  		</c:when>
  		<c:otherwise>
  			<li class="page-item"><a class="page-link" href="?page=${posts.number+1}">></a></li>
  		</c:otherwise>
  	</c:choose>
  </ul>

</div>

<script src="/js/community.js"></script>

<%@ include file="../layout/user/footer.jsp"%>