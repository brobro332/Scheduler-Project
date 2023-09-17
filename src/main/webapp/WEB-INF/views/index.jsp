<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="layout/user/header.jsp"%>

<div class="container" style="height: 300px; min-height: 360px;">
<div>
    <div style="display: inline-block; position: absolute; left: 20%; top: 11%;">
        <div style="width:470px; height:300px;">
        <p style="color: gray;"><small>Welcome to SAPP<small></p>
        <h1>계획은 <kbd id="kbd">"꼼꼼하게"</kbd><br/>
        당신의 프로젝트 플래너,<br/>
        저희가 도와드릴게요!</h1>
        <br/>
        <button onclick="location.href='/scheduler/selectPRJPlanners'" class="btn btn-lg" style="background-color: #956be8; color: white;"><b>시작하기</b></button>
        </div>
    </div>
</div>

<div style="display: inline-block; position: absolute; left: 60%; top: 10%;">
<img src="/image/banner-right-spap.png" style="width:300px;">
</div>
</div>

<div class="container" style="display: inline-block; position: relative; left: 15%; width: 70%;">

    <br/><br/><br/>
    <div>
        <div style="display: inline-block;">
            <h3>📑 프로젝트 플래너</h3>
            <h5>업무 일지를 작성해 프로젝트를 계획하고 포트폴리오를 만들어보세요</h5>

            <br/><br/><br/>

            <h3>🕛 스케줄러</h3>
            <h5>프로젝트 마감일 3일/7일 전에 브라우저 알림을 받아보세요</h5>

            <br/><br/><br/>

            <h3>👩‍👧‍👦 커뮤니티</h3>
            <h5>커뮤니티에서 정보를 공유하고 소통해보세요</h5>
        </div>
    </div>

    <br/><br/><br/>
</div>

<%@ include file="layout/user/footer.jsp"%>