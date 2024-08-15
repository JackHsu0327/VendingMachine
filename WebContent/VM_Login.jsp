<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Language" content="zh-tw">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>VendingMachine Login</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<style>
		/* 設置背景圖片 */
		body {
			background-image: url('/VendingMachine/banner.jpg');
			background-size: cover; /* 讓背景圖片覆蓋整個頁面 */
			background-repeat: no-repeat; /* 防止圖片重複 */
			background-position: center center; /* 將背景圖片居中 */
			background-attachment: fixed; /* 背景圖片固定在頁面，不隨滾動條滾動 */
			font-family: Arial, sans-serif; /* 設置默認字體 */
			height: 100vh; /* 設置視窗高度 */
			display: flex;
			justify-content: center;
			align-items: center;
		}

		/* 表單容器 */
		.form-container {
			background: rgba(255, 255, 255, 0.8); /* 半透明的白色背景 */
			padding: 20px;
			border-radius: 10px;
			box-shadow: 0 0 15px rgba(0, 0, 0, 0.3); /* 添加陰影效果 */
			max-width: 400px; /* 設置最大寬度 */
			width: 100%; /* 設置寬度為容器寬度 */
		}

		.form-container input[type="text"],
		.form-container input[type="password"],
		.form-container input[type="submit"] {
			width: 100%; /* 使表單元素寬度為容器寬度 */
			margin-bottom: 15px;
			padding: 10px;
			font-size: 16px;
		}
	</style>
	</head>
<body>
	<div class="form-container">
		<form action="LoginAction.do" method="post">
			<input type="hidden" name="action" value="login"/>
			<div class="form-group">
				<label for="identificationNo">ID</label>
				<input type="text" class="form-control" id="identificationNo" name="identificationNo" placeholder="Your ID" required>
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
			</div>
			<div class="form-group">
				<div class="form-check">
					<input type="checkbox" class="form-check-input" id="rememberMe">
					<label class="form-check-label" for="rememberMe">Remember me</label>
				</div>
			</div>
			<button type="submit" class="btn btn-primary btn-block">Sign in</button>
		</form>
	</div>
</body>
</html>
