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
  <p>${post.createdAt} | 조회 ${post.view_cnt}N </p>
  </div>
  <hr>
  ${post.content}
  <br/>
  <b>댓글 N</b>
  <hr>
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