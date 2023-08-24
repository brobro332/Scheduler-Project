<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<img src="/image/signin-spap.png" style="display: block; position: relative; width: 200px; left: 42%">

<br/>

<div class="container" style="display: inline-block; position: relative; left: 35%; width: 70%;">

<div>
    <button type="button" onclick="location.href='/user/info/updateInfoForm'" class="btn btn" style="display: block; background-color: #956be8; color: white; width: 400px; height: 60px;">개인정보 수정</button>
</div>

<br>

<div>
    <button type="button" onclick="location.href='/user/info/updatePasswordForm'" class="btn btn" style="display: block; background-color: gray; color: white; width: 400px; height: 60px;">패스워드 변경</button>
</div>

<br><br><br>

</div>

<%@ include file="../layout/user/footer.jsp"%>