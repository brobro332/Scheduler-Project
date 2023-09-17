<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div>
<form>
  <span><h2>개인프로젝트 플래너 생성</h2></span>
  <input type="text" class="form-control" placeholder="제목을 입력해주세요" id="title" style="width:100%;"> <br/>

  <hr/><br/>

  <div style="display: inline-block; width: 20%;">
  <span><h5>프로젝트 시작일</h5></span>
  <input type="date" style="border: 1px solid #d3d3d3; border-radius:5px;" id="startPRJ">
  </div>
  <div style="display: inline-block; width: 20%;">
  <span><h5>프로젝트 종료일</h5></span>
  <input type="date" style="border: 1px solid #d3d3d3; border-radius:5px;" id="endPRJ">
  </div><br/>

  <br/><hr/><br/>

  <span><h5>목표</h5></span>
  <textarea id="goal" placeholder="프로젝트를 통해 이루고자 하는 목표를 간략하게 입력해주세요" style="width: 100%; resize: none; border: 1px solid #d3d3d3; border-radius:5px;"></textarea><br/>

  <br/><hr/><br/>

  <span><h5>업무 &nbsp; <button class="btn btn" style="background-color: #956be8; color: white;" id="addMajorTask">+</button></h5></span>
      <div id="boxContainer">
          <!-- 여기에 추가된 박스가 표시됨 -->
      </div>
  <br/><hr/><br/>

  <textarea id="summernote" name="description"></textarea> <br/>
</form>
<br/><br/>

<button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
<button class="btn btn" id="btn-create" style="background-color: #956be8; color: white; width: 201px; float:right;">등록</button>
</div>
</div>

<script src="/js/scheduler.js"></script>

<%@ include file="../layout/user/footer.jsp"%>