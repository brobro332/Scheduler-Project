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

    </form>

    <br/>

    <button class="btn btn" id="btn-uploadProfileImg" style="background-color: #956be8; color: white; width: 200px;">등록</button>
    <button class="btn btn" id="btn-deleteProfileImg"  style="background-color: gray; color: white; width: 200px;">기본 이미지 등록</button>
    <br/><br/><br/>
</div>

<script>
    let index = {
            init: function() {
                $("#btn-deleteProfileImg").on("click", ()=>{
                    this.deleteProfileImg();
                });
                $("#btn-uploadProfileImg").on("click", ()=>{
                    this.uploadProfileImg();
                });
            },

        deleteProfileImg: function() {

         $.ajax({
                 type: "POST",
                 url: "/api/user/info/profileImg/delete",
                 dataType: "json"
            }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
            } else {
                alert(resp.message);
                location.href="/user/info";
            }
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
         },

         uploadProfileImg: function() {

                 var formData = new FormData();
                 formData.append("uploadImg", $("input[name='uploadImg']")[0].files[0]);

             $.ajax({
                     type: "PUT",
                     url: "/api/user/info/profileImg/update",
                     processData : false,
                     contentType : false,
                     data : formData
                 }).done(function(resp) {
                     if(resp.statusCode == 400 || resp.statusCode == 500){
                         alert(resp.message);
                         } else {
                         alert(resp.message);
                         location.href = "/user/info";
                     }
                 }).fail(function(error) {
                     alert(JSON.stringify(error));
                 });
         }
    };

    function setProfileImg(event) {
        var reader = new FileReader();

        reader.onload = function(event) {
        var img = document.createElement("img");
        img.setAttribute("src", event.target.result);
        document.querySelector("div#image_wrapper").setAttribute("style", "position: relative; width: 200px; height: 200px;")
        document.querySelector("div#image_container").appendChild(img).setAttribute("style", "position: absolute; top: 0; left: 0;transform: translate(50, 50); width: 100%; height: 100%; object-fit: cover; margin: auto;");
        };

        reader.readAsDataURL(event.target.files[0]);
        };

    index.init();
</script>
<%@ include file="../layout/user/footer.jsp"%>