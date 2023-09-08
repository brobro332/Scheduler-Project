<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div class="card">
  <div class="card-body">
  <input type="text" class="form-control" value="${post.id}" id="post_id" hidden>
  <span><h1>${post.title}</h1></span>
  <div id="image_wrapper" style="position: relative; display:inline-block; width: 40px; height: 40px; border-radius: 70%; overflow: hidden;">
      <img id="profileImg" src="/api/community/post/profileImg/${post.user.email}" style="position: absolute; width: 100%; height: 100%; object-fit: cover;">
  </div>
  <div style="display:inline-block;">
  ${post.user.name}
  <p>${post.createdAt} | 조회 ${post.view_cnt} </p>
  </div>
  <hr>
  ${post.content}
  <br/>
  <b>댓글 N</b>
  <hr>
  <c:choose>
    <c:when test="${empty comments.content}">
    </c:when>
    <c:otherwise>
      <c:forEach items="${comments.content}" var="comment">
      <div class="findComment_id">
      <input type="text" class="form-control comment_id" value="${comment.id}" hidden>
      <div class="update">
      <div class="comment" style="position: relative; left:1%;">

      <div style="height:100%; position: relative; display:inline-block;">
          <div id="image_wrapper" style="position: relative; display:inline-block; width: 45px; height: 45px; border-radius: 70%; overflow: hidden;">
            <img id="profileImg" src="/api/community/post/profileImg/${comment.user.email}" style="position: absolute; right: 0%; width: 100%; height: 100%; object-fit: cover;">
          </div>
      </div>

      <div style="position:relative; display:inline-block; left: 1%;">
      <a><b>${post.user.name}</b></a>
      <c:if test="${post.user.email == comment.user.email}">
      <a style="font-size: 12px; color: purple; position:relative; display:inline-block; border: 3px solid; border-radius: 30px;"><b>작성자</b></a>
            <div class="btn-group" style="display: inline-block; position:relative; left:360%;">
                <button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
                   ◾◾◾
                </button>
                <div class="dropdown-menu">
                  <button class="dropdown-item updateComment" id="btn-commentUpdate">수정</button>
                  <button class="dropdown-item deleteComment" id="btn-commentDelete">삭제</button>
                </div>
            </div>
      </c:if>
      <p style="display:block;">${comment.createdAt}</p>
      </div>
      <div>
      <a style="display: block;"><pre>${comment.comment}</pre></a>
      <a style="position: relative; left:91%;" href="#"><p style="color: gray;">답글쓰기</p></a>
      </div>
      </div>
      </div>
      </div>
      <hr>
      </c:forEach>
        <ul class="pagination" style="position:relative; left:45%; width: 20%;">
            <c:choose>
                <c:when test="${comments.first}">
                    <li class="page-item disabled"><a style="border: 0px;" class="page-link" href="?page=${comments.number-1}"><</a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a style="border: 0px;" class="page-link" href="?page=${comments.number-1}"><</a></li>
                </c:otherwise>
            </c:choose>

          <c:forEach var="i" begin="1" end="${comments.totalPages}">
              <li class="page-item"><a style="border: 0px;" class="page-link" href="?page=${i-1}">${i}</a></li>
          </c:forEach>

            <c:choose>
                <c:when test="${comments.last}">
                    <li class="page-item disabled"><a style="border: 0px;" class="page-link" href="?page=${comments.number+1}">></a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a style="border: 0px;" class="page-link" href="?page=${comments.number+1}">></a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </c:otherwise>
  </c:choose>




  <div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px;">
  <p>&nbsp;${principal.user.name}</p>
  <textarea type="text" style="border: 0px; color: gray; resize: none;" class="form-control" placeholder="댓글을 입력해보세요" id="comment"></textarea>
  <p></p>
  <button class="btn btn" id="btn-comment" style="background-color: white; color: gray; width: 100px; float:right;">등록</button>
  </br></br>
  </div>
  </div>
</div>
<div style="position:relative; height: 40px;">
<p></p>
<c:choose>
    <c:when test="${post.user.email == principal.user.email}">
        <button class="btn btn" onclick="location.href='/community/update/${post.id}'" style="background-color: #956be8; color: white; width: 201px; float:left;">수정</button>
        <button class="btn btn" id="btn-deletePost" style="position: relative; background-color: gray; color: white; width: 201px; float:left; left: 1%;">삭제</button>
    </c:when>
</c:choose>
<button class="btn btn" onclick="location.href='/community/view'" style="background-color: gray; color: white; width: 201px; float:right;">목록</button>
</div>
</div>

<script>
$(document).ready(function() {

     $(document).on('click', '#btn-updateComment', function() {
            var $comment = $(this).parents(".findComment_id");
            var comment_id = $comment.find(".comment_id").val();

            console.log(comment_id);
            updateComment(comment_id);
        });

        function updateComment(comment_id) {

            let updateComment = $('#updateComment').val();
            let post_id = $('#post_id').val();

            let data = {
                updateComment: updateComment
            };

            if(updateComment == '') {

                alert("댓글을 입력해주세요.");
                return false;
            }

            $.ajax({
                type: "PUT",
                url: "/api/community/post/comment/update/" + comment_id,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(resp) {
                if(resp.statusCode == 400 || resp.statusCode == 500){
                    alert(resp.message);
                    } else {
                    alert(resp.message);
                    location.href = "/community/view/post/" + post_id;
                }
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
        }

        $(document).on('click', '#btn-commentDelete', function() {
            var $comment = $(this).parents(".findComment_id");
            var comment_id = $comment.find(".comment_id").val();

            deleteComment(comment_id);
        });

        function deleteComment(comment_id) {
            if (confirm("삭제를 진행하시겠습니까?")) {

            let post_id = $('#post_id').val();

            $.ajax({
                type: "DELETE",
                url: "/api/community/post/comment/delete/" + comment_id,
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(resp) {
                if(resp.statusCode == 400 || resp.statusCode == 500){
                    alert(resp.message);
                    } else {
                    alert(resp.message);
                    location.href = "/community/view/post/" + post_id;
                }
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
          }
        }

    function createUpdateForm($comment) {
        var commentText = $comment.find('pre').text();
        var additionalElement = '<div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px;"><p>&nbsp;${principal.user.name}</p><textarea type="text" style="border: 0px; color: gray; resize: none;" class="form-control" id="updateComment">' + commentText + '</textarea><p></p><button class="btn btn" id="btn-cancel" style="background-color: white; color: gray; width: 100px; float:right;">취소</button><button class="btn btn" id="btn-updateComment" style="background-color: white; color: gray; width: 100px; float:right;">등록</button></br></br></div>';

        var originalContent = $comment.html();

        $('.update').not($comment).each(function() {
            var $otherComment = $(this);
            var $originalContent = $otherComment.data('originalContent');
            $otherComment.html($originalContent);
        });

        $comment.data('originalContent', originalContent);
        $comment.empty().append(additionalElement);
        }

    $(document).on('click', function(e) {
        if (!$('.btn.dropdown-toggle').is(e.target) && $('.btn.dropdown-toggle').has(e.target).length === 0) {
            $('.dropdown-menu').removeClass('show');
        }
    });

    $(document).on("click", ".dropdown-item.updateComment", function() {

        var $comment = $(this).closest('.update');

        createUpdateForm($comment);
    });

    $(document).on("click", "#btn-cancel", function() {
        var $comment = $(this).closest('.update');
        var originalContent = $comment.data('originalContent'); // 해당 댓글의 원래 내용을 가져옴

        $comment.html(originalContent);
    });
});
</script>
<script src="/js/community.js"></script>

<%@ include file="../layout/user/footer.jsp"%>