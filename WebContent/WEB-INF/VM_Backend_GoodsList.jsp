<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>商品列表</title>
	
	 <link rel="stylesheet" href="css/bootstrap.min.css">
	 <style>
	 	h2{
        	color:blue !important;
        	padding-left: 200px;
        }
        .table-container {
			max-width: 800px; 
			padding-left: 100px;
		}
		.custom-status {
   			 color: red;
		}
		.bold-text {
			font-weight: bold;
		}
	 </style>
</head>
<body>
	<%@ include file="VM_FunMenu.jsp" %>
	<br/><br/><HR>		
    <h2>商品列表</h2>
    <c:if test="${not empty goodsList}">
     <div class="table-container">	
		<table class="table table-striped table-bordered">
			<thead class="thead-dark">
				<tr>
					<th scope="col">商品名稱</th>
					<th scope="col">價格</th>
					<th scope="col">數量</th>
					<th scope="col">狀態</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="goods" items="${goodsList}">
					<tr>
						<td class="bold-text">${goods.goodsName}</td>
						<td>${goods.goodsPrice}</td>
						<td>${goods.goodsQuantity}</td>
						<td class="custom-status">${goods.status}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	 </div>	
	</c:if>
	<c:if test="${empty goodsList}">
		<p>無商品資料</p>
	</c:if>
    
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>

