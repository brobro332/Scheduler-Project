<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<img src="/image/signin-spap.png" style="display: block; position: relative; width: 200px; left: 42%">

<br/>

<div class="container" style="display: inline-block; position: relative; left: 35%; width: 70%;">
<form>
    <c:choose>
    <c:when test="${empty principal.user.oauth}" >
        <div class="form-group">
            <label>비밀번호 변경</label>
            <input type="password" class="form-control" placeholder="현재 비밀번호" id="prevPassword" style="width:402px;">
            <p></p>
            <input type="password" class="form-control" placeholder="새 비밀번호" id="password" style="width:402px;">
            <p id="valid_password"></p>
            <input type="password" class="form-control" placeholder="새 비밀번호 확인" id="checkedPassword" style="width:402px;">
            <p id="valid_checkedPassword"></p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="form-group">
            <label>비밀번호 변경</label>
            <input type="password" class="form-control" placeholder="현재 비밀번호" id="prevPassword" style="width:402px;" readonly>
            <p></p>
            <input type="password" class="form-control" placeholder="새 비밀번호" id="password" style="width:402px;" readonly>
            <p id="valid_password"></p>
            <input type="password" class="form-control" placeholder="새 비밀번호 확인" id="checkedPassword" style="width:402px;" readonly>
            <p id="valid_checkedPassword"></p>
        </div>
    </c:otherwise>
    </c:choose>








    <br/>
    </form>
    <button class="btn btn" id="btn-updatePassword" style="background-color: #956be8; color: white; width: 402px;">등록</button>
    <br/><br/><br/>
</div>

<script src="/js/user.js"></script>
<%@ include file="../layout/user/footer.jsp"%>