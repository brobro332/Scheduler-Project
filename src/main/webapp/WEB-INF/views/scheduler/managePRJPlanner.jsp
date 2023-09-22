<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="../layout/user/header.jsp"%>

<div class="container">

    <input type="text" value="${project_id}" id="project_id" hidden>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs">
      <li class="nav-item active">
        <a class="nav-link" data-toggle="tab" href="#outline">개요</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#manageTask">업무 수행 관리</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#dailyTask">업무 일지 관리</a>
      </li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">

      <div class="tab-pane container active" id="outline">

      <br/>
      <span><h4>${project.title}</h4></span>
      <p style="color: gray;"><small>${project.startPRJ} ~ ${project.endPRJ}</small></p>

      <hr><br/>

      <div>
      <div style="display: inline-block; position: absolute;">
      <!-- 프로젝트 D-Day -->
      <div style="display: inline-block;">
        <span class="badge bg" style="background-color: #956be8; color: white;">
            <h6>프로젝트 D-DAY</h6>
        </span>
      &nbsp;
      <c:choose>
          <c:when test="${d_day > 0}">
            프로젝트 마감일까지 D-${d_day} 남았습니다.
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${d_day < 0}">
                프로젝트 마감일부터 D+${d_day} 지났습니다.
              </c:when>
              <c:otherwise>
                D-DAY 입니다.
              </c:otherwise>
            </c:choose>
          </c:otherwise>
      </c:choose>
      </div>

      <br/><br/>

      <!-- 업무 수행률 -->
      <div style="display: inline-block;">
          <div style="position: absolute; display: inline-block;">
            <span class="badge bg " style="background-color: #956be8; color: white; display: inline-block;">
                <h6>업무 수행률&nbsp;</h6>
            </span>
          </div>

        &nbsp;

          <div style="position: relative; display: inline-block; left: 35%;">
              <c:forEach items="${project.tasks}" var="task" varStatus="loop">
                  <div>
                  <c:if test="${!loop.first}">
                      <hr style="border-top: 4px dashed #d3d3d3;">
                  </c:if>
                     <h5>${task.task}</h5>
                  </div>
                  <h6><a style="display: inline-block;"> ◾ <kbd id="kbd">${task.taskPercentage}%</kbd> 하위업무 ${task.totalSubTasks}개 중 ${task.checkedSubTasks}개 완료</a></h6>
              </c:forEach>
          </div>
      </div>
      </div>

      <div style="position: relative; border-left: 4px dashed #d3d3d3; display: inline-block; height: 280px; margin-left: 100px; left: 30%"></div>

      <div style="position: absolute; display: inline-block; left: 50%;">
      <div style="text-align: center; width: 500px; height: 500px; overflow: auto;">
      <form>
        <table class="table table-hover" style="text-align:center; width: 100%; height: 100%; border-collapse: collapse;">
          <thead>
            <tr>
                <th>작성일자</th>
                <th>제목</th>
                <th>업무</th>
                <th>하위업무</th>
            </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty taskLogs.content}">
                <tr>
                    <td colspan="4">조회결과가 없습니다.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${taskLogs.content}" var="taskLog" varStatus="loop">
                    <tr>
                        <th style="font-weight: lighter;">
                            <c:set var="createdAt" value="${taskLog.createdAt}" />
                            <c:out value="${createdAt.year}-${createdAt.monthValue}-${createdAt.dayOfMonth}" />
                        </th>
                        <th style="font-weight: lighter;"><a href="/scheduler/managePRJPlanner/${project.id}/selectTaskLog/${taskLog.id}">${taskLog.title}</a></th>
                        <th style="font-weight: lighter;">${taskLog.taskCategory}</th>
                        <th style="font-weight: lighter;">${taskLog.subTaskCategory}</th>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
        </table>
      </form>
        <div style="display: inline-block;">
        <ul class="pagination" style="position:relative; width: 20%;">
                <c:choose>
                    <c:when test="${taskLogs.first}">
                        <li class="page-item disabled"><a class="page-link" href="?page=${taskLogs.number-1}"><</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="?page=${taskLogs.number-1}"><</a></li>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="1" end="${taskLogs.totalPages}">
                    <li class="page-item"><a class="page-link" href="?page=${i-1}">${i}</a></li>
              </c:forEach>

                <c:choose>
                    <c:when test="${taskLogs.last}">
                        <li class="page-item disabled"><a class="page-link" href="?page=${taskLogs.number+1}">></a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="?page=${taskLogs.number+1}">></a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
            </div>
      </div>
      </div>
      </div>

    <br/><br/>

    <button type="button" class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>

    </div>


    <div class="tab-pane container active fade" id="manageTask">

          <br/>
          <span><h4>업무 수행 관리</h4></span>
          <p style="color: gray;"><small>업무 리스트에서 수행 완료된 업무를 체크해보세요</small></p>

          <hr><br/>

          <div>
          <div style="position: absolute; display: inline-block;">
             <span class="badge bg " style="background-color: #956be8; color: white; display: inline-block;">
                    <h6>업무 리스트&nbsp;</h6>
                </span>
              </div>

            &nbsp;

              <div style="position: relative; display: inline-block; left: 10%; width: 35%">
                  <c:forEach items="${project.tasks}" var="task" varStatus="loop">
                         <div>
                         <c:if test="${!loop.first}">
                             <hr style="border-top: 4px dashed #d3d3d3;">
                         </c:if>
                         <c:choose>
                             <c:when test="${task.check_yn == 'Y'}">
                                 <label class="custom-checkbox"><h5 style="display: inline-block;">${task.task}&nbsp;&nbsp;<input type="checkbox" class="task-checkbox dynamicCheckbox" data-task-id="${task.id}" data-task-type="task" checked="checked">
                                    <span class="checkmark"></span></h5>
                                 </label>
                             </c:when>
                             <c:otherwise>
                                 <label class="custom-checkbox"><h5 style="display: inline-block;">${task.task}&nbsp;&nbsp;<input type="checkbox" class="task-checkbox dynamicCheckbox" data-task-id="${task.id}" data-task-type="task">
                                    <span class="checkmark"></span></h5>
                                 </label>
                             </c:otherwise>
                         </c:choose>
                      </div>
                  <c:forEach items="${task.subTasks}" var="subTask">
                      <div>
                      <c:choose>
                          <c:when test="${subTask.check_yn == 'Y'}">
                              <label class="custom-checkbox">&nbsp;&nbsp;&nbsp; ◾ <h6 style="display: inline-block;">${subTask.name}&nbsp;&nbsp;<input type="checkbox" class="subTask-checkbox dynamicCheckbox" data-task-id="${subTask.id}" data-uppertask-id="${task.id}" data-task-type="subTask" checked="checked">
                                 <span class="checkmark"></span></h6>
                              </label>
                          </c:when>
                          <c:otherwise>
                              <label class="custom-checkbox">&nbsp;&nbsp;&nbsp; ◾ <h6 style="display: inline-block;">${subTask.name}&nbsp;&nbsp;<input type="checkbox" class="subTask-checkbox dynamicCheckbox" data-task-id="${subTask.id}" data-uppertask-id="${task.id}" data-task-type="subTask">
                                 <span class="checkmark"></span></h6>
                              </label>
                          </c:otherwise>
                      </c:choose>
                      </div>
                  </c:forEach>
                  </c:forEach>
              </div>
          </div>

        <br/><br/>

        <button type="button" class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
        <button type="button" class="btn btn" id="btn-set" style="float: right; background-color: #956be8; color: white; width: 201px;">등록</button>

    </div>


    <div class="tab-pane container active fade" id="dailyTask">
    <div>
    <br/>
    <form method="post">
      <span><h4>업무 일지 관리</h4></span>
      <p style="color: gray;"><small>오늘 수행한 업무 일지를 작성해보세요</small></p>
      <hr><br/>

        <div>
            <select id="task" name="task">
                <option disabled selected>업무 카테고리 선택</option>
                <c:forEach items="${project.tasks}" var="task">
                    <option value="${task.id}" data-task="${task.task}">${task.task}</option>
                </c:forEach>
            </select>

            <select id="subTask" name="subTask">
              <option disabled selected>하위업무 카테고리 선택</option>
            </select>

      </div><br/>
      <input type="text" class="form-control" placeholder="제목" id="title" style="width:100%;"> <br/>
      <textarea id="summernote" name="content"></textarea> <br/>
    </form>
    <button class="btn btn" id="btn-back" style="background-color: gray; color: white; width: 201px;">뒤로가기</button>
    <button class="btn btn" id="btn-save" style="background-color: #956be8; color: white; width: 201px; float:right;">등록</button>
    </div>
    </div>


    </div>
</div>
<script src="/js/scheduler.js"></script>

<%@ include file="../layout/user/footer.jsp"%>