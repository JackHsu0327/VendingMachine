<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Vending Machine BackendService</title>
    	
    <link rel="stylesheet" href="css/bootstrap.min.css">
     <style>
        .btn-custom-size {
            width: 9cm;
            height: 1.5cm;
            font-size: 1.5rem;
        }
        h1{
        	color:purple;
        	padding-left: 20px;
        }
        p {
            padding-left: 40px; 
        }
    </style>
</head>
<body>
    <h1>Vending Machine BackendService</h1>
   	<p>
        <span style="color:blue; font-size: 3rem;">
            ${sessionScope.member.customerName} 先生/小姐您好!
        </span>
        <br>
        <a href="FrontendAction.do?action=showGoods" style="color:green; font-size: 2rem; margin-right: 20px; padding-left: 20px;">前臺頁面</a>
        <a href="LoginAction.do?action=logout" style="color:red; font-size: 1.5rem;">(登出)</a>
    </p>
    <br/>
    <div style="margin-left:25px;">
        <button type="button" class="btn btn-outline-primary btn-custom-size" style="margin-left:25px;" onclick="window.location.href='BackendAction.do?action=queryGoods'">商品列表</button>
        <button type="button" class="btn btn-outline-secondary btn-custom-size" style="margin-left:25px;" onclick="window.location.href='BackendAction.do?action=goodsReplenishmentView'">商品維護作業</button>
        <button type="button" class="btn btn-outline-success btn-custom-size" style="margin-left:25px;" onclick="window.location.href='BackendAction.do?action=goodsCreateView'">商品新增上架</button>
        <button type="button" class="btn btn-outline-info btn-custom-size" style="margin-left:25px;" onclick="window.location.href='BackendAction.do?action=querySalesReport'">銷售報表</button>
    </div>
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>
