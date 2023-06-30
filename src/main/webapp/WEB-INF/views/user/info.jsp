<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div>
<div class="container" style="display: inline-block; position: relative; left: 35%; width: 70%; min-height: 360px;">
  <h2 style="display: inline-block;">내프로필</h2><span class="badge bg-secondary" style="color: white;">Info</span>

  <div class="card" style="width:400px">
  <img class="card-img-top" src="/image/profile-spap.png" alt="Card image" style="position:relative; width: 200px; left: 100px;">
    <div class="card-body">
      <h4 class="card-title">${info.name}</h4>

      <br/>

      <p class="card-text"><b>이메일</b> | ${info.email}</p>
      <p class="card-text"><b>휴대전화번호</b> | ${info.phone}</p>
      <p class="card-text"><b>가입일자</b> | ${info.createdAt}</p>
      <p class="card-text"><b>수정일자</b> | ${info.updatedAt}</p>
    </div>
  </div>
  <button type="button" onclick="location.href='/user/info/updateForm'" class="btn btn" style="background-color: #956be8; color: white; width: 400px;">수정</button>
</div>
</div>

<script>
    let index = {
            init: function() {
                window.addEventListener("load", ()=>{
                    this.searchInfo();
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