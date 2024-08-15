<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Language" content="zh-tw">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>BankLogin</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script type="text/javascript">
		function goodsReplenishmentSelected(){
		    var selectedOption = document.updateGoodsForm.goodsName.selectedOptions[0];
		    var goodsID = selectedOption.getAttribute('data-goods-id');
		    var goodsPrice = selectedOption.getAttribute('data-goods-price');
		    document.updateGoodsForm.goodsID.value = goodsID;
		    document.updateGoodsForm.goodsPrice.value = goodsPrice;
	        
		}
	</script>
	<style>
		h2 {
            color: blue !important;
            padding-left: 200px;
        }
        .goods-replenishment p{
         	color: orange !important;
            margin-left: 50px;
            font-size: 2rem;
        }
	</style>
</head>
<body>
	<%@ include file="VM_FunMenu.jsp" %>
	<br/><br/><HR>		
	<h2>商品維護作業</h2><br/>
	<div class="goods-replenishment">
		<form name="updateGoodsForm" action="BackendAction.do?action=updateGoods" method="post">
			<input type="hidden" name="goodsID" id="goodsID" value=""/>
		<p>
			飲料名稱：
			 <select size="1" name="goodsName" onchange="goodsReplenishmentSelected();">
			 	<option value="">----- 請選擇 -----</option>
			 	<c:forEach items="${goods}" var="goods">
					 <option data-goods-id="${goods.goodsID}" data-goods-price="${goods.goodsPrice}" value="${goods.goodsName}">
                        ${goods.goodsName}
                    </option>
				</c:forEach>
			</select>
		</p>		
		<p>
			更改價格： 
			<input type="number" name="goodsPrice" id="goodsPrice" size="5" value="0" min="0" max="1000">
		</p>
		<p>
			補貨數量：
			<input type="number" name="goodsQuantity" size="5" value="0" min="0" max="1000">
		</p>
			<p>
				<label for="status">商品狀態：</label> <select id="status" name="status">
					<option value="" disabled selected>上/下架</option>
					<option value="1">上架</option>
					<option value="0">下架</option>
				</select>
			</p>
			<p style="margin-left:50px;">
				<button type="submit"  class="btn btn-info" style="font-size: 1.5rem; padding: 10px 20px; margin-left:25px; ">送出</button>
            </p>
	</form>
	</div>
</body>
</html>