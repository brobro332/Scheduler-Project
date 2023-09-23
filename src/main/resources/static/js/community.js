if (!window.communityLoaded) {

    window.communityLoaded = true;

    $(document).ready(function() {

        $('#summernote').summernote({
            height: 500,
            minHeight: null,
            maxHeight: null,
            focus: true,
            lang: "ko-KR",
            callbacks: {
                onImageUpload: function(files, editor, welEditable){
                    for (var i = files.length - 1; i >= 0; i--) {
                        uploadSummernoteImageFile(files[i], this);
                    }
                }
           }
        });

        function uploadSummernoteImageFile(file, el) {
             data = new FormData();
             data.append("file", file);

             $.ajax({
                data: data,
                type: "POST",
                url: "/api/summernoteImg",
                cache: false,
                contentType: false,
                processData: false,
                enctype : 'multipart/form-data',
                success: function(data){
                    $(el).summernote('editor.insertImage', data);
             }
          });
        }
    });

     $(document).on('click', '#btn-updateComment', function() {
        var $comment = $(this).parents(".findComment_id");
        var comment_id = $comment.find(".comment_id").val();

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
            url: "/api/community/post/comment/" + comment_id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
                } else {
                alert(resp.message);
                location.href = "/community/selectPost/" + post_id;
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
            url: "/api/community/post/comment/" + comment_id,
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);
                } else {
                alert(resp.message);
                location.href = "/community/selectPost/" + post_id;
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
      }
    }

    function createUpdateCommentForm($comment) {
        var principal_name = $("#principal_name").val();
        var commentText = $comment.find('pre').text();

        var additionalElement = '<div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px;"><p>&nbsp;' + principal_name + '</p><textarea type="text" style="border: 0px; color: gray; resize: none;" class="form-control" id="updateComment">' + commentText + '</textarea><p></p><button class="btn btn" id="btn-cancel" style="background-color: white; color: gray; width: 100px; float:right;">취소</button><button class="btn btn" id="btn-updateComment" style="background-color: white; color: gray; width: 100px; float:right;">등록</button></br></br></div>';

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

        createUpdateCommentForm($comment);
    });

    $(document).on("click", "#btn-cancel", function() {
        var $comment = $(this).closest('.update');
        var originalContent = $comment.data('originalContent'); // 해당 댓글의 원래 내용을 가져옴

        $comment.html(originalContent);
    });

    $(document).on("click", ".createReply", function() {
        var principal_name = $("#principal_name").val();
        var commentContainer = $(this).closest('.findComment_id');
        var replyInputForm = $(this).closest('.replyButton');

        // 이미 열려있는 답글 입력 폼을 닫습니다.
        replyInputForm.find('.reply-form').remove();

        // 답글 입력 폼을 생성합니다.
        var replyInput = $('<p>&nbsp;' + principal_name + '</p><textarea type="text" style="border: 0px; color: gray; resize: none;" class="form-control comment-text" placeholder="답글을 입력해보세요"></textarea><p></p>');
        var cancelReplyButton = $('<button class="btn btn btn-cancelReply" style="background-color: white; color: gray; width: 100px; float:right;">취소</button>');
        var replyButton = $('<button class="btn btn btn-reply" style="background-color: white; color: gray; width: 100px; float:right;">등록</button></br></br>');
        var replyForm = $('<div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px;" class="reply-form"></div>');

        replyForm.append(replyInput);
        replyForm.append(cancelReplyButton);
        replyForm.append(replyButton);
        commentContainer.append(replyForm);

        replyInput.show();
        replyButton.show();

        replyButton.click(function() {
            // 답글을 서버로 전송하고 처리하는 코드를 작성하세요.
            // ...
        });
    });

    $(document).on("click", ".createReplyToReply", function() {
            var principal_name = $("#principal_name").val();
            var replyContainer  = $(this).closest('.findReply_id');
            var replyInputForm = $(this).closest('.replyButton');

            // 이미 열려있는 답글 입력 폼을 닫습니다.
            replyInputForm.find('.reply-form').remove();

            // 답글 입력 폼을 생성합니다.
            var replyInput = $('<p>&nbsp;' + principal_name + '</p><textarea type="text" style="border: 0px; color: gray; resize: none;" class="form-control comment-text" placeholder="답글을 입력해보세요"></textarea><p></p>');
            var cancelReplyButton = $('<button class="btn btn btn-cancelReply" style="background-color: white; color: gray; width: 100px; float:right;">취소</button>');
            var replyButton = $('<button class="btn btn btn-replyToReply" style="background-color: white; color: gray; width: 100px; float:right;">등록</button></br></br>');
            var replyForm = $('<div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px;" class="reply-form"></div>');

            replyForm.append(replyInput);
            replyForm.append(cancelReplyButton);
            replyForm.append(replyButton);
            replyContainer.append(replyForm);

            replyInput.show();
            replyButton.show();

            replyButton.click(function() {
                // 답글을 서버로 전송하고 처리하는 코드를 작성하세요.
                // ...
            });
        });

    $(document).on("click", ".btn-cancelReply", function() {

        var replyForm = $(this).closest('.reply-form');

        replyForm.remove();
    });

    $(document).on("click", ".btn-reply", function() {

        var $comment = $(this).parents(".findComment_id");
        var comment_id = $comment.find(".comment_id").val();
        var post_id = $('#post_id').val();
        var replyForm = $(this).closest('.reply-form');
        var reply = replyForm.find('textarea.comment-text').val();

        let data = {
                reply: reply
        };

        $.ajax({
            type: 'POST',
            url: '/api/community/post/' + post_id +'/comment/' + comment_id + '/replyToComment',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(resp) {
                alert(resp.message)
                location.href="/community/selectPost/" + post_id
            },
            error: function(error) {
                console.error(error);
            }
        });
    });

    $(document).on("click", ".btn-replyToReply", function() {

            var $comment = $(this).parents(".replyButton");
            var comment_id = $comment.find(".comment_id").val();
            var $reply = $(this).parents(".findReply_id");
            var reply_id = $reply.find(".reply_id").val();
            var post_id = $('#post_id').val();
            var replyForm = $(this).closest('.reply-form');
            var reply = replyForm.find('textarea.comment-text').val();

            let data = {
                    reply: reply,
                    parent_reply_id: reply_id
            };

            $.ajax({
                type: 'POST',
                url: '/api/community/post/' + post_id + '/comment/' + comment_id + '/replyToReply',
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(resp) {
                    alert(resp.message)
                    location.href="/community/selectPost/" + post_id
                },
                error: function(error) {
                    console.error(error);
                }
            });
        });

    $(document).on("click", ".dropdown-item.updateReply", function() {

        var $reply = $(this).closest('.update');

        createUpdateReplyForm($reply);
    });

    function createUpdateReplyForm($reply) {
        var principal_name = $("#principal_name").val();
        var commentText = $reply.find('pre').text();

        var additionalElement = '<div style="border: 1px solid #dddddd; border-radius: 5px; padding: 10px;"><p>&nbsp;' + principal_name + '</p><textarea type="text" style="border: 0px; color: gray; resize: none;" class="form-control" id="updateReply">' + commentText + '</textarea><p></p><button class="btn btn" id="btn-cancel" style="background-color: white; color: gray; width: 100px; float:right;">취소</button><button class="btn btn" id="btn-updateReply" style="background-color: white; color: gray; width: 100px; float:right;">등록</button></br></br></div>';

        var originalContent = $reply.html();

        $('.update').not($reply).each(function() {
            var $otherReply = $(this);
            var $originalContent = $otherReply.data('originalContent');
            $otherReply.html($originalContent);
        });

        $reply.data('originalContent', originalContent);
        $reply.empty().append(additionalElement);
        }

         $(document).on('click', '#btn-updateReply', function() {
            var $reply = $(this).parents(".findReply_id");
            var reply_id = $reply.find(".reply_id").val();

            updateReply(reply_id);
        });

        function updateReply(reply_id) {

            let updateReply = $('#updateReply').val();
            let post_id = $('#post_id').val();

            let data = {
                updateReply: updateReply
            };

            if(updateReply == '') {

                alert("대댓글을 입력해주세요.");
                return false;
            }

            $.ajax({
                type: "PUT",
                url: "/api/community/comment/reply/" + reply_id,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(resp) {
                if(resp.statusCode == 400 || resp.statusCode == 500){
                    alert(resp.message);
                    } else {
                    alert(resp.message);
                    location.href = "/community/selectPost/" + post_id;
                }
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
        }

    $(document).on('click', '#btn-replyDelete', function() {
            var $reply = $(this).parents(".findReply_id");
            var reply_id = $reply.find(".reply_id").val();

            deleteReply(reply_id);
        });

        function deleteReply(reply_id) {
            if (confirm("삭제를 진행하시겠습니까?")) {

            let post_id = $('#post_id').val();

            $.ajax({
                type: "DELETE",
                url: "/api/community/comment/reply/" + reply_id,
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(resp) {
                if(resp.statusCode == 400 || resp.statusCode == 500){
                    alert(resp.message);
                    } else {
                    alert(resp.message);
                    location.href = "/community/selectPost/" + post_id;
                }
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
          }
        }

    let index = {
		    init: function() {
                $("#btn-save").on("click", ()=>{
                    this.save();
                });
                $("#btn-update").on("click", ()=>{
                    this.update();
                });
                $("#btn-deletePost").on("click", ()=>{
                    this.deletePost();
                });
                $("#btn-comment").on("click", ()=>{
                    this.comment();
                });
                $("#btn-back").on("click", ()=>{
                    window.history.back();
                });
            },

            save: function() {

                var title = $("#title").val();
                var content = $("textarea[name = content]").val();

                let data = {
                        title: $("#title").val(),
                        content: $("textarea[name = content]").val(),
                };

                if(title == '') {

                    alert("제목을 입력해주세요.");
                    return false;
                }

                if(content == '') {

                    alert("내용을 입력해주세요.");
                    return false;
                }

                $.ajax({
                    type: "POST",
                    url: "/api/community/post",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                }).done(function(resp) {
                    if(resp.statusCode == 400 || resp.statusCode == 500){
                        alert(resp.message);
                        } else {
                        alert(resp.message);
                        location.href = "/community/selectPosts";
                    }
                }).fail(function(error) {
                    alert(JSON.stringify(error));
                });
            },

            update: function() {

                var title = $("#title").val();
                var content = $("textarea[name = content]").val();
                var post_id = $('#post_id').val();

                let data = {
                        title: $("#title").val(),
                        content: $("textarea[name = content]").val(),
                };

                if(title == '') {

                    alert("제목을 입력해주세요.");
                    return false;
                }

                if(content == '') {

                    alert("내용을 입력해주세요.");
                    return false;
                }

                $.ajax({
                    type: "PUT",
                    url: "/api/community/post/" + post_id,
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                }).done(function(resp) {
                    if(resp.statusCode == 400 || resp.statusCode == 500){
                        alert(resp.message);
                        } else {
                        alert(resp.message);
                        location.href = "/community/selectPost/" + post_id;
                    }
                }).fail(function(error) {
                    alert(JSON.stringify(error));
                });
            },

            deletePost: function() {
                if (confirm("삭제를 진행하시겠습니까?")) {
                var post_id = $('#post_id').val();

                $.ajax({
                    type: "DELETE",
                    url: "/api/community/post/" + post_id,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                }).done(function(resp) {
                    if(resp.statusCode == 400 || resp.statusCode == 500){
                        alert(resp.message);
                        } else {
                        alert(resp.message);
                        location.href = "/community/selectPosts";
                    }
                }).fail(function(error) {
                    alert(JSON.stringify(error));
                });
                }
            },

            comment: function() {

                let comment = $('#comment').val();
                let post_id = $('#post_id').val();

                let data = {
                        comment: comment
                };

                if(comment == '') {

                    alert("댓글을 입력해주세요.");
                    return false;
                }

                $.ajax({
                    type: "POST",
                    url: "/api/community/post/" + post_id + "/comment",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                }).done(function(resp) {
                    if(resp.statusCode == 400 || resp.statusCode == 500){
                        alert(resp.message);
                        } else {
                        alert(resp.message);
                        location.href = "/community/selectPost/" + post_id;
                    }
                }).fail(function(error) {
                    alert(JSON.stringify(error));
                });
            }
    }

    index.init();
}