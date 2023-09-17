<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div>
<input type="text" class="form-control" value="${post.id}" id="post_id" hidden>
<form method="post">
  <span><h2>글쓰기</h2></span>
  <input type="text" class="form-control" placeholder="제목" value="${post.title}" id="title" style="width:100%;"> <br/>
  <textarea id="summernote" name="content">${post.content}</textarea> <br/>
</form>
<button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
<button class="btn btn" id="btn-update" style="background-color: #956be8; color: white; width: 201px; float:right;">등록</button>
</div>
</div>

<script src="/js/community.js"></script>

<%@ include file="../layout/user/footer.jsp"%>