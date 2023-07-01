<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<img src="/image/signin-spap.png" style="display: block; position: relative; width: 200px; left: 42%">

<br/>

<div class="container" style="display: inline-block; position: relative; left: 35%; width: 70%;">
<form method="POST" enctype="multipart/form-data">
    <div>
        <div class="form_section_title">
            <label>프로필 이미지</label>

            <br>

            <label className="input-file-button" for="fileItem" style="padding: 6px 12px;background-color:#956be8;border-radius: 4px;color: white;cursor: pointer;">업로드</label>
        </div>
        <div class="form_section_content">
            <input type="file" id="fileItem" name="uploadImg" accept=".jpg, .png" onchange="setProfileImg(event);" style="display: none;">
            <div id="image_wrapper">
            <div id="image_container"></div>
            </div class="image_wrapper">
        </div>
    </div>
    <br/>
        <div class="form-group">
            <label>이메일</label>
            <input type="email" class="form-control" placeholder="이메일" id="email" value="${principal.user.email}" style="width:402px;" readOnly>
            <p id="valid_email"></p>
        </div>


        <div class="form-group">
            <label>비밀번호</label>
            <input type="password" class="form-control" placeholder="비밀번호" id="password" style="width:402px;">
            <p id="valid_password"></p>
            <input type="password" class="form-control" placeholder="확인용 비밀번호" id="checkedPassword" style="width:402px;">
            <p id="valid_checkedPassword"></p>
        </div>

        <div class="form-group">
        <label>이름</label>
            <input type="name" class="form-control" placeholder="이름" id="name" style="width:402px;">
            <p id="valid_name"></p>
        </div>

        <div class="form-group">
        <label>휴대전화번호</label>
            <input type="phone" class="form-control" placeholder="휴대전화번호" id="phone" style="width:402px;">
            <p id="valid_phone"></p>
        </div>
    </form>

    <br/>

    <button class="btn btn" id="btn-update" style="background-color: #956be8; color: white; width: 402px;">등록</button>
    <br/><br/><br/>
</div>

<script>
    function setProfileImg(event) {
    var reader = new FileReader();

    reader.onload = function(event) {
    var img = document.createElement("img");
    img.setAttribute("src", event.target.result);
    document.querySelector("div#image_wrapper").setAttribute("style", "position: relative; width: 200px; height: 200px;")
    document.querySelector("div#image_container").appendChild(img).setAttribute("style", "position: absolute; top: 0; left: 0;transform: translate(50, 50); width: 100%; height: 100%; object-fit: cover; margin: auto;");
    };

    reader.readAsDataURL(event.target.files[0]);
    }
</script>
<script src="/js/user.js"></script>
<%@ include file="../layout/user/footer.jsp"%>