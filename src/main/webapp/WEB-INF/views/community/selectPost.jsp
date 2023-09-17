<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div class="card">
  <div class="card-body">
  <c:choose>
      <c:when test="${empty name}">
      </c:when>
      <c:otherwise>
        <input type="text" class="form-control" value="${name}" id="principal_name" readonly>
      </c:otherwise>
  </c:choose>
  <input type="text" class="form-control" value="${post.id}" id="post_id" hidden>
  <span><h1>${post.title}</h1></span>
  <div id="image_wrapper" style="position: relative; display:inline-block; width: 40px; height: 40px; border-radius: 70%; overflow: hidden;">
      <img id="profileImg" src="/api/profileImg/${post.user.email}" style="position: absolute; width: 100%; height: 100%; object-fit: cover;">
  </div>
  <div style="display:inline-block;">
  ${post.user.name}
  <p>${post.createdAt} | 조회 ${post.view_cnt} </p>
  </div>
  <hr>
  ${post.content}
  <br/>
  <b>댓글 N</b>
  <hr>
  <c:choose>
    <c:when test="${empty comments.content}">
    </c:when>
    <c:otherwise>
      <c:forEach items="${comments.content}" var="comment">
      <div class="findComment_id">
      <input type="text" class="form-control comment_id" value="${comment.id}" hidden>
      <div class="update">
      <div class="comment" style="position: relative; left:1%;">

      <div style="height:100%; position: relative; display:inline-block;">
          <div id="image_wrapper" style="position: relative; display:inline-block; width: 45px; height: 45px; border-radius: 70%; overflow: hidden;">
            <img id="profileImg" src="/api/profileImg/${comment.user.email}" style="position: absolute; right: 0%; width: 100%; height: 100%; object-fit: cover;">
          </div>
      </div>

      <div style="position:relative; display:inline-block; left: 1%;">
      <a><b>${post.user.name}</b></a>
      <c:if test="${principal.user.email == comment.user.email}">
      <a style="font-size: 12px; color: purple; position:relative; display:inline-block; border: 3px solid; border-radius: 30px;"><b>작성자</b></a>
            <div class="btn-group" style="display: inline-block; position:relative; left:360%;">
                <button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
                   ◾◾◾
                </button>
                <div class="dropdown-menu">
                  <button class="dropdown-item updateComment" id="btn-commentUpdate">수정</button>
                  <button class="dropdown-item deleteComment" id="btn-commentDelete">삭제</button>
                </div>
            </div>
      </c:if>
      <p style="display:block;">${comment.createdAt}</p>
      </div>
      <div>
      <a style="display: block;"><pre>${comment.comment}</pre></a>
      <a style="position: relative; left:91%;" href="#"><p style="color: gray;">답글쓰기</p></a>
      </div>
      </div>
      </div>
      </div>
      <hr>
      </c:forEach>
        <ul class="pagination" style="position:relative; left:45%; width: 20%;">
            <c:choose>
                <c:when test="${comments.first}">
                    <li class="page-item disabled"><a style="border: 0px;" class="page-link" href="?page=${comments.number-1}"><</a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a style="border: 0px;" class="page-link" href="?page=${comments.number-1}"><</a></li>
                </c:otherwise>
            </c:choose>

          <c:forEach var="i" begin="1" end="${comments.totalPages}">
              <li class="page-item"><a style="border: 0px;" class="page-link" href="?page=${i-1}">${i}</a></li>
          </c:forEach>

            <c:choose>
                <c:when test="${comments.last}">
                    <li class="page-item disabled"><a style="border: 0px;" class="page-link" href="?page=${comments.number+1}">></a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a style="border: 0px;" class="page-link" href="?page=${comments.number+1}">></a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </c:otherwise>
  </c:choose>

  <div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px;">
  <p>&nbsp;${principal.user.name}</p>
  <textarea type="text" style="border: 0px; color: gray; resize: none;" class="form-control" placeholder="댓글을 입력해보세요" id="comment"></textarea>
  <p></p>
  <button class="btn btn" id="btn-comment" style="background-color: white; color: gray; width: 100px; float:right;">등록</button>
  </br></br>
  </div>
  </div>
</div>
<div style="position:relative; height: 40px;">
<p></p>
<c:choose>
    <c:when test="${post.user.email == principal.user.email}">
        <button class="btn btn" onclick="location.href='/community/updatePost/${post.id}'" style="background-color: #956be8; color: white; width: 201px; float:left;">수정</button>
        <button class="btn btn" id="btn-deletePost" style="position: relative; background-color: gray; color: white; width: 201px; float:left; left: 1%;">삭제</button>
    </c:when>
</c:choose>
<button class="btn btn" onclick="location.href='/community/selectPosts'" style="background-color: gray; color: white; width: 201px; float:right;">목록</button>
</div>
</div>

<script src="/js/community.js"></script>

<%@ include file="../layout/user/footer.jsp"%>