<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List" %>
<%@ page import="vm.vo.SalesReport" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>銷售報表</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	 
	<style>
		h2 {
			color: blue !important;
			padding-left: 200px;
		}
		.table-container {
			max-width: 800px; 
			padding-left: 100px;
		}
		.bold-text {
			font-weight: bold;
		}
	</style>   	
</head>
<body>
	<%@ include file="VM_FunMenu.jsp" %>
	<br/><br/><HR>		
	<h2>銷售報表</h2><br/>	
	<div style="margin-left:25px;">
		<form action="BackendAction.do" method="get">
			<input type="hidden" name="action" value="querySalesReport"/>
			<div style="margin-left: 100px;">
				<label for="startDate">起</label> <input type="date" id="startDate"
					name="startDate"
					style="height: 25px; width: 180px; font-size: 16px; text-align: center;" />
				&nbsp; <label for="endDate" style="margin-left: 20px;">迄</label> <input
					type="date" id="endDate" name="endDate"
					style="height: 25px; width: 180px; font-size: 16px; text-align: center;" />
				<button type="submit" class="btn btn-info"
					style="margin-left: 30px; width: 60px; height: 35px">查詢</button>
			</div>
		</form>
		<br/>
		<div class="table-container">
			<table class="table table-striped table-bordered">
				<thead class="thead-dark">
					<tr>
						<th scope="col">顧客姓名</th>
						<th scope="col">購買日期</th>
						<th scope="col">飲料名稱</th>
						<th scope="col">飲料價格</th>
						<th scope="col">購買數量</th>
						<th scope="col">購買金額</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${not empty reports}">
						<c:forEach var="report" items="${reports}">
							<tr>
								<td class="bold-text">${report.customerName}</td>
								<td>${report.orderDate}</td>
								<td>${report.goodsName}</td>
								<td>${report.goodsBuyPrice}</td>
								<td>${report.buyQuantity}</td>
								<td>${report.buyAmount}</td>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${empty reports}">
						<tr>
							<td colspan="6">沒有資料</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
	<script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>
