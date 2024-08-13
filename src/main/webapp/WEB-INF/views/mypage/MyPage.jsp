<%--
/**
	Class Name:
	Author : acorn
	Description:
	Modification information
	
	수정일     수정자      수정내용
    -----   -----  -------------------------------------------
    2024. 7. 25        최초작성 
    
    D-O-M-A 개발팀
    since 2020.11.23
    Copyright (C) by KandJang All right reserved.
*/
 --%>
<%@page import="com.acorn.doma.domain.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%
String userId = null;
if(session.getAttribute("user")!=null){
    User outVO = (User) session.getAttribute("user");
    userId = outVO.getUserId();
}


%>  
 

<c:set var="CP"  value="${pageContext.request.contextPath}"  />
     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%-- favicon  --%>
<link rel="shortcut icon" href="${CP}/resources/img/favicon.ico" type="image/x-icon">

<%-- bootstrap css --%>
<link rel="stylesheet" href="${CP}/resources/css/bootstrap/bootstrap.css">

<%-- jquery --%>
<script src="${CP}/resources/js/jquery_3_7_1.js"></script>

<%-- common js --%>
<script src="${CP}/resources/js/common.js"></script> 

<%-- google Nanum+Gothic --%>
<link rel="stylesheet"  href="https://fonts.googleapis.com/css2?family=Nanum+Gothic&display=swap">

<%-- bootstrap icon --%>
<%-- <link rel="stylesheet" href="${CP}/resources/icon/font/bootstrap-icons.min.css">--%>

<%-- FontAwesome for icons --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">
<link rel="stylesheet" href="${CP}/resources/css/bootstrap/bootstrap-ege.min.css"> 
 <jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>

 <style>
.container {
    display: flex;
    gap: 20px;
    padding: 20px;
}
.left-container {
    margin-left: 200px;
    flex-basis: 30%;
    padding: 20px;
}
.right-container {
    flex-basis: 70%;
    padding: 20px;
}
.form-container {
    display: flex;
    align-items: flex-start;
}
.form-container h2 {
    margin-right: 20px;
    white-space: nowrap;
    margin-top: 40px;
}
.form-content {
    flex: 1;
}
.form-btn {
    display: flex;
    justify-content: flex-end;
}
.form-content form {
    display: flex;
    flex-direction: column;
}
form label, form input {
    margin-bottom: 10px;
}
.table-container {
    display: flex;
    align-items: flex-start;
}
.table-container h2 {
    margin-right: 20px;
    white-space: nowrap;
    margin-top: 40px;
}
.table-content {
    margin-top: 40px;
    text-align: center;
    flex: 1;
}
.form-container input[type="text"],
.form-container input[type="password"],
.form-container input[type="email"] {
    width: 300px;  
}
.password-wrapper {
    position: relative;
    display: inline-block;
}
.password-wrapper input {
    padding-right: 40px; /* Space for the icon */
}
.password-wrapper .toggle-password {
    position: absolute;
    right: 10px; /* Position the icon inside the input field */
    top: 20px;
    transform: translateY(-50%);
    cursor: pointer;
}
</style>
<script>
$(document).ready(function(){
    $('.toggle-password').on('click', function() {
        var $icon = $(this);
        var $input = $icon.prev('input');

        $input.toggleClass('active');
        if ($input.hasClass('active')) {
            $icon.attr('class', 'fa fa-eye-slash fa-lg toggle-password');
            $input.attr('type', 'text');
        } else {
            $icon.attr('class', 'fa fa-eye fa-lg toggle-password');
            $input.attr('type', 'password');
        }
    });
});
</script>
<title>내 정보</title>
<script src="${CP}/resources/js/mypage/mypageinfo.js"></script>
</head>
<body>
    <div class="container">
        <div class="left-container">
            <div class="form-container">
                <h2>내 정보</h2>
                <p>${userId}</p>
                <div class="form-content">
                    <form action="" method="">
                        <div class="info">
                            <label for="userInfo" class="form-label mt-4"></label>
                            <input type="text" class="form-control" value="<c:out value='${user.userId}'/>" id="userId" name="userId" disabled="disabled">
                            <input type="text" class="form-control" value="<c:out value='${user.userName}'/>" id="userName" name="userName">
                            <div class="password-wrapper">
                                <input type="password" class="form-control" value="<c:out value='${user.userPw}'/>" id="userPw" name="userPw">
                                <i class="fa fa-eye fa-lg toggle-password"></i>
                            </div>
                            <div class="password-wrapper">
                            <input type="password" class="form-control" name="passwordCheck" id="passwordCheck" maxlength="20" required="required" placeholder="비밀번호를 확인해주세요(8~16자리)">
                             <i class="fa fa-eye fa-lg toggle-password"></i>
                            </div>
                            <input type="button" value="비밀번호 확인" class="btn btn-outline-secondary" id="passwordDuplicateCheck">
                            <input type="text" class="form-control" value="<c:out value='${user.birth}'/>" id="birth" name="birth">
                            <input type="text" class="form-control" value="<c:out value='${user.address}'/>" id="address" name="address" disabled="disabled">
                            <input type="text" class="form-control" value="<c:out value='${user.detailAddress}'/>" id="detailAddress" name="detailAddress" disabled="disabled">
                        </div>
                        <br>
                        <div class="form-btn">
                            <input type="button" id="deleteInfo" class="btn btn-outline-danger" value="회원탈퇴">
                            &nbsp; &nbsp; 
                            <input type="reset" id="resetBtn" class="btn btn-outline-secondary" value="초기화">
                            &nbsp; &nbsp; 
                            <input type="button" id="updateInfo" class="btn btn-outline-success" value="수정">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="right-container">
            <!-- 게시글 테이블  -->
            <br>
            <br>
            <h2>게시글 <input type="button" id="MoveBoard" class="btn btn-outline-success" value="관리하기"></h2>
            <br>
            <br>
            <h2>&nbsp;&nbsp;답글 &nbsp;<input type="button" id="MoveComment" class="btn btn-outline-success" value="관리하기"></h2>
        </div>
    </div>
</body>
<%@ include file="/WEB-INF/views/template/footer.jsp" %>
</html>