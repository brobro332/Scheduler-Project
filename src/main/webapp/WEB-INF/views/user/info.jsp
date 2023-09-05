<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div>
<div class="container" style="display: inline-block; position: relative; left: 35%;">
  <h2 style="display: inline-block;">내프로필</h2><span class="badge bg-secondary" style="color: white;">Info</span>
  <button type="button" onclick="location.href='/user/info/profileImgForm'" class="btn btn" style="position: absolute; left: 215px; top: 0; background-color: gray; color: white; width: 200px;">프로필이미지 변경</button>
  <div class="card" style="width:400px">
  <c:choose>
  <c:when test="${empty img.profileImgName}" >
  <img class="card-img-top" src="/image/profile-spap.png" alt="Card image" style="position:relative; width: 200px; left: 100px;">
  </c:when>
  <c:otherwise>
  <br>
  <div id="image_wrapper" style="position: relative; width: 200px; height: 200px; left: 100px; border-radius: 70%; overflow: hidden;">
    <img id="profileImg" src="/api/user/info/profileImg" style="position: absolute; width: 100%; height: 100%; object-fit: cover;">
  </div>
  </c:otherwise>
  </c:choose>
    <div class="card-body">
      <h4 class="card-title" style="display: inline-block;">${info.name}</h4>

      <br/>
      <p class="card-text"><b>이메일</b> | ${info.email}</p>
      <p class="card-text"><b>휴대전화번호</b> | ${info.phone}</p>
      <p class="card-text"><b>가입일자</b> | ${info.createdAt}</p>
      <p class="card-text"><b>수정일자</b> | ${info.updatedAt}</p>
    </div>
  </div>
  <button type="button" onclick="location.href='/user/info/updateList'" class="btn btn" style="display: block; background-color: #956be8; color: white; width: 400px;">수정</button>
</div>
</div>

<script>
    let index = {
            init: function() {
                window.addEventListener("load", ()=>{
                    this.searchInfo();
                });
                $("#btn-deleteProfileImg").on("click", ()=>{
                    this.deleteProfileImg();
                });
            },

            searchInfo: function() {

            $.ajax({
                 type: "GET",
                 url: "/api/user/info",
                 dataType: "json"
            }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert("회원정보 조회에 실패하였습니다.");
            }
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
        }
    }

    index.init();
</script>
<%@ include file="../layout/user/footer.jsp"%>