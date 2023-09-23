<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">
<div class="card" style="padding: 20px;">
  <div class="card-body">
  <input type="text" class="form-control" value="${post.id}" id="post_id" hidden>
  <input type="text" class="form-control" value="${info.name}" id="principal_name" hidden>
  <span><h1>${post.title}</h1></span>
  <div id="image_wrapper" style="position: relative; display:inline-block; width: 40px; height: 40px; border-radius: 70%; overflow: hidden;">
      <img id="profileImg" src="/api/profileImg/${post.user.email}" style="position: absolute; width: 100%; height: 100%; object-fit: cover;">
  </div>
  <div style="display:inline-block;">
  ${post.user.name}
  <p>${post.createdAt} | 조회 ${post.view_cnt} </p>
  </div>
  <hr>
  ${post.content}
  <br/>
  <b>댓글 ${countComment}</b>
  <hr>
  <c:choose>
    <c:when test="${empty comments.content}">
    </c:when>
    <c:otherwise>
    <div class="replyButton">
      <c:forEach items="${comments.content}" var="comment">
      <c:choose>
      <c:when test="${comment.delete_yn == 'N'}">
      <div class="findComment_id">
      <input type="text" class="form-control comment_id" value="${comment.id}" hidden>
      <div class="update">
      <div class="comment" style="position: relative; left:1%;">

      <div style="height:100%; position: relative; display:inline-block;">
          <div id="image_wrapper" style="position: relative; display:inline-block; width: 45px; height: 45px; border-radius: 70%; overflow: hidden;">
            <img id="profileImg" src="/api/profileImg/${comment.user.email}" style="position: absolute; right: 0%; width: 100%; height: 100%; object-fit: cover;">
          </div>
      </div>

      <div style="position:relative; display:inline-block; left: 1%;">
      <a><b>${comment.user.name}</b></a>
      <c:if test="${post.user.email == comment.user.email}">
      <a style="font-size: 12px; color: purple; position:relative; display:inline-block; border: 3px solid; border-radius: 30px;"><b>작성자</b></a>
      </c:if>
        <c:if test="${info.email == comment.user.email}">
            <div class="btn-group" style="display: inline-block;">
                <button style="position: relative; left: 1140%;" type="button" class="btn dropdown-toggle" data-toggle="dropdown">
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
      <button type="button" style="position: relative; left:91%; color: gray;" class="btn btn-outline createReply">답글쓰기</button>
      </div>
      </div>
      </div>
      </div>
      <hr>
      </c:when>
      <c:otherwise>
        댓글 작성자에 의해 삭제된 댓글입니다.
        <hr>
      </c:otherwise>
      </c:choose>
      <c:forEach items="${comment.replies}" var="reply">
                      <c:choose>
                      <c:when test="${reply.delete_yn == 'N'}">
                      <div class="findReply_id">
                      <input type="text" class="form-control reply_id" value="${reply.id}" hidden>
                      <div class="update">
                      <div class="reply" style="position: relative; left:1%;">
                      <a style="display: inline-block; color:gray;">└>&nbsp;<div style="height:100%; position: relative; display:inline-block;">
                          <div id="image_wrapper" style="position: relative; display:inline-block; width: 45px; height: 45px; border-radius: 70%; overflow: hidden;">
                            <img id="profileImg" src="/api/profileImg/${reply.user.email}" style="position: absolute; right: 0%; width: 100%; height: 100%; object-fit: cover;">
                          </div>
                      </div>

                      <div style="position:relative; display:inline-block; left: 1%;">
                      <a><b>${reply.user.name}</b></a>
                      <c:if test="${post.user.email == reply.user.email}">
                      <a style="font-size: 12px; color: purple; position:relative; display:inline-block; border: 3px solid; border-radius: 30px;"><b>작성자</b></a>
                      </c:if>
                        <c:if test="${info.email == reply.user.email}">
                            <div class="btn-group" style="display: inline-block;">
                                <button style="position: relative; left: 1102%;" type="button" class="btn dropdown-toggle" data-toggle="dropdown">
                                   ◾◾◾
                                </button>
                                <div class="dropdown-menu">
                                  <button class="dropdown-item updateReply" id="btn-replyUpdate">수정</button>
                                  <button class="dropdown-item deleteReply" id="btn-replyDelete">삭제</button>
                                </div>
                            </div>
                      </c:if>
                      <p style="display:block;">${reply.createdAt}</p>
                      </div>
                      <c:choose>
                      <c:when test="${empty reply.parentReply}">
                      <div>
                      <a style="display: flex;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<pre style="display:inline-block;">${reply.reply}</pre></a>
                      <button type="button" style="position: relative; left:91%; color: gray;" class="btn btn-outline createReplyToReply">답글쓰기</button>
                      </div>
                      </c:when>
                      <c:otherwise>
                      <div>
                      <a style="display: flex;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b style="display:inline-block;">${reply.parentReply.user.name}</b>&nbsp;&nbsp;&nbsp;<pre style="display:inline-block;">${reply.reply}</pre></a>
                      <button type="button" style="position: relative; left:91%; color: gray;" class="btn btn-outline createReplyToReply">답글쓰기</button>
                      </div>
                      </c:otherwise>
                      </c:choose>
                      </div>
                      </div>
                      </div>
                      <hr>
                      </c:when>
                      <c:otherwise>
                      대댓글 작성자에 의해 삭제된 대댓글입니다.
                      <hr>
                      </c:otherwise>
                      </c:choose>

      </c:forEach>
      </c:forEach>
      </div>
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
  <p>&nbsp;${name}</p>
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
        <button class="btn btn" onclick="location.href='/community/updatePost/${post.id}'" style="background-color: #956be8; color: white; width: 201px; float:left;">수정</button>
        <button class="btn btn" id="btn-deletePost" style="position: relative; background-color: gray; color: white; width: 201px; float:left; left: 1%;">삭제</button>
    </c:when>
</c:choose>
<button class="btn btn" onclick="location.href='/community/selectPosts'" style="background-color: gray; color: white; width: 201px; float:right;">목록</button>
</div>
</div>

<script src="/js/community.js"></script>

<%@ include file="../layout/user/footer.jsp"%>