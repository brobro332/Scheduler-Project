<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div class="card">
  <div class="card-body">
  <input type="text" class="form-control" value="${post.id}" id="post_id" hidden>
  <span><h1>${post.title}</h1></span>
  <div id="image_wrapper" style="position: relative; display:inline-block; width: 40px; height: 40px; border-radius: 70%; overflow: hidden;">
      <img id="profileImg" src="/api/community/post/profileImg/${post.user.email}" style="position: absolute; width: 100%; height: 100%; object-fit: cover;">
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
    <c:when test="${empty comments}">
    </c:when>
    <c:otherwise>
      <c:forEach items="${comments.content}" var="comment">
      <div style="position: relative; left:1%;">
      <div style="height:100%; position: relative; display:inline-block;">
      <div id="image_wrapper" style="position: relative; display:inline-block; width: 45px; height: 45px; border-radius: 70%; overflow: hidden;">
        <img id="profileImg" src="/api/community/post/profileImg/${comment.user.email}" style="position: absolute; right: 0%; width: 100%; height: 100%; object-fit: cover;">
      </div>
      </div>
      <div style="position:relative; display:inline-block; left: 1%;">
      <a><b>${post.user.name}</b></a>
      <p style="display:block;">${comment.createdAt}</p>
      </div>
      <div>
      <a style="display: block;"><pre>${comment.comment}</pre></a>
      <a style="position: relative; left:91%;" href="#"><p style="color: gray;">답글쓰기</p></a>
      </div>
      </div>
      <hr>
      </c:forEach>
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
        <button class="btn btn" onclick="location.href='/community/update/${post.id}'" style="background-color: #956be8; color: white; width: 201px; float:left;">수정</button>
        <button class="btn btn" id="btn-deletePost" style="position: relative; background-color: gray; color: white; width: 201px; float:left; left: 1%;">삭제</button>
    </c:when>
</c:choose>
<button class="btn btn" onclick="location.href='/community/view'" style="background-color: gray; color: white; width: 201px; float:right;">목록</button>
</div>
</div>

<script src="/js/community.js"></script>

<%@ include file="../layout/user/footer.jsp"%>