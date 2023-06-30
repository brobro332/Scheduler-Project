<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="layout/user/header.jsp"%>

<img src="/image/signin-spap.png" style="display: block; position: relative; width: 200px; left: 42%">

<br/>

<div class="container" style="display: inline-block; position: relative; left: 35%; width: 70%; min-height: 360px;">
    <form action="/signIn" method="POST">
        <label>로그인</label>
        <div class="form-group">
            <input type="email" class="form-control" placeholder="이메일" id="email" name="email" style="width:402px;">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" placeholder="비밀번호" id="password" name="password" style="width:402px;">
        </div>
        <c:if test="${error}"><p style="width:402px; color: red;">${exception}</p></c:if>
         <br/>

        <button class="btn btn" style="background-color: #956be8; color: white; width: 402px;">로그인</button>
    </form>

    <br/><br/>
    <span style="position: relative; left: 4%;">
    <a href="#" style="color: gray;">이메일 찾기</a> | <a href="#" style="color: gray;">비밀번호 찾기</a> | <a href="/signUpForm" style="color: gray;">회원가입</a>
    </span>
    <br/>
</div>

<%@ include file="layout/user/footer.jsp"%>