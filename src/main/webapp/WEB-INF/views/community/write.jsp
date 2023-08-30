<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container" style="height: 300px; min-height: 550px;">
<div>
<form method="post">
  <span><h2>커뮤니티 글쓰기</h2></span>
  <input type="text" class="form-control" placeholder="제목" id="title" style="width:100%;"> <br/>
  <textarea id="content" name="editordata"></textarea> <br/>
</form>
<button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
<button class="btn btn" id="btn-save" style="background-color: #956be8; color: white; width: 201px; float:right;">등록</button>
</div>
</div>

<script>
    $(document).ready(function() {
        $('#content').summernote({
          height: 300,
          minHeight: null,
          maxHeight: null,
          focus: true,
          lang: "ko-KR",
          placeholder: '최대 2048자까지 쓸 수 있습니다.'
	});

	$("#btn-back").on("click", ()=>{
        window.history.back();
    });
});
</script>
<script src="/js/community.js"></script>

<%@ include file="../layout/user/footer.jsp"%>