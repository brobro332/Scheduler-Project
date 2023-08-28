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

    <br/>
    <span style="position: relative; left: 4%;">
    <a href="#" style="color: gray;">이메일 찾기</a> | <a href="#" style="color: gray;">비밀번호 찾기</a> | <a href="/signUpForm" style="color: gray;">회원가입</a>
    </span>
    <br/><br/>
    <span style="position: relative; left: 9%;">
    <a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=262c86c00bbc08f8657b2bc0851efa0d&redirect_uri=http://localhost:8080/kakao/callback"><img src="/image/kakao_login_medium_narrow.png"></a> <br/><p></p>
    <a href="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=X7QHnxHieBySSOROJ7m8&state=STATE_STRING&redirect_uri=http://localhost:8080/naver/callback"><img src="/image/naver_login_medium_narrow.png" style="width:183px; height:45px;"></a>
    </span>
    <br/><br/>
</div>

<%@ include file="layout/user/footer.jsp"%>